package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Device;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DeviceMapper extends MasterMapper {
    Device find(@Param("id") int id);
    public int findPageCount(Device device);
    public List<Device> findPageResult(Device device);
}
