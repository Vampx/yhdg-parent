package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.StationRole;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface StationRoleMapper extends MasterMapper {
	public StationRole find(int id);
	int findUnique(@Param("id") Integer id, @Param("roleName") String roleName);
	public List<StationRole> findByStationId(@Param("stationId") String stationId);
	public int findPageCount(StationRole search);
	public List<StationRole> findPageResult(StationRole search);
	public int insert(StationRole role);
	public int update(StationRole role);
	public int delete(int id);
}
