package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/11/4.
 */
public class ReadNoticeCustomerServiceTest extends BaseJunit4Test {

    @Autowired
    ReadNoticeCustomerService readNoticeCustomerService;

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        RestResult restResult = readNoticeCustomerService.insert(new int[]{1, 1, 3}, customer.getId());
        assertEquals(0, restResult.getCode());
    }
}
