package cn.com.yusong.yhdg.appserver.web.controller.api.v1.independent_customer.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
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
import java.util.*;

@Controller("api_v1_independent_customer_basic_customer")
@RequestMapping(value = "/api/v1/independent_customer/basic/customer")
public class CustomerController extends ApiController {

    static final Logger log = LogManager.getLogger(CustomerController.class);
    @Autowired
    CustomerService customerService;
    @Autowired
    ZhizuCustomerService zhizuCustomerService;
    @Autowired
    AgentService agentService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class syncCustomerInfoParam {
        public String customerName;
        public String customerMobile;
        public String idCard;
        public Integer isActive;
        public Integer foregift;
        public Integer agentId;
        public Integer batteryType;

        public String realMobile;
        public String realIdCard;
        public Long expireTime;
        public Integer cityId;

        public Long time;
        public String sign;
    }


    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/sync_customer_info")
    public RestResult syncCustomerInfo(@Valid @RequestBody syncCustomerInfoParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        Agent agent = agentService.find(param.agentId);
        if(agent == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        if(agent.getIsIndependent() == null || agent.getIsIndependent() == ConstEnum.Flag.FALSE.getValue()){
            return RestResult.result(RespCode.CODE_2.getValue(), "不是独立用户运营商");
        }

        Customer customer = customerService.findByMobile(Constant.ZHIZU_PARTNER_ID, param.customerMobile);
        if (customer != null) {
            customerService.updateIndependentCustomer(customer.getId(),
                    param.customerName,
                    param.customerMobile,
                    param.idCard,
                    param.isActive,
                    param.foregift,
                    param.agentId,
                    param.batteryType+"");
        } else {
            customer = new Customer();
            customer.setPartnerId(Constant.ZHIZU_PARTNER_ID);
            customer.setAgentId(param.agentId);
            customer.setBalance(0);    //余额默认为0
            customer.setGiftBalance(0);
            customer.setIsActive(param.isActive);
            customer.setMobile(param.customerMobile);
            customer.setRegisterType(Customer.RegisterType.APP.getValue());
            customer.setFullname(param.customerName);
            customer.setMobile(param.customerMobile);
            customer.setIdCard(param.idCard);
            customer.setIcCard(param.batteryType+"");//电池类型保存到卡号中
            customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
            customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
            customer.setAuthStatus(Customer.AuthStatus.AUDIT_PASS.getValue());
            customer.setCreateTime(new Date());
            if (customerService.insert(customer) == 0) {
                return RestResult.result(RespCode.CODE_2.getValue(), "账号异常");
            }
        }

        //保存智租用户
        if(customer.getAgentId() == Constant.ZHIZU_AGENT_ID){
            zhizuCustomerService.save(customer,param.batteryType, param.realMobile, param.realIdCard, param.expireTime, param.cityId);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

}
