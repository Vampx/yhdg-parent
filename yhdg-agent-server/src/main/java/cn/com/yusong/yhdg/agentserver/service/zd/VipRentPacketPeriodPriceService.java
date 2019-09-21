package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.zd.VipRentPacketPeriodPriceMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VipRentPacketPeriodPriceService extends AbstractService {

    @Autowired
    VipRentPacketPeriodPriceMapper vipRentPacketPeriodPriceMapper;

    public VipRentPeriodPrice find(Long id) {
        return vipRentPacketPeriodPriceMapper.find(id);
    }

    public List<VipRentPeriodPrice> findListByForegiftId(Integer foregiftId, Integer batteryType, Integer agentId, Long priceId) {
        return vipRentPacketPeriodPriceMapper.findListByForegiftId(foregiftId, batteryType, agentId, priceId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(VipRentPeriodPrice packetPeriodPrice) {
        packetPeriodPrice.setCreateTime(new Date());
        packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
        vipRentPacketPeriodPriceMapper.insert(packetPeriodPrice);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(VipRentPeriodPrice packetPeriodPrice) {
        packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
        vipRentPacketPeriodPriceMapper.update(packetPeriodPrice);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        int total = vipRentPacketPeriodPriceMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
