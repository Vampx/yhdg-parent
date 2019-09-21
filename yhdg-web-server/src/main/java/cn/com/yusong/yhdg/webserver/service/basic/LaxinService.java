package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.LaxinMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.LaxinRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.PartnerMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class LaxinService {

    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PartnerMapper partnerMapper;


    public Laxin find(long id) {
        return laxinMapper.find(id);
    }

    public Page findPage(Laxin search) {
        Page page = search.buildPage();
        page.setTotalItems(laxinMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Laxin> laxinList = laxinMapper.findPageResult(search);
        for (Laxin laxin : laxinList) {
            Partner partner = partnerMapper.find(laxin.getPartnerId());
            if (partner != null) {
                laxin.setPartnerName(partner.getPartnerName());
            }
        }
        page.setResult(laxinList);
        return page;
    }

    public ExtResult create(Laxin laxin) {
        if (laxin.getAgentId() == null) {
            return ExtResult.failResult("请选择运营商");
        }
        Agent agent = agentMapper.find(laxin.getAgentId());
        if (agent == null) {
            return ExtResult.failResult("运营商不存在");
        }
        laxin.setPartnerId(agent.getPartnerId());
        laxin.setAgentCode(agent.getAgentCode());
        laxin.setAgentName(agent.getAgentName());
        laxin.setPassword(CodecUtils.password(laxin.getPassword()));
        laxin.setCreateTime(new Date());

        if (laxin.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
            laxin.setPacketPeriodMoney(0);
            laxin.setPacketPeriodMonth(0);
        } else if (laxin.getIncomeType() == Laxin.IncomeType.MONTH.getValue()) {
            laxin.setLaxinMoney(0);
        }

        if (laxinMapper.findByMobile(agent.getId(), laxin.getMobile()) != null) {
            return ExtResult.failResult("手机号已注册");
        }

        laxinMapper.insert(laxin);
        return ExtResult.successResult();
    }

    public ExtResult update(Laxin laxin) {
        if (StringUtils.isNotEmpty(laxin.getPassword())) {
            laxin.setPassword(CodecUtils.password(laxin.getPassword()));
        }
        if (laxin.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
            laxin.setPacketPeriodMoney(0);
            laxin.setPacketPeriodMonth(0);
        } else if (laxin.getIncomeType() == Laxin.IncomeType.MONTH.getValue()) {
            laxin.setLaxinMoney(0);
        }
        laxinMapper.update(laxin);
        return ExtResult.successResult();
    }

    public ExtResult delete(long id) {
        if(laxinRecordMapper.findExistByLaxinId(id) > 0) {
            return ExtResult.failResult("已经产生了拉新记录，无法删除");
        }
        laxinMapper.delete(id);
        return ExtResult.successResult();
    }

}
