package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PartnerMpOpenId;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.service.basic.PartnerMpOpenIdService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerMpOpenIdServiceTest extends BaseJunit4Test {
    @Autowired
    PartnerMpOpenIdService partnerMpOpenIdService;

    @Test
    public void findByOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("123");
        insertCustomer(customer);

        PartnerMpOpenId partnerMpOpenId = newPartnerMpOpenId(partner.getId(), customer.getId());
        insertPartnerMpOpenId(partnerMpOpenId);

        partnerMpOpenIdService.findByOpenId(partner.getId(), partnerMpOpenId.getOpenId());
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("123");
        insertCustomer(customer);

        PartnerMpOpenId partnerMpOpenId = newPartnerMpOpenId(partner.getId(), customer.getId());
        partnerMpOpenIdService.insert(partnerMpOpenId);
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("123");
        insertCustomer(customer);

        PartnerMpOpenId partnerMpOpenId = newPartnerMpOpenId(partner.getId(), customer.getId());
        insertPartnerMpOpenId(partnerMpOpenId);

        partnerMpOpenIdService.update(partner.getId(), partnerMpOpenId.getOpenId(), "1", "2");
    }
}
