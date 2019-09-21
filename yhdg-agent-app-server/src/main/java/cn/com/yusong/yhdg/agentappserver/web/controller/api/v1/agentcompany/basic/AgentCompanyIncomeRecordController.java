package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agentcompany.basic;

import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.AgentCompanyIncomeRatioHistoryMapper;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyIncomeRatioHistoryService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.BatteryOrderAllotService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.PacketPeriodOrderAllotService;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentPeriodOrderAllotService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyIncomeRatioHistory;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderAllot;
import cn.com.yusong.yhdg.common.domain.hdg.IncomeRatioHistory;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderAllot;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrderAllot;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("agent_api_v1_agentcompany_basic_agent_company_income_record")
@RequestMapping("/agent_api/v1/agentcompany/basic/agent_company_income_record")
public class AgentCompanyIncomeRecordController extends ApiController {
	static final Logger log = LogManager.getLogger(AgentCompanyIncomeRecordController.class);
	@Autowired
	BatteryOrderAllotService batteryOrderAllotService;
	@Autowired
	PacketPeriodOrderAllotService packetPeriodOrderAllotService;
	@Autowired
	RentPeriodOrderAllotService rentPeriodOrderAllotService;
    @Autowired
    AgentCompanyService agentCompanyService;
    @Autowired
    AgentCompanyIncomeRatioHistoryService agentCompanyIncomeRatioHistoryService;


	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ListParam {
		public String date;
	}

	/**
	 * 28-查询收款记录
	 */
	@ResponseBody
	@RequestMapping(value = "/list.htm")
	public RestResult list(@Valid @RequestBody ListParam param) throws Exception{
		String agentCompanyId = getTokenData().agentCompanyId;
        AgentCompany agentCompany = agentCompanyService.find(agentCompanyId);
        Date createTime = DateUtils.addMonths(agentCompany.getCreateTime(), -1);
        int isFinish = 0;
        List list = new ArrayList();

        Date beginDate = DateUtils.parseDate(param.date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);
        Date tempDate;
        if(beginDate.getTime() > createTime.getTime()){
            tempDate = beginDate;
        }else {
            tempDate = createTime;
            isFinish = 1;
        }
        while(tempDate.getTime() < endDate.getTime()) {
            tempDate = DateUtils.addWeeks(tempDate, 1);

            String suffix1 = BatteryOrderAllot.getSuffixByDate(tempDate);
            String exist1 = batteryOrderAllotService.exist(suffix1);
            if (StringUtils.isNotEmpty(exist1)) {
                //先判断表中是否有agent_company_id字段
                String columnName = "agent_company_id";
                if (batteryOrderAllotService.contain(columnName, suffix1)) {
                    List<BatteryOrderAllot> allotList = batteryOrderAllotService.findAgentCompanyMonthIncome(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix1, beginDate, endDate);
                    for (BatteryOrderAllot allot : allotList) {
                        NotNullMap map = new NotNullMap();
                        map.putLong("id", allot.getId());
                        map.putString("fullname", allot.getCustomerName());
                        map.putMobileMask("mobile", allot.getCustomerMobile());
                        map.putDateTime("payTime", allot.getPayTime());
                        map.putInteger("orderMoney", allot.getOrderMoney());
                        map.putDouble("money", allot.getMoney());
                        map.putInteger("type", AgentCompany.Type.HDG_BATTERY_ORDER.getValue());
                        map.putString("statsDate", allot.getStatsDate());
                        list.add(map);
                    }
                }
            }

            String suffix2 = PacketPeriodOrderAllot.getSuffixByDate(tempDate);
            String exist2 = packetPeriodOrderAllotService.exist(suffix2);
            if (StringUtils.isNotEmpty(exist2)){
                //先判断表中是否有agent_company_id字段
                String columnName = "agent_company_id";
                if (packetPeriodOrderAllotService.contain(columnName, suffix2)) {
                    List<PacketPeriodOrderAllot> allotList = packetPeriodOrderAllotService.findAgentCompanyMonthIncome(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix2, beginDate, endDate);
                    for (PacketPeriodOrderAllot allot : allotList) {
                        NotNullMap map = new NotNullMap();
                        map.putLong("id", allot.getId());
                        map.putString("fullname", allot.getCustomerName());
                        map.putMobileMask("mobile", allot.getCustomerMobile());
                        map.putDateTime("payTime", allot.getPayTime());
                        map.putInteger("orderMoney", allot.getOrderMoney());
                        map.putDouble("money", allot.getMoney());
                        map.putInteger("type", AgentCompany.Type.HDG_PACKET_PERIOD.getValue());
                        map.putString("statsDate", allot.getStatsDate());
                        list.add(map);
                    }
                }
            }

            String suffix3 = RentPeriodOrderAllot.getSuffixByDate(tempDate);
            String exist3 = rentPeriodOrderAllotService.exist(suffix3);
            if (StringUtils.isNotEmpty(exist3)){
                //先判断表中是否有agent_company_id字段
                String columnName = "agent_company_id";
                if (rentPeriodOrderAllotService.contain(columnName, suffix3)){
                    List<RentPeriodOrderAllot> allotList = rentPeriodOrderAllotService.findAgentCompanyMonthIncome(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix3, beginDate, endDate);
                    for (RentPeriodOrderAllot allot : allotList) {
                        NotNullMap map = new NotNullMap();
                        map.putLong("id", allot.getId());
                        map.putString("fullname", allot.getCustomerName());
                        map.putMobileMask("mobile", allot.getCustomerMobile());
                        map.putDateTime("payTime",allot.getPayTime());
                        map.putInteger("orderMoney", allot.getOrderMoney());
                        map.putDouble("money", allot.getMoney());
                        map.putInteger("type", AgentCompany.Type.ZD_RENT_PERIOD.getValue());
                        map.putString("statsDate", allot.getStatsDate());
                        list.add(map);
                    }
                }
            }
        }

        List listGroupByDay = groupByDay(list, param.date, agentCompanyId);
        if(!listGroupByDay.isEmpty() && listGroupByDay.size()>1) {
            Collections.sort(listGroupByDay, new Comparator<Map>() {
                public int compare(Map p1, Map p2) {
                    return -((Map)p1.get("stats")).get("statsDate").toString().compareTo(((Map)p2.get("stats")).get("statsDate").toString());
                }
            });
        }

        Map map = new HashMap();
        map.put("isFinish", isFinish);
        map.put("listMonth", listGroupByDay);

        return RestResult.dataResult(0, null, map);
	}

