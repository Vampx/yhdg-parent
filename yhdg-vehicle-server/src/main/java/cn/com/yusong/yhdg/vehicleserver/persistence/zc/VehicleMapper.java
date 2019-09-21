package cn.com.yusong.yhdg.vehicleserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VehicleMapper extends MasterMapper {

    Vehicle findByVinNo(@Param("vinNo") String vinNo);

    int insert(Vehicle vehicle);

    int update(Vehicle vehicle);

    int updateOnline(@Param("id") Integer id, @Param("isOnline") int isOnline);

    int updateHeart(@Param("id") Integer id, @Param("isOnline") int isOnline, @Param("reportTime") Date reportTime);

}
