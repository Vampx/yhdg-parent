package cn.com.yusong.yhdg.vehicleserver.persistence.basic;


import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface SystemConfigMapper extends MasterMapper {

    public String findConfigValue(String id);

}
