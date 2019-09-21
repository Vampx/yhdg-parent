package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.webserver.service.BaseJunit4Test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CabinetAddressCorrectionExemptReviewServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetAddressCorrectionExemptReviewService service;

    @Test
    public void find() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CabinetAddressCorrectionExemptReview cabinet = newCabinetAddressCorrectionExemptReview(customer.getId(),customer.getNickname(), customer.getMobile());
        insertCabinetAddressCorrectionExemptReview(cabinet);

        assertNotNull(service.find(cabinet.getId()));
    }

    @Test
    public void findPage() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CabinetAddressCorrectionExemptReview cabinet = newCabinetAddressCorrectionExemptReview(customer.getId(),customer.getNickname(), customer.getMobile());
        insertCabinetAddressCorrectionExemptReview(cabinet);

        assertTrue(1 == service.findPage(cabinet).getTotalItems());
        assertTrue(1 == service.findPage(cabinet).getResult().size());
    }

    @Test
    public void create() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CabinetAddressCorrectionExemptReview cabinet = newCabinetAddressCorrectionExemptReview(customer.getId(),customer.getNickname(), customer.getMobile());

        assertTrue(service.create(cabinet).isSuccess());
    }

    @Test
    public void delete() {
        Partner partner = newPartner(); insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CabinetAddressCorrectionExemptReview cabinet = newCabinetAddressCorrectionExemptReview(customer.getId(),customer.getNickname(), customer.getMobile());
        insertCabinetAddressCorrectionExemptReview(cabinet);

        assertTrue(service.delete(cabinet.getId()).isSuccess());
    }

}

