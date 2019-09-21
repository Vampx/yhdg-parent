package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class SystemBatteryTypeServiceTest extends BaseJunit4Test {

    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;

    @Test
    public void findList() {
        SystemBatteryType systemBatteryType = newSystemBatteryType();
        insertSystemBatteryType(systemBatteryType);

        assertEquals(1, systemBatteryTypeService.findList(systemBatteryType.getTypeName()).size());
    }


}
