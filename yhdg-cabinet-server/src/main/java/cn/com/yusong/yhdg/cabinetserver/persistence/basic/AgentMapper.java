package cn.com.yusong.yhdg.cabinetserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentMapper extends MasterMapper {
    public Agent find(@Param("id") int id);

    int updateBalance(@Param("id") int id, @Param("balance") long balance);

}