package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PartnerMpOpenId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PartnerMpOpenIdServiceTest extends BaseJunit4Test {
    @Autowired
    PartnerMpOpenIdService partnerMpOpenIdService;

    @Test
    public void find() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        PartnerMpOpenId mpOpenId = newPartnerMpOpenId(partner.getId(), customer.getId());
        insertPartnerMpOpenId(mpOpenId);
        assertNotNull(partnerMpOpenIdService.findByOpenId(partner.getId(), mpOpenId.getOpenId()));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        PartnerMpOpenId mpOpenId = newPartnerMpOpenId(partner.getId(), customer.getId());
        assertEquals(1, partnerMpOpenIdService.insert(mpOpenId));
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        PartnerMpOpenId mpOpenId = newPartnerMpOpenId(partner.getId(), customer.getId());
        insertPartnerMpOpenId(mpOpenId);
        assertEquals(1, partnerMpOpenIdService.update(partner.getId(), mpOpenId.getOpenId(), mpOpenId.getNickname(), mpOpenId.getPhotoPath()));
    }

    @Test
    public void updateCustomerId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        PartnerMpOpenId mpOpenId = newPartnerMpOpenId(partner.getId(), customer.getId());
        insertPartnerMpOpenId(mpOpenId);
        assertEquals(1, partnerMpOpenIdService.updateCustomerId(partner.getId(), mpOpenId.getOpenId(), mpOpenId.getCustomerId()));
    }
}
