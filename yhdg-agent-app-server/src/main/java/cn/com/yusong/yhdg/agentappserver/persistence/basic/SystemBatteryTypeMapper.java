package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface SystemBatteryTypeMapper extends MasterMapper {
	SystemBatteryType find(@Param("id") int id);
	public List<SystemBatteryType> findList(@Param("typeName") String typeName);
}
