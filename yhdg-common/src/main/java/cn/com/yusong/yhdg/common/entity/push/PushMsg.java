package cn.com.yusong.yhdg.common.entity.push;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *推送消息
 */
public class PushMsg {

    public enum Type {
//        SINGLE(1, "单人推送"),
//        MANY(2, "多人推送"),;
        CUSTOMER(1,"客户"),
        USER(2,"用户"),;


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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {


        String title; //消息标题
        String content; //消息内容
        Date eventTime;//推送时间
        int sourceType; //消息类型
        int isPlay;//是否播放

        Map<String, Object> ext = new HashMap<String, Object>(); //扩展内容（预留）

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }

        public Map<String, Object> getExt() {
            return ext;
        }

        public void setExt(Map<String, Object> ext) {
            this.ext = ext;
        }

        public Date getEventTime() {
            return eventTime;
        }

        public void setEventTime(Date eventTime) {
            this.eventTime = eventTime;
        }

        public int getIsPlay() {
            return isPlay;
        }

        public void setIsPlay(int isPlay) {
            this.isPlay = isPlay;
        }
    }
}
