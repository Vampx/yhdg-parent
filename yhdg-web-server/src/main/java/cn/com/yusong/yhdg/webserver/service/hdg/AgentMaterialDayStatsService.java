package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMaterialDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.AgentBatteryRentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.AgentCabinetRentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.AgentMaterialDayStatsMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AgentMaterialDayStatsService extends AbstractService{
    @Autowired
    AgentMaterialDayStatsMapper agentMaterialDayStatsMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    PlatformAccountMapper platformAccountMapper;
    @Autowired
    PlatformAccountInOutMoneyMapper platformAccountInOutMoneyMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    PartnerInOutMoneyMapper partnerInOutMoneyMapper;
    @Autowired
    AgentCabinetRentRecordMapper agentCabinetRentRecordMapper;
    @Autowired
    AgentBatteryRentRecordMapper agentBatteryRentRecordMapper;
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;

    public Page findPage(AgentMaterialDayStats search) {
        Page page = search.buildPage();
        page.setTotalItems(agentMaterialDayStatsMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<AgentMaterialDayStats> agentMaterialDayStatsList = agentMaterialDayStatsMapper.findPageResult(search);
        for (AgentMaterialDayStats agentMaterialDayStats : agentMaterialDayStatsList) {
            AgentInfo agentInfo = findAgentInfo(agentMaterialDayStats.getAgentId());
            if (agentInfo != null) {
                agentMaterialDayStats.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(agentMaterialDayStatsList);
        return page;
    }

    public List<AgentMaterialDayStats> findForExcel (AgentMaterialDayStats search) {
        List<AgentMaterialDayStats> agentMaterialDayStatsList = agentMaterialDayStatsMapper.findPageResult(search);
        for (AgentMaterialDayStats agentMaterialDayStats : agentMaterialDayStatsList) {
            AgentInfo agentInfo = findAgentInfo(agentMaterialDayStats.getAgentId());
            if (agentInfo != null) {
                agentMaterialDayStats.setAgentName(agentInfo.getAgentName());
            }
        }
        return agentMaterialDayStatsList;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult payMoney(int[] ids, String operator) {
        for (int id : ids) {
            AgentMaterialDayStats agentMaterialDayStats = agentMaterialDayStatsMapper.find(id);
            if(agentMaterialDayStats.getStatus() == AgentMaterialDayStats.Status.PAID.getValue()){
               continue;
            }
            int money = agentMaterialDayStats.getMoney();
            Agent agent = agentMapper.find(agentMaterialDayStats.getAgentId());
            PlatformAccount platformAccount = platformAccountMapper.find(agent.getPartnerId());
            int balance = agent.getBalance();
            Date date = new Date();
            if (balance < money) {
                return ExtResult.failResult("运营商余额不足，请充值");
            } else {
                //运营商减少余额
                if (agentMapper.updateBalance(agent.getId(), -money) > 0) {
                    //新建运营商流水
                    AgentInOutMoney agentInOutMoney = new AgentInOutMoney();
                    agentInOutMoney.setMoney(-money);
                    agentInOutMoney.setBalance(agent.getBalance() - money);
                    agentInOutMoney.setAgentId(agent.getId());
                    agentInOutMoney.setBizId(agentMaterialDayStats.getId().toString());
                    agentInOutMoney.setCreateTime(date);
                    agentInOutMoney.setBizType(AgentInOutMoney.BizType.OUT_AGENT_PAY_MATERIAL.getValue());
                    agentInOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
                    agentInOutMoney.setOperator(operator);
                    agentInOutMoneyMapper.insert(agentInOutMoney);
                }
                //对应平台账户增加余额
                if (platformAccountMapper.updateBalance(agent.getPartnerId(), money) > 0) {
                    //新建平台账户流水
                    PlatformAccountInOutMoney platformAccountInOutMoney = new PlatformAccountInOutMoney();
                    platformAccountInOutMoney.setPlatformAccountId(agent.getPartnerId());
                    platformAccountInOutMoney.setBizType(PlatformAccountInOutMoney.BizType.IN_AGENT_PAY_MATERIAL.getValue());
                    platformAccountInOutMoney.setBizId(agentMaterialDayStats.getId().toString());
                    platformAccountInOutMoney.setType(PlatformAccountInOutMoney.Type.IN.getValue());
                    platformAccountInOutMoney.setMoney(money);
                    platformAccountInOutMoney.setBalance(platformAccount.getBalance() + money);
                    platformAccountInOutMoney.setOperator(operator);
                    platformAccountInOutMoney.setCreateTime(date);
                    platformAccountInOutMoneyMapper.insert(platformAccountInOutMoney);
                }
                //更改设备支出状态
                agentMaterialDayStatsMapper.updateStatus(agentMaterialDayStats.getId(), ConstEnum.PayType.BALANCE.getValue(), date, AgentMaterialDayStats.Status.NOT_PAY.getValue(), AgentMaterialDayStats.Status.PAID.getValue());
                //更新设备租金金额订单支付状态
                agentCabinetRentRecordMapper.updateStatus(agentMaterialDayStats.getId().longValue(), ConstEnum.PayType.BALANCE.getValue(), date, ConstEnum.PayStatus.NO_PAY.getValue(), ConstEnum.PayStatus.PAYD.getValue());
                //更新电池租金金额订单支付状态
                agentBatteryRentRecordMapper.updateStatus(agentMaterialDayStats.getId().longValue(), ConstEnum.PayType.BALANCE.getValue(), date, ConstEnum.PayStatus.NO_PAY.getValue(), ConstEnum.PayStatus.PAYD.getValue());
                //更新客户认证金额订单支付状态
                idCardAuthRecordMapper.updateStatus(agentMaterialDayStats.getId().longValue(), ConstEnum.PayType.BALANCE.getValue(), date, ConstEnum.PayStatus.NO_PAY.getValue(), ConstEnum.PayStatus.PAYD.getValue());
            }
        }
        return ExtResult.successResult();
    }

}
