package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Agent;import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetCodeServiceTest extends BaseJunit4Test {
    @Autowired
    CabinetCodeService service;
    @Autowired
    CabinetService cabinetService;

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

        CabinetCode cabinetCode = newCabinetCode();
        cabinetCode.setId("1234");
        cabinetCode.setCode("asdf");
        insertCabinetCode(cabinetCode);

        CabinetCode cabinetCode2 = newCabinetCode();
        cabinetCode2.setId("2345");
        cabinetCode2.setCode("zxcv");
        insertCabinetCode(cabinetCode2);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setId(cabinetCode.getCode());
        cabinet.setMac(cabinetCode.getId());
        insertCabinet(cabinet);

        Cabinet cabinet2 = newCabinet(agent.getId(), null);
        cabinet2.setId(cabinetCode2.getCode());
        cabinet2.setMac(cabinetCode2.getId());
        insertCabinet(cabinet2);

        assertTrue(service.swap(cabinetCode.getCode(), cabinetCode2.getCode()).isSuccess());
        assertEquals(service.find(cabinetCode.getId()).getCode(), cabinetCode2.getCode());
        assertEquals(service.find(cabinetCode2.getId()).getCode(), cabinetCode.getCode());
        assertEquals(cabinetService.find(cabinet.getId()).getMac(), cabinetCode2.getId());
        assertEquals(cabinetService.find(cabinet2.getId()).getMac(), cabinetCode.getId());
    }

    @Test
    public void swapCode() {
        Partner partner = newPartner();
        insertPartner(partner);
        Agent agent = newAgent(partner.getId());
        insertAgent(agent);

        CabinetCode cabinetCode = newCabinetCode();
        cabinetCode.setId("1234");
        cabinetCode.setCode("asdf");
        insertCabinetCode(cabinetCode);

        CabinetCode cabinetCode2 = newCabinetCode();
        cabinetCode2.setId("2345");
        cabinetCode2.setCode("zxcv");
        insertCabinetCode(cabinetCode2);

        Cabinet cabinet = newCabinet(agent.getId(), null);
        cabinet.setId(cabinetCode.getCode());
        cabinet.setMac(cabinetCode.getId());
        insertCabinet(cabinet);

        Cabinet cabinet2 = newCabinet(agent.getId(), null);
        cabinet2.setId(cabinetCode2.getCode());
        cabinet2.setMac(cabinetCode2.getId());
        insertCabinet(cabinet2);

        service.swapCode(cabinetCode, cabinetCode2);
        assertEquals(service.find(cabinetCode.getId()).getCode(), cabinetCode2.getCode());
        assertEquals(service.find(cabinetCode2.getId()).getCode(), cabinetCode.getCode());
        assertEquals(cabinetService.find(cabinet.getId()).getMac(), cabinetCode2.getId());
        assertEquals(cabinetService.find(cabinet2.getId()).getMac(), cabinetCode.getId());

    }

}
