package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.weixinserver.constant.RespCode;
import cn.com.yusong.yhdg.weixinserver.entity.result.RestResult;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.*;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;

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

    @Transactional (rollbackFor = Throwable.class)
    public RestResult insert(long laxinId, String mobile) {
        Laxin laxin = laxinMapper.find(laxinId);
        Agent agent = agentMapper.find(laxin.getAgentId());

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

        Date now = new Date();
        Customer customer = customerMapper.findByMobile(agent.getPartnerId(), mobile);

        LaxinCustomer laxinCustomer = new LaxinCustomer();
        laxinCustomer.setPartnerId(agent.getPartnerId());
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


        return RestResult.dataResult(0, null, ticket);

    }
}
