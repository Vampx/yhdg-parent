package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.FeedbackService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Feedback;
import org.apache.commons.lang.StringUtils;
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

@Controller("api_v1_customer_basic_feedback")
@RequestMapping(value = "/api/v1/customer/basic/feedback")
public class FeedbackController extends ApiController {
    final static Logger log = LogManager.getLogger(FeedbackController.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    FeedbackService feedbackService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        @NotBlank(message = "内容不能为空")
        public String content;
        public String photoPath;
    }
    @ResponseBody
    @RequestMapping(value = "/create")
    public RestResult create(@Valid @RequestBody CreateParam param) {
        TokenCache.Data data = getTokenData();
        Customer customer = customerService.find(data.customerId);
        if (customer == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }
        if (param.content.length() > 256){
            return RestResult.result(RespCode.CODE_2.getValue(), "内容超长");
        }

        Feedback feedback = new Feedback();
        feedback.setPartnerId(customer.getPartnerId());
        feedback.setCustomerId(customer.getId());
        feedback.setCustomerMobile(customer.getMobile());
        feedback.setCustomerFullname(customer.getFullname());
        feedback.setPhotoPath(param.photoPath);
        feedback.setContent(param.content);
        feedback.setCreateTime(new Date());
        RestResult result = feedbackService.insert(feedback);
        return result;
    }
}
