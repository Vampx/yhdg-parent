package cn.com.yusong.yhdg.frontserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface SystemConfigMapper extends MasterMapper {

    SystemConfig find(String id);
    List<SystemConfig> findAll(SystemConfig systemConfig);

}
