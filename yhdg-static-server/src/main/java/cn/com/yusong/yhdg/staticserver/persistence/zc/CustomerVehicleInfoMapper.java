package cn.com.yusong.yhdg.staticserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface CustomerVehicleInfoMapper extends MasterMapper {
    CustomerVehicleInfo find(long id);
    CustomerVehicleInfo findByCustomerId(@Param("customerId") long customerId);
    int insert(CustomerVehicleInfo customerVehicleInfo);

}
