package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyMapper extends MasterMapper {
	AgentCompany find(@Param("id") String id);
	List<AgentCompany> findVipAgentCompanyList(@Param("agentId") Integer agentId,
									 @Param("keyword") String keyword,
									 @Param("offset") int offset,
									 @Param("limit") int limit
	);
	int updateBalance(@Param("id") String id, @Param("balance") int balance);
	int updatePayPassword(@Param("id") String id, @Param("payPassword")String payPassword);
	int updateInfo(AgentCompany agentCompany);
}
