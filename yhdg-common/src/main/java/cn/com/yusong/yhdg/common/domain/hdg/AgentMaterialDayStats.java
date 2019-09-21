package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.IntIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*运营商材料日统计*/

@Getter
@Setter
public class AgentMaterialDayStats extends IntIdEntity {

    public enum Status {
        NOT_PAY(1, "未支付"),
        PAID(2, "已支付");

        private final int value;
        private final String name;

        Status(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Status e : Status.values()) {
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
    Integer category;
    String statsDate;/*统计日期 格式2017-01-01*/
    Integer agentId;
    String agentName;
    String agentCode;/*运营商编号*/
    Integer cabinetForegiftCount;/*设备押金数量*/
    Integer cabinetForegiftMoney;/*设备押金金额*/
    Integer cabinetRentCount;/*设备租金数量*/
    Integer cabinetRentMoney;/*设备租金金额*/
    Integer batteryRentCount;/*电池租金数量*/
    Integer batteryRentMoney;/*电池租金金额*/
    Integer idCardAuthCount;/*客户认证数量*/
    Integer idCardAuthMoney;/*客户认证金额*/
    Integer money; /*总金额*/
    Integer status;/*1 未支付 2 已支付*/
    Integer payType;
    Date payTime;
    Date createTime;

    @Transient
    String statusName;
    Integer totalMoney;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPayTime() {
        return payTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void init() {
        cabinetForegiftCount = 0;
        cabinetForegiftMoney = 0;
        cabinetRentCount = 0;
        cabinetRentMoney = 0;
        batteryRentCount = 0;
        batteryRentMoney = 0;
        idCardAuthCount = 0;
        idCardAuthMoney = 0;
        money = 0;
        status = 1;
        createTime = new Date();
    }

    public String getStatusName() {
        if(status != null) {
            return Status.getName(status);
        }
        return "";
    }
}
