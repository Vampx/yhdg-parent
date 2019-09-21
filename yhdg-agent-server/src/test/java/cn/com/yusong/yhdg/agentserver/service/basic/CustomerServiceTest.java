package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

public class CustomerServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerService service;

    @Test
    public void findOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertNotNull( service.findOpenId(customer.getMobile()));
    }

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertTrue( 1==service.findList(customer).size());
    }
    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertNotNull(service.find(customer.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertTrue(1 == service.findPage(customer).getTotalItems());
        assertTrue(1 == service.findPage(customer).getResult().size());
    }

    @Test
    public void findPageByBindTime() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertTrue(1 == service.findPageByBindTime(customer).getTotalItems());
        assertTrue(1 == service.findPageByBindTime(customer).getResult().size());
    }

    @Test
    public void findPages() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("asdf");
        insertCustomer(customer);

        assertTrue(1 == service.findPages(customer).getTotalItems());
        assertTrue(1 == service.findPages(customer).getResult().size());
    }

    @Test
    public void findUnique() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertNotNull(service.findUnique("1234567687"));
    }

    @Test
    public void create() throws IOException {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());

        assertTrue("refund fail", service.create(customer).isSuccess());
        assertNotNull(service.find(customer.getId()));
        assertNotNull(service.findUnique("1234567687"));
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        Customer entity = service.find(customer.getId());

        customer.setNickname("haha");
        customer.setMobile("13675557888");

        assertTrue("update fail", service.update(customer).isSuccess());

        assertFalse(customer.getNickname().equals(entity.getNickname()));
        assertFalse(customer.getMobile().equals(entity.getMobile()));

        Customer result2 = (Customer)memCachedClient.get(CacheKey.key(CacheKey.K_ID_V_CUSTOMER_INFO, customer.getId()));
        assertNull(result2);
    }
    @Test
    public void resignation() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        customer.setAgentId(agent.getId());
        String operator = "asdf";
        insertCustomer(customer);



        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(),agent.getId());
        customerForegiftOrder.setId("1234");
        insertCustomerForegiftOrder(customerForegiftOrder);

        service.update(customer);

        assertTrue(service.resignation(customer.getId(),operator).isSuccess());

    }
    @Test
    public void refund() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        assertTrue(service.refund(customer, customer.getGiftBalance(), "asdf", "asdf").isSuccess());

    }
    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        // TODO 这里测试会有外键约束错误，暂时注释

//        CustomerForegiftOrder entity = newCustomerForegiftOrder(partner.getId(), customer.getId(),agent.getId());
//        insertCustomerForegiftOrder(entity);

//        CustomerInOutMoney money = newCustomerInOutMoney(customer.getId());
//        insertCustomerInOutMoney(money);

//        CustomerDepositOrder depositOrder = newCustomerDepositOrder(partner.getId(), customer.getId());
//        insertCustomerDepositOrder(depositOrder);

//        Feedback feedback = newFeedback(partner.getId(), customer.getId());
//        insertFeedback(feedback);

        assertTrue("delete fail", service.delete(customer.getId()).isSuccess());
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_foregift_order where customer_id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_deposit_order where customer_id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_feedback where customer_id = ?", customer.getId()));
        assertNull(service.find(customer.getId()));
    }
    @Test
    public void findCount() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        Date beginDate = new Date(1234);
        Date endDate =new Date(3456);
        assertTrue(1 == service.findCount(beginDate,endDate));
    }

    @Test
    public void batchRemove() {
        Customer customer2 = new Customer();
        customer2.setBalance(201700);
        customer2.setIsActive(1);
        customer2.setRegisterType(Customer.RegisterType.APP.getValue());
        customer2.setCreateTime(new Date());
        customer2.setGiftBalance(0);
        customer2.setAuthStatus(Customer.AuthStatus.NOT.getValue());
        insertCustomer(customer2);

        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer1 = newCustomer(partner.getId());
        insertCustomer(customer1);

       // insertCustomerForegiftOrder(newCustomerForegiftOrder(customer1.getId(), priceGroup.getId(),priceGroup.getAgentId()));

        insertCustomerInOutMoney(newCustomerInOutMoney(customer1.getId()));

        insertCustomerDepositOrder(newCustomerDepositOrder(partner.getId(), customer1.getId()));

        insertFeedback(newFeedback(partner.getId(), customer1.getId()));

        long[] ids = {customer1.getId(), customer2.getId()};
        assertTrue("batchRemove fail", service.batchRemove(ids).isSuccess());
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_foregift_order where customer_id = ?", customer1.getId(), Integer.class));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer1.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_deposit_order where customer_id = ?", customer1.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_feedback where customer_id = ?", customer1.getId()));
        assertTrue(service.findPage(customer1).getTotalItems() <= 0);

    }

    @Test
    public void batchActive() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer1 = newCustomer(partner.getId());
        insertCustomer(customer1);

        Customer customer2 = new Customer();
        customer2.setBalance(201700);
        customer2.setIsActive(ConstEnum.Flag.FALSE.getValue());
        customer2.setRegisterType(Customer.RegisterType.APP.getValue());
        customer2.setCreateTime(new Date());
        customer2.setGiftBalance(0);
        customer2.setAuthStatus(Customer.AuthStatus.NOT.getValue());
        insertCustomer(customer2);

        long[] ids = {customer1.getId(), customer2.getId()};
        assertTrue("batchActive fail", service.batchActive(ids).isSuccess());

        String s = jdbcTemplate.queryForObject("select is_active from bas_customer where id = " + customer1.getId(), String.class);
        assertTrue(ConstEnum.Flag.FALSE.getValue() == Integer.parseInt(s));

        String result = jdbcTemplate.queryForObject("select is_active from bas_customer where id = " + customer2.getId(), String.class);
        assertTrue(ConstEnum.Flag.TRUE.getValue() == Integer.parseInt(result));
    }

}
