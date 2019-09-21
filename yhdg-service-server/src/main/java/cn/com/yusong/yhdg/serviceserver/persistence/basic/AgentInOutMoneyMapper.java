package cn.com.yusong.yhdg.serviceserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AgentInOutMoneyMapper extends MasterMapper {
    List<AgentInOutMoney>  findByAgent(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime);
}
