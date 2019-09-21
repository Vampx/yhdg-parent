package cn.com.yusong.yhdg.frontserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalCommand;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TerminalCommandMapper extends MasterMapper {
    List<TerminalCommand> findWaitExec(@Param("terminalId") String terminalId, @Param("status") Integer status);
    int dispatch(@Param("id") long id, @Param("status") int status, @Param("dispatchTime") Date dispatchTime);
    int exec(@Param("id") long id, @Param("status") int status, @Param("execTime") Date execTime, @Param("failureReason") String failureReason);
}
