package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.AlipayfwService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlipayfwServiceTest extends BaseJunit4Test {
    @Autowired
    AlipayfwService alipayfwService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Alipayfw alipayfw = newAlipayfw(partner.getId());
        insertAlipayfw(alipayfw);

        assertNotNull(alipayfwService.find(alipayfw.getId()));
    }

    @Test
    public void findAll() {
        Partner partner = newPartner();
        insertPartner(partner);

        Alipayfw alipayfw = newAlipayfw(partner.getId());
        insertAlipayfw(alipayfw);

        alipayfwService.findAll();
    }
}
