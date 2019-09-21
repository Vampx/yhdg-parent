package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.basic;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerCouponTicketMapper;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.apache.commons.lang.time.DateUtils;
import org.apache.ibatis.annotations.Param;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("api_v1_customer_basic_laxin")
@RequestMapping(value = "/api/v1/customer/basic/laxin")
public class LaxinController extends ApiController {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CustomerService customerService;
    @Autowired
    LaxinService laxinService;
    @Autowired
    LaxinSettingService laxinSettingService;
    @Autowired
    LaxinCustomerService laxinCustomerService;
    @Autowired
    LaxinRecordService laxinRecordService;
    @Autowired
    CustomerCouponTicketService customerCouponTicketService;

    @ResponseBody
    @RequestMapping(value = "/find_laxin_list")
    public RestResult findLaxinList() {
        TokenCache.Data tokenData = getTokenData();

        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }

        List<Laxin> laxinList = laxinService.findByMobile(customer.getPartnerId(), customer.getMobile());

        List<Map> data = new ArrayList<Map>();
        for (Laxin laxin : laxinList) {
            Map line = new HashMap();
            line.put("id", laxin.getId());
            line.put("agentName", laxin.getAgentName());
            line.put("agentCode", laxin.getAgentCode());
            data.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginByIdParam {
        public long id;
    }

    @ResponseBody
    @RequestMapping(value = "/login_by_id")
    public RestResult loginById(@Valid @RequestBody LoginByIdParam param) {
        TokenCache.Data tokenData = getTokenData();

        Laxin laxin = laxinService.find(param.id);
        if (laxin == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户不存在");
        }

        Customer customer = customerService.find(tokenData.customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户不存在");
        }

        if (!customer.getMobile().equals(laxin.getMobile())) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户没有权限");
        }

        if (laxin.getIsActive() == null || laxin.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "账户被禁用");
        }

        Map data = new HashMap();
        data.put("id", laxin.getId());
        data.put("agentName", laxin.getAgentName());
        data.put("agentCode", laxin.getAgentCode());

        tokenData.laxinId = laxin.getId();
        int expireIn = MemCachedConfig.CACHE_THREE_DAY;
        tokenCache.put(tokenData, expireIn);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FindByPasswordParam {
        public String mobile;
        public String password;
    }

