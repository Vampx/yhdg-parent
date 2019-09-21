package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.VipPacketPeriodPriceRenewMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPriceRenew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipPacketPeriodPriceRenewService extends AbstractService {

    @Autowired
    VipPacketPeriodPriceRenewMapper vipPacketPeriodPriceRenewMapper;

    public VipPacketPeriodPriceRenew find(Long id) {
        return vipPacketPeriodPriceRenewMapper.find(id);
    }

    public List<VipPacketPeriodPriceRenew> findList(Integer agentId, Integer batteryType, Long foregiftId) {
        return vipPacketPeriodPriceRenewMapper.findList(agentId, batteryType, foregiftId);
    }

}
