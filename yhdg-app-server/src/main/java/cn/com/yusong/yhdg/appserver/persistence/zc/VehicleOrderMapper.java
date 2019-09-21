package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;


public interface VehicleOrderMapper extends MasterMapper {

    public VehicleOrder find(String id);
    public int insert(VehicleOrder vehicleOrder);
    public int findCountByCustomer(@Param("customerId") long customerId, @Param("idPrefix")String idPrefix);

}
