package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.PacketPeriodPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PacketPeriodPriceService extends AbstractService {

    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;

    public PacketPeriodPrice find(Long id) {
        return packetPeriodPriceMapper.find(id);
    }

    public List<PacketPeriodPrice> findList(Integer agentId, Integer batteryType, Long foregiftId) {
        return packetPeriodPriceMapper.findList(agentId, batteryType, foregiftId);
    }

}
