package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import cn.com.yusong.yhdg.staticserver.persistence.basic.CustomerCouponTicketMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AbstractServiceTest extends BaseJunit4Test {
    @Autowired
    AbstractService abstractService;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;

    @Test
    public void findConfigValue() {
        SystemConfig systemConfig = newSystemConfig();
        insertSystemConfig(systemConfig);

        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, systemConfig.getId());
        String value = (String) memCachedClient.get(key);

        String configValue = abstractService.findConfigValue(systemConfig.getId());
        assertEquals(value, configValue);
    }

    @Test
    public void findAgentInfo() {
    }

    @Test
    public void giveCouponTicket() {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerForegiftOrder customerForegiftOrder = newCustomerForegiftOrder(partner.getId(), customer.getId(), agent.getId());
        insertCustomerForegiftOrder(customerForegiftOrder);

        //押金赠送
        int type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();

        CustomerCouponTicketGift customerCouponTicketGift = newCustomerCouponTicketGift(agent.getId());
        customerCouponTicketGift.setType(type);
        insertCustomerCouponTicketGift(customerCouponTicketGift);

        abstractService.giveCouponTicket(customerForegiftOrder.getId(), ConstEnum.Category.RENT.getValue(),OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER.getValue(),
                type, 0,agent.getId(), CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customer.getMobile());

        assertEquals(1, jdbcTemplate.queryForInt("select count(*) from bas_customer_coupon_ticket"));
    }

    @Test
    public void newOrderId() {
        assertNotNull(abstractService.newOrderId(OrderId.OrderIdType.BATTERY_ORDER));
    }
}