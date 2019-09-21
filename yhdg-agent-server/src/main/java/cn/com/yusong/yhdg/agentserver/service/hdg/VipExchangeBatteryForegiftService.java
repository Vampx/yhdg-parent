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
public class VipExchangeBatteryForegiftService extends AbstractService {
	@Autowired
	VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;
	@Autowired
	VipPriceMapper vipPriceMapper;
	@Autowired
	VipPriceCabinetMapper vipPriceCabinetMapper;
	@Autowired
	VipPriceShopMapper vipPriceShopMapper;

	public VipExchangeBatteryForegift find(Long id) {
		return vipExchangeBatteryForegiftMapper.find(id);
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(VipExchangeBatteryForegift entity) {
		if (entity.getReduceMoney() > entity.getMoney()) {
			return ExtResult.failResult("减免金额应小于押金金额");
		}
		VipExchangeBatteryForegift foregift = vipExchangeBatteryForegiftMapper.find(entity.getId());
		if (foregift != null) {
			vipExchangeBatteryForegiftMapper.update(entity);
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
		} else {
			VipExchangeBatteryForegift vipForegift = new VipExchangeBatteryForegift();
			vipForegift.setAgentId(entity.getAgentId());
			vipForegift.setReduceMoney(entity.getReduceMoney());
			vipForegift.setForegiftId(entity.getForegiftId());
			vipForegift.setPriceId(entity.getPriceId());
			vipForegift.setMemo(entity.getMemo());
			vipForegift.setCreateTime(new Date());
			vipExchangeBatteryForegiftMapper.insert(vipForegift);
			VipPrice vipPrice = vipPriceMapper.findByIsActive(vipExchangeBatteryForegiftMapper.findId(), new Date());
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
}
