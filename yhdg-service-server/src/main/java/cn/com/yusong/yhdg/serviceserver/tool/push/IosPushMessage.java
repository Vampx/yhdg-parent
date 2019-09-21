package cn.com.yusong.yhdg.serviceserver.tool.push;

import java.util.HashMap;
import java.util.Map;

/**
 * Ios推送消息
 */
public class IosPushMessage extends PushTarget {
    public String title;
    public String content;
    public String contentType;
    public Map<String, String> extras = new HashMap<String, String>();
}
