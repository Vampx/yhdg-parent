package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentForegiftInOutMoney;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface AgentForegiftInOutMoneyMapper extends MasterMapper {
    int insert(AgentForegiftInOutMoney entity);
}
