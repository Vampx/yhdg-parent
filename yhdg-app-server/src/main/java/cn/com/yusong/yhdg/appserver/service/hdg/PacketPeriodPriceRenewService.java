package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.PacketPeriodPriceRenewMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPriceRenew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacketPeriodPriceRenewService extends AbstractService {

    @Autowired
    PacketPeriodPriceRenewMapper packetPeriodPriceRenewMapper;

    public PacketPeriodPriceRenew find(Long id) {
        return packetPeriodPriceRenewMapper.find(id);
    }

    public List<PacketPeriodPriceRenew> findList(Integer agentId, Integer batteryType, Long foregiftId) {
        return packetPeriodPriceRenewMapper.findList(agentId, batteryType, foregiftId);
    }

}
