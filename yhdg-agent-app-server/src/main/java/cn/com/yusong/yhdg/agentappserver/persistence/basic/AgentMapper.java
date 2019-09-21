package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMapper extends MasterMapper {
    Agent find(int id);

    List<Agent> findAll(@Param("agentName") String agentName);

    List<Agent> findByParent(@Param("parentId") int parentId);

    List<Integer> findAllId();

    int insert(Agent entity);

    int update(Agent entity);

    int updatePayPeople(Agent agent);

    int updatePayPassword(@Param("id") int id,  @Param("payPassword")String payPassword);

    int updateBalance(@Param("id") int id, @Param("balance") long balance);

    int updateForegift(@Param("id") int id, @Param("foregiftBalance") int foregiftBalance, @Param("foregiftRemainMoney") int foregiftRemainMoney, @Param("foregiftBalanceRatio") int foregiftBalanceRatio);

    int updateZdForegift(@Param("id") int id, @Param("foregiftBalance") int foregiftBalance, @Param("foregiftRemainMoney") int foregiftRemainMoney, @Param("foregiftBalanceRatio") int foregiftBalanceRatio);

}
