package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ExchangeRefundMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.agentserver.weixin.WxPayUtils;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

@Service
public class ExchangeRefundService extends AbstractService {

	static Logger log = LoggerFactory.getLogger(ExchangeRefundService.class);

	@Autowired
	CustomerMapper customerMapper;
	@Autowired
    ExchangeRefundMapper refundMapper;
	@Autowired
	AgentBatteryTypeMapper agentBatteryTypeMapper;
	@Autowired
	CustomerForegiftOrderMapper customerForegiftOrderMapper;
	@Autowired
	PacketPeriodOrderMapper packetPeriodOrderMapper;
	@Autowired
	InsuranceOrderMapper insuranceOrderMapper;
	@Autowired
	CustomerInOutMoneyMapper customerInOutMoneyMapper;
	@Autowired
	PartnerInOutMoneyMapper partnerInOutMoneyMapper;
	@Autowired
	AlipayPayOrderMapper alipayPayOrderMapper;
	@Autowired
	WeixinPayOrderMapper weixinPayOrderMapper;
	@Autowired
	WeixinmpPayOrderMapper weixinmpPayOrderMapper;
	@Autowired
	WeixinmaPayOrderMapper weixinmaPayOrderMapper;
	@Autowired
	AlipayfwPayOrderMapper alipayfwPayOrderMapper;
	@Autowired
	AlipayPayOrderRefundMapper alipayPayOrderRefundMapper;
	@Autowired
	WeixinPayOrderRefundMapper weixinPayOrderRefundMapper;
	@Autowired
	WeixinmpPayOrderRefundMapper weixinmpPayOrderRefundMapper;
	@Autowired
	WeixinmaPayOrderRefundMapper weixinmaPayOrderRefundMapper;
	@Autowired
	AlipayfwPayOrderRefundMapper alipayfwPayOrderRefundMapper;
	@Autowired
	SystemConfigMapper systemConfigMapper;
	@Autowired
	AgentSystemConfigMapper agentSystemConfigMapper;
	@Autowired
	CustomerExchangeInfoMapper customerExchangeInfoMapper;
	@Autowired
	CustomerRefundRecordMapper customerRefundRecordMapper;
	@Autowired
	AppConfig appConfig;
	@Autowired
	CustomerCouponTicketMapper customerCouponTicketMapper;
	@Autowired
	AgentMapper agentMapper;
	@Autowired
	CustomerPayTrackMapper customerPayTrackMapper;
	@Autowired
	AgentForegiftRefundMapper agentForegiftRefundMapper;
	@Autowired
	PartnerMapper partnerMapper;
	@Autowired
	AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;
	@Autowired
	AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;
	@Autowired
	AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
	@Autowired
	CabinetMapper cabinetMapper;
	@Autowired
	CustomerInstallmentRecordOrderDetailMapper customerInstallmentRecordOrderDetailMapper;
	@Autowired
	CustomerInstallmentRecordPayDetailMapper customerInstallmentRecordPayDetailMapper;
	@Autowired
	CustomerMultiOrderMapper customerMultiOrderMapper;
	@Autowired
	CustomerInstallmentRecordMapper customerInstallmentRecordMapper;

	public Page findPage(Customer search) {
		Page page = search.buildPage();
		page.setTotalItems(refundMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<Customer> customerList = refundMapper.findPageResult(search);
		for (Customer customer : customerList) {
			if (customer.getAgentId() != null) {
				AgentInfo agentInfo = findAgentInfo(customer.getAgentId());
				if (agentInfo != null) {
					customer.setAgentName(agentInfo.getAgentName());
				}
			}
			if (customer.getBatteryType() != null) {
				customer.setBatteryTypeName(findBatteryType(customer.getBatteryType()).getTypeName());
			}
			if (customer.getHdRefundStatus() != null && customer.getHdRefundStatus() == Customer.HdRefundStatus.APPLY_REFUND.getValue()) {
				CustomerRefundRecord customerRefundRecord = customerRefundRecordMapper.findOneByCustomerId(customer.getId(), CustomerRefundRecord.Status.APPLY.getValue());
				if (customerRefundRecord != null) {
					customer.setApplyRefundTime(customerRefundRecord.getCreateTime());
				}
			}

			if (StringUtils.isNotEmpty(customer.getMobile())) {
				customer.setMobile(customer.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
			}

			CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customer.getId());
			if (customerExchangeInfo != null && customerExchangeInfo.getBalanceCabinetId() != null) {
				Cabinet cabinet = cabinetMapper.find(customerExchangeInfo.getBalanceCabinetId());
				if (cabinet != null) {
					customer.setBalanceCabinetName(cabinet.getCabinetName());
				}
			}

			if (customerExchangeInfo != null) {
				CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerExchangeInfo.getForegiftOrderId());
				if (customerForegiftOrder != null && customerForegiftOrder.getPayType() == ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
					customer.setForegiftMoney(0);
				}
			}

		}
		page.setResult(customerList);
		return page;
	}

	public Customer find(Long id) {
		Customer customer = customerMapper.find(id);
		//押金
		List<CustomerForegiftOrder> customerForegiftOrderList = customerForegiftOrderMapper.findListByCustomerId(id, customer.getAgentId());
		List<PacketPeriodOrder> packetPeriodOrderList = packetPeriodOrderMapper.findListByCustomerId(id, customer.getAgentId());
		List<InsuranceOrder> insuranceOrderList = insuranceOrderMapper.findListByCustomerId(id, customer.getAgentId());
		customer.setCustomerForegiftOrderList(customerForegiftOrderList);
		customer.setPacketPeriodOrderList(packetPeriodOrderList);
		customer.setInsuranceOrderList(insuranceOrderList);
		return customer;
	}

