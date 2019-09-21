package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.PageEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * 设备信号记录
 */
@Setter
@Getter
public class BatterySignal extends PageEntity {
    String batteryId; /*充电桩id*/
    Date createTime;
    Integer currentSignal;
    Integer num;

    public String getBatteryId() {
        return batteryId;
    }

    public void setBatteryId(String batteryId) {
        this.batteryId = batteryId;
    }

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCurrentSignal() {
        return currentSignal;
    }

    public void setCurrentSignal(Integer currentSignal) {
        this.currentSignal = currentSignal;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
