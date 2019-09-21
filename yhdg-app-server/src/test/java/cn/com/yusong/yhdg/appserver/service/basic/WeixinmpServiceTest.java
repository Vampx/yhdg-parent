package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeixinmpServiceTest extends BaseJunit4Test {
    @Autowired
    WeixinmpService weixinmpService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        assertNotNull(weixinmpService.find(weixinmp.getId()));
    }

    @Test
    public void findAll() {
        Partner partner = newPartner();
        insertPartner(partner);

        Weixinmp weixinmp = newWeixinmp(partner.getId());
        insertWeixinmp(weixinmp);

        weixinmpService.findAll();
    }
}
