package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;

import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@Controller("api_v1_customer_hdg_active_packet_period_price")
@RequestMapping(value = "/api/v1/customer/hdg/packet_period_price")
public class PacketPeriodPriceController extends ApiController {

    static final Logger log = LogManager.getLogger(PacketPeriodPriceController.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    PacketPeriodPriceService packetPeriodPriceService;
    @Autowired
    PacketPeriodPriceRenewService packetPeriodPriceRenewService;
    @Autowired
    VipPacketPeriodPriceRenewService vipPacketPeriodPriceRenewService;
    @Autowired
    PacketPeriodActivityService packetPeriodActivityService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    VipExchangeBatteryForegiftService vipExchangeBatteryForegiftService;
    @Autowired
    VipPriceService vipPriceService;
    @Autowired
    VipPacketPeriodPriceService vipPacketPeriodPriceService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    CabinetBatteryTypeService cabinetBatteryTypeService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    ExchangeBatteryForegiftService exchangeBatteryForegiftService;
    @Autowired
    AgentCompanyCustomerService agentCompanyCustomerService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String stationId;
        public String cabinetId;
        public String batteryId;
        @NotNull
        public int agentId;
        @NotNull
        public int batteryType;
        @NotNull
        public Long foregiftId;
    }

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer =  customerService.find(customerId);

        //查询套餐金额
        List<Map> list = new ArrayList<Map>();

        VipPrice vipPrice = null;
        VipPacketPeriodPrice vipPacketPeriodPrice = null;

        if (StringUtils.isNotEmpty(param.batteryId)) {
            vipPrice = vipPriceService.findOneByCustomerMobile(param.agentId, param.batteryType, customer.getMobile());
            if (vipPrice != null) {
                List<VipPacketPeriodPrice> packetPeriodPriceList = vipPacketPeriodPriceService.findByPriceIdAndForegiftId(vipPrice.getId(), param.foregiftId);
                if (packetPeriodPriceList.size() > 0) {
                    for (VipPacketPeriodPrice vipPacketPeriod : packetPeriodPriceList) {
                        vipPacketPeriodPrice = vipPacketPeriod;
                    }
                }
            }

        } else if (StringUtils.isNotEmpty(param.cabinetId)) {
            vipPrice = vipPriceService.findOneByCustomerMobile(param.agentId, param.batteryType, customer.getMobile());
            if (vipPrice == null) {
                vipPrice = vipPriceService.findOneByCabinetId(param.agentId, param.batteryType, param.cabinetId);
            }
            if (vipPrice == null) {
                vipPrice = vipPriceService.findOneByStationId(param.agentId, param.batteryType, param.stationId);
            }
            if (vipPrice != null) {
                List<VipPacketPeriodPrice> packetPeriodPriceList = vipPacketPeriodPriceService.findByPriceIdAndForegiftId(vipPrice.getId(), param.foregiftId);
                if (packetPeriodPriceList.size() > 0) {
                    for (VipPacketPeriodPrice vipPacketPeriod : packetPeriodPriceList) {
                        vipPacketPeriodPrice = vipPacketPeriod;
                    }
                }
            }
        }

