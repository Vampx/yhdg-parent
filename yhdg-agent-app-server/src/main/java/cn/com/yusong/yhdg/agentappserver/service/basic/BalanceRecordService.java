package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.BalanceRecordMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class BalanceRecordService extends AbstractService {
    @Autowired
    BalanceRecordMapper balanceRecordMapper;

    public BalanceRecord findByBalanceDate(Integer agentId, String statsDate, Integer category) {
        return balanceRecordMapper.findByBalanceDate(agentId, statsDate, category);
    }

    public BalanceRecord findTotalMoney(Integer agentId, Integer bizType, Integer category) {
        return balanceRecordMapper.findTotalMoney(agentId, bizType, category);
    }

    public List<BalanceRecord> findDateRange(Integer agentId, Integer bizType, String beginDate, String endDate, Integer category) {
        return balanceRecordMapper.findDateRange(agentId, bizType, beginDate, endDate, category);
    }
    public BalanceRecord findTotalDateRange(Integer agentId, Integer bizType, String beginDate, String endDate, Integer category) {
        return balanceRecordMapper.findTotalDateRange(agentId, bizType, beginDate, endDate, category);
    }
}
