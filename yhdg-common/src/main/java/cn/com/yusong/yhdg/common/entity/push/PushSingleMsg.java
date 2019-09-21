package cn.com.yusong.yhdg.common.entity.push;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *推送消息
 */
public class PushSingleMsg {

    public enum Type {
        SINGLE(1, "单人推送"),
        MANY(2, "多人推送"),
        WHOLE(3, "APP推送"),;

        private final int value;
        private final String name;

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Type s : Type.values()) {
                map.put(s.getValue(), s.getName());
            }
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }

    int type;//消息类型
    Data data;//消息内容

    public static class Data {
        String title; //消息标题
        String text; //消息内容
        String logo; //图标名称
        String logoUrl; //图标地址
        Integer TransmissionType;//透传类型 1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
        String TransmissionContent;//透出内容
        int sourceType; //消息类型
        Date eventTime;//推送时间
        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }

        public Integer getTransmissionType() {
            return TransmissionType;
        }

        public void setTransmissionType(Integer transmissionType) {
            TransmissionType = transmissionType;
        }

        public String getTransmissionContent() {
            return TransmissionContent;
        }

        public void setTransmissionContent(String transmissionContent) {
            TransmissionContent = transmissionContent;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }
        public Date getEventTime() {
            return eventTime;
        }

        public void setEventTime(Date eventTime) {
            this.eventTime = eventTime;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
