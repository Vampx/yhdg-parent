package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerInOutMoneyServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerInOutMoneyService customerInOutMoneyService;

    @Test
    public void findList() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        CustomerInOutMoney customerInOutMoney = newCustomerInOutMoney(customer.getId());
        insertCustomerInOutMoney(customerInOutMoney);
        assertNotNull(customerInOutMoneyService.findList(customer.getId(), 0, 10));
    }

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        CustomerInOutMoney customerInOutMoney = newCustomerInOutMoney(customer.getId());
        assertEquals(1, customerInOutMoneyService.insert(customerInOutMoney));
    }
}
