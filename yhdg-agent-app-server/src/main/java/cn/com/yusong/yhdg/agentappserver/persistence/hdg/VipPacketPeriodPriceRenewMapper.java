package cn.com.yusong.yhdg.agentappserver.persistence.hdg;


import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPriceRenew;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VipPacketPeriodPriceRenewMapper extends MasterMapper {
    VipPacketPeriodPriceRenew find(@Param("id") long id);
    List<VipPacketPeriodPriceRenew> findList(@Param("packetPriceId") Long packetPriceId);
    int insert(VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew);
    int update(VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew);
    int delete(long id);
    int deleteByPriceId(@Param("packetPriceId") Long packetPriceId);
}
