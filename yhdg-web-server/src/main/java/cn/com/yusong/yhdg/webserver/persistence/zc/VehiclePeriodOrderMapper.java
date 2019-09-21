package cn.com.yusong.yhdg.webserver.persistence.zc;

import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface VehiclePeriodOrderMapper extends MasterMapper {
    int findCountByModelId(@Param("modelId") Integer modelId);
    VehiclePeriodOrder find(@Param("id") String id);
    int findPageCount(VehiclePeriodOrder rentPeriodOrder);
    List<VehiclePeriodOrder> findPageResult(VehiclePeriodOrder rentPeriodOrder);
    int updateStatus(@Param("id") String id, @Param("status") int status,
                     @Param("refundMoney") Integer refundMoney,
                     @Param("refundOperator") String refundOperator,
                     @Param("refundTime") Date refundTime);
}
