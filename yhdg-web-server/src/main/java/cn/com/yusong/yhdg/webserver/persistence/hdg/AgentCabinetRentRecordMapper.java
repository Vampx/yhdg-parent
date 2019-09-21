package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetRentRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AgentCabinetRentRecordMapper extends MasterMapper {
    public int findPageCount(AgentCabinetRentRecord search);
    public List<AgentCabinetRentRecord> findPageResult(AgentCabinetRentRecord search);
    public int updateStatus(@Param("materialDayStatsId") Long materialDayStatsId, @Param("payType") Integer payType,
                            @Param("payTime") Date payTime, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus);
}
