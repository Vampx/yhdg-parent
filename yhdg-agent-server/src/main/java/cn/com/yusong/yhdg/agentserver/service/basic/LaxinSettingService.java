package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.LaxinSettingMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.PartnerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LaxinSettingService {

    @Autowired
    LaxinSettingMapper laxinSettingMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;

    public LaxinSetting find(long id) {
        return laxinSettingMapper.find(id);
    }

    public Page findPage(LaxinSetting search) {
        Page page = search.buildPage();
        page.setTotalItems(laxinSettingMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<LaxinSetting> laxinList = laxinSettingMapper.findPageResult(search);
        page.setResult(laxinList);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(LaxinSetting laxinSetting) {
        if (laxinSetting.getAgentId() == null) {
            return ExtResult.failResult("请选择运营商");
        }
        Agent agent = agentMapper.find(laxinSetting.getAgentId());
        if (agent == null) {
            return ExtResult.failResult("运营商不存在");
        }
        laxinSetting.setAgentCode(agent.getAgentCode());
        laxinSetting.setAgentName(agent.getAgentName());
        laxinSetting.setCreateTime(new Date());

        if (laxinSetting.getIncomeType() == LaxinSetting.IncomeType.TIMES.getValue()) {
            laxinSetting.setPacketPeriodMoney(0);
            laxinSetting.setPacketPeriodMonth(0);
        } else if (laxinSetting.getIncomeType() == LaxinSetting.IncomeType.MONTH.getValue()) {
            laxinSetting.setLaxinMoney(0);
        }

        if (laxinSetting.getType() == LaxinSetting.Type.REGISTER.getValue()) {
            List<Long> list = laxinSettingMapper.findByType(agent.getId(), LaxinSetting.Type.REGISTER.getValue());
            for (Long id : list) {
                laxinSettingMapper.updateType(id, LaxinSetting.Type.NORMAL.getValue());
            }
        }
        laxinSettingMapper.insert(laxinSetting);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(LaxinSetting laxinSetting) {
        if (laxinSetting.getIncomeType() == LaxinSetting.IncomeType.TIMES.getValue()) {
            laxinSetting.setPacketPeriodMoney(0);
            laxinSetting.setPacketPeriodMonth(0);
        } else if (laxinSetting.getIncomeType() == LaxinSetting.IncomeType.MONTH.getValue()) {
            laxinSetting.setLaxinMoney(0);
        }

        if (laxinSetting.getType() == LaxinSetting.Type.REGISTER.getValue()) {
            LaxinSetting dbData = laxinSettingMapper.find(laxinSetting.getId());
            if (dbData == null) {
                return ExtResult.failResult("记录不存在");
            }

            List<Long> list = laxinSettingMapper.findByType(dbData.getAgentId(), LaxinSetting.Type.REGISTER.getValue());
            for (Long id : list) {
                laxinSettingMapper.updateType(id, LaxinSetting.Type.NORMAL.getValue());
            }
        }
        laxinSettingMapper.update(laxinSetting);
        return ExtResult.successResult();
    }

    public ExtResult delete(long id) {
        laxinSettingMapper.delete(id);
        return ExtResult.successResult();
    }

}
