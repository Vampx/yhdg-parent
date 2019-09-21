package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 换电押金分期设置
 */
@Setter
@Getter
public class ExchangeInstallmentSetting extends LongIdEntity {

    public enum Num{
        TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"),FIVE(5,"5"),SIX(6,"6"),SEVEN(7,"7"),EIGHT(8,"8"),NINE(9,"9"),TEN(10,"10");

        private final int value;
        private final String name;

        Num(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Num e : Num.values()) {
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

    String mobile;
    String fullname;
    Integer agentId;
    String agentName;
    String agentCode;
    Date deadlineTime; /*截至时间*/
    Date createTime;
    //2019年8月31日10:38:31新加字段
    String settingName;
    Integer settingType;
    Integer isActive;

    //2019年8月31日10:38:31待删除开始
    Integer totalMoney;/*总金额*/
    Integer batteryType;/*电池类型*/
    Long foregiftId; /*押金套餐id*/
    Integer foregiftMoney; /*押金金额*/
    Long packetId; /*租金套餐id*/
    Integer packetMoney;/*租金金额*/
    Long insuranceId;/*保险id*/
    Integer insuranceMoney; /*保险金额*/
    //待删除结束

    @Transient
    String batteryTypeName;
    Integer installmentNum;//分期数
    Date finalInstallmentTime;//最后分期时间
    Integer paidInstallmentNum;//分期已付期数
    Integer paidInstallmentMoney;//分期已付金额
    Integer installmentRestMoney;//分期剩余金额

    @Transient
    String customermobile;
    String cabinetId;
    String cabinetName;
    Integer customermobileNum;
    Integer cabinetNameNum;
    String stationId;
    Integer stationNameNum;
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getDeadlineTime() {
        return deadlineTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getFinalInstallmentTime() {
        return finalInstallmentTime;
    }

    public enum SettingType {
        STANDARD_STAGING(1, "标准分期"),
        CUSTOM_STAGING(2, "自定义分期");

        private final int value;
        private final String name;

        SettingType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ExchangeInstallmentSetting.SettingType e : ExchangeInstallmentSetting.SettingType.values()) {
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

}
