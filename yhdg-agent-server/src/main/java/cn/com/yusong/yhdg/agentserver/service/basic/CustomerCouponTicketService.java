package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CustomerCouponTicketService extends AbstractService {
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    PartnerMapper partnerMapper;

    public Page findPage(CustomerCouponTicket search) {
        Page page = search.buildPage();
        page.setTotalItems(customerCouponTicketMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<CustomerCouponTicket> list = customerCouponTicketMapper.findPageResult(search);
        for (CustomerCouponTicket customerCouponTicket: list) {
            Partner partner = partnerMapper.find(customerCouponTicket.getPartnerId());
            if (partner != null) {
                customerCouponTicket.setPartnerName(partner.getPartnerName());
            }
            if (customerCouponTicket.getAgentId() != null) {
                customerCouponTicket.setAgentName(findAgentInfo(customerCouponTicket.getAgentId()).getAgentName());
            }
        }
        page.setResult(list);
        return page;
    }

    public CustomerCouponTicket find(long id) {
        return customerCouponTicketMapper.find(id);
    }

    public ExtResult create(CustomerCouponTicket entity) {
        if (entity.getMoney() == null || entity.getMoney() <= 0) {
            return ExtResult.failResult("请输入正确的金额");
        }

        if (entity.getAgentId() == null) {
            return ExtResult.failResult("运营商不能是空");
        }
        Agent agent = agentMapper.find(entity.getAgentId());
        if (agent == null) {
            return ExtResult.failResult("运营商不存在");
        }

        CustomerCouponTicket cct = new CustomerCouponTicket();
        cct.setPartnerId(agent.getPartnerId());
        cct.setAgentId(entity.getAgentId());
        cct.setTicketName(entity.getTicketName());
        cct.setCategory(entity.getCategory());
        cct.setMoney(entity.getMoney());
        cct.setOperator(entity.getOperator());
        cct.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        cct.setExpireTime(entity.getExpireTime());
        cct.setMemo(entity.getMemo());
        cct.setTicketType(entity.getTicketType());
        cct.setBeginTime(entity.getBeginTime());
        cct.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        cct.setCustomerMobile(entity.getCustomerMobile());
        cct.setCreateTime(new Date());
        customerCouponTicketMapper.insert(cct);

        PushMetaData pushMetaData = new PushMetaData();
        pushMetaData.setSourceType(PushMessage.SourceType.CUSTOMER_GET_COUPON_TICKET.getValue());
        pushMetaData.setSourceId(String.format("%d", cct.getId()));
        pushMetaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(pushMetaData);
        return ExtResult.successResult();
    }

    public ExtResult delete(long id) {
        CustomerCouponTicket ticket = customerCouponTicketMapper.find(id);
        if (ticket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
            ExtResult.failResult("使用中的优惠券不能删除！");
        }
        int total = customerCouponTicketMapper.delete(id);
        if (total == 0) {
            ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
