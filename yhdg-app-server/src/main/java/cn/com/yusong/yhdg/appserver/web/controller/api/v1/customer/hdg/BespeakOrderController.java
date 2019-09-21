package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Controller(value = "api_v1_customer_hdg_bespeak_order")
@RequestMapping(value = "/api/v1/customer/hdg/bespeak_order")
public class BespeakOrderController extends ApiController {
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;

    /**
     * 76-预约满电
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateOrderParam {
        public String cabinetId;
        public String boxNum;
    }

    @ResponseBody
    @RequestMapping(value = "create.htm")
    public RestResult createOrder(@RequestBody CreateOrderParam param) {
        return bespeakOrderService.createOrder(param.cabinetId, param.boxNum, getTokenData().customerId);
    }


    /**
     * 78-查询我的预约订单
     */
    @ResponseBody
    @RequestMapping(value = "my.htm")
    public RestResult my() {
        Customer customer = customerService.find(getTokenData().customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不出存在");
        }

        BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(customer.getId());

        Map line = new HashMap();
        if(bespeakOrder != null){
            line.put("id", bespeakOrder.getId());
            line.put("cabinetId", bespeakOrder.getBespeakCabinetId());
            line.put("cabinetName", bespeakOrder.getBespeakCabinetName());
            line.put("boxNum", bespeakOrder.getBespeakBoxNum());
            line.put("remainTime", TimeUnit.MILLISECONDS.toSeconds(bespeakOrder.getExpireTime().getTime() - System.currentTimeMillis()));//秒

            Cabinet cabinet = cabinetService.find(bespeakOrder.getBespeakCabinetId());
            line.put("lng", cabinet.getLng());
            line.put("lat", cabinet.getLat());
            line.put("address", cabinet.getAddress());

            Battery battery = batteryService.find(bespeakOrder.getBespeakBatteryId());
            if(battery != null){
                line.put("volume", battery.getVolume());
            }else{
                line.put("volume", null);
            }

            String count = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BESPEAK_MAX_CANCEL.getValue(), cabinet.getAgentId());
            line.put("bespeakMaxCancel", count);
            line.put("alreadyCancel", bespeakOrderService.findFailCountForToday(customer.getId()));

        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", line);
    }

    /**
     * 77-取消预约满电
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CancelParam {
        public String orderId;
    }

    @ResponseBody
    @RequestMapping(value = "cancel.htm")
    public RestResult cancel(@RequestBody CancelParam param) {
        return bespeakOrderService.cancelOrder(param.orderId, getTokenData().customerId);
    }
}
