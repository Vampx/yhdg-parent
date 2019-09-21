package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.SystemBatteryTypeMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SystemBatteryTypeService  extends AbstractService {
    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;

    public SystemBatteryType find(Integer id) {
       return findBatteryType(id);
    }

}
