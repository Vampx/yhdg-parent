package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.EstateRole;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EstateRoleMapper extends MasterMapper {
	public EstateRole find(int id);
	int findUnique(@Param("id") Integer id, @Param("roleName") String roleName);
	public List<EstateRole> findByEstateId(@Param("estateId") String estateId);
	public int findPageCount(EstateRole search);
	public List<EstateRole> findPageResult(EstateRole search);
	public int insert(EstateRole role);
	public int update(EstateRole role);
	public int delete(int id);
}
