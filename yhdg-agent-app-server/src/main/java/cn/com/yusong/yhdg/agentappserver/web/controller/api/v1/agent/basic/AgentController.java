package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.basic;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.DataResult;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.*;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.AgentMonthStats;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.tree.Node;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
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
import java.text.SimpleDateFormat;
import java.util.*;

@Controller("agent_api_v1_agent_basic_agent")
@RequestMapping(value = "/agent_api/v1/agent/basic/agent")
public class AgentController extends ApiController {

    @Autowired
    AgentService agentService;
    @Autowired
    UserService userService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AgentDayStatsService agentDayStatsService;
    @Autowired
    AgentMonthStatsService agentMonthStatsService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BalanceRecordService agentDayBalanceRecordService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;
    @Autowired
    AgentMaterialDayStatsService agentMaterialDayStatsService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FirstParam {
        public int category;
    }

    /**
     * 6-首页数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/first")
    public RestResult first(@Valid @RequestBody FirstParam param) {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        if (param.category == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
        }
        User loginUser = userService.find(getTokenData().userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        Map totalMap = new HashMap();
        //今天 2019-01-24
        String today = DateFormatUtils.format(new Date().getTime(), Constant.DATE_FORMAT);

        AgentDayStats todayData = agentDayStatsService.find(agentId, today, param.category);

        AgentMonthStats agentMonthStats = agentMonthStatsService.findTotal(agentId, param.category);

        //换电
        int hdInCustomerCount = customerService.findHdCustomerCountByStatus(agentId, Customer.HdForegiftStatus.PAID.getValue(), null, null);
        int hdOutCustomerCount = customerService.findHdCustomerCountByStatus(agentId, Customer.HdForegiftStatus.REFUNDED.getValue(), null, null);
        //租电
        int zdInCustomerCount = customerService.findZdCustomerCountByStatus(agentId, Customer.ZdForegiftStatus.PAID.getValue(), null, null);
        int zdOutCustomerCount = customerService.findZdCustomerCountByStatus(agentId, Customer.ZdForegiftStatus.REFUNDED.getValue(), null, null);

        int zdForegiftBalanceRatio = 0;
        if (agent.getZdForegiftBalanceRatio() != null) {
            zdForegiftBalanceRatio = agent.getZdForegiftBalanceRatio();
        }
        int foregiftBalanceRatio = 0;
        if (agent.getForegiftBalanceRatio() != null) {
            foregiftBalanceRatio = agent.getForegiftBalanceRatio();
        }

        int ratio = 0;
        String systemRatio = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId());
        if(StringUtils.isNotEmpty(systemRatio)){
            ratio = Integer.parseInt(systemRatio);
        }

        if (agentMonthStats != null) {
            totalMap.put("foregiftMoney", agentMonthStats.getForegiftMoney());
            totalMap.put("foregiftRefundMoney", agentMonthStats.getForegiftRefundMoney());
            totalMap.put("packetPeriodMoney", agentMonthStats.getAgentPacketPeriodMoney());
            totalMap.put("refundPacketPeriodMoney", agentMonthStats.getAgentRefundPacketPeriodMoney());
            totalMap.put("deductionTicketMoney", agentMonthStats.getDeductionTicketMoney());
            totalMap.put("orderMoney", agentMaterialDayStatsService.findTotalMoney(agentId, param.category).getTotalMoney());

            if (param.category == ConstEnum.Category.EXCHANGE.getValue()) {
                totalMap.put("foregiftCustomerCount", hdInCustomerCount + hdOutCustomerCount);
                totalMap.put("inCustomerCount", hdInCustomerCount);//已交
                totalMap.put("outCustomerCount", hdOutCustomerCount);//已退
                totalMap.put("hdRemainMoney", agent.getForegiftRemainMoney());
                if (foregiftBalanceRatio > ratio) {
                    totalMap.put("remainStatus", ConstEnum.Flag.TRUE.getValue());
                } else {
                    totalMap.put("remainStatus", ConstEnum.Flag.FALSE.getValue());
                }
            } else {
                totalMap.put("foregiftCustomerCount", zdInCustomerCount + zdOutCustomerCount);
                totalMap.put("inCustomerCount", zdInCustomerCount);//已交
                totalMap.put("outCustomerCount", zdOutCustomerCount);//已退
                totalMap.put("zdRemainMoney", agent.getZdForegiftRemainMoney());
                if (zdForegiftBalanceRatio > ratio) {
                    totalMap.put("remainStatus", ConstEnum.Flag.TRUE.getValue());
                } else {
                    totalMap.put("remainStatus", ConstEnum.Flag.FALSE.getValue());
                }
            }

            totalMap.put("insuranceMoney", agentMonthStats.getInsuranceMoney());
            totalMap.put("insuranceRefundMoney", agentMonthStats.getInsuranceRefundMoney());
        } else {
            totalMap.put("foregiftMoney", 0);
            totalMap.put("foregiftRefundMoney", 0);
            totalMap.put("packetPeriodMoney", 0);
            totalMap.put("refundPacketPeriodMoney", 0);
            totalMap.put("deductionTicketMoney", 0);
            totalMap.put("orderMoney", 0);
            totalMap.put("inCustomerCount", 0);
            totalMap.put("outCustomerCount", 0);
            totalMap.put("remainMoney", 0);
            totalMap.put("insuranceMoney", 0);
            totalMap.put("insuranceRefundMoney", 0);
        }

        Map todayMap = new HashMap();

        if (todayData != null) {
            todayMap.put("foregiftMoney", todayData.getForegiftMoney());
            todayMap.put("packetPeriodMoney", todayData.getAgentPacketPeriodMoney());
            todayMap.put("insuranceMoney", todayData.getInsuranceMoney());
            todayMap.put("foregiftRefundMoney", todayData.getForegiftRefundMoney());
            todayMap.put("refundPacketPeriodMoney", todayData.getAgentRefundPacketPeriodMoney());
            todayMap.put("insuranceRefundMoney", todayData.getInsuranceRefundMoney());
            todayMap.put("timesMoney", todayData.getAgentExchangeMoney());
            todayMap.put("chargeMoney", todayData.getElectricPrice());
            todayMap.put("peopleCount", todayData.getActiveCustomerCount());
            todayMap.put("exchangeCount", todayData.getExchangeCount());
            todayMap.put("cabinetCount", todayData.getCabinetCount());
            todayMap.put("batteryCount", todayData.getBatteryCount());
            todayMap.put("foregiftCount", todayData.getForegiftCount());
            if (todayData.getUpdateTime() != null) {
                todayMap.put("updateTime", DateFormatUtils.format(todayData.getUpdateTime(), Constant.DATE_TIME_FORMAT));
            } else {
                todayMap.put("updateTime", "");
            }
        } else {
            todayMap.put("foregiftMoney", 0);
            todayMap.put("packetPeriodMoney", 0);
            todayMap.put("insuranceMoney", 0);
            todayMap.put("foregiftRefundMoney",0);
            todayMap.put("refundPacketPeriodMoney", 0);
            todayMap.put("insuranceRefundMoney", 0);
            todayMap.put("timesMoney", 0);
            todayMap.put("chargeMoney", 0);
            todayMap.put("peopleCount", 0);
            todayMap.put("exchangeCount", 0);
            todayMap.put("cabinetCount", 0);
            todayMap.put("batteryCount", 0);
            todayMap.put("foregiftCount", 0);
            todayMap.put("updateTime", "");
        }

        Map line = new HashMap();
        if (loginUser.getIsAdmin() == ConstEnum.Flag.TRUE.getValue()) {
            line.put("isShowPay", ConstEnum.Flag.TRUE.getValue());
        } else {
            line.put("isShowPay", ConstEnum.Flag.FALSE.getValue());
        }
        if (agent.getPayPeopleMobile() != null && agent.getPayPeopleMobile().equals(loginUser.getMobile()) && loginUser.getIsAdmin() == ConstEnum.Flag.TRUE.getValue()) {
            line.put("isShowBalance", ConstEnum.Flag.TRUE.getValue());
        } else {
            line.put("isShowBalance", ConstEnum.Flag.FALSE.getValue());
        }

        line.put("balance", agent.getBalance());
        line.put("total", totalMap);
        line.put("today", todayMap);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }

    /**
     * 22-查询运营商列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/agent_list.htm")
    public RestResult agentList() {
        List<Map> list = new ArrayList<Map>();

        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        Agent currentAgent = agentService.find(agentId);
        Map map = new HashMap();
        map.put("id", currentAgent.getId());
        map.put("agentName", currentAgent.getAgentName());

        list.add(map);

        List<Agent> agentList = agentService.findByParent(agentId);
        for (Agent agent : agentList) {
            map = new HashMap();
            map.put("id", agent.getId());
            map.put("agentName", agent.getAgentName());
            list.add(map);
        }

        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    public static class DetailListParam {
        public String agentName;
    }

    /**
     * 23-查询运营商详情列表
     *
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping("/detail_list.htm")
    public RestResult detailList(@RequestBody DetailListParam param) {

        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        List<Map> list = new ArrayList<Map>();
        List<Node<Agent>> nodeList = agentService.buildLevelList(agentId);
        for(Node<Agent> node : nodeList) {
            Map map = new HashMap();
            if (StringUtils.isNotEmpty(param.agentName) && node.getData().getAgentName().contains(param.agentName)){
                map.put("id", node.getData().getId());
                map.put("agentName", node.getData().getAgentName());
                map.put("userCount", userService.findAgentUserCount(node.getData().getId()));
                map.put("cityName", node.getData().getCityName());

                if(node.getParent() == null) {
                    map.put("parentName", "");
                } else {
                    map.put("parentName", node.getParent().getData().getAgentName());
                }
                list.add(map);
            }else if (StringUtils.isEmpty(param.agentName)){
                map.put("id", node.getData().getId());
                map.put("agentName", node.getData().getAgentName());
                map.put("userCount", userService.findAgentUserCount(node.getData().getId()));
                map.put("cityName", node.getData().getCityName());

                if(node.getParent() == null) {
                    map.put("parentName", "");
                } else {
                    map.put("parentName", node.getParent().getData().getAgentName());
                }
                list.add(map);
            }

        }

        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    public static class DetailParam {
        public int id;
    }

    /**
     * 24-查询运营商详情列表
     *
     * @param param
     * @return
     */

