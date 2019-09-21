package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.CustomerGuide;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomerGuideServiceTest extends BaseJunit4Test {

    @Autowired
    CustomerGuideService customerGuideService;

    @Test
    public void list() {
        CustomerGuide customerGuide = newCustomerGuide();
        insertCustomerGuide(customerGuide);

        assertNotNull(customerGuideService.list());
    }
}
