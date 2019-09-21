package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.LaxinMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.LaxinRecordMapper;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LaxinService {

    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    AgentMapper agentMapper;


    public Laxin find(long id) {
        return laxinMapper.find(id);
    }

    public RestResult findList(int agentId, String mobile, int offset, int limit) {
        List<Laxin> list = laxinMapper.findList(agentId, mobile, offset, limit);

        List<Map> data = new ArrayList<Map>();
        for (Laxin laxin : list) {
            Map line = new HashMap();
            line.put("id", laxin.getId());
            line.put("mobile", laxin.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            line.put("laxinMoney", laxin.getLaxinMoney());
            line.put("ticketMoney", laxin.getTicketMoney());
            line.put("ticketDayCount", laxin.getTicketDayCount());
            line.put("packetPeriodMoney", laxin.getPacketPeriodMoney());
            line.put("packetPacketMonth", laxin.getPacketPeriodMonth());
            line.put("isActive", laxin.getIsActive());
            line.put("incomeType", laxin.getIncomeType());
            line.put("intervalDay", laxin.getIntervalDay());
            data.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    public List<Laxin> findByMobilePartner(int partnerId, String mobile) {
        return laxinMapper.findByMobilePartner(partnerId, mobile);
    }

    public RestResult create(Laxin laxin) {
        if (laxin.getAgentId() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "请选择运营商");
        }
        Agent agent = agentMapper.find(laxin.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        laxin.setPartnerId(agent.getPartnerId());
        laxin.setAgentCode(agent.getAgentCode());
        laxin.setAgentName(agent.getAgentName());
        laxin.setPassword(laxin.getPassword());
        laxin.setCreateTime(new Date());

        if (laxin.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
            laxin.setPacketPeriodMoney(0);
            laxin.setPacketPeriodMonth(0);
        } else if (laxin.getIncomeType() == Laxin.IncomeType.MONTH.getValue()) {
            laxin.setLaxinMoney(0);
        }

        if (laxinMapper.findByMobile(agent.getId(), laxin.getMobile()) != null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号已注册");
        }

        laxinMapper.insert(laxin);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    public RestResult update(Laxin laxin) {
        if (laxin.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
            laxin.setPacketPeriodMoney(0);
            laxin.setPacketPeriodMonth(0);
        } else if (laxin.getIncomeType() == Laxin.IncomeType.MONTH.getValue()) {
            laxin.setLaxinMoney(0);
        }
        laxinMapper.update(laxin);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    public RestResult delete(long id) {
        if (laxinRecordMapper.findExistByLaxinId(id) > 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "已经产生了拉新记录，无法删除");
        }
        laxinMapper.delete(id);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

}
