package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalUploadLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalUploadLogService {
    @Autowired
    TerminalUploadLogMapper terminalUploadLogMapper;

    public int updateStatus(TerminalUploadLog terminalUploadLog) {
        return terminalUploadLogMapper.updateStatus(terminalUploadLog);
    }

    public int update(TerminalUploadLog uploadLog) {
        return terminalUploadLogMapper.update(uploadLog);
    }
}
