package cn.com.yusong.yhdg.agentappserver.service.basic;

import cn.com.yusong.yhdg.agentappserver.persistence.basic.SystemBatteryTypeMapper;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SystemBatteryTypeService extends AbstractService {

    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;

    public SystemBatteryType find(int id) {
        return systemBatteryTypeMapper.find(id);
    }

    public List<SystemBatteryType> findList(String typeName) {
        return systemBatteryTypeMapper.findList(typeName);
    }

}
