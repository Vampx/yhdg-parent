package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CustomerDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerDayStatsMapper extends MasterMapper {
    public int findPageCount(CustomerDayStats search);

    int deleteByCustomerId(@Param("customerId") long customerId);

    public List<CustomerDayStats> findPageResult(CustomerDayStats search);
}
