package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zd;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentCompanyCustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.service.zd.RentBatteryForegiftService;
import cn.com.yusong.yhdg.appserver.service.zd.VipRentBatteryForegiftService;
import cn.com.yusong.yhdg.appserver.service.zd.VipRentPeriodPriceService;
import cn.com.yusong.yhdg.appserver.service.zd.VipRentPriceService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.VipRentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPrice;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("api_v1_customer_zd_battery_foregift")
@RequestMapping(value = "/api/v1/customer/zd/battery_foregift")
public class BatteryForegiftController extends ApiController {
    @Autowired
    BatteryService batteryService;
    @Autowired
    RentBatteryForegiftService rentBatteryForegiftService;
    @Autowired
    CustomerService customerService;
    @Autowired
    VipRentPriceService vipRentPriceService;
    @Autowired
    VipRentBatteryForegiftService vipRentBatteryForegiftService;
    @Autowired
    VipRentPeriodPriceService vipRentPeriodPriceService;
    @Autowired
    AgentCompanyCustomerService agentCompanyCustomerService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String batteryId;
        public String shopId;
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer =  customerService.find(customerId);

        Battery battery = batteryService.find(param.batteryId);
        if(battery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        List dataList = new ArrayList();
        Map<Long, VipRentBatteryForegift> reduceMoneyMap = new HashMap<Long, VipRentBatteryForegift>();
        Map<Long, Long> priceMap = new HashMap<Long, Long>();

       if (StringUtils.isNotEmpty(param.batteryId)) {
           VipRentPrice vipPrice;
           AgentCompanyCustomer agentCompanyCustomer = agentCompanyCustomerService.findByCustomerId(customerId);
           if (agentCompanyCustomer != null) {
               vipPrice = vipRentPriceService.findOneByAgentCompanyId(battery.getAgentId(), battery.getType(), agentCompanyCustomer.getAgentCompanyId());
               if (vipPrice == null) {
                   vipPrice = vipRentPriceService.findOneByCustomerMobile(battery.getAgentId(), battery.getType(), customer.getMobile());
               }
               if (vipPrice == null) {
                   vipPrice = vipRentPriceService.findOneByShopId(battery.getAgentId(), battery.getType(), param.shopId);
               }
           } else {
               vipPrice = vipRentPriceService.findOneByCustomerMobile(battery.getAgentId(), battery.getType(), customer.getMobile());

               if (vipPrice == null) {
                   vipPrice = vipRentPriceService.findOneByShopId(battery.getAgentId(), battery.getType(), param.shopId);
               }

           }

            if (vipPrice != null) {
                List<VipRentBatteryForegift> vipRentBatteryForegiftList = vipRentBatteryForegiftService.findByPriceId(vipPrice.getId());
                for (VipRentBatteryForegift vipForegift : vipRentBatteryForegiftList) {
                    int foregiftCount = vipRentPeriodPriceService.findCountByForegiftId(vipPrice.getId(), vipForegift.getForegiftId());
                    if (foregiftCount > 0) {
                        reduceMoneyMap.put(vipForegift.getForegiftId(), vipForegift);
                        priceMap.put(vipForegift.getForegiftId(), vipPrice.getId());
                    }
                }
            }
        }

        List<RentBatteryForegift> rentBatteryForegiftList = rentBatteryForegiftService.findByAgent(battery.getAgentId(),battery.getType());
        if (rentBatteryForegiftList != null) {
            if (reduceMoneyMap.size() > 0) {
                for (RentBatteryForegift rentBatteryForegift : rentBatteryForegiftList) {
                    Map line = new HashMap();
                    line.put("type", rentBatteryForegift.getBatteryType());
                    line.put("name", rentBatteryForegift.getTypeName());
                    line.put("foregiftId", rentBatteryForegift.getId());
                    line.put("memo", rentBatteryForegift.getMemo());

                    int money = rentBatteryForegift.getMoney();
                    VipRentBatteryForegift vipForegift = reduceMoneyMap.get(rentBatteryForegift.getId());
                    int foregiftCount = vipRentPeriodPriceService.findCountByForegiftId(priceMap.get(rentBatteryForegift.getId()), rentBatteryForegift.getId());

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
                for (RentBatteryForegift rentBatteryForegift : rentBatteryForegiftList) {
                    Map line = new HashMap();
                    line.put("type", rentBatteryForegift.getBatteryType());
                    line.put("name", rentBatteryForegift.getTypeName());
                    line.put("foregiftId", rentBatteryForegift.getId());
                    line.put("memo", rentBatteryForegift.getMemo());
                    int money = rentBatteryForegift.getMoney();
                    line.put("vipForegiftId", 0);
                    line.put("money", money);
                    dataList.add(line);
                }
            }
        }

        return  RestResult.dataResult(RespCode.CODE_0.getValue(), null, dataList);
    }
}
