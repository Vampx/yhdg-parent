package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class LaxinCustomerService {
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    LaxinMapper laxinMapper;
    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    PartnerMpOpenIdMapper partnerMpOpenIdMapper;
    @Autowired
    PartnerFwOpenIdMapper partnerFwOpenIdMapper;

    public int sumPayForegiftCount(long laxinId) {
        return laxinCustomerMapper.sumPayForegiftCount(laxinId);
    }

    public RestResult checkMobile(long laxinId, String mobile) {
        Laxin laxin = laxinMapper.find(laxinId);
        if (laxin == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户不存在");
        }

        if (laxin.getIsActive() == null || laxin.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户无效");
        }

        Agent agent = agentMapper.find(laxin.getAgentId());

        LaxinCustomer laxinCustomer0 = laxinCustomerMapper.findByTargetMobile(agent.getPartnerId(), mobile);
        if (laxinCustomer0 != null) {
            if (laxinCustomer0.getForegiftTime() != null) {
                if ((System.currentTimeMillis() - laxinCustomer0.getCreateTime().getTime()) < 1L *laxin.getIntervalDay() * 24 * 3600 * 1000) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "手机号已拉新, 不能重复拉新");
                }
            }
        }

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult insert(long laxinId, String mobile, String unionOpenId, int type) {
        Laxin laxin = laxinMapper.find(laxinId);
        if (laxin == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户不存在");
        }
        Agent agent = agentMapper.find(laxin.getAgentId());

        if (laxin.getIsActive() == null || laxin.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户无效");
        }

        Customer customer = null;
        Date now = new Date();
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

        LaxinCustomer laxinCustomer0 = laxinCustomerMapper.findByTargetMobile(agent.getPartnerId(), mobile);
        if (laxinCustomer0 != null) {
            if (laxinCustomer0.getForegiftTime() == null) {
                laxinCustomerMapper.delete(laxinCustomer0.getId());
            } else {
                if ((System.currentTimeMillis() - laxinCustomer0.getCreateTime().getTime()) < 1L *laxin.getIntervalDay() * 24 * 3600 * 1000) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "手机号已拉新, 不能重复拉新");
                }
            }
        }

        LaxinCustomer laxinCustomer = new LaxinCustomer();
        laxinCustomer.setPartnerId(agent.getPartnerId());
        laxinCustomer.setAgentId(agent.getId());
        laxinCustomer.setAgentId(laxin.getAgentId());
        laxinCustomer.setAgentName(laxin.getAgentName());
        laxinCustomer.setAgentCode(laxin.getAgentCode());
        laxinCustomer.setLaxinId(laxin.getId());
        laxinCustomer.setLaxinMobile(laxin.getMobile());
        laxinCustomer.setTargetCustomerId(customer == null ? null : customer.getId());
        laxinCustomer.setTargetMobile(mobile);
        laxinCustomer.setTargetFullname(customer == null ? null : customer.getFullname());
        laxinCustomer.setCreateTime(new Date());
        laxinCustomerMapper.insert(laxinCustomer);

        CustomerCouponTicket ticket = customerCouponTicketMapper.findLaxinTicket(mobile, agent.getId(), CustomerCouponTicket.GiveType.LAXIN.getValue(), CustomerCouponTicket.Status.NOT_USER.getValue());
        if (ticket == null) {
            ticket = new CustomerCouponTicket();
            ticket.setPartnerId(agent.getPartnerId());
            ticket.setAgentId(laxin.getAgentId());
            ticket.setTicketName("拉新券");
            ticket.setMoney(laxin.getTicketMoney());
            ticket.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
            ticket.setCategory(CustomerCouponTicket.Category.EXCHANGE.getValue());
            ticket.setBeginTime(DateUtils.truncate(now, Calendar.DAY_OF_MONTH));
            ticket.setExpireTime(DateUtils.addDays(ticket.getBeginTime(), laxin.getTicketDayCount() + 1));
            ticket.setCustomerMobile(mobile);
            ticket.setTicketType(CustomerCouponTicket.TicketType.RENT.getValue());
            ticket.setGiveType(CustomerCouponTicket.GiveType.LAXIN.getValue());
            ticket.setCreateTime(now);
            customerCouponTicketMapper.insert(ticket);
        }

        Map data = new HashMap();
        data.put("ticketId", ticket.getId());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }
}
