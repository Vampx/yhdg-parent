package cn.com.yusong.yhdg.agentserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalCrashLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalCrashLogMapper extends MasterMapper {
    TerminalCrashLog find(long id);
    int findPageCount(TerminalCrashLog search);
    List<TerminalCrashLog> findPageResult(TerminalCrashLog search);
    int insert(TerminalCrashLog log);
    int deleteByTerminalId(@Param("terminalId") String terminalId, @Param("limit") int limit);
}
