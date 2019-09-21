package cn.com.yusong.yhdg.weixinserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface VehicleMapper extends MasterMapper {
    Vehicle findByVinNo(@Param("vinNo") String vinNo);
}
