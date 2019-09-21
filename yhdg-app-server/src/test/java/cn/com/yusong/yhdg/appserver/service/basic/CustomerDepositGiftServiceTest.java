package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositGift;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/11/4.
 */
public class CustomerDepositGiftServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerDepositGiftService customerDepositGiftService;

    @Test
    public void findAll() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);
        CustomerDepositGift customerDepositGift = newCustomerDepositGift(partner.getId());
        insertCustomerDepositGift(customerDepositGift);
        assertNotNull(customerDepositGiftService.findAll(agent.getId()));
    }

}
