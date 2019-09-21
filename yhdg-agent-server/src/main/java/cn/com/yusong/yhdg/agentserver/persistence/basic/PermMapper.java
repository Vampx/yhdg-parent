package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.Perm;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;


public interface PermMapper extends MasterMapper {

    public List<Perm> findAll();
}
