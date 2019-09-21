package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.RentPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentPriceService extends AbstractService {

    @Autowired
    RentPriceMapper rentPriceMapper;

    public RentPrice find(long id) {
        return rentPriceMapper.find(id);
    }

    public List<RentPrice> findListBySettingId(long settingId) {
        return rentPriceMapper.findListBySettingId(settingId);
    }
}
