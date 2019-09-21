package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
* 短信接口配置
* */
public class SmsConfig extends SmsConfigInfo {

    String balance;
    Date updateTime;

    public enum Type {
        DXW(1, "短信网"),
        CLW(2, "创蓝网"),
        WND(3, "维纳多"),
        SWLH(4, "商务领航"),
        ALDY(5, "阿里大于"),
        ALYDY(6, "阿里云大于")
        ;

        private final int value;
        private final String name;

        private Type(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Type e : Type.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum SignPlace {
        LEFT(1, "左边"),
        RIGHT(2, "右边"),
        ;

        private final int value;
        private final String name;

        private SignPlace(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (SignPlace e : SignPlace.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public static String getName(int value) {
            return map.get(value);
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

