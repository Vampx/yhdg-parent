package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
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

    public List<VipPacketPeriodPrice> findListByForegiftId(Integer foregiftId, Integer batteryType, Integer agentId, Long priceId) {
        return vipPacketPeriodPriceMapper.findListByForegiftId(foregiftId, batteryType, agentId, priceId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(VipPacketPeriodPrice packetPeriodPrice) {
        packetPeriodPrice.setCreateTime(new Date());
        packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
        vipPacketPeriodPriceMapper.insert(packetPeriodPrice);
        VipExchangeBatteryForegift foregift = vipExchangeBatteryForegiftMapper.findByAgentIdAndForegiftId(packetPeriodPrice.getAgentId(),packetPeriodPrice.getForegiftId(),packetPeriodPrice.getPriceId());
        if (foregift != null){
            VipPrice vipPrice = vipPriceMapper.findByIsActive(foregift.getPriceId(), new Date());
            if (vipPrice != null) {
                List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findListByPriceId(vipPrice.getId());
                for (VipPriceCabinet priceCabinet : vipPriceCabinetList) {
                    updatePrice(foregift.getAgentId(), priceCabinet.getCabinetId());
                }
                List<VipPriceShop> shopList = vipPriceShopMapper.findListByPriceId(vipPrice.getId());
                for (VipPriceShop vipPriceShop : shopList) {
                    updateShopPrice(vipPrice.getId(), vipPriceShop.getShopId());
                }
            }
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(VipPacketPeriodPrice packetPeriodPrice) {
        VipPacketPeriodPrice price = vipPacketPeriodPriceMapper.find(packetPeriodPrice.getId());
        packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
        vipPacketPeriodPriceMapper.update(packetPeriodPrice);
        VipExchangeBatteryForegift foregift = vipExchangeBatteryForegiftMapper.findByAgentIdAndForegiftId(price.getAgentId(),price.getForegiftId(),price.getPriceId());
        if (foregift != null){
            VipPrice vipPrice = vipPriceMapper.findByIsActive(foregift.getPriceId(), new Date());
            if (vipPrice != null) {
                List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findListByPriceId(vipPrice.getId());
                for (VipPriceCabinet priceCabinet : vipPriceCabinetList) {
                    updatePrice(foregift.getAgentId(), priceCabinet.getCabinetId());
                }
                List<VipPriceShop> shopList = vipPriceShopMapper.findListByPriceId(vipPrice.getId());
                for (VipPriceShop vipPriceShop : shopList) {
                    updateShopPrice(vipPrice.getId(), vipPriceShop.getShopId());
                }
            }
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        VipPacketPeriodPrice price = vipPacketPeriodPriceMapper.find(id);
        int total = vipPacketPeriodPriceMapper.delete(id);
        VipExchangeBatteryForegift foregift = vipExchangeBatteryForegiftMapper.findByAgentIdAndForegiftId(price.getAgentId(),price.getForegiftId(),price.getPriceId());
        if (foregift != null){
            VipPrice vipPrice = vipPriceMapper.findByIsActive(foregift.getPriceId(), new Date());
            if (vipPrice != null) {
                List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findListByPriceId(vipPrice.getId());
                for (VipPriceCabinet priceCabinet : vipPriceCabinetList) {
                    updatePrice(foregift.getAgentId(), priceCabinet.getCabinetId());
                }
                List<VipPriceShop> shopList = vipPriceShopMapper.findListByPriceId(vipPrice.getId());
                for (VipPriceShop vipPriceShop : shopList) {
                    updateShopPrice(vipPrice.getId(), vipPriceShop.getShopId());
                }
            }
        }
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
