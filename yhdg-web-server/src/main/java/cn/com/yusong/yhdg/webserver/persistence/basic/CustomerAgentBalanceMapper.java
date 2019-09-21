package cn.com.yusong.yhdg.webserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.CustomerAgentBalance;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

public interface CustomerAgentBalanceMapper  extends MasterMapper {
    CustomerAgentBalance findByCustomerId(@Param("customerId") Long customerId);

    int resignation(@Param("customerId") long customerId, @Param("agentBalance") Integer agentBalance);

    int clearAgentId(@Param("customerId") long customerId);

    int insert(CustomerAgentBalance customerAgentBalance);

    int updateWhitelistPriceGroupId(@Param("customerId") long customerId, @Param("agentId") Integer agentId);

    int updateAgentBalance(@Param("customerId") long customerId, @Param("agentBalance") Integer agentBalance);

    int deleteByCustomerId(@Param("customerId") long customerId);

    int updateBalance(@Param("customerId") long customerId, @Param("agentBalance") long agentBalance);

}
