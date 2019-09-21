package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.DeviceCommandMapper;
import cn.com.yusong.yhdg.common.domain.basic.DeviceCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DeviceCommandService {
    @Autowired
    DeviceCommandMapper deviceCommandMapper;

    public DeviceCommand findOneByType(int type, String deviceId, int deviceType, int status) {
        return deviceCommandMapper.findOneByType(type, deviceId, deviceType, status);
    }

    public int updateStatus(long id, int status, Date dispatchTime) {
        return deviceCommandMapper.updateStatus(id, status, dispatchTime);
    }
}
