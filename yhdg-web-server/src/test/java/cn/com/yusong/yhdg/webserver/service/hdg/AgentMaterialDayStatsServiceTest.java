package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import cn.com.yusong.yhdg.webserver.persistence.basic.PlatformAccountInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.webserver.service.basic.AgentInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.PlatformAccountInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.basic.PlatformAccountService;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class AgentMaterialDayStatsServiceTest extends BaseJunit4Test {
	@Autowired
	AgentMaterialDayStatsService service;
	@Autowired
	AgentService agentService;
	@Autowired
	PlatformAccountService platformAccountService;
	@Autowired
	AgentInOutMoneyService agentInOutMoneyService;
	@Autowired
	PlatformAccountInOutMoneyService platformAccountInOutMoneyService;

	@Test
	public void findPage() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
		insertAgentMaterialDayStats(agentMaterialDayStats);

		assertTrue(1 == service.findPage(agentMaterialDayStats).getTotalItems());
		assertTrue(1 == service.findPage(agentMaterialDayStats).getResult().size());
	}

	@Test
	public void findForExcel() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		insertAgent(agent);

		AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
		insertAgentMaterialDayStats(agentMaterialDayStats);

		assertTrue(1 == service.findForExcel(agentMaterialDayStats).size());
	}

	@Test
	public void payMoney() {
		Partner partner = newPartner();
		insertPartner(partner);

		Agent agent = newAgent(partner.getId());
		agent.setBalance(10000);
		insertAgent(agent);

		PlatformAccount platformAccount = newPlatformAccount(agent.getPartnerId());
		platformAccount.setBalance(0);
		insertPlatformAccount(platformAccount);

		AgentMaterialDayStats agentMaterialDayStats = newAgentMaterialDayStats(agent.getId());
		agentMaterialDayStats.setMoney(1000);
		agentMaterialDayStats.setStatus(AgentMaterialDayStats.Status.NOT_PAY.getValue());
		insertAgentMaterialDayStats(agentMaterialDayStats);

		AgentMaterialDayStats agentMaterialDayStats2 = newAgentMaterialDayStats(agent.getId());
		agentMaterialDayStats2.setMoney(1000);
		agentMaterialDayStats2.setStatus(AgentMaterialDayStats.Status.NOT_PAY.getValue());
		insertAgentMaterialDayStats(agentMaterialDayStats2);

		AgentInOutMoney agentInOutMoney = newAgentInOutMoney(agent.getId());
		agentInOutMoney.setBizType(AgentInOutMoney.BizType.OUT_AGENT_PAY_MATERIAL.getValue());
		Date now = new Date();
		String format = DateFormatUtils.format(now, Constant.DATE_FORMAT);
		agentInOutMoney.setStatsDate(format);
		agentInOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
		int agentInOutMoneyResults1 = agentInOutMoneyService.findPage(agentInOutMoney).getResult().size();

		PlatformAccountInOutMoney platformAccountInOutMoney = newPlatformAccountInOutMoney(platformAccount.getId());
		int platformInOutMoneyResults1 = platformAccountInOutMoneyService.findPage(platformAccountInOutMoney).getResult().size();

		int[] ids = {agentMaterialDayStats.getId(), agentMaterialDayStats2.getId()};
		assertTrue(service.payMoney(ids, "admin").isSuccess());

		//运营商减少余额
		Agent dbAgent = agentService.find(agent.getId());
		assertEquals(dbAgent.getBalance().intValue(), agent.getBalance() - agentMaterialDayStats.getMoney() - agentMaterialDayStats2.getMoney());

		//平台账户增加余额
		PlatformAccount dbPlatformAccount = platformAccountService.find(platformAccount.getId());
		assertEquals(dbPlatformAccount.getBalance().intValue(), platformAccount.getBalance() + agentMaterialDayStats.getMoney() + agentMaterialDayStats2.getMoney());

		int agentInOutMoneyResults2 = agentInOutMoneyService.findPage(agentInOutMoney).getResult().size();
		int platformInOutMoneyResults2 = platformAccountInOutMoneyService.findPage(platformAccountInOutMoney).getResult().size();
		//新增运营商流水
		assertEquals(agentInOutMoneyResults1 + 2, agentInOutMoneyResults2);
		//新增平台账户流水
		assertEquals(platformInOutMoneyResults1 + 2, platformInOutMoneyResults2);
		//更新状态
		List<AgentMaterialDayStats> result = service.findPage(agentMaterialDayStats).getResult();
		for (AgentMaterialDayStats materialDayStats : result) {
			if (materialDayStats.getId().equals(agentMaterialDayStats.getId())) {
				assertEquals(AgentMaterialDayStats.Status.PAID.getValue(), materialDayStats.getStatus().intValue());
			}
			if (materialDayStats.getId().equals(agentMaterialDayStats2.getId())) {
				assertEquals(AgentMaterialDayStats.Status.PAID.getValue(), materialDayStats.getStatus().intValue());
			}
		}
	}
}
