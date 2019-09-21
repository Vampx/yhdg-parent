package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class VipPacketPeriodPriceService extends AbstractService {

    @Autowired
    VipPacketPeriodPriceMapper vipPacketPeriodPriceMapper;
    @Autowired
    VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;
    @Autowired
    VipPriceCabinetMapper vipPriceCabinetMapper;
    @Autowired
    VipPriceShopMapper vipPriceShopMapper;

    public VipPacketPeriodPrice find(Long id) {
        return vipPacketPeriodPriceMapper.find(id);
    }

    public List<VipPacketPeriodPrice> findListByVipForegiftId(Long vipForegiftId) {
        return vipPacketPeriodPriceMapper.findListByVipForegiftId(vipForegiftId);
    }

    public List<VipPacketPeriodPrice> findListByForegiftId(Integer foregiftId, Integer batteryType, Integer agentId, Long priceId, Long vipForegiftId) {
        return vipPacketPeriodPriceMapper.findListByForegiftId(foregiftId, batteryType, agentId, priceId, vipForegiftId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(VipPacketPeriodPrice packetPeriodPrice) {
        if (packetPeriodPrice.getId() == null || packetPeriodPrice.getId() == 0) {
            packetPeriodPrice.setCreateTime(new Date());
            packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
            vipPacketPeriodPriceMapper.insert(packetPeriodPrice);
        } else {
            vipPacketPeriodPriceMapper.update(packetPeriodPrice);
        }
        return DataResult.successResult(packetPeriodPrice);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(VipPacketPeriodPrice packetPeriodPrice) {
        packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
        vipPacketPeriodPriceMapper.update(packetPeriodPrice);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        int total = vipPacketPeriodPriceMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
