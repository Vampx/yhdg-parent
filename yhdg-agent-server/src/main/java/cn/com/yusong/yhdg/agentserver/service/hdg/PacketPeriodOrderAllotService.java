package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.PacketPeriodOrderAllotMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.entity.pagination.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;

@Service
public class PacketPeriodOrderAllotService extends AbstractService{

    @Autowired
    PacketPeriodOrderAllotMapper packetPeriodOrderAllotMapper;

    public Page findPageByRecord(BalanceRecord search) throws ParseException {
        String suffix = PacketPeriodOrderAllot.getSuffixByString(search.getBalanceDate());
        search.setSuffix(suffix);
        PageRequest pageRequest = new PageRequest(search.getPage(), search.getRows());
        Page page = new Page(pageRequest);
        page.setTotalItems(packetPeriodOrderAllotMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<PacketPeriodOrderAllot> list = packetPeriodOrderAllotMapper.findPageResult(search);
        for (PacketPeriodOrderAllot packetPeriodOrderAllot : list) {
            Partner partner = findPartner(packetPeriodOrderAllot.getPartnerId());
            if (partner != null) {
                packetPeriodOrderAllot.setPartnerName(partner.getPartnerName());
            }
            AgentInfo agentInfo = findAgentInfo(packetPeriodOrderAllot.getAgentId());
            if (agentInfo != null) {
                packetPeriodOrderAllot.setAgentName(agentInfo.getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }

}
