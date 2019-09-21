package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.ReliefStation;
import cn.com.yusong.yhdg.common.tool.zk.Listener;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by ruanjian5 on 2017/11/21.
 */
public class ReliefStationServiceTest extends BaseJunit4Test {
    @Autowired
    ReliefStationService reliefStationService;

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);

        ReliefStation reliefStation = newReliefStation(partner.getId());
        insertReliefStation(reliefStation);

        List<ReliefStation> list = reliefStationService.findList(partner.getId(), null, reliefStation.getLng(), reliefStation.getLat(), 0, 11);
        System.out.println("size()==" + list.size());

        assertNotNull(list);
    }
}
