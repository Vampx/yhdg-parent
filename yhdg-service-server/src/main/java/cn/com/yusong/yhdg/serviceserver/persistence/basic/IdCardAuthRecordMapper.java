package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.IdCardAuthRecord;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface IdCardAuthRecordMapper extends MasterMapper {
    public List<IdCardAuthRecord> findByStatus(@Param("status") int status, @Param("statsTime") Date statsTime);

    public int updateDayStatsId(@Param("id") long id, @Param("materialDayStatsId") int materialDayStatsId);


}
