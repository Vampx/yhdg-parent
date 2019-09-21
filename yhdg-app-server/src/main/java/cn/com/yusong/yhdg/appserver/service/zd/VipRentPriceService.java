package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.VipRentPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VipRentPriceService extends AbstractService {

    @Autowired
    VipRentPriceMapper vipRentPriceMapper;

    public VipRentPrice findOneByShopId(int agentId, int batteryType, String shopId) {
        return vipRentPriceMapper.findOneByShopId(agentId, batteryType, shopId, new Date());
    }

    public VipRentPrice findOneByCustomerMobile(int agentId, int batteryType, String mobile) {
        return vipRentPriceMapper.findOneByCustomerMobile(agentId, batteryType, mobile, new Date());
    }

    public VipRentPrice findOneByAgentCompanyId(int agentId, int batteryType, String agentCompanyId) {
        return vipRentPriceMapper.findOneByAgentCompanyId(agentId, batteryType, agentCompanyId, new Date());
    }
}
