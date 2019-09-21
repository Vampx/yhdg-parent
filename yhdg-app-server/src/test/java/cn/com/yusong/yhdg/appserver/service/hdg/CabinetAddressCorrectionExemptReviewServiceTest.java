package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrectionExemptReview;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ruanjian5 on 2017/12/15.
 */
public class CabinetAddressCorrectionExemptReviewServiceTest extends BaseJunit4Test {

    @Autowired
    CabinetAddressCorrectionExemptReviewService cabinetAddressCorrectionExemptReviewService;

    @Test
    public void findByMobile() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        customer.setMobile("123456789");
        insertCustomer(customer);
        CabinetAddressCorrectionExemptReview review = newCabinetAddressCorrectionExemptReview(customer.getId(), customer.getMobile());
        insertCabinetAddressCorrectionExemptReview(review);
        assertNotNull(cabinetAddressCorrectionExemptReviewService.find(customer.getId()));

    }
}
