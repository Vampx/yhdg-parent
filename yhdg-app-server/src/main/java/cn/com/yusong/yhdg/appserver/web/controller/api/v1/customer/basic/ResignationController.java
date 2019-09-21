package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.FeedbackService;
import cn.com.yusong.yhdg.appserver.service.basic.ResignationService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import cn.com.yusong.yhdg.common.domain.basic.Resignation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.Date;

@Controller("api_v1_customer_basic_resignation")
@RequestMapping(value = "/api/v1/customer/basic/resignation")
public class ResignationController extends ApiController {

    @Autowired
    CustomerService customerService;
    @Autowired
    ResignationService resignationService;

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list() {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return resignationService.findList(customerId);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        public String content;
    }

    @ResponseBody
    @RequestMapping(value = "/create")
    public RestResult create(@Valid @RequestBody CreateParam param) {
        TokenCache.Data data = getTokenData();
        Customer customer = customerService.find(data.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }


        if (param.content.length() > 256) {
            return RestResult.result(RespCode.CODE_2.getValue(), "内容超长");
        }

        Resignation resignation = new Resignation();
        resignation.setCustomerId(customer.getId());
        resignation.setCustomerMobile(customer.getMobile());
        resignation.setCustomerFullname(customer.getFullname());
        resignation.setState(Resignation.State.AUDIT.getValue());
        resignation.setContent(param.content);
        resignation.setCreateTime(new Date());
        return resignationService.insert(resignation);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CancelParam {
        public Long id;
    }

    @ResponseBody
    @RequestMapping(value = "/cancel")
    public RestResult cancel(@Valid @RequestBody CancelParam param) {
        TokenCache.Data data = getTokenData();
        Customer customer = customerService.find(data.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }
        return resignationService.cancel(data.customerId, param.id);
    }
}
