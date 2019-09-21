package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import org.apache.commons.lang.time.DateFormatUtils;
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

@Controller("api_v1_customer_hdg_insurance_order")
@RequestMapping("api/v1/customer/hdg/insurance_order")
public class InsuranceOrderController  extends ApiController {

    @Autowired
    InsuranceOrderService insuranceOrderService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        @NotNull
        public int agentId;
        public Integer status;
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "list")
    public RestResult List(@Valid @RequestBody ListParam param){
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        List<InsuranceOrder> list = insuranceOrderService.findList( param.agentId, customerId, param.status, param.offset, param.limit);
        List<Map> result = new ArrayList<Map>();
        for (InsuranceOrder insuranceOrder : list) {
            Map line = new HashMap();
            line.put("id", insuranceOrder.getId());
            line.put("monthCount", insuranceOrder.getMonthCount());
            line.put("beginDate", insuranceOrder.getBeginTime());
            line.put("endDate", insuranceOrder.getEndTime());
            line.put("money", insuranceOrder.getMoney());
            line.put("paid", insuranceOrder.getPaid());
            line.put("customerMobile", insuranceOrder.getCustomerMobile());
            line.put("status", insuranceOrder.getStatus());
            line.put("payTypeName", "");
            if (insuranceOrder.getPayType() != null) {
                line.put("payTypeName", ConstEnum.PayType.getName(insuranceOrder.getPayType()));
            }
            if (insuranceOrder.getPayTime() != null) {
                line.put("payTime", DateFormatUtils.format(insuranceOrder.getPayTime(), Constant.DATE_TIME_FORMAT) );
            }
            line.put("createTime", DateFormatUtils.format(insuranceOrder.getCreateTime(), Constant.DATE_TIME_FORMAT) );
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }
}
