package cn.com.yusong.yhdg.serviceserver.tool.push;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class PushTarget {
    public List<String> aliasList = new ArrayList<String>();
    public List<String> registrationIdList = new ArrayList<String>();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
