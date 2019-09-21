package cn.com.yusong.yhdg.frontserver.service.yms;


import cn.com.yusong.yhdg.common.domain.yms.TerminalProperty;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalPropertyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalPropertyService {

    @Autowired
    TerminalPropertyMapper terminalPropertyMapper;

    public List<TerminalProperty> findByTerminal(String terminalId){
        return terminalPropertyMapper.findByTerminal(terminalId);
    }

}
