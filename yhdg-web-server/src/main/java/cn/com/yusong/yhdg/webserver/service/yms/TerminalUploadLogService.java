package cn.com.yusong.yhdg.webserver.service.yms;

import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.domain.yms.TerminalUploadLog;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.yms.TerminalMapper;
import cn.com.yusong.yhdg.webserver.persistence.yms.TerminalUploadLogMapper;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TerminalUploadLogService extends AbstractService {

    @Autowired
    TerminalUploadLogMapper terminalUploadLogMapper;
    @Autowired
    TerminalMapper terminalMapper;
    @Autowired
    CabinetService cabinetService;

    public TerminalUploadLog find(long id) {
        return terminalUploadLogMapper.find(id);
    }

    public Page findPage(TerminalUploadLog search) {
        Page page = search.buildPage();
        page.setTotalItems(terminalUploadLogMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        page.setResult(terminalUploadLogMapper.findPageResult(search));
        return page;
    }

    public int insert(TerminalUploadLog terminalUploadLog) {
        return terminalUploadLogMapper.insert(terminalUploadLog);
    }

    public int delete(long id) {
        return terminalUploadLogMapper.delete(id);
    }

    public ExtResult noticeUpload(TerminalUploadLog terminalUploadLog, String logTime) {
        Terminal terminal = terminalMapper.find(terminalUploadLog.getTerminalId());
        terminalUploadLog.setTerminalId(terminal.getId());

        Cabinet cabinet = cabinetService.findByTerminalId(terminalUploadLog.getTerminalId());
        terminalUploadLog.setAgentId(cabinet != null ? cabinet.getAgentId() : null);
        terminalUploadLog.setStatus(TerminalUploadLog.Status.INIT.getValue());
        terminalUploadLog.setLogTime(logTime);
        terminalUploadLog.setCreateTime(new Date());
        terminalUploadLog.setUploadTime(new Date());
        terminalUploadLogMapper.insert(terminalUploadLog);

        return ExtResult.successResult();
    }
}
