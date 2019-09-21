package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ShopRole;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopRoleMapper extends MasterMapper {
	public ShopRole find(int id);
	int findUnique(@Param("id") Integer id, @Param("roleName") String roleName);
	public List<ShopRole> findByShopId(@Param("shopId") String shopId);
	public int findPageCount(ShopRole search);
	public List<ShopRole> findPageResult(ShopRole search);
	public int insert(ShopRole role);
	public int update(ShopRole role);
	public int delete(int id);
}
