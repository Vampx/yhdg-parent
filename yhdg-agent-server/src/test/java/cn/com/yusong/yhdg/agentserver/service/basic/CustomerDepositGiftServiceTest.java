package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositGift;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDepositGiftServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerDepositGiftService service;

    @Test
    public void findAll() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CustomerDepositGift depositGift = newCustomerDepositGift(partner.getId());
        insertCustomerDepositGift(depositGift);

        assertNotNull(service.findAll());
    }
    @Test
    public void update() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        final CustomerDepositGift depositGift = newCustomerDepositGift(partner.getId());
        insertCustomerDepositGift(depositGift);

        int[] appId = {partner.getId()};
        float[] money = {100f, 150f, 200f};
        float[] gift = {5f, 15f, 25f};

        String key = CacheKey.key("5:%d", agent.getId());
        memCachedClient.set(key, "", MemCachedConfig.CACHE_ONE_WEEK);
        service.update(appId, money, gift);

        final CustomerDepositGift customerDepositGift2 = new CustomerDepositGift();
        jdbcTemplate.query("select * from bas_customer_deposit_gift where money = ?",
                new Object[]{100},
                new RowCallbackHandler() {
                    public void processRow(ResultSet rs) throws SQLException {
                        customerDepositGift2.setGift((int) rs.getFloat("gift"));
                        customerDepositGift2.setMoney(rs.getInt("money"));
                    }
                });

        assertFalse(depositGift.getGift().equals(customerDepositGift2.getGift()));
        assertFalse(depositGift.getMoney().equals(customerDepositGift2.getMoney()));

        service.findAll();
    }

}
