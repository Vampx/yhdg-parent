package cn.com.yusong.yhdg.appserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPriceCabinet;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPriceCabinetMapper extends MasterMapper {
    List<VipPriceCabinet> findListByPriceId(@Param("priceId") long priceId);
    List<VipPriceCabinet> findByCabinetId(@Param("cabinetId") String cabinetId);
    int insert(VipPriceCabinet vipPriceCabinet);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
