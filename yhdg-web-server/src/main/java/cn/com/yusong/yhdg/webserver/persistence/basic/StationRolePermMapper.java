package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.StationRolePerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StationRolePermMapper extends MasterMapper {
	List<String> findAll(@Param("roleId") Integer roleId);
	int insert(StationRolePerm stationRolePerm);
	int delete(@Param("roleId") Integer roleId);
}