    private List groupByDay(List<NotNullMap> monthList, String date, String agentCompanyId) throws Exception {
        Date beginDate = DateUtils.parseDate(date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);
        AgentCompany agentCompany = agentCompanyService.find(agentCompanyId);

        List listGroupByDay = new ArrayList();
        while (beginDate.getTime() <= endDate.getTime()) {
            List list = new ArrayList();
            int totalCount = 0;
            double totalMoney = 0;

            for (NotNullMap map : monthList) {
                Date statsDate = DateUtils.parseDate((String) map.get("statsDate"), new String[]{Constant.DATE_FORMAT});
                if (statsDate.getTime() == beginDate.getTime()) {
                    totalCount += 1;
                    totalMoney += (Double) map.get("money");
                    list.add(map);
                }
            }

            if (totalCount > 0) {
                NotNullMap stats = new NotNullMap();
                stats.putDate("statsDate", beginDate);
                stats.put("totalCount", totalCount);
                stats.put("totalMoney", totalMoney);
                //获取分成比例
                String formatDate = DateFormatUtils.format(beginDate.getTime(), Constant.DATE_FORMAT);
                String suffix = IncomeRatioHistory.getSuffix(formatDate);
                stats.put("ratio", null);
                stats.put("fixedMoney", null);
                if (StringUtils.isNotEmpty(agentCompanyIncomeRatioHistoryService.exist(suffix))) {
                    AgentCompanyIncomeRatioHistory agentCompanyIncomeRatioHistory = agentCompanyIncomeRatioHistoryService.find(formatDate, agentCompany.getAgentId(), agentCompanyId, IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), suffix);
                    stats.put("ratio", agentCompanyIncomeRatioHistory.getRatio());
                    stats.put("fixedMoney", agentCompanyIncomeRatioHistory.getAgentCompanyFixedMoney());
                }
                Map data = new HashMap();
                data.put("list", list);
                data.put("stats", stats);

                listGroupByDay.add(data);
            }

            beginDate = DateUtils.addDays(beginDate, 1);
        }
        return listGroupByDay;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DayListParam {
        public String date;
    }

    /**
     * 34-按天查询收款记录
     */
    @ResponseBody
    @RequestMapping(value = "/day_list.htm")
    public RestResult list(@Valid @RequestBody DayListParam param) throws Exception{
        String agentCompanyId = getTokenData().agentCompanyId;
        AgentCompany agentCompany = agentCompanyService.find(agentCompanyId);
        String suffix = IncomeRatioHistory.getSuffix(param.date);


        Map stats = new HashMap();
        stats.put("ratio", null);
        stats.put("fixedMoney", null);
        if (StringUtils.isNotEmpty(agentCompanyIncomeRatioHistoryService.exist(suffix))) {
            AgentCompanyIncomeRatioHistory agentCompanyIncomeRatioHistory = agentCompanyIncomeRatioHistoryService.find(param.date, agentCompany.getAgentId(), agentCompanyId, IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), suffix);
            if (agentCompanyIncomeRatioHistory != null) {
                stats.put("ratio", agentCompanyIncomeRatioHistory.getRatio());
                stats.put("fixedMoney", agentCompanyIncomeRatioHistory.getAgentCompanyFixedMoney());
            }
        }
        stats.put("statsDate", param.date);

