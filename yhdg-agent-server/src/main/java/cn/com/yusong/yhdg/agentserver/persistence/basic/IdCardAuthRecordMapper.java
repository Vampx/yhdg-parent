package cn.com.yusong.yhdg.agentserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import cn.com.yusong.yhdg.common.domain.hdg.AgentCabinetForegiftRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IdCardAuthRecordMapper extends MasterMapper {
    public int findPageCount(IdCardAuthRecord search);
    public List<IdCardAuthRecord> findPageResult(IdCardAuthRecord search);
    public int updateStatus(@Param("materialDayStatsId") Long materialDayStatsId, @Param("payType") Integer payType,
                            @Param("payTime") Date payTime, @Param("fromStatus") Integer fromStatus, @Param("toStatus") Integer toStatus);
}
