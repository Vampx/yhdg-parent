package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.webserver.persistence.basic.*;
import cn.com.yusong.yhdg.webserver.persistence.hdg.EstateMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class WithdrawService extends AbstractService {
    @Autowired
    WithdrawMapper withdrawMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    EstateMapper estateMapper;
    @Autowired
    ShopInOutMoneyMapper shopInOutMoneyMapper;
    @Autowired
    AgentCompanyInOutMoneyMapper agentCompanyInOutMoneyMapper;
    @Autowired
    EstateInOutMoneyMapper estateInOutMoneyMapper;
    @Autowired
    PlatformAccountMapper platformAccountMapper;
    @Autowired
    PlatformAccountInOutMoneyMapper platformAccountInOutMoneyMapper;
    @Autowired
    WithdrawTransferLogMapper withdrawTransferLogMapper;
    @Autowired
    PartnerMapper partnerMapper;


    public Withdraw find(String id){
        Withdraw withdraw = withdrawMapper.find(id);
        setParameters(withdraw);
        return withdraw;
    }

    public Page findPage(Withdraw search) {
        Page page = search.buildPage();
        page.setTotalItems(withdrawMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Withdraw> withdrawList = withdrawMapper.findPageResult(search);
        for(Withdraw withdraw:withdrawList){
            setParameters(withdraw);
        }
        page.setResult(withdrawList);

        return page;
    }

    public void setParameters(Withdraw withdraw){
        Partner partner = partnerMapper.find(withdraw.getPartnerId());
        withdraw.setBelongPartnerName(partner.getPartnerName());
        if(withdraw.getType() == Withdraw.Type.CUSTOMER.getValue()){

            if(withdraw.getCustomerId() != null) {
                Customer customer = customerMapper.find(withdraw.getCustomerId());
                if (customer.getAgentId() != null) {
                    Agent agent = agentMapper.find(customer.getAgentId());
                    withdraw.setBelongAgentName(agent.getAgentName());
                }
                withdraw.setBalance(customer.getBalance());
            }
        }else if(withdraw.getType() == Withdraw.Type.SHOP.getValue()){

            if(withdraw.getShopId()!=null) {
                Shop shop = shopMapper.find(withdraw.getShopId());
                if (shop.getAgentId() != null) {
                    Agent agent = agentMapper.find(shop.getAgentId());
                    withdraw.setBelongAgentName(agent.getAgentName());
                }
                withdraw.setBalance(shop.getBalance());
            }

        } else if (withdraw.getType() == Withdraw.Type.AGENT_COMPANY.getValue()) {
            if (withdraw.getAgentCompanyId() != null) {
                AgentCompany agentCompany = agentCompanyMapper.find(withdraw.getAgentCompanyId());
                if (agentCompany.getAgentId() != null) {
                    Agent agent = agentMapper.find(agentCompany.getAgentId());
                    withdraw.setBelongAgentName(agent.getAgentName());
                }
                withdraw.setBalance(agentCompany.getBalance());
            }
        } else if (withdraw.getType() == Withdraw.Type.AGENT.getValue()) {

            if (withdraw.getAgentId() != null) {
                Agent agent = agentMapper.find(withdraw.getAgentId());
                withdraw.setBelongAgentName(agent.getAgentName());
                withdraw.setBalance(agent.getBalance());
            }
        } else if (withdraw.getType() == Withdraw.Type.ESTATE.getValue()) {

            if (withdraw.getEstateId() != null) {
                Estate estate = estateMapper.find(withdraw.getEstateId());
                if (estate.getAgentId() != null) {
                    Agent agent = agentMapper.find(estate.getAgentId());
                    withdraw.setBelongAgentName(agent.getAgentName());
                }
                withdraw.setBalance(estate.getBalance());
            }
        } else if (withdraw.getType() == Withdraw.Type.SYSTEM.getValue()) {
            if (withdraw.getPlatformAccountId() != null) {
                PlatformAccount platformAccount = platformAccountMapper.find(withdraw.getPlatformAccountId());
                withdraw.setBalance(platformAccount.getBalance());
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult audit(String id, int fromStatus, int toStatus, String auditUser, String auditMemo) {
        Withdraw withdraw = withdrawMapper.find(id);
        if (withdraw == null) {
            return ExtResult.failResult("记录不存在");
        }
        if (withdraw.getStatus() != Withdraw.Status.TO_AUDIT.getValue() && withdraw.getStatus() != Withdraw.Status.WITHDRAW_NO.getValue()) {
            return ExtResult.failResult("提现状态错误");
        }

        if (toStatus == Withdraw.Status.AUDIT_OK.getValue()) {
            withdrawMapper.audit(id, fromStatus, toStatus, new Date(), auditUser, auditMemo);

        } else if (toStatus == Withdraw.Status.AUDIT_NO.getValue()) {
            if (withdrawMapper.audit(id, fromStatus, toStatus, new Date(), auditUser, auditMemo) > 0) {
                if (withdraw.getType() == Withdraw.Type.CUSTOMER.getValue()) {
                    if (customerMapper.updateBalance(withdraw.getCustomerId(), withdraw.getMoney(), 0) > 0) {
                        Customer customer = customerMapper.find(withdraw.getCustomerId());
                        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
                        inOutMoney.setCustomerId(withdraw.getCustomerId());
                        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_WITHDRAW_REFUND.getValue());
                        inOutMoney.setBizId(withdraw.getId());
                        inOutMoney.setType(CustomerInOutMoney.Type.IN.getValue());
                        inOutMoney.setMoney(withdraw.getMoney());
                        inOutMoney.setBalance(customer.getBalance() + customer.getGiftBalance());
                        inOutMoney.setCreateTime(new Date());
                        customerInOutMoneyMapper.insert(inOutMoney);
                    }


                } else if (withdraw.getType() == Withdraw.Type.AGENT.getValue()) {
                    if (agentMapper.updateBalance(withdraw.getAgentId(), withdraw.getMoney()) > 0) {
                        Agent agent = agentMapper.find(withdraw.getAgentId());
                        AgentInOutMoney inOutMoney = new AgentInOutMoney();
                        inOutMoney.setAgentId(withdraw.getAgentId());
                        inOutMoney.setBizType(AgentInOutMoney.BizType.IN_WITHDRAW_REFUND.getValue());
                        inOutMoney.setBizId(withdraw.getId());
                        inOutMoney.setType(AgentInOutMoney.Type.IN.getValue());
                        inOutMoney.setMoney(withdraw.getMoney());
                        inOutMoney.setBalance(agent.getBalance());
                        inOutMoney.setOperator(auditUser);
                        inOutMoney.setCreateTime(new Date());
                        agentInOutMoneyMapper.insert(inOutMoney);
                    }

                } else if (withdraw.getType() == Withdraw.Type.SHOP.getValue()) {
                    if (shopMapper.updateBalance(withdraw.getShopId(), withdraw.getMoney()) > 0) {
                        Shop shop = shopMapper.find(withdraw.getShopId());
                        ShopInOutMoney inOutMoney = new ShopInOutMoney();
                        inOutMoney.setShopId(withdraw.getShopId());
                        inOutMoney.setBizType(ShopInOutMoney.BizType.WITHDRAW_REFUND.getValue());
                        inOutMoney.setBizId(withdraw.getId());
                        inOutMoney.setType(ShopInOutMoney.Type.INCOME.getValue());
                        inOutMoney.setMoney(withdraw.getMoney());
                        inOutMoney.setBalance(shop.getBalance());
                        inOutMoney.setOperator(auditUser);
                        inOutMoney.setCreateTime(new Date());
                        shopInOutMoneyMapper.insert(inOutMoney);
                    }

                } else if (withdraw.getType() == Withdraw.Type.AGENT_COMPANY.getValue()) {
                    if (agentCompanyMapper.updateBalance(withdraw.getAgentCompanyId(), withdraw.getMoney()) > 0) {
                        AgentCompany agentCompany = agentCompanyMapper.find(withdraw.getAgentCompanyId());
                        AgentCompanyInOutMoney inOutMoney = new AgentCompanyInOutMoney();
                        inOutMoney.setAgentCompanyId(withdraw.getAgentCompanyId());
                        inOutMoney.setBizType(AgentCompanyInOutMoney.BizType.WITHDRAW_REFUND.getValue());
                        inOutMoney.setBizId(withdraw.getId());
                        inOutMoney.setType(AgentCompanyInOutMoney.Type.INCOME.getValue());
                        inOutMoney.setMoney(withdraw.getMoney());
                        inOutMoney.setBalance(agentCompany.getBalance());
                        inOutMoney.setOperator(auditUser);
                        inOutMoney.setCreateTime(new Date());
                        agentCompanyInOutMoneyMapper.insert(inOutMoney);
                    }

                } else if (withdraw.getType() == Withdraw.Type.ESTATE.getValue()) {
                    if (estateMapper.updateBalance(withdraw.getEstateId(), withdraw.getMoney()) > 0) {
                        Estate estate = estateMapper.find(withdraw.getEstateId());
                        EstateInOutMoney inOutMoney = new EstateInOutMoney();
                        inOutMoney.setEstateId(withdraw.getEstateId());
                        inOutMoney.setBizType(EstateInOutMoney.BizType.WITHDRAW_REFUND.getValue());
                        inOutMoney.setBizId(withdraw.getId());
                        inOutMoney.setType(EstateInOutMoney.Type.INCOME.getValue());
                        inOutMoney.setMoney(withdraw.getMoney());
                        inOutMoney.setBalance(estate.getBalance());
                        inOutMoney.setOperator(auditUser);
                        inOutMoney.setCreateTime(new Date());
                        estateInOutMoneyMapper.insert(inOutMoney);
                    }

                } else if (withdraw.getType() == Withdraw.Type.SYSTEM.getValue()) {
                    if (platformAccountMapper.updateBalance(withdraw.getPlatformAccountId(), withdraw.getMoney()) > 0) {
                        PlatformAccount platformAccount = platformAccountMapper.find(withdraw.getPlatformAccountId());
                        PlatformAccountInOutMoney inOutMoney = new PlatformAccountInOutMoney();
                        inOutMoney.setPlatformAccountId(withdraw.getPlatformAccountId());
                        inOutMoney.setBizType(PlatformAccountInOutMoney.BizType.IN_WITHDRAW_REFUND.getValue());
                        inOutMoney.setBizId(withdraw.getId());
                        inOutMoney.setType(PlatformAccountInOutMoney.Type.IN.getValue());
                        inOutMoney.setMoney(withdraw.getMoney());
                        inOutMoney.setBalance(platformAccount.getBalance());
                        inOutMoney.setOperator(auditUser);
                        inOutMoney.setCreateTime(new Date());
                        platformAccountInOutMoneyMapper.insert(inOutMoney);
                    }
                }
            }
        }

        return ExtResult.successResult();
    }

    public ExtResult reset(String id, String accountName, String weixinAccount, String alipayAccount, String wxOpenId, String auditUser) {
        withdrawMapper.reset(id, Withdraw.Status.WITHDRAW_NO.getValue(), Withdraw.Status.AUDIT_OK.getValue(), auditUser,
        new Date(), accountName,
        weixinAccount, alipayAccount, wxOpenId);

        WithdrawTransferLog log = new WithdrawTransferLog();
        log.setWithdrawId(id);
        log.setOperatorName(auditUser);
        log.setContent(String.format("账户重置, accountName=%s, weixinAccount=%s, alipayAccount=%s, wxOpenId=%s", accountName, weixinAccount, alipayAccount, wxOpenId));
        log.setCreateTime(new Date());
        withdrawTransferLogMapper.insert(log);

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult insert(int accountType, int money, int serviceMoney, PlatformAccount platformAccount, String username) {

        int effect = platformAccountMapper.updateBalance(platformAccount.getId(), -money);
        if (effect == 0) {
            throw new BalanceNotEnoughException();
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setId(newOrderId(OrderId.OrderIdType.WITHDRAW_ORDER));
        withdraw.setPlatformAccountId(platformAccount.getId());
        withdraw.setPlatformAccountName(platformAccount.getPartnerName());
        withdraw.setPartnerId(platformAccount.getId());
        withdraw.setType(Withdraw.Type.SYSTEM.getValue());
        withdraw.setAccountType(accountType);

        if (accountType == Withdraw.AccountType.WEIXIN_MP.getValue()) {
            withdraw.setAccountName(platformAccount.getMpAccountName());
            withdraw.setWeixinAccount(platformAccount.getMpOpenId());

        } else if (accountType == Withdraw.AccountType.ALIPAY.getValue()) {
            withdraw.setAccountName(platformAccount.getAlipayAccountName());
            withdraw.setAlipayAccount(platformAccount.getAlipayAccount());
        }

        withdraw.setMoney(money);
        withdraw.setRealMoney(money-serviceMoney);
        withdraw.setServiceMoney(serviceMoney);
        withdraw.setStatus(Withdraw.Status.AUDIT_OK.getValue());
        withdraw.setCreateTime(new Date());
        withdraw.setAuditTime(new Date());
        withdraw.setHandleTime(new Date());
        withdraw.setAuditUser(username);
        withdrawMapper.insert(withdraw);

//        PlatformAccount platformAccount1 = platformAccountMapper.find(platformAccount.getId());
        PlatformAccountInOutMoney platformAccountInOutMoney = new PlatformAccountInOutMoney();
        platformAccountInOutMoney.setPlatformAccountId(withdraw.getPlatformAccountId());
        platformAccountInOutMoney.setType(PlatformAccountInOutMoney.Type.OUT.getValue());
        platformAccountInOutMoney.setBizType(PlatformAccountInOutMoney.BizType.OUT_WITHDRAW.getValue());
        platformAccountInOutMoney.setBizId(withdraw.getId());
        platformAccountInOutMoney.setMoney(-withdraw.getMoney());
        platformAccountInOutMoney.setBalance(platformAccount.getBalance()-money);
        platformAccountInOutMoney.setCreateTime(new Date());
        platformAccountInOutMoney.setOperator(username);
        platformAccountInOutMoneyMapper.insert(platformAccountInOutMoney);

        return ExtResult.successResult();
    }


    @Transactional(rollbackFor = Throwable.class)
    public ExtResult offline(String id) {
        Withdraw withdraw = withdrawMapper.find(id);

        withdrawMapper.transfer(id, Withdraw.Status.TO_AUDIT.getValue(), Withdraw.Status.OFFLINE.getValue(), new Date());

        WithdrawTransferLog log = new WithdrawTransferLog();
        log.setWithdrawId(withdraw.getId());
        log.setOperatorName("system");
        log.setContent("线下提现");
        log.setCreateTime(new Date());
        withdrawTransferLogMapper.insert(log);

        //因为是线下体现 所以商户上没有流水

        if (withdraw.getServiceMoney() > 0) {
            platformAccountMapper.updateBalance(withdraw.getPartnerId(), withdraw.getServiceMoney());

            PlatformAccountInOutMoney platformAccountInOutMoney = new PlatformAccountInOutMoney();
            platformAccountInOutMoney.setPlatformAccountId(withdraw.getPartnerId());
            platformAccountInOutMoney.setBizType(PlatformAccountInOutMoney.BizType.IN_WITHDRAW_SERVICE_MONEY.getValue());
            platformAccountInOutMoney.setBizId(withdraw.getId());
            platformAccountInOutMoney.setType(PlatformAccountInOutMoney.Type.IN.getValue());
            platformAccountInOutMoney.setMoney(withdraw.getServiceMoney());
            platformAccountInOutMoney.setBalance(platformAccountMapper.find(withdraw.getPartnerId()).getBalance());
            platformAccountInOutMoney.setOperator("system");
            platformAccountInOutMoney.setCreateTime(new Date());
            platformAccountInOutMoneyMapper.insert(platformAccountInOutMoney);
        }

        return ExtResult.successResult();
    }
}
