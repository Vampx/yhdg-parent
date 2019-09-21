package cn.com.yusong.yhdg.staticserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.staticserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class PartnerServiceTest extends BaseJunit4Test {
    @Autowired
    PartnerService partnerService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        assertNotNull(partnerService.find(partner.getId()));
    }
}