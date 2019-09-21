package cn.com.yusong.yhdg.agentserver.persistence.basic;


import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMapper extends MasterMapper {
    int hasRecordByProperty(@Param("property") String property, @Param("value") Object value);

    Agent find(long id);

    int findCountByParentId(@Param("id") int id);

    AgentInfo findAgentInfo(@Param("id") int id);

    List<Agent> findAll();

    List<Agent> topAgentList();


    int findPageCount(Agent search);

    List<Agent> findPageResult(Agent search);

    int findTreePageCount(Agent search);

    List<Agent> findTreePageResult(Agent search);

    List<Agent> findByParentId(@Param("parentId") Integer parentId);

    List<Agent> findByPartnerId(@Param("partnerId") Integer partnerId);

    int insert(Agent entity);

    int update(Agent entity);

    int delete(int id);

    int updateBalance(@Param("id") int id, @Param("balance") long balance);

    int updateForegift(@Param("id") int id, @Param("foregiftBalance") int foregiftBalance, @Param("foregiftRemainMoney") int foregiftRemainMoney, @Param("foregiftBalanceRatio") int foregiftBalanceRatio);

    int updateZdForegift(@Param("id") int id, @Param("foregiftBalance") int foregiftBalance, @Param("foregiftRemainMoney") int foregiftRemainMoney, @Param("foregiftBalanceRatio") int foregiftBalanceRatio);

    int updateOrderNum(@Param("id") int id, @Param("orderNum") int orderNum);

    int updatePayPeople(@Param("id") int id, @Param("payPeopleName") String payPeopleName,
                        @Param("payPeopleMpOpenId") String payPeopleMpOpenId, @Param("payPeopleFwOpenId") String payPeopleFwOpenId,
                        @Param("payPeopleMobile") String payPeopleMobile, @Param("payPassword") String payPassword);
}
