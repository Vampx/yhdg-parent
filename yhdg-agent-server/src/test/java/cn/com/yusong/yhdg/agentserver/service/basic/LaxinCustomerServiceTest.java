package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.domain.basic.LaxinCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class LaxinCustomerServiceTest extends BaseJunit4Test {
    @Autowired
    private LaxinCustomerService laxinCustomerService;

    private LaxinCustomer laxinCustomer;

    @Before
    public void setUp() throws Exception {
        Partner partner = newPartner();
        insertPartner(partner);

        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        Laxin laxin = newLaxin(partner.getId(), agent.getId());
        insertLaxin(laxin);

        laxinCustomer = newLaxinCustomer(partner.getId(), agent.getId(), laxin.getId());
    }

    @Test
    public void find() {
        insertLaxinCustomer(laxinCustomer);

        assertNotNull(laxinCustomerService.find(String.valueOf(laxinCustomer.getId())));
    }

    @Test
    public void findPage() {
        insertLaxinCustomer(laxinCustomer);

        assertTrue(1 == laxinCustomerService.findPage(laxinCustomer).getTotalItems());
        assertTrue(1 == laxinCustomerService.findPage(laxinCustomer).getResult().size());
    }
}