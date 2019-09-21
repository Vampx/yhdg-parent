package cn.com.yusong.yhdg.serviceserver.tool.push;

import java.util.HashMap;
import java.util.Map;

public class AndroidPushNotification extends PushTarget {

    public String title;
    public String content;
    public int alterType;
    public Map<String, String> extras = new HashMap<String, String>();
}
