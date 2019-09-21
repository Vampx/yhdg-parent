package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceCommand;
import cn.com.yusong.yhdg.common.domain.basic.DeviceUpgradePack;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.DeviceCommandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DeviceCommandService {
    @Autowired
    private DeviceCommandMapper deviceCommandMapper;

    public DeviceCommand find(Long id) {
        return deviceCommandMapper.find(id);
    }

    public Page findPage(DeviceCommand deviceCommand) {
        Page page = deviceCommand.buildPage();
        page.setTotalItems(deviceCommandMapper.findPageCount(deviceCommand));
        deviceCommand.setBeginIndex(page.getOffset());
        page.setResult(deviceCommandMapper.findPageResult(deviceCommand));
        return page;
    }

    public int create(DeviceCommand deviceCommand) {
        deviceCommand.setDeviceType(DeviceUpgradePack.DeviceType.IRON_TOWER.getValue());
        deviceCommand.setType(DeviceCommand.Type.REPORT_LOG.getValue());
        deviceCommand.setStatus(DeviceCommand.Status.NOT.getValue());
        deviceCommand.setCreateTime(new Date());
        int result = deviceCommandMapper.insert(deviceCommand);
        return result;
    }
}
