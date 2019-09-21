package cn.com.yusong.yhdg.agentserver.persistence.zc;


import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerVehicleInfoMapper extends MasterMapper {
    CustomerVehicleInfo find(@Param("id") long id);
    CustomerVehicleInfo findByVehicle(@Param("vehicleId") long vehicleId);
    int findPageCount(CustomerVehicleInfo customerVehicleInfo);
    List<CustomerVehicleInfo> findPageResult(CustomerVehicleInfo customerVehicleInfo);
    int delete(@Param("id") long id);
}
