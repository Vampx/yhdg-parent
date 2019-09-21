package cn.com.yusong.yhdg.webserver.service.zd;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.config.AppConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.zd.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import cn.com.yusong.yhdg.webserver.weixin.WxPayUtils;
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
public class RentRefundService extends AbstractService {

	static Logger log = LoggerFactory.getLogger(RentRefundService.class);

	@Autowired
	CustomerMapper customerMapper;
	@Autowired
	RentRefundMapper rentRefundMapper;
	@Autowired
	AgentBatteryTypeMapper agentBatteryTypeMapper;
	@Autowired
	RentForegiftOrderMapper rentForegiftOrderMapper;
	@Autowired
	RentPeriodOrderMapper rentPeriodOrderMapper;
	@Autowired
	RentInsuranceOrderMapper rentInsuranceOrderMapper;
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
	AlipayfwPayOrderMapper alipayfwPayOrderMapper;
	@Autowired
	AlipayPayOrderRefundMapper alipayPayOrderRefundMapper;
	@Autowired
	WeixinPayOrderRefundMapper weixinPayOrderRefundMapper;
	@Autowired
	WeixinmpPayOrderRefundMapper weixinmpPayOrderRefundMapper;
	@Autowired
	AlipayfwPayOrderRefundMapper alipayfwPayOrderRefundMapper;
	@Autowired
	SystemConfigMapper systemConfigMapper;
	@Autowired
	AgentSystemConfigMapper agentSystemConfigMapper;
	@Autowired
	CustomerRentInfoMapper customerRentInfoMapper;
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
	RentForegiftRefundMapper rentForegiftRefundMapper;
	@Autowired
	PartnerMapper partnerMapper;
	@Autowired
	AgentForegiftDepositOrderMapper agentForegiftDepositOrderMapper;
	@Autowired
	AgentForegiftWithdrawOrderMapper agentForegiftWithdrawOrderMapper;
	@Autowired
	AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
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
		page.setTotalItems(rentRefundMapper.findPageCount(search));
		search.setBeginIndex(page.getOffset());
		List<Customer> customerList = rentRefundMapper.findPageResult(search);
		for (Customer customer : customerList) {
			if (customer.getAgentId() != null) {
				AgentInfo agentInfo = findAgentInfo(customer.getAgentId());
				if (agentInfo != null) {
					customer.setAgentName(agentInfo.getAgentName());
				}
			}
			if (customer.getZdRefundStatus() != null && customer.getZdRefundStatus() == Customer.ZdRefundStatus.APPLY_REFUND.getValue()) {
				CustomerRefundRecord customerRefundRecord = customerRefundRecordMapper.findOneByCustomerId(customer.getId(), CustomerRefundRecord.Status.APPLY.getValue());
				if (customerRefundRecord != null) {
					customer.setApplyRefundTime(customerRefundRecord.getCreateTime());
				}
			}
			if (customer.getBatteryType() != null) {
				customer.setBatteryTypeName(findBatteryType(customer.getBatteryType()).getTypeName());
			}
			CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customer.getId());
			if (customerRentInfo != null) {
				RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(customerRentInfo.getForegiftOrderId());
				if (rentPeriodOrder != null && rentPeriodOrder.getPayType() == ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
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
		List<RentForegiftOrder> rentForegiftOrderList = rentForegiftOrderMapper.findListByCustomerId(id, customer.getAgentId());
		List<RentPeriodOrder> rentPeriodOrderList = rentPeriodOrderMapper.findListByCustomerId(id, customer.getAgentId());
		List<RentInsuranceOrder> rentInsuranceOrderList = rentInsuranceOrderMapper.findListByCustomerId(id, customer.getAgentId());
		customer.setRentForegiftOrderList(rentForegiftOrderList);
		customer.setRentPeriodOrderList(rentPeriodOrderList);
		customer.setRentInsuranceOrderList(rentInsuranceOrderList);
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
		if(sourceType == CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue()){
			RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(sourceId);
			if (rentForegiftOrder == null) {
				return DataResult.failResult("订单不存在", data);
			}
			if (StringUtils.isEmpty(orderId) && rentForegiftOrder.getStatus() != RentForegiftOrder.Status.APPLY_REFUND.getValue()) {
				return DataResult.failResult("申请退款状态不对", data);
			}
            int orderMoney = rentForegiftOrder.getMoney() + (rentForegiftOrder.getDeductionTicketMoney() == null ? 0 : rentForegiftOrder.getDeductionTicketMoney());
            if (refundMoney > orderMoney) {
				return DataResult.failResult("退款金额不能大于订单金额", data);
			}

			CustomerInstallmentRecordOrderDetail rentForegiftOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderBySourceId(sourceId, ConstEnum.Category.RENT.getValue());

			String memo = null;

			//判断用户退款金额是否大于支付金额，如果大于支付金额，剩下的钱加入资金账户
			int payMoney = refundMoney;
			int balanceMoney = 0;

			if(refundMoney > rentForegiftOrder.getMoney()){
				payMoney = rentForegiftOrder.getMoney();
				balanceMoney = refundMoney - rentForegiftOrder.getMoney();
			}

			int bizType = CustomerInOutMoney.BizType.IN_CUSTOMER_RENT_FOREGIFT_REFUND.getValue();
			//退款
			DataResult result = doRefund(sourceType, refundType, bizType, rentForegiftOrder.getPayType(), rentForegiftOrder.getId(), data, payMoney, refundMoney, rentForegiftOrder.getCustomerId(), rentForegiftOrder.getPartnerId(), userName, "押金退款", test);
			if(!result.isSuccess()){
				return result;
			}

			//抵扣券退款
			if(balanceMoney > 0){
				if(customerMapper.updateBalance(rentForegiftOrder.getCustomerId(), balanceMoney, 0) > 0){
					//余额流水
					Customer customer = customerMapper.find(rentForegiftOrder.getCustomerId());
					CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
					customerInOutMoney.setCustomerId(rentForegiftOrder.getCustomerId());
					customerInOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_DEDUCTION_TICKET.getValue());
					customerInOutMoney.setBizId(rentForegiftOrder.getId());
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
			rentForegiftOrderMapper.updateStatus(rentForegiftOrder.getId(),
					RentForegiftOrder.Status.REFUND_SUCCESS.getValue(),
					refundMoney, userName, new Date(),
					null, memo, new Date());
			//更新客户租电押金信息
			customerMapper.updateZdForegiftStatus(rentForegiftOrder.getCustomerId(), Customer.ZdForegiftStatus.REFUNDED.getValue());
			//清空客户绑定押金信息
			customerRentInfoMapper.delete(rentForegiftOrder.getCustomerId());
			//解绑运营商
//			customerMapper.clearAgentId(rentForegiftOrder.getCustomerId());
			//存在未使用的优惠券 将优惠券状态置为失效
			CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.findBySource(rentForegiftOrder.getId(), OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue());
			if (customerCouponTicket != null && customerCouponTicket.getStatus() == CustomerCouponTicket.Status.NOT_USER.getValue()) {
				customerCouponTicketMapper.updateStatus(customerCouponTicket.getId(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.EXPIRED.getValue());
			}
			//押金加入押金池
			handleZdAgentForegift(rentForegiftOrder);

			//分期付订单处理
			if(rentForegiftOrder.getPayType() == ConstEnum.PayType.INSTALLMENT.getValue()){
				//如果分期付没有完成就退款，这个时候订单的支付时间为空，需要更新上，否则订单无法进入统计
				if(rentForegiftOrder.getPayTime() == null){
					rentForegiftOrderMapper.updatePayTime(rentForegiftOrder.getId(), new Date());
				}
				handleCustomerInstallment(sourceId, OrderId.OrderIdType.RENT_FORGIFT_ORDER.getValue(), ConstEnum.Category.RENT.getValue());
			}

			//保存退款记录
            CustomerRefundRecord customerRefundRecord = insertRefundRecord(refundType, sourceType, refundMoney, orderId, getPtPayOrderId(result), rentForegiftOrder.getPartnerId(), rentForegiftOrder.getAgentId(), rentForegiftOrder.getAgentName(), rentForegiftOrder.getCustomerId(), rentForegiftOrder.getCustomerMobile(), rentForegiftOrder.getCustomerFullname(), rentForegiftOrder.getId());

            //生成运营商押金退款订单
            if(refundMoney < orderMoney) {
                RentForegiftRefund rentForegiftRefund = new RentForegiftRefund();
                rentForegiftRefund.setAgentCode(rentForegiftOrder.getAgentCode());
                rentForegiftRefund.setAgentId(rentForegiftOrder.getAgentId());
                rentForegiftRefund.setAgentName(rentForegiftOrder.getAgentName());
                rentForegiftRefund.setCustomerId(rentForegiftOrder.getCustomerId());
                rentForegiftRefund.setDeductionTicketMoney(rentForegiftOrder.getDeductionTicketMoney()==null?0:rentForegiftOrder.getDeductionTicketMoney());
                rentForegiftRefund.setForegiftOrderId(rentForegiftOrder.getId());
                rentForegiftRefund.setFullname(rentForegiftOrder.getCustomerFullname());
                rentForegiftRefund.setMobile(rentForegiftOrder.getCustomerMobile());
                rentForegiftRefund.setOperatorName(rentForegiftOrder.getRefundOperator());
                rentForegiftRefund.setPrice(rentForegiftOrder.getPrice());
                rentForegiftRefund.setRefundMoney(rentForegiftOrder.getRefundMoney()==null?0:rentForegiftOrder.getRefundMoney());
                rentForegiftRefund.setRefundRecordId(customerRefundRecord.getId()==null?refundRecordId:customerRefundRecord.getId());
                rentForegiftRefund.setPayMoney(rentForegiftOrder.getMoney());
                rentForegiftRefund.setTicketMoney(rentForegiftOrder.getTicketMoney()==null?0:rentForegiftOrder.getTicketMoney());
                rentForegiftRefund.setCreateTime(new Date());
                rentForegiftRefund.setRemainMoney(orderMoney-refundMoney);
                rentForegiftRefundMapper.insert(rentForegiftRefund);
            }

			//客户退押金消费轨迹
			addCustomerPayTrack(rentForegiftOrder.getAgentId(), rentForegiftOrder.getCustomerId(), CustomerPayTrack.TrackType.BACK_FOREGIFT.getValue(),
					StringUtils.replaceEach("退押金，押金：${refundMoney}。",
							new String[]{"${refundMoney}"},
							new String[]{String.format("%.2f元", 1d * refundMoney / 100.0)}));
        }
		//包时段退款
		else if(sourceType == CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue()){
			RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(sourceId);
			if (rentPeriodOrder == null) {
				return DataResult.failResult("订单不存在", data);
			}
			if (StringUtils.isEmpty(orderId) &&  rentPeriodOrder.getStatus() != RentPeriodOrder.Status.APPLY_REFUND.getValue()) {
				return DataResult.failResult("申请退款状态不对", data);
			}
			if (refundMoney > rentPeriodOrder.getMoney()) {
				return DataResult.failResult("退款金额不能大于订单金额", data);
			}

			CustomerInstallmentRecordOrderDetail rentPeriodOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderBySourceId(sourceId, ConstEnum.Category.RENT.getValue());

			int bizType = CustomerInOutMoney.BizType.IN_CUSTOMER_RENT_PERIOD_ORDER_REFUND.getValue();
			//退款
			DataResult result = doRefund(sourceType, refundType, bizType, rentPeriodOrder.getPayType(), rentPeriodOrder.getId(), data, null, refundMoney, rentPeriodOrder.getCustomerId(), rentPeriodOrder.getPartnerId(), userName, "套餐退款", test);
			if(!result.isSuccess()){
				return result;
			}

			rentPeriodOrderMapper.updateRefund(rentPeriodOrder.getId(), refundMoney, new Date(), RentPeriodOrder.Status.REFUND.getValue());

			//存在未使用的优惠券 将优惠券状态置为失效
			CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.findBySource(rentPeriodOrder.getId(), OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue());
			if (customerCouponTicket != null && customerCouponTicket.getStatus() == CustomerCouponTicket.Status.NOT_USER.getValue()) {
				customerCouponTicketMapper.updateStatus(customerCouponTicket.getId(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.EXPIRED.getValue());
			}

			//分期付订单处理
			if(rentPeriodOrder.getPayType() == ConstEnum.PayType.INSTALLMENT.getValue()){
				//如果分期付没有完成就退款，这个时候订单的支付时间为空，需要更新上，否则订单无法进入统计
				if(rentPeriodOrder.getPayTime() == null){
					rentPeriodOrderMapper.updatePayTime(rentPeriodOrder.getId(), new Date());
				}
				handleCustomerInstallment(sourceId, OrderId.OrderIdType.RENT_PERIOD_ORDER.getValue(), ConstEnum.Category.RENT.getValue());
			}

			//保存退款记录
			insertRefundRecord(refundType, sourceType, refundMoney, orderId, getPtPayOrderId(result), rentPeriodOrder.getPartnerId(), rentPeriodOrder.getAgentId(), rentPeriodOrder.getAgentName(), rentPeriodOrder.getCustomerId(), rentPeriodOrder.getCustomerMobile(), rentPeriodOrder.getCustomerFullname(), rentPeriodOrder.getId());

			//客户退租金消费轨迹
			addCustomerPayTrack(rentPeriodOrder.getAgentId(), rentPeriodOrder.getCustomerId(), CustomerPayTrack.TrackType.BACK_RENT.getValue(),
					StringUtils.replaceEach("退租金，租金：${refundMoney}。",
							new String[]{"${refundMoney}"},
							new String[]{String.format("%.2f元", 1d * refundMoney / 100.0)}));
		}
		//保险退款
		else if(sourceType == CustomerRefundRecord.SourceType.ZDINSURANCE.getValue()){
			RentInsuranceOrder rentInsuranceOrder = rentInsuranceOrderMapper.find(sourceId);
			if (rentInsuranceOrder == null) {
				return DataResult.failResult("订单不存在", data);
			}
			if (StringUtils.isEmpty(orderId) &&  rentInsuranceOrder.getStatus() != RentPeriodOrder.Status.APPLY_REFUND.getValue()) {
				return DataResult.failResult("申请退款状态不对", data);
			}
			if (refundMoney > rentInsuranceOrder.getMoney()) {
				return DataResult.failResult("退款金额不能大于订单金额", data);
			}
			CustomerInstallmentRecordOrderDetail rentInsuranceOrderDetail = customerInstallmentRecordOrderDetailMapper.findOrderBySourceId(sourceId, ConstEnum.Category.RENT.getValue());

			int bizType = CustomerInOutMoney.BizType.IN_CUSTOMER_RENT_INSURANCE_REFUND.getValue();
			//退款
			DataResult result = doRefund(sourceType, refundType, bizType, rentInsuranceOrder.getPayType(), rentInsuranceOrder.getId(), data, null, refundMoney, rentInsuranceOrder.getCustomerId(), rentInsuranceOrder.getPartnerId(), userName, "保险退款", test);
			if(!result.isSuccess()){
				return result;
			}

			rentInsuranceOrderMapper.updateRefund(rentInsuranceOrder.getId(), refundMoney, new Date(), RentInsuranceOrder.Status.APPLY_REFUND.getValue(), RentInsuranceOrder.Status.REFUND_SUCCESS.getValue());

			//分期付订单处理
			if(rentInsuranceOrder.getPayType() == ConstEnum.PayType.INSTALLMENT.getValue()){
				//如果分期付没有完成就退款，这个时候订单的支付时间为空，需要更新上，否则订单无法进入统计
				if(rentInsuranceOrder.getPayTime() == null){
					rentInsuranceOrderMapper.updatePayTime(rentInsuranceOrder.getId(), new Date());
				}
				handleCustomerInstallment(sourceId, OrderId.OrderIdType.RENT_INSURANCE_ORDER.getValue(), ConstEnum.Category.RENT.getValue());
			}

			//保存退款记录
			insertRefundRecord(refundType, sourceType, refundMoney, orderId, getPtPayOrderId(result), rentInsuranceOrder.getPartnerId(), rentInsuranceOrder.getAgentId(), rentInsuranceOrder.getAgentName(), rentInsuranceOrder.getCustomerId(), rentInsuranceOrder.getCustomerMobile(), rentInsuranceOrder.getCustomerFullname(), rentInsuranceOrder.getId());
		}
		//多通道退款
		else if(sourceType == CustomerRefundRecord.SourceType.ZDMULTI.getValue()){
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
		if(sourceType == CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue()){
			bizType = PayOrderRefund.BizType.RENT_FOREGIFT_ORDER_REFUND.getValue();
		}else if(sourceType == CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue()){
			bizType = PayOrderRefund.BizType.RENT_PERIOD_ORDER_REFUND.getValue();
		}else if(sourceType == CustomerRefundRecord.SourceType.ZDINSURANCE.getValue()){
			bizType = PayOrderRefund.BizType.RENT_INSURANCE_ORDER_REFUND.getValue();
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

				} else if (payType == ConstEnum.PayType.ALIPAY_FW.getValue()) {
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
	 * @param rentForegiftOrder
	 */
	private void handleZdAgentForegift(RentForegiftOrder rentForegiftOrder) {
		//更新运营商押金余额 预留金额  押金余额比例
		List<Integer> statusList = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(),
				RentForegiftOrder.Status.APPLY_REFUND.getValue());
		//押金余额
		int foregiftBalance = rentForegiftOrderMapper.sumMoneyByAgent(rentForegiftOrder.getAgentId(), statusList);
		//运营商押金充值
		int deposit =  agentForegiftDepositOrderMapper.sumMoney(rentForegiftOrder.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftDepositOrder.Status.HAVE_PAID.getValue());
		//运营商提现
		int withdraw = agentForegiftWithdrawOrderMapper.sumMoney(rentForegiftOrder.getAgentId(), ConstEnum.Category.RENT.getValue(), AgentForegiftWithdrawOrder.Status.WITHDRAW_OK.getValue());

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
		if(agentMapper.updateZdForegift(rentForegiftOrder.getAgentId(), foregiftBalance, foregiftRemainMoney, foregiftBalanceRatio) > 0){
			//运营商押金流水
			AgentForegiftInOutMoney inOutMoney = new AgentForegiftInOutMoney();
			inOutMoney.setAgentId(rentForegiftOrder.getAgentId());
			inOutMoney.setCategory(ConstEnum.Category.RENT.getValue());
			inOutMoney.setMoney(-rentForegiftOrder.getMoney());
			inOutMoney.setBizType(AgentForegiftInOutMoney.BizType.OUT_CUSTOMER_FOREGIFT.getValue());
			inOutMoney.setBizId(rentForegiftOrder.getId());
			inOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
			inOutMoney.setBalance(foregiftBalance);
			inOutMoney.setRemainMoney(foregiftRemainMoney);
			inOutMoney.setRatio(foregiftBalanceRatio);
			inOutMoney.setOperator(rentForegiftOrder.getCustomerFullname());
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
