package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/8.
 */
public interface BatteryOrderRefundMapper extends MasterMapper {
    public int insert(BatteryOrderRefund batteryOrderRefund);
}
