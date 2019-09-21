package cn.com.yusong.yhdg.batteryserver.persistence.hdg;

import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface KeepOrderMapper extends MasterMapper {
    public int update(@Param("id") String id, @Param("currentVolume") Integer currentVolume);
}
