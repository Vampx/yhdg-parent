package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class LaxinService {
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    PartnerMpOpenIdMapper partnerMpOpenIdMapper;
    @Autowired
    PartnerFwOpenIdMapper partnerFwOpenIdMapper;

    public Laxin find(long id) {
        return laxinMapper.find(id);
    }

    public List<Laxin> findByMobile(int partnerId, String mobile) {
        return laxinMapper.findByMobile(partnerId, mobile);
    }

    public Laxin findByAgentMobile(int agentId, String mobile) {
        return laxinMapper.findByAgentMobile(agentId, mobile);
    }

    public int updatePassword(long id, String password) {
        return laxinMapper.updatePassword(id, password);
    }

    public int insert(LaxinSetting laxinSetting, String mobile, String password, String unionOpenId, int type) {
        Agent agent = agentMapper.find(laxinSetting.getAgentId());
        if (agent == null) {
            return 0;
        }

        Customer customer = null;
        if (StringUtils.isNotEmpty(unionOpenId)) {
            customer = customerMapper.findByMobile(agent.getPartnerId(), mobile);
            String[] openIdArray = StringUtils.split(unionOpenId, ":");
            String openId = null;
            if (openIdArray.length == 3) {
                openId = openIdArray[1];
            }

            if (customer == null) {
                if (StringUtils.isNotEmpty(openId)) {
                    if (type == 1) {
                        PartnerMpOpenId oi = partnerMpOpenIdMapper.findByOpenId(agent.getPartnerId(), openId);
                        if (oi != null) {
                            customer = new Customer();
                            customer.setPartnerId(agent.getPartnerId());
                            customer.setBalance(0);    //余额默认为0
                            customer.setGiftBalance(0);
                            customer.setIsActive(ConstEnum.Flag.TRUE.getValue());    //默认活动状态为 是
                            customer.setRegisterType(Customer.RegisterType.WEIXIN_MP.getValue());
                            customer.setCreateTime(new Date());
                            customer.setMpOpenId(openId);
                            customer.setMobile(mobile);
                            customer.setNickname(oi.getNickname());
                            customer.setPhotoPath(oi.getPhotoPath());
                            customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
                            customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
                            customer.setAuthStatus(Customer.AuthStatus.NOT.getValue());
                            customerMapper.insert(customer);
                            partnerMpOpenIdMapper.updateCustomerId(agent.getPartnerId(), openId, customer.getId());
                        }
                    } else {
                        PartnerFwOpenId oi = partnerFwOpenIdMapper.findByOpenId(agent.getPartnerId(), openId);
                        if (oi != null) {
                            customer = new Customer();
                            customer.setPartnerId(agent.getPartnerId());
                            customer.setBalance(0);    //余额默认为0
                            customer.setGiftBalance(0);
                            customer.setIsActive(ConstEnum.Flag.TRUE.getValue());    //默认活动状态为 是
                            customer.setRegisterType(Customer.RegisterType.ALI_FW.getValue());
                            customer.setCreateTime(new Date());
                            customer.setFwOpenId(openId);
                            customer.setMobile(mobile);
                            customer.setNickname(oi.getNickname());
                            customer.setPhotoPath(oi.getPhotoPath());
                            customer.setHdForegiftStatus(Customer.HdForegiftStatus.UN_PAID.getValue());
                            customer.setZdForegiftStatus(Customer.ZdForegiftStatus.UN_PAID.getValue());
                            customer.setAuthStatus(Customer.AuthStatus.NOT.getValue());
                            customerMapper.insert(customer);
                            partnerFwOpenIdMapper.updateCustomerId(agent.getPartnerId(), openId, customer.getId());
                        }
                    }

                }
            } else {
                if (StringUtils.isNotEmpty(openId)) {
                    if (StringUtils.isEmpty(customer.getMpOpenId()) && type == 1) {
                        PartnerMpOpenId oi = partnerMpOpenIdMapper.findByOpenId(agent.getPartnerId(), openId);
                        if (oi != null) {
                            customerMapper.updateMpOpenId(customer.getId(), openId, oi.getNickname(), oi.getPhotoPath());
                            partnerMpOpenIdMapper.updateCustomerId(agent.getPartnerId(), openId, customer.getId());
                        }

                    } else if (StringUtils.isEmpty(customer.getFwOpenId()) && type == 2) {
                        PartnerFwOpenId oi = partnerFwOpenIdMapper.findByOpenId(agent.getPartnerId(), openId);
                        if (oi != null) {
                            customerMapper.updateFwOpenId(customer.getId(), openId, oi.getNickname(), oi.getPhotoPath());
                            partnerFwOpenIdMapper.updateCustomerId(agent.getPartnerId(), openId, customer.getId());
                        }

                    }

                }

            }
        }

        Laxin laxin = new Laxin();
        laxin.setPartnerId(agent.getPartnerId());
        laxin.setAgentId(agent.getId());
        laxin.setAgentName(agent.getAgentName());
        laxin.setAgentCode(agent.getAgentCode());
        laxin.setMobile(mobile);
        laxin.setPassword(password);
        laxin.setLaxinMoney(laxinSetting.getLaxinMoney());
        laxin.setTicketMoney(laxinSetting.getLaxinMoney());
        laxin.setTicketDayCount(laxinSetting.getTicketDayCount());
        laxin.setPacketPeriodMoney(laxinSetting.getPacketPeriodMoney());
        laxin.setPacketPeriodMonth(laxinSetting.getPacketPeriodMonth());
        laxin.setIsActive(laxinSetting.getIsActive());
        laxin.setIncomeType(laxinSetting.getIncomeType());
        laxin.setIntervalDay(laxinSetting.getIntervalDay());
        laxin.setCreateTime(new Date());
        return laxinMapper.insert(laxin);
    }
}
