package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.VipPacketPeriodPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.VipPacketPeriodPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipPacketPeriodPriceService extends AbstractService {

    @Autowired
    VipPacketPeriodPriceMapper vipPacketPeriodPriceMapper;

    public int findCountByForegiftId(Long priceId, long foregiftId) {
        return vipPacketPeriodPriceMapper.findCountByForegiftId(priceId, foregiftId);
    }

    public List<VipPacketPeriodPrice> findByPriceIdAndForegiftId(Long priceId, Long foregiftId) {
        return vipPacketPeriodPriceMapper.findByPriceIdAndForegiftId(priceId, foregiftId);
    }

}
