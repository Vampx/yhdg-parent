package cn.com.yusong.yhdg.agentserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehicleOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VehicleOrderMapper  extends MasterMapper {

    VehicleOrder find(String id);
    int findCountByModelId(@Param("modelId") Integer modelId);
    int findPageCount(VehicleOrder vehicle);
    List<VehicleOrder> findPageResult(VehicleOrder vehicle);
    int complete(@Param("id") String id,
                 @Param("toStatus") int toStatus,
                 @Param("fromStatus") int fromStatus,
                 @Param("backTime") Date backTime,
                 @Param("backOperator") String backOperator);
}
