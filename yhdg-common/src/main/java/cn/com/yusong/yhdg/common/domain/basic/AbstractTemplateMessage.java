package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class AbstractTemplateMessage extends LongIdEntity {
    String sourceId;
    Integer type;
    String variable; //变量
    Integer status;

    public static final String MESSAGE_SALT = "cn.com.yusong.yhdg.common.domain.basic.AbstractTemplateMessage";

    public static String buildSign(int appId, String openId) {
        Map<String, String> source = new HashMap<String, String>();
        source.put("openId", openId);
        source.put("appId", String.valueOf(appId));
        source.put("MESSAGE_SALT", MESSAGE_SALT);

        String sign = AppUtils.sign(source, null, AppUtils.SignType.MD5);
        return sign;
    }

    public static String periodOrderExpireUrl(int appId, String openId, String secondOpenId, String weixinUrl) {
//        String url = String.format("/v_vee/myPage/recharge?appId=%s&openId=%s", appId, secondOpenId);
        String url = String.format(weixinUrl+"/v_vee/myPage/recharge");
        String URL = String.format("/message_detail/index.htm?appId=%s&openId=%s&secondOpenId=%s&url=%s", appId, openId, secondOpenId, url);
        return URL;
    }

    public static String couponTicketUrl(int agentId, int appId, int category, String openId, String secondOpenId, String weixinUrl) {

        String url = String.format("%s/v_vee/scan/pushCoupon?agentId=%s&category=%d", weixinUrl, agentId, category);
        String URL = null;
        try {
            URL = String.format("/message_detail/index.htm?appId=%s&openId=%s&secondOpenId=%s&url=%s",  appId, openId, secondOpenId, URLEncoder.encode(url,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL;
    }


    public static String batteryUrl(int appId, String openId, String secondOpenId, String weixinUrl) {
//        String url = String.format("/v_vee/myPage/batteryMSG?appId=%s&openId=%s", appId, secondOpenId);
        String url = String.format(weixinUrl+"/v_vee/myPage/batteryMSG");
        String URL = String.format("/message_detail/index.htm?appId=%s&openId=%s&secondOpenId=%s&url=%s", appId, openId, secondOpenId, url);
        return URL;
    }

    public static String batteryAgentUrl(Long userId, int agentId, int appId, String openId, String secondOpenId, String weixinUrl, String batteryId) {
//        String url = String.format("/v_vee/myPage/batteryMSG?appId=%s&openId=%s", appId, secondOpenId);
        String url = String.format("%s/v_vee/admin/batteryManager/AdminbatteryDetail?agentId=%s&userId=%d&batteryId=%s", weixinUrl, agentId, userId, batteryId);
        String URL = null;
        try {
            URL = String.format("/message_detail/index.htm?appId=%s&openId=%s&secondOpenId=%s&url=%s",  appId, openId, secondOpenId, URLEncoder.encode(url,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL;
    }

    public static String periodOrderExpireAgentUrl(Long userId, int agentId, int appId, String openId, String secondOpenId, String weixinUrl) {
//        String url = String.format("/v_vee/myPage/recharge?appId=%s&openId=%s", appId, secondOpenId);
        String url = String.format("%s/agent/adminInfo/renewNote?agentId=%s&userId=%d", weixinUrl, agentId, userId);
        String URL = null;
        try {
            URL = String.format("/message_detail/index.htm?appId=%s&openId=%s&secondOpenId=%s&url=%s",  appId, openId, secondOpenId, URLEncoder.encode(url,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL;
    }

    public static String batteryListAgentUrl(Long userId, int agentId, int appId, String openId, String secondOpenId, String weixinUrl, int faultType) {
//        String url = String.format("/v_vee/myPage/batteryMSG?appId=%s&openId=%s", appId, secondOpenId);
        String url = String.format("%s/agent/adminInfo/infoDetail?agentId=%s&userId=%d&faultType=%s", weixinUrl, agentId, userId, faultType);
        String URL = null;
        try {
            URL = String.format("/message_detail/index.htm?appId=%s&openId=%s&secondOpenId=%s&url=%s",  appId, openId, secondOpenId, URLEncoder.encode(url,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL;
    }

    public static String batteryOverTimeUrl(Long userId, int agentId, int appId, String openId, String secondOpenId, String weixinUrl) {
//        String url = String.format("/v_vee/myPage/batteryMSG?appId=%s&openId=%s", appId, secondOpenId);
        String url = String.format("%s/agent/adminInfo/overTimeDetail?agentId=%s&userId=%d", weixinUrl, agentId, userId);
        String URL = null;
        try {
            URL = String.format("/message_detail/index.htm?appId=%s&openId=%s&secondOpenId=%s&url=%s",  appId, openId, secondOpenId, URLEncoder.encode(url,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL;
    }

    public static String unbindBatteryOutOverTimeUrl(Long userId, int agentId, int appId, String openId, String secondOpenId, String weixinUrl) {
//        String url = String.format("/v_vee/myPage/batteryMSG?appId=%s&openId=%s", appId, secondOpenId);
        String url = String.format("%s/agent/adminInfo/orderOverTime?agentId=%s&userId=%d", weixinUrl, agentId, userId);
        String URL = null;
        try {
            URL = String.format("/message_detail/index.htm?appId=%s&openId=%s&secondOpenId=%s&url=%s",  appId, openId, secondOpenId, URLEncoder.encode(url,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return URL;
    }
}
