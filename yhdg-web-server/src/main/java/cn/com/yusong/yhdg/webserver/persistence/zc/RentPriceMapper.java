package cn.com.yusong.yhdg.webserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RentPriceMapper extends MasterMapper {
    RentPrice find(long id);
    List<RentPrice> findListBySetting(Long priceSettingId);
    List<RentPrice> findListByBatteryType(@Param("batteryType") Integer batteryType,
                                          @Param("agentId") int agentId,
                                          @Param("priceSettingId") long priceSettingId);
    int insert(RentPrice rentPrice);
    int update(RentPrice rentPrice);
    int delete(long id);
    int deleteBySettingId(long priceSettingId);
}
