package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemBatteryTypeMapper extends MasterMapper {
	SystemBatteryType find(@Param("id") int id);

	int findPageCount(SystemBatteryType systemBatteryType);

	List<SystemBatteryType> findPageResult(SystemBatteryType systemBatteryType);

	int insert(SystemBatteryType systemBatteryType);

	int update(SystemBatteryType systemBatteryType);

	int delete(@Param("id") int id);
}
