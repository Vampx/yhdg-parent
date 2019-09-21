package cn.com.yusong.yhdg.agentserver.service.yms;


import cn.com.yusong.yhdg.agentserver.persistence.yms.TerminalPropertyMapper;
import cn.com.yusong.yhdg.common.domain.yms.TerminalProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TerminalPropertyService {

    @Autowired
    TerminalPropertyMapper terminalPropertyMapper;

    public List<TerminalProperty> findByTerminal(String terminalId){
        return terminalPropertyMapper.findByTerminal(terminalId);
    }

    @Transactional(rollbackFor=Throwable.class)
    public int insert(String terminalId, int[] active, String[] property, String[] value) {
        int effect = 0;
        terminalPropertyMapper.deleteByTerminal(terminalId);
        if(active != null && property != null && value != null) {

            for(int i = 0; i < active.length; i++) {
                TerminalProperty record = new TerminalProperty();
                record.setOrderNum(i + 1);
                record.setTerminalId(terminalId);
                record.setIsActive(active[i]);
                record.setPropertyName(property[i]);
                record.setPropertyValue(value[i]);

                terminalPropertyMapper.insert(record);
                effect++;
            }
        }
        return effect;
    }

}
