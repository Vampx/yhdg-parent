package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyIncomeRatioHistoryMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyIncomeRatioHistory;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AgentCompanyIncomeRatioHistoryService extends AbstractService {
	@Autowired
	AgentCompanyIncomeRatioHistoryMapper agentCompanyIncomeRatioHistoryMapper;

	public String exist(String suffix) {
		return agentCompanyIncomeRatioHistoryMapper.exist(suffix);
	}

	public AgentCompanyIncomeRatioHistory find(String statsDate, Integer agentId, String agentCompanyId, Integer orgType, String suffix) {
		return agentCompanyIncomeRatioHistoryMapper.find(statsDate, agentId, agentCompanyId, orgType, suffix);
	}

}
