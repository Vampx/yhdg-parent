package cn.com.yusong.yhdg.webserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.Menu;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface MenuMapper extends MasterMapper {
    List<String> findRoots();
    List<Menu> findAll();
}