    @ResponseBody
    @RequestMapping("/detail.htm")
    public RestResult detail(@RequestBody DetailParam param) {

        Agent agent = agentService.find(param.id);

        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在");
        }

        List<Map> userListMap = new ArrayList<Map>();
        List<User> userList = userService.findListByAgentId(param.id, User.AccountType.AGENT.getValue(), null, null);
        for (User user : userList) {
            Map map = new HashMap();
            map.put("id", user.getId());
            map.put("loginName", user.getLoginName());
            map.put("fullname", user.getFullname());
            map.put("mobile", user.getMobile());
            map.put("password", user.getPassword());
            userListMap.add(map);
        }

        Map data = new HashMap();
        data.put("id", agent.getId());
        data.put("agentName", agent.getAgentName());
        data.put("provinceId", agent.getProvinceId());
        data.put("cityId", agent.getCityId());
        data.put("districtId", agent.getDistrictId());
        data.put("provinceName", agent.getProvinceName());
        data.put("cityName", agent.getCityName());
        data.put("districtName", agent.getDistrictName());
        data.put("street", agent.getStreet());
        data.put("parentId", agent.getParentId());
        if (agent.getParentId() == null) {
            data.put("parentName", null);
        } else {
            data.put("parentName", agentService.find(agent.getParentId()).getAgentName());
        }
        data.put("linkman", agent.getLinkman());
        data.put("tel", agent.getTel());
        data.put("userList", userListMap);
        return DataResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    /**
     * 25-修改运营商
     *
     * @return
     */
    public static class UpdateParam {
        public int id;
        @NotBlank(message = "运营商名称不能为空")
        public String agentName;
        public Integer parentId;
        public String parentName;
        public String linkman;
        public String tel;

