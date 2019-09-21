package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpSubscribe;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.WeixinmpSubscribeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WeixinmpSubscribeServiceTest extends BaseJunit4Test {

    @Autowired
    WeixinmpSubscribeService weixinmpSubscribeService;

    @Test
    public void findByOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpSubscribe subscribe = newWeixinmpSubscribe(weixinmp.getId());
        insertWeixinmpSubscribe(subscribe);

        assertNotNull(weixinmpSubscribeService.findByOpenId(weixinmp.getId(), subscribe.getOpenId()));
    }

    @Test
    public void subscribe() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpSubscribe subscribe = newWeixinmpSubscribe(weixinmp.getId());
        weixinmpSubscribeService.subscribe(weixinmp.getId(), subscribe.getOpenId());
    }

    @Test
    public void unsubscribe() {

        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpSubscribe subscribe = newWeixinmpSubscribe(weixinmp.getId());
        insertWeixinmpSubscribe(subscribe);

        weixinmpSubscribeService.unsubscribe(weixinmp.getId(), subscribe.getOpenId());
    }
}
