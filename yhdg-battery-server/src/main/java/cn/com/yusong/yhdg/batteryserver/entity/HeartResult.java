package cn.com.yusong.yhdg.batteryserver.entity;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.HashMap;
import java.util.Map;

public class HeartResult {

    public String id;
    public String code;
    public String version;
    public Map json = new HashMap();
}