	/**
	 * 退款
	 * @param userName  操作人
	 * @param refundType  退款方式
	 * @param sourceType 来源类型
	 * @param sourceId 来源id
	 * @param refundMoney 退款金额
	 * @return
	 * @throws AlipayApiException
	 */
	@Transactional(rollbackFor = Throwable.class)
	public DataResult refund(String userName, int refundType, int sourceType, String sourceId, int refundMoney, Long refundRecordId, String orderId, boolean test) throws AlipayApiException {
		Map<String,String> data = new HashMap();
		//押金退款
		if(sourceType == CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue()){
			CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(sourceId);
			if (customerForegiftOrder == null) {
				return DataResult.failResult("订单不存在", data);
			}
			if (StringUtils.isEmpty(orderId) && customerForegiftOrder.getStatus() != CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) {
				return DataResult.failResult("申请退款状态不对", data);
			}
			int orderMoney = customerForegiftOrder.getMoney() + (customerForegiftOrder.getDeductionTicketMoney() == null ? 0 : customerForegiftOrder.getDeductionTicketMoney());
			if (refundMoney > orderMoney) {
				return DataResult.failResult("退款金额不能大于订单金额", data);
			}
			CustomerInstallmentRecordOrderDetail customerForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderBySourceId(sourceId, ConstEnum.Category.EXCHANGE.getValue());

			String memo = null;

			//判断用户退款金额是否大于支付金额，如果大于支付金额，剩下的钱加入资金账户
			int payMoney = refundMoney;
			int balanceMoney = 0;

			if(refundMoney > customerForegiftOrder.getMoney()){
				payMoney = customerForegiftOrder.getMoney();
				balanceMoney = refundMoney - customerForegiftOrder.getMoney();
			}

			int bizType = CustomerInOutMoney.BizType.IN_CUSTOMER_FOREGIFT_REFUND.getValue();
			//退款
			DataResult result = doRefund(sourceType, refundType, bizType, customerForegiftOrder.getPayType(), customerForegiftOrder.getId(), data, payMoney, refundMoney, customerForegiftOrder.getCustomerId(), customerForegiftOrder.getPartnerId(), userName, "押金退款", test);
			if(!result.isSuccess()){
				return result;
			}

			//抵扣券退款
			if(balanceMoney > 0){
				if(customerMapper.updateBalance(customerForegiftOrder.getCustomerId(), balanceMoney, 0) > 0){
					//余额流水
					Customer customer = customerMapper.find(customerForegiftOrder.getCustomerId());
					CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
					customerInOutMoney.setCustomerId(customerForegiftOrder.getCustomerId());
					customerInOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEDUCTION_TICKET.getValue());
					customerInOutMoney.setBizId(customerForegiftOrder.getId());
					customerInOutMoney.setMoney(balanceMoney);
					customerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
					customerInOutMoney.setBalance(customer.getBalance());
					customerInOutMoney.setCreateTime(new Date());
					customerInOutMoneyMapper.insert(customerInOutMoney);
				}else{
					memo = "押金退款成功，退款抵扣券金额失败";
				}
			}

			//更新押金订单
			customerForegiftOrderMapper.updateStatus(customerForegiftOrder.getId(),
					CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(),
					refundMoney, userName, new Date(),
					null, memo, new Date());
			//更新客户换电押金状态
			customerMapper.updateHdForegiftStatus(customerForegiftOrder.getCustomerId(), Customer.HdForegiftStatus.REFUNDED.getValue());
			//清空客户绑定押金信息
			customerExchangeInfoMapper.delete(customerForegiftOrder.getCustomerId());
			//解绑运营商
//			customerMapper.clearAgentId(customerForegiftOrder.getCustomerId());
			//存在未使用的优惠券 将优惠券状态置为失效
			CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.findBySource(customerForegiftOrder.getId(), OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue());
			if (customerCouponTicket != null && customerCouponTicket.getStatus() == CustomerCouponTicket.Status.NOT_USER.getValue()) {
				customerCouponTicketMapper.updateStatus(customerCouponTicket.getId(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.EXPIRED.getValue());
			}
			//押金加入押金池
			handleAgentForegift(customerForegiftOrder);

			//分期付订单处理
			if(customerForegiftOrder.getPayType() == ConstEnum.PayType.INSTALLMENT.getValue()){
				//如果分期付没有完成就退款，这个时候订单的支付时间为空，需要更新上，否则订单无法进入统计
				if(customerForegiftOrder.getPayTime() == null){
					customerForegiftOrderMapper.updatePayTime(customerForegiftOrder.getId(), new Date());
				}
				handleCustomerInstallment(sourceId, OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue(), ConstEnum.Category.EXCHANGE.getValue());
			}

			//保存退款记录
			CustomerRefundRecord customerRefundRecord = insertRefundRecord(refundType, sourceType, refundMoney, orderId, getPtPayOrderId(result), customerForegiftOrder.getPartnerId(), customerForegiftOrder.getAgentId(), customerForegiftOrder.getAgentName(), customerForegiftOrder.getCustomerId(), customerForegiftOrder.getCustomerMobile(), customerForegiftOrder.getCustomerFullname(), customerForegiftOrder.getId());

			//生成运营商押金退款订单
			if(refundMoney < orderMoney) {
				AgentForegiftRefund agentForegiftRefund = new AgentForegiftRefund();
				agentForegiftRefund.setAgentCode(customerForegiftOrder.getAgentCode());
				agentForegiftRefund.setAgentId(customerForegiftOrder.getAgentId());
				agentForegiftRefund.setAgentName(customerForegiftOrder.getAgentName());
				agentForegiftRefund.setCustomerId(customerForegiftOrder.getCustomerId());
				agentForegiftRefund.setDeductionTicketMoney(customerForegiftOrder.getDeductionTicketMoney()==null?0:customerForegiftOrder.getDeductionTicketMoney());
				agentForegiftRefund.setForegiftOrderId(customerForegiftOrder.getId());
				agentForegiftRefund.setFullname(customerForegiftOrder.getCustomerFullname());
				agentForegiftRefund.setMobile(customerForegiftOrder.getCustomerMobile());
				agentForegiftRefund.setOperatorName(customerForegiftOrder.getRefundOperator());
				agentForegiftRefund.setPrice(customerForegiftOrder.getPrice());
				agentForegiftRefund.setRefundMoney(customerForegiftOrder.getRefundMoney()==null?0:customerForegiftOrder.getRefundMoney());
				agentForegiftRefund.setRefundRecordId(customerRefundRecord.getId()==null?refundRecordId:customerRefundRecord.getId());
				agentForegiftRefund.setPayMoney(customerForegiftOrder.getMoney());
				agentForegiftRefund.setTicketMoney(customerForegiftOrder.getTicketMoney()==null?0:customerForegiftOrder.getTicketMoney());
				agentForegiftRefund.setCreateTime(new Date());
				agentForegiftRefund.setRemainMoney(orderMoney-refundMoney);
				agentForegiftRefundMapper.insert(agentForegiftRefund);
			}

			//客户退押金消费轨迹
			addCustomerPayTrack(customerForegiftOrder.getAgentId(), customerForegiftOrder.getCustomerId(), CustomerPayTrack.TrackType.BACK_FOREGIFT.getValue(),
					StringUtils.replaceEach("退押金，押金：${refundMoney}。",
							new String[]{"${refundMoney}"},
							new String[]{String.format("%.2f元", 1d * refundMoney / 100.0)}));
		}
		//包时段退款
		else if(sourceType == CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue()){
			PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(sourceId);
			if (packetPeriodOrder == null) {
				return DataResult.failResult("订单不存在", data);
			}
			if (StringUtils.isEmpty(orderId) &&  packetPeriodOrder.getStatus() != PacketPeriodOrder.Status.APPLY_REFUND.getValue()) {
				return DataResult.failResult("申请退款状态不对", data);
			}
			if (refundMoney > packetPeriodOrder.getMoney()) {
				return DataResult.failResult("退款金额不能大于订单金额", data);
			}
			CustomerInstallmentRecordOrderDetail packetPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderBySourceId(sourceId, ConstEnum.Category.EXCHANGE.getValue());

			int bizType = CustomerInOutMoney.BizType.IN_CUSTOMER_PACKET_PERIOD_ORDER_REFUND.getValue();
			//退款
			DataResult result = doRefund(sourceType, refundType, bizType, packetPeriodOrder.getPayType(), packetPeriodOrder.getId(), data, null, refundMoney, packetPeriodOrder.getCustomerId(), packetPeriodOrder.getPartnerId(), userName, "套餐退款", test);
			if(!result.isSuccess()){
				return result;
			}

			packetPeriodOrderMapper.updateRefund(packetPeriodOrder.getId(), refundMoney, new Date(), PacketPeriodOrder.Status.REFUND.getValue());

			//存在未使用的优惠券 将优惠券状态置为失效
			CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.findBySource(packetPeriodOrder.getId(), OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue());
			if (customerCouponTicket != null && customerCouponTicket.getStatus() == CustomerCouponTicket.Status.NOT_USER.getValue()) {
				customerCouponTicketMapper.updateStatus(customerCouponTicket.getId(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.EXPIRED.getValue());
			}

			//分期付订单处理
			if(packetPeriodOrder.getPayType() == ConstEnum.PayType.INSTALLMENT.getValue()){
				//如果分期付没有完成就退款，这个时候订单的支付时间为空，需要更新上，否则订单无法进入统计
				if(packetPeriodOrder.getPayTime() == null){
					packetPeriodOrderMapper.updatePayTime(packetPeriodOrder.getId(), new Date());
				}
				handleCustomerInstallment(sourceId, OrderId.OrderIdType.PACKET_PERIOD_ORDER.getValue(), ConstEnum.Category.EXCHANGE.getValue());
			}

			//保存退款记录
			insertRefundRecord(refundType, sourceType, refundMoney, orderId, getPtPayOrderId(result), packetPeriodOrder.getPartnerId(), packetPeriodOrder.getAgentId(), packetPeriodOrder.getAgentName(), packetPeriodOrder.getCustomerId(), packetPeriodOrder.getCustomerMobile(), packetPeriodOrder.getCustomerFullname(), packetPeriodOrder.getId());

			//客户退租金消费轨迹
			addCustomerPayTrack(packetPeriodOrder.getAgentId(), packetPeriodOrder.getCustomerId(), CustomerPayTrack.TrackType.BACK_RENT.getValue(),
					StringUtils.replaceEach("退租金，租金：${refundMoney}。",
							new String[]{"${refundMoney}"},
							new String[]{String.format("%.2f元", 1d * refundMoney / 100.0)}));
		}
		//保险退款
		else if(sourceType == CustomerRefundRecord.SourceType.HDGINSURANCE.getValue()){
			InsuranceOrder insuranceOrder = insuranceOrderMapper.find(sourceId);
			if (insuranceOrder == null) {
				return DataResult.failResult("订单不存在", data);
			}
			if (StringUtils.isEmpty(orderId) &&  insuranceOrder.getStatus() != PacketPeriodOrder.Status.APPLY_REFUND.getValue()) {
				return DataResult.failResult("申请退款状态不对", data);
			}
			if (refundMoney > insuranceOrder.getMoney()) {
				return DataResult.failResult("退款金额不能大于订单金额", data);
			}
			CustomerInstallmentRecordOrderDetail insuranceOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderBySourceId(sourceId, ConstEnum.Category.EXCHANGE.getValue());

			int bizType = CustomerInOutMoney.BizType.IN_CUSTOMER_INSURANCE_REFUND.getValue();
			//退款
			DataResult result = doRefund(sourceType, refundType, bizType, insuranceOrder.getPayType(), insuranceOrder.getId(), data, null, refundMoney, insuranceOrder.getCustomerId(), insuranceOrder.getPartnerId(), userName, "保险退款", test);
			if(!result.isSuccess()){
				return result;
			}

			insuranceOrderMapper.updateRefund(insuranceOrder.getId(), refundMoney, new Date(), InsuranceOrder.Status.APPLY_REFUND.getValue(), InsuranceOrder.Status.REFUND_SUCCESS.getValue());

			//分期付订单处理
			if(insuranceOrder.getPayType() == ConstEnum.PayType.INSTALLMENT.getValue()){
				//如果分期付没有完成就退款，这个时候订单的支付时间为空，需要更新上，否则订单无法进入统计
				if(insuranceOrder.getPayTime() == null){
					insuranceOrderMapper.updatePayTime(insuranceOrder.getId(), new Date());
				}
				handleCustomerInstallment(sourceId, OrderId.OrderIdType.INSURANCE_ORDER.getValue(), ConstEnum.Category.EXCHANGE.getValue());
			}

			//保存退款记录
			insertRefundRecord(refundType, sourceType, refundMoney, orderId, getPtPayOrderId(result), insuranceOrder.getPartnerId(), insuranceOrder.getAgentId(), insuranceOrder.getAgentName(), insuranceOrder.getCustomerId(), insuranceOrder.getCustomerMobile(), insuranceOrder.getCustomerFullname(), insuranceOrder.getId());
		}
		//多通道退款
		else if(sourceType == CustomerRefundRecord.SourceType.HDMULTI.getValue()){
			CustomerMultiOrder customerMultiOrder = customerMultiOrderMapper.find(Long.parseLong(sourceId));
			if (customerMultiOrder == null) {
				return DataResult.failResult("订单不存在", data);
			}
			if (StringUtils.isEmpty(orderId) &&  customerMultiOrder.getStatus() != CustomerMultiOrder.Status.APPLY_REFUND.getValue()) {
				return DataResult.failResult("申请退款状态不对", data);
			}
			if (refundMoney > customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney()) {
				return DataResult.failResult("退款金额不能大于支付金额", data);
			}
			if(refundType != CustomerRefundRecord.RefundType.BALANCE.getValue()){
				return DataResult.failResult("请选择退到余额", data);
			}

			int bizType = CustomerInOutMoney.BizType.IN_CUSTOMER_MULTI_REFUND.getValue();
			//退款
			DataResult result = doRefund(sourceType, refundType, bizType, ConstEnum.PayType.MULTI_CHANNEL.getValue(), customerMultiOrder.getId().toString(), data, null, refundMoney, customerMultiOrder.getCustomerId(), customerMultiOrder.getPartnerId(), userName, "多通道退款", test);
			if(!result.isSuccess()){
				return result;
			}

			customerMultiOrderMapper.updateRefund(customerMultiOrder.getId(), refundMoney, new Date(), CustomerMultiOrder.Status.REFUND_SUCCESS.getValue());

			//保存退款记录
			insertRefundRecord(refundType, sourceType, refundMoney, orderId, getPtPayOrderId(result), customerMultiOrder.getPartnerId(), customerMultiOrder.getAgentId(), customerMultiOrder.getAgentName(), customerMultiOrder.getCustomerId(), customerMultiOrder.getMobile(), customerMultiOrder.getFullname(), customerMultiOrder.getId().toString());

			//客户退多通道订单消费轨迹
			addCustomerPayTrack(customerMultiOrder.getAgentId(), customerMultiOrder.getCustomerId(), CustomerPayTrack.TrackType.BACK_MULTI.getValue(),
					StringUtils.replaceEach("退多通道订单，金额：${refundMoney}。",
							new String[]{"${refundMoney}"},
							new String[]{String.format("%.2f元", 1d * refundMoney / 100.0)}));
		}
		return DataResult.successResult(data);
	}

	public void addCustomerPayTrack(Integer agentId, Long customerId, int trackType, String memo){
		Agent agent = agentMapper.find(agentId);
		Customer customer = customerMapper.find(customerId);

		CustomerPayTrack customerPayTrack = new CustomerPayTrack();
		customerPayTrack.setAgentId(agent.getId());
		customerPayTrack.setCustomerId(customer.getId());
		customerPayTrack.setCustomerFullname(customer.getFullname());
		customerPayTrack.setCustomerMobile(customer.getMobile());
		customerPayTrack.setTrackType(trackType);
		customerPayTrack.setMemo(memo);
		customerPayTrack.setCreateTime(new Date());
		customerPayTrackMapper.insert(customerPayTrack);
	}

	public String getPtPayOrderId(DataResult result) {
		String ptPayOrderId = "";
		if (result.getData() != null) {
			Map m = (Map) result.getData();
			Object objId = m.get("ptPayOrderId");
			if (objId != null) {
				ptPayOrderId = (String) objId;
			}
		}
		return ptPayOrderId;
	}

	private CustomerRefundRecord insertRefundRecord(int refundType, int sourceType, int refundMoney, String orderId, String ptPayOrderId, Integer partnerId, Integer agentId, String agentName, Long customerId, String customerMobile, String customerFullname, String id) {
		CustomerRefundRecord refundRecord = new CustomerRefundRecord();
		if(StringUtils.isNotEmpty(orderId)){
			refundRecord.setPartnerId(partnerId);
			refundRecord.setAgentId(agentId);
			refundRecord.setAgentName(agentName);
			refundRecord.setOrderId(orderId);

			refundRecord.setCustomerId(customerId);
			refundRecord.setMobile(customerMobile);
			refundRecord.setFullname(customerFullname);
			refundRecord.setSourceType(sourceType);
			refundRecord.setSourceId(id);

			refundRecord.setRefundMoney(refundMoney);
			refundRecord.setCreateTime(new Date());
			refundRecord.setRefundTime(new Date());
			refundRecord.setStatus(CustomerRefundRecord.Status.FINISH.getValue());
			refundRecord.setRefundType(refundType);
			refundRecord.setPtPayOrderId(ptPayOrderId);
			customerRefundRecordMapper.insert(refundRecord);
		}
		return refundRecord;
	}


	/**
	 * 保存退款支付订单
	 * @param payOrder
	 * @param payType
	 * @param sourceType
	 * @param payMoney
	 */
	public void addPayOrderRefund(PayOrder payOrder, int payType, int sourceType, int payMoney){
		Integer bizType = null;
		if(sourceType == CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue()){
			bizType = PayOrderRefund.BizType.FOREGIFT_ORDER_CUSTOMER_REFUND.getValue();
		}else if(sourceType == CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue()){
			bizType = PayOrderRefund.BizType.PACKET_PERIOD_ORDER_REFUND.getValue();
		}else if(sourceType == CustomerRefundRecord.SourceType.HDGINSURANCE.getValue()){
			bizType = PayOrderRefund.BizType.INSURANCE_ORDER_REFUND.getValue();
		}

		if(payType == ConstEnum.PayType.ALIPAY.getValue()){
			AlipayPayOrderRefund payOrderRefund = new AlipayPayOrderRefund();
			payOrderRefund.setOrderId(payOrder.getId());
			payOrderRefund.setPartnerId(payOrder.getPartnerId());
			payOrderRefund.setAgentId(payOrder.getAgentId());
			payOrderRefund.setAgentName(payOrder.getAgentName());
			payOrderRefund.setRefundMoney(payMoney);
			payOrderRefund.setBizType(bizType);
			payOrderRefund.setBizId(payOrder.getSourceId());
			payOrderRefund.setCustomerId(payOrder.getCustomerId());
			payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
			payOrderRefund.setCustomerMobile(payOrder.getMobile());
			payOrderRefund.setCreateTime(new Date());
			alipayPayOrderRefundMapper.insert(payOrderRefund);
		}else if(payType == ConstEnum.PayType.WEIXIN.getValue()){
			WeixinPayOrderRefund payOrderRefund = new WeixinPayOrderRefund();
			payOrderRefund.setOrderId(payOrder.getId());
			payOrderRefund.setPartnerId(payOrder.getPartnerId());
			payOrderRefund.setAgentId(payOrder.getAgentId());
			payOrderRefund.setAgentName(payOrder.getAgentName());
			payOrderRefund.setRefundMoney(payMoney);
			payOrderRefund.setBizType(bizType);
			payOrderRefund.setBizId(payOrder.getSourceId());
			payOrderRefund.setCustomerId(payOrder.getCustomerId());
			payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
			payOrderRefund.setCustomerMobile(payOrder.getMobile());
			payOrderRefund.setCreateTime(new Date());
			weixinPayOrderRefundMapper.insert(payOrderRefund);
		}else if(payType == ConstEnum.PayType.ALIPAY_FW.getValue()){
			AlipayfwPayOrderRefund payOrderRefund = new AlipayfwPayOrderRefund();
			payOrderRefund.setOrderId(payOrder.getId());
			payOrderRefund.setPartnerId(payOrder.getPartnerId());
			payOrderRefund.setAgentId(payOrder.getAgentId());
			payOrderRefund.setAgentName(payOrder.getAgentName());
			payOrderRefund.setRefundMoney(payMoney);
			payOrderRefund.setBizType(bizType);
			payOrderRefund.setBizId(payOrder.getSourceId());
			payOrderRefund.setCustomerId(payOrder.getCustomerId());
			payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
			payOrderRefund.setCustomerMobile(payOrder.getMobile());
			payOrderRefund.setCreateTime(new Date());
			alipayfwPayOrderRefundMapper.insert(payOrderRefund);
		}else if(payType == ConstEnum.PayType.WEIXIN_MP.getValue()){
			WeixinmpPayOrderRefund payOrderRefund = new WeixinmpPayOrderRefund();
			payOrderRefund.setOrderId(payOrder.getId());
			payOrderRefund.setPartnerId(payOrder.getPartnerId());
			payOrderRefund.setAgentId(payOrder.getAgentId());
			payOrderRefund.setAgentName(payOrder.getAgentName());
			payOrderRefund.setRefundMoney(payMoney);
			payOrderRefund.setBizType(bizType);
			payOrderRefund.setBizId(payOrder.getSourceId());
			payOrderRefund.setCustomerId(payOrder.getCustomerId());
			payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
			payOrderRefund.setCustomerMobile(payOrder.getMobile());
			payOrderRefund.setCreateTime(new Date());
			weixinmpPayOrderRefundMapper.insert(payOrderRefund);
		}else if(payType == ConstEnum.PayType.WEIXIN_MA.getValue()){
			WeixinmaPayOrderRefund payOrderRefund = new WeixinmaPayOrderRefund();
			payOrderRefund.setOrderId(payOrder.getId());
			payOrderRefund.setPartnerId(payOrder.getPartnerId());
			payOrderRefund.setAgentId(payOrder.getAgentId());
			payOrderRefund.setAgentName(payOrder.getAgentName());
			payOrderRefund.setRefundMoney(payMoney);
			payOrderRefund.setBizType(bizType);
			payOrderRefund.setBizId(payOrder.getSourceId());
			payOrderRefund.setCustomerId(payOrder.getCustomerId());
			payOrderRefund.setCustomerFullname(payOrder.getCustomerName());
			payOrderRefund.setCustomerMobile(payOrder.getMobile());
			payOrderRefund.setCreateTime(new Date());
			weixinmaPayOrderRefundMapper.insert(payOrderRefund);
		}

	}

	public DataResult doRefund(int sourceType, int refundType, int bizType, int payType,
							   String sourceId, Map data, Integer payMoney, int refundMoney, long customerId, int partnerId,
							   String userName, String refundReason, boolean test){
		if(payMoney == null){
			payMoney = refundMoney;
		}
		String ptPayOrderId;
		boolean thirdPay = false;
		//根据退款方式进行退款
		if(refundType == CustomerRefundRecord.RefundType.BALANCE.getValue()){ //余额退款
			if(customerMapper.updateBalance(customerId, payMoney, 0) > 0){
				//余额流水
				Customer customer = customerMapper.find(customerId);
				CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
				customerInOutMoney.setCustomerId(customerId);
				customerInOutMoney.setMoney(payMoney);
				customerInOutMoney.setBizType(bizType);
				customerInOutMoney.setBizId(sourceId);
				customerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
				customerInOutMoney.setBalance(customer.getBalance());
				customerInOutMoney.setCreateTime(new Date());
				customerInOutMoneyMapper.insert(customerInOutMoney);
			}else{
				return DataResult.failResult("更新用户余额失败", data);
			}
		}else{ //原路返回
			if(payMoney > 0){
				if (payType == ConstEnum.PayType.ALIPAY.getValue()) {
					thirdPay = true;
					AlipayPayOrder alipayPayOrder = alipayPayOrderMapper.findBySourceId(sourceId);
					if (alipayPayOrder == null) {
						return DataResult.failResult(ConstEnum.PayType.ALIPAY.getName() + "订单不存在", data);
					}
					ptPayOrderId = alipayPayOrder.getId();
					data.put("ptPayOrderId", ptPayOrderId);

					Partner partner = partnerMapper.find(alipayPayOrder.getPartnerId());
					String appId = partner.getAlipayAppId();
					String alipayPublic = partner.getAlipayAliKey();
					String alipayAppRsaPrivate = partner.getAlipayPriKey();

					double refundAmount = ((double) payMoney) / 100;
					AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, alipayAppRsaPrivate, "json", "GBK", alipayPublic, "RSA2");
					AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
					request.setBizContent("{" +
							"\"out_trade_no\":\"" + alipayPayOrder.getId() + "\"," +
							"\"out_request_no\":\"" + System.currentTimeMillis() + "\"," +
							"\"refund_amount\":" + refundAmount + "," +
							"\"refund_reason\":\"" + refundReason + "\"" +
							"  }");
					try {
						AlipayTradeRefundResponse response;
						if(test){
							response = new AlipayTradeRefundResponse();
							response.setSubCode(null);
						}else {
							response = alipayClient.execute(request);
						}
						if (response != null && response.isSuccess()) {
							alipayPayOrderMapper.refundOk(alipayPayOrder.getId(), payMoney, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
							//加入退款订单
							addPayOrderRefund(alipayPayOrder, ConstEnum.PayType.ALIPAY.getValue(), sourceType, payMoney);
						}else{
							return DataResult.failResult(ConstEnum.PayType.ALIPAY.getName() + "退款失败:" + response.getMsg() + "," + response.getSubMsg(), data);
						}
					} catch (Exception e) {
						log.error("Alipay退款失败", e);
						return DataResult.failResult(ConstEnum.PayType.ALIPAY.getName() + "退款失败:" + e.getMessage(), data);
					}

				} else if (payType == ConstEnum.PayType.WEIXIN.getValue()) {
					thirdPay = true;
					WeixinPayOrder weixinPayOrder = weixinPayOrderMapper.findBySourceId(sourceId);
					if (weixinPayOrder == null) {
						return DataResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "订单不存在", data);
					}
					ptPayOrderId = weixinPayOrder.getId();
					data.put("ptPayOrderId", ptPayOrderId);

					Partner partner = partnerMapper.find(weixinPayOrder.getPartnerId());
					String appId = partner.getWeixinAppId();
					String mchId = partner.getWeixinPartnerCode();
					String partnerKey = partner.getWeixinPartnerKey();

					String outTradeNo = weixinPayOrder.getId();
					int totalFee = weixinPayOrder.getMoney();
					int refundFee = payMoney;

					try {
						WxPayUnifiedOrderResult result;
						if(test){
							result = new WxPayUnifiedOrderResult();
							result.setReturnCode("SUCCESS");
						}else {
							File certFile = AppUtils.getWeixinCertFile(appConfig.appDir, weixinPayOrder.getPartnerId());
							if(!certFile.exists()) {
								return DataResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "证书不存在", data);
							}
							result = WxPayUtils.refundOrder(appId, mchId, partnerKey, outTradeNo, totalFee, refundFee, refundReason, certFile);
						}

						if (result != null && result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
							weixinPayOrderMapper.refundOk(weixinPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
							//加入退款订单
							addPayOrderRefund(weixinPayOrder, ConstEnum.PayType.WEIXIN.getValue(), sourceType, payMoney);
						}else{
							return DataResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "退款失败:" + result.getReturnCode()  + result.getErrCodeDes(), data);
						}
					} catch (Exception e) {
						log.error("Weixin退款失败", e);
						return DataResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "退款失败:" + e.getMessage(), data);
					}

				} else if (payType == ConstEnum.PayType.WEIXIN_MP.getValue()) {
					thirdPay = true;
					WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderMapper.findBySourceId(sourceId);
					if (weixinmpPayOrder == null) {
						return DataResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "订单不存在", data);
					}
					ptPayOrderId = weixinmpPayOrder.getId();
					data.put("ptPayOrderId", ptPayOrderId);

					Partner partner = partnerMapper.find(weixinmpPayOrder.getPartnerId());
					String appId = partner.getMpAppId();
					String mchId = partner.getMpPartnerCode();
					String partnerKey = partner.getMpPartnerKey();

					String outTradeNo = weixinmpPayOrder.getId();
					int totalFee = weixinmpPayOrder.getMoney();
					int refundFee = payMoney;

					try {
						WxPayUnifiedOrderResult result;
						if(test){
							result = new WxPayUnifiedOrderResult();
							result.setReturnCode("SUCCESS");
						}else {
							File certFile = AppUtils.getWeixinmpCertFile(appConfig.appDir, weixinmpPayOrder.getPartnerId());
							if(!certFile.exists()) {
								return DataResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "证书不存在", data);
							}
							result = WxPayUtils.refundOrder(appId, mchId, partnerKey, outTradeNo, totalFee, refundFee, refundReason, certFile);
						}

						if (result != null && result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
							weixinmpPayOrderMapper.refundOk(weixinmpPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
							//加入退款订单
							addPayOrderRefund(weixinmpPayOrder, ConstEnum.PayType.WEIXIN_MP.getValue(), sourceType, payMoney);
						}else{
							return DataResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "退款失败:" + result.getReturnCode()  + result.getErrCodeDes(), data);
						}
					} catch (Exception e) {
						log.error("WeixinMp退款失败", e);
						return DataResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "退款失败:" + e.getMessage(), data);
					}

				} else if (payType == ConstEnum.PayType.WEIXIN_MA.getValue()) {
					thirdPay = true;
					WeixinmaPayOrder weixinmaPayOrder = weixinmaPayOrderMapper.findBySourceId(sourceId);
					if (weixinmaPayOrder == null) {
						return DataResult.failResult(ConstEnum.PayType.WEIXIN_MA.getName() + "订单不存在", data);
					}
					ptPayOrderId = weixinmaPayOrder.getId();
					data.put("ptPayOrderId", ptPayOrderId);

					Partner partner = partnerMapper.find(weixinmaPayOrder.getPartnerId());
					String appId = partner.getMaAppId();
					String mchId = partner.getMaPartnerCode();
					String partnerKey = partner.getMaPartnerKey();

					String outTradeNo = weixinmaPayOrder.getId();
					int totalFee = weixinmaPayOrder.getMoney();
					int refundFee = payMoney;

					try {
						WxPayUnifiedOrderResult result;
						if(test){
							result = new WxPayUnifiedOrderResult();
							result.setReturnCode("SUCCESS");
						}else {
							File certFile = AppUtils.getWeixinmpCertFile(appConfig.appDir, weixinmaPayOrder.getPartnerId());
							if(!certFile.exists()) {
								return DataResult.failResult(ConstEnum.PayType.WEIXIN_MA.getName() + "证书不存在", data);
							}
							result = WxPayUtils.refundOrder(appId, mchId, partnerKey, outTradeNo, totalFee, refundFee, refundReason, certFile);
						}

						if (result != null && result.getReturnCode().equals("SUCCESS") && result.getResultCode().equals("SUCCESS")) {
							weixinmaPayOrderMapper.refundOk(weixinmaPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
							//加入退款订单
							addPayOrderRefund(weixinmaPayOrder, ConstEnum.PayType.WEIXIN_MA.getValue(), sourceType, payMoney);
						}else{
							return DataResult.failResult(ConstEnum.PayType.WEIXIN_MA.getName() + "退款失败:" + result.getReturnCode()  + result.getErrCodeDes(), data);
						}
					} catch (Exception e) {
						log.error("WeixinMa退款失败", e);
						return DataResult.failResult(ConstEnum.PayType.WEIXIN_MA.getName() + "退款失败:" + e.getMessage(), data);
					}

				}  else if (payType == ConstEnum.PayType.ALIPAY_FW.getValue()) {
					thirdPay = true;
					AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderMapper.findBySourceId(sourceId);
					if (alipayfwPayOrder == null) {
						return DataResult.failResult(ConstEnum.PayType.ALIPAY_FW.getName() + "订单不存在", data);
					}
					ptPayOrderId = alipayfwPayOrder.getId();
					data.put("ptPayOrderId", ptPayOrderId);

					Partner partner = partnerMapper.find(alipayfwPayOrder.getPartnerId());
					String appId = partner.getFwAppId();
					String alipayfwPublic = partner.getFwAliKey();
					String alipayfwAppRsaPrivate = partner.getFwPriKey();

					double refundAmount = ((double) payMoney) / 100;
					AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", appId, alipayfwAppRsaPrivate, "json", "GBK", alipayfwPublic, "RSA2");
					AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
					request.setBizContent("{" +
							"\"out_trade_no\":\"" + alipayfwPayOrder.getId() + "\"," +
							"\"out_request_no\":\"" + System.currentTimeMillis() + "\"," +
							"\"refund_amount\":" + refundAmount + "," +
							"\"refund_reason\":\"" + refundReason + "\"" +
							"  }");
					try {
						AlipayTradeRefundResponse response;
						if(test){
							response = new AlipayTradeRefundResponse();
							response.setSubCode(null);
						}else {
							response = alipayClient.execute(request);
						}
						if (response != null && response.isSuccess()) {
							alipayfwPayOrderMapper.refundOk(alipayfwPayOrder.getId(), payMoney, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
							//加入退款订单
							addPayOrderRefund(alipayfwPayOrder, ConstEnum.PayType.ALIPAY_FW.getValue(), sourceType, payMoney);
						}else{
							return DataResult.failResult(ConstEnum.PayType.ALIPAY_FW.getName() + "退款失败:" + response.getMsg() + "," + response.getSubMsg(), data);
						}

					} catch (Exception e) {
						log.error("AlipayFw退款失败", e);
						return DataResult.failResult(ConstEnum.PayType.ALIPAY_FW.getName() + "退款失败:" + e.getMessage(), data);
					}

				} else {
					if(customerMapper.updateBalance(customerId, payMoney, 0) > 0){
						//余额流水
						Customer customer = customerMapper.find(customerId);
						CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
						customerInOutMoney.setCustomerId(customerId);
						customerInOutMoney.setMoney(payMoney);
						customerInOutMoney.setBizType(bizType);
						customerInOutMoney.setBizId(sourceId);
						customerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
						customerInOutMoney.setBalance(customer.getBalance());
						customerInOutMoney.setCreateTime(new Date());
						customerInOutMoneyMapper.insert(customerInOutMoney);
					}else{
						return DataResult.failResult("更新用户余额失败", data);
					}
				}

				//商户流水
				if(thirdPay) {
					//客户收入流水
					Customer customer = customerMapper.find(customerId);
					CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
					customerInOutMoney.setCustomerId(customerId);
					customerInOutMoney.setMoney(refundMoney);
					customerInOutMoney.setBizType(bizType);
					customerInOutMoney.setBizId(sourceId);
					customerInOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
					customerInOutMoney.setBalance(customer.getBalance() + refundMoney);
					customerInOutMoney.setCreateTime(new Date());
					customerInOutMoneyMapper.insert(customerInOutMoney);
					//客户支出流水
					customerInOutMoney = new CustomerInOutMoney();
					customerInOutMoney.setCustomerId(customerId);
					customerInOutMoney.setMoney(-refundMoney);
					customerInOutMoney.setBizType(bizType);
					customerInOutMoney.setBizId(sourceId);
					customerInOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
					customerInOutMoney.setBalance(customer.getBalance());
					customerInOutMoney.setCreateTime(new Date());
					customerInOutMoneyMapper.insert(customerInOutMoney);

					PartnerInOutMoney partnerInOutMoney = new PartnerInOutMoney();
					partnerInOutMoney.setPartnerType(partnerInOutMoney.getPartnerType(payType));
					partnerInOutMoney.setPartnerId(partnerId);
					partnerInOutMoney.setBizType(bizType);
					partnerInOutMoney.setBizId(sourceId);
					partnerInOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
					partnerInOutMoney.setMoney(-refundMoney);
					partnerInOutMoney.setOperator(userName);
					partnerInOutMoney.setCreateTime(new Date());
					partnerInOutMoneyMapper.insert(partnerInOutMoney);
				}
			}
		}
		data.put("thirdPay", thirdPay);
		return DataResult.successResult(data);
	}

	/**
	 * 运营商押金池
	 * @param customerForegiftOrder
	 */
	private void handleAgentForegift(CustomerForegiftOrder customerForegiftOrder) {
		//更新运营商押金余额 预留金额  押金余额比例
		List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
				CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
		//押金余额
		int foregiftBalance = customerForegiftOrderMapper.sumMoneyByAgent(customerForegiftOrder.getAgentId(), statusList);
		//运营商押金充值
		int deposit =  agentForegiftDepositOrderMapper.sumMoney(customerForegiftOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
		//运营商提现
		int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(customerForegiftOrder.getAgentId(), ConstEnum.Category.EXCHANGE.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

		//预留金额 = 押金余额 + 运营商押金充值 - 运营商押金提现
		int foregiftRemainMoney = foregiftBalance + deposit - withdraw;
		int foregiftBalanceRatio = 100;
		if(foregiftBalance != 0 ){
			foregiftBalanceRatio = foregiftRemainMoney * 100 / foregiftBalance;
		}
		if(foregiftRemainMoney < 0 ){
			foregiftBalanceRatio = 0;
		}

		//更新运营商押金
		if(agentMapper.updateForegift(customerForegiftOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
			//运营商押金流水
			AgentForegiftInOutMoney inOutMoney = new AgentForegiftInOutMoney();
			inOutMoney.setAgentId(customerForegiftOrder.getAgentId());
			inOutMoney.setCategory(ConstEnum.Category.EXCHANGE.getValue());
			inOutMoney.setMoney(-customerForegiftOrder.getMoney());
			inOutMoney.setBizType(AgentForegiftInOutMoney.BizType.OUT_CUSTOMER_FOREGIFT.getValue());
			inOutMoney.setBizId(customerForegiftOrder.getId());
			inOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
			inOutMoney.setBalance(foregiftBalance);
			inOutMoney.setRemainMoney(foregiftRemainMoney);
			inOutMoney.setRatio(foregiftBalanceRatio);
			inOutMoney.setOperator(customerForegiftOrder.getCustomerFullname());
			inOutMoney.setCreateTime(new Date());
			agentForegiftInOutMoneyMapper.insert(inOutMoney);
		}
	}

	/**
	 * 分期付订单处理
	 */
	private void handleCustomerInstallment(String sourceId, int sourceType, int category) {
		CustomerInstallmentRecordOrderDetail detail = customerInstallmentRecordOrderDetailMapper.find(sourceId, sourceType, category);
		if(detail != null){
			CustomerInstallmentRecord customerInstallmentRecord = customerInstallmentRecordMapper.find(detail.getRecordId());
			if (customerInstallmentRecord != null){
				//更新状态
				if(customerInstallmentRecord.getStatus() != CustomerInstallmentRecord.Status.WAIT_PAY.getValue()){
					customerInstallmentRecordMapper.updateStatus(customerInstallmentRecord.getId(), CustomerInstallmentRecord.Status.REFUND_SUCCESS.getValue());
					List<CustomerInstallmentRecordPayDetail>  payDetailList = customerInstallmentRecordPayDetailMapper.findListByRecordId(customerInstallmentRecord.getId());
					for(CustomerInstallmentRecordPayDetail customerInstallmentRecordPayDetail : payDetailList){
						if(customerInstallmentRecordPayDetail.getStatus() == CustomerInstallmentRecordPayDetail.Status.WAIT_PAY.getValue()){
							customerInstallmentRecordPayDetailMapper.updateStatus(customerInstallmentRecordPayDetail.getId(), CustomerInstallmentRecordPayDetail.Status.CANCEL.getValue());
						}
						if(customerInstallmentRecordPayDetail.getStatus() == CustomerInstallmentRecordPayDetail.Status.PAY_OK.getValue()){
							customerInstallmentRecordPayDetailMapper.updateStatus(customerInstallmentRecordPayDetail.getId(), CustomerInstallmentRecordPayDetail.Status.REFUND.getValue());
						}
					}
				}
			}
		}
	}

}
