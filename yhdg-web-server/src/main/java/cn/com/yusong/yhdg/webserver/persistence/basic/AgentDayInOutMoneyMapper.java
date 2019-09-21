package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDayInOutMoney;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface AgentDayInOutMoneyMapper extends MasterMapper {
    public int findPageCount(AgentDayInOutMoney search);
    public List<AgentDayInOutMoney> findPageResult(AgentDayInOutMoney search);
}