        public AddUserList[] addUserList;
        public static class AddUserList {
            public String loginName;
            public String fullname;
            public String mobile;
            public String password;
        }

        public UpdateUserList[] updateUserList;
        public static class UpdateUserList {
            public Long id;
            public String loginName;
            public String fullname;
            public String mobile;
            public String password;
        }
        public Long[] deleteUserList;

    }

    @ResponseBody
    @RequestMapping("/update.htm")
    public RestResult updateAgent(@Valid @RequestBody UpdateParam param) {

        if (agentService.find(param.id) == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Agent agent = new Agent();
        agent.setId(param.id);
        agent.setParentId(param.parentId);
        agent.setParentName(param.parentName);
        agent.setLinkman(param.linkman);
        agent.setTel(param.tel);
        agent.setAgentName(param.agentName);

        User[] addList = new User[param.addUserList.length];

        if (param.addUserList != null && param.addUserList.length > 0) {
            for (int i = 0; i < param.addUserList.length; i++) {
                UpdateParam.AddUserList detail = param.addUserList[i];
                User user = userService.findByLoginName(detail.loginName);
                if (user != null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "登录名"+detail.loginName+"已存在");
                }

                User agentUser = new User();
                agentUser.setLoginName(detail.loginName);
                agentUser.setFullname(detail.fullname);
                agentUser.setMobile(detail.mobile);
                if (StringUtils.isNotEmpty(detail.password)) {
                    agentUser.setPassword(CodecUtils.password(detail.password));
                } else {
                    agentUser.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
                }
                addList[i] = agentUser;
            }
        }

        User[] updateList = new User[param.updateUserList.length];

        if (param.updateUserList != null && param.updateUserList.length > 0) {
            for (int i = 0; i < param.updateUserList.length; i++) {
                UpdateParam.UpdateUserList detail = param.updateUserList[i];

                User agentUser = new User();
                User user = userService.find(detail.id);
                agentUser.setId(detail.id);
                agentUser.setLoginName(detail.loginName);
                agentUser.setFullname(detail.fullname);
                agentUser.setMobile(detail.mobile);
                if (StringUtils.isNotEmpty(detail.password)) {
                    agentUser.setPassword(CodecUtils.password(detail.password));
                } else {
                    agentUser.setPassword(user.getPassword());
                }

                updateList[i] = agentUser;
            }
        }

        Long[] deleteList = new Long[param.deleteUserList.length];
        if (param.deleteUserList != null && param.deleteUserList.length > 0) {
            deleteList = param.deleteUserList;
        }
        return agentService.update(agent, addList, updateList, deleteList);
    }

