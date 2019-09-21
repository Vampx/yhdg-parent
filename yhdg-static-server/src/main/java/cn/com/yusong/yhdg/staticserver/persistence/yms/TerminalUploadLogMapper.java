package cn.com.yusong.yhdg.staticserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TerminalUploadLogMapper extends MasterMapper {
    TerminalUploadLog find(Long id);

    public List<TerminalUploadLog> findByTerminal(@Param("terminalId") String terminalId, @Param("status") Integer status);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int update(TerminalUploadLog uploadLog);
}
