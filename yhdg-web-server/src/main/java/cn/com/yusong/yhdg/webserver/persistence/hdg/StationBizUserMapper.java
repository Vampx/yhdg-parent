package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.StationBizUser;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StationBizUserMapper extends MasterMapper {
    StationBizUser find(@Param("stationId") String stationId, @Param("userId") Long userId);
    List<StationBizUser> findListByStationId(@Param("stationId") String stationId);
    int insert(StationBizUser stationBizUser);
    int delete(@Param("id") long id);
    int deleteByStationId(@Param("stationId") String stationId);
}
