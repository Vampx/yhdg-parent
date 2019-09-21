package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.annotation.Transient;
import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 柜子Memcached日志
 */
@Setter
@Getter
public class CabinetMemcachedLog  implements Serializable {
    Integer agentId;
    String fromService;
    String fromCabinetId;
    String fromBoxNum;
    String fromBatteryId;
    String oldBatteryOrder;

    String toService;
    String toCabinetId;
    String toBoxNum;
    String toBatteryId;
    String newBatteryOrder;

    Long customerId;
    String customerName;

    @Override
    public String toString() {
        return "CabinetMemcachedLog{" +
                "agentId=" + agentId +
                ", fromService='" + fromService + '\'' +
                ", fromCabinetId='" + fromCabinetId + '\'' +
                ", fromBoxNum='" + fromBoxNum + '\'' +
                ", fromBatteryId='" + fromBatteryId + '\'' +
                ", oldBatteryOrder='" + oldBatteryOrder + '\'' +
                ", toService='" + toService + '\'' +
                ", toCabinetId='" + toCabinetId + '\'' +
                ", toBoxNum='" + toBoxNum + '\'' +
                ", toBatteryId='" + toBatteryId + '\'' +
                ", newBatteryOrder='" + newBatteryOrder + '\'' +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                '}';
    }
}
