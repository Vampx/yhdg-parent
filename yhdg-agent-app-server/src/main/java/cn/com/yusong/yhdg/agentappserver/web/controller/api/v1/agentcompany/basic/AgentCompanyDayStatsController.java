package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agentcompany.basic;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyCustomerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyDayStatsService;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentCompanyService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopDayStatsService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompany;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyDayStats;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.hdg.ShopDayStats;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.*;


/**
 * 运营公司日统计
 */
@Controller("agent_api_v1_agentcompany_basic_agent_company_day_stats")
@RequestMapping(value = "/agent_api/v1/agentcompany/basic/agent_company_day_stats")
public class AgentCompanyDayStatsController extends ApiController {
    @Autowired
    AgentCompanyDayStatsService agentCompanyDayStatsService;
    @Autowired
    AgentCompanyService agentCompanyService;
    @Autowired
    AgentCompanyCustomerService agentCompanyCustomerService;
    @Autowired
    CustomerService customerService;

    public static class QueryCalendarParam {
        public int category;
        public String month;
    }

    @ResponseBody
    @RequestMapping(value = "/query_calendar.htm")
    public RestResult queryCalendar(@RequestBody QueryCalendarParam param) throws ParseException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户信息错误");
        }
        AgentCompany agentCompany = agentCompanyService.find(getTokenData().agentCompanyId);
        if (agentCompany == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营公司不存在",null);
        }
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(DateUtils.parseDate(param.month + "-01", new String[] {Constant.DATE_FORMAT}));
        calendar1.set(Calendar.DAY_OF_MONTH, 1);
        calendar1.add(Calendar.MONTH, 0);
        Date beginDate = calendar1.getTime();
        //本月开始结束
        Date beginTime = DateUtils.truncate(beginDate, Calendar.MONTH);
        Date endTime = DateUtils.addSeconds(DateUtils.addMonths(beginTime, 1), -1);
        String beginDate1 = DateFormatUtils.format(beginTime, Constant.DATE_FORMAT);
        String beginDate2 = DateFormatUtils.format(endTime, Constant.DATE_FORMAT);

        //当月新增注册人数
        Map line = new HashMap();
        List<AgentCompanyCustomer> agentCompanyCustomerList = agentCompanyCustomerService.findDateRange(agentId, agentCompany.getId(), beginTime, endTime);
        line.put("registerNum", agentCompanyCustomerList.size());

        List<AgentCompanyDayStats> agentCompanyDayStatsList = agentCompanyDayStatsService.findDateRange(agentId, agentCompany.getId(), beginDate1, beginDate2, param.category);
        Set<String> set = new TreeSet<String>();
        for (AgentCompanyDayStats data : agentCompanyDayStatsList){
            Date parse = DateUtils.parseDate(data.getStatsDate(), new String[]{Constant.DATE_FORMAT});
            String date1 = DateFormatUtils.format(parse, "yyyy-MM");
            set.add(date1);
        }
        List data = new ArrayList();

        for (String e : set){
            data.add(line);
            line.put("month", e);
            int totalMoney = 0;
            List dayList = new ArrayList();
            line.put("list", dayList);
            for (AgentCompanyDayStats stats : agentCompanyDayStatsList) {
                Map map = new HashMap();
                Date parse = DateUtils.parseDate(stats.getStatsDate(), new String[]{Constant.DATE_FORMAT});
                String day = DateFormatUtils.format(parse, "yyyy-MM");
                if (e.equals(day)){
                    map.put("day", stats.getStatsDate());
                    int money =  stats.getPacketPeriodMoney() + stats.getExchangeMoney();
                    int refundMoney = stats.getRefundPacketPeriodMoney();
                    map.put("money", money);
                    totalMoney += money;
                    map.put("refundMoney", refundMoney);
                    dayList.add(map);
                }
            }
            line.put("totalMoney", totalMoney);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    public static class QueryDayInfoParam {
        public int category;
        public String day;
    }

    @ResponseBody
    @RequestMapping(value = "/query_day_info.htm")
    public RestResult QueryDayInfo(@RequestBody QueryDayInfoParam param) {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户信息错误");
        }
        AgentCompany agentCompany = agentCompanyService.find(getTokenData().agentCompanyId);
        if (agentCompany == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营公司不存在",null);
        }
        Map data = new HashMap();
        if (StringUtils.isNotEmpty(param.day)){
            List<AgentCompanyDayStats> agentCompanyDayStatsList = agentCompanyDayStatsService.findList(agentId, agentCompany.getId(),param.day, param.category);
            if (agentCompanyDayStatsList.size() == 0) {
                data.put("exchangeMoney", 0);
                data.put("packetPeriodMoney", 0);
                data.put("refundPacketPeriodMoney", 0);
            } else {
                int exchangeMoney = 0;
                int packetPeriodMoney = 0;
                int refundPacketPeriodMoney = 0;
                for (AgentCompanyDayStats agentCompanyDayStats : agentCompanyDayStatsList) {
                    exchangeMoney += agentCompanyDayStats.getExchangeMoney();
                    packetPeriodMoney += agentCompanyDayStats.getPacketPeriodMoney();
                    refundPacketPeriodMoney += agentCompanyDayStats.getRefundPacketPeriodMoney();
                }
                data.put("exchangeMoney", exchangeMoney);
                data.put("packetPeriodMoney", packetPeriodMoney);
                data.put("refundPacketPeriodMoney", refundPacketPeriodMoney);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


}
