package cn.com.yusong.yhdg.webserver.persistence.basic;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentCompanyMapper extends MasterMapper {
	AgentCompany find(@Param("id") String id);

	List<AgentCompany> findByAgent(@Param("agentId") Integer agentId);

	List<AgentCompany> findAll();

	String findMaxId(String id);

	int findPageCount(AgentCompany agentCompany);

	List<AgentCompany> findPageResult(AgentCompany agentCompany);

	int insert(AgentCompany agentCompany);

	int update(AgentCompany agentCompany);

	int updateRatio(@Param("id") String id, @Param("companyRatio") Integer companyRatio, @Param("keepShopRatio") Integer keepShopRatio, @Param("companyFixedMoney") Integer companyFixedMoney, @Param("ratioBaseMoney") Integer ratioBaseMoney);

	int delete(@Param("id") String id);

	int updatePayPeople(@Param("id") String id, @Param("payPeopleName") String payPeopleName,
						@Param("payPeopleMpOpenId") String payPeopleMpOpenId, @Param("payPeopleFwOpenId") String payPeopleFwOpenId,
						@Param("payPeopleMobile") String payPeopleMobile, @Param("payPassword") String payPassword);

	int updateBalance(@Param("id") String id, @Param("balance") long balance);
}
