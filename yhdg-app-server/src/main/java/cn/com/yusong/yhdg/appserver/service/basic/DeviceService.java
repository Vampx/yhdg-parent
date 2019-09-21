package cn.com.yusong.yhdg.appserver.service.basic;
import cn.com.yusong.yhdg.appserver.persistence.basic.DeviceMapper;
import cn.com.yusong.yhdg.common.domain.basic.Device;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService extends AbstractService {
    @Autowired
    DeviceMapper deviceMapper;

    public Device findByDeviceId(int type, String deviceId) {
        return deviceMapper.findByDeviceId(type,deviceId);
    }

    public int insert(Device entity) {
        return deviceMapper.insert(entity);
    }

    public int updateVersion(Device device) {
        return deviceMapper.updateVersion(device);
    }
}
