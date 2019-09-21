package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.InsuranceService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
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

@Controller("api_v1_customer_hdg_insurance")
@RequestMapping(value = "/api/v1/customer/hdg/insurance")
public class InsuranceController extends ApiController {

    static final Logger log = LogManager.getLogger(InsuranceController.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    InsuranceService insuranceService;
    @Autowired
    InsuranceOrderService insuranceOrderService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        @NotNull
        public int agentId;
        @NotNull
        public int batteryType;
    }

    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Map returnMap = new HashMap();

        String value = agentSystemConfigService.findConfigValue( ConstEnum.AgentSystemConfigKey.BATTERY_INSURANCE_REQUIER.getValue(), param.agentId);
        if(value != null){
            returnMap.put("isRequire",Integer.parseInt(value));
        }else{
            returnMap.put("isRequire",0);
        }

        //查询保險金额
        List<Map> list = new ArrayList<Map>();
        //判断用户是否存在该类保险
        InsuranceOrder insuranceOrder = insuranceOrderService.findByCustomerId( customerId,  param.batteryType, InsuranceOrder.Status.PAID.getValue());
        if(insuranceOrder == null){
            List<Insurance> insuranceList = insuranceService.findList(param.agentId,param.batteryType);
            for (Insurance e : insuranceList) {
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

