package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.shop.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.AgentDayStatsController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;


/**
 * 门店日统计
 */
@Controller("agent_api_v1_shop_hdg_shop_day_stats")
@RequestMapping(value = "/agent_api/v1/shop/hdg/shop_day_stats")
public class ShopDayStatsController extends ApiController {
    @Autowired
    ShopDayStatsService shopDayStatsService;
    @Autowired
    ShopService shopService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;

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
        Shop shop = shopService.find(getTokenData().shopId);
        if (shop == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
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

        List<ShopDayStats> shopDayStatsList = shopDayStatsService.findDateRange(agentId, shop.getId(), beginDate1, beginDate2, param.category);
        Set<String> set = new TreeSet<String>();
        for (ShopDayStats data : shopDayStatsList){
            Date parse = DateUtils.parseDate(data.getStatsDate(), new String[]{Constant.DATE_FORMAT});
            String date1 = DateFormatUtils.format(parse, "yyyy-MM");
            set.add(date1);
        }
        List data = new ArrayList();

        Map line = new HashMap();
        List<Integer> inCustomerStatus = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
        int inCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, inCustomerStatus, beginTime, endTime);
        line.put("registerNum", inCount);

        for (String e : set){

            data.add(line);
            line.put("month", e);
            int totalMoney = 0;
            List dayList = new ArrayList();
            line.put("list", dayList);
            for (ShopDayStats stats : shopDayStatsList) {
                Map map = new HashMap();
                Date parse = DateUtils.parseDate(stats.getStatsDate(), new String[]{Constant.DATE_FORMAT});
                String day = DateFormatUtils.format(parse, "yyyy-MM");
                if (e.equals(day)){
                    map.put("day", stats.getStatsDate());
                    int money =  stats.getPacketPeriodMoney() + stats.getExchangeMoney();
                    int refundMoney = stats.getRefundPacketPeriodMoney();
                    map.put("money", money);
                    map.put("refundMoney", refundMoney);
                    totalMoney += money;
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
        Shop shop = shopService.find(getTokenData().shopId);
        if (shop == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"门店不存在",null);
        }
        Map data = new HashMap();
        if (StringUtils.isNotEmpty(param.day)){
            ShopDayStats shopDayStats = shopDayStatsService.find(agentId, shop.getId(),param.day, param.category);
            if (shopDayStats != null) {
                data.put("exchangeMoney", shopDayStats.getExchangeMoney());
                data.put("packetPeriodMoney", shopDayStats.getPacketPeriodMoney());
                data.put("refundPacketPeriodMoney", shopDayStats.getRefundPacketPeriodMoney());
            } else {
                data.put("exchangeMoney", 0);
                data.put("packetPeriodMoney", 0);
                data.put("refundPacketPeriodMoney", 0);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


}
