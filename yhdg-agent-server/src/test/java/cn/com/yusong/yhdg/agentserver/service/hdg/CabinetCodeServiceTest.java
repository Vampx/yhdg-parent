package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetCodeServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetCodeService service;

    @Test
    public void findPage() {
        CabinetCode subcabinetCode = newCabinetCode();
        insertCabinetCode(subcabinetCode);

        assertTrue(1 == service.findPage(subcabinetCode).getTotalItems());
        assertTrue(1 == service.findPage(subcabinetCode).getResult().size());
    }

    @Test
    public void find() {
        CabinetCode subcabinetCode = newCabinetCode();
        insertCabinetCode(subcabinetCode);

        assertNotNull(service.find(subcabinetCode.getId()));
    }

    @Test
    public void swap() {
        Partner partner = newPartner(); insertPartner(partner); Agent agent = newAgent(partner.getId());
        insertAgent(agent);



        Cabinet cabinet = newCabinet(agent.getId(),  null);
        insertCabinet(cabinet);

        CabinetCode subcabinetCode1 = newCabinetCode();
        insertCabinetCode(subcabinetCode1);


    }

}
