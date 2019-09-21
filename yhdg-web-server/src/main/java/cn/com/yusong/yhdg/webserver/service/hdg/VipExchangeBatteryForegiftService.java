package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipExchangeBatteryForegiftMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceCabinetMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.VipPriceShopMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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

	public VipExchangeBatteryForegift findByAgentIdAndForegiftId(Integer agentId, Long foregiftId, Long priceId) {
		VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.findByAgentIdAndForegiftId(agentId, foregiftId, priceId);
		return vipForegift;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(VipExchangeBatteryForegift entity) {
		if (entity.getId() == 0) {
			VipExchangeBatteryForegift vipForegift = new VipExchangeBatteryForegift();
			vipForegift.setAgentId(entity.getAgentId());
			vipForegift.setReduceMoney(entity.getReduceMoney());
			vipForegift.setForegiftId(entity.getForegiftId());
			vipForegift.setMemo(entity.getMemo());
			vipForegift.setNum(entity.getNum());
			vipForegift.setCreateTime(new Date());
			vipExchangeBatteryForegiftMapper.insert(vipForegift);
			return DataResult.successResult(vipForegift.getId());
		} else {
			vipExchangeBatteryForegiftMapper.update(entity);
			return DataResult.successResult(entity.getId());
		}
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult createOrUpdate(VipExchangeBatteryForegift entity) {
		if (entity.getReduceMoney() > entity.getMoney()) {
			return ExtResult.failResult("减免金额应小于押金金额");
		}
		List<VipExchangeBatteryForegift>  priceList = vipExchangeBatteryForegiftMapper.findByPriceId(entity.getPriceId());
		if (priceList.size() > 0) {
			vipExchangeBatteryForegiftMapper.deleteByPriceId(entity.getPriceId());
		}
		VipExchangeBatteryForegift foregift = vipExchangeBatteryForegiftMapper.find(entity.getId());
		if (foregift != null) {
			vipExchangeBatteryForegiftMapper.update(entity);
		} else {
			VipExchangeBatteryForegift vipForegift = new VipExchangeBatteryForegift();
			vipForegift.setAgentId(entity.getAgentId());
			vipForegift.setReduceMoney(entity.getReduceMoney());
			vipForegift.setForegiftId(entity.getForegiftId());
			vipForegift.setPriceId(entity.getPriceId());
			vipForegift.setMemo(entity.getMemo());
			vipForegift.setNum(entity.getNum());
			vipForegift.setCreateTime(new Date());
			vipExchangeBatteryForegiftMapper.insert(vipForegift);
		}

		return ExtResult.successResult();
	}
}
