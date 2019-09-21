package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerCouponTicketService;
import cn.com.yusong.yhdg.agentappserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.PriceGroupController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;


@Controller("agent_api_v1_agent_basic_customer_coupon_ticket")
@RequestMapping(value = "/agent_api/v1/agent/basic/customer_coupon_ticket")
public class CustomerCouponTicketController extends ApiController {
    @Autowired
    AgentService agentService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    protected UserService userService;

    public static class CreateParam {
        public int category;
        @NotBlank(message = "优惠券名称不能为空")
        public String ticketName;
        public String mobile;
        public Integer ticketType;
        public Integer money;
        public Date beginTime;
        public Date expireTime;
        public String memo;
    }

    /**
     * 77-新建优惠券
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/create.htm")
    public RestResult createTicket(@Valid @RequestBody CreateParam param) {

        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        long userId = tokenData.userId;
        if (agentId == 0||userId ==0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        CustomerCouponTicket cct = new CustomerCouponTicket();
        cct.setPartnerId(agent.getPartnerId());
        cct.setAgentId(agentId);
        cct.setTicketName(param.ticketName);
        cct.setMoney(param.money);
        cct.setStatus(CustomerCouponTicket.Status.NOT_USER.getValue());
        cct.setExpireTime(param.expireTime);
        cct.setCustomerMobile(param.mobile);
        cct.setMemo(param.memo);
        cct.setTicketType(param.ticketType);
        cct.setBeginTime(param.beginTime);
        cct.setOperator(loginUser.getFullname());
        cct.setCategory(param.category);
        cct.setGiveType(CustomerCouponTicket.GiveType.PAY_AGENT.getValue());
        cct.setCreateTime(new Date());

        return customerCouponTicketService.create(cct);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public int category;
        public String mobile;
        public int status;
        public int offset;
        public int limit;
    }

    /**
     * 79-查询优惠券
     * <p>
     */
    @ResponseBody
    @RequestMapping(value = "/list.htm")
    public RestResult couponTicketList(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        List<NotNullMap> list = new ArrayList<NotNullMap>();
        List<CustomerCouponTicket> ticketList = customerCouponTicketService.findList(agentId, param.mobile, param.status, param.category, param.offset, param.limit);
        if (ticketList != null) {
            for (CustomerCouponTicket customerCouponTicket : ticketList) {

                NotNullMap notNullMap = new NotNullMap();
                notNullMap.putLong("id", customerCouponTicket.getId());
                notNullMap.putInteger("status", customerCouponTicket.getStatus());
                notNullMap.putMobileMask("mobile", customerCouponTicket.getCustomerMobile());
                notNullMap.putInteger("money", customerCouponTicket.getMoney() );
                notNullMap.putString("ticketTypeName", customerCouponTicket.getTicketTypeName());
                notNullMap.putString("ticketName", customerCouponTicket.getTicketName() == null ? "" : customerCouponTicket.getTicketName());
                notNullMap.putString("beginTime", customerCouponTicket.getBeginTime() != null ? DateFormatUtils.format(customerCouponTicket.getBeginTime(), Constant.DATE_TIME_FORMAT) : "");
                notNullMap.putString("expireTime", customerCouponTicket.getExpireTime() != null ? DateFormatUtils.format(customerCouponTicket.getExpireTime(), Constant.DATE_TIME_FORMAT) : "");
                notNullMap.putString("createTime", customerCouponTicket.getCreateTime() != null ? DateFormatUtils.format(customerCouponTicket.getCreateTime(), Constant.DATE_TIME_FORMAT) : "");
                list.add(notNullMap);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeleteParam {
        public Long id;
    }

    /**
     * 80-删除优惠券
     */
    @ResponseBody
    @RequestMapping(value = "/delete.htm")
    public RestResult delete(@Valid @RequestBody DeleteParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        return customerCouponTicketService.delete(param.id);
    }


}
