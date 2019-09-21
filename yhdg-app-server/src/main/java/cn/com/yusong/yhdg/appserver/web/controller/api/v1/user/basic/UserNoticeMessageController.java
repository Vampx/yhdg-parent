package cn.com.yusong.yhdg.appserver.web.controller.api.v1.user.basic;


import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.UserNoticeMessageService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
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

@Controller("api_v1_user_basic_user_notice_message")
@RequestMapping(value = "/api/v1/user/basic/user_notice_message")
public class UserNoticeMessageController extends ApiController {
    @Autowired
    UserNoticeMessageService userNoticeMessageService;

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
        List<UserNoticeMessage> userNoticeMessageList = userNoticeMessageService.findListByUserId(
                tokenData.userId, param.type, param.offset, param.limit);
        if (userNoticeMessageList != null) {
            for (UserNoticeMessage userNoticeMessage : userNoticeMessageList) {
                Map line = new HashMap();
                line.put("id", userNoticeMessage.getId());
                line.put("type", userNoticeMessage.getType());
                line.put("title", userNoticeMessage.getTitle());
                line.put("content", userNoticeMessage.getContent());
                line.put("createTime", DateFormatUtils.format(userNoticeMessage.getCreateTime(), Constant.DATE_TIME_FORMAT));
                list.add(line);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

}
