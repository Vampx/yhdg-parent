package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentShopBalanceRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentShopBalanceRecordMapper extends MasterMapper {

	public int findPageCount(AgentShopBalanceRecord search);
	public List<AgentShopBalanceRecord> findPageResult(AgentShopBalanceRecord search);
	int insert(AgentShopBalanceRecord agentShopBalanceRecord);
	long findMaxId();
}
