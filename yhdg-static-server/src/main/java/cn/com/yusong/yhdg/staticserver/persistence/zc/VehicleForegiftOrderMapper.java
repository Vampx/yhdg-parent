package cn.com.yusong.yhdg.staticserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface VehicleForegiftOrderMapper extends MasterMapper {
    VehicleForegiftOrder find(String id);
    int payOk(@Param("id") String id, @Param("payTime") Date payTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
    int findCountByCustomerId(@Param("id") String id, @Param("agentId") int agentId, @Param("customerId") long customerId, @Param("status") int status);

}
