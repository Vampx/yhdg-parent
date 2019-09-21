package cn.com.yusong.yhdg.webserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VipPriceMapper extends MasterMapper {
    VipPrice find(@Param("id") long id);
    VipPrice findByBatteryType(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);
    int findPageCount(VipPrice vipPrice);
    List<VipPrice> findPageResult(VipPrice vipPrice);
    VipPrice findByIsActive(@Param("id") long id, @Param("now") Date now);
    int insert(VipPrice vipPrice);
    int update(VipPrice vipPrice);
    int updateCustomerCount(@Param("id") Long id, @Param("customerCount") Integer customerCount);
    int updateCabinetCount(@Param("id") Long id, @Param("cabinetCount") Integer cabinetCount);
    int updateStationCount(@Param("id") Long id, @Param("stationCount") Integer stationCount);
    int updateShopCount(@Param("id") Long id, @Param("shopCount") Integer shopCount);
    int updateAgentCompanyCount(@Param("id") Long id, @Param("agentCompanyCount") Integer agentCompanyCount);
    int delete(long id);
}
