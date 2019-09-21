package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.VipRentPeriodPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPeriodPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VipRentPeriodPriceService extends AbstractService {

    @Autowired
    VipRentPeriodPriceMapper vipRentPeriodPriceMapper;

    public int findCountByForegiftId(Long priceId,long foregiftId) {
        return vipRentPeriodPriceMapper.findCountByForegiftId(priceId, foregiftId);
    }

    public List<VipRentPeriodPrice> findByPriceIdAndForegiftId(Long priceId, Long foregiftId) {
        return vipRentPeriodPriceMapper.findByPriceIdAndForegiftId(priceId, foregiftId);
    }

}