    public static class CreateParam {
        @NotBlank(message = "运营商名称不能为空")
        public String agentName;
        public Integer parentId;
        public String parentName;
        public String linkman;
        public String tel;
        public AgentUser[] userList;

        public static class AgentUser {
            public String loginName;
            public String fullname;
            public String mobile;
            public String password;
        }
    }

    /**
     * 26-新建运营商
     *
     * @param param
     * @return
     */

    @ResponseBody
    @RequestMapping("/create.htm")
    public RestResult createAgent(@Valid @RequestBody CreateParam param) {

        Agent parentAgent = agentService.find(param.parentId);
        if (parentAgent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "上级运营商不存在");
        }

        Agent agent = new Agent();
        agent.setPartnerId(parentAgent.getPartnerId());
        agent.setBalance(0);
        agent.setParentId(param.parentId);
        agent.setParentName(param.parentName);
        agent.setLinkman(param.linkman);
        agent.setTel(param.tel);
        agent.setAgentName(param.agentName);
        agent.setIsActive(ConstEnum.Flag.TRUE.getValue());
        agent.setOrderNum(10);
        agent.setBalanceStatus(Agent.BalanceStatus.NO.getValue());
        agent.setForegiftBalance(0);
        agent.setForegiftBalanceRatio(0);
        agent.setForegiftRemainMoney(0);
        agent.setZdForegiftBalance(0);
        agent.setZdForegiftBalanceRatio(0);
        agent.setZdForegiftRemainMoney(0);
        agent.setCreateTime(new Date());

