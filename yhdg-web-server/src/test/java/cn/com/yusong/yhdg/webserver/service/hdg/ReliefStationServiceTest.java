package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.ReliefStation;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReliefStationServiceTest extends BaseJunit4Test {
    @Autowired
    ReliefStationService service;

    @Test
    public void find(){
        Partner partner = newPartner(); insertPartner(partner);

        ReliefStation reliefStation = newReliefStation(partner.getId());
        insertReliefStation(reliefStation);
        assertNotNull(service.find(reliefStation.getId()));
    }

    @Test
    public void findPage(){
        Partner partner = newPartner(); insertPartner(partner);

        ReliefStation reliefStation = newReliefStation(partner.getId());
        insertReliefStation(reliefStation);
        assertTrue(1 == service.findPage(reliefStation).getTotalItems());
        assertTrue(1 == service.findPage(reliefStation).getResult().size());
    }


    @Test
    public void newCreate(){
        Partner partner = newPartner(); insertPartner(partner);

        ReliefStation reliefStation = newReliefStation(partner.getId());
        assertTrue(service.newCreate(reliefStation).isSuccess());
    }

    @Test
    public void update(){
        Partner partner = newPartner(); insertPartner(partner);

        ReliefStation reliefStation = newReliefStation(partner.getId());
        insertReliefStation(reliefStation);
        reliefStation.setProvinceId(123);
        reliefStation.setStationName("七贤桥救助");
        assertTrue(service.update(reliefStation).isSuccess());
    }

    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner);

        ReliefStation reliefStation = newReliefStation(partner.getId());
        insertReliefStation(reliefStation);
        assertTrue(service.delete(reliefStation.getId()).isSuccess());
    }
}
