package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ShopRolePerm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopRolePermMapper extends MasterMapper {
	List<String> findAll(@Param("roleId") Integer roleId);
	int insert(ShopRolePerm shopRolePerm);
	int delete(@Param("roleId") Integer roleId);
}
