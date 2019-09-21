package cn.com.yusong.yhdg.appserver.service.yms;

import cn.com.yusong.yhdg.appserver.persistence.yms.TerminalCodeMapper;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCode;
import cn.com.yusong.yhdg.common.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TerminalCodeService extends AbstractService {
    @Autowired
    TerminalCodeMapper terminalCodeMapper;

    public TerminalCode find(String id) {
        return terminalCodeMapper.find(id);
    }

    public int insert(TerminalCode entity) {
        return terminalCodeMapper.insert(entity);
    }
}
