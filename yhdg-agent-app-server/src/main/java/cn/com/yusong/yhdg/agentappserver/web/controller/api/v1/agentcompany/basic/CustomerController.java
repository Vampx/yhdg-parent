package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agentcompany.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
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
import java.text.ParseException;
import java.util.*;


@Controller("agent_api_v1_agentcompany_basic_customer")
@RequestMapping(value = "/agent_api/v1/agentcompany/basic/customer")
public class CustomerController extends ApiController {
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerService customerService;
    @Autowired
    UserService userService;
    @Autowired
    ShopService shopService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentService agentService;
    @Autowired
    AgentCompanyService agentCompanyService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    AgentCompanyCustomerService agentCompanyCustomerService;
    @Autowired
    AgentCompanyDayStatsService agentCompanyDayStatsService;
    @Autowired
    AgentCompanyTotalStatsService agentCompanyTotalStatsService;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgentCompanyPayMobileListParam {
        public String mobile;
        public int offset;
        public int limit;
    }

    /**
     * 11-查询运营公司收款人手机号列表
     * <p>
     */
    @ResponseBody
    @RequestMapping("/agent_company_pay_mobile_list.htm")
    public RestResult agentCompanyPayMobileList(@Valid @RequestBody AgentCompanyPayMobileListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        return customerService.findMobileList(tokenData.agentId, agent.getPartnerId(), param.mobile, param.offset, param.limit);
    }

