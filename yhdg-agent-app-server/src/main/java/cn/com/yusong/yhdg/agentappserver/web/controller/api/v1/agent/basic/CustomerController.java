package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller("agent_api_v1_agent_basic_customer")
@RequestMapping(value = "/agent_api/v1/agent/basic/customer")
public class CustomerController extends ApiController {
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerService customerService;
    @Autowired
    UserService userService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryParameterService batteryParameterService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    InsuranceOrderService insuranceOrderService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerPayTrackService customerPayTrackService;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentMonthStatsService agentMonthStatsService;
    @Autowired
    AgentDayStatsService agentDayStatsService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public long customerId;
    }

    /**
     * 16-查询客户电池信息
     * <p>
     */
    @ResponseBody
    @RequestMapping(value = "/customer_battery_info.htm")
    public RestResult customerBatteryInfo(@Valid @RequestBody ListParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Customer customer = customerService.find(param.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        NotNullMap data = new NotNullMap();

        data.putLong("id", customer.getId());
        data.putString("fullname", customer.getFullname());
        data.putString("photoPath", staticImagePath(customer.getPhotoPath()));
        data.putString("mobile", customer.getMobile());
        if (customer.getIdCard() != null) {
            data.putString("idCard", customer.getIdCard().replaceAll("(\\d{4})\\d{10}(\\d{4})","$1$2****"));
        } else {
            data.putString("idCard", "");
        }
        data.putDate("createTime", customer.getCreateTime());

        String leavingDay = null;
        long now = System.currentTimeMillis();
        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findLastEndTime(customer.getId());
        if (packetPeriodOrder != null) {
            if (now < packetPeriodOrder.getEndTime().getTime()) {
                leavingDay = AppUtils.formatTimeUnit((packetPeriodOrder.getEndTime().getTime() - now) / 1000);
            }
        }


        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
        InsuranceOrder insuranceOrder = null;
        if (customerExchangeInfo != null) {
            data.putInteger("foregiftMoney", customerExchangeInfo.getForegift());
            Integer batteryType = customerExchangeInfo.getBatteryType();
            insuranceOrder = insuranceOrderService.findByCustomerId(customer.getId(), batteryType, InsuranceOrder.Status.PAID.getValue());
            data.putInteger("insuranceMoney", insuranceOrder != null ? insuranceOrder.getPrice() : 0);
            data.putInteger("insurancePaid", insuranceOrder != null ? insuranceOrder.getPaid() : 0);
            data.putInteger("monthCount", insuranceOrder != null ? insuranceOrder.getMonthCount() : 0);
        } else {
            data.putInteger("foregiftMoney", 0);
            data.putInteger("insuranceMoney", 0);
            data.putInteger("insurancePaid", 0);
            data.putInteger("monthCount", 0);
        }
        if (packetPeriodOrder!= null) {
            data.putInteger("packetPeriodMoney", packetPeriodOrder.getPrice());
            data.putInteger("dayCount", packetPeriodOrder.getDayCount());
            data.putDate("beginTime", packetPeriodOrder.getBeginTime());
            data.putDate("endTime", packetPeriodOrder.getEndTime());
            data.putString("leavingDay", leavingDay);
        } else {
            data.putInteger("packetPeriodMoney", 0);
            data.putInteger("dayCount", 0);
            data.putDate("beginTime", null);
            data.putDate("endTime", null);
            data.putString("leavingDay", "");
        }

        data.putInteger("customerTicketCount", customerCouponTicketService.findCount(agentId, customer.getMobile(), null));
        data.putInteger("foregiftTicketCount", customerCouponTicketService.findCount(agentId, customer.getMobile(), CustomerCouponTicket.TicketType.FOREGIFT.getValue()));
        data.putInteger("packetTicketCount", customerCouponTicketService.findCount(agentId, customer.getMobile(), CustomerCouponTicket.TicketType.RENT.getValue()));
        data.putInteger("deductionTicketCount", customerCouponTicketService.findCount(agentId, customer.getMobile(), CustomerCouponTicket.TicketType.DEDUCTION.getValue()));

        List<CustomerExchangeBattery> orderList = customerExchangeBatteryService.findByCustomerId(param.customerId);
        List<NotNullMap> batteryList = new ArrayList<NotNullMap>();
        for (CustomerExchangeBattery customerExchangeBattery : orderList) {
            NotNullMap notNullMap = new NotNullMap();

            Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
            if (battery != null) {
                BatteryParameter batteryParameter = batteryParameterService.find(battery.getId());

                notNullMap.put("batteryId", battery.getId());
                notNullMap.put("status", battery.getStatus());
                notNullMap.put("shellCode", battery.getShellCode());
                notNullMap.put("code", battery.getCode());
                notNullMap.put("lng", battery.getLng());
                notNullMap.put("lat", battery.getLat());
                notNullMap.put("volume", battery.getVolume());
                if (batteryParameter != null && batteryParameter.getVoltage() != null) {
                    notNullMap.put("voltage", batteryParameter.getVoltage()/1000);
                } else {
                    notNullMap.put("voltage", 0);
                }
                if (batteryParameter != null && batteryParameter.getElectricity() != null) {
                    notNullMap.put("electricity", batteryParameter.getElectricity()/1000);
                } else {
                    notNullMap.put("electricity", 0);
                }
                Integer designMileage;
                if (customer.getAgentId() != null && customer.getAgentId() != 0) {
                    designMileage = Integer.parseInt(agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue(), customer.getAgentId()));
                } else {
                    designMileage = Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue()));
                }
                notNullMap.put("estimateDistance", designMileage * battery.getVolume() / 100);//预计行驶里程
                String statusName = Battery.Status.getName(battery.getStatus());
                if (battery.getChargeStatus() != null && Battery.ChargeStatus.CHARGING.getValue() == battery.getChargeStatus()) {
                    statusName += "/充电中";
                }
                notNullMap.put("statusName", statusName);

            }

            BatteryOrder batteryOrder = batteryOrderService.find(customerExchangeBattery.getBatteryOrderId());

            notNullMap.putString("takeCabinetId", batteryOrder.getTakeCabinetId());
            notNullMap.putString("takeCabinetName", batteryOrder.getTakeCabinetName());
            notNullMap.putString("takeBoxNum", batteryOrder.getTakeBoxNum());
            notNullMap.putDateTime("takeTime", batteryOrder.getTakeTime());

            notNullMap.putString("putCabinetId", batteryOrder.getPutCabinetId());
            notNullMap.putString("putCabinetName", batteryOrder.getPutCabinetName());
            notNullMap.putString("putBoxNum", batteryOrder.getPutBoxNum());
            notNullMap.putDateTime("putTime", batteryOrder.getPutTime());

            notNullMap.putInteger("beginVolume", batteryOrder.getInitVolume());
            notNullMap.putInteger("endVolume", batteryOrder.getCurrentVolume());

            batteryList.add(notNullMap);
        }
        data.put("batteryList", batteryList);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PayTrackListParam {
        public Long customerId;
        public int offset;
        public int limit;
    }

    /**
     * 27-查询客户消费轨迹
     * <p>
     */
    @ResponseBody
    @RequestMapping("/pay_track_list.htm")
    public RestResult list(@Valid @RequestBody PayTrackListParam param) {
        TokenCache.Data tokenData = getTokenData();
        return customerPayTrackService.findList(tokenData.agentId, param.customerId, param.offset, param.limit);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerListParam {
        public String mobile;
        public int offset;
        public int limit;
    }

    /**
     * 28-查询客户列表
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customer_list.htm")
    public RestResult customerList(@RequestBody CustomerListParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        //按交押金的时间倒序排列，没交过押金的排在最后
        List<Customer> list = customerService.findListOrderByForegift(agentId, param.mobile, param.offset, param.limit);
        NotNullMap data = new NotNullMap();

        List<Map> result = new ArrayList<Map>();
        for (Customer customer : list) {
            //租金处理
            Long packetRemainTime = null;
            Integer rentMoney = 0;
            Date endTime = null;
            long now = System.currentTimeMillis();

            NotNullMap notNullMap = new NotNullMap();
            PacketPeriodOrder lastEndTime = packetPeriodOrderService.findLastEndTime(customer.getId());
            List<PacketPeriodOrder> noUsedList = packetPeriodOrderService.findListByNoUsed(customer.getId());
            if (lastEndTime != null) {
                if (now < lastEndTime.getEndTime().getTime()) {
                    //packetRemainTime = AppUtils.formatTimeUnit((lastEndTime.getEndTime().getTime() - now) / 1000);
                    packetRemainTime = (lastEndTime.getEndTime().getTime() - now) / 1000;
                    rentMoney = lastEndTime.getMoney();
                    endTime = lastEndTime.getEndTime();
                }
            }
            if(noUsedList.size() > 0){
                for(PacketPeriodOrder order : noUsedList){
                    if(packetRemainTime != null){
                        packetRemainTime += order.getDayCount() * 24 * 3600 * 1l;
                        rentMoney += order.getMoney();
                        endTime = DateUtils.addDays(endTime,order.getDayCount());
                    }else{
                        packetRemainTime = order.getDayCount() * 24 * 3600 * 1l;
                        rentMoney = order.getMoney();
                        endTime = DateUtils.addDays(new Date(),order.getDayCount());
                    }
                }
            }
            if(packetRemainTime != null){
                notNullMap.put("endTime", DateFormatUtils.format(endTime, Constant.DATE_TIME_FORMAT));
                String restDay = AppUtils.formatTimeUnit(packetRemainTime);
                String dayNumString = restDay.replace("天", "");
                Integer rentRestDay = Integer.parseInt(dayNumString);
                notNullMap.put("rentRestDay", rentRestDay);
                notNullMap.put("rentMoney", rentMoney);
            }else{
                notNullMap.put("endTime", "");
                notNullMap.put("rentRestDay", 0);
                notNullMap.put("rentMoney", 0);
            }
            notNullMap.putLong("id", customer.getId());
            notNullMap.putString("fullname", customer.getFullname());
            notNullMap.putMobileMask("mobileMask", customer.getMobile());
            notNullMap.put("mobile", customer.getMobile());
            if (customer.getHdPaidForegift() != null) {
                notNullMap.putInteger("foregiftMoney", customer.getHdPaidForegift());
            }else {
                notNullMap.putInteger("foregiftMoney", 0);
            }

            notNullMap.putMobileMask("laxinMobile", customer.getLaxinMobile() != null ? customer.getLaxinMobile() : "");
            notNullMap.putString("laxinFullname", customer.getLaxinFullname() != null ? customer.getLaxinFullname() : "");
            if (customer.getBelongCabinetId() != null) {
               Cabinet cabinet = cabinetMapper.find(customer.getBelongCabinetId());
               notNullMap.putString("cabinetName", cabinet.getCabinetName());
            } else {
                notNullMap.putString("cabinetName", "");
            }
            result.add(notNullMap);
        }


        data.putInteger("customerCount", customerService.findCustomerCount(agentId));
        data.putInteger("foregiftCount", customerExchangeInfoService.findCountByAgentId(agentId));
        AgentMonthStats agentMonthStats = agentMonthStatsService.findTotal(agentId, ConstEnum.Category.EXCHANGE.getValue());
        if (agentMonthStats != null) {
            Integer totalRefundCount = agentMonthStats.getRefundPacketPeriodOrderCount() + agentMonthStats.getForegiftRefundCount() + agentMonthStats.getInsuranceRefundCount();
            data.putInteger("totalRefundcount", totalRefundCount);
        } else {
            data.putInteger("totalRefundcount", 0);
        }
        String today = DateFormatUtils.format(new Date().getTime(), Constant.DATE_FORMAT);
        //默认取换电
        AgentDayStats agentDayStats = agentDayStatsService.find(agentId, today, ConstEnum.Category.EXCHANGE.getValue());
        if (agentDayStats != null) {
            data.putInteger("todayForegiftCount", agentDayStats.getForegiftCount());
        } else {
            data.putInteger("todayForegiftCount", 0);
        }
        String statsMonth = DateFormatUtils.format(new Date(), Constant.MONTH_FORMAT);
        AgentMonthStats agentMonth = agentMonthStatsService.find(agentId, statsMonth, ConstEnum.Category.EXCHANGE.getValue());
        if (agentMonth != null) {
            data.putInteger("monthForegiftCount", agentMonth.getForegiftCount());
            Integer monthRefundCount = agentMonth.getRefundPacketPeriodOrderCount() + agentMonth.getForegiftRefundCount() + agentMonth.getInsuranceRefundCount();
            data.putInteger("monthRefundcount", monthRefundCount);
        } else {
            data.putInteger("monthForegiftCount",0);
            data.putInteger("monthRefundcount",0);
        }

        data.put("customerList", result);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @ResponseBody
    @RequestMapping("/will_expire_list.htm")
    public RestResult WillExpireList() {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        return packetPeriodOrderService.willExpireList(agentId);
    }
}
