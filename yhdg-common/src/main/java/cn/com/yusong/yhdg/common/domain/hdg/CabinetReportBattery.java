package cn.com.yusong.yhdg.common.domain.hdg;

import cn.com.yusong.yhdg.common.domain.LongIdEntity;
import cn.com.yusong.yhdg.common.entity.json.DateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;


/**
 * 柜子上报日志
 */

@Setter
@Getter
public class CabinetReportBattery extends LongIdEntity {
    public final static String CABINET_REPORT_BATTERY_TABLE_NAME = "hdg_cabinet_report_battery_";

    Long cabinetReportId;
    String cabinetId;
    String cabinetName;
    String batteryId;
    String batteryCode;
    Date createTime;
    String suffix;

    @JsonSerialize(using = DateTimeSerializer.class)
    public Date getCreateTime(){
        return createTime;
    }
}
