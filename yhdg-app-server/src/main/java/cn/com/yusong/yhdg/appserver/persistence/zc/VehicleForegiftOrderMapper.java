package cn.com.yusong.yhdg.appserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface VehicleForegiftOrderMapper extends MasterMapper {

    VehicleForegiftOrder find(String id);

    int findCountByCustomerId(@Param("id") String id, @Param("agentId") int agentId, @Param("customerId") long customerId, @Param("status") int status);

    int payOk(@Param("id") String id, @Param("payTime") Date payTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

    List<VehicleForegiftOrder> findListByCustomerIdAndStatus(@Param("customerId") long customerId, @Param("status") int status);

    VehicleForegiftOrder findOneEnabled(@Param("customerId") long customerId, @Param("status") Integer status, @Param("agentId") int agentId, @Param("modelId") int modelId);

    int insert(VehicleForegiftOrder vehicleForegiftOrder);

    int updateRefund(@Param("id") String id,
                     @Param("toStatus") int toStatus,
                     @Param("fromStatus") Integer fromStatus);

    int updateOrderInfo(@Param("id") String id, @Param("vehicleName") String vehicleName);
}
