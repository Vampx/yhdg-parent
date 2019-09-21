package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface SystemConfigMapper extends MasterMapper {
    public String findConfigValue(String id);
    public List<SystemConfig> find();
}
