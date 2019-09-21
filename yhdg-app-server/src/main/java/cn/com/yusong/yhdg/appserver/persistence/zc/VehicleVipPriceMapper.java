package cn.com.yusong.yhdg.appserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface VehicleVipPriceMapper extends MasterMapper {
    public VehicleVipPrice findByRentPriceId(@Param("rentPriceId") Long rentPriceId);
    public VehicleVipPrice find(@Param("id") Integer id);
}
