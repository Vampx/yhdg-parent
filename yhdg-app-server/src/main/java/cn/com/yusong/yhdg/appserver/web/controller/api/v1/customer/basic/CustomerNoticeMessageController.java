package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;


import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerNoticeMessageService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_customer_basic_customer_notice_message")
@RequestMapping(value = "/api/v1/customer/basic/customer_notice_message")
public class CustomerNoticeMessageController extends ApiController {
    @Autowired
    CustomerNoticeMessageService customerNoticeMessageService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public Integer type;
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "/list")
    public RestResult list(@Valid @RequestBody ListParam param) {

        TokenCache.Data tokenData = getTokenData();

        List<Map> list = new ArrayList<Map>();
        List<CustomerNoticeMessage> customerNoticeMessageList = customerNoticeMessageService.findListByCustomerId(tokenData.customerId, param.type, param.offset, param.limit);
        for (CustomerNoticeMessage customerNoticeMessage : customerNoticeMessageList) {
            Map line = new HashMap();
            line.put("id", customerNoticeMessage.getId());
            line.put("type", param.type);
            line.put("title", customerNoticeMessage.getTitle());
            line.put("content", customerNoticeMessage.getContent());
            line.put("createTime", DateFormatUtils.format(customerNoticeMessage.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnreadListParam {
        public Integer type;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping("/unread_list.htm")
    public RestResult unreadList(@Valid @RequestBody UnreadListParam param) {
        TokenCache.Data tokenData = getTokenData();

        List<Map> list = new ArrayList<Map>();
        List<CustomerNoticeMessage> customerNoticeMessageList = customerNoticeMessageService.findUnreadListByCustomerId(tokenData.customerId, param.type, param.offset, param.limit);
        for (CustomerNoticeMessage customerNoticeMessage : customerNoticeMessageList) {
            Map line = new HashMap();
            line.put("id", customerNoticeMessage.getId());
            line.put("type", customerNoticeMessage.getType());
            line.put("title", customerNoticeMessage.getTitle());
            line.put("content", customerNoticeMessage.getContent());
            line.put("createTime", DateFormatUtils.format(customerNoticeMessage.getCreateTime(), Constant.DATE_TIME_FORMAT));
            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        public Integer type;
        public Long id;
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public RestResult info(@Valid @RequestBody InfoParam param) {

        CustomerNoticeMessage customerNoticeMessage = customerNoticeMessageService.find(param.type, param.id);
        Map line = new HashMap();
        if(customerNoticeMessage==null){
            return RestResult.result(RespCode.CODE_2.getValue(), "通知消息不存在");
        }
        line.put("title", customerNoticeMessage.getTitle());
        line.put("content", customerNoticeMessage.getContent());
        line.put("createTime", DateFormatUtils.format(customerNoticeMessage.getCreateTime(), Constant.DATE_TIME_FORMAT));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReadParam {
        public int[] ids;
    }

    @ResponseBody
    @RequestMapping("/read.htm")
    public RestResult read(@Valid @RequestBody ReadParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return customerNoticeMessageService.read(param.ids);
    }

}
