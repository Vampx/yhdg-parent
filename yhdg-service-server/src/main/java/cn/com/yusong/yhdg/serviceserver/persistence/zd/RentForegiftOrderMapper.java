package cn.com.yusong.yhdg.serviceserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyDayStats;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.PlatformDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RentForegiftOrderMapper extends MasterMapper {
    public List<ShopDayStats> findIncrementShopExchange(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<AgentCompanyDayStats> findIncrementAgentCompanyExchange(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<ShopDayStats> findRefundShopExchange(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<AgentCompanyDayStats> findRefundAgentCompanyExchange(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<Map> findIncrementExchange(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public List<Map> findRefund(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public PlatformDayStats findIncrement(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public PlatformDayStats findTotal(@Param("status") Integer status);

    public PlatformDayStats findIncrementRefund(@Param("status") Integer status, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime);

    public PlatformDayStats findTotalRefund(@Param("status") Integer status);

    RentForegiftOrder find(String sourceId);
}
