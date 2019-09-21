package cn.com.yusong.yhdg.webserver.persistence.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface TerminalUploadLogMapper extends MasterMapper {
    TerminalUploadLog find(long id);
    List<TerminalUploadLog> findPageResult(TerminalUploadLog search);
    int findPageCount(TerminalUploadLog search);
    int insert(TerminalUploadLog terminalUploadLog);
    int delete(long id);
}
