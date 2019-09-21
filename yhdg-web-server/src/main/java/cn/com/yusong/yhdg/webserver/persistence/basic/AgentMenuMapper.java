package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentMenu;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMenuMapper extends MasterMapper {
    AgentMenu findByMenuName(@Param("menuName") String menuName);
    List<String> findRootsByClientType(@Param("clientType") Integer clientType);
    List<AgentMenu> findAllByClientType(@Param("clientType") Integer clientType);
}
