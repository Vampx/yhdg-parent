package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MobileMessageTemplateServiceTest extends BaseJunit4Test {
    @Autowired
    MobileMessageTemplateService mobileMessageTemplateService;

    @Test
    public void find() {
        MobileMessageTemplate mobileMessageTemplate = newMobileMessageTemplate(1);
        insertMobileMessageTemplate(mobileMessageTemplate);
        assertNotNull(mobileMessageTemplateService.find(0, MobileMessageTemplate.Type.AUTH_CODE.getValue()));
    }
}
