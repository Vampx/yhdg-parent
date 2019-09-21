package cn.com.yusong.yhdg.common.domain.hdg;


import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.*;

/**
 * 故障反馈
 */
@Setter
@Getter
public class FaultFeedback extends LongIdEntity {

    public enum HandleStatus {
        UNHANDLED(1, "未处理"),
        HANDLED(2, "已处理");

        private final int value;
        private final String name;

        HandleStatus(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (HandleStatus e : HandleStatus.values()) {
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


    public String getHandleStatusName() {
        if (handleStatus != null) {
            return HandleStatus.getName(handleStatus);
        }
        return "";
    }

    public enum FaultType {
        CABINET_FAULT(1, "换电柜报修"),
        BATTERY_FAULT(2, "电池报修"),
        OTHER_FAULT(3, "其他故障");

        private final int value;
        private final String name;

        FaultType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (FaultType e : FaultType.values()) {
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

    public static List<String> getFaultNames(int type) {
        List<String> strings = new ArrayList<String>();
        FaultName[] faultNames = FaultName.values();
        for (FaultName faultName : faultNames) {
            if (faultName.getType() == type) {
                strings.add(faultName.getName());
            }
        }
        return strings;
    }

    public enum FaultName {
        CABINET_FAULT_1(1, "柜门关不上"),
        CABINET_FAULT_2(1, "柜门打不开"),
        CABINET_FAULT_3(1, "扫码没反应"),
        CABINET_FAULT_4(1, "无法换电"),
        BATTERY_FAULT_1(2, "显示电量与实际不符"),
        BATTERY_FAULT_2(2, "耗电过快"),
        OTHER_FAULT_1(3, "账号无法进行换电"),
        OTHER_FAULT_2(3, "地图不显示附近换电柜"),;

        private final int type;
        private final String name;

        FaultName(int type, String name) {
            this.type = type;
            this.name = name;
        }

        private static Map<Integer, String> map = new HashMap<Integer, String>();

        static {
            for (FaultName e : FaultName.values()) {
                map.put(e.getType(), e.getName());
            }
        }

        public int getType() {
            return type;
        }

        public String getName() {
            return name;
        }
    }

    Integer agentId; //运营商ID
    String cabinetId;
    String cabinetName;
    String cabinetAddress;
    String batteryId;
    String content; //反馈内容
    Integer faultType; //柜子故障
    String faultName; //柜子故障名称
    String memo; //柜子故障备注
    String photoPath1; //
    String photoPath2; //
    String photoPath3; //
    String photoPath4; //
    String photoPath5; //
    String photoPath6; //
    long customerId;//客户id
    String customerMobile; //客户手机
    String customerName; //客户姓名
    String handlerName; //处理人
    Date handleTime;//处理时间
    String handleResult; //处理结果
    Integer handleStatus; //处理状态
    Date createTime; //创建时间

    String agentName; //运营商名称
    Integer defaultHandleStatus, defaultFaultType;

    public String getFaultTypeName() {
        if (faultType != null) {
            return FaultType.getName(faultType);
        }
        return "";
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }
}
