package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.ExchangePriceTime;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExchangePriceTimeMapper extends MasterMapper {
    ExchangePriceTime find(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);
    List<ExchangePriceTime> findList(Integer agentId);
    int insert(ExchangePriceTime exchangePriceTime);
    int delete(@Param("agentId") Integer agentId, @Param("batteryType") Integer batteryType);

}
