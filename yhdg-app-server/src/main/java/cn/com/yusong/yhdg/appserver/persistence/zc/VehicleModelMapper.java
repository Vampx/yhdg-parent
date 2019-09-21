package cn.com.yusong.yhdg.appserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.VehicleModel;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface VehicleModelMapper extends MasterMapper {
    VehicleModel find(@Param("id") Integer id);
}
