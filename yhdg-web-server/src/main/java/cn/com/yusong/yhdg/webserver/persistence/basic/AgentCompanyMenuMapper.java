package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyMenu;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentCompanyMenuMapper extends MasterMapper {
	List<String> findRoots();
	List<AgentCompanyMenu> findAll();
}
