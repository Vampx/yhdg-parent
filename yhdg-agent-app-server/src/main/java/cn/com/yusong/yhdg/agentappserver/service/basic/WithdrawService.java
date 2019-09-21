package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.EstateMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Estate;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
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
    ShopInOutMoneyMapper shopInOutMoneyMapper;
    @Autowired
    EstateInOutMoneyMapper estateInOutMoneyMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    EstateMapper estateMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    AgentInOutMoneyMapper agentInOutMoneyMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    AgentCompanyInOutMoneyMapper agentCompanyInOutMoneyMapper;

    public Withdraw find(String id) {
        return withdrawMapper.find(id);
    }

    public List<Withdraw> findByEstate(int type, long estateId, int offset, int limit) {
        return withdrawMapper.findByEstate(type, estateId, offset, limit);
    }

    public List<Withdraw> findByShop(int type, String shopId, int offset, int limit) {
        return withdrawMapper.findByShop(type, shopId, offset, limit);
    }

    public List<Withdraw> findByAgentCompany(int type, String agentCompanyId, int offset, int limit) {
        return withdrawMapper.findByAgentCompany(type, agentCompanyId, offset, limit);
    }

    public List<Withdraw> findByAgent(int type, Integer agentId, int offset, int limit) {
        return withdrawMapper.findByAgent(type, agentId, offset, limit);
    }

    @Transactional(rollbackFor = Throwable.class)
    public String shopWithdraw(int accountType, String alipayAccount, int money, int serviceMoney, Shop shop) {

        int effect = shopMapper.updateBalance(shop.getId(), -money);
        if (effect == 0) {
            throw new BalanceNotEnoughException();
        }
        Agent agent = agentMapper.find(shop.getAgentId());

        Withdraw withdraw = new Withdraw();
        withdraw.setId(newOrderId(OrderId.OrderIdType.WITHDRAW_ORDER));
        withdraw.setPartnerId(agent.getPartnerId());
        withdraw.setType(Withdraw.Type.SHOP.getValue());
        withdraw.setShopId(shop.getId());
        withdraw.setShopName(shop.getShopName());
        withdraw.setAgentId(agent.getId());
        withdraw.setAgentName(agent.getAgentName());
        withdraw.setAccountType(accountType);
        withdraw.setAccountName(shop.getPayPeopleName());
        if (accountType == Withdraw.AccountType.WEIXIN_MP.getValue()) {
            withdraw.setWeixinAccount(shop.getPayPeopleMpOpenId());
        } else if (accountType == Withdraw.AccountType.ALIPAY.getValue()) {
            withdraw.setAlipayAccount(alipayAccount);
        }
        withdraw.setMoney(money);
        withdraw.setRealMoney(money - serviceMoney);
        withdraw.setServiceMoney(serviceMoney);
        withdraw.setStatus(Withdraw.Status.TO_AUDIT.getValue());
        withdraw.setCreateTime(new Date());
        withdraw.setBelongAgentId(shop.getAgentId());
        withdrawMapper.insert(withdraw);

        ShopInOutMoney shopInOutMoney = new ShopInOutMoney();
        shopInOutMoney.setShopId(withdraw.getShopId());
        shopInOutMoney.setType(ShopInOutMoney.Type.PAY.getValue());
        shopInOutMoney.setBizType(ShopInOutMoney.BizType.SHOP_PAY_WITHDRAW_ORDER.getValue());
        shopInOutMoney.setBizId(withdraw.getId());
        shopInOutMoney.setMoney(withdraw.getMoney());
        shopInOutMoney.setBalance(shopMapper.find(shop.getId()).getBalance());
        shopInOutMoney.setCreateTime(new Date());
        shopInOutMoneyMapper.insert(shopInOutMoney);

        return withdraw.getId();
    }

    @Transactional(rollbackFor = Throwable.class)
    public String agentWithdraw(int accountType, String alipayAccount, int money, int serviceMoney, Agent agent) {

        int effect = agentMapper.updateBalance(agent.getId(), -money);
        if (effect == 0) {
            throw new BalanceNotEnoughException();
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setId(newOrderId(OrderId.OrderIdType.WITHDRAW_ORDER));
        withdraw.setPartnerId(agent.getPartnerId());
        withdraw.setType(Withdraw.Type.AGENT.getValue());
        withdraw.setAgentId(agent.getId());
        withdraw.setAgentName(agent.getAgentName());
        withdraw.setAgentCode(agent.getAgentCode());
        withdraw.setAccountType(accountType);
        withdraw.setAccountName(agent.getPayPeopleName());
        if (accountType == Withdraw.AccountType.WEIXIN_MP.getValue()) {
            withdraw.setWeixinAccount(agent.getPayPeopleMpOpenId());
        } else if (accountType == Withdraw.AccountType.ALIPAY.getValue()) {
            withdraw.setAlipayAccount(alipayAccount);
        }
        withdraw.setMoney(money);
        withdraw.setRealMoney(money - serviceMoney);
        withdraw.setServiceMoney(serviceMoney);
        withdraw.setStatus(Withdraw.Status.TO_AUDIT.getValue());
        withdraw.setCreateTime(new Date());
        withdrawMapper.insert(withdraw);

        AgentInOutMoney inOutMoney = new AgentInOutMoney();
        inOutMoney.setAgentId(agent.getId());
        inOutMoney.setType(AgentInOutMoney.Type.OUT.getValue());
        inOutMoney.setBizType(AgentInOutMoney.BizType.OUT_AGENT_PAY_WITHDRAW_ORDER.getValue());
        inOutMoney.setBizId(withdraw.getId());
        inOutMoney.setMoney(-withdraw.getMoney());
        inOutMoney.setBalance(agentMapper.find(agent.getId()).getBalance());
        inOutMoney.setCreateTime(new Date());

        agentInOutMoneyMapper.insert(inOutMoney);

        return withdraw.getId();
    }

    @Transactional(rollbackFor = Throwable.class)
    public String estateWithdraw(int accountType, String alipayAccount, int money, int serviceMoney, Estate estate) {

        int effect = estateMapper.updateBalance(estate.getId(), -money);
        if (effect == 0) {
            throw new BalanceNotEnoughException();
        }
        Agent agent = agentMapper.find(estate.getAgentId());

        Withdraw withdraw = new Withdraw();
        withdraw.setId(newOrderId(OrderId.OrderIdType.WITHDRAW_ORDER));
        withdraw.setPartnerId(agent.getPartnerId());
        withdraw.setType(Withdraw.Type.ESTATE.getValue());
        withdraw.setEstateId(estate.getId());
        withdraw.setEstateName(estate.getEstateName());
        withdraw.setAgentId(agent.getId());
        withdraw.setAgentName(agent.getAgentName());
        withdraw.setAccountType(accountType);
        withdraw.setAccountName(estate.getPayPeopleName());
        if (accountType == Withdraw.AccountType.WEIXIN_MP.getValue()) {
            withdraw.setWeixinAccount(estate.getPayPeopleMpOpenId());
        } else if (accountType == Withdraw.AccountType.ALIPAY.getValue()) {
            withdraw.setAlipayAccount(alipayAccount);
        }
        withdraw.setMoney(money);
        withdraw.setRealMoney(money - serviceMoney);
        withdraw.setServiceMoney(serviceMoney);
        withdraw.setStatus(Withdraw.Status.TO_AUDIT.getValue());
        withdraw.setCreateTime(new Date());
        withdraw.setBelongAgentId(estate.getAgentId());
        withdrawMapper.insert(withdraw);

        EstateInOutMoney estateInOutMoney = new EstateInOutMoney();
        estateInOutMoney.setEstateId(withdraw.getEstateId());
        estateInOutMoney.setType(EstateInOutMoney.Type.PAY.getValue());
        estateInOutMoney.setBizType(EstateInOutMoney.BizType.ESTATE_PAY_WITHDRAW_ORDER.getValue());
        estateInOutMoney.setBizId(withdraw.getId());
        estateInOutMoney.setMoney(withdraw.getMoney());
        estateInOutMoney.setBalance(estateMapper.find(estate.getId()).getBalance());
        estateInOutMoney.setCreateTime(new Date());
        estateInOutMoneyMapper.insert(estateInOutMoney);

        return withdraw.getId();
    }

    @Transactional(rollbackFor = Throwable.class)
    public String agentCompanyWithdraw(int accountType, String alipayAccount, int money, int serviceMoney, AgentCompany agentCompany) {

        int effect = agentCompanyMapper.updateBalance(agentCompany.getId(), -money);
        if (effect == 0) {
            throw new BalanceNotEnoughException();
        }
        Agent agent = agentMapper.find(agentCompany.getAgentId());

        Withdraw withdraw = new Withdraw();
        withdraw.setId(newOrderId(OrderId.OrderIdType.WITHDRAW_ORDER));
        withdraw.setPartnerId(agent.getPartnerId());
        withdraw.setType(Withdraw.Type.AGENT_COMPANY.getValue());
        withdraw.setAgentCompanyId(agentCompany.getId());
        withdraw.setAgentCompanyName(agentCompany.getCompanyName());
        withdraw.setAgentId(agent.getId());
        withdraw.setAgentName(agent.getAgentName());
        withdraw.setAccountType(accountType);
        withdraw.setAccountName(agentCompany.getPayPeopleName());
        if (accountType == Withdraw.AccountType.WEIXIN_MP.getValue()) {
            withdraw.setWeixinAccount(agentCompany.getPayPeopleMpOpenId());
        } else if (accountType == Withdraw.AccountType.ALIPAY.getValue()) {
            withdraw.setAlipayAccount(alipayAccount);
        }
        withdraw.setMoney(money);
        withdraw.setRealMoney(money - serviceMoney);
        withdraw.setServiceMoney(serviceMoney);
        withdraw.setStatus(Withdraw.Status.TO_AUDIT.getValue());
        withdraw.setCreateTime(new Date());
        withdraw.setBelongAgentId(agentCompany.getAgentId());
        withdrawMapper.insert(withdraw);

        AgentCompanyInOutMoney agentCompanyInOutMoney = new AgentCompanyInOutMoney();
        agentCompanyInOutMoney.setAgentCompanyId(withdraw.getAgentCompanyId());
        agentCompanyInOutMoney.setType(AgentCompanyInOutMoney.Type.PAY.getValue());
        agentCompanyInOutMoney.setBizType(AgentCompanyInOutMoney.BizType.AGENT_COMPANY_PAY_WITHDRAW_ORDER.getValue());
        agentCompanyInOutMoney.setBizId(withdraw.getId());
        agentCompanyInOutMoney.setMoney(withdraw.getMoney());
        agentCompanyInOutMoney.setBalance(agentCompanyMapper.find(agentCompany.getId()).getBalance());
        agentCompanyInOutMoney.setCreateTime(new Date());
        agentCompanyInOutMoneyMapper.insert(agentCompanyInOutMoney);

        return withdraw.getId();
    }
}
