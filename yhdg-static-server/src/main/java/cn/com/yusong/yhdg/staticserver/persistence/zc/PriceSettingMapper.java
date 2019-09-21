package cn.com.yusong.yhdg.staticserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PriceSettingMapper extends MasterMapper {

    PriceSetting find(@Param("id") Long id);

    List<PriceSetting> findPriceSettingAll();

    List<PriceSetting> findAgentIdAll(List list);

    List<PriceSetting> findShopIdAll(@Param("shopId") String shopId);





}
