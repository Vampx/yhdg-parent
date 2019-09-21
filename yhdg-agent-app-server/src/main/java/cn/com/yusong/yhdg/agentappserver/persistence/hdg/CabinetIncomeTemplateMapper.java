package cn.com.yusong.yhdg.agentappserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetIncomeTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CabinetIncomeTemplateMapper extends MasterMapper {

    CabinetIncomeTemplate findByAgentId(@Param("agentId") int agentId);

}
