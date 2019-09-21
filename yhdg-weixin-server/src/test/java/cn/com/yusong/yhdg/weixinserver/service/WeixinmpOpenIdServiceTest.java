package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpOpenId;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.WeixinmpOpenIdService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WeixinmpOpenIdServiceTest extends BaseJunit4Test {
    @Autowired
    WeixinmpOpenIdService weixinmpOpenIdService;

    @Test
    public void findByOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        insertWeixinmpOpenId(weixinmpOpenId);

        weixinmpOpenIdService.findByOpenId(weixinmp.getId(), weixinmpOpenId.getOpenId());
    }

    @Test
    public void insert() {

        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        WeixinmpOpenId weixinmpOpenId = newWeixinmpOpenId(weixinmp.getId());
        weixinmpOpenIdService.insert(weixinmpOpenId);
    }
}
