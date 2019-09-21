package cn.com.yusong.yhdg.common.entity;

import org.apache.commons.lang.StringUtils;

public class UnionOpenId {
    public int appId;
    public String openId;
    public String secondOpenId;

    public static UnionOpenId newInstance(String openId) {
        String[] value = StringUtils.split(openId, ":");
        if(value.length == 2) {
            value = new String[] {value[0], value[1], null};
        }

        UnionOpenId unionOpenId = new UnionOpenId();
        unionOpenId.appId = Integer.parseInt(value[0]);
        unionOpenId.openId = value[1];
        unionOpenId.secondOpenId = value[2];

        return unionOpenId;
    }
}
