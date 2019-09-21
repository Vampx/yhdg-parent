package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepPutOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface KeepPutOrderMapper extends MasterMapper {
    KeepPutOrder find(String id);

    int insert(KeepPutOrder keepPutOrder);

    int updateOrderCount(@Param("id") String id, @Param("lastTime") Date lastTime, @Param("orderCount") Integer orderCount);
}
