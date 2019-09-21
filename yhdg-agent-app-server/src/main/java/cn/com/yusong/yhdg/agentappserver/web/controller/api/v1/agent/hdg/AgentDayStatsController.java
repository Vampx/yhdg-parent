package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.zd.*;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;


/**
 * 运营商日统计
 */
@Controller("agent_api_v1_agent_hdg_agent_day_stats")
@RequestMapping(value = "/agent_api/v1/agent/hdg/agent_day_stats")
public class AgentDayStatsController extends ApiController {
    @Autowired
    CabinetDayStatsService cabinetDayStatsService;
    @Autowired
    CabinetMonthStatsService cabinetMonthStatsService;
    @Autowired
    CabinetTotalStatsService cabinetTotalStatsService;
    @Autowired
    AgentDayStatsService agentDayStatsService;
    @Autowired
    AgentTotalStatsService agentTotalStatsService;
    @Autowired
    AgentMonthStatsService agentMonthStatsService;
    @Autowired
    BatteryOrderAllotService batteryOrderAllotService;
    @Autowired
    PacketPeriodOrderAllotService packetPeriodOrderAllotService;
    @Autowired
    RentPeriodOrderAllotService rentPeriodOrderAllotService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    InsuranceOrderService insuranceOrderService;
    @Autowired
    RentInsuranceOrderService rentInsuranceOrderService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CabinetDayDegreeStatsService cabinetDayDegreeStatsService;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    RentOrderService rentOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    BalanceRecordService balanceRecordService;
    @Autowired
    AgentMaterialDayStatsService agentMaterialDayStatsService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    AgentService agentService;
    @Autowired
    ShopService shopService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    ShopDayStatsService shopDayStatsService;
    @Autowired
    ShopTotalStatsService shopTotalStatsService;
    @Autowired
    ShopStoreBatteryService shopStoreBatteryService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    /**
     * 57-查询运营商报表数据日历
     * */
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
        if (param.category == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
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

        List<BalanceRecord> balanceRecordList = balanceRecordService.findDateRange(agentId, BalanceRecord.BizType.AGENT.getValue(), beginDate1, beginDate2, param.category);
        Set<String> set = new TreeSet<String>();
        for (BalanceRecord data : balanceRecordList){
            Date parse = DateUtils.parseDate(data.getBalanceDate(), new String[]{Constant.DATE_FORMAT});
            String date1 = DateFormatUtils.format(parse, "yyyy-MM");
            set.add(date1);
        }
        List data = new ArrayList();

        for (String e : set){
            Map line = new HashMap();
            data.add(line);
            line.put("month", e);

            List dayList = new ArrayList();
            line.put("list", dayList);
            for (BalanceRecord stats : balanceRecordList) {
                Map map = new HashMap();
                Date parse = DateUtils.parseDate(stats.getBalanceDate(), new String[]{Constant.DATE_FORMAT});
                String day = DateFormatUtils.format(parse, "yyyy-MM");
                if (e.equals(day)){
                    map.put("day", stats.getBalanceDate());
                    map.put("money", stats.getMoney());
                    map.put("status", stats.getStatus());
                    dayList.add(map);
                }
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    /**
     * 58-查询运营商结算日数据
     *
     * */
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
        if (param.category == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
        }
        Map data = new HashMap();

        BalanceRecord balanceRecord = balanceRecordService.findByBalanceDate(agentId, param.day, param.category);
        if (balanceRecord != null) {
            //结算详情
            data.put("balanceRecordMoney", balanceRecordService.findTotalMoney(agentId, BalanceRecord.BizType.AGENT.getValue(), param.category).getTotalMoney());
            data.put("packetPeriodMoney", balanceRecord.getPacketPeriodMoney());
            data.put("refundPacketPeriodMoney", balanceRecord.getRefundPacketPeriodMoney());
            data.put("exchangeMoney", balanceRecord.getExchangeMoney());
            data.put("insuranceMoney", balanceRecord.getInsuranceMoney());
            data.put("refundInsuranceMoney", balanceRecord.getRefundInsuranceMoney());
            data.put("foregiftRemainMoney", balanceRecord.getForegiftRemainMoney());
            data.put("balanceMoney", balanceRecord.getMoney());
            data.put("deductionTicketMoney",balanceRecord.getDeductionTicketMoney());
        } else {
            data.put("balanceRecordMoney", 0);
            data.put("refundPacketPeriodMoney", 0);
            data.put("packetPeriodMoney", 0);
            data.put("exchangeMoney", 0);
            data.put("insuranceMoney", 0);
            data.put("refundInsuranceMoney", 0);
            data.put("foregiftRemainMoney", 0);
            data.put("balanceMoney", 0);
            data.put("deductionTicketMoney",0);
        }
        AgentDayStats agentDayStats = agentDayStatsService.find(agentId, param.day, param.category);
        if (agentDayStats != null) {
            AgentTotalStats agentTotalStats = agentTotalStatsService.find(agentId, param.category);
            //押金详情
            data.put("totalForegiftMoney", agentTotalStats.getForegiftMoney());
            data.put("totalRefundForegiftMoney", agentTotalStats.getForegiftRefundMoney());
            data.put("foregiftMoney", agentDayStats.getForegiftMoney());
            data.put("refundForegiftMoney", agentDayStats.getRefundPacketPeriodMoney());
            data.put("foregiftInOutMoney", agentDayStats.getForegiftMoney() - agentDayStats.getRefundPacketPeriodMoney());
        } else {
            data.put("totalForegiftMoney", 0);
            data.put("totalRefundForegiftMoney", 0);
            data.put("foregiftMoney", 0);
            data.put("refundForegiftMoney", 0);
            data.put("foregiftInOutMoney", 0);
        }
        AgentMaterialDayStats agentMaterialDayStats = agentMaterialDayStatsService.find(agentId, param.day, param.category);
        if (agentMaterialDayStats != null) {
            //支出详情
            data.put("totalInOutMoney", agentMaterialDayStatsService.findTotalMoney(agentId, param.category).getTotalMoney());
            data.put("cabinetForegiftMoney", agentMaterialDayStats.getCabinetForegiftMoney());
            data.put("cabinetRentMoney", agentMaterialDayStats.getCabinetRentMoney());
            data.put("batteryForegift", agentMaterialDayStats.getBatteryRentMoney());
            data.put("idCardAuthMoney", agentMaterialDayStats.getIdCardAuthMoney());
            data.put("inOutMoney", agentMaterialDayStats.getMoney());
        } else {
            data.put("totalInOutMoney", 0);
            data.put("cabinetForegiftMoney", 0);
            data.put("cabinetRentMoney", 0);
            data.put("batteryForegift", 0);
            data.put("idCardAuthMoney", 0);
            data.put("inOutMoney", 0);
        }
        //电费信息
        CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findForAgent(agentId, param.day, null);
        CabinetDayDegreeStats totalElectricPrice = cabinetDayDegreeStatsService.findForAgent(agentId, null, null);
        if(cabinetDayDegreeStats != null && totalElectricPrice != null){
            data.put("electricDegree", cabinetDayDegreeStats.getNum());
            data.put("electricPrice", cabinetDayDegreeStats.getPrice() * 100);
            data.put("totalElectricPrice", totalElectricPrice.getPrice() * 100);
        } else {
            data.put("electricDegree", 0);
            data.put("electricPrice", 0);
            data.put("totalElectricPrice", 0);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    public static class AgentInfoParam {
        public int category;
        public String cabinetId;
        public String shopId;
        public String day;
        public int type;
        public int offset;
        public int limit;
    }

    /**
     * 59-查询运营商结算明细
     * @param param
     */
    @ResponseBody
    @RequestMapping(value = "/agent_info.htm")
    public RestResult AgentInfo(@RequestBody AgentInfoParam param) throws ParseException {

        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        if (param.category == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
        }
        //2019-01-19 00:00:00
        Date date = DateUtils.parseDate(param.day, new String[]{Constant.DATE_FORMAT});
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        //2019-01-19 23:59:59
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);

        //日统计更新时间
        Date updateTime = null;
        AgentDayStats agentDayStats = agentDayStatsService.find(agentId, param.day, param.category);
        if (agentDayStats != null) {
            updateTime = agentDayStats.getUpdateTime();
        }

        List lines = new ArrayList();
        if (param.type == 1) {
            String suffix = BatteryOrderAllot.getSuffixByString(param.day);
            String exist = batteryOrderAllotService.exist(suffix);
            if (StringUtils.isNotEmpty(exist)){
                List<BatteryOrderAllot> allotList = batteryOrderAllotService.findByOrder(param.cabinetId, IncomeRatioHistory.OrgType.AGENT.getValue(), agentId, BatteryOrderAllot.ServiceType.INCOME.getValue(), param.day, suffix, param.offset, param.limit);
                for (BatteryOrderAllot day : allotList) {
                    Map map = new HashMap();
                    map.put("cabinetId", day.getCabinetId());
                    map.put("cabinetName", day.getCabinetName());
                    map.put("orderMoney", day.getOrderMoney());
                    map.put("money", day.getMoney());
                    map.put("customerName", day.getCustomerName());
                    map.put("mobile", day.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    lines.add(map);
                }
            }
        }

        if (param.type == 2) {//押金
            if (param.category == ConstEnum.Category.EXCHANGE.getValue()) {
                List<CustomerForegiftOrder> orderList = customerForegiftOrderService.findIncrement(agentId, CustomerForegiftOrder.Status.PAY_OK.getValue(), beginTime, updateTime != null ? updateTime : endTime, param.offset, param.limit);
                for (CustomerForegiftOrder order : orderList) {
                    Map map = new HashMap();
                    map.put("customerName", order.getCustomerFullname());
                    map.put("mobile", order.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    map.put("money", order.getMoney());
                    map.put("payTime",DateFormatUtils.format(order.getPayTime(), Constant.DATE_TIME_FORMAT) );
                    lines.add(map);
                }
            } else {
                List<RentForegiftOrder> orderList = rentForegiftOrderService.findIncrement(agentId, RentForegiftOrder.Status.PAY_OK.getValue(), beginTime, updateTime != null ? updateTime : endTime, param.offset, param.limit);
                for (RentForegiftOrder order : orderList) {
                    Map map = new HashMap();
                    map.put("customerName", order.getCustomerFullname());
                    map.put("mobile", order.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    map.put("money", order.getMoney());
                    map.put("payTime",DateFormatUtils.format(order.getPayTime(), Constant.DATE_TIME_FORMAT) );
                    lines.add(map);
                }
            }

        }

        if (param.type == 3) {//包时段

            if (param.category == ConstEnum.Category.EXCHANGE.getValue()) {
                String suffix = PacketPeriodOrderAllot.getSuffixByString(param.day);
                String exist = packetPeriodOrderAllotService.exist(suffix);
                if (StringUtils.isNotEmpty(exist)){
                    List<PacketPeriodOrderAllot> allotList = packetPeriodOrderAllotService.findByOrder(param.cabinetId, IncomeRatioHistory.OrgType.AGENT.getValue(), agentId, PacketPeriodOrderAllot.ServiceType.INCOME.getValue(), param.day, suffix, param.offset, param.limit);
                    for (PacketPeriodOrderAllot stats : allotList) {
                        Map map = new HashMap();
                        map.put("customerName", stats.getCustomerName());
                        map.put("mobile", stats.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                        map.put("dayCount", stats.getDayCount());
                        if (stats.getBeginTime() != null && stats.getEndTime() != null) {
                            map.put("beginTime", DateFormatUtils.format(stats.getBeginTime(), Constant.DATE_FORMAT));
                            map.put("endTime", DateFormatUtils.format(stats.getEndTime(), Constant.DATE_FORMAT));
                        } else {
                            map.put("beginTime", null);
                            map.put("endTime", null);
                        }
                        map.put("payTime",DateFormatUtils.format(stats.getPayTime(), Constant.DATE_TIME_FORMAT) );
                        map.put("orderMoney", stats.getOrderMoney());
                        map.put("money", stats.getMoney());
                        lines.add(map);
                    }
                }
            } else {
                String suffix = RentPeriodOrderAllot.getSuffixByString(param.day);
                String exist = rentPeriodOrderAllotService.exist(suffix);
                if (StringUtils.isNotEmpty(exist)){
                    List<RentPeriodOrderAllot> rentAllotList = rentPeriodOrderAllotService.findByOrder(param.shopId, IncomeRatioHistory.OrgType.AGENT.getValue(), agentId, RentPeriodOrderAllot.ServiceType.INCOME.getValue(), param.day, suffix, param.offset, param.limit);
                    for (RentPeriodOrderAllot stats : rentAllotList) {
                        Map map = new HashMap();
                        map.put("customerName", stats.getCustomerName());
                        map.put("mobile", stats.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                        map.put("dayCount", stats.getDayCount());
                        if (stats.getBeginTime() != null && stats.getEndTime() != null) {
                            map.put("beginTime", DateFormatUtils.format(stats.getBeginTime(), Constant.DATE_FORMAT));
                            map.put("endTime", DateFormatUtils.format(stats.getEndTime(), Constant.DATE_FORMAT));
                        } else {
                            map.put("beginTime", null);
                            map.put("endTime", null);
                        }
                        map.put("payTime",DateFormatUtils.format(stats.getPayTime(), Constant.DATE_TIME_FORMAT) );
                        map.put("orderMoney", stats.getOrderMoney());
                        map.put("money", stats.getMoney());
                        lines.add(map);
                    }
                }
            }

        }

        if (param.type == 4) {//按次退款
            String suffix = BatteryOrderAllot.getSuffixByString(param.day);
            String exist = batteryOrderAllotService.exist(suffix);
            if (StringUtils.isNotEmpty(exist)){
                List<BatteryOrderAllot> allotList = batteryOrderAllotService.findByOrder(param.cabinetId, IncomeRatioHistory.OrgType.AGENT.getValue(), agentId, BatteryOrderAllot.ServiceType.REFUND.getValue(), param.day, suffix, param.offset, param.limit);
                for (BatteryOrderAllot day : allotList) {
                    Map map = new HashMap();
                    map.put("cabinetId", day.getCabinetId());
                    map.put("cabinetName", day.getCabinetName());
                    map.put("customerName", day.getCustomerName());
                    map.put("mobile", day.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    map.put("money", day.getMoney());
                    lines.add(map);
                }
            }
        }

        if (param.type == 5) {//押金退款
            if (param.category == ConstEnum.Category.EXCHANGE.getValue()) {
                List<CustomerForegiftOrder> orderList = customerForegiftOrderService.findRefund(agentId, CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue(), beginTime, updateTime != null ? updateTime : endTime, param.offset, param.limit);
                for (CustomerForegiftOrder order : orderList) {
                    Map map = new HashMap();
                    map.put("customerName", order.getCustomerFullname());
                    map.put("mobile", order.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    map.put("money", order.getMoney());
                    map.put("refundTime",DateFormatUtils.format(order.getRefundTime(), Constant.DATE_TIME_FORMAT) );
                    lines.add(map);
                }
            } else {
                List<RentForegiftOrder> rentOrderList = rentForegiftOrderService.findRefund(agentId, RentForegiftOrder.Status.REFUND_SUCCESS.getValue(), beginTime, updateTime != null ? updateTime : endTime, param.offset, param.limit);
                for (RentForegiftOrder order : rentOrderList) {
                    Map map = new HashMap();
                    map.put("customerName", order.getCustomerFullname());
                    map.put("mobile", order.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    map.put("money", order.getMoney());
                    map.put("refundTime",DateFormatUtils.format(order.getRefundTime(), Constant.DATE_TIME_FORMAT) );
                    lines.add(map);
                }
            }

        }

        if (param.type == 6) {
            if (param.category == ConstEnum.Category.EXCHANGE.getValue()) {
                String suffix = PacketPeriodOrderAllot.getSuffixByString(param.day);
                String exist = packetPeriodOrderAllotService.exist(suffix);
                if (StringUtils.isNotEmpty(exist)){
                    List<PacketPeriodOrderAllot> allotList = packetPeriodOrderAllotService.findByOrder(param.cabinetId, IncomeRatioHistory.OrgType.AGENT.getValue(), agentId, PacketPeriodOrderAllot.ServiceType.REFUND.getValue(), param.day, suffix, param.offset, param.limit);
                    for (PacketPeriodOrderAllot stats : allotList) {
                        Map map = new HashMap();
                        map.put("customerName", stats.getCustomerName());
                        map.put("mobile", stats.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                        map.put("dayCount", stats.getDayCount());
                        if (stats.getBeginTime() != null && stats.getEndTime() != null) {
                            map.put("beginTime", DateFormatUtils.format(stats.getBeginTime(), Constant.DATE_FORMAT));
                            map.put("endTime", DateFormatUtils.format(stats.getEndTime(), Constant.DATE_FORMAT));
                        } else {
                            map.put("beginTime", null);
                            map.put("endTime", null);
                        }
                        //退款时间
                        PacketPeriodOrder packetPeriodOrder =  packetPeriodOrderService.find(stats.getOrderId());
                        if(packetPeriodOrder != null && packetPeriodOrder.getRefundTime() != null){
                            map.put("refundTime",DateFormatUtils.format(packetPeriodOrder.getRefundTime(), Constant.DATE_TIME_FORMAT));
                        }
                        map.put("refundMoney", stats.getMoney());
                        map.put("money", stats.getOrderMoney());
                        lines.add(map);
                    }
                }
            } else {
                String suffix = RentPeriodOrderAllot.getSuffixByString(param.day);
                String exist = rentPeriodOrderAllotService.exist(suffix);
                if (StringUtils.isNotEmpty(exist)){
                    List<RentPeriodOrderAllot> rentAllotList = rentPeriodOrderAllotService.findByOrder(param.cabinetId, IncomeRatioHistory.OrgType.AGENT.getValue(), agentId, RentPeriodOrderAllot.ServiceType.REFUND.getValue(), param.day, suffix, param.offset, param.limit);
                    for (RentPeriodOrderAllot stats : rentAllotList) {
                        Map map = new HashMap();
                        map.put("customerName", stats.getCustomerName());
                        map.put("mobile", stats.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                        map.put("dayCount", stats.getDayCount());
                        if (stats.getBeginTime() != null && stats.getEndTime() != null) {
                            map.put("beginTime", DateFormatUtils.format(stats.getBeginTime(), Constant.DATE_FORMAT));
                            map.put("endTime", DateFormatUtils.format(stats.getEndTime(), Constant.DATE_FORMAT));
                        } else {
                            map.put("beginTime", null);
                            map.put("endTime", null);
                        }
                        //退款时间
                        RentPeriodOrder rentPeriodOrder =  rentPeriodOrderService.find(stats.getOrderId());
                        if(rentPeriodOrder != null && rentPeriodOrder.getRefundTime() != null){
                            map.put("refundTime",DateFormatUtils.format(rentPeriodOrder.getRefundTime(), Constant.DATE_TIME_FORMAT));
                        }
                        map.put("refundMoney", stats.getMoney());
                        map.put("money", stats.getOrderMoney());
                        lines.add(map);
                    }
                }
            }

        }


        if (param.type == 7) {
            if (param.category == ConstEnum.Category.EXCHANGE.getValue()) {
                List<InsuranceOrder> orderList = insuranceOrderService.findIncrement(agentId, InsuranceOrder.Status.PAID.getValue(), beginTime, updateTime != null ? updateTime : endTime, param.offset, param.limit);
                for (InsuranceOrder order : orderList) {
                    Map map = new HashMap();
                    map.put("customerName", order.getCustomerFullname());
                    map.put("mobile", order.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    if (order.getBeginTime() != null && order.getEndTime() != null) {
                        map.put("beginTime", DateFormatUtils.format(order.getBeginTime(), Constant.DATE_FORMAT));
                        map.put("endTime", DateFormatUtils.format(order.getEndTime(), Constant.DATE_FORMAT));
                    } else {
                        map.put("beginTime", null);
                        map.put("endTime", null);
                    }
                    map.put("money", order.getMoney());
                    map.put("payTime",DateFormatUtils.format(order.getPayTime(), Constant.DATE_TIME_FORMAT) );
                    lines.add(map);
                }
            } else {
                List<RentInsuranceOrder> orderList = rentInsuranceOrderService.findIncrement(agentId, RentInsuranceOrder.Status.PAID.getValue(), beginTime, updateTime != null ? updateTime : endTime, param.offset, param.limit);
                for (RentInsuranceOrder rentInsuranceOrder : orderList) {
                    Map map = new HashMap();
                    map.put("customerName", rentInsuranceOrder.getCustomerFullname());
                    map.put("mobile", rentInsuranceOrder.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    if (rentInsuranceOrder.getBeginTime() != null && rentInsuranceOrder.getEndTime() != null) {
                        map.put("beginTime", DateFormatUtils.format(rentInsuranceOrder.getBeginTime(), Constant.DATE_FORMAT));
                        map.put("endTime", DateFormatUtils.format(rentInsuranceOrder.getEndTime(), Constant.DATE_FORMAT));
                    } else {
                        map.put("beginTime", null);
                        map.put("endTime", null);
                    }
                    map.put("money", rentInsuranceOrder.getMoney());
                    map.put("payTime",DateFormatUtils.format(rentInsuranceOrder.getPayTime(), Constant.DATE_TIME_FORMAT) );
                    lines.add(map);
                }
            }

        }

        if (param.type == 8) {//保险退款
            if (param.category == ConstEnum.Category.EXCHANGE.getValue()) {
                List<InsuranceOrder> orderList = insuranceOrderService.findRefund(agentId, InsuranceOrder.Status.REFUND_SUCCESS.getValue(), beginTime, updateTime != null ? updateTime : endTime, param.offset, param.limit);
                for (InsuranceOrder order : orderList) {
                    Map map = new HashMap();
                    map.put("customerName", order.getCustomerFullname());
                    map.put("mobile", order.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    if (order.getBeginTime() != null && order.getEndTime() != null) {
                        map.put("beginTime", DateFormatUtils.format(order.getBeginTime(), Constant.DATE_FORMAT));
                        map.put("endTime", DateFormatUtils.format(order.getEndTime(), Constant.DATE_FORMAT));
                    } else {
                        map.put("beginTime", null);
                        map.put("endTime", null);
                    }
                    map.put("money", order.getMoney());
                    map.put("refundMoney", order.getRefundMoney());
                    if(order.getRefundTime() != null){
                        map.put("refundTime",DateFormatUtils.format(order.getRefundTime(), Constant.DATE_TIME_FORMAT) );
                    }
                    lines.add(map);
                }
            } else {
                List<RentInsuranceOrder> orderList = rentInsuranceOrderService.findRefund(agentId, RentInsuranceOrder.Status.REFUND_SUCCESS.getValue(), beginTime, updateTime != null ? updateTime : endTime, param.offset, param.limit);
                for (RentInsuranceOrder rentInsuranceOrder : orderList) {
                    Map map = new HashMap();
                    map.put("customerName", rentInsuranceOrder.getCustomerFullname());
                    map.put("mobile", rentInsuranceOrder.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                    if (rentInsuranceOrder.getBeginTime() != null && rentInsuranceOrder.getEndTime() != null) {
                        map.put("beginTime", DateFormatUtils.format(rentInsuranceOrder.getBeginTime(), Constant.DATE_FORMAT));
                        map.put("endTime", DateFormatUtils.format(rentInsuranceOrder.getEndTime(), Constant.DATE_FORMAT));
                    } else {
                        map.put("beginTime", null);
                        map.put("endTime", null);
                    }
                    map.put("money", rentInsuranceOrder.getMoney());
                    map.put("refundMoney", rentInsuranceOrder.getRefundMoney());
                    if(rentInsuranceOrder.getRefundTime() != null){
                        map.put("refundTime",DateFormatUtils.format(rentInsuranceOrder.getRefundTime(), Constant.DATE_TIME_FORMAT) );
                    }
                    lines.add(map);
                }
            }

        }

        if (param.type == 9) {//电费
            List<CabinetDayDegreeStats> orderList = cabinetDayDegreeStatsService.findForStats(agentId, param.day, param.offset, param.limit);
            for (CabinetDayDegreeStats order : orderList) {
                Map map = new HashMap();

                map.put("cabinetId", order.getCabinetId());
                map.put("cabinetName", order.getCabinetName());
                map.put("electricDegree", order.getNum());
                map.put("unitPrice", order.getPrice() * 100 );
                map.put("electricPrice", order.getNum() * order.getPrice() * 100 );
                lines.add(map);
            }
        }


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, lines);
    }

    public static class QueryAgentDayStatsParam {
        public int category;
        @NotBlank(message = "开始日期不能为空")
        public String queryBeginDate;
        @NotBlank(message = "结束日期不能为空")
        public String queryEndDate;
    }

    /**
     * 90-查询运营商经营报表(日周月)
     */
    @ResponseBody
    @RequestMapping("/query_agent_day_stats.htm")
    public RestResult queryAgentDayStats(@RequestBody QueryAgentDayStatsParam param) throws IOException, ParseException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        if (param.category == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
        }
        int ratio = 0;
        String systemRatio = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId());
        if(StringUtils.isNotEmpty(systemRatio)){
            ratio = Integer.parseInt(systemRatio);
        }

        NotNullMap data = new NotNullMap();

        int hdInCustomerCount = 0,hdOutCustomerCount = 0,totalHdInCustomerCount = 0,totalHdOutCustomerCount = 0;
        int zdInCustomerCount = 0,zdOutCustomerCount = 0,totalZdInCustomerCount = 0,totalZdOutCustomerCount = 0;

        //运营商期间统计
        AgentDayStats agentDayStats = agentDayStatsService.findAgentDayTotal(agentId, param.queryBeginDate, param.queryEndDate, param.category);
        //运营商总统计
        AgentTotalStats agentTotalStats = agentTotalStatsService.find(agentId, param.category);
        //平台待支付费用
        AgentMaterialDayStats agentMaterialDayStats = agentMaterialDayStatsService.findTotalRentMoney(agentId, param.queryBeginDate, param.queryEndDate, param.category);
        //结算
        BalanceRecord balanceRecord = balanceRecordService.findTotalDateRange(agentId, BalanceRecord.BizType.AGENT.getValue(), param.queryBeginDate, param.queryEndDate, param.category);

        //换电
        if(param.category == ConstEnum.Category.EXCHANGE.getValue()){
            //查询总押金数 （指定时间总注册人数）
            hdInCustomerCount = agentDayStats.getForegiftCount();
            //查询总退款押金（指定时间总退网人数）
            hdOutCustomerCount = agentDayStats.getForegiftRefundCount();
            //总注册
            totalHdInCustomerCount = agentTotalStats.getForegiftCount();
            //总退网
            totalHdOutCustomerCount = agentTotalStats.getForegiftRefundCount();

            int foregiftBalanceRatio = 0;
            if (agent.getForegiftBalanceRatio() != null) {
                foregiftBalanceRatio = agent.getForegiftBalanceRatio();
            }

            data.put("foregiftCustomerCount", hdInCustomerCount);
            data.put("inCustomerCount", hdInCustomerCount );//已交 在网
            data.put("outCustomerCount", hdOutCustomerCount);//已退 退网
            data.put("hdTotalCustomerCount", totalHdInCustomerCount);
            data.put("totalOutCustomerCount", totalHdOutCustomerCount);//换电总退网

            data.put("hdRemainMoney", agent.getForegiftRemainMoney());//押金池余额
            if (foregiftBalanceRatio > ratio) {
                data.put("remainStatus", ConstEnum.Flag.TRUE.getValue());//押金池余额状态
            } else {
                data.put("remainStatus", ConstEnum.Flag.FALSE.getValue());
            }

            //押金
            //收入
            data.put("foregiftMoney", agentDayStats.getForegiftMoney());
            data.put("totalForegiftMoney", agentTotalStats.getForegiftMoney());
            //支出
            data.put("foregiftRefundMoney", agentDayStats.getForegiftRefundMoney());
            data.put("totalForegiftRefundMoney", agentTotalStats.getForegiftRefundMoney());

            //换电租金
            //收入
            data.put("packetPeriodMoney", agentDayStats.getPacketPeriodMoney());
            data.put("totalPacketPeriodMoney", agentTotalStats.getPacketPeriodMoney());
            //支出
            data.put("refundPacketPeriodMoney", agentDayStats.getRefundPacketPeriodMoney());
            data.put("totalRefundPacketPeriodMoney", agentTotalStats.getRefundPacketPeriodMoney());

            //保险
            //收入
            data.put("insuranceMoney", agentDayStats.getInsuranceMoney());
            data.put("totalInsuranceMoney", agentTotalStats.getInsuranceMoney());
            //支出
            data.put("insuranceRefundMoney", agentDayStats.getInsuranceRefundMoney());
            data.put("totalRefundInsuranceMoney", agentTotalStats.getInsuranceRefundMoney());

            if (agentMaterialDayStats != null) {
                //支出详情
                data.put("cabinetRentMoney", agentMaterialDayStats.getCabinetRentMoney());
                data.put("idCardAuthMoney", agentMaterialDayStats.getIdCardAuthMoney());
                data.put("batteryRentMoney", agentMaterialDayStats.getBatteryRentMoney());
            } else {
                data.put("cabinetRentMoney",0);
                data.put("idCardAuthMoney", 0);
                data.put("batteryRentMoney",0);
            }

            if (balanceRecord != null) {
                //退押金收入
                data.put("foregiftRemainMoney", balanceRecord.getForegiftRemainMoney());
            } else {
                data.put("foregiftRemainMoney", 0);
            }

            //换电人次
            data.put("orderCount", agentDayStats.getOrderCount());
            data.put("activeCount", agentDayStats.getActiveCustomerCount());
            //电费
            data.put("electricPrice", agentDayStats.getElectricPrice());
            data.put("totalElectricPrice", agentTotalStats.getElectricPrice());

            data.put("electricDegree", agentDayStats.getElectricDegree());
            data.put("totalElectricDegree", agentTotalStats.getElectricDegree());

            if(agentDayStats.getActiveCustomerCount() != 0){
                data.put("referencePrice",  agentDayStats.getElectricPrice()/agentDayStats.getActiveCustomerCount());
            } else {
                data.put("referencePrice", 0);
            }
            data.put("totalReferencePrice",  agentTotalStats.getPerElectric());

            data.put("cabinetCount", agentDayStats.getCabinetCount());
            data.put("totalCabinetCount", agentTotalStats.getCabinetCount());
            data.put("hdTotalOrderCount", agentTotalStats.getOrderCount());
            data.put("hdTotalBatteryCount", agentTotalStats.getBatteryCount());
        }
        //租电
        else if(param.category == ConstEnum.Category.RENT.getValue()){
            //查询总押金数 （指定时间总注册人数）
            zdInCustomerCount = agentDayStats.getForegiftCount();
            //查询总退款押金（指定时间总退网人数）
            zdOutCustomerCount = agentDayStats.getForegiftRefundCount();
            //总注册
            totalZdInCustomerCount = agentTotalStats.getForegiftCount();
            //总退网
            totalZdOutCustomerCount = agentTotalStats.getForegiftRefundCount();

            int zdForegiftBalanceRatio = 0;
            if (agent.getZdForegiftBalanceRatio() != null) {
                zdForegiftBalanceRatio = agent.getZdForegiftBalanceRatio();
            }

            data.put("foregiftCustomerCount", zdInCustomerCount);
            data.put("inCustomerCount", zdInCustomerCount);//已交 在网
            data.put("outCustomerCount", zdOutCustomerCount);//已退 退网

            List<Integer> inCustomerStatus = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
            List<Integer> outCustomerCountStatus = Arrays.asList(RentForegiftOrder.Status.REFUND_SUCCESS.getValue());

            int inCount = rentForegiftOrderService.findCountByShopId(null, agentId, inCustomerStatus, null, null);
            int outCount = rentForegiftOrderService.findCountByShopId(null, agentId, outCustomerCountStatus, null, null);
            int totalInCustomerCount = inCount;
            int totalOutCustomerCount = outCount;

            data.put("zdTotalCustomerCount", totalInCustomerCount + totalOutCustomerCount);
            data.put("totalOutCustomerCount", totalZdOutCustomerCount);//租电总退网

            data.put("zdRemainMoney", agent.getZdForegiftRemainMoney());//押金池余额
            if (zdForegiftBalanceRatio > ratio) {
                data.put("remainStatus", ConstEnum.Flag.TRUE.getValue());//押金池余额状态
            } else {
                data.put("remainStatus", ConstEnum.Flag.FALSE.getValue());
            }

            //押金
            //收入
            data.put("foregiftMoney", agentDayStats.getForegiftMoney());
            data.put("totalForegiftMoney", agentTotalStats.getForegiftMoney());
            //支出
            data.put("foregiftRefundMoney", agentDayStats.getForegiftRefundMoney());
            data.put("totalForegiftRefundMoney", agentTotalStats.getForegiftRefundMoney());


            //租金
            //收入
            data.put("packetPeriodMoney", agentDayStats.getPacketPeriodMoney());
            data.put("totalPacketPeriodMoney", agentTotalStats.getPacketPeriodMoney());
            //支出
            data.put("refundPacketPeriodMoney", agentDayStats.getRefundPacketPeriodMoney());
            data.put("totalRefundPacketPeriodMoney", agentTotalStats.getRefundPacketPeriodMoney());

            //保险
            //收入
            data.put("insuranceMoney", agentDayStats.getInsuranceMoney());
            data.put("totalInsuranceMoney", agentTotalStats.getInsuranceMoney());
            //支出
            data.put("insuranceRefundMoney", agentDayStats.getInsuranceRefundMoney());
            data.put("totalRefundInsuranceMoney", agentTotalStats.getInsuranceRefundMoney());


            if (agentMaterialDayStats != null) {
                //支出详情
                data.put("cabinetRentMoney", agentMaterialDayStats.getCabinetRentMoney());
                data.put("idCardAuthMoney", agentMaterialDayStats.getIdCardAuthMoney());
                data.put("batteryRentMoney", agentMaterialDayStats.getBatteryRentMoney());
            } else {
                data.put("cabinetRentMoney",0);
                data.put("idCardAuthMoney", 0);
                data.put("batteryRentMoney",0);
            }

            if (balanceRecord != null) {
                //退押金收入
                data.put("foregiftRemainMoney", balanceRecord.getForegiftRemainMoney());
            } else {
                data.put("foregiftRemainMoney", 0);
            }

            int shopCount = shopService.findShopCount(agentId);
            data.put("shopCount", shopCount);
            data.put("totalShopCount", shopCount);
            data.put("zdTotalOrderCount", agentTotalStats.getPacketPeriodOrderCount());
            data.put("zdTotalBatteryCount", agentTotalStats.getBatteryCount());
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);

    }

    public static class QueryAgentCabinetStatsParam {
        public String keyword;
        @NotBlank(message = "开始日期不能为空")
        public String queryBeginDate;
        @NotBlank(message = "结束日期不能为空")
        public String queryEndDate;
        public int offset;
        public int limit;
    }

    /**
     * 91-查询运营商经营报表按设备(日周月)
     */
    @ResponseBody
    @RequestMapping("/query_agent_cabinet_stats.htm")
    public RestResult queryAgentCabinetStats(@RequestBody QueryAgentCabinetStatsParam param) throws IOException, ParseException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        List<CabinetDayStats> cabinetDayStats = cabinetDayStatsService.findTotalCabinetStatsList(
                agentId, param.queryBeginDate, param.queryEndDate, param.keyword, param.offset, param.limit);

        List<NotNullMap> list = new ArrayList<NotNullMap>();


        for (CabinetDayStats stats : cabinetDayStats) {
            NotNullMap notNullMap = new NotNullMap();

            Cabinet cabinet = cabinetService.find(stats.getCabinetId());
            CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.find(stats.getCabinetId(), agentId);

            if(cabinet.getUpLineTime() != null){
                notNullMap.put("upLineTime",DateFormatUtils.format( cabinet.getUpLineTime(), Constant.DATE_TIME_FORMAT));
            }else{
                notNullMap.put("upLineTime","暂无时间");
            }

            notNullMap.put("cabinetId", stats.getCabinetId());
            notNullMap.put("cabinetName", cabinet.getCabinetName());

            //押金
            //收入
            notNullMap.put("foregiftMoney", stats.getForegiftMoney());
            notNullMap.put("totalForegiftMoney", cabinetTotalStats.getForegiftMoney());
            //支出
            notNullMap.put("foregiftRefundMoney", stats.getRefundForegiftMoney());
            notNullMap.put("totalForegiftRefundMoney", cabinetTotalStats.getRefundForegiftMoney());
            //租金
            //收入
            notNullMap.put("packetPeriodMoney", stats.getPacketPeriodMoney());
            notNullMap.put("totalPacketPeriodMoney", cabinetTotalStats.getPacketPeriodMoney());
            //支出
            notNullMap.put("refundPacketPeriodMoney", stats.getRefundPacketPeriodMoney());
            notNullMap.put("totalRefundPacketPeriodMoney", cabinetTotalStats.getRefundPacketPeriodMoney());
            //保险
            //收入
            notNullMap.put("insuranceMoney", stats.getInsuranceMoney());
            notNullMap.put("totalInsuranceMoney", cabinetTotalStats.getInsuranceMoney());
            //支出
            notNullMap.put("insuranceRefundMoney", stats.getRefundInsuranceMoney());
            notNullMap.put("totalRefundInsuranceMoney", cabinetTotalStats.getRefundInsuranceMoney());
            //换电人次
            notNullMap.put("activeCustomerCount", stats.getActiveCustomerCount());
            notNullMap.put("orderCount", stats.getOrderCount());

            //设备
            notNullMap.put("rentMoney", cabinet.getRentMoney());
            notNullMap.put("rentPeriodType", cabinet.getRentPeriodType());
            if (cabinet.getRentExpireTime() != null) {
                notNullMap.put("rentExpireTime", DateFormatUtils.format(cabinet.getRentExpireTime(), Constant.DATE_FORMAT));
            } else {
                notNullMap.put("rentExpireTime", "");
            }

            List<Integer> inCustomerStatus = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
            List<Integer> outCustomerCountStatus = Arrays.asList(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue());

            int inCount = customerForegiftOrderService.findCountByCabinetId(cabinet.getId(), agentId, inCustomerStatus, null, null);
            int outCount = customerForegiftOrderService.findCountByCabinetId(cabinet.getId(), agentId, outCustomerCountStatus, null, null);

            Date beginTime = DateUtils.parseDate(param.queryBeginDate + " 00:00:00", new String[]{Constant.DATE_TIME_FORMAT});
            Date endTime = DateUtils.parseDate(param.queryEndDate + " 23:59:59", new String[]{Constant.DATE_TIME_FORMAT});

            //用户
            int foregiftCount = customerForegiftOrderService.findCountByCabinetId(cabinet.getId(), agentId, inCustomerStatus, beginTime, endTime);

            notNullMap.put("foregiftCustomerCount", foregiftCount);
            notNullMap.put("inCustomerCount", inCount);//已交 在网
            notNullMap.put("outCustomerCount", outCount);//已退 退网
            notNullMap.put("totalCustomerCount", inCount + outCount);
            //电费
            notNullMap.put("unitPrice", cabinet.getPrice() * 100);
            notNullMap.put("electricDegree", stats.getElectricDegree());
            notNullMap.put("totalElectricDegree", cabinetTotalStats.getElectricDegree());
            notNullMap.put("electricPrice", stats.getElectricPrice());
            notNullMap.put("totalElectricPrice", cabinetTotalStats.getElectricPrice());

            if(stats.getActiveCustomerCount() != 0){
                notNullMap.put("referencePrice",  stats.getElectricPrice()/stats.getActiveCustomerCount());
            } else {
                notNullMap.put("referencePrice", 0);
            }
            notNullMap.put("totalReferencePrice", cabinetTotalStats.getPerElectric());
            //电池
            int exchangeCount = cabinetBoxService.statsCompleteChargeCount(cabinet.getId(), Battery.Status.IN_BOX.getValue());
            int lockCount = cabinetBoxService.statsBoxCountByStatus(cabinet.getId());
            int batteryCount = exchangeCount + lockCount + cabinet.getChargeBatteryNum();
            notNullMap.put("exchangeCount", exchangeCount);
            notNullMap.put("chargeCount", cabinet.getChargeBatteryNum());
            notNullMap.put("lockCount", lockCount);
            notNullMap.put("batteryCount", batteryCount);

            list.add(notNullMap);
        }

        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    public static class QueryAgentCabinetDetailsStatsParam {
        public String cabinetId;
        @NotBlank(message = "开始日期不能为空")
        public String queryBeginDate;
        @NotBlank(message = "结束日期不能为空")
        public String queryEndDate;
    }

    /**
     * 95-查询运营商经营报表按设备明细(日周月)
     */
    @ResponseBody
    @RequestMapping("/query_agent_cabinet_details_stats.htm")
    public RestResult queryAgentCabinetDetailsStats(@RequestBody QueryAgentCabinetDetailsStatsParam param) throws IOException, ParseException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        CabinetDayStats stats = cabinetDayStatsService.findTotalStatsListByCabinetId(
                agentId,param.cabinetId, param.queryBeginDate, param.queryEndDate);

        NotNullMap notNullMap = new NotNullMap();

        Cabinet cabinet = cabinetService.find(param.cabinetId);
        CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.find(param.cabinetId, agentId);

        notNullMap.put("cabinetId", cabinet.getId());
        notNullMap.put("cabinetName", cabinet.getCabinetName() != null ? cabinet.getCabinetName() : "");

        if (cabinet.getUpLineTime() != null) {
            notNullMap.putDateTime("upLineTime", cabinet.getUpLineTime());
        } else {
            notNullMap.put("upLineTime", "");
        }

        //押金
        //收入
        notNullMap.put("foregiftMoney", stats.getForegiftMoney());
        notNullMap.put("totalForegiftMoney", cabinetTotalStats.getForegiftMoney());
        //支出
        notNullMap.put("foregiftRefundMoney", stats.getRefundForegiftMoney());
        notNullMap.put("totalForegiftRefundMoney", cabinetTotalStats.getRefundForegiftMoney());
        //租金
        //收入
        notNullMap.put("packetPeriodMoney", stats.getPacketPeriodMoney());
        notNullMap.put("totalPacketPeriodMoney", cabinetTotalStats.getPacketPeriodMoney());
        //支出
        notNullMap.put("refundPacketPeriodMoney", stats.getRefundPacketPeriodMoney());
        notNullMap.put("totalRefundPacketPeriodMoney", cabinetTotalStats.getRefundPacketPeriodMoney());
        //保险
        //收入
        notNullMap.put("insuranceMoney", stats.getInsuranceMoney());
        notNullMap.put("totalInsuranceMoney", cabinetTotalStats.getInsuranceMoney());
        //支出
        notNullMap.put("insuranceRefundMoney", stats.getRefundInsuranceMoney());
        notNullMap.put("totalRefundInsuranceMoney", cabinetTotalStats.getRefundInsuranceMoney());
        //换电人次
        notNullMap.put("activeCustomerCount", stats.getActiveCustomerCount());
        notNullMap.put("orderCount", stats.getOrderCount());
        notNullMap.put("totalOrderCount", cabinetTotalStats.getOrderCount());

        //设备
        notNullMap.put("rentMoney", cabinet.getRentMoney());
        if (cabinet.getRentExpireTime() != null) {
            notNullMap.put("rentExpireTime", DateFormatUtils.format(cabinet.getRentExpireTime(), Constant.DATE_FORMAT));
        } else {
            notNullMap.put("rentExpireTime", "");
        }

        List<Integer> inCustomerStatus = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        List<Integer> outCustomerCountStatus = Arrays.asList(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue());

        int inCount = customerForegiftOrderService.findCountByCabinetId(cabinet.getId(), agentId, inCustomerStatus, null, null);
        int outCount = customerForegiftOrderService.findCountByCabinetId(cabinet.getId(), agentId, outCustomerCountStatus, null, null);

        Date beginTime = DateUtils.parseDate(param.queryBeginDate + " 00:00:00", new String[]{Constant.DATE_TIME_FORMAT});
        Date endTime = DateUtils.parseDate(param.queryEndDate + " 23:59:59", new String[]{Constant.DATE_TIME_FORMAT});

        //用户
        int foregiftCount = customerForegiftOrderService.findCountByCabinetId(cabinet.getId(), agentId, inCustomerStatus, beginTime, endTime);

        notNullMap.put("foregiftCustomerCount", foregiftCount);
        notNullMap.put("inCustomerCount", inCount);//已交 在网
        notNullMap.put("outCustomerCount", outCount);//已退 退网
        notNullMap.put("totalCustomerCount", inCount + outCount);
        //电费
        notNullMap.put("unitPrice", cabinet.getPrice() * 100);
        notNullMap.put("electricDegree", stats.getElectricDegree());
        notNullMap.put("totalElectricDegree", cabinetTotalStats.getElectricDegree());
        notNullMap.put("electricPrice", stats.getElectricPrice());
        notNullMap.put("totalElectricPrice", cabinetTotalStats.getElectricPrice());

        if(stats.getActiveCustomerCount() != 0){
            notNullMap.put("referencePrice",  stats.getElectricPrice()/stats.getActiveCustomerCount());
            notNullMap.put("totalReferencePrice", cabinetTotalStats.getPerElectric());
        } else {
            notNullMap.put("referencePrice", 0);
            notNullMap.put("totalReferencePrice", 0);
        }
        //电池
        int exchangeCount = cabinetBoxService.statsCompleteChargeCount(cabinet.getId(), Battery.Status.IN_BOX.getValue());
        int lockCount = cabinetBoxService.statsBoxCountByStatus(cabinet.getId());
        int batteryCount = exchangeCount + lockCount + cabinet.getChargeCount();
        notNullMap.put("exchangeCount", exchangeCount);
        notNullMap.put("chargeCount", cabinet.getChargeBatteryNum());
        notNullMap.put("lockCount", lockCount);
        notNullMap.put("batteryCount", batteryCount);

        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, notNullMap);
    }

    public static class QueryForegiftOrderListParam {
        public String cabinetId;
        public String date;
        public String keyword;
    }

    /**
     * 92-查询在设备上购买押金记录列表
     */
    @ResponseBody
    @RequestMapping(value = "/query_foregift_order_list.htm")
    public RestResult queryForegiftOrderList(@Valid @RequestBody QueryForegiftOrderListParam param) throws Exception {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Date createTime = null;
        Cabinet cabinet = null;
        int isFinish = 0;
        List<CustomerForegiftOrder> list = null;
        Date beginDate = DateUtils.parseDate(param.date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);
        if (StringUtils.isNotEmpty(param.cabinetId)) {
            cabinet = cabinetService.find(param.cabinetId);
            createTime = cabinet.getCreateTime();
            Date tempDate;
            if(beginDate.getTime() > createTime.getTime()){
                tempDate = beginDate;
            } else {
                tempDate = createTime;
                isFinish = 1;
            }
            list = customerForegiftOrderService.findListByCabinetId(agentId, param.cabinetId, param.keyword, tempDate, endDate);
        } else {
            isFinish = 1;
            list = customerForegiftOrderService.findListByCabinetId(agentId, null, param.keyword, beginDate, endDate);
        }


        int packetPeriodMoney = 0;
        List<NotNullMap> result = new ArrayList<NotNullMap>();
        for (CustomerForegiftOrder stats : list) {
            NotNullMap notNullMap = new NotNullMap();
            PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findOneEnabled(stats.getCustomerId(), agentId);
            notNullMap.put("cabinetId", cabinetService.find(stats.getCabinetId()).getId());
            notNullMap.put("cabinetName", cabinetService.find(stats.getCabinetId()).getCabinetName());
            notNullMap.put("customerFullname", stats.getCustomerFullname());
            if (stats.getPayTime() != null) {
                notNullMap.putDate("payTime", stats.getPayTime());
            } else {
                notNullMap.put("payTime", "");
            }
            notNullMap.put("foregiftMoney", stats.getMoney());
            notNullMap.put("ticketMoney", stats.getTicketMoney() != null ? stats.getTicketMoney() : 0);
            notNullMap.put("deductionTicketMoney", stats.getDeductionTicketMoney() != null ? stats.getDeductionTicketMoney() : 0);
            if (packetPeriodOrder != null) {
                packetPeriodMoney = packetPeriodOrder.getMoney();
            }
            notNullMap.put("packetPeriodMoney", packetPeriodMoney);

            int totalMoney = (stats.getTicketMoney() != null ? stats.getTicketMoney() : 0)
                    + (stats.getDeductionTicketMoney() != null ? stats.getDeductionTicketMoney() : 0)
                    + (packetPeriodOrder != null ? packetPeriodOrder.getMoney() : 0) + stats.getMoney();
            notNullMap.put("totalMoney", totalMoney);

            result.add(notNullMap);
        }

        List listGroupByDay = groupByDayForegiftMoney(result, param.date);

        Map map = new HashMap();
        map.put("isFinish", isFinish);
        map.put("listMonth", listGroupByDay);

        return RestResult.dataResult(0, null, map);
    }



    public static class QueryPacketPeriodOrderListParam {
        public String cabinetId;
        public String date;
        public String keyword;
    }

    /**
     * 92-查询在设备上购买租金记录列表
     */
    @ResponseBody
    @RequestMapping(value = "/query_packet_period_order_list.htm")
    public RestResult queryPacketPeriodOrderList(@Valid @RequestBody QueryPacketPeriodOrderListParam param) throws Exception {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Date createTime = null;
        Cabinet cabinet = null;
        int isFinish = 0;
        List<PacketPeriodOrder> list = null;
        Date beginDate = DateUtils.parseDate(param.date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);
        if (StringUtils.isNotEmpty(param.cabinetId)) {
            cabinet = cabinetService.find(param.cabinetId);
            createTime = cabinet.getCreateTime();
            Date tempDate;
            if(beginDate.getTime() > createTime.getTime()){
                tempDate = beginDate;
            } else {
                tempDate = createTime;
                isFinish = 1;
            }
            list = packetPeriodOrderService.findListByCabinetId(agentId, param.cabinetId, param.keyword, tempDate, endDate);
        } else {
            isFinish = 1;
            list = packetPeriodOrderService.findListByCabinetId(agentId, null, param.keyword, beginDate, endDate);
        }


        int customerForegiftMoney = 0;
        List<NotNullMap> result = new ArrayList<NotNullMap>();
        for (PacketPeriodOrder stats : list) {
            NotNullMap notNullMap = new NotNullMap();
            CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderService.findOneEnabled(stats.getCustomerId(), agentId);
            notNullMap.put("cabinetId", cabinetService.find(stats.getCabinetId()).getId());
            notNullMap.put("cabinetName", cabinetService.find(stats.getCabinetId()).getCabinetName());
            notNullMap.put("customerFullname", stats.getCustomerFullname());
            if (stats.getPayTime() != null) {
                notNullMap.putDate("payTime", stats.getPayTime());
            } else {
                notNullMap.put("payTime", "");
            }
            notNullMap.put("packetPeriodMoney", stats.getMoney());
            notNullMap.put("ticketMoney", stats.getTicketMoney() != null ? stats.getTicketMoney() : 0);
            if (customerForegiftOrder != null) {
                customerForegiftMoney = customerForegiftOrder.getMoney();
            }
            notNullMap.put("foregiftMoney", customerForegiftMoney);

            int totalMoney = (stats.getTicketMoney() != null ? stats.getTicketMoney() : 0)
                    + (customerForegiftOrder != null ? customerForegiftOrder.getMoney() : 0) + stats.getMoney();
            notNullMap.put("totalMoney", totalMoney);

            result.add(notNullMap);
        }

        List listGroupByDay = groupByDayPacketPeriodMoney(result, param.date);

        Map map = new HashMap();
        map.put("isFinish", isFinish);
        map.put("listMonth", listGroupByDay);

        return RestResult.dataResult(0, null, map);
    }

    public static class QueryForegiftPacketInoutMoneyListParam {
        public String keyword;
        public int offset;
        public int limit;
    }

    /**
     * 93-查询所有设备押金租金订单电费列表
     */
    @ResponseBody
    @RequestMapping(value = "/query_foregift_packet_inout_money_list.htm")
    public RestResult queryForegiftPacketInoutMoneyList(@Valid @RequestBody QueryForegiftPacketInoutMoneyListParam param) throws ParseException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        List<CabinetTotalStats> list = cabinetTotalStatsService.findListByAgentId(agentId, param.keyword, param.offset, param.limit);
        List<Map> result = new ArrayList<Map>();
        NotNullMap data = new NotNullMap();

        for (CabinetTotalStats stats : list) {
            NotNullMap notNullMap = new NotNullMap();
            Cabinet cabinet = cabinetService.find(stats.getCabinetId());
            notNullMap.put("cabinetId", cabinet.getId());
            notNullMap.put("cabinetName", cabinet.getCabinetName());
            notNullMap.put("foregiftMoney", stats.getForegiftMoney());
            notNullMap.put("refundForegiftMoney", stats.getRefundForegiftMoney());
            notNullMap.put("packetPeriodMoney", stats.getPacketPeriodMoney());
            notNullMap.put("refundPacketPeriodMoney", stats.getRefundPacketPeriodMoney());
            notNullMap.put("orderCount", stats.getOrderCount());
            int activeCount = stats.getForegiftCount() - stats.getRefundForegiftCount();
            notNullMap.put("activeCustomerCount", activeCount > 0 ? activeCount : 0);
            notNullMap.put("unitPrice", cabinet.getPrice() * 100);
            notNullMap.put("electricPrice", stats.getElectricPrice());
            notNullMap.put("electricDegree", stats.getElectricDegree());

            result.add(notNullMap);
        }
        CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.findCountByAgentId(agentId);
        data.put("totalOrderCount", cabinetTotalStats.getOrderCount());
        if (cabinetTotalStats != null) {
            data.put("totalActiveCustomerCount", cabinetTotalStats.getForegiftCount()- cabinetTotalStats.getRefundForegiftCount());
        } else {
            data.put("totalActiveCustomerCount", 0);
        }
        data.put("totalElectricPrice", cabinetTotalStats.getElectricPrice());
        data.put("totalElectricDegree", cabinetTotalStats.getElectricDegree());
        data.put("totalForegiftMoney", cabinetTotalStats.getForegiftMoney());
        data.put("totalRefundForegiftMoney", cabinetTotalStats.getRefundForegiftMoney());
        data.put("totalPacketPeriodMoney", cabinetTotalStats.getPacketPeriodMoney());
        data.put("totalRefundPacketPeriodMoney", cabinetTotalStats.getRefundPacketPeriodMoney());
        data.put("list", result);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CabinetListParam {
        public String keyword;
        public int offset;
        public int limit;
    }
    /**
     * 94-查询运营商经营报表选择设备列表
     *
     **/
    @ResponseBody
    @RequestMapping(value = "/query_cabinet_list")
    public RestResult queryCabinetList(@Valid @RequestBody CabinetListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<Cabinet> list = cabinetService.findList(agentId, null, param.keyword, param.offset, param.limit);

        List<Map> result = new ArrayList<Map>();
        for (Cabinet cabinet : list) {
            Map line = new HashMap();
            line.put("cabinetId", cabinet.getId());
            line.put("cabinetName", cabinet.getCabinetName());
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }


    public static class QueryAgentShopStatsParam {
        public String keyword;
        @NotBlank(message = "开始日期不能为空")
        public String queryBeginDate;
        @NotBlank(message = "结束日期不能为空")
        public String queryEndDate;
        public int offset;
        public int limit;
    }

    /**
     * 150-查询运营商经营报表按门店(日周月)
     */
    @ResponseBody
    @RequestMapping("/query_agent_shop_stats.htm")
    public RestResult queryAgentShopStats(@RequestBody QueryAgentShopStatsParam param) throws IOException, ParseException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        List<ShopDayStats> shopDayStats = shopDayStatsService.findTotalShopStatsList(
                agentId, ConstEnum.Category.RENT.getValue(), param.queryBeginDate, param.queryEndDate, param.keyword, param.offset, param.limit);

        List<NotNullMap> list = new ArrayList<NotNullMap>();

        for (ShopDayStats stats : shopDayStats) {
            NotNullMap notNullMap = new NotNullMap();

            Shop shop = shopService.find(stats.getShopId());
            ShopTotalStats shopTotalStats = shopTotalStatsService.find(stats.getShopId(), ConstEnum.Category.RENT.getValue(), agentId);

            notNullMap.putDateTime("createTime",shop.getCreateTime());

            notNullMap.put("shopId", stats.getShopId());
            notNullMap.put("shopName", stats.getShopName());

            //押金
            //收入
            notNullMap.put("foregiftMoney", stats.getAgentForegiftMoney());
            notNullMap.put("totalForegiftMoney",shopTotalStats.getAgentForegiftMoney());
            //支出
            notNullMap.put("foregiftRefundMoney", stats.getAgentRefundForegiftMoney());
            notNullMap.put("totalForegiftRefundMoney", shopTotalStats.getAgentRefundForegiftMoney());
            //租金
            //收入
            notNullMap.put("packetPeriodMoney", stats.getPacketPeriodMoney());
            notNullMap.put("totalPacketPeriodMoney", shopTotalStats.getPacketPeriodMoney());
            //支出
            notNullMap.put("refundPacketPeriodMoney", stats.getRefundPacketPeriodMoney());
            notNullMap.put("totalRefundPacketPeriodMoney", shopTotalStats.getRefundPacketPeriodMoney());
            //保险
            //收入
            notNullMap.put("insuranceMoney", stats.getAgentInsuranceMoney());
            notNullMap.put("totalInsuranceMoney", shopTotalStats.getAgentInsuranceMoney());
            //支出
            notNullMap.put("insuranceRefundMoney", stats.getAgentRefundInsuranceMoney());
            notNullMap.put("totalRefundInsuranceMoney", shopTotalStats.getAgentRefundInsuranceMoney());

            List<Integer> inCustomerStatus = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
            List<Integer> outCustomerCountStatus = Arrays.asList(RentForegiftOrder.Status.REFUND_SUCCESS.getValue());
            int inCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, inCustomerStatus, null, null);
            int outCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, outCustomerCountStatus, null, null);

            Date beginTime = DateUtils.parseDate(param.queryBeginDate + " 00:00:00", new String[]{Constant.DATE_TIME_FORMAT});
            Date endTime = DateUtils.parseDate(param.queryEndDate + " 23:59:59", new String[]{Constant.DATE_TIME_FORMAT});

            //用户
            int foregiftCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, inCustomerStatus, beginTime, endTime);

            notNullMap.put("foregiftCustomerCount", foregiftCount);
            notNullMap.put("inCustomerCount", inCount);//已交 在网
            notNullMap.put("outCustomerCount", outCount);//已退 退网
            notNullMap.put("totalCustomerCount", inCount + outCount);

            //电池
            int shopStoreBattery = shopStoreBatteryService.findCount(shop.getId());
            int customerBatteryCount = batteryService.shopCustomerUseList(shop.getId(), null, null,null).size();
            int totalBatteryCount = shopStoreBattery + customerBatteryCount;
            notNullMap.put("storeBatteryCount", shopStoreBattery);
            notNullMap.put("customerBatteryCount", customerBatteryCount);
            notNullMap.put("totalBatteryCount", totalBatteryCount);

            list.add(notNullMap);
        }

        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    public static class QueryAgentShopDetailsStatsParam {
        public String shopId;
        @NotBlank(message = "开始日期不能为空")
        public String queryBeginDate;
        @NotBlank(message = "结束日期不能为空")
        public String queryEndDate;
    }
    /**
     * 154-查询运营商经营报表按门店明细(日周月)
     */
    @ResponseBody
    @RequestMapping("/query_agent_shop_details_stats.htm")
    public RestResult queryAgentShopDetailsStats(@RequestBody QueryAgentShopDetailsStatsParam param) throws IOException, ParseException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        ShopDayStats stats = shopDayStatsService.findTotalStatsListShopId(
                agentId, param.shopId, ConstEnum.Category.RENT.getValue(), param.queryBeginDate, param.queryEndDate);


        NotNullMap notNullMap = new NotNullMap();

        Shop shop = shopService.find(param.shopId);
        ShopTotalStats shopTotalStats = shopTotalStatsService.find(param.shopId, ConstEnum.Category.RENT.getValue(), agentId);

        notNullMap.putDateTime("createTime",shop.getCreateTime());

        notNullMap.put("shopId", shop.getId());
        notNullMap.put("shopName", shop.getShopName());

        //押金
        //收入
        notNullMap.put("foregiftMoney", stats.getAgentForegiftMoney());
        notNullMap.put("totalForegiftMoney",shopTotalStats.getAgentForegiftMoney());
        //支出
        notNullMap.put("foregiftRefundMoney", stats.getAgentRefundForegiftMoney());
        notNullMap.put("totalForegiftRefundMoney", shopTotalStats.getAgentRefundForegiftMoney());
        //租金
        //收入
        notNullMap.put("packetPeriodMoney", stats.getAgentPacketPeriodMoney());
        notNullMap.put("totalPacketPeriodMoney", shopTotalStats.getAgentPacketPeriodMoney());
        //支出
        notNullMap.put("refundPacketPeriodMoney", stats.getAgentRefundPacketPeriodMoney());
        notNullMap.put("totalRefundPacketPeriodMoney", shopTotalStats.getAgentRefundPacketPeriodMoney());
        //保险
        //收入
        notNullMap.put("insuranceMoney", stats.getAgentInsuranceMoney());
        notNullMap.put("totalInsuranceMoney", shopTotalStats.getAgentInsuranceMoney());
        //支出
        notNullMap.put("insuranceRefundMoney", stats.getAgentRefundInsuranceMoney());
        notNullMap.put("totalRefundInsuranceMoney", shopTotalStats.getAgentRefundInsuranceMoney());

        List<Integer> inCustomerStatus = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
        List<Integer> outCustomerCountStatus = Arrays.asList(RentForegiftOrder.Status.REFUND_SUCCESS.getValue());
        int inCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, inCustomerStatus, null, null);
        int outCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, outCustomerCountStatus, null, null);

        Date beginTime = DateUtils.parseDate(param.queryBeginDate + " 00:00:00", new String[]{Constant.DATE_TIME_FORMAT});
        Date endTime = DateUtils.parseDate(param.queryEndDate + " 23:59:59", new String[]{Constant.DATE_TIME_FORMAT});

        //用户
        int foregiftCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, inCustomerStatus, beginTime, endTime);

        notNullMap.put("foregiftCustomerCount", foregiftCount);
        notNullMap.put("inCustomerCount", inCount);//已交 在网
        notNullMap.put("outCustomerCount", outCount);//已退 退网

        notNullMap.put("totalCustomerCount", inCount + outCount);
        //电池
        int shopStoreBattery = shopStoreBatteryService.findCount(shop.getId());
        int customerBatteryCount = batteryService.shopCustomerUseList(shop.getId(), null, null,null).size();
        int totalBatteryCount = shopStoreBattery + customerBatteryCount;
        notNullMap.put("storeBatteryCount", shopStoreBattery);
        notNullMap.put("customerBatteryCount", customerBatteryCount);
        notNullMap.put("totalBatteryCount", totalBatteryCount);
        notNullMap.put("totalOrderCount",  shopTotalStats.getAgentPacketPeriodCount());

        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, notNullMap);
    }

    public static class QueryRentForegiftOrderListParam {
        public String shopId;
        public String date;
        public String keyword;
    }

    /**
     * 151-查询在门店上购买押金租金记录列表
     */
    @ResponseBody
    @RequestMapping(value = "/query_rent_foregift_order_list.htm")
    public RestResult queryRentForegiftOrderList(@Valid @RequestBody QueryRentForegiftOrderListParam param) throws Exception {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        Date createTime = null;
        Shop shop = null;
        int isFinish = 0;
        List<RentForegiftOrder> list = null;
        Date beginDate = DateUtils.parseDate(param.date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);
        if (StringUtils.isNotEmpty(param.shopId)) {
            shop = shopService.find(param.shopId);
            createTime = shop.getCreateTime();
            Date tempDate;
            if(beginDate.getTime() > createTime.getTime()){
                tempDate = beginDate;
            } else {
                tempDate = createTime;
                isFinish = 1;
            }
            list = rentForegiftOrderService.findListByShopId(agentId, param.shopId, param.keyword, tempDate, endDate);
        } else {
            isFinish = 1;
            list = rentForegiftOrderService.findListByShopId(agentId, null, param.keyword, beginDate, endDate);
        }

        List<NotNullMap> result = new ArrayList<NotNullMap>();
        for (RentForegiftOrder stats : list) {
            NotNullMap notNullMap = new NotNullMap();
            RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.findLastEndTime(stats.getCustomerId());
            notNullMap.put("shopId", shopService.find(stats.getShopId()).getId());
            notNullMap.put("shopName", shopService.find(stats.getShopId()).getShopName());
            notNullMap.put("customerFullname", stats.getCustomerFullname());
            notNullMap.put("payTime", DateFormatUtils.format(stats.getPayTime(), Constant.DATE_FORMAT));
            notNullMap.put("foregiftMoney", stats.getMoney());
            notNullMap.put("ticketMoney", stats.getTicketMoney() != null ? stats.getTicketMoney() : 0);
            notNullMap.put("deductionTicketMoney", stats.getDeductionTicketMoney() != null ? stats.getDeductionTicketMoney() : 0);
            notNullMap.put("packetPeriodMoney", rentPeriodOrder != null ? rentPeriodOrder.getMoney() : 0);
            int totalMoney = (stats.getTicketMoney() != null ? stats.getTicketMoney() : 0)
                    + (stats.getDeductionTicketMoney() != null ? stats.getDeductionTicketMoney() : 0)
                    + (rentPeriodOrder != null ? rentPeriodOrder.getMoney() : 0) + stats.getMoney();
            notNullMap.put("totalMoney", totalMoney);
            result.add(notNullMap);
        }

        List listGroupByDay = groupByDayForegiftMoney(result, param.date);

        Map map = new HashMap();
        map.put("isFinish", isFinish);
        map.put("listMonth", listGroupByDay);

        return RestResult.dataResult(0, null, map);
    }

    public static class QueryRentPacketPeriodOrderListParam {
        public String shopId;
        public String date;
        public String keyword;
    }

    /**
     * 151-查询在门店上购买押金租金记录列表
     */
    @ResponseBody
    @RequestMapping(value = "/query_rent_packet_period_order_list.htm")
    public RestResult queryRentPacketPeriodOrderList(@Valid @RequestBody QueryRentPacketPeriodOrderListParam param) throws Exception {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        Date createTime = null;
        Shop shop = null;
        int isFinish = 0;
        List<RentPeriodOrder> list = null;
        Date beginDate = DateUtils.parseDate(param.date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);
        if (StringUtils.isNotEmpty(param.shopId)) {
            shop = shopService.find(param.shopId);
            createTime = shop.getCreateTime();
            Date tempDate;
            if(beginDate.getTime() > createTime.getTime()){
                tempDate = beginDate;
            } else {
                tempDate = createTime;
                isFinish = 1;
            }
            list = rentPeriodOrderService.findListByShopId(agentId, param.shopId, param.keyword, tempDate, endDate);
        } else {
            isFinish = 1;
            list = rentPeriodOrderService.findListByShopId(agentId, null, param.keyword, beginDate, endDate);
        }

        List<NotNullMap> result = new ArrayList<NotNullMap>();
        for (RentPeriodOrder stats : list) {
            NotNullMap notNullMap = new NotNullMap();
            RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.findLastEndTime(stats.getCustomerId());
            notNullMap.put("shopId", shopService.find(stats.getShopId()).getId());
            notNullMap.put("shopName", shopService.find(stats.getShopId()).getShopName());
            notNullMap.put("customerFullname", stats.getCustomerFullname());
            notNullMap.put("payTime", DateFormatUtils.format(stats.getPayTime(), Constant.DATE_FORMAT));
            notNullMap.put("packetPeriodMoney", stats.getMoney());
            notNullMap.put("ticketMoney", stats.getTicketMoney() != null ? stats.getTicketMoney() : 0);
            notNullMap.put("foregiftMoney", rentForegiftOrder != null ? rentForegiftOrder.getMoney() : 0);
            int totalMoney = (stats.getTicketMoney() != null ? stats.getTicketMoney() : 0)
                    + (rentForegiftOrder != null ? rentForegiftOrder.getMoney() : 0) + stats.getMoney();
            notNullMap.put("totalMoney", totalMoney);
            result.add(notNullMap);
        }

        List listGroupByDay = groupByDayPacketPeriodMoney(result, param.date);

        Map map = new HashMap();
        map.put("isFinish", isFinish);
        map.put("listMonth", listGroupByDay);

        return RestResult.dataResult(0, null, map);
    }

    private List groupByDayForegiftMoney(List<NotNullMap> monthList, String date) throws Exception {
        Date beginDate = DateUtils.parseDate(date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);

        List listGroupByDay = new ArrayList();
        while (beginDate.getTime() <= endDate.getTime()) {
            List list = new ArrayList();
            int totalCount = 0, totalMoney = 0;

            for (NotNullMap map : monthList) {
                if (map.get("payTime") != null) {
                    Date statsDate = DateUtils.parseDate((String)map.get("payTime"), new String[]{Constant.DATE_FORMAT});
                    if (statsDate.getTime() == beginDate.getTime()) {
                        totalCount += 1;
                        totalMoney += (Integer) map.get("foregiftMoney");
                        list.add(map);
                    }
                }

            }

            if(totalCount > 0) {
                NotNullMap stats = new NotNullMap();
                stats.putDate("statsDate", beginDate);
                stats.put("totalCount", totalCount);
                stats.put("totalMoney", totalMoney);

                Map data = new HashMap();
                data.put("list", list);
                data.put("stats", stats);

                listGroupByDay.add(data);
            }

            beginDate = DateUtils.addDays(beginDate, 1);
        }
        return listGroupByDay;
    }

    private List groupByDayPacketPeriodMoney(List<NotNullMap> monthList, String date) throws Exception {
        Date beginDate = DateUtils.parseDate(date, new String[]{Constant.MONTH_FORMAT, Constant.DATE_FORMAT});
        Date date1 = DateUtils.addMonths(beginDate, 1);
        Date endDate = DateUtils.addDays(date1, -1);

        List listGroupByDay = new ArrayList();
        while (beginDate.getTime() <= endDate.getTime()) {
            List list = new ArrayList();
            int totalCount = 0, totalMoney = 0;

            for (NotNullMap map : monthList) {
                if (map.get("payTime") != null) {
                    Date statsDate = DateUtils.parseDate((String)map.get("payTime"), new String[]{Constant.DATE_FORMAT});
                    if (statsDate.getTime() == beginDate.getTime()) {
                        totalCount += 1;
                        totalMoney += (Integer) map.get("packetPeriodMoney");
                        list.add(map);
                    }
                }

            }

            if(totalCount > 0) {
                NotNullMap stats = new NotNullMap();
                stats.putDate("statsDate", beginDate);
                stats.put("totalCount", totalCount);
                stats.put("totalMoney", totalMoney);

                Map data = new HashMap();
                data.put("list", list);
                data.put("stats", stats);

                listGroupByDay.add(data);
            }

            beginDate = DateUtils.addDays(beginDate, 1);
        }
        return listGroupByDay;
    }

    public static class QueryRentForegiftPacketInoutMoneyListParam {
        public String keyword;
        public int offset;
        public int limit;
    }

    /**
     * 152-查询所有门店押金租金订单收入支出列表
     */
    @ResponseBody
    @RequestMapping(value = "/query_rent_foregift_packet_inout_money_list.htm")
    public RestResult QueryRentForegiftPacketInoutMoneyList(@Valid @RequestBody QueryRentForegiftPacketInoutMoneyListParam param) throws ParseException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }


        List<ShopTotalStats> list = shopTotalStatsService.findListByAgentId(agentId, param.keyword, ConstEnum.Category.RENT.getValue(), param.offset, param.limit);
        List<Map> result = new ArrayList<Map>();
        NotNullMap data = new NotNullMap();
        for (ShopTotalStats stats : list) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.put("shopId", stats.getShopId());
            notNullMap.put("shopName", stats.getShopName());
            notNullMap.put("foregiftMoney", stats.getAgentForegiftMoney());
            notNullMap.put("refundForegiftMoney", stats.getAgentRefundForegiftMoney());
            notNullMap.put("packetPeriodMoney", stats.getAgentPacketPeriodMoney());
            notNullMap.put("refundPacketPeriodMoney", stats.getAgentRefundPacketPeriodMoney());
            notNullMap.put("orderCount", stats.getAgentPacketPeriodCount());
            int activeCount = stats.getAgentForegiftCount() - stats.getAgentRefundForegiftCount();
            notNullMap.put("activeCustomerCount", activeCount > 0 ? activeCount : 0);

            result.add(notNullMap);
        }


        AgentTotalStats agentTotalStats = agentTotalStatsService.find(agentId, ConstEnum.Category.RENT.getValue());
        data.put("totalOrderCount", agentTotalStats.getPacketPeriodOrderCount());
        data.put("totalActiveCustomerCount", agentTotalStats.getForegiftCount() - agentTotalStats.getForegiftRefundCount());

        ShopTotalStats shopTotalStats = shopTotalStatsService.findCountByAgentId(agentId, ConstEnum.Category.RENT.getValue());
        data.put("totalForegiftMoney", shopTotalStats.getAgentForegiftMoney());
        data.put("totalRefundForegiftMoney", shopTotalStats.getAgentRefundForegiftMoney());
        data.put("totalPacketPeriodMoney", shopTotalStats.getPacketPeriodMoney());
        data.put("totalRefundPacketPeriodMoney", shopTotalStats.getRefundPacketPeriodMoney());
        data.put("list", result);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShopListParam {
        public String keyword;
        public int offset;
        public int limit;
    }
    /**
     * 23-查询运营商经营报表选择门店列表
     *
     **/
    @ResponseBody
    @RequestMapping(value = "/query_shop_list")
    public RestResult shopListParam(@Valid @RequestBody ShopListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<Shop> list = shopService.findList(agentId, param.keyword, param.offset, param.limit);

        List<Map> result = new ArrayList<Map>();
        for (Shop shop : list) {
            Map line = new HashMap();
            line.put("shopId", shop.getId());
            line.put("shopName", shop.getShopName());
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NearestParam {
        public int areaId;
        public double lng;
        public double lat;
        public int type;
        public Integer offset;
        public Integer limit;
    }

    /**
     * 160-查询运营商经营报表设备充换在线离线列表
     *
     **/
    @ResponseBody
    @RequestMapping(value = "/query_cabinet_charge_is_online_list")
    public RestResult queryCabinetChargeIsOnlineList(@Valid @RequestBody NearestParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;

        Area area = areaCache.get(param.areaId);
        if(area == null) {
            area = areaCache.get(param.areaId / 100 * 100);
            if (area == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }

        Area province = null, city = null;
        if(area.getAreaLevel() == 3) {
            city = areaCache.get(area.getParentId());
        } else if(area.getAreaLevel() == 2) {
            city = area;
        } else if(area.getAreaLevel() == 1) {
            province = area;
        }

        if (city != null) {
            province = areaCache.get(city.getParentId());
            if(province == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }
        if (province == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        if (param == null || param.lng == 0 || param.lat == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "经纬度错误");
        }
        int allCount = 0;
        int onLineCount = 0;
        int offLineCount = 0;
        int notOnlineCount = 0;

        //普通用户默认查找20公里内的柜子
        String geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);
        NotNullMap data = new NotNullMap();
        // 全部
        List<Cabinet> allList;
        List<Integer> upLineStatus = Arrays.asList(
                Cabinet.UpLineStatus.NOT_ONLINE.getValue(),
                Cabinet.UpLineStatus.ONLINE.getValue());
        List<Integer> isOnLine = Arrays.asList(
                ConstEnum.Flag.TRUE.getValue(),
                ConstEnum.Flag.FALSE.getValue());
        allList = cabinetService.findNearest(
                agentId,
                geoHash,
                param.lng,
                param.lat,
                null,
                province == null ? null : province.getId(),
                city == null ? null: city.getId(),
                upLineStatus,//上线
                isOnLine,//在线
                param.offset,
                param.limit);

        // 正常
        List<Cabinet> onLineList;
        List<Integer> upLineStatusOnLine = Arrays.asList(
                Cabinet.UpLineStatus.ONLINE.getValue());
        List<Integer> isOnLineOnLine = Arrays.asList(
                ConstEnum.Flag.TRUE.getValue());
        onLineList = cabinetService.findNearest(
                agentId,
                geoHash,
                param.lng,
                param.lat,
                null,
                province == null ? null : province.getId(),
                city == null ? null: city.getId(),
                upLineStatusOnLine,//上线
                isOnLineOnLine,//在线
                param.offset,
                param.limit);

        // 离线
        List<Cabinet> offLineList;
        List<Integer> upLineStatusOffLine = Arrays.asList(
                Cabinet.UpLineStatus.ONLINE.getValue());
        List<Integer> isOnLineOffLine = Arrays.asList(
                ConstEnum.Flag.FALSE.getValue());
        offLineList = cabinetService.findNearest(
                agentId,
                geoHash,
                param.lng,
                param.lat,
                null,
                province == null ? null : province.getId(),
                city == null ? null: city.getId(),
                upLineStatusOffLine,//上线
                isOnLineOffLine,//离线
                param.offset,
                param.limit);

        // 未上线
        List<Cabinet> notOnlineList;
        List<Integer> upLineStatusNotOnline = Arrays.asList(
                Cabinet.UpLineStatus.NOT_ONLINE.getValue());
        List<Integer> isOnLineNotOnline = Arrays.asList(
                ConstEnum.Flag.FALSE.getValue());
        notOnlineList = cabinetService.findNearest(
                agentId,
                geoHash,
                param.lng,
                param.lat,
                null,
                province == null ? null : province.getId(),
                city == null ? null: city.getId(),
                upLineStatusNotOnline,//未上线
                isOnLineNotOnline,//离线
                param.offset,
                param.limit);

        List<Map> result = new ArrayList<Map>();
        if (param.type == 1) { //全部
            for (Cabinet cabinet : allList) {
                Map line = new HashMap();
                line.put("id", cabinet.getId());
                line.put("agentId", cabinet.getAgentId());
                line.put("cabinetName", cabinet.getCabinetName());
                line.put("address", cabinet.getAddress());
                line.put("distance", cabinet.getDistance());
                line.put("fullCount", cabinet.getFullCount());
                line.put("chargeCount", cabinet.getChargeCount());
                line.put("totalCount", cabinet.getFullCount() + cabinet.getChargeCount());
                result.add(line);
            }
        } else if (param.type == 2) { //正常
            for (Cabinet cabinet : onLineList) {
                Map line = new HashMap();
                line.put("id", cabinet.getId());
                line.put("agentId", cabinet.getAgentId());
                line.put("cabinetName", cabinet.getCabinetName());
                line.put("address", cabinet.getAddress());
                line.put("distance", cabinet.getDistance());
                line.put("fullCount", cabinet.getFullCount());
                line.put("chargeCount", cabinet.getChargeCount());
                line.put("totalCount", cabinet.getFullCount() + cabinet.getChargeCount());
                result.add(line);
            }
        } else if (param.type == 3) { //离线
            for (Cabinet cabinet : offLineList) {
                Map line = new HashMap();
                line.put("id", cabinet.getId());
                line.put("agentId", cabinet.getAgentId());
                line.put("cabinetName", cabinet.getCabinetName());
                line.put("address", cabinet.getAddress());
                line.put("distance", cabinet.getDistance());
                line.put("fullCount", cabinet.getFullCount());
                line.put("chargeCount", cabinet.getChargeCount());
                line.put("totalCount", cabinet.getFullCount() + cabinet.getChargeCount());
                result.add(line);
            }
        } else if (param.type == 4) { //未上线
            for (Cabinet cabinet : notOnlineList) {
                Map line = new HashMap();
                line.put("id", cabinet.getId());
                line.put("agentId", cabinet.getAgentId());
                line.put("cabinetName", cabinet.getCabinetName());
                line.put("address", cabinet.getAddress());
                line.put("distance", cabinet.getDistance());
                line.put("fullCount", cabinet.getFullCount());
                line.put("chargeCount", cabinet.getChargeCount());
                line.put("totalCount", cabinet.getFullCount() + cabinet.getChargeCount());
                result.add(line);
            }
        }
        allCount = cabinetService.findCountByAgentId(agentId, upLineStatus, isOnLine);
        onLineCount = cabinetService.findCountByAgentId(agentId, upLineStatusOnLine, isOnLineOnLine);
        offLineCount = cabinetService.findCountByAgentId(agentId, upLineStatusOffLine, isOnLineOffLine);
        notOnlineCount = cabinetService.findCountByAgentId(agentId, upLineStatusNotOnline, isOnLineNotOnline);
        data.put("allCount", allCount);
        data.put("onLineCount", onLineCount);
        data.put("offLineCount", offLineCount);
        data.put("notOnlineCount", notOnlineCount);
        data.put("cabinetList", result);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    /**
     * 162-查询经营报表运营商门店电池数量
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShopBatteryListParam {
        public String keyword;
        public int type;
        public int category;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/agent_shop_battery_count.htm")
    public RestResult batteryList(@Valid @RequestBody ShopBatteryListParam param ) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<Battery> list = new ArrayList<Battery>();

        List<Integer> normal = Arrays.asList(ConstEnum.Flag.TRUE.getValue());

        List<Integer> onlineStatus = Arrays.asList(Battery.UpLineStatus.ONLINE.getValue());

        List<Integer> customerUserStatus = Arrays.asList(Battery.Status.CUSTOMER_OUT.getValue());

        List<Integer> inCabinetStatus = Arrays.asList(
                Battery.Status.IN_BOX_NOT_PAY.getValue(),
                Battery.Status.IN_BOX.getValue(),
                Battery.Status.IN_BOX_CUSTOMER_USE.getValue());

        //查询运营商电池
        if (param.type == 1) { //客户使用中
            list = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, normal, customerUserStatus, onlineStatus,null,null,null,null, param.offset, param.limit);
        } else if (param.type == 2) { //柜子中
            list = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, normal, inCabinetStatus, onlineStatus,null,null,null,null,param.offset, param.limit);
        } else if (param.type == 3) { //门店中
            list = batteryService.agentShopList(agentId, param.category, param.keyword, param.offset, param.limit);
        } else if (param.type == 4) {//其他 不在库存中 不是客户使用柜子中
            list = batteryService.agentRestsList(agentId, param.category, param.keyword, param.offset, param.limit);
        }

        //客户使用中
        int agentCustomerUseCount = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, normal, customerUserStatus, onlineStatus,null,null,null,null, null, null).size();
        //柜子中
        int agentCabinetCount = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, normal, inCabinetStatus, onlineStatus,null,null,null,null,null, null).size();
        //门店中
        int agentShopCount = batteryService.agentShopList(agentId, param.category, param.keyword, null, null).size();
        //其他
        int agentRestsCount = batteryService.agentRestsList(agentId, param.category, param.keyword, null, null).size();


        List<Map> result = new ArrayList<Map>();
        for (Battery battery : list) {
            Map line = new HashMap();
            line.put("id", battery.getId());
            String batteryTypeName = batteryService.getBatteryTypeName(battery.getType());
            line.put("typeName", batteryTypeName);
            line.put("code", battery.getCode());
            line.put("status", battery.getStatus());
            line.put("volume", battery.getVolume());
            line.put("customerFullname", battery.getCustomerFullname());
            line.put("customerMobile", battery.getCustomerMobile() == null ? null : battery.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            line.put("isOnline", battery.getIsOnline());
            line.put("isNormal", battery.getIsNormal());
            line.put("chargeStatus", battery.getChargeStatus());
            line.put("upLineStatus", battery.getUpLineStatus());
            line.put("shellCode", battery.getShellCode());
            line.put("signalType", battery.getSignalType());
            line.put("currentSignal", battery.getCurrentSignal());
            line.put("category", battery.getCategory());
            result.add(line);
        }
        NotNullMap data = new NotNullMap();
        data.put("agentCustomerUseCount", agentCustomerUseCount);
        data.put("agentCabinetCount", agentCabinetCount);
        data.put("agentShopCount", agentShopCount);
        data.put("agentRestsCount", agentRestsCount);
        data.put("batteryList",result);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InOutCustomerCabinetListParam {
        public String keyword;
        public int offset;
        public int limit;
    }
    /**
     * 163-查询换电柜用户在网退网列表
     *
     **/
    @ResponseBody
    @RequestMapping(value = "/in_out_customer_cabinet_list")
    public RestResult inOutCustomerCabinetList(@Valid @RequestBody InOutCustomerCabinetListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<Cabinet> list = cabinetService.findList(agentId, null, param.keyword, param.offset, param.limit);
        NotNullMap data = new NotNullMap();
        List<Map> result = new ArrayList<Map>();

        AgentTotalStats agentTotalStats = agentTotalStatsService.find(agentId, ConstEnum.Category.EXCHANGE.getValue());
        int totalInCustomerCount = agentTotalStats.getForegiftCount() - agentTotalStats.getForegiftRefundCount();
        int totalOutCustomerCount = agentTotalStats.getForegiftRefundCount();

        for (Cabinet cabinet : list) {
            Map line = new HashMap();
            line.put("id", cabinet.getId());
            line.put("cabinetName", cabinet.getCabinetName());
            CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.find(cabinet.getId(), agentId);
            if (cabinetTotalStats != null) {
                line.put("inCustomerCount", cabinetTotalStats.getForegiftCount() - cabinetTotalStats.getRefundForegiftCount());
                line.put("outCustomerCount", cabinetTotalStats.getRefundForegiftCount());
                line.put("totalCount", cabinetTotalStats.getForegiftCount());
            } else {
                line.put("inCustomerCount", 0);
                line.put("outCustomerCount", 0);
                line.put("totalCount", 0);
            }
            result.add(line);
        }
        data.put("totalInCustomerCount", totalInCustomerCount);
        data.put("totalOutCustomerCount", totalOutCustomerCount);
        data.put("customerList", result);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    public static class QueryAgentDayMonthStatsParam {
        public int category;
    }
    /**
     * 164-查询运营商经营报表按月(全部)
     */
    @ResponseBody
    @RequestMapping("/query_agent_month_stats.htm")
    public RestResult queryAgentDayMonthStats(@RequestBody QueryAgentDayMonthStatsParam param) {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        if (param.category == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
        }
        NotNullMap data = new NotNullMap();

        List<AgentMonthStats> statsList = agentMonthStatsService.findList(agentId, param.category);
        List<NotNullMap> list = new ArrayList<NotNullMap>();
        for (AgentMonthStats stats : statsList) {
            NotNullMap notNullMap = new NotNullMap();

            notNullMap.put("month", stats.getStatsMonth());
            notNullMap.put("foregiftMoney", stats.getForegiftMoney());
            notNullMap.put("foregiftRefundMoney", stats.getForegiftRefundMoney());
            notNullMap.put("packetPeriodMoney", stats.getPacketPeriodMoney());
            notNullMap.put("refundPacketPeriodMoney", stats.getRefundPacketPeriodMoney());
            notNullMap.put("foregiftCount", stats.getForegiftCount());

            list.add(notNullMap);
        }
        data.put("statsList", list);


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryCabinetCustomerParam {
        public String cabinetId;
        public String keyword;
        public int type;
        public int offset;
        public int limit;
    }

    /**
     * 165-查询站点用户
     */
    @ResponseBody
    @RequestMapping(value = "/query_cabinet_customer.htm")
    public RestResult queryCabinetCustomer(@Valid @RequestBody QueryCabinetCustomerParam param) {
        int agentId = getTokenData().agentId;
        Map data = new HashMap();
        List<Map> list = new ArrayList<Map>();
        List<Integer> allCountStatus = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue(), CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue());
        List<Integer> inCustomerStatus = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        List<Integer> outCustomerCountStatus = Arrays.asList(CustomerForegiftOrder.Status.REFUND_SUCCESS.getValue());

        List<CustomerForegiftOrder> allList = customerForegiftOrderService.findByCabinetId(agentId, param.cabinetId, allCountStatus, param.keyword, param.offset, param.limit);
        List<CustomerForegiftOrder> inList = customerForegiftOrderService.findByCabinetId(agentId, param.cabinetId, inCustomerStatus, param.keyword, param.offset, param.limit);
        List<CustomerForegiftOrder> outList = customerForegiftOrderService.findByCabinetId(agentId, param.cabinetId, outCustomerCountStatus, param.keyword, param.offset, param.limit);;

        int inCount = 0;
        int outCount = 0;
        int foregiftCount = 0;
        if(StringUtils.isNotEmpty(param.cabinetId)){
            CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.find(param.cabinetId, agentId);
            if (cabinetTotalStats != null) {
                inCount = cabinetTotalStats.getForegiftCount() - cabinetTotalStats.getRefundForegiftCount();
                outCount = cabinetTotalStats.getRefundForegiftCount();
                foregiftCount = cabinetTotalStats.getForegiftCount();
            }
        } else {
            CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.sumAll(agentId);
            if (cabinetTotalStats != null) {
                int count = cabinetTotalStats.getForegiftCount() != null ? cabinetTotalStats.getForegiftCount() : 0;
                int refundCount = cabinetTotalStats.getRefundForegiftCount() != null ? cabinetTotalStats.getRefundForegiftCount() : 0;
                inCount = count - refundCount; //在网
                outCount = cabinetTotalStats.getRefundForegiftCount();//退网
                foregiftCount = cabinetTotalStats.getForegiftCount();//注册
            }
        }

        if (param.type == 1) {
            for (CustomerForegiftOrder cfo : allList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.put("customerFullname", cfo.getCustomerFullname());
                notNullMap.put("customerMobile", cfo.getCustomerMobile());
                notNullMap.put("cabinetName", cabinetService.find(cfo.getCabinetId()).getCabinetName());
                notNullMap.put("money", cfo.getMoney());
                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findOneEnabled(cfo.getCustomerId(), agentId);

                if (packetPeriodOrder != null) {
                    notNullMap.putInteger("price", packetPeriodOrder.getMoney() != null ? packetPeriodOrder.getMoney() : 0);
                    notNullMap.putDateTime("endTime", packetPeriodOrder.getEndTime());
                    notNullMap.putLong("dayCount", packetPeriodOrder.getEndTime() != null ?  InstallUtils.getRestDay(packetPeriodOrder.getEndTime()) : 0);
                } else {
                    notNullMap.putInteger("price", 0);
                    notNullMap.putString("endTime", "");
                    notNullMap.putInteger("dayCount", 0);
                }

                Customer customer = customerService.find(cfo.getCustomerId());
                notNullMap.put("laxinMobile", customer.getLaxinMobile() == null ? null : customer.getLaxinMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                notNullMap.put("laxinFullname", customer.getLaxinFullname());
                notNullMap.put("date", DateFormatUtils.format(cfo.getCreateTime(), Constant.DATE_FORMAT));
                list.add(notNullMap);
            }
        } else if (param.type == 2) {
            for (CustomerForegiftOrder cfo : inList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.put("customerFullname", cfo.getCustomerFullname());
                notNullMap.put("customerMobile", cfo.getCustomerMobile());
                notNullMap.put("cabinetName", cabinetService.find(cfo.getCabinetId()).getCabinetName());
                notNullMap.put("money", cfo.getMoney());
                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findOneEnabled(cfo.getCustomerId(), agentId);
                if (packetPeriodOrder != null) {
                    notNullMap.putInteger("price", packetPeriodOrder.getMoney() != null ? packetPeriodOrder.getMoney() : 0);
                    notNullMap.putDateTime("endTime", packetPeriodOrder.getEndTime());
                    notNullMap.putLong("dayCount", packetPeriodOrder.getEndTime() != null ?  InstallUtils.getRestDay(packetPeriodOrder.getEndTime()) : 0);
                } else {
                    notNullMap.putInteger("price", 0);
                    notNullMap.putString("endTime", "");
                    notNullMap.putInteger("dayCount", 0);
                }
                Customer customer = customerService.find(cfo.getCustomerId());
                notNullMap.put("laxinMobile", customer.getLaxinMobile() == null ? null : customer.getLaxinMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                notNullMap.put("laxinFullname", customer.getLaxinFullname());
                notNullMap.put("date", DateFormatUtils.format(cfo.getCreateTime(), Constant.DATE_FORMAT));
                list.add(notNullMap);
            }
        } else if (param.type == 3) {
            for (CustomerForegiftOrder cfo : outList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.put("customerFullname", cfo.getCustomerFullname());
                notNullMap.put("customerMobile", cfo.getCustomerMobile());
                notNullMap.put("cabinetName", cabinetService.find(cfo.getCabinetId()).getCabinetName());
                notNullMap.put("money", cfo.getMoney());
                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findOneEnabled(cfo.getCustomerId(), agentId);
                if (packetPeriodOrder != null) {
                    notNullMap.putInteger("price", packetPeriodOrder.getMoney() != null ? packetPeriodOrder.getMoney() : 0);
                    notNullMap.putDateTime("endTime", packetPeriodOrder.getEndTime());
                    notNullMap.putLong("dayCount", packetPeriodOrder.getEndTime() != null ?  InstallUtils.getRestDay(packetPeriodOrder.getEndTime()) : 0);
                } else {
                    notNullMap.putInteger("price", 0);
                    notNullMap.putString("endTime", "");
                    notNullMap.putInteger("dayCount", 0);
                }
                Customer customer = customerService.find(cfo.getCustomerId());
                notNullMap.put("laxinMobile", customer.getLaxinMobile() == null ? null : customer.getLaxinMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
                notNullMap.put("laxinFullname", customer.getLaxinFullname());
                notNullMap.put("date", DateFormatUtils.format(cfo.getCreateTime(), Constant.DATE_FORMAT));
                list.add(notNullMap);
            }
        }
        data.put("signInCount", foregiftCount);
        data.put("inCount", inCount);
        data.put("outCount", outCount);
        data.put("list", list);
        return RestResult.dataResult(0, null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryCabinetOrderParam {
        public String cabinetId;
        public String keyword;
        public int offset;
        public int limit;
    }

    /**
     * 166-查询站点订单
     */
    @ResponseBody
    @RequestMapping(value = "/query_cabinet_order.htm")
    public RestResult queryCabinetOrder(@Valid @RequestBody QueryCabinetOrderParam param) {
        int agentId = getTokenData().agentId;
        List<Map> result = new ArrayList<Map>();
        Map data = new HashMap();
        List<BatteryOrder> list = batteryOrderService.findByCabinetId(agentId, param.cabinetId, param.offset, param.limit);
        int orderCount = 0;
        int activeUserCount = 0;

        if(StringUtils.isNotEmpty(param.cabinetId)){
            CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.find(param.cabinetId, agentId);
            if (cabinetTotalStats != null) {
                orderCount = cabinetTotalStats.getOrderCount();
                int foregiftCount = cabinetTotalStats.getForegiftCount() != null ? cabinetTotalStats.getForegiftCount() : 0;
                int refundForegiftCount = cabinetTotalStats.getRefundForegiftCount() != null ? cabinetTotalStats.getRefundForegiftCount() : 0;
                activeUserCount = foregiftCount - refundForegiftCount;
            }
        }else{
            CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.sumAll(agentId);
            if (cabinetTotalStats != null) {
                orderCount = cabinetTotalStats.getOrderCount();
                int foregiftCount = cabinetTotalStats.getForegiftCount() != null ? cabinetTotalStats.getForegiftCount() : 0;
                int refundForegiftCount = cabinetTotalStats.getRefundForegiftCount() != null ? cabinetTotalStats.getRefundForegiftCount() : 0;
                activeUserCount = foregiftCount - refundForegiftCount;
            }
        }

        for (BatteryOrder batteryOrder : list) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.put("customerFullname", batteryOrder.getCustomerFullname());
            notNullMap.put("customerMobile", batteryOrder.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            notNullMap.put("orderStatus", batteryOrder.getOrderStatus());
            notNullMap.put("batteryId", batteryOrder.getBatteryId());
            notNullMap.put("batteryType", batteryOrder.getBatteryTypeName());
            notNullMap.put("initVolume", batteryOrder.getInitVolume());
            notNullMap.put("currentVolume", batteryOrder.getCurrentVolume());
            notNullMap.put("currentDistance", batteryOrder.getCurrentDistance());
            notNullMap.put("takeTime", DateFormatUtils.format(batteryOrder.getTakeTime(), Constant.DATE_TIME_FORMAT));
            result.add(notNullMap);
        }
        data.put("orderCount", orderCount);
        data.put("activeUserCount", activeUserCount);
        data.put("list", result);
        return RestResult.dataResult(0, null, data);


    }



    /**
     * 167-查询站点电费记录
     */
    @ResponseBody
    @RequestMapping(value = "/query_cabinet_degree_record.htm")
    public RestResult queryCabinetDegreeRecord(@Valid @RequestBody QueryCabinetOrderParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        Cabinet cabinet = null;
        List<CabinetDayDegreeStats> list = null;
        if (StringUtils.isNotEmpty(param.cabinetId)) {
            cabinet = cabinetService.find(param.cabinetId);
            if (cabinet == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该换电柜不存在");
            }
            list = cabinetDayDegreeStatsService.findList(agentId, param.cabinetId, param.offset, param.limit);
        } else {
            list = cabinetDayDegreeStatsService.findList(agentId, null, param.offset, param.limit);
        }
        int totalPrice = 0;
        int totalDegreeNum = 0;
        NotNullMap data = new NotNullMap();
        List<Map> result = new ArrayList<Map>();
        for (CabinetDayDegreeStats stats : list) {
            Map line = new HashMap();
            line.put("statsDate", stats.getStatsDate());
            line.put("beginTime", DateFormatUtils.format(stats.getBeginTime(), Constant.DATE_TIME_FORMAT));
            line.put("endTime", DateFormatUtils.format(stats.getEndTime(), Constant.DATE_TIME_FORMAT));
            line.put("beginNum", stats.getBeginNum());
            line.put("endNum", stats.getEndNum());
            line.put("num", stats.getNum());
            cabinet = cabinetService.find(stats.getCabinetId());
            line.put("price", cabinet == null || cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100 * stats.getNum());
            CabinetDayStats cabinetDayStats = cabinetDayStatsService.findForCabinet(cabinet.getId(), stats.getStatsDate());
            if (cabinetDayStats != null) {
                line.put("orderCount", cabinetDayStats.getOrderCount());
                if(cabinetDayStats.getActiveCustomerCount() != 0){
                    line.put("activeCustomerCount", cabinetDayStats.getActiveCustomerCount());
                    line.put("referencePrice", (cabinet.getPrice() == null ? 0 : cabinet.getPrice() * 100 * stats.getNum())/cabinetDayStats.getActiveCustomerCount());
                } else {
                    line.put("activeCustomerCount", 0);
                    line.put("referencePrice", 0);
                }
            } else {
                line.put("orderCount", 0);
                line.put("referencePrice", 0);
                line.put("activeCustomerCount", 0);
            }
            result.add(line);
        }

        if(StringUtils.isNotEmpty(param.cabinetId)){
            CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.find(param.cabinetId, agentId);
            if (cabinetTotalStats != null) {
                totalPrice =cabinetTotalStats.getElectricPrice();
                totalDegreeNum = cabinetTotalStats.getElectricDegree();
            }
        }else{
            CabinetTotalStats cabinetTotalStats = cabinetTotalStatsService.sumAll(agentId);
            if (cabinetTotalStats != null) {
                totalPrice =cabinetTotalStats.getElectricPrice();
                totalDegreeNum = cabinetTotalStats.getElectricDegree();
            }
        }

        data.put("unitPrice", cabinet == null ? 0 : cabinet.getPrice() * 100);
        data.put("totalPrice", totalPrice);
        data.put("totalDegreeNum", totalDegreeNum);
        data.put("degreeList", result);
        return RestResult.dataResult(0, null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InOutCustomerShopListParam {
        public String keyword;
        public int offset;
        public int limit;
    }
    /**
     * 168-查询门店用户在网退网列表
     *
     **/
    @ResponseBody
    @RequestMapping(value = "/in_out_customer_shop_list")
    public RestResult inOutCustomerShopList(@Valid @RequestBody InOutCustomerShopListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<Shop> list = shopService.findList(agentId, param.keyword, param.offset, param.limit);
        NotNullMap data = new NotNullMap();
        List<Map> result = new ArrayList<Map>();
        List<Integer> inCustomerStatus = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
        List<Integer> outCustomerCountStatus = Arrays.asList(RentForegiftOrder.Status.REFUND_SUCCESS.getValue());

        int inCount = rentForegiftOrderService.findCountByShopId(null, agentId, inCustomerStatus, null, null);
        int outCount = rentForegiftOrderService.findCountByShopId(null, agentId, outCustomerCountStatus, null, null);
        int totalInCustomerCount = inCount;
        int totalOutCustomerCount = outCount;

        for (Shop shop : list) {
            Map line = new HashMap();
            line.put("id", shop.getId());
            line.put("shopName", shop.getShopName());
            int inCustomerCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, inCustomerStatus, null, null);
            int outCustomerCount = rentForegiftOrderService.findCountByShopId(shop.getId(), agentId, outCustomerCountStatus, null, null);
            line.put("inCustomerCount", inCustomerCount);
            line.put("outCustomerCount", outCustomerCount);
            line.put("totalCount", inCustomerCount + outCustomerCount);
            result.add(line);
        }

        data.put("totalInCustomerCount", totalInCustomerCount);
        data.put("totalOutCustomerCount", totalOutCustomerCount);
        data.put("customerList", result);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryShopCustomerParam {
        public String shopId;
        public String keyword;
        public int type;
        public int offset;
        public int limit;
    }
    /**
     * 169-查询门店用户
     */
    @ResponseBody
    @RequestMapping(value = "/query_shop_customer.htm")
    public RestResult queryShopCustomer(@Valid @RequestBody QueryShopCustomerParam param) {
        int agentId = getTokenData().agentId;
        Map data = new HashMap();
        List<Map> list = new ArrayList<Map>();

        List<Integer> allCountStatus = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue(), RentForegiftOrder.Status.REFUND_SUCCESS.getValue());
        List<Integer> inCustomerStatus = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
        List<Integer> outCustomerCountStatus = Arrays.asList(RentForegiftOrder.Status.REFUND_SUCCESS.getValue());

        List<RentForegiftOrder> allList = rentForegiftOrderService.findByShopId(agentId, param.shopId, allCountStatus, param.keyword, param.offset, param.limit);
        List<RentForegiftOrder> inList = rentForegiftOrderService.findByShopId(agentId, param.shopId, inCustomerStatus, param.keyword, param.offset, param.limit);
        List<RentForegiftOrder> outList = rentForegiftOrderService.findByShopId(agentId, param.shopId, outCustomerCountStatus, param.keyword, param.offset, param.limit);

        int inCustomerCount = rentForegiftOrderService.findCountByShopId(param.shopId, agentId, inCustomerStatus, null, null);
        int outCustomerCount = rentForegiftOrderService.findCountByShopId(param.shopId, agentId, outCustomerCountStatus, null, null);
        int inCount = inCustomerCount;
        int outCount = outCustomerCount;
        int foregiftCount = inCustomerCount + outCustomerCount;

        if (param.type == 1) {
            for (RentForegiftOrder cfo : allList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.put("customerFullname", cfo.getCustomerFullname());
                notNullMap.put("customerMobile", cfo.getCustomerMobile());
                notNullMap.put("shopName", shopService.find(cfo.getShopId()).getShopName());
                notNullMap.put("money", cfo.getMoney());
                RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.findOneEnabled(cfo.getCustomerId(), agentId);
                if (rentPeriodOrder != null) {
                    notNullMap.putInteger("price", rentPeriodOrder.getMoney() != null ? rentPeriodOrder.getMoney() : 0);
                    notNullMap.putDateTime("endTime", rentPeriodOrder.getEndTime());
                    notNullMap.putLong("dayCount", rentPeriodOrder.getEndTime() != null ?  InstallUtils.getRestDay(rentPeriodOrder.getEndTime()) : 0);
                } else {
                    notNullMap.putInteger("price", 0);
                    notNullMap.putString("endTime", "");
                    notNullMap.putInteger("dayCount", 0);
                }
                notNullMap.put("date", DateFormatUtils.format(cfo.getCreateTime(), Constant.DATE_FORMAT));
                list.add(notNullMap);
            }
        } else if (param.type == 2) {
            for (RentForegiftOrder cfo : inList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.put("customerFullname", cfo.getCustomerFullname());
                notNullMap.put("customerMobile", cfo.getCustomerMobile());
                notNullMap.put("shopName", shopService.find(cfo.getShopId()).getShopName());
                notNullMap.put("money", cfo.getMoney());
                RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.findOneEnabled(cfo.getCustomerId(), agentId);
                if (rentPeriodOrder != null) {
                    notNullMap.putInteger("price", rentPeriodOrder.getMoney() != null ? rentPeriodOrder.getMoney() : 0);
                    notNullMap.putDateTime("endTime", rentPeriodOrder.getEndTime());
                    notNullMap.putLong("dayCount", rentPeriodOrder.getEndTime() != null ?  InstallUtils.getRestDay(rentPeriodOrder.getEndTime()) : 0);
                } else {
                    notNullMap.putInteger("price", 0);
                    notNullMap.putString("endTime", "");
                    notNullMap.putInteger("dayCount", 0);
                }
                notNullMap.put("date", DateFormatUtils.format(cfo.getCreateTime(), Constant.DATE_FORMAT));
                list.add(notNullMap);
            }
        } else if (param.type == 3) {
            for (RentForegiftOrder cfo : outList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.put("customerFullname", cfo.getCustomerFullname());
                notNullMap.put("customerMobile", cfo.getCustomerMobile());
                notNullMap.put("shopName", shopService.find(cfo.getShopId()).getShopName());
                notNullMap.put("money", cfo.getMoney());
                RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.findOneEnabled(cfo.getCustomerId(), agentId);
                if (rentPeriodOrder != null) {
                    notNullMap.putInteger("price", rentPeriodOrder.getMoney() != null ? rentPeriodOrder.getMoney() : 0);
                    notNullMap.putDateTime("endTime", rentPeriodOrder.getEndTime());
                    notNullMap.putLong("dayCount", rentPeriodOrder.getEndTime() != null ?  InstallUtils.getRestDay(rentPeriodOrder.getEndTime()) : 0);
                } else {
                    notNullMap.putInteger("price", 0);
                    notNullMap.putString("endTime", "");
                    notNullMap.putInteger("dayCount", 0);
                }
                notNullMap.put("date", DateFormatUtils.format(cfo.getCreateTime(), Constant.DATE_FORMAT));
                list.add(notNullMap);
            }
        }
        data.put("signInCount", foregiftCount);
        data.put("inCount", inCount);
        data.put("outCount", outCount);
        data.put("list", list);
        return RestResult.dataResult(0, null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QueryShopOrderParam {
        public String shopId;
        public String keyword;
        public int offset;
        public int limit;
    }

    /**
     * 170-查询门店订单
     */
    @ResponseBody
    @RequestMapping(value = "/query_shop_order.htm")
    public RestResult queryShopOrder(@Valid @RequestBody QueryShopOrderParam param) {
        int agentId = getTokenData().agentId;
        List<Map> result = new ArrayList<Map>();
        Map data = new HashMap();
        List<RentOrder> list = rentOrderService.findListByShop(agentId, param.shopId, null, param.offset, param.limit);

        int orderCount = 0;
        int activeUserCount = 0;
        if(StringUtils.isNotEmpty(param.shopId)){
            ShopTotalStats shopTotalStats = shopTotalStatsService.find(param.shopId, ConstEnum.Category.RENT.getValue(), agentId);
            if (shopTotalStats != null) {
                orderCount = shopTotalStats.getAgentPacketPeriodCount();
                int foregiftCount = shopTotalStats.getAgentForegiftCount() != null ? shopTotalStats.getAgentForegiftCount() : 0;
                int refundForegiftCount = shopTotalStats.getAgentRefundForegiftCount() != null ? shopTotalStats.getAgentRefundForegiftCount() : 0;
                activeUserCount = foregiftCount - refundForegiftCount;
            }
        }else{
            ShopTotalStats shopTotalStats = shopTotalStatsService.sumAll(agentId);
            if (shopTotalStats != null) {
                orderCount = shopTotalStats.getAgentPacketPeriodCount();
                int foregiftCount = shopTotalStats.getAgentForegiftCount() != null ? shopTotalStats.getAgentForegiftCount() : 0;
                int refundForegiftCount = shopTotalStats.getAgentRefundForegiftCount() != null ? shopTotalStats.getAgentRefundForegiftCount() : 0;
                activeUserCount = foregiftCount - refundForegiftCount;
            }
        }

        for (RentOrder rentOrder : list) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.putString("customerFullname", rentOrder.getCustomerFullname());
            notNullMap.putString("customerMobile", rentOrder.getCustomerMobile());
            notNullMap.putString("batteryId", rentOrder.getBatteryId());
            notNullMap.putString("batteryTypeName", rentOrder.getBatteryTypeName());
            notNullMap.putInteger("currentVolume", rentOrder.getCurrentVolume());
            notNullMap.putInteger("currentDistance", rentOrder.getCurrentDistance());
            notNullMap.putInteger("status", rentOrder.getStatus());
            notNullMap.putDateTime("createTime", rentOrder.getCreateTime());
            result.add(notNullMap);
        }
        data.put("orderCount", orderCount);
        data.put("activeUserCount", activeUserCount);
        data.put("list", result);
        return RestResult.dataResult(0, null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShopStoreBatteryListParam {
        public String keyword;
        public int offset;
        public int limit;
        public int areaId;
        public double lng;
        public double lat;
    }

    /**
     * 171-查询门店库存使用电池列表
     *
     */
    @ResponseBody
    @RequestMapping(value = "/query_shop_battery_list.htm")
    public RestResult list(@Valid @RequestBody ShopStoreBatteryListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;

        Area area = areaCache.get(param.areaId);
        if(area == null) {
            area = areaCache.get(param.areaId / 100 * 100);
            if (area == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }

        Area province = null, city = null;
        if(area.getAreaLevel() == 3) {
            city = areaCache.get(area.getParentId());
        } else if(area.getAreaLevel() == 2) {
            city = area;
        } else if(area.getAreaLevel() == 1) {
            province = area;
        }

        if (city != null) {
            province = areaCache.get(city.getParentId());
            if(province == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }
        if (province == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        if (param == null || param.lng == 0 || param.lat == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "经纬度错误");
        }

        List<Shop> list = shopService.findByAgentId(tokenData.agentId, param.keyword, param.lat, param.lng, param.offset, param.limit);

        List<Map> result = new ArrayList<Map>();
        for (Shop shop : list) {
            Map line = new HashMap();
            line.put("id", shop.getId());
            line.put("shopName", shop.getShopName());
            line.put("address", shop.getAddress());
            line.put("distance", shop.getDistance());
            //客户使用中
            int useCount = batteryService.shopCustomerUseCount(agentId, shop.getId());
            //门店中
            int storeCount = batteryService.shopStoreCount(agentId, shop.getId());
            line.put("useCount", useCount);
            line.put("storeCount", storeCount);
            line.put("totalCount", storeCount + useCount);
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }


    public static class QueryCabinetStatsParam {
        public String keyword;
        public String day;
        public int offset;
        public int limit;
    }

    /**
     * 92-查询运营商日统计设备列表
     */
    @ResponseBody
    @RequestMapping("/query_cabinet_stats.htm")
    public RestResult queryCabinetStats(@RequestBody QueryCabinetStatsParam param) throws IOException {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        NotNullMap data = new NotNullMap();

        List<CabinetMonthStats> statsList = cabinetMonthStatsService.findByCabinetList(agentId, null);

        double foregiftMoney = 0;
        double foregiftRefundMoney = 0;
        double packetPeriodMoney = 0;
        double refundPacketPeriodMoney = 0;

        for (CabinetMonthStats e : statsList){
            foregiftMoney = foregiftMoney + e.getForegiftMoney();
            foregiftRefundMoney = foregiftRefundMoney + e.getRefundForegiftMoney();
            packetPeriodMoney = packetPeriodMoney + e.getAgentPacketPeriodMoney();
            refundPacketPeriodMoney = refundPacketPeriodMoney + e.getAgentRefundPacketPeriodMoney();
        }

        CabinetDayDegreeStats totalElectricPrice = cabinetDayDegreeStatsService.findForAgent(agentId, null, null);

        data.put("totalForegiftMoney", (int)foregiftMoney - foregiftRefundMoney);
        data.put("totalPacketPeriodMoney", (int)packetPeriodMoney - refundPacketPeriodMoney);
        if (totalElectricPrice != null) {
            data.put("totalElectricPrice", totalElectricPrice.getPrice() * 100);
        } else {
            data.put("totalElectricPrice", 0);
        }
        CabinetDayStats cabinetDay = cabinetDayStatsService.findTotalByStats(agentId, param.day);
        if(cabinetDay != null){
            data.put("totalOrderCount", cabinetDay.getOrderCount());
            data.put("totalActiveCount", cabinetDay.getActiveCustomerCount());
        } else {
            data.put("totalOrderCount", 0);
            data.put("totalActiveCount", 0);
        }

        List<CabinetDayStats> cabinetDayStatsList = cabinetDayStatsService.findForStats(agentId, param.day, param.keyword, param.offset, param.limit);

        List<NotNullMap> list = new ArrayList<NotNullMap>();
        for (CabinetDayStats stats : cabinetDayStatsList) {
            NotNullMap notNullMap = new NotNullMap();

            Cabinet cabinet = cabinetService.find(stats.getCabinetId());

            notNullMap.put("cabinetId", stats.getCabinetId());
            notNullMap.put("cabinetName", cabinet.getCabinetName());
            notNullMap.put("foregiftMoney", stats.getForegiftMoney());
            notNullMap.put("packetPeriodMoney", stats.getAgentPacketPeriodMoney());

            //电费
            CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findLast(stats.getCabinetId());
            if(cabinetDayDegreeStats != null){
                notNullMap.put("price", stats.getUnitPrice());
                notNullMap.put("useVolume", cabinetDayDegreeStats.getNum());
                notNullMap.put("powerPrice", stats.getUnitPrice() * cabinetDayDegreeStats.getNum());
            }else{
                notNullMap.put("useVolume",0);
                notNullMap.put("price",0);
                notNullMap.put("powerPrice",0);
            }

            notNullMap.put("activeCustomerCount", stats.getActiveCustomerCount());
            notNullMap.put("foregiftCount", stats.getForegiftCount());
            notNullMap.put("orderCount", stats.getOrderCount());
            notNullMap.put("refundPacketPeriodMoney", stats.getAgentRefundPacketPeriodMoney());
            notNullMap.put("exchangeMoney", stats.getExchangeMoney());
            notNullMap.put("foregiftRefundMoney", stats.getRefundForegiftMoney());

            list.add(notNullMap);
        }
        data.put("cabinetList", list);


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);

    }
}
