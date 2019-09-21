package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwSubscribe;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.AlipayfwSubscribeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlipayfwSubscribeServiceTest extends BaseJunit4Test {

    @Autowired
    AlipayfwSubscribeService alipayfwSubscribeService;

    @Test
    public void findByOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Alipayfw alipayfw = newAlipayfw(partner.getId());
        insertAlipayfw(alipayfw);

        AlipayfwSubscribe subscribe = newAlipayfwSubscribe(alipayfw.getId());
        insertAlipayfwSubscribe(subscribe);

        assertNotNull(alipayfwSubscribeService.findByOpenId(alipayfw.getId(), subscribe.getOpenId()));
    }

    @Test
    public void subscribe() {
        Partner partner = newPartner();
        insertPartner(partner);

        Alipayfw alipayfw = newAlipayfw(partner.getId());
        insertAlipayfw(alipayfw);

        AlipayfwSubscribe subscribe = newAlipayfwSubscribe(alipayfw.getId());
        alipayfwSubscribeService.subscribe(alipayfw.getId(), subscribe.getOpenId());
    }

    @Test
    public void unsubscribe() {

        Partner partner = newPartner();
        insertPartner(partner);

        Alipayfw alipayfw = newAlipayfw(partner.getId());
        insertAlipayfw(alipayfw);

        AlipayfwSubscribe subscribe = newAlipayfwSubscribe(alipayfw.getId());
        insertAlipayfwSubscribe(subscribe);

        alipayfwSubscribeService.unsubscribe(alipayfw.getId(), subscribe.getOpenId());
    }
}
