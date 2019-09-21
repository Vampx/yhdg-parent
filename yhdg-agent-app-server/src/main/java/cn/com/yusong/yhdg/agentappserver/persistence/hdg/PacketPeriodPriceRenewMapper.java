package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPriceRenew;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PacketPeriodPriceRenewMapper extends MasterMapper {
    PacketPeriodPriceRenew find(@Param("id") long id);
    List<PacketPeriodPriceRenew> findList(@Param("priceId") Long priceId);
    int insert(PacketPeriodPriceRenew packetPeriodPriceRenew);
    int update(PacketPeriodPriceRenew packetPeriodPriceRenew);
    int delete(long id);
    int deleteByPriceId(@Param("priceId") Long priceId);
}
