package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwOpenId;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.AlipayfwOpenIdService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AlipayfwOpenIdServiceTest extends BaseJunit4Test {
    @Autowired
    AlipayfwOpenIdService alipayfwOpenIdService;

    @Test
    public void findByOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Alipayfw alipayfw = newAlipayfw(partner.getId());
        insertAlipayfw(alipayfw);

        AlipayfwOpenId alipayfwOpenId = newAlipayfwOpenId(alipayfw.getId());
        insertAlipayfwOpenId(alipayfwOpenId);

        alipayfwOpenIdService.findByOpenId(alipayfw.getId(), alipayfwOpenId.getOpenId());
    }

    @Test
    public void insert() {

        Partner partner = newPartner();
        insertPartner(partner);

        Alipayfw alipayfw = newAlipayfw(partner.getId());
        insertAlipayfw(alipayfw);

        AlipayfwOpenId alipayfwOpenId = newAlipayfwOpenId(alipayfw.getId());
        alipayfwOpenIdService.insert(alipayfwOpenId);
    }

}
