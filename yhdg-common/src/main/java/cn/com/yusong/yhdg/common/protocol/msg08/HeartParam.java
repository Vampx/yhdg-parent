package cn.com.yusong.yhdg.common.protocol.msg08;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 后面可能会出现新的心跳版本 为了兼容新的版本 这里统一做一个转换
 */
public class HeartParam {

    public static final String EMPTY_BATTERY_CARD = "00000000";
    public static final String EMPTY_IMEI_CODE = "000000000000000";
    public static final String EMPTY_CHARGER_CODE = "000000";

    //请求参数
    public String code;
    public String version;
    public Integer temp1;
    public Integer temp2;
    public Integer degree;
    public Byte network;
    public Byte signal;
    public Byte fireState;
    public Byte peripheral;
    //
    public Byte fanSpeed;
    public Byte waterLevel;
    public Byte smokeState;

    public Integer acVoltage;
    public Integer boxSize;

    public List<Box> boxList = new ArrayList<Box>(20);

    public static class Box {
        public byte boxNum;
        public Byte isClosed;
        public int boxState;
        public Byte isSmokeAlarm;

        public Integer batteryExist;

        public String batteryId;
        public String batteryCode;
        public String batteryVersion;
        public Integer voltage;
        public Integer electricity;
        public int restCapacity;
        public Byte volume;
        public int circle;
        public int ratedCapacity;
        public String temp;
        public Integer serials;
        public String singleVoltage;
        public Integer protectState;
        public Byte fet;
        public int linkStatus;
        public Byte chargeStatus;
        public Integer power;
        public Integer batteryTemp1;
        public Integer batteryTemp2;

        //
        public Byte boxVersion;
        public Integer boxTemp;
        public Byte fanSpeed;
        //充电器
        public String chargerVersion;
        public String chargerModule;
        public Integer chargeState;
        public Integer chargeStage;
        public Integer chargeTime;
        public Integer chargeVoltage;
        public Integer batteryVoltage;
        public Integer chargeCurrent;
        public Integer transformerTemperature;
        public Integer heatsinkTemperature;
        public Integer ambientTemperature;
        public Integer chargerFault;

        //格口是否禁用
        public Integer boxForbidden;//1禁用 0启用

//        @Override
//        public String toString() {
//            return "Box{" +
//                    "boxNum=" + boxNum +
//                    ", isClosed=" + isClosed +
//                    ", isSmokeAlarm=" + isSmokeAlarm +
//                    ", batteryId='" + batteryId + '\'' +
//                    ", batteryCode='" + batteryCode + '\'' +
//                    ", voltage=" + voltage +
//                    ", electricity=" + electricity +
//                    ", volume=" + volume +
//                    ", protectState=" + protectState +
//                    ", fet=" + fet +
//                    ", chargeStatus=" + chargeStatus +
//                    ", power=" + power +
//                    ", batteryTemp1=" + batteryTemp1 +
//                    ", batteryTemp2=" + batteryTemp2 +
//                    '}';
//        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE).replaceAll("[ \r]","").replaceAll("\n\n","\n");
        }
    }

}
