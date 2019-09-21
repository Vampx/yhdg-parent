package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.agentserver.service.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerInOutMoneyServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerInOutMoneyService service;

    @Test
    public void findPage() {
        Partner partner = newPartner();
        insertPartner(partner);

        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);

        CustomerInOutMoney entity = newCustomerInOutMoney(customer.getId());
        insertCustomerInOutMoney(entity);

        assertTrue(1 == service.findPage(entity).getTotalItems());
        assertTrue(1 == service.findPage(entity).getResult().size());

    }

}
