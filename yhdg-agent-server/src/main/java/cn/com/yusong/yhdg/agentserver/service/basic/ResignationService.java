package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class ResignationService extends AbstractService {
    @Autowired
    ResignationMapper resignationMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    CustomerAgentBalanceMapper customerAgentBalanceMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    public Resignation find(long id) {
        return resignationMapper.find(id);
    }

    public Page findPage(Resignation search) {
        Page page = search.buildPage();
        page.setTotalItems(resignationMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(resignationMapper.findPageResult(search));
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(Resignation search) {
//        Resignation resignation = resignationMapper.find(search.getId());
//        if (resignation.getState() != Resignation.State.AUDIT.getValue()) {
//            return ExtResult.failResult("离职申请状态已变,请刷新页面!");
//        }
//        Agent agent = agentMapper.find(resignation.getAgentId());
//        if (agent == null) {
//            return ExtResult.failResult("所属运营商不存在!");
//        }
//        if (search.getState() == Resignation.State.ADOPT.getValue()) {
//            Customer customer = customerMapper.find(resignation.getCustomerId());
//            if (customerExchangeBatteryMapper.findByCustomerId(customer.getId()) != null) {
//                return ExtResult.failResult("骑手下存在未退租电池,不可离职!");
//            }
//            CustomerAgentBalance customerAgentBalance = customerAgentBalanceMapper.findByCustomerId(customer.getId());
//            if (customerAgentBalance != null) {
//                return ExtResult.failResult("骑手运营商资金小于0,不可离职!");
//            }
//            int total = customerAgentBalanceMapper.resignation(customer.getId(), customerAgentBalance.getAgentBalance());
//            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customer.getId());
//            CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(customerExchangeInfo.getForegiftOrderId());
//            if (customerForegiftOrder == null) {
//                return ExtResult.failResult("押金订单不存在");
//            }
//            if (customerExchangeInfo.getForegift() == 0) {
//                List<Integer> statusList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
//                customerForegiftOrderMapper.updateStatus(customerForegiftOrder.getId(),
//                        CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(),
//                        0, search.getOperator(), new Date(),
//                        null,
//                        null, new Date());
//
////                customerMapper.setOldBatteryForegiftOrderId(customer.getId());
//                customerExchangeInfoMapper.clearBatteryForegiftOrderId(customer.getId(), statusList);
//            }
//            if (total != 1) {
//                return ExtResult.failResult("修改失败");
//            }
//            if (customerAgentBalance.getAgentBalance() > 0) {
//                CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
//                customerInOutMoney.setMoney(-customerAgentBalance.getAgentBalance().intValue());
//                customerInOutMoney.setCustomerId(customer.getId());
//                customerInOutMoney.setBizId(search.getId().toString());
//                customerInOutMoney.setCreateTime(new Date());
//                customerInOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_RESIGNATION.getValue());
//                customerInOutMoneyMapper.insert(customerInOutMoney);
//
//                agentMapper.updateBalance(customerAgentBalance.getAgentId(), customerAgentBalance.getAgentBalance());
//
//                AgentInOutMoney agentInOutMoney = new AgentInOutMoney();
//                agentInOutMoney.setMoney(customerAgentBalance.getAgentBalance().intValue());
//                agentInOutMoney.setBalance(agent.getBalance() + customerAgentBalance.getAgentBalance().intValue());
//                agentInOutMoney.setAgentId(agent.getId());
//                agentInOutMoney.setBizId("骑手(" + customer.getMobile() + ")离职退款");
//                agentInOutMoney.setCreateTime(new Date());
//                agentInOutMoney.setBizType(AgentInOutMoney.BizType.IN_AGENT_CUSTOMER_ROLLBACK.getValue());
//                agentInOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
//                agentInOutMoney.setOperator(search.getOperator());
//                agentInOutMoneyMapper.insert(agentInOutMoney);
//            }
//        }
//
//        search.setHandleTime(new Date());
//        resignationMapper.update(search);
        return ExtResult.successResult();
    }

}
