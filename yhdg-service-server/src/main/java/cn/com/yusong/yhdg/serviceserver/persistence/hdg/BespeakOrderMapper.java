package cn.com.yusong.yhdg.serviceserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.BespeakOrder;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface BespeakOrderMapper extends MasterMapper {
    public List<BespeakOrder> findExpireList(@Param("status") int status, @Param("expireTime") Date expireTime,@Param("offset") int offset, @Param("limit") int limit);

    public int updateExpiredOrder(@Param("id") String id, @Param("fromStatus") int fromStatus, @Param("toStatus") int toStatus);
}
