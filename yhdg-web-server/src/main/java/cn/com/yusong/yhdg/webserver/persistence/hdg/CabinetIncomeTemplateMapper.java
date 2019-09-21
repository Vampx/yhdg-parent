package cn.com.yusong.yhdg.webserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.CabinetIncomeTemplate;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CabinetIncomeTemplateMapper extends MasterMapper {

    CabinetIncomeTemplate findByAgentId(@Param("agentId") int agentId);

    CabinetIncomeTemplate find(@Param("id") int id);

    int findByAgentIdCount(@Param("agentId") int agentId);

    int insert(CabinetIncomeTemplate entity);

    int update(CabinetIncomeTemplate entity);

    List<CabinetIncomeTemplate> findPageResult(CabinetIncomeTemplate entity);

    int findPageCount(CabinetIncomeTemplate entity);

    int delete(@Param("id") int id);

}
