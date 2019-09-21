package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.persistence.hdg.PacketPeriodOrderAllotMapper;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Service
public class PacketPeriodOrderAllotService extends AbstractService {

    @Autowired
    PacketPeriodOrderAllotMapper packetPeriodOrderAllotMapper;

    public String exist(String suffix) {
        return packetPeriodOrderAllotMapper.exist(suffix);
    }

    public List<PacketPeriodOrderAllot> findByOrder(String cabinetId, int orgType, int agentId, int serviceType, String statsDate, String suffix, int offset, int limit) {
        return packetPeriodOrderAllotMapper.findByOrder(cabinetId, orgType, agentId, serviceType, statsDate, suffix, offset, limit);
    }

//    public List<PacketPeriodOrderAllot> findShopDayIncome(int orgType, String shopId, int serviceType, String statsDate, String suffix) {
//        return packetPeriodOrderAllotMapper.findShopDayIncome(orgType, shopId, serviceType, statsDate, suffix);
//    }

    public List<PacketPeriodOrderAllot> findByAgentCompany(String statsDate, Integer agentId, String agentCompanyId, Integer orgType) throws ParseException {
        Date date = DateUtils.parseDate(statsDate, new String[]{Constant.DATE_FORMAT});
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String suffix = String.format("%d%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.WEEK_OF_YEAR));
        return packetPeriodOrderAllotMapper.findByAgentCompany(statsDate, agentId, agentCompanyId, suffix, orgType);
    }

    public List<PacketPeriodOrderAllot> findShopMonthIncome(int orgType, String shopId, int serviceType, String suffix, Date beginDate, Date endDate) {
        return packetPeriodOrderAllotMapper.findShopMonthIncome(orgType, shopId, serviceType, suffix, beginDate, endDate);
    }

    public List<PacketPeriodOrderAllot> findAgentCompanyMonthIncome(int orgType, String agentCompanyId, int serviceType, String suffix, Date beginDate, Date endDate) {
        return packetPeriodOrderAllotMapper.findAgentCompanyMonthIncome(orgType, agentCompanyId, serviceType, suffix, beginDate, endDate);
    }

    public List<PacketPeriodOrderAllot> findAgentCompanyDayIncome(int orgType, String agentCompanyId, int serviceType, String suffix, String statsDate) {
        return packetPeriodOrderAllotMapper.findAgentCompanyDayIncome(orgType, agentCompanyId, serviceType, suffix, statsDate);
    }

    public boolean contain(String columnName, String suffix) {
        return packetPeriodOrderAllotMapper.contain(columnName, suffix) == 1;
    }
}
