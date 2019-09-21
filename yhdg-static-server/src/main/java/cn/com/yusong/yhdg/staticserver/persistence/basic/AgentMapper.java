package cn.com.yusong.yhdg.staticserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface AgentMapper extends MasterMapper {
    public Agent find(int id);

    public int updateForegift(@Param("id") int id, @Param("foregiftBalance") int foregiftBalance, @Param("foregiftRemainMoney") int foregiftRemainMoney, @Param("foregiftBalanceRatio") int foregiftBalanceRatio);

    int updateZdForegift(@Param("id") int id, @Param("foregiftBalance") int foregiftBalance, @Param("foregiftRemainMoney") int foregiftRemainMoney, @Param("foregiftBalanceRatio") int foregiftBalanceRatio);

    public int updateBalance(@Param("id") long id, @Param("balance") long balance);

}
