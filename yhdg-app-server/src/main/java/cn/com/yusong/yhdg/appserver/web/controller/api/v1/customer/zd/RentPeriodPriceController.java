package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zd;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentCompanyCustomerService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.service.zd.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.AgentCompanyCustomer;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.zd.*;
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

@Controller("api_v1_customer_zd_rent_period_price")
@RequestMapping(value = "/api/v1/customer/zd/rent_period_price")
public class RentPeriodPriceController extends ApiController {

    static final Logger log = LogManager.getLogger(RentPeriodPriceController.class);

    @Autowired
    BatteryService batteryService;
    @Autowired
    RentBatteryForegiftService rentBatteryForegiftService;
    @Autowired
    RentPeriodPriceService rentPeriodPriceService;
    @Autowired
    RentPeriodActivityService rentPeriodActivityService;
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
        @NotNull
        public Long foregiftId;
        public String shopId;
    }

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer =  customerService.find(customerId);
        RentBatteryForegift rentBatteryForegift = rentBatteryForegiftService.find(param.foregiftId);
        if(rentBatteryForegift == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "押金不存在，无法续租");
        }

        //查询套餐金额
        List<Map> list = new ArrayList<Map>();

        VipRentPrice vipRentPrice = null;
        VipRentPeriodPrice vipPacketPeriodPrice = null;

        if (param.foregiftId != null) {
            AgentCompanyCustomer agentCompanyCustomer = agentCompanyCustomerService.findByCustomerId(customerId);
            if (agentCompanyCustomer != null) {
                vipRentPrice = vipRentPriceService.findOneByAgentCompanyId(rentBatteryForegift.getAgentId(), rentBatteryForegift.getBatteryType(), agentCompanyCustomer.getAgentCompanyId());
                if (vipRentPrice == null) {
                    vipRentPrice = vipRentPriceService.findOneByCustomerMobile(rentBatteryForegift.getAgentId(), rentBatteryForegift.getBatteryType(), customer.getMobile());
                }

                if (vipRentPrice == null) {
                    vipRentPrice = vipRentPriceService.findOneByShopId(rentBatteryForegift.getAgentId(), rentBatteryForegift.getBatteryType(), param.shopId);
                }
            } else {
                vipRentPrice = vipRentPriceService.findOneByCustomerMobile(rentBatteryForegift.getAgentId(), rentBatteryForegift.getBatteryType(), customer.getMobile());

                if (vipRentPrice == null) {
                    vipRentPrice = vipRentPriceService.findOneByShopId(rentBatteryForegift.getAgentId(), rentBatteryForegift.getBatteryType(), param.shopId);
                }
            }

            if (vipRentPrice != null) {
                List<VipRentPeriodPrice> vipRentPeriodPriceList = vipRentPeriodPriceService.findByPriceIdAndForegiftId(vipRentPrice.getId(), param.foregiftId);
                if (vipRentPeriodPriceList.size() > 0) {
                    for (VipRentPeriodPrice vipRentPeriodPrice : vipRentPeriodPriceList) {
                        vipPacketPeriodPrice = vipRentPeriodPrice;
                    }
                }
            }
        }

        if (vipPacketPeriodPrice == null) {
            List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceService.findList(rentBatteryForegift.getAgentId(), rentBatteryForegift.getBatteryType(), param.foregiftId);
            for (RentPeriodPrice e : rentPeriodPriceList) {
                Map map = new HashMap();
                map.put("id", e.getId());
                map.put("dayCount", e.getDayCount());
                map.put("price", e.getPrice());
                map.put("memo", e.getMemo());
                list.add(map);
            }
        } else {
            List<VipRentPeriodPrice> vipRentPeriodPriceList = vipRentPeriodPriceService.findByPriceIdAndForegiftId(vipRentPrice.getId(), param.foregiftId);
            for (VipRentPeriodPrice e : vipRentPeriodPriceList) {
                Map map = new HashMap();
                map.put("id", 0);
                map.put("vipId", e.getId());
                map.put("dayCount", e.getDayCount());
                map.put("price", e.getPrice());
                map.put("memo", e.getMemo());
                list.add(map);
            }
        }


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }
}

