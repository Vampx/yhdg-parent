package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 柜子操作日志
 */
@Setter
@Getter
public class CabinetOperateLog extends LongIdEntity {

    public enum OperateType {
        OPEN_DOOR(1, "开门"),
        CLOSE_DOOR(2, "关门"),
        PUT_BATTERY(3, "放入电池"),
        TAKE_BATTERY(4, "取出电池"),
        NO_ACTIVE(5, "禁用"),
        ACTIVE(6, "激活"),
        ;

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
    public enum OperatorType {
        DISPATCH(1, "调度"),
        CUSTOMER(2, "骑手"),
        PLATFORM(3, "平台"),
        HEARTBEAT(4, "心跳检测"),
        AGENT_CABINET(5, "运营端设备"),
        SHOP_CABINET(6, "门店端设备")
        ;

        private final int value;
        private final String name;

        OperatorType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();
        static {
            for (OperatorType e : OperatorType.values()) {
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


    public String getOperatorTypeName() {
        if(operatorType != null) {
            return OperatorType.getName(operatorType);
        }
        return "";
    }

    public String getOperateTypeName() {
        if(operateType != null) {
            return OperateType.getName(operateType);
        }
        return "";
    }

    Integer agentId;
    String cabinetId;
    String cabinetName;
    String boxNum;
    Integer operatorType;
    Integer operateType;
    String operator;
    String content;
    Date createTime;

    @Transient
    String agentName;

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
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

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public Integer getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(Integer operatorType) {
        this.operatorType = operatorType;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
