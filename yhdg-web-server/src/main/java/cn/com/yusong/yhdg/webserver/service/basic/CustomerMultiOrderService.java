package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import com.alipay.api.AlipayApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerMultiOrderService extends AbstractService {

    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    CustomerMultiPayDetailMapper customerMultiPayDetailMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerRefundRecordMapper customerRefundRecordMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    AgentMapper agentMapper;

    public Page findPage(CustomerMultiOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(customerMultiOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerMultiOrder> list = customerMultiOrderMapper.findPageResult(search);
        for (CustomerMultiOrder customerMultiOrder : list) {
            if (customerMultiOrder.getPartnerId() != null) {
                customerMultiOrder.setPartnerName(partnerMapper.find(customerMultiOrder.getPartnerId()).getPartnerName());
            }
        }
        page.setResult(list);
        return page;
    }

    public CustomerMultiOrder find (long id) {
        return customerMultiOrderMapper.find(id);
    }

    public List<CustomerMultiOrder> findCanRefund(Long id, int type, int status) {
        return customerMultiOrderMapper.findCanRefund(id, type, status);
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
        //多通道退款
         if(sourceType == CustomerRefundRecord.SourceType.ZCMULTI.getValue()){
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
        }
        data.put("thirdPay", thirdPay);
        return DataResult.successResult(data);
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
            refundRecord.setFullname(customerFullname != null ? customerFullname : customerMobile);
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

    public void addCustomerPayTrack(Integer agentId, Long customerId, int trackType, String memo){
        Agent agent = agentMapper.find(agentId);
        Customer customer = customerMapper.find(customerId);

        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname() != null ? customer.getFullname() : customer.getMobile());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(trackType);
        customerPayTrack.setMemo(memo);
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);
    }

}