        User[] userList = new User[param.userList.length];
        if (param.userList != null && param.userList.length > 0) {
            for (int i = 0; i < param.userList.length; i++) {
                AgentController.CreateParam.AgentUser detail = param.userList[i];
                User user = userService.findByLoginName(detail.loginName);
                if (user != null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "登录名"+detail.loginName+"已存在");
                }
                User agentUser = new User();
                agentUser.setLoginName(detail.loginName);
                agentUser.setFullname(detail.fullname);
                agentUser.setMobile(detail.mobile);
                if (StringUtils.isEmpty(detail.password)) {
                    agentUser.setPassword(CodecUtils.password(Constant.DEFAULT_PASSWORD));
                } else {
                    agentUser.setPassword(CodecUtils.password(detail.password));
                }
                userList[i] = agentUser;
            }
        }


        return agentService.create(agent, userList);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SwitchParam {
        public int agentId;
    }

    @ResponseBody
    @RequestMapping(value = "/switch_agent.htm")
    public RestResult switchAgent(@RequestBody SwitchParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        tokenData.agentId = param.agentId;
        tokenCache.put(tokenData, MemCachedConfig.CACHE_TWO_HOUR);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgentPayMobileListParam {
        public String mobile;
        public int offset;
        public int limit;
    }

    /**
     * 103-查询运营商收款人手机号列表
     * <p>
     */
    @ResponseBody
    @RequestMapping("/agent_pay_mobile_list.htm")
    public RestResult agentPayMobileListParam(@Valid @RequestBody AgentPayMobileListParam param) {
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

    public static class UpdatePayPeopleParam {
        public String authCode;
        @NotBlank(message = "收款人手机不能为空")
        public String payPeopleMobile;
        public String payPeopleName;
        public String payPeopleMpOpenId;
        public String payPeopleFwOpenId;
    }

    /**
     * 104-修改运营商收款人信息
     *
     */
    @ResponseBody
    @RequestMapping("/update_pay_people.htm")
    public RestResult updatePayPeople(@RequestBody UpdatePayPeopleParam param) {
        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.payPeopleMobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }
        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.payPeopleMobile));
        TokenCache.Data tokenData = getTokenData();
        return agentService.updatePayPeople(tokenData.agentId, param.payPeopleMobile, param.payPeopleName, param.payPeopleMpOpenId, param.payPeopleFwOpenId);
    }

    /**
     * 105-查询运营商收款人设置自己信息
     * <p>
     */
    @ResponseBody
    @RequestMapping("/agent_pay_mobile_info.htm")
    public RestResult agentPayMobileInfo() {
        TokenCache.Data tokenData = getTokenData();
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        if (StringUtils.isEmpty(loginUser.getMobile())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户未实名认证，请先认证。");
        }

        Agent agent = agentService.find(loginUser.getAgentId());
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        return customerService.findMobileInfo(agent.getPartnerId(), loginUser.getMobile());
    }

    /**
     * 106-查询运营商余额支付密码
     */
    @ResponseBody
    @RequestMapping(value = "/info_balance.htm")
    public RestResult infoBalance() {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        if (StringUtils.isEmpty(agent.getPayPeopleMobile())){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"运营商收款人不存在，请先设置收款人",null);
        }
        User loginUser = userService.find(tokenData.userId);
        if (loginUser == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户不存在");
        }
        if (loginUser.getIsAdmin() != ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "该用户非运营商核心管理员");
        }
        Customer customer = customerService.findByMobile(agent.getPartnerId(), agent.getPayPeopleMobile());

        int hasPayPassword = 1;
        if (StringUtils.isEmpty(agent.getPayPassword())) {
            hasPayPassword = 0;
        }
        Map map = new HashMap();
        map.put("balance", agent.getBalance());//运营商余额
        map.put("hasPayPassword", hasPayPassword);
        map.put("alipayAccount", agent.getPayPeopleFwOpenId());
        map.put("nickname", customer == null ? "" : customer.getNickname());
        map.put("fullname", customer == null ? "" : customer.getFullname());
        map.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

    /**
     * 107-设置运营商提现支付密码
     */

    public static class SetPayPassword {
        public String payPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/set_pay_password.htm")
    public RestResult setPayPassword(@Valid @RequestBody SetPayPassword param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        agentService.setPayPassword(agent.getId(), param.payPassword);
        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 113-修改运营商提现支付密码
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
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        String uuid = (String) memCachedClient.get(CacheKey.key(CacheKey.K_AGENT_ID_V_UUID, agentId));
        if (!param.key.equals(uuid)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "密钥验证失败");
        }

        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        agentService.setPayPassword(agent.getId(), param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    /**
     * 114-修改支付密码
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
        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }
        if (StringUtils.isEmpty(agent.getPayPassword())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码没有设置");
        }
        if (!agent.getPayPassword().equals(param.oldPassword)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        agentService.setPayPassword(agent.getId(), param.payPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    @ResponseBody
    @RequestMapping(value = "/balance.htm")
    public RestResult balance() {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "AgentId错误");
        }

        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Map data = new HashMap();
        data.put("balance", agent.getBalance());
        data.put("withdrawRatio", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WITHDRAW_RATIO.getValue()));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ForegiftBalanceParam {
        public int category;
    }

    @ResponseBody
    @RequestMapping(value = "/foregift_balance.htm")
    public RestResult foregiftBalance(@Valid @RequestBody ForegiftBalanceParam param) {
        int agentId = getTokenData().agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "AgentId错误");
        }

        Agent agent = agentService.find(agentId);
        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        Map data = new HashMap();

        if(param.category == ConstEnum.Category.EXCHANGE.getValue()){

            data.put("foregiftBalance", agent.getForegiftBalance());
            data.put("foregiftRemainMoney", agent.getForegiftRemainMoney());
            data.put("foregiftBalanceRatio", agent.getForegiftBalanceRatio());

            int ratio = 100;
            String systemRatio = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId());
            if(StringUtils.isNotEmpty(systemRatio)) {
                ratio = Integer.parseInt(systemRatio);
            }
            int floorBalance =  agent.getForegiftBalance() * ratio / 100;
            int withdrawMoney = agent.getForegiftRemainMoney() - floorBalance;
            data.put("withdrawMoney",withdrawMoney);
        }else if(param.category == ConstEnum.Category.RENT.getValue()){

            data.put("foregiftBalance", agent.getZdForegiftBalance());
            data.put("foregiftRemainMoney", agent.getZdForegiftRemainMoney());
            data.put("foregiftBalanceRatio", agent.getZdForegiftBalanceRatio());

            int ratio = 100;
            String systemRatio = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_FOREGIFT_KEEP_RATIO.getValue(), agent.getId());
            if(StringUtils.isNotEmpty(systemRatio)) {
                ratio = Integer.parseInt(systemRatio);
            }
            int floorBalance =  agent.getZdForegiftBalance() * ratio / 100;
            int withdrawMoney = agent.getZdForegiftRemainMoney() - floorBalance;
            data.put("withdrawMoney",withdrawMoney);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }
}
