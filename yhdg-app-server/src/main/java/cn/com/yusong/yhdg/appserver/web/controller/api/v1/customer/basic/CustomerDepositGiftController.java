package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.AgentService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerDepositGiftService;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositGift;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_customer_basic_customer_deposit_gift")
@RequestMapping(value = "/api/v1/customer/basic/customer_deposit_gift")
public class CustomerDepositGiftController extends ApiController {

    @Autowired
    CustomerDepositGiftService customerDepositGiftService;

    @ResponseBody
    @RequestMapping(value = "/list")
    public RestResult list() {
        TokenCache.Data tokenData = getTokenData();
        List<CustomerDepositGift> list = customerDepositGiftService.findAll(tokenData.partnerId);

        List<Map> lines = new ArrayList<Map>(list.size());
        for (CustomerDepositGift gift : list) {
            Map line = new HashMap();
            line.put("money", gift.getMoney());
          //line.put("gift", gift.getGift());
            lines.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, lines);
    }
}
