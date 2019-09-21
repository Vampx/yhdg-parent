package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;


import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller("api_v1_customer_basic_public_notice")
@RequestMapping(value = "/api/v1/customer/basic/public_notice")
public class PublicNoticeController extends ApiController {
    @Autowired
    PublicNoticeService publicNoticeService;
    @Autowired
    ReadNoticeCustomerService readNoticeCustomerService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        return publicNoticeService.findList(customerId, param.offset, param.limit);

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

        return readNoticeCustomerService.insert(param.ids, customerId);
    }
}
