package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class ExchangeBatteryForegiftService extends AbstractService {
	@Autowired
	ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
	@Autowired
	VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;
	@Autowired
	PacketPeriodPriceMapper packetPeriodPriceMapper;
	@Autowired
	CustomerForegiftOrderMapper customerForegiftOrderMapper;
	@Autowired
	ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
	@Autowired
	ExchangeInstallmentDetailMapper exchangeInstallmentDetailMapper;
	@Autowired
	CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
	@Autowired
	AgentBatteryTypeMapper agentBatteryTypeMapper;
	@Autowired
	CabinetMapper cabinetMapper;
	@Autowired
	VipPriceShopMapper vipPriceShopMapper;


	public ExchangeBatteryForegift find(Long foregiftId) {
		return exchangeBatteryForegiftMapper.find(foregiftId);
	}

	public List<ExchangeBatteryForegift> findListByBatteryType(Integer batteryType, Integer agentId) {
		return exchangeBatteryForegiftMapper.findListByBatteryType(batteryType,agentId);
	}

	public List<ExchangeBatteryForegift> findListByVipBatteryTypeAdd(Integer batteryType, Integer agentId, Long vipExchangeId) {
		List<ExchangeBatteryForegift> list = exchangeBatteryForegiftMapper.findListByBatteryType(batteryType, agentId);
		for (ExchangeBatteryForegift foregift : list) {
			foregift.setForegiftId(foregift.getId());
		}
		return list;
	}

	public List<ExchangeBatteryForegift> findListByVipBatteryType(Integer batteryType, Integer agentId,Long priceId) {
		List<ExchangeBatteryForegift> list = exchangeBatteryForegiftMapper.findListByBatteryType(batteryType,agentId);
		for (ExchangeBatteryForegift foregift : list) {
			VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.findByAgentIdAndForegiftId(agentId, foregift.getId(), priceId);
			if (vipForegift != null && vipForegift.getReduceMoney() != null) {
				foregift.setReduceMoney(vipForegift.getReduceMoney());
				foregift.setVipExchangeId(vipForegift.getId());
			} else {
				foregift.setReduceMoney(0);
			}
			foregift.setForegiftId(foregift.getId());
		}
		return list;
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult create(ExchangeBatteryForegift exchangeBatteryForegift) {
		exchangeBatteryForegiftMapper.insert(exchangeBatteryForegift);
		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult update(ExchangeBatteryForegift exchangeBatteryForegift) {
		exchangeBatteryForegiftMapper.update(exchangeBatteryForegift.getMoney(), exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getAgentId(), exchangeBatteryForegift.getMemo(), exchangeBatteryForegift.getId());
		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult delete(Long foregiftId) {
		ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
		//无法删除的押金状态
		List<Integer> foregiftList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
				CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
		List<CustomerForegiftOrder> customerForegiftOrderList = customerForegiftOrderMapper.findByForegiftIdAndStatus(exchangeBatteryForegift.getId(), foregiftList);
		if (customerForegiftOrderList.size() > 0) {
			return ExtResult.failResult("存在使用中的押金，无法删除");
		}
		int total = exchangeBatteryForegiftMapper.delete(foregiftId);
		packetPeriodPriceMapper.deleteByForegiftId(foregiftId.intValue());
		if (total == 0) {
			return ExtResult.failResult("操作失败！");
		}
		return ExtResult.successResult();
	}
}
