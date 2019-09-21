package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalSequence;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalSequenceMapper;
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
        return StringUtils.leftPad(Long.toString(entity.getId(), 35), 6, '0').toUpperCase();
    }
}
