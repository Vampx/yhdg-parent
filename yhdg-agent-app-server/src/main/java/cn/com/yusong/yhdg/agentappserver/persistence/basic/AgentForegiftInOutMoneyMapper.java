package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface AgentForegiftInOutMoneyMapper extends MasterMapper {

    List<AgentForegiftInOutMoney> findList(@Param("agentId") int agentId, @Param("category") int category, @Param("bizTypeList") List<Integer> bizTypeList, @Param("offset") int offset, @Param("limit") int limit);

    int insert(AgentForegiftInOutMoney entity);

}