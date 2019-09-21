package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.CustomerCouponTicketMapper;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
public class CustomerCouponTicketService {

    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;

    public CustomerCouponTicket find(long id) {
        return customerCouponTicketMapper.find(id);
    }

    //查询可用的
    public List<CustomerCouponTicket> findList(Integer agentId, String customerMobile,int status, int category, Integer offset, Integer limit) {
        return customerCouponTicketMapper.findList(agentId, customerMobile, status, category, offset, limit);
    }

    public int findCount(Integer agentId, String customerMobile, Integer ticketType) {
        return customerCouponTicketMapper.findCount(agentId, customerMobile, ticketType);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult create(CustomerCouponTicket customerCouponTicket) {
        customerCouponTicketMapper.insert(customerCouponTicket);

        PushMetaData pushMetaData = new PushMetaData();
        pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_GET_COUPON_TICKET.getValue());
        pushMetaData.setSourceId(String.format("%d", customerCouponTicket.getId()));
        pushMetaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(pushMetaData);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult delete(Long id) {
        customerCouponTicketMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }
}
