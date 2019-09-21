package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.PushOrderMessageMapper;
import cn.com.yusong.yhdg.common.domain.hdg.PushOrderMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PushOrderMessageService {
    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;

    public void insert(PushOrderMessage pushOrderMessage) {
        pushOrderMessageMapper.insert(pushOrderMessage);
    }
}
