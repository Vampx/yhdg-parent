package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerCouponTicketGiftServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerCouponTicketGiftService service;

    @Test
    public void findPage() {

        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        assertTrue(1 == service.findPage(customerCouponTicketGift).getTotalItems());
        assertTrue(1 == service.findPage(customerCouponTicketGift).getResult().size());

    }

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        assertNotNull(service.find(customerCouponTicketGift.getId()));
    }

    @Test
    public void create() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        service.create(customerCouponTicketGift);

        assertNotNull(service.find(customerCouponTicketGift.getId()));
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CustomerCouponTicketGift entity = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(entity);

        CustomerCouponTicketGift result1 = service.find(entity.getId());

        entity.setNewType(CustomerCouponTicketGift.Type.BUY_RENT.getValue());
        entity.setDayCount(55);
//        entity.setIsActive(0);
        entity.setMoney(100000);
        assertTrue("update fail", service.update(entity).isSuccess());

        CustomerCouponTicketGift result2 = service.find(entity.getId());
        assertFalse(result1.getDayCount().equals(result2.getDayCount()));
//        assertFalse(result1.getIsActive().equals(result2.getIsActive()));
        assertFalse(result1.getMoney().equals(result2.getMoney()));
    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CustomerCouponTicketGift entity = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(entity);

        assertNotNull(service.find(entity.getId()));
        assertTrue("delete fail", service.delete(entity.getId()).isSuccess());
    }

    @Test
    public void update1() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CustomerCouponTicketGift entity = newCustomerCouponTicketGift(agent.getId());
        insertCustomerCouponTicketGift(entity);

        CustomerCouponTicketGift entity1 = newCustomerCouponTicketGift(agent.getId());
        entity1.setType(CustomerCouponTicketGift.Type.BUY_RENT.getValue());
        insertCustomerCouponTicketGift(entity1);

        entity.setNewType(CustomerCouponTicketGift.Type.BUY_RENT.getValue());
        entity.setDayCount(55);
        entity.setIsActive(0);
        entity.setMoney(100000);
        assertFalse("update fail", service.update(entity).isSuccess());

        CustomerCouponTicketGift result = service.find(entity.getId());
        assertFalse(entity.getDayCount().equals(result.getDayCount()));
        assertFalse(entity.getIsActive().equals(result.getIsActive()));
        assertFalse(entity.getMoney().equals(result.getMoney()));
    }


}
