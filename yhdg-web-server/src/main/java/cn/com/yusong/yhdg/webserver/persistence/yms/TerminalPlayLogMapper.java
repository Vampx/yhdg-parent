package cn.com.yusong.yhdg.webserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalPlayLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalPlayLogMapper extends MasterMapper {
    TerminalPlayLog find(@Param("id") long id, @Param("suffix") String suffix);
    int findPageCount(TerminalPlayLog search);
    List<TerminalPlayLog> findPageResult(TerminalPlayLog search);
    List<String> findTablelist();
    String tableExist(@Param("suffix") String suffix);
    int insert(TerminalPlayLog search);
    int createTable(@Param("suffix") String suffix);
    int createFKAgentId(@Param("suffix") String suffix);
    int createFKTerminalId(@Param("suffix") String suffix);
    int deleteByTerminalId(@Param("terminalId") String terminalId, @Param("limit") int limit, @Param("suffix") String suffix);
}
