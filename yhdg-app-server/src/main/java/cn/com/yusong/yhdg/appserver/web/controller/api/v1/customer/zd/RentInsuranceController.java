package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zd;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.appserver.service.zd.RentBatteryForegiftService;
import cn.com.yusong.yhdg.appserver.service.zd.RentInsuranceOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentInsuranceService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryForegift;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_customer_zd_rent_insurance")
@RequestMapping(value = "/api/v1/customer/zd/rent_insurance")
public class RentInsuranceController extends ApiController {

    static final Logger log = LogManager.getLogger(RentInsuranceController.class);
    @Autowired
    BatteryService batteryService;
    @Autowired
    CustomerService customerService;
    @Autowired
    RentInsuranceService insuranceService;
    @Autowired
    RentInsuranceOrderService insuranceOrderService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    RentBatteryForegiftService rentBatteryForegiftService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        @NotNull
        public long foregiftId;
    }

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        RentBatteryForegift rentBatteryForegift = rentBatteryForegiftService.find(param.foregiftId);
        if(rentBatteryForegift == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "押金不存在，无法购买保险");
        }

        Map returnMap = new HashMap();

        String value = agentSystemConfigService.findConfigValue( ConstEnum.AgentSystemConfigKey.BATTERY_INSURANCE_REQUIER.getValue(), rentBatteryForegift.getAgentId());
        if(value != null){
            returnMap.put("isRequire",Integer.parseInt(value));
        }else{
            returnMap.put("isRequire",0);
        }

        //查询保險金额
        List<Map> list = new ArrayList<Map>();
        //判断用户是否存在该类保险
        RentInsuranceOrder insuranceOrder = insuranceOrderService.findByCustomerId( customerId,  rentBatteryForegift.getBatteryType(), RentInsuranceOrder.Status.PAID.getValue());
        if(insuranceOrder == null){
            List<RentInsurance> insuranceList = insuranceService.findList(rentBatteryForegift.getAgentId(),rentBatteryForegift.getBatteryType());
            for (RentInsurance e : insuranceList) {
                Map map = new HashMap();
                map.put("id", e.getId());
                map.put("insuranceName", e.getInsuranceName());
                map.put("price", e.getPrice());
                map.put("money", e.getPaid());
                map.put("monthCount", e.getMonthCount());
                map.put("price", e.getPrice());
                map.put("memo", e.getMemo());
                list.add(map);
            }
        }
        returnMap.put("insuranceList",list);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, returnMap);
    }

}

