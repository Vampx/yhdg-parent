package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物业
 */
@Setter
@Getter
public class Estate extends LongIdEntity {
    public enum IsActive {
        ENABLE(1, "启用"),
        DISABLE(2, "禁用");
        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        private IsActive(int value, String name) {
            this.value = value;
            this.name = name;
        }
        static {
            for (IsActive e : IsActive.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }

    public enum AuthType {
        PERSONAL (1, "个人"),
        ENTERPRISE(2, "企业")
        ;
        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        private AuthType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (AuthType e : AuthType.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(int value) {
            return map.get(value);
        }
    }


    Integer agentId;
    Integer authType;
    String estateName;
    String workTime;
    String linkname;
    String tel;
    String address;
    String memo;
    Integer isActive;
    Integer balance;/* 物业提现余额*/
    String payPeopleMobile;//收款人手机
    String payPeopleName;//收款人姓名
    String payPeopleMpOpenId;//收款人OpenId
    String payPeopleFwOpenId;//收款人支付宝账号
    String payPassword;//提现密码
    Date createTime;
    @Transient
    String password;
    String agentName;
    List<Cabinet> cabinetList;
    Integer carCount;
    Long customerId;
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }


}
