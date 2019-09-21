package cn.com.yusong.yhdg.serviceserver.tool.push;

import java.util.HashMap;
import java.util.Map;

public class IosPushNotification extends PushTarget {
    public String title;
    public String content;
    public Map<String, String> extras = new HashMap<String, String>();
}
