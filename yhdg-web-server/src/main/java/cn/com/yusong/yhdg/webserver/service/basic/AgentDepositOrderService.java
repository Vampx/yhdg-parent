package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.ChargerUtils;
import cn.com.yusong.yhdg.webserver.constant.RespCode;
import cn.com.yusong.yhdg.webserver.entity.result.DataResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentDepositOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.WeixinmpPayOrderMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import me.chanjar.weixin.mp.api.WxMpService;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentDepositOrderService extends AbstractService {
    final static Logger log = LogManager.getLogger(AgentDepositOrderService.class);
    @Autowired
    private AgentDepositOrderMapper agentDepositOrderMapper;
    @Autowired
    private OrderIdService orderIdService;
    @Autowired
    private WeixinmpPayOrderMapper weixinmpPayOrderMapper;

    public AgentDepositOrder find(String id){
        AgentDepositOrder agentDepositOrder = agentDepositOrderMapper.find(id);
        return agentDepositOrder;
    }

    public Page findPage(AgentDepositOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(agentDepositOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentDepositOrder> agentDepositOrderList = agentDepositOrderMapper.findPageResult(search);
        page.setResult(agentDepositOrderList);

        return page;
    }

    public ExtResult insert(AgentDepositOrder agentDepositOrder){
        agentDepositOrder.setCreateTime(new Date());
        agentDepositOrderMapper.insert(agentDepositOrder);
        return ExtResult.successResult();
    }

    public ExtResult update(AgentDepositOrder agentDepositOrder) {
        agentDepositOrderMapper.update(agentDepositOrder);
        return ExtResult.successResult();
    }

    public DataResult payByWeixinMp(boolean test, User user, Agent agent, AgentDepositOrder agentDepositOrder) {

        WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
        weixinmpPayOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
        weixinmpPayOrder.setPartnerId(agent.getPartnerId());
        weixinmpPayOrder.setAgentId(agent.getId());
        weixinmpPayOrder.setMobile(user.getMobile());
        weixinmpPayOrder.setCustomerId(null);
        weixinmpPayOrder.setCustomerName(null);
        weixinmpPayOrder.setMoney(agentDepositOrder.getMoney());
        weixinmpPayOrder.setSourceType(WeixinPayOrder.SourceType.DEPOSIT_ORDER_AGENT_PAY.getValue());
        weixinmpPayOrder.setSourceId(agentDepositOrder.getId());
        weixinmpPayOrder.setOrderStatus(AlipayPayOrder.Status.INIT.getValue());
        weixinmpPayOrder.setCreateTime(new Date());
        weixinmpPayOrderMapper.insert(weixinmpPayOrder);

        WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
        orderRequest.setBody("充值订单支付");
        orderRequest.setOutTradeNo(weixinmpPayOrder.getId());
        orderRequest.setTotalFee( weixinmpPayOrder.getMoney());//元转成分
        orderRequest.setSpbillCreateIp(ChargerUtils.getLocalIp());
        orderRequest.setTradeType(WxPayConstants.TradeType.NATIVE);
        orderRequest.setProductId(agentDepositOrder.getId());


        if (test) {
            Map data = new HashMap<String, String>();
            data.put("codeUrl", "http://122.224.164.50:9110/webserver/security/basic/withdraw/index.htm");
            data.put("id", agentDepositOrder.getId());
            return DataResult.dataResult(RespCode.CODE_0.getValue(), null, data);
        }

        try {
            //实例化客户端
            WxPayService wxPayService = wxPayServiceHolder.getMp(agent.getPartnerId());
            if(wxPayService == null) {
                throw new IllegalArgumentException(String.format("WxMpService is null (partnerId=%d)", agent.getPartnerId()));
            }
            log.debug("getWxMpConfigStorage: {}", wxPayService.getConfig());
            WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(orderRequest);
            if (log.isDebugEnabled()) {
                String string = ReflectionToStringBuilder.toString(result, ToStringStyle.MULTI_LINE_STYLE);
                log.debug("WxMpPrepayIdResult: {}", string);
            }


            if (result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
                Map data = new HashMap<String, String>();
                data.put("codeUrl", result.getCodeURL());
                data.put("id", agentDepositOrder.getId());

                return DataResult.dataResult(RespCode.CODE_0.getValue(), null, data);
            } else {
                return DataResult.dataResult(RespCode.CODE_2.getValue(), "获取支付二维码失败", "统一下单失败");
            }
        } catch (WxPayException e) {
            log.error("unifiedOrder 统一下单失败, {}", weixinmpPayOrder.getId());
            log.error("unifiedOrder error", e);
            return DataResult.dataResult(RespCode.CODE_2.getValue(), "获取支付二维码失败", "统一下单失败");
        }
    }
}
