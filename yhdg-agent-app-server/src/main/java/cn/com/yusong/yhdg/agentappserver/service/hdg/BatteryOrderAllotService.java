package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.BatteryOrderAllotMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderAllot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class BatteryOrderAllotService extends AbstractService {
    @Autowired
    BatteryOrderAllotMapper batteryOrderAllotMapper;

    public String exist(String suffix) {
        return batteryOrderAllotMapper.exist(suffix);
    }

    public List<BatteryOrderAllot> findByOrder(String cabinetId, int orgType, int agentId, int serviceType, String statsDate, String suffix, int offset, int limit) {
        return batteryOrderAllotMapper.findByOrder(cabinetId, orgType, agentId, serviceType, statsDate, suffix, offset, limit);
    }

    public List<BatteryOrderAllot> findShopMonthIncome(int orgType, String shopId, int serviceType, String suffix, Date beginDate, Date endDate) {
        return batteryOrderAllotMapper.findShopMonthIncome(orgType, shopId, serviceType, suffix, beginDate, endDate);
    }

    public List<BatteryOrderAllot> findAgentCompanyMonthIncome(int orgType, String agentCompanyId, int serviceType, String suffix, Date beginDate, Date endDate) {
        return batteryOrderAllotMapper.findAgentCompanyMonthIncome(orgType, agentCompanyId, serviceType, suffix, beginDate, endDate);
    }

    public List<BatteryOrderAllot> findAgentCompanyDayIncome(int orgType, String agentCompanyId, int serviceType, String suffix, String statsDate) {
        return batteryOrderAllotMapper.findAgentCompanyDayIncome(orgType, agentCompanyId, serviceType, suffix, statsDate);
    }

    public boolean contain(String columnName, String suffix) {
        return batteryOrderAllotMapper.contain(columnName, suffix) == 1;
    }
}
