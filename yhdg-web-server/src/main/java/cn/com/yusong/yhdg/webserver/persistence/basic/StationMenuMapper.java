package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.StationMenu;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface StationMenuMapper extends MasterMapper {
	List<String> findRoots();
	List<StationMenu> findAll();
}
