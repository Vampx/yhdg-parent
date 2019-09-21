package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface SystemConfigMapper extends MasterMapper {
    public List<SystemConfig> findAllCategory();
    public SystemConfig find(String id);
    public String findConfigValue(String id);
    public List<SystemConfig> findAll(SystemConfig systemConfig);
    public void update(SystemConfig systemConfig);

}

