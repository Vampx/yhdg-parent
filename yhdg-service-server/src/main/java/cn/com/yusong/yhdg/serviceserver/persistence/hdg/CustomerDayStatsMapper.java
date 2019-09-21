package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CustomerDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerDayStatsMapper extends MasterMapper {
    public CustomerDayStats find(@Param("customerId") long customerId, @Param("statsDate") String statsDate);

    public CustomerDayStats sumByMonth(@Param("customerId") long customerId, @Param("statsMonth") String statsMonth);

    public int findDayCount(@Param("statsDate") String statsDate);

    public int findMonthCount(@Param("statsMonth") String statsMonth);

    public int insert(CustomerDayStats customerDayStats);

    public int update(CustomerDayStats customerDayStats);
}
