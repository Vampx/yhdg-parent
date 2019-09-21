package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BackBatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BackBatteryOrderMapper extends MasterMapper {
    public BackBatteryOrder find(@Param("id") String id);
    public List<BackBatteryOrder> findStatus(@Param("expireTime") Date expireTime, @Param("orderStatus") Integer orderStatus);
    public int updateStatus(@Param("id")String id, @Param("fromStatus")int fromStatus, @Param("toStatus")int toStatus);
}
