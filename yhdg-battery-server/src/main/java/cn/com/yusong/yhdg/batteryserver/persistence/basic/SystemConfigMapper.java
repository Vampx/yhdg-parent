package cn.com.yusong.yhdg.batteryserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface SystemConfigMapper extends MasterMapper {

    public SystemConfig find(String id);

}
