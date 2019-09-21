package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.persistence.zd.RentPeriodOrderAllotMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrderAllot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RentPeriodOrderAllotService extends AbstractService {
    @Autowired
    RentPeriodOrderAllotMapper rentPeriodOrderAllotMapper;

    public String exist(String suffix) {
        return rentPeriodOrderAllotMapper.exist(suffix);
    }

    public List<RentPeriodOrderAllot> findShopDayIncome(int orgType, String shopId, int serviceType, String statsDate, String suffix) {
        return rentPeriodOrderAllotMapper.findShopDayIncome(orgType, shopId, serviceType, statsDate, suffix);
    }

    public List<RentPeriodOrderAllot> findShopMonthIncome(int orgType, String shopId, int serviceType, String suffix, Date beginDate, Date endDate) {
        return rentPeriodOrderAllotMapper.findShopMonthIncome(orgType, shopId, serviceType, suffix, beginDate, endDate);
    }

    public List<RentPeriodOrderAllot> findAgentCompanyMonthIncome(int orgType, String agentCompanyId, int serviceType, String suffix, Date beginDate, Date endDate) {
        return rentPeriodOrderAllotMapper.findAgentCompanyMonthIncome(orgType, agentCompanyId, serviceType, suffix, beginDate, endDate);
    }

    public List<RentPeriodOrderAllot> findAgentCompanyDayIncome(int orgType, String agentCompanyId, int serviceType, String suffix, String statsDate) {
        return rentPeriodOrderAllotMapper.findAgentCompanyDayIncome(orgType, agentCompanyId, serviceType, suffix, statsDate);
    }

    public List<RentPeriodOrderAllot> findByOrder(String shopId, int orgType, int agentId, int serviceType, String statsDate, String suffix, int offset, int limit) {
        return rentPeriodOrderAllotMapper.findByOrder(shopId, orgType, agentId, serviceType, statsDate, suffix, offset, limit);
    }

    public boolean contain(String columnName, String suffix) {
        return rentPeriodOrderAllotMapper.contain(columnName, suffix) == 1;
    }
}
