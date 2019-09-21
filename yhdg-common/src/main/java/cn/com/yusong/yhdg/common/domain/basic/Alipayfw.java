package cn.com.yusong.yhdg.common.domain.basic;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Alipayfw extends IntIdEntity {

    public enum PageType {
        DEFAULT(1, "默认"), CUSTOM(2, "定制");

        private final int value;
        private final String name;

        PageType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (PageType e : PageType.values()) {
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

    public enum AuthType {
        AUTO(1, "自动认证"), MANUAL(2, "人工认证");

        private final int value;
        private final String name;

        AuthType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (AuthType e : AuthType.values()) {
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

    Integer partnerId;
    @Transient
    String partnerName;
    String appName;
    String appId;
    String pubKey;
    String priKey;
    String aliKey;
    Integer userinfoVersion;
    String subscribeUrl;
    String systemTel;
    String logoPath;
    Integer pageType;
    Integer authType;
    Date createTime;
    String logoImagePath;
    String attentionImagePath;
    String alipayId;
}
