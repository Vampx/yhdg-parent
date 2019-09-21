package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Device;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface DeviceMapper extends MasterMapper {
    Device findByDeviceId(@Param("type") int type, @Param("deviceId") String deviceId);
    public int insert(Device entity);
    int updateVersion(Device entity);
}
