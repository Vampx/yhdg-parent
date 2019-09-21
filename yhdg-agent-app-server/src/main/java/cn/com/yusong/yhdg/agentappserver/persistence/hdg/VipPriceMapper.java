package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCustomer;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPriceMapper extends MasterMapper {
    VipPrice find(@Param("id") long id);
    List<VipPrice> findListByAgentId(@Param("agentId") int agentId,
                                     @Param("keyword") String keyword,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);
    int insert(VipPrice vipPrice);
    int update(VipPrice vipPrice);
    int updateCustomerCount(@Param("id") Long id, @Param("customerCount") Integer customerCount);
    int updateCabinetCount(@Param("id") Long id, @Param("cabinetCount") Integer cabinetCount);
    int updateShopCount(@Param("id") Long id, @Param("shopCount") Integer shopCount);
    int updateStationCount(@Param("id") Long id, @Param("stationCount") Integer stationCount);
    int updateAgentCompanyCount(@Param("id") Long id, @Param("agentCompanyCount") Integer agentCompanyCount);
    int delete(long id);
}
