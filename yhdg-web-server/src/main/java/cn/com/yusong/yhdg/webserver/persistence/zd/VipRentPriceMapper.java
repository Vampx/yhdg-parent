package cn.com.yusong.yhdg.webserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipRentPriceMapper extends MasterMapper {
    VipRentPrice find(@Param("id") long id);
    VipRentPrice findByBatteryType(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);
    int findPageCount(VipRentPrice vipPrice);
    List<VipRentPrice> findPageResult(VipRentPrice vipPrice);
    int insert(VipRentPrice vipPrice);
    int update(VipRentPrice vipPrice);
    int updateCustomerCount(@Param("id") Long id, @Param("customerCount") Integer customerCount);
    int updateShopCount(@Param("id") Long id, @Param("shopCount") Integer shopCount);
    int updateAgentCompanyCount(@Param("id") Long id, @Param("agentCompanyCount") Integer agentCompanyCount);
    int delete(long id);
}
