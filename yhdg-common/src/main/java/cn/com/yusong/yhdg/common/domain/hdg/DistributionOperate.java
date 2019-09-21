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

@Setter
@Getter
public class DistributionOperate extends LongIdEntity {

    public enum IsActive {
        ENABLE(1, "启用"),
        DISABLE(2, "禁用");

        private final int value;
        private final String name;

        IsActive(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (IsActive e : IsActive.values()) {
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

    Integer agentId;/*运营商id*/
    String agentName;/*运营商名称*/
    String agentCode;/*运营商编号*/
    Integer isActive;
    String distributionName;
    String loginName;
    String password;
    String fullname;
    String payPeopleMobile;
    String payPeopleName;
    String payPeopleMpOpenId;
    String payPeopleFwOpenId;
    String payPassword;
    String memo;
    Date createTime;

    @Transient
    String deptName, stationName, isNotFixedName, isFixedName, isFixedPercentName;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
