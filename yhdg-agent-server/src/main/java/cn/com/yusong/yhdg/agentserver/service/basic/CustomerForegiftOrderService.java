package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import com.alipay.api.AlipayApiException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerForegiftOrderService extends AbstractService {

    private static final Logger log = LogManager.getLogger(CustomerForegiftOrderService.class);

    @Autowired
    CustomerForegiftRefundDetailedMapper customerForegiftRefundDetailedMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AlipayPayOrderMapper alipayPayOrderMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    AlipayfwPayOrderMapper alipayfwPayOrderMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AppConfig appConfig;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;
    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentForegiftInOutMoneyMapper agentForegiftInOutMoneyMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;

    public Page findPage(CustomerForegiftOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(customerForegiftOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerForegiftOrder> list = customerForegiftOrderMapper.findPageResult(search);
        for (CustomerForegiftOrder customerForegiftOrder : list) {
            if (customerForegiftOrder.getStatus() != null) {
                customerForegiftOrder.setStatusName(CustomerForegiftOrder.Status.getName(customerForegiftOrder.getStatus().intValue()));
            }
        }
        page.setResult(customerForegiftOrderMapper.findPageResult(search));
        return page;
    }

    public CustomerForegiftOrder find(String id) {
        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(id);
        if (customerForegiftOrder != null) {
            if (customerForegiftOrder.getBatteryType() != null) {
                String type = findBatteryType(customerForegiftOrder.getBatteryType()).getTypeName();
                customerForegiftOrder.setBatteryTypeName(type);
            }
            if (customerForegiftOrder.getStatus() != null) {
                customerForegiftOrder.setStatusName(CustomerForegiftOrder.Status.getName(customerForegiftOrder.getStatus().intValue()));
            }
        }
        return customerForegiftOrder;
    }

    /**
     * 退款
     *
     * @param userName 操作人
     * @param customerForegiftOrder
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ExtResult refund(String userName, CustomerForegiftOrder customerForegiftOrder) throws AlipayApiException {
//        CustomerForegiftOrder order = customerForegiftOrderMapper.find(customerForegiftOrder.getId());
//        if (order == null) {
//            return ExtResult.failResult("订单不存在");
//        }
//
//        if (order.getStatus() != CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) {
//            return ExtResult.failResult("只有申请退款状态才可退款！");
//        }
//
//        if (customerForegiftOrder.getRefundMoney() > order.getMoney()) {
//            return ExtResult.failResult("退款金额不能大于订单金额");
//        }
//
//        if (order.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
//            AlipayPayOrder alipayPayOrder = alipayPayOrderMapper.findBySourceId(order.getId());
//            if (alipayPayOrder == null) {
//                return ExtResult.failResult(ConstEnum.PayType.ALIPAY.getName() + "订单不存在");
//            }
//            alipayPayOrderMapper.refundOk(alipayPayOrder.getId(), order.getRefundMoney(), new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
//        } else if (order.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
//            WeixinPayOrder weixinPayOrder = weixinPayOrderMapper.findBySourceId(order.getId());
//            if (weixinPayOrder == null) {
//                return ExtResult.failResult(ConstEnum.PayType.WEIXIN.getName() + "订单不存在");
//            }
//            int refundFee = customerForegiftOrder.getRefundMoney();
//            weixinPayOrderMapper.refundOk(weixinPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
//
//        } else if (order.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
//            WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderMapper.findBySourceId(order.getId());
//            if (weixinmpPayOrder == null) {
//                return ExtResult.failResult(ConstEnum.PayType.WEIXIN_MP.getName() + "订单不存在");
//            }
//            int refundFee = customerForegiftOrder.getRefundMoney();
//            weixinmpPayOrderMapper.refundOk(weixinmpPayOrder.getId(), refundFee, new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
//
//        } else if (order.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
//            AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderMapper.findBySourceId(order.getId());
//            if (alipayfwPayOrder == null) {
//                return ExtResult.failResult(ConstEnum.PayType.ALIPAY_FW.getName() + "订单不存在");
//            }
//            alipayPayOrderMapper.refundOk(alipayfwPayOrder.getId(), order.getRefundMoney(), new Date(), PayOrder.Status.SUCCESS.getValue(), PayOrder.Status.REFUND_SUCCESS.getValue());
//        }
//
//        //余额流水
//        CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
//        customerInOutMoney.setCustomerId(order.getCustomerId());
//        customerInOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_FOREGIFT_REFUND.getValue());
//        customerInOutMoney.setBizId(order.getId());
//        customerInOutMoney.setMoney(customerForegiftOrder.getRefundMoney());
//        customerInOutMoney.setCreateTime(new Date());
//        customerInOutMoneyMapper.insert(customerInOutMoney);
//        //更新余额
//        customerMapper.updateBalance(order.getCustomerId(), customerForegiftOrder.getRefundMoney(), 0);
//
//        //更新客户运营商余额
//        CustomerAgentBalance customerAgentBalance = customerAgentBalanceMapper.findByCustomerId(order.getCustomerId());
//        Customer customer = customerMapper.find(order.getCustomerId());
//        if (customerAgentBalance != null) {
//            Agent agent = agentMapper.find(customerAgentBalance.getAgentId());
//            AgentForegiftInOutMoney agentForegiftInOutMoney = new AgentForegiftInOutMoney();
//            agentForegiftInOutMoney.setAgentId(customerAgentBalance.getAgentId());
//            agentForegiftInOutMoney.setSourceType(AgentForegiftInOutMoney.SourceType.CASH_PLEDGE_PAY.getValue());
//            agentForegiftInOutMoney.setSourceId(order.getId());
//            agentForegiftInOutMoney.setBizType(AgentForegiftInOutMoney.Type.OUT.getValue());
//            agentForegiftInOutMoney.setMoney(customerForegiftOrder.getRefundMoney());
//            agentForegiftInOutMoney.setBalance(agent.getForegiftBalance()+customerForegiftOrder.getRefundMoney());
//            agentForegiftInOutMoney.setOperator(customer.getFullname());
//            agentForegiftInOutMoney.setCreateTime(new Date());
//            agentForegiftInOutMoneyMapper.insert(agentForegiftInOutMoney);
//
//            agentMapper.updateForegift(customerAgentBalance.getAgentId(),
//                    agent.getForegiftBalance()-customerForegiftOrder.getRefundMoney(),
//                    agent.getForegiftRemainMoney()-customerForegiftOrder.getRefundMoney(),
//                    (agent.getForegiftRemainMoney()-customerForegiftOrder.getRefundMoney())/(agent.getForegiftBalance()-customerForegiftOrder.getRefundMoney())*100);
//        }
//
//        //更新押金订单
//        customerForegiftOrderMapper.updateStatus(customerForegiftOrder.getId(),
//                CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(),
//                customerForegiftOrder.getRefundMoney(), userName, new Date(),
//                customerForegiftOrder.getRefundPhoto(),
//                customerForegiftOrder.getMemo(), new Date());
//
////        //将保险订单设置为已失效
////        insuranceOrderMapper.updateStatus(order.getCustomerId(), InsuranceOrder.Status.NOT_AVAIL.getValue());
////        //将用户的包时段订单设置为已失效
////        packetPeriodOrderMapper.updateStatus(order.getCustomerId(), PacketPeriodOrder.Status.NOT_AVAIL.getValue());
//        //清空客户绑定押金信息
//        customerExchangeInfoMapper.delete(order.getCustomerId());
//        //解绑运营商
//        customerMapper.clearAgentId(order.getCustomerId());
//        //押金退款明细
//        Integer num = customerForegiftRefundDetailedMapper.findMaxNum(customerForegiftOrder.getId());
//        num = num == null ? 0 : num;
//        CustomerForegiftRefundDetailed customerForegiftRefundDetailed = new CustomerForegiftRefundDetailed();
//        customerForegiftRefundDetailed.setId(customerForegiftOrder.getId());
//        customerForegiftRefundDetailed.setNum(++num);
//        customerForegiftRefundDetailed.setRefundMoney(customerForegiftOrder.getRefundMoney());
//        customerForegiftRefundDetailed.setRefundOperator(userName);
//        customerForegiftRefundDetailed.setRefundPhoto(customerForegiftOrder.getRefundPhoto());
//        customerForegiftRefundDetailed.setRefundType(customerForegiftOrder.getPayType());
//        customerForegiftRefundDetailed.setCreateTime(new Date());
//
//        customerForegiftRefundDetailedMapper.insert(customerForegiftRefundDetailed);

        return ExtResult.successResult();
    }

    public List<CustomerForegiftOrder> findCanRefundByCustomerId(Long customerId) {
        return customerForegiftOrderMapper.findCanRefundByCustomerId(customerId);
    }

}
