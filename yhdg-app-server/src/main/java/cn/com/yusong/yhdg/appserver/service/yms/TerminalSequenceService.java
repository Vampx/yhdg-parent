package cn.com.yusong.yhdg.appserver.service.yms;

import cn.com.yusong.yhdg.appserver.persistence.yms.TerminalSequenceMapper;
import cn.com.yusong.yhdg.common.domain.yms.TerminalSequence;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalSequenceService {

    @Autowired
    TerminalSequenceMapper terminalSequenceMapper;

    public String nextCode() {
        TerminalSequence entity = new TerminalSequence();
        terminalSequenceMapper.insert(entity);
        return StringUtils.leftPad(Integer.toString(entity.getId().intValue(), 35), 6, '0').toUpperCase();
    }
}
