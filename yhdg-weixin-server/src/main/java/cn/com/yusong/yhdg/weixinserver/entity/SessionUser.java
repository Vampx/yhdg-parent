package cn.com.yusong.yhdg.weixinserver.entity;

import org.apache.commons.lang.StringUtils;

public class SessionUser implements java.io.Serializable {

    int appId;
    String openId;
    String secondOpenId;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSecondOpenId() {
        return secondOpenId;
    }

    public void setSecondOpenId(String secondOpenId) {
        this.secondOpenId = secondOpenId;
    }

    public String getUnionOpenId() {
        return appId + ":" + openId + ":" +StringUtils.trimToEmpty(secondOpenId);
    }
}