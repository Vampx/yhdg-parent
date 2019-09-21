package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
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
public class PacketPeriodPriceRenewService extends AbstractService {

    @Autowired
    PacketPeriodPriceRenewMapper packetPeriodPriceRenewMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;
    @Autowired
    VipPriceShopMapper vipPriceShopMapper;

    public PacketPeriodPriceRenew find(Long id) {
        return packetPeriodPriceRenewMapper.find(id);
    }

    public List<PacketPeriodPriceRenew> findList(Long priceId) {
        return packetPeriodPriceRenewMapper.findList(priceId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(PacketPeriodPriceRenew packetPeriodPriceRenew) {
        packetPeriodPriceRenew.setCreateTime(new Date());
        packetPeriodPriceRenew.setAgentName(findAgentInfo(packetPeriodPriceRenew.getAgentId()).getAgentName());
        packetPeriodPriceRenewMapper.insert(packetPeriodPriceRenew);
        return DataResult.successResult(packetPeriodPriceRenew);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(PacketPeriodPriceRenew packetPeriodPriceRenew) {
        packetPeriodPriceRenew.setAgentName(findAgentInfo(packetPeriodPriceRenew.getAgentId()).getAgentName());
        packetPeriodPriceRenewMapper.update(packetPeriodPriceRenew);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        int total = packetPeriodPriceRenewMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
