package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.AreaEntity;
import cn.com.yusong.yhdg.common.domain.Distance;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class Cabinet extends StringIdEntity implements AreaEntity, Distance {

    public enum NetworkType {
        NETWORK_0(0, "GSM/GPRS"),
        NETWORK_1(1, "Wired"),
        NETWORK_2(2, "3G"),
        NETWORK_3(3, "4G");

        private final int value;
        private final String name;

        NetworkType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (NetworkType e : NetworkType.values()) {
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

    public enum IsFpOpen {
        OPEN(1, "开启"),
        CLOSED(0, "关闭"),
        ;

        private final int value;
        private final String name;

        IsFpOpen(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (IsFpOpen e : IsFpOpen.values()) {
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

    public enum WaterLevel {
        OVERLEVEL(1, "超水位"),
        NORMAL(0, "正常"),
        ;

        private final int value;
        private final String name;

        WaterLevel(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (WaterLevel e : WaterLevel.values()) {
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

    public enum SmokeState {
        ALARM(1, "报警"),
        NORMAL(0, "正常"),
        ;

        private final int value;
        private final String name;

        SmokeState(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (SmokeState e : SmokeState.values()) {
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

    public enum AcVoltageState {
        INTERRUPT(1, "断电"),
        NORMAL(0, "正常"),
        ;

        private final int value;
        private final String name;

        AcVoltageState(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (AcVoltageState e : AcVoltageState.values()) {
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

    public enum Subtype {
        EXCHANGE(1, "换电"),
        EXCHANGE_CHARGE(2, "换/充电");

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        Subtype(int value, String name) {
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

    public enum FaultType {
        NORMAL(0, "正常"),
        alarm(1, "报警");

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        FaultType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (Cabinet.FaultType e : Cabinet.FaultType.values()) {
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

    public enum ViewType {
        SHARED(1, "共享"),
        UNSHARED(2, "独享");

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        ViewType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (ViewType e : ViewType.values()) {
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

    public enum ActiveStatus {
        ENABLE(1, "启用"),
        DISABLE(2, "禁用"),
        BUILD(3, "建设中");

        private final int value;
        private final String name;

        ActiveStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (ActiveStatus e : ActiveStatus.values()) {
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
        APPLY_FOR_ONLINE(1, "申请上线"),
        ONLINE(2, "已上线");

        private final int value;
        private final String name;
        private static final Map<Integer, String> map = new HashMap<Integer, String>();

        UpLineStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        static {
            for (Cabinet.UpLineStatus e : Cabinet.UpLineStatus.values()) {
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
    Long packetPeriodGroupId; /*套餐组id*/
    String cabinetName;
    Integer permitExchangeVolume;/*允许换电电量 电量没有小于等于规定电量，则不允许换电*/
    Integer minExchangeVolume;/*最低可换电量*/
    Integer provinceId;
    Integer cityId;
    Integer districtId;
    String street;
    Double lng;
    Double lat;
    String geoHash;
    Integer chargeFullVolume;
    Integer activeStatus;
    String imagePath1;
    String imagePath2;
    String imagePath3;
    String workTime;/*工作时间*/
    String linkname;            /*联系人*/
    String tel;
    Long dispatcherId;          /*调度人员*/
    String address;
    String keyword;
    String terminalId;
    Integer operationFlag;/*是否有开关箱操作 */
    Integer allFullCount;/*箱体数量*/
    Long allFullFaultLogId; /*箱体全满故障*/
    String loginToken;/*登录token*/
    String dynamicCode;
    Double price;/*电价*/
    Integer useVolume;/*用电量*/
    Integer batteryNum;
    Integer chargeBatteryNum;/*充电电池数*/
    Integer subtype;
    Integer networkType; /*网络类型 0:gsm/gprs 1:wired 2:3g 3:4g*/
    Integer currentSignal;//
    String ip;//
    String mac;//
    String version;//
    Integer maxChargePower; //
    Integer boxMaxPower; //
    Integer boxMinPower; //
    Integer boxTrickleTime; //
    Integer enableWifi;//是否开启wifi
    Integer enableBluetooth;//是否开启蓝牙
    Integer enableVoice;//是否开启语音播报
    Date heartTime;//
    Integer isOnline;//
    String statusInfo;//
    Integer temp1;//
    Integer temp2;//
    Integer power;//
    String simMemo;//                     /*sim卡信息*/
    Integer maxChargeCount;//
    Integer faultType;//
    Long offlineFaultLogId;//
    Long tempFaultLogId;//
    Integer activeFanTemp; //启用风扇的温度
    Integer isFpOpen; ///*灭火器是否打开 1开启 0 关闭*/
    Integer fanSpeed; //
    Integer waterLevel; ///*水位状态 0正常 1超水位 */
    Integer smokeState; ///*柜子烟雾传感器状态 0正常 1报警*/
    Integer acVoltage; //
    Integer acVoltageState; ///*交流电输入状态 0:正常 1:断电*/
    Integer upLineStatus;/*上线状态 0 未上线 1 申请上线 2 已上线*/
    Date upLineTime; //上线时间
    Integer foregiftMoney;/*押金金额*/
    Integer rentMoney;/*租金金额*/
    Integer rentPeriodType;/*租金周期 单位0 无 1 月 2 年*/
    Date rentExpireTime;/*租金截至时间*/
    Date rentRecordTime;
    String shopId;//门店id
    Long estateId;//物业id
    Integer isAllowOpenBox;//判断门店下设备是否可远程开箱结束订单等操作
    Integer activePlatformDeduct;/*启用扣除金额 1 启用 0 禁用*/
    Integer platformDeductMoney; /*从运营商收入里面扣除多少钱*/
    Date platformDeductExpireTime;/*从运营商收入里面扣除多少钱 过期时间*/
    Integer platformRatio;//平台分成
    Integer agentRatio;//运营商分成
    Integer provinceAgentRatio;//省代分成
    Integer cityAgentRatio;//市代分成
    Integer shopRatio;//门店分成
    Integer shopFixedMoney;/*门店按固定金额分成*/
    Integer viewType;
    Integer inputDegreeNum; /*上次录入度数*/
    Integer inputDegreeMoney; /*上次录入电费金额*/
    Date inputDegreeTime; /*上次录入电表时间*/
    Integer minPrice;
    Integer maxPrice;
    Integer recoilVolume;
    String memo;
    Date createTime;
    @Transient
    String agentName;

    String upLineStatusName;

    @Transient
    double distance;
    @Transient
    int emptyCount, fullCount, chargeCount, lockCount, reserveCount;
    @Transient
    String provinceName, cityName, districtName,streetName,streetNumber, keeperName, groupName, cabinetCompanyName;
    @Transient
    Integer busyCount, freeCount, waitCount, type;//type 格口电池类型
    @Transient
    Integer userCount;
    @Transient
    Long userId;
    @Transient
    Integer queryFlag, unboundFlag;
    @Transient
    String priceSettingName, showStatus, faultTypeName;
    @Transient
    Integer chargingCount; //充电中数量
    Integer batteryType;//电池类型
    Integer unbind;//是否未绑定电池类型
    Integer unbindShop;//是否未绑定门店
    Integer unbindEstate;//是否未绑定物业
    Integer terminalDetailFlag;//是否是升级版本设备
    Integer vipFlag;
    Integer priceFlag;
    @Transient
    String shopName;//门店名称
    String shopLinkname;//门店联系人
    String shopTel;//门店联系电话
    String shopWorkTime;//门店工作时间
    Integer shopBalance;//门店余额
    Integer shopPermitExchangeVolume;//门店可换进电量
    Integer shopActiveStatus;//门店是否启用
    Integer unActiveBoxNum;//禁用格口数
    String queryAntiVersion;
    @Transient
    Long settingId;//换电分期设置ID
    @Override
    public Integer getDistrictId() {
        return districtId;
    }

    @Override
    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public void setStreet(String street) {
        this.street = street;
    }


    @Override
    public String getProvinceName() {
        return provinceName;
    }

    @Override
    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    @Override
    public String getCityName() {
        return cityName;
    }

    @Override
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String getDistrictName() {
        return districtName;
    }

    @Override
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddressName() {
        return StringUtils.trimToEmpty(getProvinceName()) + StringUtils.trimToEmpty(getCityName()) + StringUtils.trimToEmpty(getDistrictName());
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getRentExpireTime() {
        return rentExpireTime;
    }

    public void setRentExpireTime(Date rentExpireTime) {
        this.rentExpireTime = rentExpireTime;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getUpLineTime() {
        return upLineTime;
    }

    public void setUpLineTime(Date upLineTime) {
        this.upLineTime = upLineTime;
    }



    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getHeartTime() {
        return heartTime;
    }

    public void setHeartTime(Date heartTime) {
        this.heartTime = heartTime;
    }

    public String getUpLineStatusName() {
        if (upLineStatus != null) {
            return Cabinet.UpLineStatus.getName(upLineStatus);
        }
        return "";

    }

    public String getNetworkTypeName() {
        if (networkType != null) {
            return Cabinet.NetworkType.getName(networkType);
        }
        return "";
    }

    public String getIsFpOpenName() {
        if (isFpOpen != null) {
            return Cabinet.IsFpOpen.getName(isFpOpen);
        }
        return "";
    }

    public String getWaterLevelName() {
        if (waterLevel != null) {
            return Cabinet.WaterLevel.getName(waterLevel);
        }
        return "";
    }

    public String getSmokeStateName() {
        if (smokeState != null) {
            return Cabinet.SmokeState.getName(smokeState);
        }
        return "";
    }

    public String getAcVoltageStateName() {
        if (acVoltageState != null) {
            return Cabinet.AcVoltageState.getName(acVoltageState);
        }
        return "";
    }

    public static int[] parsePeripheral(int peripheral) {
        int[] array = new int[8];
        for (int i = 0; i < 8; i++) {
            array[i] = peripheral >> i & 0x01;
        }
        return array;
    }
}
