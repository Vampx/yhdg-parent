package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * 充电器上报日志
 */

@Setter
@Getter
public class CabinetChargerReport extends LongIdEntity {
    public final static String CABINET_CHARGER_REPORT_TABLE_NAME = "hdg_cabinet_charger_report_";

    public enum ChargeState {
        CLOSED(0, "关"),
        OPEN(1, "开"),
        ;

        private final int value;
        private final String name;

        ChargeState(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ChargeState e : ChargeState.values()) {
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

    public enum ChargeStage {
        FREE(0, "空闲"),
        PRECHARGE(1, "预充"),
        CONSTANT_CURRENT1(2, "恒流1"),
        CONSTANT_CURRENT2(3, "恒流2"),
        CONSTANT_VOLTAGE(4, "恒压"),
        CHARGE_FULL(5, "充满"),
        ;

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();


        static {
            for (ChargeStage e : ChargeStage.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        ChargeStage(int value, String name) {
            this.value = value;
            this.name = name;
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

    public enum ChargerFault {
        CODE_0(0, "无故障"),
        CODE_1(1, "电压异常"),
        CODE_2(2, "电流异常"),
        CODE_3(3, "充电时间异常"),
        CODE_4(4, "输入欠压"),
        CODE_5(5, "输入过压"),
        CODE_6(6, "温度异常"),
        ;

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ChargerFault e : ChargerFault.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        ChargerFault(int value, String name) {
            this.value = value;
            this.name = name;
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

    public enum BmsStopFault {
        CODE_0(0, "无异常"),
        CODE_1(1, "高温异常"),
        CODE_2(2, "低温异常"),
        CODE_3(3, "单体过压异常"),
        CODE_4(4, "整体过压异常"),
        CODE_5(5, "充电器设置充电电流大于BMS过流保护值"),
        CODE_6(6, "未读取到BMS参数"),
        ;

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BmsStopFault e : BmsStopFault.values()) {
                map.put(e.getValue(), e.getName());
            }
        }

        BmsStopFault(int value, String name) {
            this.value = value;
            this.name = name;
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

    public enum EnableCharge {
        NO(0, "否"),
        YES(1, "是"),
        ;

        private final int value;
        private final String name;

        EnableCharge(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (EnableCharge e : EnableCharge.values()) {
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

    public enum EnableLink {
        NO(0, "否"),
        YES(1, "是"),
        ;

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        EnableLink(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (EnableLink e : EnableLink.values()) {
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

    public enum AutoSwtichMode {
        CLOSED(0, "关闭"),
        OPEN(1, "开启"),
        ;

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        AutoSwtichMode(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (AutoSwtichMode e : AutoSwtichMode.values()) {
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

    public enum LowTemperatureMode {
        CODE_0(0, "不允许充电"),
        CODE_1(1, "以1/2的电流进行充电"),
        CODE_2(2, "正常充电"),
        ;

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        LowTemperatureMode(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (LowTemperatureMode e : LowTemperatureMode.values()) {
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

    public enum Other {
        CODE_1(1, "是否异常"),
        CODE_2(2, "使能NFC检测"),
        ;


        private final int value;
        private final String name;

        Other(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Other e : Other.values()) {
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

    public enum BoxForbidden {
        CODE_0(0, "使用"),
        CODE_1(1, "禁用"),
        ;

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        BoxForbidden(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (BoxForbidden e : BoxForbidden.values()) {
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

    String cabinetId;/*主柜id*/
    String boxNum;/*格子编号*/
    String chargerVersion;/*充电器版本*/
    String chargerModule;/*充电器型号*/
    Integer chargeState;/*充电状态*/
    Integer chargeStage;/*充电阶段 0:空闲 1:预充 2:恒流1 3:恒流2 4:恒压 5:充满 */
    Integer chargeTime;/*充电时长 */
    Integer chargeVoltage;/*充电器输出电压 */
    Integer batteryVoltage;/*电池端检测电压 */
    Integer chargeCurrent;/*充电器输出电流 */
    Integer transformerTemp;/*变压器温度 */
    Integer heatsinkTemp;/*散热片温度 */
    Integer ambientTemp;/*环境温度 */
    Integer chargerFault;/*充电器故障 */
    Date createTime;/*创建时间*/

    Integer enableCharge;
    Integer enableLink;
    Integer autoSwtichMode;
    Integer maxChargeVoltageOfStage1;
    Integer chargeCurrentOfStage1;
    Integer maxChargeVoltageOfStage2;
    Integer chargeCurrentOfStage2;
    Integer slopeVoltage;
    Integer chargeCurrentOfStage3;
    Integer fullVoltage;
    Integer slopeCurrent;
    Integer minCurrentOfStage4;
    Integer lowTemperatureMode;
    Integer boxForbidden;
    Integer other;
    String suffix;

    public String getOtherName() {
        String name = "";
        if (null != other) {
            Other[] names = Other.values();

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(other)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if (StringUtils.isNotEmpty(name)) {
                    name += ",";
                }
                if ('1' == str[i]) {
                    if(i==0){
                        name += names[i].name +"异常";
                    } else if (i == 1) {
                        name += names[i].name + "使能";
                    }
                }else{
                    if(i==0) {
                        name += names[i].name + "正常";
                    } else if (i == 1) {
                        name += names[i].name + "禁用";
                    }
                }
            }
        }
        return name;
    }

    public String getLowTemperatureModeName() {
        if (lowTemperatureMode != null) {
            return LowTemperatureMode.getName(lowTemperatureMode);
        }
        return "";
    }

    public String getAutoSwtichModeName() {
        if (autoSwtichMode != null) {
            return AutoSwtichMode.getName(autoSwtichMode);
        }
        return "";
    }

    public String getEnableChargeName() {
        if (enableCharge != null) {
            return EnableCharge.getName(enableCharge);
        }
        return "";
    }

    public String getEnableLinkName() {
        if (enableLink != null) {
            return EnableLink.getName(enableLink);
        }
        return "";
    }

    public String getChargeStateName() {
        if (chargeState != null) {
            return ChargeState.getName(chargeState);
        }
        return "";
    }

    public String getChargeStageName() {
        if (chargeStage != null) {
            return ChargeStage.getName(chargeStage);
        }
        return "";
    }

    public String getChargeFaultName() {
        if (chargerFault != null && chargerFault != 0) {
            String faultString = String.valueOf(chargerFault);
            String chargerFaultString = faultString.substring(0, 4);
            Integer integer = Integer.valueOf(chargerFaultString, 2);
            return ChargerFault.getName(integer);
        }
        return "";
    }

    public String getBmsStopFaultName() {
        if (chargerFault != null && chargerFault != 0) {
            String faultString = String.valueOf(chargerFault);
            String bmsShopFaultString = faultString.substring(4, 8);
            Integer integer = Integer.valueOf(bmsShopFaultString, 2);
            return BmsStopFault.getName(integer);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

}
