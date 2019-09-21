package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOperateLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryOperateLogMapper extends MasterMapper {
    public BatteryOperateLog findLastByCustomer(@Param("customerId") long customerId);
    public int insert(BatteryOperateLog batteryOperateLog);
}
