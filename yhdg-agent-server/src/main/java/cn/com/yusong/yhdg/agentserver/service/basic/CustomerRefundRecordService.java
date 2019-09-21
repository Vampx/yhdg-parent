package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerRefundRecordMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.GroupOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.VehicleForegiftOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zc.VehiclePeriodOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerRefundRecord;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CustomerRefundRecordService extends AbstractService {

	@Autowired
	CustomerRefundRecordMapper customerRefundRecordMapper;
	@Autowired
	CustomerForegiftOrderMapper customerForegiftOrderMapper;
	@Autowired
	PacketPeriodOrderMapper packetPeriodOrderMapper;
	@Autowired
	InsuranceOrderMapper insuranceOrderMapper;
	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	GroupOrderMapper groupOrderMapper;
	@Autowired
	VehicleForegiftOrderMapper vehicleForegiftOrderMapper;
	@Autowired
	VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
	@Autowired
	RentForegiftOrderMapper rentForegiftOrderMapper;
	@Autowired
	RentPeriodOrderMapper rentPeriodOrderMapper;

	public CustomerRefundRecord find(Long id) {
		return customerRefundRecordMapper.find(id);
	}

	public Page findPage(CustomerRefundRecord search) {
		Page page = search.buildPage();
		page.setTotalItems(customerRefundRecordMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		page.setResult(customerRefundRecordMapper.findPageResult(search));
		return page;
	}

	public CustomerRefundRecord findOneByStatus(Long customerId, Integer status, Integer sourceType) {
		return customerRefundRecordMapper.findOneByStatus(customerId, status, sourceType);
	}

	public List<CustomerRefundRecord> findByCustomerId(Long customerId, Integer status) {
		return customerRefundRecordMapper.findByCustomerId(customerId, status);
	}

	public String newOrderId() {
		return newOrderId(OrderId.OrderIdType.CUSTOMER_REFUND_RECORD);
	}

	private CustomerRefundRecord insert(Customer customer, Integer appId, Integer agentId, String agentName, String id, Integer money, String orderId, Integer sourceType, Integer refundStatus) {
		CustomerRefundRecord refundRecord = new CustomerRefundRecord();
		refundRecord.setPartnerId(customer.getPartnerId());
		refundRecord.setAgentId(agentId);
		refundRecord.setAgentName(agentName);
		refundRecord.setOrderId(orderId);

		refundRecord.setCustomerId(customer.getId());
		refundRecord.setMobile(customer.getMobile());
		refundRecord.setFullname(customer.getFullname());
		refundRecord.setSourceType(sourceType);
		refundRecord.setSourceId(id);

		refundRecord.setRefundMoney(money);
		refundRecord.setCreateTime(new Date());
		refundRecord.setRefundTime(new Date());
		refundRecord.setStatus(refundStatus);

		customerRefundRecordMapper.insert(refundRecord);

		return refundRecord;
	}


	public int backStatus(long id, int status, int refundMoney, Date refundTime) {
		return customerRefundRecordMapper.backStatus(id, status, refundMoney, refundTime);
	}

	@Transactional(rollbackFor=Throwable.class)
	public int updateHdStatus(long id,int refundType, String ptPayOrderId, int status,  int refundMoney, Date refundTime) {
		int result = customerRefundRecordMapper.updateStatus(id, refundType, ptPayOrderId, status, refundMoney, refundTime);

		if(result > 0){
			CustomerRefundRecord customerRefundRecord = customerRefundRecordMapper.find(id);

			//如果审核失败，对应原订单状态要还原成审核前
			if(customerRefundRecord.getStatus() == CustomerRefundRecord.Status.FAIL.getValue()){
				if(customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue()){
					//回滚押金
					customerForegiftOrderMapper.updateRefund(customerRefundRecord.getSourceId(),null,null, CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
				}else if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue()){
					//回滚租金
					PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(customerRefundRecord.getSourceId());
					if(packetPeriodOrder.getBeginTime() != null){
						packetPeriodOrderMapper.backRefund(customerRefundRecord.getSourceId(), PacketPeriodOrder.Status.APPLY_REFUND.getValue(), PacketPeriodOrder.Status.USED.getValue());
					}else{
						packetPeriodOrderMapper.backRefund(customerRefundRecord.getSourceId(), PacketPeriodOrder.Status.APPLY_REFUND.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue());
					}
				}else if(customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGINSURANCE.getValue()){
					//回滚保险
					insuranceOrderMapper.updateRefund(customerRefundRecord.getSourceId(),null,null, InsuranceOrder.Status.APPLY_REFUND.getValue(), InsuranceOrder.Status.PAID.getValue());
				}
			}

			//用户更新退款状态
			if(customerRefundRecordMapper.existHdRefund(customerRefundRecord.getCustomerId()) > 0 ){
				customerMapper.updateHdRefundStatus(customerRefundRecord.getCustomerId(), ConstEnum.Flag.TRUE.getValue());
			}else{
				customerMapper.updateHdRefundStatus(customerRefundRecord.getCustomerId(), ConstEnum.Flag.FALSE.getValue());
			}

		}
		return result;
	}

	@Transactional(rollbackFor=Throwable.class)
	public int updateZdStatus(long id,int refundType, String ptPayOrderId, int status,  int refundMoney, Date refundTime) {
		int result = customerRefundRecordMapper.updateStatus(id, refundType, ptPayOrderId, status, refundMoney, refundTime);

		if(result > 0){
			CustomerRefundRecord customerRefundRecord = customerRefundRecordMapper.find(id);

			//如果审核失败，对应原订单状态要还原成审核前
			if(customerRefundRecord.getStatus() == CustomerRefundRecord.Status.FAIL.getValue()){
				if(customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue()){
					//回滚押金
					customerForegiftOrderMapper.updateRefund(customerRefundRecord.getSourceId(),null,null, CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
				}else if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue()){
					//回滚租金
					PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(customerRefundRecord.getSourceId());
					if(packetPeriodOrder.getBeginTime() != null){
						packetPeriodOrderMapper.backRefund(customerRefundRecord.getSourceId(), PacketPeriodOrder.Status.APPLY_REFUND.getValue(), PacketPeriodOrder.Status.USED.getValue());
					}else{
						packetPeriodOrderMapper.backRefund(customerRefundRecord.getSourceId(), PacketPeriodOrder.Status.APPLY_REFUND.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue());
					}
				}else if(customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGINSURANCE.getValue()){
					//回滚保险
					insuranceOrderMapper.updateRefund(customerRefundRecord.getSourceId(),null,null, InsuranceOrder.Status.APPLY_REFUND.getValue(), InsuranceOrder.Status.PAID.getValue());
				}
			}

			//用户更新退款状态
			if(customerRefundRecordMapper.existZdRefund(customerRefundRecord.getCustomerId()) > 0 ){
				customerMapper.updateZdRefundStatus(customerRefundRecord.getCustomerId(), ConstEnum.Flag.TRUE.getValue());
			}else{
				customerMapper.updateZdRefundStatus(customerRefundRecord.getCustomerId(), ConstEnum.Flag.FALSE.getValue());
			}

		}
		return result;
	}

	@Transactional(rollbackFor=Throwable.class)
	public int updateZcStatus(long id,int refundType, String ptPayOrderId, int status,  int refundMoney, Date refundTime) {
		int result = customerRefundRecordMapper.updateStatus(id, refundType, ptPayOrderId, status, refundMoney, refundTime);

		if(result > 0){
			CustomerRefundRecord customerRefundRecord = customerRefundRecordMapper.find(id);

			if(customerRefundRecord.getStatus() == CustomerRefundRecord.Status.FAIL.getValue()){
				//如果审核失败，对应原订单状态要还原成审核前
				GroupOrder groupOrder = groupOrderMapper.find(customerRefundRecord.getSourceId());
				if (groupOrder != null) {
					groupOrderMapper.updateStatus(groupOrder.getId(), GroupOrder.Status.PAY_OK.getValue(), 0, null, null, null, null, groupOrder.getHandleTime());
				}
				if(customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.ZCGROUP.getValue()){
					if (groupOrder != null) {
						//回滚租车押金
						if (StringUtils.isNotEmpty(groupOrder.getVehicleForegiftId())) {
							VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderMapper.find(groupOrder.getVehicleForegiftId());
							if (vehicleForegiftOrder != null) {
								vehicleForegiftOrderMapper.updateStatus(vehicleForegiftOrder.getId(), VehicleForegiftOrder.Status.PAY_OK.getValue(), null, null, null, null, new Date());
							}
						}
						//回滚租车租金
						if (StringUtils.isNotEmpty(groupOrder.getVehiclePeriodId())) {
							VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderMapper.find(groupOrder.getVehiclePeriodId());
							if (vehiclePeriodOrder != null) {
								if (vehiclePeriodOrder.getBeginTime() != null) {
									vehiclePeriodOrderMapper.updateStatus(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.USED.getValue(), null, null, null);
								} else {
									vehiclePeriodOrderMapper.updateStatus(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.NOT_USE.getValue(), null, null, null);
								}
							}
						}
						//回滚电池押金
						if (StringUtils.isNotEmpty(groupOrder.getBatteryForegiftId())) {
							if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
								CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
								if (customerForegiftOrder != null) {
									customerForegiftOrderMapper.updateRefund(customerForegiftOrder.getId(), null, null, CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
								}
							} else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
								RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
								if (rentForegiftOrder != null) {
									rentForegiftOrderMapper.updateRefund(rentForegiftOrder.getId(), null, null, RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
								}
							}
						}
						//回滚电池租金
						if (StringUtils.isNotEmpty(groupOrder.getBatteryRentId())) {
							if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
								PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(groupOrder.getBatteryRentId());
								if (packetPeriodOrder != null) {
									if (packetPeriodOrder.getBeginTime() != null) {
										packetPeriodOrderMapper.updateRefund(packetPeriodOrder.getId(), 0, null, PacketPeriodOrder.Status.USED.getValue());
									} else {
										packetPeriodOrderMapper.updateRefund(packetPeriodOrder.getId(), 0, null, PacketPeriodOrder.Status.NOT_USE.getValue());
									}
								}
							} else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
								RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(groupOrder.getBatteryRentId());
								if (rentPeriodOrder != null) {
									if (rentPeriodOrder.getBeginTime() != null) {
										rentPeriodOrderMapper.updateRefund(rentPeriodOrder.getId(), 0, null, RentPeriodOrder.Status.USED.getValue());
									} else {
										rentPeriodOrderMapper.updateRefund(rentPeriodOrder.getId(), 0, null, RentPeriodOrder.Status.NOT_USE.getValue());
									}
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	public int existHdRefund(Long customerId) {
		return customerRefundRecordMapper.existZdRefund(customerId);
	}

	public int existZdRefund(Long customerId) {
		return customerRefundRecordMapper.existZdRefund(customerId);
	}

	public int existZcRefund(Long customerId) {
		return customerRefundRecordMapper.existZcRefund(customerId);
	}
}
