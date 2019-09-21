package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerExchangeInfoMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.BatteryOrderMapper;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

public class CustomerServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerService customerService;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;

    @Test
    public void findOpenId() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertNotNull( customerService.findOpenId(customer.getMobile()));
    }

    @Test
    public void findList() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertTrue( 1==customerService.findList(customer).size());
    }
    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertNotNull(customerService.find(customer.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertTrue(1 == customerService.findPage(customer).getTotalItems());
        assertTrue(1 == customerService.findPage(customer).getResult().size());
    }

    @Test
    public void findWhitelistCustomerPage() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertTrue(1 == customerService.findWhitelistCustomerPage(customer).getTotalItems());
        assertTrue(1 == customerService.findWhitelistCustomerPage(customer).getResult().size());
    }

    @Test
    public void findPageByBindTime() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertTrue(1 == customerService.findPageByBindTime(customer).getTotalItems());
        assertTrue(1 == customerService.findPageByBindTime(customer).getResult().size());
    }

    @Test
    public void findPages() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("asdf");
        insertCustomer(customer);

        assertTrue(1 == customerService.findPages(customer).getTotalItems());
        assertTrue(1 == customerService.findPages(customer).getResult().size());
    }

    @Test
    public void findUnique() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        assertNotNull(customerService.findUnique("1234567687"));
    }

    @Test
    public void create() throws IOException {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());

        assertTrue("refund fail", customerService.create(customer).isSuccess());
        assertNotNull(customerService.find(customer.getId()));
        assertNotNull(customerService.findUnique("1234567687"));
    }

    @Test
    public void update() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        Customer entity = customerService.find(customer.getId());

        customer.setNickname("haha");
        customer.setMobile("13675557888");

        assertTrue("update fail", customerService.update(customer).isSuccess());

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

        customerService.update(customer);

        assertTrue(customerService.resignation(customer.getId(),operator).isSuccess());

    }

    @Test
    public void delete() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerAgentBalance customerAgentBalance = newCustomerAgentBalance(customer.getId(), agent.getId());
        insertCustomerAgentBalance(customerAgentBalance);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(newOrderId(OrderId.OrderIdType.BATTERY_ORDER),
                systemBatteryType.getId(),
                partner.getId(),
                agent.getId(),
                battery.getId(), customer.getId());
        insertBatteryOrder(batteryOrder);

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery.getId(), batteryOrder.getId());
        insertCustomerExchangeBattery(customerExchangeBattery);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        CustomerExchangeInfo customerExchangeInfo = newCustomerExchangeInfo(customer.getId(), customerForegiftOrder.getId(), systemBatteryType.getId(), agent.getId());
        insertCustomerExchangeInfo(customerExchangeInfo);

        assertFalse(customerService.delete(customer.getId()).isSuccess());

        jdbcTemplate.execute("delete from bas_customer_exchange_info where id="+customer.getId());
        jdbcTemplate.execute("delete from bas_customer_foregift_order where customer_id="+customer.getId());
        jdbcTemplate.execute("delete from bas_customer_exchange_battery where customer_id="+customer.getId());
