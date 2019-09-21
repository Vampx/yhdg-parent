package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PushOrderMessage;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface PushOrderMessageMapper extends MasterMapper {
    int insert(PushOrderMessage message);
}
