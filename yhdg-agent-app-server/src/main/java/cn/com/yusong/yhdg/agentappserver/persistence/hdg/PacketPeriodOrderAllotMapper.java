package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface PacketPeriodOrderAllotMapper extends HistoryMapper {
    public String exist(@Param("suffix") String suffix);
    public List<PacketPeriodOrderAllot> findByOrder(@Param("cabinetId") String cabinetId,
                                                    @Param("orgType") int orgType,
                                                    @Param("agentId") int agentId,
                                                    @Param("serviceType") int serviceType,
                                                    @Param("statsDate") String statsDate,
                                                    @Param("suffix") String suffix,
                                                    @Param("offset") int offset,
                                                    @Param("limit") int limit);
    List<PacketPeriodOrderAllot> findShopDayIncome(@Param("orgType") int orgType,
                                              @Param("shopId") String shopId,
                                              @Param("serviceType") int serviceType,
                                              @Param("statsDate") String statsDate,
                                              @Param("suffix") String suffix);
    List<PacketPeriodOrderAllot> findShopMonthIncome(@Param("orgType") int orgType,
                                                   @Param("shopId") String shopId,
                                                   @Param("serviceType") int serviceType,
                                                   @Param("suffix") String suffix,
                                                   @Param("beginDate") Date beginDate,
                                                   @Param("endDate") Date endDate);

    List<PacketPeriodOrderAllot> findAgentCompanyMonthIncome(@Param("orgType") int orgType,
                                                   @Param("agentCompanyId") String agentCompanyId,
                                                   @Param("serviceType") int serviceType,
                                                   @Param("suffix") String suffix,
                                                   @Param("beginDate") Date beginDate,
                                                   @Param("endDate") Date endDate);

    List<PacketPeriodOrderAllot> findAgentCompanyDayIncome(@Param("orgType") int orgType,
                                                           @Param("agentCompanyId") String agentCompanyId,
                                                           @Param("serviceType") int serviceType,
                                                           @Param("suffix") String suffix,
                                                           @Param("statsDate") String statsDate);

    List<PacketPeriodOrderAllot> findByAgentCompany(@Param("statsDate") String statsDate,
                                                    @Param("agentId") Integer agentId,
                                                    @Param("agentCompanyId") String agentCompanyId,
                                                    @Param("suffix") String suffix,
                                                    @Param("orgType") Integer orgType);

    int contain(@Param("columnName") String columnName, @Param("suffix") String suffix);
}