        if (vipPacketPeriodPrice == null) {
            //首次租金列表 续租租金列表 如果没有续租使用首次
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
            if (customerExchangeInfo != null) {
                //用户已购买电池押金 查询续租租金列表
                List<PacketPeriodPriceRenew> packetPeriodPriceRenewList = packetPeriodPriceRenewService.findList(param.agentId, param.batteryType,  param.foregiftId);
                if (packetPeriodPriceRenewList.size() > 0) {
                    for (PacketPeriodPriceRenew renew : packetPeriodPriceRenewList) {
                        Map map = new HashMap();
                        map.put("id", renew.getId());
                        map.put("activityId", 0);
                        map.put("vipId", 0);
                        map.put("dayCount", renew.getDayCount());
                        map.put("price", renew.getPrice());
                        map.put("limitCount", renew.getLimitCount());
                        map.put("isTicket", renew.getIsTicket());
                        map.put("dayLimitCount", renew.getDayLimitCount());
                        map.put("memo", renew.getMemo());
                        list.add(map);
                    }
                } else {
                    //如果没有续租使用首次
                    List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findList(param.agentId, param.batteryType,  param.foregiftId);
                    for (PacketPeriodPrice e : packetPeriodPriceList) {
                        Map map = new HashMap();
                        map.put("id", e.getId());
                        map.put("activityId", 0);
                        map.put("vipId", 0);
                        map.put("dayCount", e.getDayCount());
                        map.put("price", e.getPrice());
                        map.put("isTicket", e.getIsTicket());
                        map.put("limitCount", e.getLimitCount());
                        map.put("dayLimitCount", e.getDayLimitCount());
                        map.put("memo", e.getMemo());
                        list.add(map);
                    }
                }
            } else {
                List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findList(param.agentId, param.batteryType,  param.foregiftId);
                for (PacketPeriodPrice e : packetPeriodPriceList) {
                    Map map = new HashMap();
                    map.put("id", e.getId());
                    map.put("activityId", 0);
                    map.put("vipId", 0);
                    map.put("dayCount", e.getDayCount());
                    map.put("price", e.getPrice());
                    map.put("isTicket", e.getIsTicket());
                    map.put("limitCount", e.getLimitCount());
                    map.put("dayLimitCount", e.getDayLimitCount());
                    map.put("memo", e.getMemo());
                    list.add(map);
                }
            }
        } else {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
            if (customerExchangeInfo != null) {
                //用户已购买电池押金 查询续租租金列表
                List<VipPacketPeriodPriceRenew> vipPacketPeriodPriceRenewList = vipPacketPeriodPriceRenewService.findList(param.agentId, param.batteryType,  param.foregiftId);
                if (vipPacketPeriodPriceRenewList.size() > 0) {
                    for (VipPacketPeriodPriceRenew vipRenew : vipPacketPeriodPriceRenewList) {
                        Map map = new HashMap();
                        map.put("id", 0);
                        map.put("activityId", 0);
                        map.put("vipId", vipRenew.getId());
                        map.put("dayCount", vipRenew.getDayCount());
                        map.put("price", vipRenew.getPrice());
                        map.put("isTicket", vipRenew.getIsTicket());
                        map.put("limitCount", vipRenew.getLimitCount());
                        map.put("dayLimitCount", vipRenew.getDayLimitCount());
                        map.put("memo", vipRenew.getMemo());
                        list.add(map);
                    }
                } else {
                    //如果没有续租使用首次
                    List<VipPacketPeriodPrice> packetPeriodPriceList = vipPacketPeriodPriceService.findByPriceIdAndForegiftId(vipPrice.getId(), param.foregiftId);
                    for (VipPacketPeriodPrice e : packetPeriodPriceList) {
                        Map map = new HashMap();
                        map.put("id", 0);
                        map.put("activityId", 0);
                        map.put("vipId", e.getId());
                        map.put("dayCount", e.getDayCount());
                        map.put("price", e.getPrice());
                        map.put("isTicket", e.getIsTicket());
                        map.put("limitCount", e.getLimitCount());
                        map.put("dayLimitCount", e.getDayLimitCount());
                        map.put("memo", e.getMemo());
                        list.add(map);
                    }
                }
            } else {
                List<VipPacketPeriodPrice> packetPeriodPriceList = vipPacketPeriodPriceService.findByPriceIdAndForegiftId(vipPrice.getId(), param.foregiftId);
                for (VipPacketPeriodPrice e : packetPeriodPriceList) {
                    Map map = new HashMap();
                    map.put("id", 0);
                    map.put("activityId", 0);
                    map.put("vipId", e.getId());
                    map.put("dayCount", e.getDayCount());
                    map.put("price", e.getPrice());
                    map.put("isTicket", e.getIsTicket());
                    map.put("limitCount", e.getLimitCount());
                    map.put("dayLimitCount", e.getDayLimitCount());
                    map.put("memo", e.getMemo());
                    list.add(map);
                }
            }

        }


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CabinetBatteryDetailsParam {
        public Integer agentId;
        public String cabinetId;
    }

    @ResponseBody
    @RequestMapping("/cabinet_battery_details.htm")
    public RestResult CabinetBatteryDetails(@Valid @RequestBody CabinetBatteryDetailsParam param) {
        Cabinet cabinet = new Cabinet();
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(),"柜子不存在");
        }
        List<Map> list = new ArrayList<Map>();
        List<CabinetBatteryType> listByCabinet = cabinetBatteryTypeService.findListByCabinet(param.cabinetId);
        for (CabinetBatteryType type : listByCabinet) {
            Map map = new HashMap();
            map.put("batteryTypeName", type.getTypeName());
            map.put("type", type.getBatteryType());
            List<ExchangeBatteryForegift> batteryForegiftList = exchangeBatteryForegiftService.findByBatteryType(param.agentId, type.getBatteryType());
            List foregiftList = new ArrayList();
            for (ExchangeBatteryForegift foregift : batteryForegiftList){
                Map foregiftMap = new HashMap();
                foregiftMap.put("foregiftName", (foregift.getMoney()/100)+"元");
                foregiftMap.put("batteryType", foregift.getBatteryType());
                foregiftMap.put("batteryTypeName", foregift.getTypeName());
                foregiftMap.put("foregiftId", foregift.getId());
                foregiftMap.put("memo", foregift.getMemo());
                List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findList(param.agentId, type.getBatteryType(), foregift.getId());
                List packetList = new ArrayList();
                for (PacketPeriodPrice price : packetPeriodPriceList) {
                    Map packetMap = new HashMap();
                    packetMap.put("packetMoney", (price.getPrice()/100)+"元/"+price.getDayCount()+"天");
                    packetMap.put("id", price.getId());
                    packetMap.put("activityId", 0);
                    packetMap.put("limitCount", price.getLimitCount());
                    packetMap.put("dayLimitCount", price.getDayLimitCount());
                    packetMap.put("memo", price.getMemo());
                    packetList.add(packetMap);
                }
                foregiftMap.put("packetList", packetList);
                foregiftList.add(foregiftMap);
            }
            map.put("foregiftList", foregiftList);
            list.add(map);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

}

