package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PartnerDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PartnerDayStatsMapper extends MasterMapper {
    public PartnerDayStats find(@Param("partnerId") Integer partnerId, @Param("statsDate") String statsDate, @Param("category") Integer category);

    public int insert(PartnerDayStats stats);

    public int update(PartnerDayStats stats);
}
