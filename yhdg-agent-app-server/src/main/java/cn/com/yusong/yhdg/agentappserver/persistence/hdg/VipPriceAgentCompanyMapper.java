package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPriceAgentCompany;
import cn.com.yusong.yhdg.common.domain.hdg.VipPriceShop;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPriceAgentCompanyMapper extends MasterMapper {
    List<VipPriceAgentCompany> findListByPriceId(@Param("priceId") long priceId);
    VipPriceAgentCompany findByPriceId(@Param("priceId") long priceId, @Param("agentCompanyId") String agentCompanyId);
    int insert(VipPriceAgentCompany vipPriceAgentCompany);
    int delete(@Param("id") long id);
    int deleteByPriceId(@Param("priceId") long priceId);
}
