package cn.com.yusong.yhdg.appserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SystemConfigMapper extends MasterMapper {

    public String findConfigValue(@Param("id") String id);

    public List<SystemConfig> findAll();

}
