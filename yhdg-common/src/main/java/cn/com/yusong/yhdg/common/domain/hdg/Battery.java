package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 电池
 */
@Setter
@Getter
public class Battery extends StringIdEntity {


    public enum BatteryColumn {
        ID(1, "电池id"), //电池id
        CODE(2, "IMEI"), //IMEI 电池编号
        TYPE(3, "类型"),//类型
        AGENT_ID(4, "运营商id"), //运营商id
        VOLUME(5, "当前电量"),//当前电量
        STATUS(6, "状态"), //状态
        QRCODE(7, "二维码"), //二维码
        SHELL_CODE(8, "外壳编号"), //外壳编号
        CREATE_TIME(9, "创建时间"), //创建时间
        UP_LINE_TIME(10, "上线时间"),//上线时间
        ;

        private final int value;
        private final String name;

        BatteryColumn(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (BatteryColumn e : BatteryColumn.values()) {
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

    public enum Appearance {
        hard(3, "Y"),
        middle(2, "M"),
        soft(1, "R"),
        ;

        private final int value;
        private final String name;

        Appearance(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (Battery.Appearance e : Battery.Appearance.values()) {
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

    public enum SignalType {
        G2(0, "2G"),
        G3(1, "3G"),
        G4(2, "4G"),
        NBIot(3, "NBIot");

        private final int value;
        private final String name;

        SignalType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (SignalType e : SignalType.values()) {
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

    public enum LockSwitch {
        NO(0, "不控制"),
        DISCHG_CLOSE(1, "放电关"),
        DISCHG_OPEN(2, "放电开"),
        ;

        private final int value;
        private final String name;

        LockSwitch(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (LockSwitch e : LockSwitch.values()) {
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

    public enum Material {
        CODE_1(1, "铅酸电池"),
        CODE_2(2, "镍氢电池"),
        CODE_3(3, "磷酸铁锂电池"),
        CODE_4(4, "锰酸锂电池"),
        CODE_5(5, "钴酸电池"),
        CODE_6(6, "三元材料电池"),
        CODE_7(7, "聚合物锂离子电池"),
        CODE_8(8, "钛酸锂电池");

        private final int value;
        private final String name;

        Material(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Material e : Material.values()) {
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

    public enum Fet {
        CODE_1(1, "充电"),
        CODE_2(2, "放电"),;

        private final int value;
        private final String name;

        Fet(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Fet e : Fet.values()) {
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

    public enum Category {
        EXCHANGE(1, "换电"),
        RENT(2, "租电");

        private final int value;
        private final String name;

        Category(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (Category e : Category.values()) {
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

    public enum UpLineStatus {
        NOT_ONLINE(0, "未上线"),
        ONLINE(1, "已上线");

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        UpLineStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (UpLineStatus e : UpLineStatus.values()) {
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

    public enum Status {
        NOT_USE(1, "未使用"), //电池没有在柜子中
        IN_BOX_NOT_PAY(2, "柜子中(换电未付款)"), //柜子中(客户换电未付款)
        IN_BOX(3, "柜子中"),
        KEEPER_OUT(4, "维护取出"), //电池被维护取出
        IN_BOX_CUSTOMER_USE(5, "客户未取出"),
        CUSTOMER_OUT(6, "客户使用中"), //电池被客户取出
        ;

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


    //1 未充电 2 待充电 3 充电中

    public enum ChargeStatus {
        NOT_CHARGE(1, "未充电"),
        WAIT_CHARGE(2, "待充电"),
        CHARGING(3, "充电中"),
        CHARGE_FULL(4, "已充满");

        private final int value;
        private final String name;

        ChargeStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ChargeStatus e : ChargeStatus.values()) {
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
    //0 位置不移动 1 位置移动中 2 通电中

    public enum PositionState {
        NOT_MOVE(0, "位置不移动"),
        MOVE(1, "位置移动中"),
        ELECTRICITY(2, "充电中");

        private final int value;
        private final String name;

        PositionState(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (PositionState e : PositionState.values()) {
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

    //0充电  1放电  2不充电也不放电
    public enum FetStatus {
        CHARGE(0, "充电"),
        DISCHARGE(1, "放电"),
        STATIC(2, "不充电也不放电");

        private final int value;
        private final String name;

        FetStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (FetStatus e : FetStatus.values()) {
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

    public enum RescueStatus {
        NOT(0, "未自救"),
        WAIT(1, "待下发自救"),
        END(2, "已下发自救"),
        RECOVERED(3, "已恢复"),
        ;

        private final int value;
        private final String name;

        RescueStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (RescueStatus e : RescueStatus.values()) {
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

    //1 无 2 返修中 3 已返修
    public enum RepairStatus {
        NOT(1, "无"),
        REWORK(2, "返修中"),
        COMPLETE(3, "已返修");

        private final int value;
        private final String name;

        RepairStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (RepairStatus e : RepairStatus.values()) {
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

    public enum LinkStatus {
        CODE_1(1, "检测到网络模块"),
        CODE_2(2, "SIM卡正常"),
        CODE_3(3, "网络注册成功"),
        CODE_4(4, "GPRS激活"),
        CODE_5(5, "服务器连接成功"),
        CODE_6(6, "数据接收成功"),
        CODE_7(7, "参数保存状态");

        private final int value;
        private final String name;

        LinkStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (LinkStatus e : LinkStatus.values()) {
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

    String code;
    Integer type;
    Integer category;
    Integer agentId;
    String orderId;                 //订单Id
    Long chargeRecordId;            //充电记录订单Id
    Integer volume;                 //当前电量
    Integer fullVolume;             //满电电量
    Integer chargeCompleteVolume;   //充满电量
    Integer currentCapacity;          //当前电量
    Integer status; //状态
    Integer exchangeAmount;         //换电次数
    Date reportTime;                //上报时间
    Integer orderDistance;          //订单距离
    Long totalDistance;             //总距离
    Integer isActive;               //是否启用
    Integer gpsSwitch;
    Integer lockSwitch;
    Integer currentSignal;
    Integer belongCityId;           //所属城市id
    Double lng;                     //经度
    Double lat;                     //维度
    Date gpsUpdateTime;         //gps更新时间
    String address;
    Integer health;
    String simMemo;
    String brand;                   //品牌

    String version;                 /*版本*/
    Integer voltage;                /*电压*/
    Integer electricity;            /*电流*/
    Integer totalCapacity;            /*总容量*/
    Integer useCount;               /*使用次数*/
    Integer useDay;                 /*使用天数*/
    Date produceDate;               /*生产日期*/
    Integer protectState;            /*保护状态 0为未保护，1发生保护*/
    Integer fet;                     /*MOS指示状态 0表示MOS关闭，1表示打开*/
    Integer strand;                 /*电池串数*/
    String temp;                    /*温度*/
    Integer fetStatus;                  /*0充电  1放电  2不充电也不放电*/
    Integer positionState;          /*0表示位置不移动 1表示位置移动中 2表示通电中*/
    Integer chargeStatus;           /*0 未充电 1 充电中*/

    String singleVoltage;           /*电池每串单体电压*/

    Date readyChargeTime;           //待充电时间

    String shopId;
    String shopName;
    Date customerOutTime;
    Long customerId;
    String customerMobile;
    String customerFullname;

    Date inBoxTime;
    String cabinetId;//当前所属站点
    String cabinetName;//当前所属站点名称
    String boxNum;

    Date keeperOutTime;
    Long keeperId;
    String keeperName;
    String keeperMobile;
    String keeperOrderId;

    Date freeOutTime;

    Integer stayHeartbeat;          /*停留心跳间隔*/
    Integer moveHeartbeat;          /*移动心跳间隔*/
    Integer electrifyHeartbeat;     /*通电心跳间隔*/
    Integer standbyHeartbeat; /*存储心跳间隔*/
    Integer dormancyHeartbeat; /*休眠心跳间隔*/

    Integer isOnline;     /*是否在线*/
    Integer isNormal;/*是否异常*/
    String operator;/*标记异常人*/
    Date operatorTime;/*异常时间*/
    String abnormalCause;/*异常标识原因*/
    Integer isElectrify;
    Date lowVolumeNoticeTime;
    Integer isReportVoltage;
    Date reportVoltageTime;
    Integer acResistance;/*交流内阻 单位mΩ*/
    Integer resilienceVol;/*回弹电压 单位mV*/
    Integer staticVol;/*静置电压 单位mV*/
    Integer circle;/*循环次数*/

    String qrcode;
    String shellCode;
    String cellMfr;//电芯厂家
    String cellModel;//电芯型号
    Integer cellCount;/*电芯串数*/
    Integer appearance;//外观指标1.R 2.M 3.Y
    Integer  nominalCap;/*组包容量 单位mAh*/

    Date createTime;
    Long notElectrifyFaultLogId;
    Long monomerOvervoltageFaultLogId; //
    Long monomerLowvoltageFaultLogId; //
    Long wholeOvervoltageFaultLogId; //
    Long wholeLowvoltageFaultLogId; //
    Long chargeOvertempFaultLogId; //
    Long chargeLowtempFaultLogId; //
    Long dischargeOvertempFaultLogId; //
    Long dischargeLowtempFaultLogId; //
    Long chargeOvercurrentFaultLogId; //
    Long dischargeOvercurrentFaultLogId; //
    Long shortCircuitFaultLogId; //
    Long testingIcFaultLogId; //
    Long softwareLockingFaultLogId; //
    Long dischargeLockingFaultLogId; //
    Long chargeMosFaultLogId; //
    Long dischargeMosFaultLogId; //
    Long maxVolDiffLogId; //
    Long signVolLowLogId;

    Long hitFaultLogId; //
    Long dismantleFaultLogId; //
    Integer lowVolumeNoticeVolume; /*低电量通知时电量*/
    Integer gprsShutdown;
    Integer shutdownVoltage;
    Integer acceleretedSpeed;
    Integer repairStatus;
    String belongCabinetId; //首次放入柜子时的所属站点

    Integer realVoltageDiff;//当前压差 单位mV
    Date realVoltageDiffTime;//当前压差时间
    Integer fullVoltageDiff;//充满最大压差 单位mV
    Date fullVoltageDiffTime;//充满最大压差时间
    Integer dischargeVoltageDiff;//放电最大压差 单位mV
    Date dischargeVoltageDiffTime;//放电最大压差时间

    Integer signalType;         //信号类型 0:2g 1:3G 2:4G 3:NBIot
    Integer material;           //材质（1.三元,2.磷酸铁锂,3.铅酸）*/
    String bmsModel;

    Integer rescueStatus;

    Integer upLineStatus; //上线状态 0 未上线 1 已上线*/
    Date upLineTime;/*上线时间*/
    Date rentRecordTime;

    String upLineStatusName;
    Integer linkStatus;

    @Transient
    String agentName;
    String brandName;
    String belongCabinetName; //首次放入柜子时所属的站点名称
    @Transient
    String batteryType;
    @Transient
    String statusName; //状态名称
    @Transient
    String imageSuffix; //电池监控图片后缀
    @Transient
    String chargeStatusName; //充电状态名称
    @Transient
    String positionStateName; //位置状态名称
    @Transient
    String fetStatusName, monitorStatusName;
    Integer maxVolume, minVolume;
    List<Integer> customerIds;
    @Transient
    Integer cabinetCompanyId;
    Long userId;
    @Transient
    Integer queryFlag, monitorStatus, chargeCompleteFlag;
    @Transient
    BatteryFormat batteryFormat1;//电池规格
    BatteryFormat batteryFormat2;//电池规格
    BatteryFormat batteryFormat3;//电池规格
    BatteryFormat batteryFormat4;//电池规格
    BatteryFormat batteryFormat5;//电池规格
    BatteryFormat batteryFormat6;//电池规格
    Integer expectCellCount;//电池规格电芯条数

    //电池上线租金
    Integer rentPeriodType;/*租金周期*/
    Integer rentPeriodMoney; /*租金周期 每个周期收多少钱*/
    Date rentExpireTime;/*租金周期 过期时间*/

    //站点统计电池数量
    Integer batteryCount;
    Integer boxCount;
    Integer cabinetBatteryCount;
    Integer lessBatteryCount;
    String queryAntiVersion;
    Integer customerUseFlag;
    Integer inCabinetFlag;
    Integer notUseFlag;
    Integer countByCustomerUse;
    Integer countByInCabinet;
    Integer countByNotUse;
    Integer firstDataFlag;

    String batteryTypeList; //电池类型
    Integer[] types;

    public String getRepairStatusName() {
        if (this.repairStatus != null) {
            return RepairStatus.getName(this.repairStatus);
        }
        return "无";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getProduceDate() {
        return produceDate;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReadyChargeTime() {
        return readyChargeTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCustomerOutTime() {
        return customerOutTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getInBoxTime() {
        return inBoxTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getKeeperOutTime() {
        return keeperOutTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getFreeOutTime() {
        return freeOutTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpLineTime() {
        return upLineTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getOperatorTime() {
        return operatorTime;
    }

    public String getChargeStatusName() {
        if (chargeStatus != null) {
            return ChargeStatus.getName(chargeStatus);
        }
        return "";
    }

    public String getPositionStateName() {
        if (positionState != null) {
            return PositionState.getName(positionState);
        }
        return "";
    }

    public String getFetStatusName() {
        if (fetStatus != null) {
            return FetStatus.getName(fetStatus);
        }
        return "";
    }

    public String getStatusName() {
        if (status != null) {
            return Status.getName(status);
        }
        return "";
    }

    public String getSignalTypeName() {
        if(signalType != null) {
            return Battery.SignalType.getName(signalType);
        }
        return "";
    }

    public String getMaterialName() {
        if(material != null) {
            return Battery.Material.getName(material);
        }
        return "";
    }

    public String getUpLineStatusName() {
        if(upLineStatus != null) {
            return BatteryInstallRecord.Status.getName(upLineStatus);
        }
        return "";
    }

    public String getCategoryName() {
        if (category != null) {
            return Battery.Category.getName(category);
        }
        return "";
    }

    public String getLinkStatusName() {
        String name = "";
        if (linkStatus != null) {
            LinkStatus[] names = LinkStatus.values();
            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(linkStatus)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if ('1' == str[i]) {
                    if (StringUtils.isNotEmpty(name)) {
                        name += ",";
                    }
                    name += names[i].name;
                }
            }
        }
        return name;
    }

    public String getFetName() {
        String name = "";
        if (null != fet) {
            Fet[] names = Fet.values();

            char[] str = new StringBuilder(String.format("%0" + names.length + "d", Long.parseLong(Integer.toBinaryString(fet)))).reverse().toString().toCharArray();

            for (int i = 0; i < names.length; i++) {
                if ('1' == str[i]) {
                    if (StringUtils.isNotEmpty(name)) {
                        name += ",";
                    }
                    name += names[i].name + "开";
                } else if ('0' == str[i]) {
                    if (StringUtils.isNotEmpty(name)) {
                        name += ",";
                    }
                    name += names[i].name + "关";
                }
            }
        }
        return name;
    }

}
