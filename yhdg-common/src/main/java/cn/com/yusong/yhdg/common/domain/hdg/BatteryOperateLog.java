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
 * 电池操作日志
 */
@Setter
@Getter
public class BatteryOperateLog extends LongIdEntity {

    public enum OperateType {
        CUSTOMER_PUT_OLD(1, "客户放入旧电"),
        CUSTOMER_TAKE_OLD(2, "客户取出旧电"),
        CUSTOMER_PAY_OLD(3, "换电付款"),
        CUSTOMER_TAKE_NEW(4, "客户取出新电"),
        CUSTOMER_BACK_OLD(5, "客户退租电池"),
        KEEPER_PUT(6, "维护放入"), //电池被维护取出
        KEEPER_TAKE(7, "维护取出"), //电池被维护取出
        BATTERY_PUT(8, "电池放入"),
        BATTERY_TAKE(9, "电池取出"),
        CUSTOMER_START_CHARGE(10, "客户开始充电"),
        CUSTOMER_END_CHARGE(11, "客户结束充电"),
        KEEPER_START_CHARGE(12, "维护开始充电"),
        KEEPER_END_CHARGE(13, "维护结束充电"),
        CABINET_START_CHARGE(14, "柜子开始充电"),
        CABINET_END_CHARGE(15, "柜子结束充电"),
        CUSTOMER_START_USE(16, "客户开始用电"),;


        private final int value;
        private final String name;

        OperateType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (OperateType e : OperateType.values()) {
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

    String batteryId;
    Integer operateType;
    Long customerId;
    String customerMobile;
    String customerFullname;
    String cabinetId;
    String cabinetName;
    String shopId;
    String shopName;
    String boxNum;
    Long keeperId;
    String keeperName;
    String keeperMobile;
    Integer volume;
    Date createTime;

    @Transient
    String operateTypeName; //操作类型名称

    public BatteryOperateLog(String batteryId, Integer operateType, Long customerId, String customerMobile, String customerFullname, String cabinetId, String cabinetName, String boxNum, Integer volume, Date createTime) {
        this.batteryId = batteryId;
        this.operateType = operateType;
        this.customerId = customerId;
        this.customerMobile = customerMobile;
        this.customerFullname = customerFullname;
        this.cabinetId = cabinetId;
        this.cabinetName = cabinetName;
        this.boxNum = boxNum;
        this.volume = volume;
        this.createTime = createTime;
    }

    public BatteryOperateLog(String batteryId, Integer operateType, Long customerId, String customerMobile, String customerFullname, Integer volume, Date createTime) {
        this.batteryId = batteryId;
        this.operateType = operateType;
        this.customerId = customerId;
        this.customerMobile = customerMobile;
        this.customerFullname = customerFullname;
        this.volume = volume;
        this.createTime = createTime;
    }

    public BatteryOperateLog(String batteryId, Integer operateType, String keeperMobile, String keeperName, Long keeperId, Integer volume, Date createTime) {
        this.batteryId = batteryId;
        this.operateType = operateType;
        this.keeperMobile = keeperMobile;
        this.keeperName = keeperName;
        this.keeperId = keeperId;
        this.volume = volume;
        this.createTime = createTime;
    }

    public BatteryOperateLog(String batteryId, Integer operateType, String keeperMobile, String keeperName, Long keeperId, String cabinetId, String cabinetName, String boxNum, Integer volume, Date createTime) {
        this.batteryId = batteryId;
        this.operateType = operateType;
        this.keeperMobile = keeperMobile;
        this.keeperName = keeperName;
        this.keeperId = keeperId;
        this.cabinetId = cabinetId;
        this.cabinetName = cabinetName;
        this.boxNum = boxNum;
        this.volume = volume;
        this.createTime = createTime;
    }

    public BatteryOperateLog(String batteryId, Integer operateType, String cabinetId, String cabinetName, String boxNum, Integer volume, Date createTime) {
        this.batteryId = batteryId;
        this.operateType = operateType;
        this.cabinetId = cabinetId;
        this.cabinetName = cabinetName;
        this.boxNum = boxNum;
        this.volume = volume;
        this.createTime = createTime;
    }

    public BatteryOperateLog(String batteryId, Integer operateType, Integer volume, Date createTime) {
        this.batteryId = batteryId;
        this.operateType = operateType;
        this.volume = volume;
        this.createTime = createTime;
    }

    public BatteryOperateLog() {
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerFullname() {
        return customerFullname;
    }

    public void setCustomerFullname(String customerFullname) {
        this.customerFullname = customerFullname;
    }

    public String getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(String cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }

    public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum;
    }

    public Long getKeeperId() {
        return keeperId;
    }

    public void setKeeperId(Long keeperId) {
        this.keeperId = keeperId;
    }

    public String getKeeperName() {
        return keeperName;
    }

    public void setKeeperName(String keeperName) {
        this.keeperName = keeperName;
    }

    public String getKeeperMobile() {
        return keeperMobile;
    }

    public void setKeeperMobile(String keeperMobile) {
        this.keeperMobile = keeperMobile;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }
    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOperateTypeName() {
        return operateTypeName;
    }

    public void setOperateTypeName(String operateTypeName) {
        this.operateTypeName = operateTypeName;
    }
}
