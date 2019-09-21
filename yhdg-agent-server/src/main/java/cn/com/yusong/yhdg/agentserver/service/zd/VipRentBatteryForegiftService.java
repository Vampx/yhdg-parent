package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.zd.VipRentBatteryForegiftMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.VipRentBatteryForegift;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class VipRentBatteryForegiftService extends AbstractService {
	@Autowired
	VipRentBatteryForegiftMapper vipRentBatteryForegiftMapper;

	public VipRentBatteryForegift find(Long id) {
		return vipRentBatteryForegiftMapper.find(id);
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(VipRentBatteryForegift entity) {
		if (entity.getReduceMoney() > entity.getMoney()) {
			return ExtResult.failResult("减免金额应小于押金金额");
		}
		VipRentBatteryForegift foregift = vipRentBatteryForegiftMapper.find(entity.getId());
		if (foregift != null) {
			vipRentBatteryForegiftMapper.update(entity);
		} else {
			VipRentBatteryForegift vipForegift = new VipRentBatteryForegift();
			vipForegift.setAgentId(entity.getAgentId());
			vipForegift.setReduceMoney(entity.getReduceMoney());
			vipForegift.setForegiftId(entity.getForegiftId());
			vipForegift.setPriceId(entity.getPriceId());
			vipForegift.setMemo(entity.getMemo());
			vipForegift.setCreateTime(new Date());
			vipRentBatteryForegiftMapper.insert(vipForegift);
		}

		return ExtResult.successResult();
	}
}
