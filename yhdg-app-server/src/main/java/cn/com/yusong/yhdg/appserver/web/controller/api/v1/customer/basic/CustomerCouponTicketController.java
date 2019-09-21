package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerCouponTicketService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerCouponTicket;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("api_v1_customer_basic_customer_coupon_ticket")
@RequestMapping(value = "/api/v1/customer/basic/customer_coupon_ticket")
public class CustomerCouponTicketController extends ApiController {
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    CustomerService customerService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int ticketType;
        public int agentId;
    }

    //9.查询可用优惠券
    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();

        Customer customer = customerService.find(tokenData.customerId);
        if (customer.getMobile() == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "无可用优惠券");
        }
        List<Map> list = new ArrayList<Map>();
        List<CustomerCouponTicket> ticketList = customerCouponTicketService.findList(param.agentId,
                customer.getMobile(), new Date(), param.ticketType, CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Category.EXCHANGE.getValue(),null, null);
        if (ticketList != null) {
            for (CustomerCouponTicket customerCouponTicket : ticketList) {
                Map mapTicket = new HashMap();
                mapTicket.put("id", customerCouponTicket.getId());
                mapTicket.put("ticketName", customerCouponTicket.getTicketName() );
                mapTicket.put("money", customerCouponTicket.getMoney() );
                mapTicket.put("expireTime", customerCouponTicket.getExpireTime() != null ? DateFormatUtils.format(customerCouponTicket.getExpireTime(), Constant.DATE_FORMAT) : null);
                list.add(mapTicket);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }
}
