package cn.com.yusong.yhdg.staticserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface BatteryOrderMapper extends MasterMapper {

    public BatteryOrder find(String id);

    public int insert(BatteryOrder batteryOrder);

    public int payOk(@Param("id") String id, @Param("payTime") Date payTime, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);

}
