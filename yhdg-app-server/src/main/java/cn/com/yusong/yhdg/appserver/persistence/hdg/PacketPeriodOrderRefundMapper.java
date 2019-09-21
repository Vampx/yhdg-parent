package cn.com.yusong.yhdg.appserver.persistence.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.common.persistence.MasterMapper;

public interface PacketPeriodOrderRefundMapper extends MasterMapper {
    public int insert(PacketPeriodOrderRefund packetPeriodOrderRefund);
}
