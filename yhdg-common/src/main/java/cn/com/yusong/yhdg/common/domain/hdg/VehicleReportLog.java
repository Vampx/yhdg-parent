package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 车辆上报日志
 */
public class VehicleReportLog  extends PageEntity {

    public static final int NOT_CHARGE = 0; //未充电
    public static final int CHARGING = 1; //充电中

    public final static String VEHICLE_REPORT_LOG_TABLE_NAME = "hdg_vehicle_report_log_";

    public enum Status {
        ENABLE(1, "启用"),
        DISABLE(2, "禁用");

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

    String vehicleId; /*车辆id*/
    Long modelId;/*车辆型号id*/
    String modelCode;/*车编号*/
    String modelName;/*车辆型号名*/
    Integer agentId;/*运营商id*/
    String shopId;/*店铺id*/
    String batteryId;/*电池id*/
    String orderId;/*订单id*/
    Integer status;/*状态*/
    Date reportTime;/*上报时间*/
    Integer orderDistance;/*订单距离*/
    Integer useCount;/*使用次数*/
    Integer baudRate;/*波特率*/
    String controllerVersion;/*控制器版本*/
    String communicationVersion;/*通信版本*/
    Integer voltagePercent;/*电压百分比*/
    Integer speedLimit;/*限速比率*/
    Integer voltage;/*电压*/
    Integer electricity;/*电流*/
    Integer holzerSpeed;/*霍尔转速*/
    Integer status1;/*心跳实时状态1*/
    Integer status2;/*心跳实时状态2*/
    Integer status3;/*心跳实时状态3*/
    Integer status4;/*心跳实时状态4*/
    Integer dismantleFaultLogId; /*防拆报警*/
    Integer controlInstruction;/*控制指令*/
    Integer totalDistance;/*总距离*/
    Integer isActive;/*是否启用*/
    Integer currentSignal;/*当前信号*/
    Double lng;/*经度*/
    Double lat;/*纬度*/
    String address ;/*地址*/
    Integer customerId;/*客户id*/
    String customerMobile;/*客户手机*/
    String customerFullname;/*客户名称*/
    Integer isOnline;/*是否在线*/
    Date createTime;


    @Transient
    String suffix;
    String agentName, shopName;
    Integer batteryType;/*电池类型*/
    String endurance; /*续航单位千米*/
    Integer speed;/*最高时速 小时/公里*/
    String meter;/*仪表*/
    String burglarproof;  /*防盗*/
    String brake;  /*刹车*/
    String tyre;/*轮胎*/
    String volume; /*长宽高*/
    String weight;/*车重*/
    String imagePath;/*车型图片*/

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public Integer getBaudRate() {
        return baudRate;
    }

    public void setBaudRate(Integer baudRate) {
        this.baudRate = baudRate;
    }

    public String getControllerVersion() {
        return controllerVersion;
    }

    public void setControllerVersion(String controllerVersion) {
        this.controllerVersion = controllerVersion;
    }

    public String getCommunicationVersion() {
        return communicationVersion;
    }

    public void setCommunicationVersion(String communicationVersion) {
        this.communicationVersion = communicationVersion;
    }

    public Integer getVoltagePercent() {
        return voltagePercent;
    }

    public void setVoltagePercent(Integer voltagePercent) {
        this.voltagePercent = voltagePercent;
    }

    public Integer getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(Integer speedLimit) {
        this.speedLimit = speedLimit;
    }

    public Integer getVoltage() {
        return voltage;
    }

    public void setVoltage(Integer voltage) {
        this.voltage = voltage;
    }

    public Integer getElectricity() {
        return electricity;
    }

    public void setElectricity(Integer electricity) {
        this.electricity = electricity;
    }

    public Integer getHolzerSpeed() {
        return holzerSpeed;
    }

    public void setHolzerSpeed(Integer holzerSpeed) {
        this.holzerSpeed = holzerSpeed;
    }

    public Integer getStatus1() {
        return status1;
    }

    public void setStatus1(Integer status1) {
        this.status1 = status1;
    }

    public Integer getStatus2() {
        return status2;
    }

    public void setStatus2(Integer status2) {
        this.status2 = status2;
    }

    public Integer getStatus3() {
        return status3;
    }

    public void setStatus3(Integer status3) {
        this.status3 = status3;
    }

    public Integer getStatus4() {
        return status4;
    }

    public void setStatus4(Integer status4) {
        this.status4 = status4;
    }

    public Integer getDismantleFaultLogId() {
        return dismantleFaultLogId;
    }

    public void setDismantleFaultLogId(Integer dismantleFaultLogId) {
        this.dismantleFaultLogId = dismantleFaultLogId;
    }

    public Integer getControlInstruction() {
        return controlInstruction;
    }

    public void setControlInstruction(Integer controlInstruction) {
        this.controlInstruction = controlInstruction;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getOrderDistance() {
        return orderDistance;
    }

    public void setOrderDistance(Integer orderDistance) {
        this.orderDistance = orderDistance;
    }

    public Integer getUseCount() {
        return useCount;
    }

    public void setUseCount(Integer useCount) {
        this.useCount = useCount;
    }

    public Integer getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(Integer totalDistance) {
        this.totalDistance = totalDistance;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getCurrentSignal() {
        return currentSignal;
    }

    public void setCurrentSignal(Integer currentSignal) {
        this.currentSignal = currentSignal;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
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

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getBatteryType() {
        return batteryType;
    }

    public void setBatteryType(Integer batteryType) {
        this.batteryType = batteryType;
    }

    public String getEndurance() {
        return endurance;
    }

    public void setEndurance(String endurance) {
        this.endurance = endurance;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getBurglarproof() {
        return burglarproof;
    }

    public void setBurglarproof(String burglarproof) {
        this.burglarproof = burglarproof;
    }

    public String getBrake() {
        return brake;
    }

    public void setBrake(String brake) {
        this.brake = brake;
    }

    public String getTyre() {
        return tyre;
    }

    public void setTyre(String tyre) {
        this.tyre = tyre;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getstatusName() {
        if(status != null) {
            return Status.getName(status);
        }
        return "";
    }
}
