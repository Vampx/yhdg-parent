package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.IncomeRatioHistory;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomeRatioHistoryMapper extends HistoryMapper {
    public String exist(@Param("suffix") String suffix);
    public int createTable(@Param("suffix") String suffix);
    public List<IncomeRatioHistory> findList(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("statsDate") String statsDate,  @Param("suffix") String suffix);
    public IncomeRatioHistory find(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("statsDate") String statsDate, @Param("orgType") Integer orgType, @Param("suffix") String suffix);
    public int insert(IncomeRatioHistory history);
}
