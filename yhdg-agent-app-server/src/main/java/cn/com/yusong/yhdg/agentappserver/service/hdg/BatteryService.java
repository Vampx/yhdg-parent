package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentappserver.persistence.zd.CustomerRentBatteryMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.zd.CustomerRentInfoMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentOrderMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BatteryService extends AbstractService {
	@Autowired
	BatteryMapper batteryMapper;
	@Autowired
	ShopStoreBatteryMapper shopStoreBatteryMapper;
	@Autowired
	CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
	@Autowired
	CustomerRentBatteryMapper customerRentBatteryMapper;
	@Autowired
	CustomerForegiftOrderMapper customerForegiftOrderMapper;
	@Autowired
	RentForegiftOrderMapper rentForegiftOrderMapper;
	@Autowired
	ShopMapper shopMapper;
	@Autowired
	CustomerExchangeInfoMapper customerExchangeInfoMapper;
	@Autowired
	BatteryOrderMapper batteryOrderMapper;
	@Autowired
	RentOrderMapper rentOrderMapper;
	@Autowired
	AgentMapper agentMapper;
	@Autowired
	CustomerRentInfoMapper customerRentInfoMapper;
	@Autowired
	CustomerRefundRecordMapper customerRefundRecordMapper;
	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	FaultLogMapper faultLogMapper;
	@Autowired
	BatteryParameterMapper batteryParameterMapper;

	public String getBatteryTypeName(Integer batteryType) {
		if (batteryType != null) {
			return findBatteryType(batteryType).getTypeName();
		} else {
			return null;
		}
	}

	public Battery find(String id){
		return batteryMapper.find(id);
	}

	public List<Battery> findAgentBatteryList(int agentId, int category, String keyword, List<Integer> isNormal, List<Integer> status,
											  List<Integer> upLineStatus, Integer lowDay, Integer highDay,
											  Integer lowCircle, Integer highCircle, Integer offset, Integer limit) {
		return batteryMapper.findAgentBatteryList(agentId, category, keyword, isNormal, status,upLineStatus,
				                              lowDay, highDay, lowCircle, highCircle, offset, limit);
	}

	public RestResult faultBatteryList(int agentId, int faultType, Integer offset, Integer limit) {
		List<Map> list = new ArrayList<Map>();

		List<FaultLog> faultLogList = faultLogMapper.findByAgent(agentId, faultType, FaultLog.Status.WAIT_PROCESS.getValue(), offset, limit);
		for (FaultLog faultLog : faultLogList) {
			Map line = new HashMap();

			Battery battery = batteryMapper.find(faultLog.getBatteryId());
			BatteryParameter batteryParameter = batteryParameterMapper.find(faultLog.getBatteryId());

			String content = "";
			if(faultType == FaultLog.FaultType.CODE_5.getValue()){
				Integer sysTemp = null;
				if(batteryParameter != null && batteryParameter.getChgOtTrip() != null )
					sysTemp = ( batteryParameter.getChgOtTrip() - 2731) / 10;
				content = String.format("电池【电量%d%%】出现充电高温异常，当前温度值：【%s/%s℃】，请监控该电池",battery.getVolume(),  battery.getTemp(), sysTemp);

			}else if(faultType == FaultLog.FaultType.CODE_6.getValue()){
				Integer sysTemp = null;
				if(batteryParameter != null && batteryParameter.getChgUtTrip() != null )
					sysTemp = ( batteryParameter.getChgUtTrip() - 2731) / 10;
				content = String.format("电池【电量%d%%】出现充电低温异常，当前温度值：【%s/%s℃】，请监控该电池",battery.getVolume(),  battery.getTemp(), sysTemp);

			}else if(faultType == FaultLog.FaultType.CODE_7.getValue()){
				Integer sysTemp = null;
				if(batteryParameter != null && batteryParameter.getDsgOtTrip() != null )
					sysTemp = ( batteryParameter.getDsgOtTrip() - 2731) / 10;
				content = String.format("电池【电量%d%%】出现放电高温异常，当前温度值：【%s/%s℃】，请监控该电池",battery.getVolume(),  battery.getTemp(), sysTemp);

			}else if(faultType == FaultLog.FaultType.CODE_8.getValue()){
				Integer sysTemp = null;
				if(batteryParameter != null && batteryParameter.getDsgUtTrip() != null )
					sysTemp = ( batteryParameter.getDsgUtTrip() - 2731) / 10;
				content = String.format("电池【电量%d%%】出现放电低温异常，当前温度值：【%s/%s℃】，请监控该电池",battery.getVolume(),  battery.getTemp(), sysTemp);

			}else if(faultType == FaultLog.FaultType.CODE_9.getValue()){
				content = String.format("电池【电量%d%%】出现充电过流异常，当前电流值：【%d MA】，请监控该电池",battery.getVolume(), battery.getElectricity());

			}else if(faultType == FaultLog.FaultType.CODE_10.getValue()){
				content = String.format("电池【电量%d%%】出现放电过流异常，当前电流值：【%d MA】，请监控该电池",battery.getVolume(), battery.getElectricity());

			}else if(faultType == FaultLog.FaultType.CODE_11.getValue()){
				content = String.format("电池【电量%d%%】出现短路保护异常，请监控该电池",battery.getVolume());

			}else if(faultType == FaultLog.FaultType.CODE_12.getValue()){
				content = String.format("电池【电量%d%%】IC错误异常，请监控该电池",battery.getVolume());

			}else if(faultType == FaultLog.FaultType.CODE_17.getValue()){
				content = String.format("电池【电量%d%%】压差过大，当前压差%d MA，请重点关注",battery.getVolume(), battery.getRealVoltageDiff());

			}else if(faultType == FaultLog.FaultType.CODE_20.getValue()){
				Integer minOcvTable = null;
				if(batteryParameter != null && batteryParameter.getOcvTable() != null){
					String ocvTable = batteryParameter.getOcvTable();
					 minOcvTable = Integer.parseInt(StringUtils.split(ocvTable, ",")[0]);
				}
				Integer minVoltage = null;
				String [] singleVoltageStrs =  StringUtils.split(batteryParameter.getSingleVoltage(), ",");
				Integer [] singleVoltages = new Integer[singleVoltageStrs.length];
				for(int i=0 ; i < singleVoltageStrs.length; i++){
					singleVoltages[i] = Integer.parseInt(singleVoltageStrs[i]);
				}
				List<Integer> list1 = Arrays.asList(singleVoltages);
				if(!list1.isEmpty()){
					Collections.sort(list1);
					minVoltage = Integer.parseInt(list1.get(0).toString());
				}
				content = String.format("电池【电量%d%%】当前单体电压【%d】，预设单体电压【%d】，电池已断电，请监控该电池",battery.getVolume(), minVoltage, minOcvTable);
			}


			line.put("id", battery.getId());
			line.put("code", battery.getCode());
			line.put("content", content);
			list.add(line);
		}

		int count = faultLogMapper.findCountByAgent(agentId, faultType, FaultLog.Status.WAIT_PROCESS.getValue());

		Map returnMap = new HashMap();
		returnMap.put("faultTypeName",FaultLog.FaultType.getName(faultType));
		returnMap.put("count",count);
		returnMap.put("orderList",list);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, returnMap);
	}


	public RestResult unbindBatteryOverTimeList(int agentId, Integer offset, Integer limit) {
		List<Map> list = new ArrayList<Map>();
		Date now = new Date();

		Integer timeout = Integer.valueOf(findConfigValue(ConstEnum.SystemConfigKey.UNBIND_BATTERY_OUT_TIME.getValue()));
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -(timeout == null ? 0 : timeout));

		int count  = batteryMapper.findFreeOutTimeCount(agentId, Battery.Status.NOT_USE.getValue(), calendar.getTime());

		List<Battery> batteryList = batteryMapper.findFreeOutTimeList(agentId, Battery.Status.NOT_USE.getValue(), calendar.getTime(), offset, limit);

		for (Battery battery : batteryList) {
			Map line = new HashMap();

			line.put("id", battery.getId());
			line.put("code", battery.getCode());
			line.put("shellCode", battery.getShellCode());
			line.put("customerName", battery.getCustomerFullname());
			line.put("volume", battery.getVolume());
			line.put("freeOutTime",  DateFormatUtils.format(battery.getFreeOutTime(), "yyyy-MM-dd HH:mm:ss"));
			long overTime = (now.getTime() - battery.getFreeOutTime().getTime())/1000/60/60;
			line.put("overTime", (int)overTime);
			list.add(line);
		}

		Map returnMap = new HashMap();
		returnMap.put("hours",timeout * 24);
		returnMap.put("count",count);
		returnMap.put("orderList",list);

		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, returnMap);
	}


	//查询门店库存电池
	public List<Battery> shopStoreList(String shopId, String keyword, Integer offset, Integer limit) {
		return batteryMapper.shopStoreList(shopId, keyword, offset, limit);
	}
	public int shopStoreCount(Integer agentId, String shopId){
		return batteryMapper.shopStoreCount(agentId, shopId);
	}
	//查询门店客户使用电池
	public List<Battery> shopCustomerUseList(String shopId, String keyword, Integer offset, Integer limit) {
		return batteryMapper.shopCustomerUseList(shopId, keyword, offset, limit);
	}
	public int shopCustomerUseCount(Integer agentId, String shopId) {
		return batteryMapper.shopCustomerUseCount(agentId, shopId);
	}

	//查询门店柜子电池
	public List<Battery> shopCabinetList(String shopId,String keyword,Integer offset,Integer limit) {
		return batteryMapper.shopCabinetList(shopId, keyword, offset, limit);
	}

	//查询运营商门店中电池
	public List<Battery> agentShopList(Integer agentId,Integer category,String keyword,Integer offset,Integer limit) {
		return batteryMapper.agentShopList(agentId, category, keyword, offset, limit);
	}

	//查询运营商其他电池
	public List<Battery> agentRestsList(Integer agentId,Integer category,String keyword,Integer offset,Integer limit) {
		return batteryMapper.agentRestsList(agentId, category, keyword, offset, limit);
	}

	public int updateLockSwitch(String batteryId, int lockSwitch) {
		return batteryMapper.updateLockSwitch(batteryId, lockSwitch);
	}

	public List<Battery> findByCode(String code) {
		return batteryMapper.findByCode(code);
	}

	@Transactional(rollbackFor=Throwable.class)
	public RestResult updateShopStoreBattery(String id, String shopId, Battery battery, Agent agent, Shop shop) {
		ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
		shopStoreBattery.setCategory(battery.getCategory());
		shopStoreBattery.setAgentId(agent.getId());
		shopStoreBattery.setAgentName(agent.getAgentName());
		shopStoreBattery.setAgentCode(agent.getAgentCode());
		shopStoreBattery.setShopId(shopId);
		shopStoreBattery.setShopName(shop.getShopName());
		shopStoreBattery.setBatteryId(id);
		shopStoreBattery.setCreateTime(new Date());

		shopStoreBatteryMapper.insert(shopStoreBattery);
		if (battery.getCategory() == Battery.Category.RENT.getValue()) {
			batteryMapper.updateShopInfo(id, shop.getId(), shop.getShopName());
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

	@Transactional(rollbackFor=Throwable.class)
	public RestResult unBindShopStoreBattery(String batteryId, String shopId, Battery battery) {
		shopStoreBatteryMapper.clearBattery(shopId, batteryId);
		if (battery.getCategory() == Battery.Category.RENT.getValue()) {
			batteryMapper.updateShopInfo(batteryId, null, null);
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

	@Transactional(rollbackFor = Throwable.class)
	public RestResult switchShopBattery(String shopId, String oldBatteryQrcode, String newBatteryQrcode) {
		Shop shop = shopMapper.find(shopId);
		if (shop == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
		}

		Agent agent = agentMapper.find(shop.getAgentId());
		if (agent == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
		}

		List<Battery> oldBatteryList = batteryMapper.findByCode(oldBatteryQrcode);
		if (oldBatteryList.isEmpty()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "原电池不存在");
		}
		if (oldBatteryList.size() > 1) {
			return RestResult.result(RespCode.CODE_2.getValue(), String.format("电池编号:%s 查找到多块电池，请联系管理员", oldBatteryQrcode));
		}
		Battery oldBattery = oldBatteryList.get(0);


		List<Battery> newBatteryList = batteryMapper.findByCode(newBatteryQrcode);
		if (newBatteryList.isEmpty()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "新电池不存在");
		}
		if (newBatteryList.size() > 1) {
			return RestResult.result(RespCode.CODE_2.getValue(), String.format("电池编号:%s 查找到多块电池，请联系管理员", oldBatteryQrcode));
		}
		Battery newBattery = newBatteryList.get(0);

		if (!oldBattery.getCategory().equals(newBattery.getCategory())) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池换租电类型不一致");
		}

		if (!oldBattery.getType().equals(newBattery.getType())) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池类型不一致");
		}

		if (newBattery.getCategory() == Battery.Category.RENT.getValue() && shopStoreBatteryMapper.findByShopBatteryId(shop.getId(), newBattery.getId()) == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "新电池不是门店库存电池");
		}

		if (oldBattery.getCategory() == Battery.Category.EXCHANGE.getValue()) { //换电

			CustomerExchangeInfo exchangeInfo = customerExchangeInfoMapper.find(oldBattery.getCustomerId());
			if (exchangeInfo == null) {
				return RestResult.result(RespCode.CODE_2.getValue(), "客户未缴押金");
			}
//			if (!shop.getId().equals(exchangeInfo.getBalanceShopId())) {
//				return RestResult.result(RespCode.CODE_2.getValue(), "原电池不是当前门店电池");
//			}
			CustomerExchangeBattery customerExchangeBattery = null;
			List<CustomerExchangeBattery> exchangeBatteryList = customerExchangeBatteryMapper.findByCustomerId(exchangeInfo.getId());
			for (CustomerExchangeBattery battery : exchangeBatteryList) {
				if (battery.getBatteryId().equals(oldBattery.getId())) {
					customerExchangeBattery = battery;
					break;
				}
			}

			if (customerExchangeBattery == null) {
				return RestResult.result(RespCode.CODE_2.getValue(), "原电池所属客户错误");
			}
			customerExchangeBatteryMapper.updateBattery(exchangeInfo.getId(), oldBattery.getId(), newBattery.getId());

			if (StringUtils.isNotEmpty(oldBattery.getOrderId())) {
				batteryOrderMapper.updateBattery(oldBattery.getOrderId(), newBattery.getId());
			}

		} else if (oldBattery.getCategory() == Battery.Category.RENT.getValue()) { //租电
			CustomerRentInfo rentInfo = customerRentInfoMapper.find(oldBattery.getCustomerId());
			if (rentInfo == null) {
				return RestResult.result(RespCode.CODE_2.getValue(), "客户未缴押金");
			}
			if (!shop.getId().equals(rentInfo.getBalanceShopId())) {
				return RestResult.result(RespCode.CODE_2.getValue(), "原电池不是当前门店电池");
			}

			customerRentBatteryMapper.updateBattery(rentInfo.getId(), oldBattery.getId(), newBattery.getId());

			ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
			shopStoreBattery.setCategory(oldBattery.getCategory());
			shopStoreBattery.setAgentId(agent.getId());
			shopStoreBattery.setAgentName(agent.getAgentName());
			shopStoreBattery.setAgentCode(agent.getAgentCode());
			shopStoreBattery.setShopId(shopId);
			shopStoreBattery.setShopName(shop.getShopName());
			shopStoreBattery.setBatteryId(oldBattery.getId());
			shopStoreBattery.setCreateTime(new Date());
			shopStoreBatteryMapper.insert(shopStoreBattery);

			shopStoreBatteryMapper.deleteByShopBatteryId(shop.getId(), newBattery.getId());

		}

		batteryMapper.clearCustomer(oldBattery.getId(), Battery.Status.NOT_USE.getValue());

		//设置新电池
		batteryMapper.updateOrderId(newBattery.getId(),
				Battery.Status.CUSTOMER_OUT.getValue(),
				oldBattery.getOrderId(),
				new Date(),
				oldBattery.getCustomerId(),
				oldBattery.getCustomerMobile(),
				oldBattery.getCustomerFullname());

		return RestResult.result(RespCode.CODE_0.getValue(), null);
	}

	@Transactional(rollbackFor = Throwable.class)
	public RestResult backBattery(String[] batteryId, Integer refundMoney, Agent agent, Shop shop, User user) {
		if (batteryId == null || batteryId.length == 0) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池列表不能是空");
		}
		String operator = (user.getFullname() != null && !user.getFullname().equals("")) ? user.getFullname() : user.getMobile();

		Set<String> batteryIdSet = new HashSet<String>();
		List<Battery> batteryList = new ArrayList<Battery>();
		for (String e : batteryId) {
			Battery battery = batteryMapper.find(e);
			if (battery == null) {
				return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
			}
			batteryList.add(battery);
			batteryIdSet.add(e);
		}

		Integer category = batteryList.get(0).getCategory();
		if (category == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池类型不存在");
		}
		Long customerId = batteryList.get(0).getCustomerId();
		if (customerId == null) {
			return RestResult.result(RespCode.CODE_2.getValue(), "电池未绑定骑手");
		}

		CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);

		if (customerRentInfo != null && customerRentInfo.getVehicleForegiftFlag() != null && customerRentInfo.getVehicleForegiftFlag() == ConstEnum.Flag.TRUE.getValue()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "客户已交纳租车押金，不能退电池");
		}

		CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);

		if (customerExchangeInfo != null && customerExchangeInfo.getVehicleForegiftFlag() != null && customerExchangeInfo.getVehicleForegiftFlag() == ConstEnum.Flag.TRUE.getValue()) {
			return RestResult.result(RespCode.CODE_2.getValue(), "客户已交纳租车押金，不能退电池");
		}

		List<String> batteryIdList = new ArrayList<String>();

		if (category == Battery.Category.EXCHANGE.getValue()) {

			Set<String> exchangeBatterySet = new HashSet<String>();
			List<CustomerExchangeBattery> exchangeBatteryList = customerExchangeBatteryMapper.findByCustomerId(customerId);
			for (CustomerExchangeBattery e : exchangeBatteryList) {
				exchangeBatterySet.add(e.getBatteryId());
			}

			if (!batteryIdSet.equals(exchangeBatterySet)) {
				return RestResult.result(RespCode.CODE_2.getValue(), "电池列表不完整");
			}

			CustomerExchangeInfo exchangeInfo = customerExchangeInfoMapper.find(customerId);
			if (StringUtils.isNotEmpty(exchangeInfo.getForegiftOrderId())) {
				CustomerForegiftOrder foregiftOrder = customerForegiftOrderMapper.find(exchangeInfo.getForegiftOrderId());
				if (foregiftOrder != null) {
					if (refundMoney > foregiftOrder.getMoney() + (foregiftOrder.getDeductionTicketMoney() != null ? foregiftOrder.getDeductionTicketMoney() : 0)) {
						return RestResult.result(RespCode.CODE_2.getValue(), String.format("退款金额不能大于%.2f元", (foregiftOrder.getMoney() + foregiftOrder.getDeductionTicketMoney()) / 100.0));
					}

//					if (foregiftOrder.getMoney() + (foregiftOrder.getDeductionTicketMoney() != null ? foregiftOrder.getDeductionTicketMoney() : 0) == 0) { //实付金额是0 直接修改订单状态
//						customerForegiftOrderMapper.refund(foregiftOrder.getId(),
//								CustomerForegiftOrder.Status.PAY_OK.getValue(),
//								CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(),
//								new Date(),
//								new Date(),
//								0,
//								operator);
//						//更新客户换电押金状态
//						customerMapper.updateHdForegiftStatus(customerId, Customer.HdForegiftStatus.REFUNDED.getValue());
//					} else {
						customerForegiftOrderMapper.applyRefund(foregiftOrder.getId(),
								CustomerForegiftOrder.Status.PAY_OK.getValue(),
								CustomerForegiftOrder.Status.APPLY_REFUND.getValue(),
								new Date(),
								refundMoney);

						CustomerRefundRecord refundRecord = new CustomerRefundRecord();
						refundRecord.setPartnerId(foregiftOrder.getPartnerId());
						refundRecord.setAgentId(agent.getId());
						refundRecord.setAgentName(agent.getAgentName());
						refundRecord.setAgentCode(agent.getAgentCode());
						refundRecord.setOrderId(newOrderId(OrderId.OrderIdType.CUSTOMER_REFUND_RECORD));

						refundRecord.setCustomerId(customerId);
						refundRecord.setMobile(foregiftOrder.getCustomerMobile());
						refundRecord.setFullname(foregiftOrder.getCustomerFullname());
						refundRecord.setSourceType(CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
						refundRecord.setSourceId(foregiftOrder.getId());
						refundRecord.setRefundMoney(refundMoney);
						refundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
						refundRecord.setCreateTime(new Date());
						customerRefundRecordMapper.insert(refundRecord);

						customerMapper.updateHdRefundStatus(customerId, Customer.HdRefundStatus.APPLY_REFUND.getValue());
//					}
				}
			}

			for (CustomerExchangeBattery e : exchangeBatteryList) {
				customerExchangeBatteryMapper.clearBattery(e.getCustomerId(), e.getBatteryId());
				if (StringUtils.isNotEmpty(e.getBatteryOrderId())) {
					batteryOrderMapper.complete(e.getBatteryOrderId(), new Date(), ConstEnum.PayType.BALANCE.getValue(), BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue());
				}
				batteryMapper.clearCustomer(e.getBatteryId(), Battery.Status.NOT_USE.getValue());
				batteryIdList.add(e.getBatteryId());
			}

		} else if (category == Battery.Category.RENT.getValue()) {
			Set<String> rentBatterySet = new HashSet<String>();
			List<CustomerRentBattery> rentBatteryList = customerRentBatteryMapper.findByCustomerId(customerId);
			for (CustomerRentBattery e : rentBatteryList) {
				rentBatterySet.add(e.getBatteryId());
			}
			if (!batteryIdSet.equals(rentBatterySet)) {
				return RestResult.result(RespCode.CODE_2.getValue(), "电池列表不完整");
			}

			CustomerRentInfo rentInfo = customerRentInfoMapper.find(customerId);
			if (StringUtils.isNotEmpty(rentInfo.getForegiftOrderId())) {
				RentForegiftOrder foregiftOrder = rentForegiftOrderMapper.find(rentInfo.getForegiftOrderId());
				if (foregiftOrder != null) {
					if (refundMoney > foregiftOrder.getMoney() + (foregiftOrder.getDeductionTicketMoney() != null ? foregiftOrder.getDeductionTicketMoney() : 0)) {
						return RestResult.result(RespCode.CODE_2.getValue(), String.format("退款金额不能大于%.2f元", (foregiftOrder.getMoney() + foregiftOrder.getDeductionTicketMoney()) / 100.0));
					}

//					if (foregiftOrder.getMoney() + (foregiftOrder.getDeductionTicketMoney() != null ? foregiftOrder.getDeductionTicketMoney() : 0) == 0) { //实付金额是0 直接修改订单状态
//						rentForegiftOrderMapper.refund(foregiftOrder.getId(),
//								RentForegiftOrder.Status.PAY_OK.getValue(),
//								RentForegiftOrder.Status.REFUND_SUCCESS.getValue(),
//								new Date(),
//								new Date(),
//								0,
//								operator);
//						//更新用户租电押金状态
//						customerMapper.updateZdForegiftStatus(foregiftOrder.getCustomerId(), Customer.ZdForegiftStatus.REFUNDED.getValue());
//					} else {
						rentForegiftOrderMapper.applyRefund(foregiftOrder.getId(),
								RentForegiftOrder.Status.PAY_OK.getValue(),
								RentForegiftOrder.Status.APPLY_REFUND.getValue(),
								new Date(),
								refundMoney);

						CustomerRefundRecord refundRecord = new CustomerRefundRecord();
						refundRecord.setPartnerId(foregiftOrder.getPartnerId());
						refundRecord.setAgentId(agent.getId());
						refundRecord.setAgentName(agent.getAgentName());
						refundRecord.setAgentCode(agent.getAgentCode());
						refundRecord.setOrderId(newOrderId(OrderId.OrderIdType.CUSTOMER_REFUND_RECORD));

						refundRecord.setCustomerId(customerId);
						refundRecord.setMobile(foregiftOrder.getCustomerMobile());
						refundRecord.setFullname(foregiftOrder.getCustomerFullname());
						refundRecord.setSourceType(CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue());
						refundRecord.setSourceId(foregiftOrder.getId());
						refundRecord.setRefundMoney(refundMoney);
						refundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());
						refundRecord.setCreateTime(new Date());
						customerRefundRecordMapper.insert(refundRecord);

						customerMapper.updateZdRefundStatus(customerId, Customer.ZdRefundStatus.APPLY_REFUND.getValue());
//					}
				}
			}

			for (CustomerRentBattery e : rentBatteryList) {
				customerRentBatteryMapper.clearBattery(e.getCustomerId(), e.getBatteryId());
				if (StringUtils.isNotEmpty(e.getRentOrderId())) {
					rentOrderMapper.complete(e.getRentOrderId(), RentOrder.Status.BACK.getValue(), RentOrder.Status.RENT.getValue(), new Date(), operator);
				}
				batteryMapper.clearCustomer(e.getBatteryId(), Battery.Status.NOT_USE.getValue());
				batteryIdList.add(e.getBatteryId());
			}

			for (String e : batteryIdList) {
				ShopStoreBattery shopStoreBattery = new ShopStoreBattery();
				shopStoreBattery.setCategory(category);
				shopStoreBattery.setAgentId(agent.getId());
				shopStoreBattery.setAgentName(agent.getAgentName());
				shopStoreBattery.setAgentCode(agent.getAgentCode());
				shopStoreBattery.setShopId(shop.getId());
				shopStoreBattery.setShopName(shop.getShopName());
				shopStoreBattery.setBatteryId(e);
				shopStoreBattery.setCreateTime(new Date());
				shopStoreBatteryMapper.insert(shopStoreBattery);
			}
		}
		return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
	}

	public int countShopCabinetBattery(String shopId) {
		return batteryMapper.countShopCabinetBattery(shopId);
	}

	public int countShopCustomerUseNum(String shopId) {
		return batteryMapper.countShopCustomerUseNum(shopId);
	}

	public int changeToNormal(String batteryId) {
		Battery battery = batteryMapper.find(batteryId);
		return batteryMapper.changeIsNormal(batteryId, battery.getIsNormal(), null,null ,null);
	}

	public int changeToAbnormal(String batteryId, String abnormalCause, String operator ,Date operatorTime) {
		Battery battery = batteryMapper.find(batteryId);
		return batteryMapper.changeIsNormal(batteryId, battery.getIsNormal(), abnormalCause, operator, operatorTime);
	}

	public String viewAbnormalCause(String batteryId) {
		Battery battery = batteryMapper.find(batteryId);
		return battery.getAbnormalCause();
	}
}
