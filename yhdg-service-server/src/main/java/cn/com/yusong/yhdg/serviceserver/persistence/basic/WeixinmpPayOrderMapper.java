package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpPayOrder;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


public interface WeixinmpPayOrderMapper extends MasterMapper {
    public List<WeixinmpPayOrder> findPartnerIncrement(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
