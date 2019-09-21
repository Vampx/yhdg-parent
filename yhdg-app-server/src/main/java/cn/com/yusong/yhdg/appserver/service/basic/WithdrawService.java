package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerInOutMoneyMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.WithdrawMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.OrderId;
import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class WithdrawService extends AbstractService{
    @Autowired
    WithdrawMapper withdrawMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;

    public Withdraw find(String id) {
        return withdrawMapper.find(id);
    }

    public List<Withdraw> findByCustomer(int type, long customerId, int offset, int limit) {
        return withdrawMapper.findByCustomer(type, customerId, offset, limit);
    }

    @Transactional(rollbackFor = Throwable.class)
    public String insert(int accountType, String alipayAccount, String wxOpenId, int money, int serviceMoney, Customer customer) {

        int effect = customerMapper.updateBalance(customer.getId(), -money, 0);
        if (effect == 0) {
            throw new BalanceNotEnoughException();
        }

        if (accountType == Withdraw.AccountType.ALIPAY.getValue() && !alipayAccount.equals(customer.getAlipayAccount())) {
            customerMapper.updateAlipayAccount(customer.getId(), alipayAccount);
        } else if (accountType == Withdraw.AccountType.WEIXIN.getValue() && !wxOpenId.equals(customer.getWxOpenId())) {
            customerMapper.updateWxOpenId(customer.getId(), wxOpenId);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setId(newOrderId(OrderId.OrderIdType.WITHDRAW_ORDER));
        withdraw.setPartnerId(customer.getPartnerId());
        withdraw.setType(Withdraw.Type.CUSTOMER.getValue());
        withdraw.setCustomerId(customer.getId());
        withdraw.setCustomerFullname(customer.getFullname());
        withdraw.setCustomerMobile(customer.getMobile());
        withdraw.setAccountType(accountType);
        withdraw.setAccountName(customer.getFullname());
        if (accountType == Withdraw.AccountType.WEIXIN_MP.getValue()) {
            withdraw.setWeixinAccount(customer.getMpOpenId());
        } else if (accountType == Withdraw.AccountType.ALIPAY.getValue()) {
            withdraw.setAlipayAccount(alipayAccount);
        } else if (accountType == Withdraw.AccountType.WEIXIN.getValue()) {
            withdraw.setWxOpenId(wxOpenId);
        }
        withdraw.setMoney(money);
        withdraw.setRealMoney(money - serviceMoney);
        withdraw.setServiceMoney(serviceMoney);
        withdraw.setStatus(Withdraw.Status.TO_AUDIT.getValue());
        withdraw.setCreateTime(new Date());
        withdraw.setBelongAgentId(customer.getAgentId());
        effect = withdrawMapper.insert(withdraw);

        Customer customer1 = customerMapper.find(customer.getId());
        CustomerInOutMoney customerInOutMoney = new CustomerInOutMoney();
        customerInOutMoney.setCustomerId(withdraw.getCustomerId());
        customerInOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
        customerInOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_WITHDRAW.getValue());
        customerInOutMoney.setBizId(withdraw.getId());
        customerInOutMoney.setMoney(-withdraw.getMoney());
        customerInOutMoney.setBalance(customer1.getBalance() + customer1.getGiftBalance());
        customerInOutMoney.setCreateTime(new Date());
        customerInOutMoneyMapper.insert(customerInOutMoney);

        return withdraw.getId();
    }
}
