package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.zd.RentPeriodOrderAllotMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrderAllot;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.entity.pagination.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class RentPeriodOrderAllotService extends AbstractService {

    @Autowired
    RentPeriodOrderAllotMapper rentPeriodOrderAllotMapper;

    public Page findPageByRecord(BalanceRecord search) throws ParseException {
        String suffix = RentPeriodOrderAllot.getSuffixByString(search.getBalanceDate());
        search.setSuffix(suffix);
        PageRequest pageRequest = new PageRequest(search.getPage(), search.getRows());
        Page page = new Page(pageRequest);
        page.setTotalItems(rentPeriodOrderAllotMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentPeriodOrderAllot> list = rentPeriodOrderAllotMapper.findPageResult(search);
        for (RentPeriodOrderAllot rentPeriodOrderAllot : list) {
            Partner partner = findPartner(rentPeriodOrderAllot.getPartnerId());
            if (partner != null) {
                rentPeriodOrderAllot.setPartnerName(partner.getPartnerName());
            }
            AgentInfo agentInfo = findAgentInfo(rentPeriodOrderAllot.getAgentId());
            if (agentInfo != null) {
                rentPeriodOrderAllot.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }

}
