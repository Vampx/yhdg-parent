package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerInOutMoneyService;
import cn.com.yusong.yhdg.appserver.service.basic.WithdrawService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInOutMoney;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ruanjian5 on 2017/11/4.
 */
@Controller( "api_v1_customer_basic_customer_in_out_money")
@RequestMapping(value = "/api/v1/customer/basic/customer_in_out_money")
public class CustomerInOutMoneyController extends ApiController {

    @Autowired
    CustomerInOutMoneyService customerInOutMoneyService;
    @Autowired
    WithdrawService withdrawService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int offset;
        public int limit;
    }
    @ResponseBody
    @RequestMapping("list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        long customerId = getTokenData().customerId;

        List<Map> list = new ArrayList<Map>(param.limit);
        List<CustomerInOutMoney> customerList = customerInOutMoneyService.findList(customerId, param.offset, param.limit);
        for (CustomerInOutMoney e : customerList) {
            NotNullMap line = new NotNullMap();
            line.put("id", e.getId());
            line.put("bizTypeName", e.getBizTypeName());
            line.put("money", e.getMoney());
            line.put("statusInfo", e.getStatusInfo());
            line.putDateTime("createTime", e.getCreateTime());
            list.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }


}
