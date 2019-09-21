package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.domain.yms.TerminalCode;
import cn.com.yusong.yhdg.frontserver.persistence.yms.TerminalCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalCodeService {
    @Autowired
    TerminalCodeMapper terminalCodeMapper;

    public TerminalCode find(String id) {
        return terminalCodeMapper.find(id);
    }

    public TerminalCode findByCode(String code) {
        return terminalCodeMapper.findByCode(code);
    }

    public int insert(TerminalCode entity) {
        return terminalCodeMapper.insert(entity);
    }
}
