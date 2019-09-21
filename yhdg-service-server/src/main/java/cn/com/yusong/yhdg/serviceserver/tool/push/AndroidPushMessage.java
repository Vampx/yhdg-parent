package cn.com.yusong.yhdg.serviceserver.tool.push;

import cn.com.yusong.yhdg.common.constant.ConstEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Andriod推送消息
 */
public class AndroidPushMessage extends PushTarget {
    public String title;
    public String content;
    public String contentType;
    public Map<String, String> extras = new HashMap<String, String>();
}
