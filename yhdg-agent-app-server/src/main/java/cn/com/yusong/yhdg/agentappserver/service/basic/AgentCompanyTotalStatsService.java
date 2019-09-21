package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyDayStats;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyTotalStats;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class AgentCompanyTotalStatsService extends AbstractService {
	@Autowired
	AgentCompanyTotalStatsMapper agentCompanyTotalStatsMapper;

	public List<AgentCompanyTotalStats> findList(Integer agentId, String agentCompanyId, Integer category) {
		return agentCompanyTotalStatsMapper.findList(agentId, agentCompanyId, category);
	}

}
