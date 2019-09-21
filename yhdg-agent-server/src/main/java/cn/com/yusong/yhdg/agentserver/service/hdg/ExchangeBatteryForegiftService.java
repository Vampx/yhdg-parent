package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
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

	public List<ExchangeBatteryForegift> findListByVipBatteryType(Integer batteryType, Integer agentId,Long priceId) {
		List<ExchangeBatteryForegift> list = exchangeBatteryForegiftMapper.findListByBatteryType(batteryType,agentId);
		for (ExchangeBatteryForegift foregift : list) {
			VipExchangeBatteryForegift vipForegift = vipExchangeBatteryForegiftMapper.findByAgentIdAndForegiftId(agentId, Long.valueOf(foregift.getId()), priceId);
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
		AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getAgentId());
		if (agentBatteryType != null) {
			List<Cabinet> cabinetList = cabinetMapper.findListByAgentAndBatteryType(agentBatteryType.getAgentId(), agentBatteryType.getBatteryType());
			for (Cabinet cabinet : cabinetList) {
				updatePrice(cabinet.getAgentId(), cabinet.getId());
				if (cabinet.getShopId() != null) {
					VipPriceShop vipPriceShop = vipPriceShopMapper.findByShopId(cabinet.getShopId());
					if (vipPriceShop != null) {
						updateShopPrice(vipPriceShop.getId(), cabinet.getShopId());
					}else {
						updateShopPriceByCabint(cabinet.getShopId());
					}
				}
			}
		}
		return ExtResult.successResult();
	}

	@Transactional(rollbackFor = Throwable.class)
	public ExtResult update(ExchangeBatteryForegift exchangeBatteryForegift) {
		ExchangeBatteryForegift dbExchangeBatteryForegift = exchangeBatteryForegiftMapper.find(exchangeBatteryForegift.getId());
		if (dbExchangeBatteryForegift.getMoney().intValue() != exchangeBatteryForegift.getMoney()) {
			//删除押金对应的分期设置
			List<ExchangeInstallmentSetting> exchangeInstallmentSettingList = exchangeInstallmentSettingMapper.findByForegiftId(exchangeBatteryForegift.getId());
			exchangeInstallmentSettingMapper.deleteByForegiftId(exchangeBatteryForegift.getId());
			//删除押金对应的分期详情
			//清空分期记录的settingId
			for (ExchangeInstallmentSetting exchangeInstallmentSetting : exchangeInstallmentSettingList) {
				exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
				customerInstallmentRecordMapper.clearExchangeSettingId(exchangeInstallmentSetting.getId());
			}
		}
		exchangeBatteryForegiftMapper.update(exchangeBatteryForegift.getMoney(), exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getAgentId(), exchangeBatteryForegift.getMemo(), exchangeBatteryForegift.getId());
		AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getAgentId());
		if (agentBatteryType != null) {
			List<Cabinet> cabinetList = cabinetMapper.findListByAgentAndBatteryType(agentBatteryType.getAgentId(), agentBatteryType.getBatteryType());
			for (Cabinet cabinet : cabinetList) {
				updatePrice(cabinet.getAgentId(), cabinet.getId());
				if (cabinet.getShopId() != null) {
					VipPriceShop vipPriceShop = vipPriceShopMapper.findByShopId(cabinet.getShopId());
					if (vipPriceShop != null) {
						updateShopPrice(vipPriceShop.getId(), cabinet.getShopId());
					}else {
						updateShopPriceByCabint(cabinet.getShopId());
					}
				}
			}
		}
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
		//删除押金对应的分期设置
		List<ExchangeInstallmentSetting> exchangeInstallmentSettingList = exchangeInstallmentSettingMapper.findByForegiftId(exchangeBatteryForegift.getId());
		exchangeInstallmentSettingMapper.deleteByForegiftId(exchangeBatteryForegift.getId());
		//删除押金对应的分期详情
		//清空分期记录的settingId
		for (ExchangeInstallmentSetting exchangeInstallmentSetting : exchangeInstallmentSettingList) {
			exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
			customerInstallmentRecordMapper.clearExchangeSettingId(exchangeInstallmentSetting.getId());
		}
		//删除租金对应的分期设置
		List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceMapper.findListByForegiftId(foregiftId.intValue(), exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getAgentId());
		for (PacketPeriodPrice packetPeriodPrice : packetPeriodPriceList) {
			List<ExchangeInstallmentSetting> exchangeInstallmentSettingList1 = exchangeInstallmentSettingMapper.findByPacketId(packetPeriodPrice.getId());
			exchangeInstallmentSettingMapper.deleteByPacketId(packetPeriodPrice.getId());
			//删除押金对应的分期详情
			//清空分期记录的settingId
			for (ExchangeInstallmentSetting exchangeInstallmentSetting : exchangeInstallmentSettingList1) {
				exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
				customerInstallmentRecordMapper.clearExchangeSettingId(exchangeInstallmentSetting.getId());
			}
		}
		int total = exchangeBatteryForegiftMapper.delete(foregiftId);
		packetPeriodPriceMapper.deleteByForegiftId(foregiftId.intValue());
		AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getAgentId());
		if (agentBatteryType != null) {
			List<Cabinet> cabinetList = cabinetMapper.findListByAgentAndBatteryType(agentBatteryType.getAgentId(), agentBatteryType.getBatteryType());
			for (Cabinet cabinet : cabinetList) {
				updatePrice(cabinet.getAgentId(), cabinet.getId());
				if (cabinet.getShopId() != null) {
					VipPriceShop vipPriceShop = vipPriceShopMapper.findByShopId(cabinet.getShopId());
					if (vipPriceShop != null) {
						updateShopPrice(vipPriceShop.getId(), cabinet.getShopId());
					}else {
						updateShopPriceByCabint(cabinet.getShopId());
					}
				}
			}
		}
		if (total == 0) {
			return ExtResult.failResult("操作失败！");
		}
		return ExtResult.successResult();
	}
}
