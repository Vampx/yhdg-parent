package cn.com.yusong.yhdg.agentserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

import java.util.List;

public interface PacketPeriodActivityMapper extends MasterMapper {
    PacketPeriodActivity find(long id);
    int findPageCount(PacketPeriodActivity packetPeriodActivity);
    List<PacketPeriodActivity> findPageResult(PacketPeriodActivity packetPeriodActivity);
    int insert(PacketPeriodActivity packetPeriodActivity);
    int update(PacketPeriodActivity packetPeriodActivity);
    int delete(long id);
}
