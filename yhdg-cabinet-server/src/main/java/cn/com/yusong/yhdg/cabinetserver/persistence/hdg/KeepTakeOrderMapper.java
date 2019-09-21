package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.KeepTakeOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface KeepTakeOrderMapper extends MasterMapper {
    public KeepTakeOrder find(String id);
    public int insert(KeepTakeOrder order);
    int updateOrderCount(@Param("id") String id, @Param("lastTime") Date lastTime, @Param("orderCount") Integer orderCount);
}
