package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.PacketPeriodOrderRefundMapper;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacketPeriodOrderRefundService {

    @Autowired
    PacketPeriodOrderRefundMapper packetPeriodOrderRefundMapper;

    public int insert(PacketPeriodOrderRefund packetPeriodOrderRefund) {
        return packetPeriodOrderRefundMapper.insert(packetPeriodOrderRefund);
    }

}
