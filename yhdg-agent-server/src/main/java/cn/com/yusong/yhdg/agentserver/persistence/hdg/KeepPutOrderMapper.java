package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepPutOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 投电订单
 */
public interface KeepPutOrderMapper extends MasterMapper {
    KeepPutOrder find(String id);

    int findCountByCabinet(@Param("cabinetId") String cabinetId);

    int findOrderCount(@Param("agentId") Integer agentId,
                       @Param("queryBeginTime") Date queryBeginTime,
                       @Param("queryEndTime") Date queryEndTime,
                       @Param("cabinetId") String cabinetId);

    int findPageCount(KeepPutOrder keepPutOrder);

    List findPageResult(KeepPutOrder keepPutOrder);
}
