package cn.com.yusong.yhdg.staticserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface SystemConfigMapper extends MasterMapper {
    public String findConfigValue(@Param("id") String id);

    public SystemConfig find(String id);
}
