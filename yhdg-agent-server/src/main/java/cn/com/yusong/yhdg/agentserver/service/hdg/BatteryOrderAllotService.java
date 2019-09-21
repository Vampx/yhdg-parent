package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.BalanceRecord;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderAllot;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.BatteryOrderAllotMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.PacketPeriodOrderAllotMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.entity.pagination.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class BatteryOrderAllotService extends AbstractService{

    @Autowired
    BatteryOrderAllotMapper batteryOrderAllotMapper;

    public Page findPageByRecord(BalanceRecord search) throws ParseException {
        String suffix = BatteryOrderAllot.getSuffixByString(search.getBalanceDate());
        search.setSuffix(suffix);
        PageRequest pageRequest = new PageRequest(search.getPage(), search.getRows());
        Page page = new Page(pageRequest);
        page.setTotalItems(batteryOrderAllotMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<BatteryOrderAllot> list = batteryOrderAllotMapper.findPageResult(search);
        for (BatteryOrderAllot batteryOrderAllot : list) {
            Partner partner = findPartner(batteryOrderAllot.getPartnerId());
            if (partner != null) {
                batteryOrderAllot.setPartnerName(partner.getPartnerName());
            }
            AgentInfo agentInfo = findAgentInfo(batteryOrderAllot.getAgentId());
            if (agentInfo != null) {
                batteryOrderAllot.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }

}
