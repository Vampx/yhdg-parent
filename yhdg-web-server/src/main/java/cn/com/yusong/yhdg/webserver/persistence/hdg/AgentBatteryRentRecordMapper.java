package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentBatteryRentRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AgentBatteryRentRecordMapper extends MasterMapper {
    public int findPageCount(AgentBatteryRentRecord search);
    public List<AgentBatteryRentRecord> findPageResult(AgentBatteryRentRecord search);
    public int updateStatus(@Param("materialDayStatsId") Long materialDayStatsId, @Param("payType") Integer payType,
                            @Param("payTime") Date payTime, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus);
}
