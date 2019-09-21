package cn.com.yusong.yhdg.staticserver.service.yms;

import cn.com.yusong.yhdg.staticserver.persistence.yms.TerminalUploadLogMapper;
import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalUploadLogService {
    @Autowired
    TerminalUploadLogMapper terminalUploadLogMapper;

    public TerminalUploadLog find(Long id) {
        return terminalUploadLogMapper.find(id);
    }

    public List<TerminalUploadLog> findByTerminal(String terminalId, Integer status) {
        return terminalUploadLogMapper.findByTerminal(terminalId, status);
    }

    public int updateStatus(Long id, Integer status) {
        return terminalUploadLogMapper.updateStatus(id, status);
    }

    public int update(TerminalUploadLog uploadLog) {
        return terminalUploadLogMapper.update(uploadLog);
    }
}
