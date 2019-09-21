package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.*;

/**
 * 换电柜充电器
 */
@Setter
@Getter
public class CabinetCharger extends PageEntity {

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

    String cabinetId; //主柜id
    String boxNum; //换电柜格子编号

    String chargerModule;/*充电器型号*/
    String chargerVersion;/*充电器版本*/
    Integer chargeState;/*充电状态*/
    Integer chargeStage;/*充电阶段 0:空闲 1:预充 2:恒流1 3:恒流2 4:恒压 5:充满 */
    Integer chargeTime;/*充电时长 */
    Integer chargeVoltage;/*充电器输出电压  mV*/
    Integer batteryVoltage;/*电池端检测电压  mV*/
    Integer chargeCurrent;/*充电器输出电流  mA*/
    Integer transformerTemp;/*变压器温度 */
    Integer heatsinkTemp;/*散热片温度 */
    Integer ambientTemp;/*环境温度 */
    Integer chargerFault;/*充电器故障 */

    Integer enableCharge;/*是否充电 1是 0否 */
    Integer enableLink;/*是否开启电池数据连接*/
    Integer autoSwtichMode;/*自动选择充电电压U2  0:关闭 1:开启*/
    Integer maxChargeVoltageOfStage1;/*阶段1(预充)最大充电电压 mV*/
    Integer chargeCurrentOfStage1;/*阶段1(预充)充电电流 mA 设置范围 1000-6000mA*/
    Integer maxChargeVoltageOfStage2;/*阶段2(恒流1)最大充电电压U3 mV*/
    Integer chargeCurrentOfStage2;/*阶段2(恒流1)充电电流 mA 设置范围 1000-12000mA*/
    Integer slopeVoltage;/*阶段3开始改变电流时电压 mV*/
    Integer chargeCurrentOfStage3;/*阶段3(恒流2)充电电流 mA 设置范围 1000-10000mA*/
    Integer fullVoltage;/*电池满电电压 mV*/
    Integer slopeCurrent;/*满电电压时斜率电流 mA 设置范围 1000-currentOfStage3 mA*/
    Integer minCurrentOfStage4;/*阶段4最小充电电流 mA 设置范围 50-3000mA*/
    Integer lowTemperatureMode;/*低温环境充电模式 0:不允许充电 1:以1/2的电流进行充电 2:正常充电*/
    Integer boxForbidden;/*是否禁用格口 1禁用 0使用 */
    Integer other;/*bit0:是否异常,离线时通过蓝牙发送 1异常/0正常 bit1:使能NFC检测 1使能/0禁用*/

    @Transient
    Integer abnormal;//1异常/0正常
    Integer enableNfc;//1使能/0禁用

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

    public static int[] parseOther(int other) {
        int[] array = new int[8];
        for (int i = 0; i < 8; i++) {
            array[i] = other >> i & 0x01;
        }
        return array;
    }



    public String getChargerFaultName() {
        if (chargerFault != null) {
            int[] array = new int[8];
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                array[i] = (chargerFault >> i & 0x01);
                builder.append(String.valueOf(array[i]));
            }

            String chargerFaultString = builder.reverse().toString().substring(4, 8);
            Integer integer = Integer.valueOf(chargerFaultString, 2);
            return ChargerFault.getName(integer);
        }
        return "";
    }

    public String getBmsStopFaultName() {
        if (chargerFault != null) {
            int[] array = new int[8];
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                array[i] = (chargerFault >> i & 0x01);
                builder.append(String.valueOf(array[i]));
            }

            String bmsStopFaultString = builder.reverse().toString().substring(0, 4);
            Integer integer = Integer.valueOf(bmsStopFaultString, 2);
            return BmsStopFault.getName(integer);
        }
        return "";
    }
}
