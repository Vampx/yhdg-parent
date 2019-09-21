package cn.com.yusong.yhdg.agentserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalStrategyMapper extends MasterMapper {
    TerminalStrategy find(long id);

    int hasRecordByProperty(@Param("property") String property, @Param("value") Object value);

    int findPageCount(TerminalStrategy search);

    List findPageResult(TerminalStrategy search);

    List<TerminalStrategy> findByAgent(@Param("agentId") Integer agentId);

    int insert(TerminalStrategy entity);

    int update(TerminalStrategy entity);

    int delete(long id);
}
