package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.DeviceCommand;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface DeviceCommandMapper extends MasterMapper {
    public DeviceCommand findOneByType(@Param("type") int type, @Param("deviceId")String deviceId, @Param("deviceType")int deviceType, @Param("status")int status);

    public int updateStatus(@Param("id")long id, @Param("status")int status, @Param("dispatchTime")Date dispatchTime);
}
