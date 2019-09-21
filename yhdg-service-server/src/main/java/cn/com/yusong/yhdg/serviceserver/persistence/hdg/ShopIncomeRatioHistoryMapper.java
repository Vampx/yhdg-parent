package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.IncomeRatioHistory;
import cn.com.yusong.yhdg.common.domain.hdg.ShopIncomeRatioHistory;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopIncomeRatioHistoryMapper extends HistoryMapper {
    public String exist(@Param("suffix") String suffix);
    public int createTable(@Param("suffix") String suffix);
    public List<ShopIncomeRatioHistory> findList(@Param("agentId") Integer agentId, @Param("shopId") String shopId, @Param("statsDate") String statsDate, @Param("suffix") String suffix);
    public ShopIncomeRatioHistory find(@Param("agentId") Integer agentId, @Param("shopId") String shopId, @Param("statsDate") String statsDate, @Param("orgType") Integer orgType, @Param("suffix") String suffix);
    public int insert(ShopIncomeRatioHistory history);
}
