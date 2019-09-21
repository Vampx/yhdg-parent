package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.persistence.zc.PriceSettingMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.RentPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PriceSettingService extends AbstractService {

    @Autowired
    PriceSettingMapper priceSettingMapper;
    @Autowired
    RentPriceMapper rentPriceMapper;

    public PriceSetting find(Long id) {
        return priceSettingMapper.find(id);
    }

    public  List<PriceSetting> findAgentIdAll(List agentIdList) {
        return priceSettingMapper.findAgentIdAll(agentIdList);
    }

    public  List<PriceSetting> findShopIdAll(String shopId) {
        return priceSettingMapper.findShopIdAll(shopId);
    }
}
