package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerCouponTicketMapper;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/7.
 */
@Service
public class CustomerCouponTicketService {

    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;

    public CustomerCouponTicket find(long id) {
        return customerCouponTicketMapper.find(id);
    }

    //查询可用的
    public List<CustomerCouponTicket> findList(Integer agentId, String mobile, Date newTime, Integer ticketType, Integer status, Integer category, Integer offset, Integer limit) {
        return customerCouponTicketMapper.findList(agentId, mobile, newTime, ticketType, status, category, offset, limit);
    }

    //查询可用的
    public int findCount(int partnerId, String customerMobile, int status, Integer category) {
        return customerCouponTicketMapper.findCount(partnerId, customerMobile, status, category);
    }

    public int useTicket(long id) {
        return customerCouponTicketMapper.useTicket(id, new Date(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.USED.getValue());
    }

    public int insert(CustomerCouponTicket customerCouponTicket) {
        return customerCouponTicketMapper.insert(customerCouponTicket);
    }
}
