package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetMonthStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetDayStatsMapper extends MasterMapper {

    List<CabinetDayStats> findByCabinetList(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId,
                                            @Param("beginDate") String beginDate,
                                            @Param("endDate") String endDate, @Param("keyword") String keyword,
                                            @Param("offset") Integer offset, @Param("limit") Integer limit);

    List<CabinetDayStats> findTotalCabinetStatsList(@Param("agentId") Integer agentId,
                                            @Param("beginDate") String beginDate,
                                            @Param("endDate") String endDate, @Param("keyword") String keyword,
                                            @Param("offset") Integer offset, @Param("limit") Integer limit);
    CabinetDayStats findTotalStatsListByCabinetId(@Param("agentId") Integer agentId,
                                                  @Param("cabinetId") String cabinetId,
                                                  @Param("beginDate") String beginDate,
                                                  @Param("endDate") String endDate);

    List<CabinetDayStats> findListByCabinetId(@Param("agentId") int agentId, @Param("cabinetId") String cabinetId);

    CabinetDayStats findForCabinet(@Param("cabinetId") String cabinetId, @Param("statsDate") String statsDate);

    CabinetDayStats findTotalByStats(@Param("agentId") Integer agentId, @Param("statsDate") String statsDate);

    List<CabinetDayStats> findForStats(@Param("agentId") int agentId, @Param("statsDate") String statsDate,
                                       @Param("keyword") String keyword,
                                       @Param("offset") int offset, @Param("limit") int limit);
}
