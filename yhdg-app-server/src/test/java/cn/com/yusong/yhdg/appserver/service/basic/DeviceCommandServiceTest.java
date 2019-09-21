package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.BaseJunit4Test;
import cn.com.yusong.yhdg.common.domain.basic.DeviceCommand;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class DeviceCommandServiceTest extends BaseJunit4Test {
    @Autowired
    DeviceCommandService deviceCommandService;

    @Test
    public void findOneByType() {
        DeviceCommand deviceCommand = newDeviceCommand(1, "001");
        insertDeviceCommand(deviceCommand);

        assertNotNull(deviceCommandService.findOneByType(DeviceCommand.Type.REPORT_LOG.getValue(), "001", 1, DeviceCommand.Status.NOT.getValue()));
    }

    @Test
    public void updateStatus() {
        DeviceCommand deviceCommand = newDeviceCommand(1, "001");
        insertDeviceCommand(deviceCommand);

        deviceCommandService.updateStatus(deviceCommand.getId(), DeviceCommand.Status.DISPATCHED.getValue(),new Date());
    }
}
