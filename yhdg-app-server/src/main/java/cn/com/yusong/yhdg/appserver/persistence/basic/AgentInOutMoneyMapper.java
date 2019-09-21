package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface AgentInOutMoneyMapper extends MasterMapper {
    int insert(AgentInOutMoney entity);
}
