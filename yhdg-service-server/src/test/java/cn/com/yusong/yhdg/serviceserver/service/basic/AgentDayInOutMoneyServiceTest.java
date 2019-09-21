package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryTypeIncomeRatio;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.serviceserver.BaseJunit4Test;
import cn.com.yusong.yhdg.serviceserver.service.hdg.CabinetService;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.Date;


public class AgentDayInOutMoneyServiceTest extends BaseJunit4Test {
    @Autowired
    AgentDayInOutMoneyService agentDayInOutMoneyService;

    @Test
    public void stats_1() throws ParseException {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        AgentInOutMoney agentInOutMoney = newAgentInOutMoney(agent.getId());
        agentInOutMoney.setMoney(100);
        agentInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
        agentInOutMoney.setCreateTime(DateUtils.addDays(new Date(), -1));
        insertAgentInOutMoney(agentInOutMoney);

        AgentInOutMoney agentInOutMoney1 = newAgentInOutMoney(agent.getId());
        agentInOutMoney1.setMoney(100);
        agentInOutMoney1.setType(AgentInOutMoney.Type.OUT.getValue());
        agentInOutMoney1.setCreateTime(DateUtils.addDays(new Date(), -1));
        insertAgentInOutMoney(agentInOutMoney1);


        //今天统计昨天数据
        agentDayInOutMoneyService.stats(DateUtils.addDays(new Date(), -1));
        agentDayInOutMoneyService.stats(DateUtils.addDays(new Date(), -1));

        //收入
        assertEquals(100, jdbcTemplate.queryForInt("select agent_in_money from bas_agent_day_in_out_money where agent_id = ?", agent.getId()));
        //支出
        assertEquals(100, jdbcTemplate.queryForInt("select agent_out_money from bas_agent_day_in_out_money where agent_id = ?", agent.getId()));

    }



}