    @ResponseBody
    @RequestMapping(value = "/find_by_password")
    public RestResult findByPassword(@Valid @RequestBody FindByPasswordParam param) {
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        List<Laxin> laxinList = laxinService.findByMobile(customer.getPartnerId(), param.mobile);

        List<Map> data = new ArrayList<Map>();
        for (Laxin laxin : laxinList) {
            if (laxin.getPassword().equals(param.password)) {
                Map line = new HashMap();
                line.put("id", laxin.getId());
                line.put("agentName", laxin.getAgentName());
                line.put("agentCode", laxin.getAgentCode());
                data.add(line);
            }
        }

        if (data.isEmpty()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户名或密码错误");
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoginByPasswordParam {
        public long id;
        public String mobile;
        public String password;
    }

    @ResponseBody
    @RequestMapping(value = "/login_by_password")
    public RestResult loginByPassword(@Valid @RequestBody LoginByPasswordParam param) {
        TokenCache.Data tokenData = getTokenData();

        Laxin laxin = laxinService.find(param.id);
        if (laxin == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户不存在");
        }
        if (laxin.getMobile().equals(param.mobile) && laxin.getPassword().equals(param.password)) {
        } else {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号或密码错误");
        }
        if (laxin.getIsActive() == null || laxin.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "账户被禁用");
        }

        Map data = new HashMap();
        data.put("id", laxin.getId());
        data.put("agentName", laxin.getAgentName());
        data.put("agentCode", laxin.getAgentCode());

        tokenData.laxinId = laxin.getId();
        int expireIn = MemCachedConfig.CACHE_THREE_DAY;
        tokenCache.put(tokenData, expireIn);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdatePasswordParam {
        public String oldPassword;
        public String newPassword;
    }

    @ResponseBody
    @RequestMapping(value = "/update_password")
    public RestResult loginById(@Valid @RequestBody UpdatePasswordParam param) {
        TokenCache.Data tokenData = getTokenData();

        Laxin laxin = laxinService.find(tokenData.laxinId);
        if (laxin == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户不存在");
        }

        if (!laxin.getPassword().equals(param.oldPassword)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "原密码错误");
        }

        laxinService.updatePassword(laxin.getId(), param.newPassword);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public RestResult info() {
        TokenCache.Data tokenData = getTokenData();

        Laxin laxin = laxinService.find(tokenData.laxinId);
        if (laxin == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户不存在");
        }

        Agent agent = agentService.find(laxin.getAgentId());
        Customer customer = customerService.findByMobile(agent.getPartnerId(), laxin.getMobile());

        Map data = new HashMap();
        data.put("id", laxin.getId());
        data.put("fullname", customer == null ? "" : customer.getFullname());
        data.put("photoPath", customer == null ? "" : staticImagePath(customer.getPhotoPath()));
        data.put("customerId", customer == null ? 0 : customer.getId());
        data.put("mobile", laxin.getMobile());
        data.put("agentName", laxin.getAgentName());
        data.put("agentCode", laxin.getAgentCode());
        data.put("laxinUrl", String.format("%s/join_laxin/join.htm?v=%d", config.weixinUrl, laxin.getId()));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @ResponseBody
    @RequestMapping(value = "/index_stats_info")
    public RestResult indexStatsInfo() {
        TokenCache.Data tokenData = getTokenData();

        Laxin laxin = laxinService.find(tokenData.laxinId);
        if (laxin == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新账户不存在");
        }

        Agent agent = agentService.find(laxin.getAgentId());

        Customer customer = customerService.findByMobile(agent.getPartnerId(), laxin.getMobile());

        Date beingTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addDays(beingTime, 1);
        int todayPayMoney = laxinRecordService.totalMoneyByTransferTime(laxin.getId(), LaxinRecord.Status.SUCCESS.getValue(), beingTime, endTime);
        int totalPayCount = laxinRecordService.totalCountByTransferTime(laxin.getId(), LaxinRecord.Status.SUCCESS.getValue(), beingTime, endTime);
        int totalWaitPayMoney = laxinRecordService.totalMoneyByStatus(laxin.getId(), new int[] {LaxinRecord.Status.WAIT.getValue(), LaxinRecord.Status.TRANSFER.getValue(), LaxinRecord.Status.FAIL.getValue()});

        Map data = new HashMap();
        data.put("id", laxin.getId());
        data.put("fullname", customer == null ? "" : customer.getFullname());
        data.put("photoPath", customer == null ? "" : staticImagePath(customer.getPhotoPath()));
        data.put("customerId", customer == null ? 0 : customer.getId());
        data.put("mobile", laxin.getMobile());
        data.put("todayPayMoney", todayPayMoney);
        data.put("totalPayCount", totalPayCount);
        data.put("totalWaitPayMoney", totalWaitPayMoney);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CheckCustomerMobileParam {
        public long laxinId;
        public String mobile;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/check_customer_mobile")
    public RestResult checkCustomerMobile(@Valid @RequestBody CheckCustomerMobileParam param) {
        return laxinCustomerService.checkMobile(param.laxinId, param.mobile);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InputCustomerMobileParam {
        public int type; //
        public String openId;
        public long laxinId;
        public String mobile;
        public String authCode;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/input_customer_mobile")
    public RestResult inputCustomerMobile(@Valid @RequestBody InputCustomerMobileParam param) {
        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }
        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));

        return laxinCustomerService.insert(param.laxinId, param.mobile, param.openId, param.type);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LaxinTicketParam {
        public long ticketId;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/laxin_ticket")
    public RestResult laxinTicket(@Valid @RequestBody LaxinTicketParam param) {
        CustomerCouponTicket ticket = customerCouponTicketService.find(param.ticketId);
        if (ticket == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "优惠券不存在");
        }

        NotNullMap data = new NotNullMap();
        data.put("ticketName", ticket.getTicketName());
        data.putDate("beginTime", ticket.getBeginTime());
        data.putDate("endTime", ticket.getExpireTime());
        data.put("money", ticket.getMoney());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CheckLaxinMobileParam {
        public long laxinSettingId;
        public String mobile;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/check_laxin_mobile")
    public RestResult checkLaxinMobile(@Valid @RequestBody CheckLaxinMobileParam param) {
        LaxinSetting setting = laxinSettingService.find(param.laxinSettingId);
        if (setting == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新规则不存在");
        }

        Laxin laxin = laxinService.findByAgentMobile(setting.getAgentId(), param.mobile);
        if (laxin != null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号已注册拉新账户");
        }

        return RestResult.SUCCESS;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RegisterLaxinParam {
        public int type;
        public String openId;
        public long laxinSettingId;
        public String mobile;
        public String authCode;
        public String password;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/register_laxin")
    public RestResult registerLaxin(@Valid @RequestBody RegisterLaxinParam param) {
        LaxinSetting setting = laxinSettingService.find(param.laxinSettingId);
        if (setting == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "拉新规则不存在");
        }

        String cached = (String) memCachedClient.get(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));
        if (cached == null || !cached.contains(param.authCode)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "验证码错误");
        }
        memCachedClient.delete(CacheKey.key(CacheKey.K_MOBILE_V_AUTH_CODE, param.mobile));

        Laxin laxin = laxinService.findByAgentMobile(setting.getAgentId(), param.mobile);
        if (laxin != null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "手机号已注册拉新账户");
        }

        laxinService.insert(setting, param.mobile, param.password, param.openId, param.type);

        return RestResult.result(RespCode.CODE_0.getValue(), null);
    }

    @ResponseBody
    @RequestMapping(value = "/customer_index")
    public RestResult customerIndex() {
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        int exist = 0;
        int laxinMoney = 0;
        int paySuccessMoney = 0;
        int waitPayMoney = 0;
        int payForegiftCount = 0;
        String laxinUrl = "";

        List<Laxin> laxinList = laxinService.findByMobile(customer.getPartnerId(), customer.getMobile());
        if (!laxinList.isEmpty()) {
            exist = 1;
            Laxin laxin = laxinList.get(0);

            if (laxin.getIncomeType() == Laxin.IncomeType.TIMES.getValue()) {
                laxinMoney = laxin.getLaxinMoney();
            } else {
                laxinMoney = laxin.getPacketPeriodMoney();
            }

            paySuccessMoney = laxinRecordService.totalMoneyByStatus(laxin.getId(), new int[]{LaxinRecord.Status.SUCCESS.getValue()});
            waitPayMoney = laxinRecordService.totalMoneyByStatus(laxin.getId(), new int[]{LaxinRecord.Status.WAIT.getValue(), LaxinRecord.Status.TRANSFER.getValue(), LaxinRecord.Status.FAIL.getValue()});
            payForegiftCount = laxinCustomerService.sumPayForegiftCount(laxin.getId());
            laxinUrl = String.format("%s/join_laxin/join.htm?v=%d", config.weixinUrl, laxin.getId());
        }

        Map data = new HashMap();
        data.put("exist", exist);
        data.put("laxinMoney", laxinMoney);
        data.put("paySuccessMoney", paySuccessMoney);
        data.put("waitPayMoney", waitPayMoney);
        data.put("payForegiftCount", payForegiftCount);
        data.put("laxinUrl", laxinUrl);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }
}
