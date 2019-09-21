package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.StringIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 维护订单
 */
@Setter
@Getter
public class KeepOrder extends StringIdEntity {

    Integer agentId;
    String takeOrderId; //收电订单Id
    String putOrderId; //投电订单Id
    String takeGroupId;
    String takeGroupName;
    String takeCabinetId;       /*取电柜子id*/
    String takeCabinetName;   /*取电柜子名称*/
    String takeBoxNum;        /*取电箱号*/
    Date takeTime;            /*取电时间*/
    Integer initVolume;       /*初始电量*/
    String putGroupId;
    String putGroupName;
    String putCabinetId;        /*放电柜子id*/
    String putCabinetName;    /*放电柜子名称*/
    String putBoxNum;         /*放电箱号*/
    Date putTime;             /*放电时间*/
    Integer currentVolume;    /*当前电量*/
    String batteryId;//电池Id
    Long takeUserId; //收电用户Id
    String takeUserFullname; //收电用户姓名
    String takeUserMobile; //收电用户手机号
    Long putUserId; //投电用户手机号
    String putUserFullname; //投电用户姓名
    String putUserMobile; //投电用户手机号
    Integer orderStatus;
    Date createTime;

    public enum OrderStatus {
        INIT(1, "未取出"),
        OUT(2,"已取出"),
        IN(3,"已放入"),
        ;

        private final int value;
        private final String name;

        OrderStatus(int value, String name){
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (OrderStatus e : OrderStatus.values()) {
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

    public String getOrderStatusName() {
        if(orderStatus != null) {
            return OrderStatus.getName(orderStatus);
        }
        return "";
    }

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getTakeCabinetId() {
        return takeCabinetId;
    }

    public void setTakeCabinetId(String takeCabinetId) {
        this.takeCabinetId = takeCabinetId;
    }

    public String getTakeCabinetName() {
        return takeCabinetName;
    }

    public void setTakeCabinetName(String takeCabinetName) {
        this.takeCabinetName = takeCabinetName;
    }

    public String getTakeBoxNum() {
        return takeBoxNum;
    }

    public void setTakeBoxNum(String takeBoxNum) {
        this.takeBoxNum = takeBoxNum;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getTakeTime() {
        return takeTime;
    }

    public void setTakeTime(Date takeTime) {
        this.takeTime = takeTime;
    }

    public Integer getInitVolume() {
        return initVolume;
    }

    public void setInitVolume(Integer initVolume) {
        this.initVolume = initVolume;
    }

    public String getPutCabinetId() {
        return putCabinetId;
    }

    public void setPutCabinetId(String putCabinetId) {
        this.putCabinetId = putCabinetId;
    }

    public String getPutCabinetName() {
        return putCabinetName;
    }

    public void setPutCabinetName(String putCabinetName) {
        this.putCabinetName = putCabinetName;
    }

    public String getPutBoxNum() {
        return putBoxNum;
    }

    public void setPutBoxNum(String putBoxNum) {
        this.putBoxNum = putBoxNum;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getPutTime() {
        return putTime;
    }

    public void setPutTime(Date putTime) {
        this.putTime = putTime;
    }

    public Integer getCurrentVolume() {
        return currentVolume;
    }

    public void setCurrentVolume(Integer currentVolume) {
        this.currentVolume = currentVolume;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTakeOrderId() {
        return takeOrderId;
    }

    public void setTakeOrderId(String takeOrderId) {
        this.takeOrderId = takeOrderId;
    }

    public String getPutOrderId() {
        return putOrderId;
    }

    public void setPutOrderId(String putOrderId) {
        this.putOrderId = putOrderId;
    }

    public Long getTakeUserId() {
        return takeUserId;
    }

    public void setTakeUserId(Long takeUserId) {
        this.takeUserId = takeUserId;
    }

    public String getTakeUserFullname() {
        return takeUserFullname;
    }

    public void setTakeUserFullname(String takeUserFullname) {
        this.takeUserFullname = takeUserFullname;
    }

    public String getTakeUserMobile() {
        return takeUserMobile;
    }

    public void setTakeUserMobile(String takeUserMobile) {
        this.takeUserMobile = takeUserMobile;
    }

    public Long getPutUserId() {
        return putUserId;
    }

    public void setPutUserId(Long putUserId) {
        this.putUserId = putUserId;
    }

    public String getPutUserFullname() {
        return putUserFullname;
    }

    public void setPutUserFullname(String putUserFullname) {
        this.putUserFullname = putUserFullname;
    }

    public String getPutUserMobile() {
        return putUserMobile;
    }

    public void setPutUserMobile(String putUserMobile) {
        this.putUserMobile = putUserMobile;
    }

    public String getTakeGroupId() {
        return takeGroupId;
    }

    public void setTakeGroupId(String takeGroupId) {
        this.takeGroupId = takeGroupId;
    }

    public String getTakeGroupName() {
        return takeGroupName;
    }

    public void setTakeGroupName(String takeGroupName) {
        this.takeGroupName = takeGroupName;
    }

    public String getPutGroupId() {
        return putGroupId;
    }

    public void setPutGroupId(String putGroupId) {
        this.putGroupId = putGroupId;
    }

    public String getPutGroupName() {
        return putGroupName;
    }

    public void setPutGroupName(String putGroupName) {
        this.putGroupName = putGroupName;
    }

}
