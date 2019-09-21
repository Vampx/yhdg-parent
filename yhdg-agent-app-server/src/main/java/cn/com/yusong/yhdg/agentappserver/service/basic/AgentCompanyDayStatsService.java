package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

@Service
public class AgentCompanyDayStatsService extends AbstractService {
	@Autowired
	AgentCompanyDayStatsMapper agentCompanyDayStatsMapper;
	@Autowired
	AgentCompanyMapper agentCompanyMapper;
	@Autowired
	AgentMapper agentMapper;
	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	AgentCompanyIncomeRatioHistoryMapper agentCompanyIncomeRatioHistoryMapper;

	public List<AgentCompanyDayStats> findByCompanyId(String agentCompanyId) {
			return agentCompanyDayStatsMapper.findByCompanyId(agentCompanyId);
	}

	public List<AgentCompanyDayStats> findList(Integer agentId, String agentCompanyId, String statsDate, Integer category) {
		return agentCompanyDayStatsMapper.findList(agentId, agentCompanyId, statsDate, category);
	}

	public List<AgentCompanyDayStats> findDateRange(Integer agentId, String agentCompanyId, String beginDate, String endDate, Integer category) {
		return agentCompanyDayStatsMapper.findDateRange(agentId, agentCompanyId, beginDate, endDate, category);
	}

}
