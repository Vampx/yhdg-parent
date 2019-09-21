package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.LaxinSettingMapper;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class LaxinSettingService {

    @Autowired
    LaxinSettingMapper laxinSettingMapper;
    @Autowired
    AgentMapper agentMapper;


    public LaxinSetting find(long id) {
        return laxinSettingMapper.find(id);
    }

    public RestResult findList(int agentId, String settingName, int offset, int limit) {
        List<LaxinSetting> list = laxinSettingMapper.findList(agentId, settingName, offset, limit);

        List<Map> data = new ArrayList<Map>();
        for (LaxinSetting laxin : list) {
            Map line = new HashMap();
            line.put("id", laxin.getId());
            line.put("settingName", laxin.getSettingName());
            line.put("laxinMoney", laxin.getLaxinMoney());
            line.put("ticketMoney", laxin.getTicketMoney());
            line.put("ticketDayCount", laxin.getTicketDayCount());
            line.put("packetPeriodMoney", laxin.getPacketPeriodMoney());
            line.put("packetPacketMonth", laxin.getPacketPeriodMonth());
            line.put("isActive", laxin.getIsActive());
            line.put("incomeType", laxin.getIncomeType());
            line.put("intervalDay", laxin.getIntervalDay());
            line.put("type", laxin.getType());

            data.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult create(LaxinSetting laxinSetting) {
        if (laxinSetting.getAgentId() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "请选择运营商");
        }
        Agent agent = agentMapper.find(laxinSetting.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        laxinSetting.setAgentCode(agent.getAgentCode());
        laxinSetting.setAgentName(agent.getAgentName());
        laxinSetting.setCreateTime(new Date());

        if (laxinSetting.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
            laxinSetting.setPacketPeriodMoney(0);
            laxinSetting.setPacketPeriodMonth(0);
        } else if (laxinSetting.getIncomeType() == Laxin.IncomeType.MONTH.getValue()) {
            laxinSetting.setLaxinMoney(0);
        }

        if (laxinSetting.getType() == LaxinSetting.Type.REGISTER.getValue()) {
            List<Long> list = laxinSettingMapper.findByType(agent.getId(), LaxinSetting.Type.REGISTER.getValue());
            for (Long id : list) {
                laxinSettingMapper.updateType(id, LaxinSetting.Type.NORMAL.getValue());
            }
        }
        laxinSettingMapper.insert(laxinSetting);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult update(LaxinSetting laxinSetting) {
        if (laxinSetting.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
            laxinSetting.setPacketPeriodMoney(0);
            laxinSetting.setPacketPeriodMonth(0);
        } else if (laxinSetting.getIncomeType() == Laxin.IncomeType.MONTH.getValue()) {
            laxinSetting.setLaxinMoney(0);
        }

        if (laxinSetting.getType() == LaxinSetting.Type.REGISTER.getValue()) {
            LaxinSetting dbData = laxinSettingMapper.find(laxinSetting.getId());
            if (dbData == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在");
            }

            List<Long> list = laxinSettingMapper.findByType(dbData.getAgentId(), LaxinSetting.Type.REGISTER.getValue());
            for (Long id : list) {
                laxinSettingMapper.updateType(id, LaxinSetting.Type.NORMAL.getValue());
            }
        }
        laxinSettingMapper.update(laxinSetting);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    public RestResult delete(long id) {
        laxinSettingMapper.delete(id);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

}
