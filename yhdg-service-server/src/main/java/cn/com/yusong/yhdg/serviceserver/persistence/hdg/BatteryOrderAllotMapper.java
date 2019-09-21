package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderAllot;
import cn.com.yusong.yhdg.common.persistence.HistoryMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BatteryOrderAllotMapper extends HistoryMapper {
    public String exist(@Param("suffix") String suffix);

    public int createTable(@Param("suffix") String suffix);

    public int findCountByOrder(@Param("orderId") String orderId, @Param("suffix") String suffix);

    public List<BatteryOrderAllot> findByOrder(@Param("agentId") int agentId, @Param("serviceType") int serviceType, @Param("orderId") String orderId, @Param("suffix") String suffix, @Param("statsDate") String statsDate);

    public Integer exchangeCount(@Param("partnerId") Integer partnerId, @Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("shopId") String shopId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public Integer agentCompanyExchangeCount(@Param("partnerId") Integer partnerId, @Param("agentId") Integer agentId,  @Param("cabinetId") String cabinetId, @Param("agentCompanyId") String agentCompanyId,  @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumOrderMoney(@Param("partnerId") Integer partnerId, @Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumMoney(@Param("partnerId") Integer partnerId, @Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId,  @Param("serviceType") int serviceType, @Param("orgType") List<Integer> orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumShopMoney(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("shopId") String shopId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumAgentCompanyMoney(@Param("agentId") Integer agentId, @Param("cabinetId") String cabinetId, @Param("agentCompanyId") String agentCompanyId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public double sumMoneyByOrgType(@Param("agentId") int agentId, @Param("serviceType") int serviceType, @Param("orgType") int orgType, @Param("statsDate") String statsDate, @Param("suffix") String suffix);

    public int insert(BatteryOrderAllot allot);

    public int deleteByOrder(@Param("orderId") String orderId, @Param("type") int type, @Param("suffix") String suffix);
}
