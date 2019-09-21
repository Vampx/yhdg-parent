package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.PacketPeriodActivityMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.PacketPeriodPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacketPeriodActivityService extends AbstractService {

    @Autowired
    PacketPeriodActivityMapper packetPeriodActivityMapper;

    public List<PacketPeriodActivity> findList(Integer agentId, Integer batteryType) {
        return packetPeriodActivityMapper.findList(agentId, batteryType);
    }
}