//        jdbcTemplate.execute("delete from hdg_battery_order where customer_id="+customer.getId());
        batteryOrderMapper.deleteByCustomerId(customer.getId());//增删改会更新mybatis缓存

        assertTrue(customerService.delete(customer.getId()).isSuccess());

        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_agent_balance where customer_id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_battery where customer_id = ?", customer.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_exchange_info where id = ?", customer.getId()));
        assertNull(customerService.find(customer.getId()));
    }
    @Test
    public void findCount() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        Date beginDate = new Date(1234);
        Date endDate =new Date(3456);
        assertTrue(1 == customerService.findCount(beginDate,endDate));
    }

    @Test
    public void batchRemove() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer1 = newCustomer(partner.getId());
        insertCustomer(customer1);

        Customer customer2 = new Customer();
        customer2.setBalance(201700);
        customer2.setIsActive(1);
        customer2.setRegisterType(Customer.RegisterType.APP.getValue());
        customer2.setCreateTime(new Date());
        customer2.setAuthStatus(Customer.AuthStatus.NOT.getValue());
        customer2.setGiftBalance(0);
        insertCustomer(customer2);

       // insertCustomerForegiftOrder(newCustomerForegiftOrder(customer1.getId(), priceGroup.getId(),priceGroup.getAgentId()));

        insertCustomerInOutMoney(newCustomerInOutMoney(customer1.getId()));

        insertCustomerDepositOrder(newCustomerDepositOrder(partner.getId(), customer1.getId()));

        insertFeedback(newFeedback(partner.getId(), customer1.getId()));

        long[] ids = {customer1.getId(), customer2.getId()};
        assertTrue("batchRemove fail", customerService.batchRemove(ids).isSuccess());
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_foregift_order where customer_id = ?", customer1.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_in_out_money where customer_id = ?", customer1.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_customer_deposit_order where customer_id = ?", customer1.getId()));
        assertEquals(0, jdbcTemplate.queryForInt("select count(*) from bas_feedback where customer_id = ?", customer1.getId()));
        assertTrue(customerService.findPage(customer1).getTotalItems() <= 0);

    }

    @Test
    public void batchActive() {
        Partner partner = newPartner(); insertPartner(partner);
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
        assertTrue("batchActive fail", customerService.batchActive(ids).isSuccess());

        String s = jdbcTemplate.queryForObject("select is_active from bas_customer where id = " + customer1.getId(), String.class);
        assertTrue(ConstEnum.Flag.FALSE.getValue() == Integer.parseInt(s));

        String result = jdbcTemplate.queryForObject("select is_active from bas_customer where id = " + customer2.getId(), String.class);
        assertTrue(ConstEnum.Flag.TRUE.getValue() == Integer.parseInt(result));
    }

    @Test
    public void mpUnbindMobile() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer1 = newCustomer(partner.getId());
        customer1.setMpOpenId("测试的mpOpenId");
        insertCustomer(customer1);
        assertTrue(1 == customerService.mpUnbindMobile(customer1.getId(), customer1.getMpOpenId()));
        assertEquals(null, customerService.find(customer1.getId()).getMpOpenId());
    }

    @Test
    public void fwUnbindMobile() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer1 = newCustomer(partner.getId());
        customer1.setFwOpenId("测试的fwOpenId");
        insertCustomer(customer1);
        assertTrue(1 == customerService.fwUnbindMobile(customer1.getId(), customer1.getFwOpenId()));
        assertEquals(null, customerService.find(customer1.getId()).getFwOpenId());
    }

    @Test
    public void appUnbindMobile() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer1 = newCustomer(partner.getId());
        customer1.setMobile("测试的mobile1");
        customer1.setMpOpenId("测试的mpOpenId");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setMobile("测试的mobile2");
        customer2.setFwOpenId("测试的fwOpenId");
        insertCustomer(customer2);


        Long[] ids = {customer1.getId(), customer2.getId()};
        assertTrue(customerService.appUnbindMobile(ids).isSuccess());
        assertEquals(null, customerService.find(customer1.getId()).getMpOpenId());
        assertEquals(null, customerService.find(customer2.getId()).getFwOpenId());
    }

    @Test
    public void clearAgent() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer1 = newCustomer(partner.getId());
        customer1.setMpOpenId("测试的mpOpenId");
        insertCustomer(customer1);

        Customer customer2 = newCustomer(partner.getId());
        customer2.setFwOpenId("测试的fwOpenId");
        insertCustomer(customer2);

        Long[] ids = {customer1.getId(), customer2.getId()};
        ExtResult extResult = customerService.clearAgent(ids);
        assertTrue(extResult.isSuccess());
        assertEquals(extResult.getMessage(),"成功清空2个用户的运营商id");
    }

    @Test
    public void updateIsWhiteList() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer1 = newCustomer(partner.getId());
        customer1.setMpOpenId("测试的mpOpenId");
        customer1.setIsWhiteList(ConstEnum.Flag.FALSE.getValue());
        insertCustomer(customer1);

        assertTrue(customerService.updateIsWhiteList(customer1.getId(), ConstEnum.Flag.TRUE.getValue()).isSuccess());
        assertEquals(ConstEnum.Flag.TRUE.getValue(), customerService.find(customer1.getId()).getIsWhiteList().intValue());
    }

    @Test
    public void updateTransferPeople() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("测试的mpOpenId");
        customer.setAgentId(agent.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
        insertCustomerForegiftOrder(customerForegiftOrder);

        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        PacketPeriodOrder packetPeriodOrder = newPacketPeriodOrder(partner.getId(), customer.getId(), systemBatteryType.getId(), agent.getId());
        packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());

        insertPacketPeriodOrder(packetPeriodOrder);

        Battery battery = newBattery(agent.getId(), systemBatteryType.getId());
        insertBattery(battery);

        BatteryOrder batteryOrder = newBatteryOrder(battery.getId(), systemBatteryType.getId(), partner.getId(), agent.getId(), battery.getId(), customer.getId());
        insertBatteryOrder(batteryOrder);

        CustomerExchangeBattery customerExchangeBattery = newCustomerExchangeBattery(customer.getId(), agent.getId(), battery.getId(), batteryOrder.getId());
        insertCustomerExchangeBattery(customerExchangeBattery);

        Customer customer1 = newCustomer(partner.getId());
        customer1.setMobile("15125652653");
        customer1.setMpOpenId("测试的mpOpenId");
        insertCustomer(customer1);

        assertTrue(customerService.updateTransferPeople(customer.getId(), agent.getPartnerId(), customer1.getMobile()).isSuccess());
    }
}
