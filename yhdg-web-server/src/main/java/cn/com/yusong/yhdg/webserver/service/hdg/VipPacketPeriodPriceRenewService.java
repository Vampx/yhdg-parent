package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPriceRenew;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VipPacketPeriodPriceRenewService extends AbstractService {

    @Autowired
    VipPacketPeriodPriceRenewMapper vipPacketPeriodPriceRenewMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;
    @Autowired
    VipPriceShopMapper vipPriceShopMapper;

    public VipPacketPeriodPriceRenew find(Long id) {
        return vipPacketPeriodPriceRenewMapper.find(id);
    }

    public List<VipPacketPeriodPriceRenew> findList(Long packetPriceId) {
        return vipPacketPeriodPriceRenewMapper.findList(packetPriceId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew) {
        vipPacketPeriodPriceRenew.setCreateTime(new Date());
        vipPacketPeriodPriceRenew.setAgentName(findAgentInfo(vipPacketPeriodPriceRenew.getAgentId()).getAgentName());
        vipPacketPeriodPriceRenewMapper.insert(vipPacketPeriodPriceRenew);
        return DataResult.successResult(vipPacketPeriodPriceRenew);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew) {
        vipPacketPeriodPriceRenew.setAgentName(findAgentInfo(vipPacketPeriodPriceRenew.getAgentId()).getAgentName());
        vipPacketPeriodPriceRenewMapper.update(vipPacketPeriodPriceRenew);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        int total = vipPacketPeriodPriceRenewMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
