package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOperateLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryOperateLogMapper extends MasterMapper {
    List<BatteryOperateLog> findList(@Param("offset") int offset, @Param("limit") int limit);
    public int insert(BatteryOperateLog batteryOperateLog);
}
