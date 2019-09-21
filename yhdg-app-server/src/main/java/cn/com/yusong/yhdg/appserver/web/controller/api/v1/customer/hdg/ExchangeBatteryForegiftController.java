package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.appserver.service.basic.AgentCompanyCustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerExchangeInfoService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("api_v1_customer_hdg_battery_foregift")
@RequestMapping(value = "/api/v1/customer/hdg/exchange_battery_foregift")
public class ExchangeBatteryForegiftController extends ApiController {
    @Autowired
    AgentBatteryTypeService agentBatteryTypeService;
    @Autowired
    ExchangeBatteryForegiftService exchangeBatteryForegiftService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ExchangePriceTimeService exchangePriceTimeService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    PacketPeriodPriceService packetPeriodPriceService;
    @Autowired
    CabinetBatteryTypeService cabinetBatteryTypeService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    VipExchangeBatteryForegiftService vipExchangeBatteryForegiftService;
    @Autowired
    VipPriceService vipPriceService;
    @Autowired
    VipPacketPeriodPriceService vipPacketPeriodPriceService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    AgentCompanyCustomerService agentCompanyCustomerService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String stationId;
        public String cabinetId;
        public String batteryId;
        public int agentId;
        public int batteryType;
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer =  customerService.find(customerId);

        List dataList = new ArrayList();
        Map<Long, VipExchangeBatteryForegift> reduceMoneyMap = new HashMap<Long, VipExchangeBatteryForegift>();
        Map<Long, Long> priceMap = new HashMap<Long, Long>();

        if (StringUtils.isNotEmpty(param.batteryId)) {
            VipPrice vipPrice = vipPriceService.findOneByCustomerMobile(param.agentId, param.batteryType, customer.getMobile());
            if (vipPrice != null) {
                List<VipExchangeBatteryForegift> vipForegiftList = vipExchangeBatteryForegiftService.findByPriceId(vipPrice.getId());
                for (VipExchangeBatteryForegift foregift : vipForegiftList) {
                    if (foregift.getReduceMoney() != null && vipPacketPeriodPriceService.findCountByForegiftId(vipPrice.getId(), foregift.getId()) > 0) {
                        reduceMoneyMap.put(foregift.getForegiftId(), foregift);
                        priceMap.put(foregift.getForegiftId(), vipPrice.getId());
                    }
                }
            }

        } else if (StringUtils.isNotEmpty(param.cabinetId)) {
            VipPrice vipPrice;
            vipPrice = vipPriceService.findOneByCustomerMobile(param.agentId, param.batteryType, customer.getMobile());
            if (vipPrice == null) {
                vipPrice = vipPriceService.findOneByCabinetId(param.agentId, param.batteryType, param.cabinetId);
            }
            if (vipPrice == null) {
                vipPrice = vipPriceService.findOneByStationId(param.agentId, param.batteryType, param.stationId);
            }

            if (vipPrice != null) {
                List<VipExchangeBatteryForegift> vipForegiftList = vipExchangeBatteryForegiftService.findByPriceId(vipPrice.getId());
                for (VipExchangeBatteryForegift vipForegift : vipForegiftList) {
                    int foregiftCount = vipPacketPeriodPriceService.findCountByForegiftId(vipPrice.getId(), vipForegift.getForegiftId());
                    if (foregiftCount > 0) {
                        reduceMoneyMap.put(vipForegift.getForegiftId(), vipForegift);
                        priceMap.put(vipForegift.getForegiftId(), vipPrice.getId());
                    }
                }
            }
        }

        List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findByBatteryType(param.agentId, param.batteryType);
        if (exchangeBatteryForegiftList != null) {

            if (reduceMoneyMap.size() > 0) {
                for (ExchangeBatteryForegift exchangeBatteryForegift : exchangeBatteryForegiftList) {
                    Map line = new HashMap();
                    line.put("type", exchangeBatteryForegift.getBatteryType());
                    line.put("batteryTypeName", exchangeBatteryForegift.getTypeName());
                    line.put("foregiftId", exchangeBatteryForegift.getId());
                    line.put("memo", exchangeBatteryForegift.getMemo());

                    int money = exchangeBatteryForegift.getMoney();
                    VipExchangeBatteryForegift vipForegift = reduceMoneyMap.get(exchangeBatteryForegift.getId());
                    int foregiftCount = vipPacketPeriodPriceService.findCountByForegiftId(priceMap.get(exchangeBatteryForegift.getId()), exchangeBatteryForegift.getId());

                    if (vipForegift != null && foregiftCount > 0) {
                        money = Math.max(money - vipForegift.getReduceMoney(), 0);
                        line.put("vipForegiftId", vipForegift.getId());
                        line.put("money", money);
                    } else {
                        line.put("vipForegiftId", null);
                    }

                    dataList.add(line);
                    if (line.get("vipForegiftId") == null) {
                        dataList.remove(line);
                    }
                }
            } else {
                for (ExchangeBatteryForegift exchangeBatteryForegift : exchangeBatteryForegiftList) {
                    Map line = new HashMap();
                    line.put("type", exchangeBatteryForegift.getBatteryType());
                    line.put("batteryTypeName", exchangeBatteryForegift.getTypeName());
                    line.put("foregiftId", exchangeBatteryForegift.getId());
                    line.put("memo", exchangeBatteryForegift.getMemo());
                    int money = exchangeBatteryForegift.getMoney();
                    line.put("vipForegiftId", 0);
                    line.put("money", money);

                    dataList.add(line);
                }
            }

        }

        return  RestResult.dataResult(RespCode.CODE_0.getValue(), null, dataList);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryTypeListParam {
        public String cabinetId;
    }

    @ResponseBody
    @RequestMapping(value = "/battery_type_list")
    public RestResult batteryTypeList(@Valid @RequestBody BatteryTypeListParam param) {
        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if(cabinet == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        List dataList = new ArrayList();

        //查询柜子对应电池类型
        List<CabinetBatteryType> batteryTypeList = cabinetBatteryTypeService.findListByCabinet(param.cabinetId);
        for(CabinetBatteryType cabinetBatteryType : batteryTypeList){
            Map map = new HashMap();
            map.put("type",cabinetBatteryType.getBatteryType());
            map.put("batteryTypeName", cabinetBatteryType.getTypeName());
            dataList.add(map);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, dataList);
    }
}
