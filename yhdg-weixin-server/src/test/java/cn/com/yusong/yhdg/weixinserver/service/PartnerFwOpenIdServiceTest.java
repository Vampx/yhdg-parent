package cn.com.yusong.yhdg.weixinserver.service;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PartnerFwOpenId;
import cn.com.yusong.yhdg.weixinserver.BaseJunit4Test;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.PartnerFwOpenIdMapper;
import cn.com.yusong.yhdg.weixinserver.service.basic.PartnerFwOpenIdService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerFwOpenIdServiceTest extends BaseJunit4Test {
    @Autowired
    PartnerFwOpenIdService partnerFwOpenIdService;

    @Test
    public void findByOpenId() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("123");
        insertCustomer(customer);

        PartnerFwOpenId partnerFwOpenId = newPartnerFwOpenId(partner.getId(), customer.getId());
        insertPartnerFwOpenId(partnerFwOpenId);

        partnerFwOpenIdService.findByOpenId(partner.getId(), partnerFwOpenId.getOpenId());
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("123");
        insertCustomer(customer);

        PartnerFwOpenId partnerFwOpenId = newPartnerFwOpenId(partner.getId(), customer.getId());
        partnerFwOpenIdService.insert(partnerFwOpenId);
    }

    @Test
    public void update() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMpOpenId("123");
        insertCustomer(customer);

        PartnerFwOpenId partnerFwOpenId = newPartnerFwOpenId(partner.getId(), customer.getId());
        insertPartnerFwOpenId(partnerFwOpenId);

        partnerFwOpenIdService.update(partner.getId(), partnerFwOpenId.getOpenId(), "1", "2");
    }
}
