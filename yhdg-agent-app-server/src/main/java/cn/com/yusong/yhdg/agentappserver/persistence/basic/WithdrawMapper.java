package cn.com.yusong.yhdg.agentappserver.persistence.basic;

import cn.com.yusong.yhdg.common.domain.basic.Withdraw;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WithdrawMapper extends MasterMapper {
    Withdraw find(@Param("id") String id);
    List<Withdraw> findByEstate(@Param("type")int type, @Param("estateId")Long estateId, @Param("offset")int offset, @Param("limit")int limit);
    List<Withdraw> findByShop(@Param("type")int type, @Param("shopId")String shopId, @Param("offset")int offset, @Param("limit")int limit);
    List<Withdraw> findByAgentCompany(@Param("type") int type, @Param("agentCompanyId") String agentCompanyId, @Param("offset") int offset, @Param("limit") int limit);
    List<Withdraw> findByAgent(@Param("type")int type, @Param("agentId")Integer agentId, @Param("offset")int offset, @Param("limit")int limit);
    int insert(Withdraw entity);
}
