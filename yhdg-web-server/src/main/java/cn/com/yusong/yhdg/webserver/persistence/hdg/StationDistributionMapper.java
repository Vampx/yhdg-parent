package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.StationDistribution;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StationDistributionMapper extends MasterMapper {

    List<StationDistribution> findByStationId(@Param("stationId") String stationId);
    List<StationDistribution> findListByOperateId(@Param("operateId") long operateId);
    int insert(StationDistribution stationDistribution);
    int deleteByStationId(@Param("stationId") String stationId);
}
