package cn.com.yusong.yhdg.appserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentMapper extends MasterMapper {
    public Agent find(int id);

    List<Agent> findByParentId(@Param("parentId") Integer parentId);

    List<Integer> findByPartnerId(@Param("partnerId") Integer partnerId);

    List<Integer> findByWeixinmp(@Param("weixinmpId") Integer weixinmpId);

    List<Integer> findByAlipayfw(@Param("alipayfwId") Integer alipayfwId);

    List<Integer> findByPhoneapp(@Param("phoneappId") Integer phoneappId);

    int updateBalance(@Param("id") int id, @Param("balance") long balance);

    int updateForegift(@Param("id") int id, @Param("foregiftBalance") int foregiftBalance, @Param("foregiftRemainMoney") int foregiftRemainMoney, @Param("foregiftBalanceRatio") int foregiftBalanceRatio);

    int updateZdForegift(@Param("id") int id, @Param("foregiftBalance") int foregiftBalance, @Param("foregiftRemainMoney") int foregiftRemainMoney, @Param("foregiftBalanceRatio") int foregiftBalanceRatio);

}
