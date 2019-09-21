package cn.com.yusong.yhdg.common.domain.hdg;

import lombok.Getter;
import lombok.Setter;


/**
 * 电池上报日志
 */

@Setter
@Getter
public class BatteryReport extends BatteryParameter {

    public final static String BATTERY_REPORT_TABLE_NAME = "hdg_battery_report_";

    String batteryId;

}
