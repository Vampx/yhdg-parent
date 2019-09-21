package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.AppConfig;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller("api_v1_customer_basic_customer_deposit_order")
@RequestMapping(value = "/api/v1/customer/basic/customer_deposit_order")
public class CustomerDepositOrderController extends ApiController {

    static final Logger log = LogManager.getLogger(CustomerDepositOrderController.class);

    @Autowired
    AgentService agentService;
    @Autowired
    CustomerService customerService;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    CustomerDepositOrderService customerDepositOrderService;
    @Autowired
    CustomerDepositGiftService customerDepositGiftService;
    @Autowired
    MobileMessageTemplateService mobileMessageTemplateService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AppConfig appConfig;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping("list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        long customerId = getTokenData().customerId;
        return customerDepositOrderService.findList(customerId, param.offset, param.limit);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByAlipayParam {
        public int money;
    }

    /**
     * 用户支付宝充值
     */
    @ResponseBody
    @RequestMapping(value = "/create_by_alipay")
    public RestResult createByAlipay(@Valid @RequestBody CreateByAlipayParam param) {
        TokenCache.Data tokenData = getTokenData();
        if (param.money <= 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "充值金额有误");
        }
        return customerDepositOrderService.payByAlipay(param.money, tokenData.customerId, false);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateByWeixinParam {
        public int money;
    }

    /**
     * 用户充值(微信)
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create_by_weixin")
    public RestResult createByWeixin(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        if (param.money <= 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "充值金额有误");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

       // List<CustomerDepositGift> customerDepositGiftList = customerDepositGiftService.findAll(customer.getAppId());
       // int gift = CustomerDepositGift.gift(customerDepositGiftList, param.money);

        CustomerDepositOrder customerDepositOrder = new CustomerDepositOrder();
        customerDepositOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER));
        customerDepositOrder.setPartnerId(customer.getPartnerId());
        customerDepositOrder.setMoney(param.money);
        customerDepositOrder.setGift(0);
        customerDepositOrder.setCustomerId(customerId);
        customerDepositOrder.setCustomerMobile(customer.getMobile());
        customerDepositOrder.setCustomerFullname(customer.getFullname());
        customerDepositOrder.setStatus(CustomerDepositOrder.Status.NOT.getValue());
        customerDepositOrder.setCreateTime(new Date());
        customerDepositOrder.setPayType(ConstEnum.PayType.WEIXIN.getValue());
        customerDepositOrder.setClientType(ConstEnum.ClientType.APP.getValue());
        customerDepositOrderService.insert(customerDepositOrder);

        return customerDepositOrderService.payByWeixin(customer.getPartnerId(), customerDepositOrder.getId(), param.money, tokenData.customerId);
    }

    /**
     * 用户充值(公众号)
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create_by_weixin_mp")
    public RestResult createByWeixinMp(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        if (param.money <= 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "充值金额有误");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }


        CustomerDepositOrder customerDepositOrder = new CustomerDepositOrder();
        customerDepositOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER));
        customerDepositOrder.setPartnerId(customer.getPartnerId());
        customerDepositOrder.setMoney(param.money);
        customerDepositOrder.setGift(0);
        customerDepositOrder.setCustomerId(customerId);
        customerDepositOrder.setCustomerMobile(customer.getMobile());
        customerDepositOrder.setCustomerFullname(customer.getFullname());
        customerDepositOrder.setStatus(CustomerDepositOrder.Status.NOT.getValue());
        customerDepositOrder.setCreateTime(new Date());
        customerDepositOrder.setPayType(ConstEnum.PayType.WEIXIN_MP.getValue());
        customerDepositOrder.setClientType(ConstEnum.ClientType.MP.getValue());
        customerDepositOrderService.insert(customerDepositOrder);

        return customerDepositOrderService.payByWeixinMp(false, customer.getPartnerId(), customerDepositOrder.getId(), param.money, tokenData.customerId, customer.getMpOpenId());
    }

    /**
     * 用户充值(小程序)
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create_by_weixin_ma")
    public RestResult createByWeixinMa(@Valid @RequestBody CreateByWeixinParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        if (param.money <= 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "充值金额有误");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }


        CustomerDepositOrder customerDepositOrder = new CustomerDepositOrder();
        customerDepositOrder.setId(orderIdService.newOrderId(OrderId.OrderIdType.CUSTOMER_DEPOSIT_ORDER));
        customerDepositOrder.setPartnerId(customer.getPartnerId());
        customerDepositOrder.setMoney(param.money);
        customerDepositOrder.setGift(0);
        customerDepositOrder.setCustomerId(customerId);
        customerDepositOrder.setCustomerMobile(customer.getMobile());
        customerDepositOrder.setCustomerFullname(customer.getFullname());
        customerDepositOrder.setStatus(CustomerDepositOrder.Status.NOT.getValue());
        customerDepositOrder.setCreateTime(new Date());
        customerDepositOrder.setPayType(ConstEnum.PayType.WEIXIN_MA.getValue());
        customerDepositOrder.setClientType(ConstEnum.ClientType.MA.getValue());
        customerDepositOrderService.insert(customerDepositOrder);

        return customerDepositOrderService.payByWeixinMa(false, customer.getPartnerId(), customerDepositOrder.getId(), param.money, tokenData.customerId, customer.getMaOpenId());
    }

    public static class CreateByAlipayfwParam {
        public int money;
    }

    /**
     * 用户充值(支付宝 生活号)
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create_by_alipay_fw")
    public RestResult createAlipayFw(@Valid @RequestBody CreateByAlipayfwParam param) throws IOException {
        TokenCache.Data tokenData = getTokenData();
        if (param.money <= 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "充值金额有误");
        }
        return customerDepositOrderService.payByAlipayfw(param.money, tokenData.customerId, false);

    }
}
