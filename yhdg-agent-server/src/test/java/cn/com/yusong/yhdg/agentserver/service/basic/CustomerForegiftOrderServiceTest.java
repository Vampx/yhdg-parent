package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import com.alipay.api.AlipayApiException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerForegiftOrderServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerForegiftOrderService service;

    @Test
    public void findPage() {

        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(entity);

        assertTrue(1 == service.findPage(entity).getTotalItems());
        assertTrue(1 == service.findPage(entity).getResult().size());

    }

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(entity);

        assertNotNull(service.find(entity.getId()));
    }

    @Test
    public void refund() throws AlipayApiException {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        entity.setPayType(ConstEnum.PayType.BALANCE.getValue());
        entity.setStatus(CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        entity.setMemo("高兴");
        insertCustomerForegiftOrder(entity);

//        assertTrue("refund fail", service.refund("admin", entity).isSuccess());
//        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where biz_id = ?", entity.getId()));
////        assertEquals(customer.getBalance() + entity.getRefundMoney(), jdbcTemplate.queryForInt("select balance from bas_customer where id = ?", entity.getCustomerId()));
//        assertEquals(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(), jdbcTemplate.queryForInt("select status from bas_customer_foregift_order where id = ?", entity.getId()));
    }

    @Test
    public void findCanRefundByCustomerId() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);


        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(entity);

        assertNotNull(service.findCanRefundByCustomerId(customer.getId()));
    }

}
