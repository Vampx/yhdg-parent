package cn.com.yusong.yhdg.serviceserver.persistence.zd;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RentOrderMapper extends MasterMapper {
    public RentOrder find(@Param("id") String id);

    public List<AgentDayStats> findAgentActiveCustomerCount(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}