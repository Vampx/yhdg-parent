package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface BatteryParameterMapper extends MasterMapper {
    BatteryParameter find(@Param("id") String id);
}
