package cn.com.yusong.yhdg.webserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentOrderMapper extends MasterMapper {
    int findPageCount(RentOrder rentOrder);

    List<RentOrder> findPageResult(RentOrder rentOrder);

    RentOrder find(String id);

    int updateStatusById(@Param("id") String id,@Param("status") int status,@Param("backTime") Date backTime);

    int updateBattery(@Param("id") String id, @Param("batteryId") String batteryId);

    int complete(@Param("id") String id,
                 @Param("toStatus") int toStatus,
                 @Param("fromStatus") int fromStatus,
                 @Param("backTime") Date backTime,
                 @Param("backOperator")String backOperator);
}

