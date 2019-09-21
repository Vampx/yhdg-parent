package cn.com.yusong.yhdg.appserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPriceStation;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPriceStationMapper extends MasterMapper {
    List<VipPriceStation> findListByPriceId(@Param("priceId") long priceId);
    VipPriceStation findByPriceId(@Param("priceId") long priceId, @Param("stationId") String stationId);
    List<VipPriceStation> findByStationId(@Param("stationId") String stationId);
    int insert(VipPriceStation vipPriceStation);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
