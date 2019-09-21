package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentDayBalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.DayBalanceRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentDayBalanceRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by chen on 2017/7/9.
 */
@Service
public class AgentDayBalanceRecordService {
    @Autowired
    AgentDayBalanceRecordMapper agentDayBalanceRecordMapper;

    public AgentDayBalanceRecord find(long id) {
        return agentDayBalanceRecordMapper.find(id);
    }

    public List<AgentDayBalanceRecord> findBayagentlist(int agentId, Integer bizType){
        return agentDayBalanceRecordMapper.findBayagentlist(agentId, bizType);
    }

    public Page findPage(AgentDayBalanceRecord search) {
        Page page = search.buildPage();
        page.setTotalItems(agentDayBalanceRecordMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(agentDayBalanceRecordMapper.findPageResult(search));
        return page;
    }

    public ExtResult confirm(Long[] ids, Date confirmTime, String confirmUser) {
        int effect = 0;
        for (Long id : ids) {
            effect += agentDayBalanceRecordMapper.confirm(id, DayBalanceRecord.Status.WAIT_CONFIRM.getValue(), DayBalanceRecord.Status.CONFIRM_OK_BY_WEIXINMP.getValue(), confirmTime, confirmUser);
        }
        return ExtResult.successResult(String.format("成功确认%d条", effect));
    }


    public ExtResult confirmStatus(Long[] ids, Date confirmTime, String confirmUser) {
        int effect = 0;
        for (Long id : ids) {
            effect += agentDayBalanceRecordMapper.confirm(id, DayBalanceRecord.Status.WAIT_CONFIRM.getValue(), DayBalanceRecord.Status.CONFIRM_OK_BY_OFFLINE.getValue(), confirmTime, confirmUser);
        }
        return ExtResult.successResult(String.format("成功确认%d条", effect));
    }
}