        Date date = DateUtils.parseDate(param.date, new String[]{"yyyy-MM-dd"});

        List list = new ArrayList();

        String suffix1 = BatteryOrderAllot.getSuffixByDate(date);
        String exist1 = batteryOrderAllotService.exist(suffix1);
        if (StringUtils.isNotEmpty(exist1)) {
            //先判断表中是否有agent_company_id字段
            String columnName = "agent_company_id";
            if (batteryOrderAllotService.contain(columnName, suffix1)) {
                List<BatteryOrderAllot> allotList = batteryOrderAllotService.findAgentCompanyDayIncome(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix1, param.date);
                for (BatteryOrderAllot allot : allotList) {
                    NotNullMap map = new NotNullMap();
                    map.putLong("id", allot.getId());
                    map.putString("fullname", allot.getCustomerName());
                    map.putMobileMask("mobile", allot.getCustomerMobile());
                    map.putDateTime("payTime", allot.getPayTime());
                    map.putInteger("orderMoney", allot.getOrderMoney());
                    map.putDouble("money", allot.getMoney());
                    map.putInteger("type", AgentCompany.Type.HDG_BATTERY_ORDER.getValue());
                    map.putString("statsDate", allot.getStatsDate());
                    list.add(map);
                }
            }
        }

        String suffix2 = PacketPeriodOrderAllot.getSuffixByDate(date);
        String exist2 = packetPeriodOrderAllotService.exist(suffix2);
        if (StringUtils.isNotEmpty(exist2)) {
            //先判断表中是否有agent_company_id字段
            String columnName = "agent_company_id";
            if (packetPeriodOrderAllotService.contain(columnName, suffix2)) {
                List<PacketPeriodOrderAllot> allotList = packetPeriodOrderAllotService.findAgentCompanyDayIncome(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix2, param.date);
                for (PacketPeriodOrderAllot allot : allotList) {
                    NotNullMap map = new NotNullMap();
                    map.putLong("id", allot.getId());
                    map.putString("fullname", allot.getCustomerName());
                    map.putMobileMask("mobile", allot.getCustomerMobile());
                    map.putDateTime("payTime", allot.getPayTime());
                    map.putInteger("orderMoney", allot.getOrderMoney());
                    map.putDouble("money", allot.getMoney());
                    map.putInteger("type", AgentCompany.Type.HDG_PACKET_PERIOD.getValue());
                    map.putString("statsDate", allot.getStatsDate());
                    list.add(map);
                }
            }
        }

        String suffix3 = RentPeriodOrderAllot.getSuffixByDate(date);
        String exist3 = rentPeriodOrderAllotService.exist(suffix3);
        if (StringUtils.isNotEmpty(exist3)) {
            //先判断表中是否有agent_company_id字段
            String columnName = "agent_company_id";
            if (rentPeriodOrderAllotService.contain(columnName, suffix3)) {
                List<RentPeriodOrderAllot> allotList = rentPeriodOrderAllotService.findAgentCompanyDayIncome(IncomeRatioHistory.OrgType.AGENT_COMPANY.getValue(), agentCompanyId, BatteryOrderAllot.ServiceType.INCOME.getValue(), suffix3, param.date);
                for (RentPeriodOrderAllot allot : allotList) {
                    NotNullMap map = new NotNullMap();
                    map.putLong("id", allot.getId());
                    map.putString("fullname", allot.getCustomerName());
                    map.putMobileMask("mobile", allot.getCustomerMobile());
                    map.putDateTime("payTime", allot.getPayTime());
                    map.putInteger("orderMoney", allot.getOrderMoney());
                    map.putDouble("money", allot.getMoney());
                    map.putInteger("type", AgentCompany.Type.ZD_RENT_PERIOD.getValue());
                    map.putString("statsDate", allot.getStatsDate());
                    list.add(map);
                }
            }
        }
        int totalCount = 0;
        int totalMoney = 0;
        for (Object o : list) {
            NotNullMap map = (NotNullMap) o;
            totalCount += 1;
            totalMoney += (Double) map.get("money");
        }
        stats.put("totalCount", totalCount);
        stats.put("totalMoney", totalMoney);

        Map map = new HashMap();
        map.put("stats", stats);
        map.put("list", list);
        return RestResult.dataResult(0, null, map);
    }

}
