package cn.com.yusong.yhdg.appserver.web.controller.api.v1.cabinetapp.hdg;


import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.exception.OrderStatusExpireException;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller("api_v1_cabinet_app_hdg_battery_order")
@RequestMapping(value = "/api/v1/cabinet_app/hdg/battery_order")
public class BatteryOrderController extends ApiController {

    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    BespeakOrderService bespeakOrderService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class queryPayQrcodeParam {
        public Integer payType;
        public String orderId;
        public long couponTicketId;
        public long customerId;
    }

    @ResponseBody
    @RequestMapping(value = "/query_pay_qrcode")
    public RestResult queryPayQrcode(@Valid @RequestBody queryPayQrcodeParam param) {
        if (param.payType == 1) {
            return batteryOrderService.qrcodeByWeixin(false, param.orderId, param.customerId, param.couponTicketId);
        } else {
            return batteryOrderService.qrcodeByAlipay(false, param.orderId, param.customerId, param.couponTicketId);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayByBalanceParam {
        public String orderId;
        public long couponTicketId;
        public long customerId;
    }

    @ResponseBody
    @RequestMapping(value = "/pay_by_balance")
    public RestResult payByBalance(@Valid @RequestBody PayByBalanceParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        try {
            RestResult restResult = batteryOrderService.payByBalance(param.orderId, param.customerId, param.couponTicketId);
            if (restResult.getCode() != 0) {
                return restResult;
            }
            Cabinet cabinet = cabinetService.find(tokenData.cabinetId);
            if (cabinet == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
            }
            Customer customer = customerService.find(param.customerId);
            if (customer == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
            }
            Integer batteryType = null;
            //查询白名单
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(cabinet.getAgentId(), customer.getId());
            if(exchangeWhiteList != null){
                batteryType = exchangeWhiteList.getBatteryType();
            }else{
                CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
                if(customerExchangeInfo != null){
                    batteryType = customerExchangeInfo.getBatteryType();
                }
            }
            if (batteryType == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户电池类型不存在");
            }

            //查询是否有预约订单
            String bespeakBoxNum = null;
            BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(param.customerId);
            if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(tokenData.cabinetId)){
                bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
            }

            CabinetBox cabinetBox = cabinetBoxService.findOneFull(tokenData.cabinetId, batteryType, bespeakBoxNum);
            if (cabinetBox == null) {
                int fullCount = cabinetBoxService.findFullCount(tokenData.cabinetId);
                String errorMessage = "没有符合类型的已充满电池";
                if (fullCount != 0) {
                    errorMessage = "扫码者与当前柜子不匹配";
                }
                return RestResult.result(RespCode.CODE_2.getValue(), errorMessage);
            }

            //查询柜子情况
            Cabinet subcabinet = cabinetService.find(cabinetBox.getCabinetId());
            if(subcabinet == null ){
                return RestResult.result(RespCode.CODE_2.getValue(), "换电柜异常");
            }

            Battery battery = batteryService.find(cabinetBox.getBatteryId());
            if(battery == null){
                return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在:"+cabinetBox.getBatteryId());
            }

            if(bespeakBoxNum == null || !bespeakBoxNum.equals(cabinetBox.getBoxNum())){
                int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
                if(effect == 0) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
                }
            }

            Map map = new HashMap();
            map.put("cabinetId", tokenData.cabinetId);
            map.put("subcabinetId", cabinetBox.getCabinetId());
            map.put("cabinetName", cabinet.getCabinetName());
            map.put("boxNum", cabinetBox.getBoxNum());
            map.put("batteryId", battery.getId());
            map.put("volume", battery.getVolume());
            int openSuccess = 0;

            //发送开箱指令
            ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), subcabinet.getSubtype());
            //如果协议发送成功
            if (result.getCode() == RespCode.CODE_0.getValue()) {
                openSuccess = 1;
                BatteryOrder order =  batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);
                map.put("orderId", order.getId());

            } else {
                openSuccess = 0;
                if(result.getSerial() == -1) {
                    cabinetBoxService.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
                }  else {
                    CabinetBox box = cabinetBoxService.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                    if (box.getIsOpen() != ConstEnum.Flag.TRUE.getValue()) {
                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                cabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.CUSTOMER,
                                String.format("客户%s %s, 换电订单打开满箱失败", customer.getFullname(), customer.getMobile()),
                                customer.getFullname());
                    }else{
                        openSuccess = 1;
                    }
                    BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);
                    map.put("orderId", order.getId());
                }
            }

            map.put("openSuccess", openSuccess);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } catch (OrderStatusExpireException e) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单状态过期");
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class createByBalanceParam {
        public String orderId;
        public long customerId;
    }

    @ResponseBody
    @RequestMapping(value = "/query_pay_result")
    public RestResult queryPayResult(@Valid @RequestBody createByBalanceParam param) throws InterruptedException {
        BatteryOrder batteryOrder = batteryOrderService.find(param.orderId);
        Map map = new HashMap();
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
        if (batteryOrder.getCustomerId() != param.customerId) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单与客户不对应");
        }
        if (batteryOrder.getOrderStatus() != BatteryOrder.OrderStatus.PAY.getValue()) {
            map.put("result", 0);
        } else {
            map.put("result", 1);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }
}