    /**
     * 9-查询运营公司职员账号列表
     *
     */
    @ResponseBody
    @RequestMapping(value = "/agent_company_user_list.htm")
    public RestResult shopUserList() {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        AgentCompany agentCompany = agentCompanyService.find(getTokenData().agentCompanyId);
        if (agentCompany == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营公司不存在",null);
        }
        List<User> list = userService.findListByAgentId(agentId, User.AccountType.AGENT_COMPANY.getValue(), null, agentCompany.getId());
        List<Map> result = new ArrayList<Map>();
        for (User user : list) {
            NotNullMap line = new NotNullMap();
            line.put("id", user.getId());
            line.put("loginName", user.getLoginName());
            line.put("fullname", user.getFullname());
            line.putMobileMask("mobile", user.getMobile());
            line.put("agentCompanyId", user.getAgentCompanyId());
            if (user.getAgentCompanyId() != null) {
                line.put("agentCompanyName", agentCompanyService.find(user.getAgentCompanyId()).getCompanyName());
            } else {
                line.put("agentCompanyName", "");
            }
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    /**
     * 13-查询运营公司收款人设置自己信息
     * <p>
     */
    @ResponseBody
    @RequestMapping("/agent_company_pay_mobile_info.htm")
    public RestResult agentCompanyPayMobileInfo() {
        TokenCache.Data tokenData = getTokenData();
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        Agent agent = agentService.find(loginUser.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        return customerService.findMobileInfo(agent.getPartnerId(), loginUser.getMobile());
    }

    /**
     * 14-查询运营公司余额支付密码
     */
    @ResponseBody
    @RequestMapping(value = "/info_balance.htm")
    public RestResult infoBalance() {
        TokenCache.Data tokenData = getTokenData();
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        if (loginUser.getIsAdmin() != ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户非运营公司核心管理员");
        }
        AgentCompany agentCompany = agentCompanyService.find(tokenData.agentCompanyId);
        if (agentCompany == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营公司不存在",null);
        }
        Agent agent = agentService.find(loginUser.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Customer customer = customerService.findByMobile(agent.getPartnerId(), agentCompany.getPayPeopleMobile());

        int hasPayPassword = 1;
        if (StringUtils.isEmpty(agentCompany.getPayPassword())) {
            hasPayPassword = 0;
        }
        Map map = new HashMap();
        map.put("balance", agentCompany.getBalance());
        map.put("hasPayPassword", hasPayPassword);
        map.put("alipayAccount", agentCompany.getPayPeopleFwOpenId());
        map.put("nickname", customer == null ? "" : customer.getNickname());
        map.put("fullname", customer == null ? "" : customer.getFullname());
        map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }


    /**
     * 15-设置运营公司提现支付密码
     */

    public static class SetPayPassword {
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/set_pay_password.htm")
    public RestResult setPayPassword(@Valid @RequestBody SetPayPassword param) {
        TokenCache.Data tokenData = getTokenData();
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        AgentCompany agentCompany = agentCompanyService.find(tokenData.agentCompanyId);
        if (agentCompany == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营公司不存在",null);
        }
        agentCompanyService.setPayPassword(agentCompany.getId(), param.payPassword);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }


    /**
     * 19-修改运营公司支付密码
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePayPasswordParam {
        @NotBlank(message = "Key不能为空")
        public String key;
        @NotBlank(message = "密码不能为空")
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/update_pay_password.htm")
    public RestResult UpdatePayPassword(@Valid @RequestBody UpdatePayPasswordParam param) {
        TokenCache.Data tokenData = getTokenData();
        AgentCompany agentCompany = agentCompanyService.find(tokenData.agentCompanyId);
        if (agentCompany == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营公司不存在",null);
        }
        String uuid = (String) memCachedClient.get(CacheKey.key(CacheKey.K_AGENT_COMPANY_ID_V_UUID, agentCompany.getId()));
        if (!param.key.equals(uuid)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密钥验证失败");
        }

        agentCompanyService.setPayPassword(agentCompany.getId(), param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 20-修改运营公司支付密码2
     */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePayPassword2Param {
        public String oldPassword;
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/update_pay_password2.htm")
    public RestResult UpdatePayPassword2(@Valid @RequestBody UpdatePayPassword2Param param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        AgentCompany agentCompany = agentCompanyService.find(tokenData.agentCompanyId);
        if (agentCompany == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营公司不存在",null);
        }
        if (StringUtils.isEmpty(agentCompany.getPayPassword())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码没有设置");
        }
        if (!agentCompany.getPayPassword().equals(param.oldPassword)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        agentCompanyService.setPayPassword(agentCompany.getId(), param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CustomerListParam {
        public String mobile;
        public int offset;
        public int limit;
    }

    /**
     * 24-查询骑手列表
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/customer_list.htm")
    public RestResult customerList(@RequestBody CustomerListParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        List<Customer> list = customerService.findList(agentId, param.mobile, param.offset, param.limit);

        NotNullMap data = new NotNullMap();

        List<Map> result = new ArrayList<Map>();
        for (Customer customer : list) {
            NotNullMap notNullMap = new NotNullMap();
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
            PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findOneEnabled(customer.getId(), agentId);
            notNullMap.putLong("id", customer.getId());
            notNullMap.putString("fullname", customer.getFullname());
            notNullMap.putMobileMask("mobileMask", customer.getMobile());
            notNullMap.put("mobile", customer.getMobile());
            if (customerExchangeInfo != null) {
                notNullMap.putInteger("foregiftMoney", customerExchangeInfo.getForegift() != null ? customerExchangeInfo.getForegift() : 0);
            } else {
                notNullMap.putInteger("foregiftMoney", 0);
            }
            if (packetPeriodOrder != null) {
                notNullMap.putInteger("rentMoney", packetPeriodOrder.getMoney() != null ? packetPeriodOrder.getMoney() : 0);
                notNullMap.putDateTime("endTime", packetPeriodOrder.getEndTime());
                notNullMap.putLong("rentRestDay", packetPeriodOrder.getEndTime() != null ?  InstallUtils.getRestDay(packetPeriodOrder.getEndTime()) : 0);
            } else {
                notNullMap.putInteger("rentMoney", 0);
                notNullMap.putString("endTime", "");
                notNullMap.putInteger("rentRestDay", 0);
            }
            notNullMap.putMobileMask("laxinMobile", customer.getLaxinMobile() != null ? customer.getLaxinMobile() : "");
            notNullMap.putString("laxinFullname", customer.getLaxinFullname() != null ? customer.getLaxinFullname() : "");
            if (customer.getBelongCabinetId() != null) {
                Cabinet cabinet = cabinetService.find(customer.getBelongCabinetId());
                notNullMap.putString("cabinetName", cabinet.getCabinetName());
            } else {
                notNullMap.putString("cabinetName", "");
            }
            result.add(notNullMap);
        }

        data.put("customerList", result);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgentCompanyCustomerListParam {
        @NotBlank(message = "运营公司id不能为空")
        public String agentCompanyId;
        public String mobile;
        public int offset;
        public int limit;
    }

    /**
     * 25-查询运营公司骑手列表
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/agent_company_customer_list.htm")
    public RestResult agentCompanyCustomerList(@RequestBody AgentCompanyCustomerListParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        AgentCompany agentCompany = agentCompanyService.find(param.agentCompanyId);
        if (agentCompany == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营公司不存在");
        }

        List<Customer> list = customerService.findAgentCompanyCustomer(agentId, param.agentCompanyId, param.mobile, param.offset, param.limit);
        NotNullMap data = new NotNullMap();

        Map stats = new HashMap();
        Date date = new Date();
        String statsDate = DateFormatUtils.format(date.getTime(), Constant.DATE_FORMAT);
        Date beginTime = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addMilliseconds(DateUtils.addDays(beginTime, 1), -1);

        //当日新增注册人数
        List<AgentCompanyCustomer> agentCompanyCustomerList = agentCompanyCustomerService.findDateRange(agentId, agentCompany.getId(), beginTime, endTime);
        stats.put("todayRegisterCount", agentCompanyCustomerList.size());
        List<AgentCompanyCustomer> agentCompanyCustomers = agentCompanyCustomerService.findByCompanyId(agentCompany.getId());
        stats.put("totalRegisterCount", agentCompanyCustomers.size());

        List<AgentCompanyDayStats> agentCompanyDayStatsList = agentCompanyDayStatsService.findList(agentCompany.getAgentId(), agentCompany.getId(), statsDate, null);
        if (agentCompanyDayStatsList.size() == 0) {
            stats.put("todayPeriodMoney", 0);
        } else {
            int totalPeriodMoney = 0;
            for (AgentCompanyDayStats agentCompanyDayStats : agentCompanyDayStatsList) {
                totalPeriodMoney += agentCompanyDayStats.getPacketPeriodMoney();
            }
            stats.put("todayPeriodMoney", totalPeriodMoney);
        }
        List<AgentCompanyTotalStats> agentCompanyTotalStatsList = agentCompanyTotalStatsService.findList(agentCompany.getAgentId(), agentCompany.getId(), null);
        if (agentCompanyTotalStatsList.size() == 0) {
            stats.put("totalPeriodMoney", 0);
        } else {
            int totalPeriodMoney = 0;
            for (AgentCompanyTotalStats agentCompanyTotalStats : agentCompanyTotalStatsList) {
                totalPeriodMoney += agentCompanyTotalStats.getPacketPeriodMoney();
            }
            stats.put("totalPeriodMoney", totalPeriodMoney);
        }
        data.put("stats", stats);

        List<Map> result = new ArrayList<Map>();
        for (Customer customer : list) {
            NotNullMap notNullMap = new NotNullMap();
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
            PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.findOneEnabled(customer.getId(), agentId);
            notNullMap.putLong("id", customer.getId());
            notNullMap.putString("fullname", customer.getFullname());
            notNullMap.putMobileMask("mobileMask", customer.getMobile());
            notNullMap.put("mobile", customer.getMobile());
            if (customerExchangeInfo != null) {
                notNullMap.putInteger("foregiftMoney", customerExchangeInfo.getForegift() != null ? customerExchangeInfo.getForegift() : 0);
            } else {
                notNullMap.putInteger("foregiftMoney", 0);
            }
            if (packetPeriodOrder != null) {
                notNullMap.putInteger("rentMoney", packetPeriodOrder.getMoney() != null ? packetPeriodOrder.getMoney() : 0);
                notNullMap.putDateTime("endTime", packetPeriodOrder.getEndTime());
                notNullMap.putLong("rentRestDay", packetPeriodOrder.getEndTime() != null ?  InstallUtils.getRestDay(packetPeriodOrder.getEndTime()) : 0);
            } else {
                notNullMap.putInteger("rentMoney", 0);
                notNullMap.putString("endTime", "");
                notNullMap.putInteger("rentRestDay", 0);
            }
            notNullMap.putMobileMask("laxinMobile", customer.getLaxinMobile() != null ? customer.getLaxinMobile() : "");
            notNullMap.putString("laxinFullname", customer.getLaxinFullname() != null ? customer.getLaxinFullname() : "");
            if (customer.getBelongCabinetId() != null) {
                Cabinet cabinet = cabinetService.find(customer.getBelongCabinetId());
                notNullMap.putString("cabinetName", cabinet.getCabinetName());
            } else {
                notNullMap.putString("cabinetName", "");
            }
            result.add(notNullMap);
        }

        data.put("customerList", result);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        @NotBlank(message = "运营公司id不能为空")
        public String agentCompanyId;
        public String fullname;
        @NotBlank(message = "骑手手机号不能为空")
        public String mobile;
    }

    /**
     * 26-新建运营公司骑手
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/create.htm")
    public RestResult create(@RequestBody CreateParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        AgentCompany agentCompany = agentCompanyService.find(param.agentCompanyId);
        if (agentCompany == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营公司不存在");
        }
        Customer dbCustomer = agentCompanyService.findCustomer(agentId, param.fullname, param.mobile);
        if (dbCustomer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "骑手不存在");
        }
        List<AgentCompanyCustomer> agentCompanyCustomerList = agentCompanyCustomerService.findByCustomerId(dbCustomer.getId());
        if (agentCompanyCustomerList.size() > 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该骑手已绑定运营公司");
        }
        AgentCompanyCustomer agentCompanyCustomer = new AgentCompanyCustomer();
        agentCompanyCustomer.setAgentId(agentId);
        Agent agent = agentService.find(agentId);
        agentCompanyCustomer.setAgentName(agent.getAgentName());
        agentCompanyCustomer.setAgentCompanyId(param.agentCompanyId);
        agentCompanyCustomer.setCustomerId(dbCustomer.getId());
        agentCompanyCustomer.setCustomerMobile(dbCustomer.getMobile());
        agentCompanyCustomer.setCustomerFullname(dbCustomer.getFullname());
        agentCompanyCustomerService.insert(agentCompanyCustomer);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DeleteParam {
        @NotBlank(message = "运营公司id不能为空")
        public String agentCompanyId;
        @NotBlank(message = "骑手id不能为空")
        public Long customerId;
    }

    /**
     * 27-删除运营公司骑手
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/delete.htm")
    public RestResult delete(@RequestBody DeleteParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        AgentCompany agentCompany = agentCompanyService.find(param.agentCompanyId);
        if (agentCompany == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营公司不存在");
        }
        Customer dbCustomer = customerService.find(param.customerId);
        if (dbCustomer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "骑手不存在");
        }
        List<AgentCompanyCustomer> agentCompanyCustomerList = agentCompanyCustomerService.findByCustomerId(param.customerId);
        if (agentCompanyCustomerList.size() == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该骑手未绑定运营公司");
        }
        agentCompanyCustomerService.delete(param.agentCompanyId, param.customerId);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

}
