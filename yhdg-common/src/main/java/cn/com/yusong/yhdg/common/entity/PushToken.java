package cn.com.yusong.yhdg.common.entity;

public class PushToken implements java.io.Serializable {
    Integer pushType;
    String pushToken;

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }
}
