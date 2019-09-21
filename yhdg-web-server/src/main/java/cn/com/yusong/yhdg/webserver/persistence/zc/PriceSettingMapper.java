package cn.com.yusong.yhdg.webserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PriceSettingMapper extends MasterMapper {
    int findCountByModelId(@Param("modelId") Integer modelId);
    PriceSetting find(long id);
    List<PriceSetting> findListByAgentId(@Param("agentId") Integer agentId);
    PriceSetting findByAgentId(@Param("agentId") Integer agentId,
                               @Param("category") Integer category,
                               @Param("modelId") Integer modelId);
    int findPageCount(PriceSetting priceSetting);
    List<PriceSetting> findPageResult(PriceSetting priceSetting);
    int findNotShopPricePageCount(PriceSetting priceSetting);
    List<PriceSetting> findNotShopPricePageResult(PriceSetting priceSetting);
    int insert(PriceSetting priceSetting);
    int update(PriceSetting priceSetting);
    int updatePrice(@Param("id") long id, @Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);
    int delete(long id);
}
