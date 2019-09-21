package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PacketPeriodOrderAllotMapper extends HistoryMapper {
    public String exist(@Param("suffix") String suffix);

    public int createTable(@Param("suffix") String suffix);

    public int findCountByOrder(@Param("orderId") String orderId, @Param("serviceType") int serviceType, @Param("suffix") String suffix);

    public List<PacketPeriodOrderAllot> findByOrder(@Param("agentId") int agentId, @Param("serviceType") int serviceType, @Param("orderId") String orderId, @Param("suffix") String suffix, @Param("statsDate") String statsDate);

    public Integer packetPeriodOrderCount(@Param("partnerId") Integer partnerId, @Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("shopId") String shopId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public Integer agentCompanyPacketPeriodOrderCount(@Param("partnerId") Integer partnerId, @Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("agentCompanyId") String agentCompanyId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public Integer shopPacketPeriodOrderCount(@Param("shopId") String shopId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public Integer companyPacketPeriodOrderCount(@Param("agentCompanyId") String agentCompanyId,  @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumOrderMoney(@Param("partnerId") Integer partnerId, @Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumMoney(@Param("partnerId") Integer partnerId, @Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("serviceType") int serviceType, @Param("orgType") List<Integer> orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumShopMoney(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("shopId") String shopId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumAgentCompanyMoney(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("agentCompanyId") String agentCompanyId, @Param("serviceType") int serviceType,  @Param("orgType") int orgType,  @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumShopAgentMoney(@Param("shopId") String shopId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumAgentCompanyAgentMoney(@Param("agentCompanyId") String agentCompanyId, @Param("serviceType") int serviceType,  @Param("orgType") int orgType,  @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumMoneyByOrgType(@Param("agentId") int agentId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public int insert(PacketPeriodOrderAllot allot);

    public int deleteByOrder(@Param("orderId") String orderId, @Param("suffix") String suffix);
}
