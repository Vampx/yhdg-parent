package cn.com.yusong.yhdg.agentappserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrderAllot;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface RentPeriodOrderAllotMapper extends HistoryMapper {
    String exist(@Param("suffix") String suffix);
    List<RentPeriodOrderAllot> findByOrder(@Param("shopId") String shopId,
                                            @Param("orgType") int orgType,
                                            @Param("agentId") int agentId,
                                            @Param("serviceType") int serviceType,
                                            @Param("statsDate") String statsDate,
                                            @Param("suffix") String suffix,
                                            @Param("offset") int offset,
                                            @Param("limit") int limit);
    List<RentPeriodOrderAllot> findShopDayIncome(@Param("orgType") int orgType,
                                                 @Param("shopId") String shopId,
                                                 @Param("serviceType") int serviceType,
                                                 @Param("statsDate") String statsDate,
                                                 @Param("suffix") String suffix);
    List<RentPeriodOrderAllot> findShopMonthIncome(@Param("orgType") int orgType,
                                                   @Param("shopId") String shopId,
                                                   @Param("serviceType") int serviceType,
                                                   @Param("suffix") String suffix,
                                                   @Param("beginDate") Date beginDate,
                                                   @Param("endDate") Date endDate);

    List<RentPeriodOrderAllot> findAgentCompanyMonthIncome(@Param("orgType") int orgType,
                                                   @Param("agentCompanyId") String agentCompanyId,
                                                   @Param("serviceType") int serviceType,
                                                   @Param("suffix") String suffix,
                                                   @Param("beginDate") Date beginDate,
                                                   @Param("endDate") Date endDate);

    List<RentPeriodOrderAllot> findAgentCompanyDayIncome(@Param("orgType") int orgType,
                                                         @Param("agentCompanyId") String agentCompanyId,
                                                         @Param("serviceType") int serviceType,
                                                         @Param("suffix") String suffix,
                                                         @Param("statsDate") String statsDate);

    int contain(@Param("columnName") String columnName, @Param("suffix") String suffix);
}
