package cn.com.yusong.yhdg.cabinetserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface SystemConfigMapper extends MasterMapper {

    public String findConfigValue(String id);

}
