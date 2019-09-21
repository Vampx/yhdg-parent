package cn.com.yusong.yhdg.frontserver.service.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalCommand;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalCommandMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TerminalCommandService {
    @Autowired
    TerminalCommandMapper terminalCommandMapper;

    public List<TerminalCommand> findWaitExec(String terminalId, Integer status) {
        return terminalCommandMapper.findWaitExec(terminalId, status);
    }

    public int dispatch(long id, int status, Date dispatchTime) {
        return terminalCommandMapper.dispatch(id, status, dispatchTime);
    }
    public int exec(long id, int status, Date execTime, String failureReason) {
        return terminalCommandMapper.exec(id, status, execTime, failureReason);
    }


}
