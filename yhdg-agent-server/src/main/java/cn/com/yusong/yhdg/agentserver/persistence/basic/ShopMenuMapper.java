package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.ShopMenu;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface ShopMenuMapper extends MasterMapper {
	List<String> findRoots();
	List<ShopMenu> findAll();
}
