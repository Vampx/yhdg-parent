package cn.com.yusong.yhdg.agentserver.persistence.zd;


import cn.com.yusong.yhdg.common.domain.zd.VipRentPriceAgentCompany;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipRentPriceAgentCompanyMapper extends MasterMapper {
    List<VipRentPriceAgentCompany> findListByPriceId(@Param("priceId") long priceId);
    VipRentPriceAgentCompany findByPriceId(@Param("priceId") long priceId, @Param("agentCompanyId") String agentCompanyId);
    int insert(VipRentPriceAgentCompany vipRentPriceAgentCompany);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
