package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerManualAuthRecordMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerManualAuthRecord;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomerManualAuthRecordServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerManualAuthRecordMapper customerManualAuthRecordMapper;
    @Autowired
    CustomerMapper customerMapper;

    @Test
    public void insert() {
        Partner partner = newPartner();
        insertPartner(partner);
        Customer customer = newCustomer(partner.getId());
        insertCustomer(customer);
        CustomerManualAuthRecord customerManualAuthRecord = newCustomerManualAuthRecord(customer.getPartnerId(),customer.getId());
        insertCustomerManualAuthRecord(customerManualAuthRecord);
        assertEquals(1,customerManualAuthRecordMapper.insert(customerManualAuthRecord));
        assertEquals(1,customerMapper.updateAuthStatus(customerManualAuthRecord.getCustomerId(), Customer.AuthStatus.WAIT_AUDIT.getValue()));
    }

}
