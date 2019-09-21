package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftDepositOrder;
import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentForegiftInOutMoneyMapper extends MasterMapper{
	int findPageCount(AgentForegiftInOutMoney search);

	List<AgentForegiftInOutMoney> findPageResult(AgentForegiftInOutMoney search);

	int insert(AgentForegiftInOutMoney entity);
}
