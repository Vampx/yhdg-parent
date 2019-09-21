package cn.com.yusong.yhdg.frontserver.persistence.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface TerminalUploadLogMapper extends MasterMapper {
    int updateStatus(TerminalUploadLog terminalUploadLog);

    int update(TerminalUploadLog uploadLog);
}
