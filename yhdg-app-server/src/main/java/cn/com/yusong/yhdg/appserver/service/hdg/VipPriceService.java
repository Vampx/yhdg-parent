package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.VipPriceMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VipPriceService extends AbstractService {

    @Autowired
    VipPriceMapper vipPriceMapper;

    public VipPrice findOneByStationId(int agentId, int batteryType, String stationId) {
        return vipPriceMapper.findOneByStationId(agentId, batteryType, stationId, new Date());
    }

    public VipPrice findOneByCabinetId(int agentId, int batteryType, String cabinetId) {
        return vipPriceMapper.findOneByCabinetId(agentId, batteryType, cabinetId, new Date());
    }

    public VipPrice findOneByCustomerMobile(int agentId, int batteryType, String mobile) {
        return vipPriceMapper.findOneByCustomerMobile(agentId, batteryType, mobile, new Date());
    }

    public VipPrice findOneByAgentCompanyId(int agentId, int batteryType, String agentCompanyId) {
        return vipPriceMapper.findOneByAgentCompanyId(agentId, batteryType, agentCompanyId, new Date());
    }

}
