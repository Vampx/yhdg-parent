package cn.com.yusong.yhdg.cabinetserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BackBatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface BackBatteryOrderMapper extends MasterMapper {
    public BackBatteryOrder find(@Param("id") String id);

    public BackBatteryOrder findByBoxNum(@Param("cabinetId") String cabinetId, @Param("boxNum") String boxNum, @Param("orderStatus") Integer orderStatus);

    public int updateStatus(@Param("id") String id, @Param("orderStatus") Integer orderStatus, @Param("cancelTime") Date cancelTime);

    public int backBattery(@Param("id") String id, @Param("batteryId") String batteryId,  @Param("orderStatus") int orderStatus, @Param("backTime") Date backTime);
}
