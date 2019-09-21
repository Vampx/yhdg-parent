package cn.com.yusong.yhdg.appserver.web.controller.api.v1.common;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.entity.Day;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_common_basic_query")
@RequestMapping(value = "/api/v1/common/basic/query")
public class QueryController extends ApiController {
    final static Logger log = LogManager.getLogger(QueryController.class);
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CustomerService customerService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    WeixinmpService weixinmpService;
    @Autowired
    AlipayfwService alipayfwService;
    @Autowired
    PhoneappService phoneappService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SettingByMpParam {
        public int appId;
    }
    /**
     * 查询设置
     *
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/setting_by_mp")
    public RestResult SettingByMp(@RequestBody SettingByMpParam param, HttpServletRequest request) {
        return setting(param.appId, 0, 0, request);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SettingByFwParam {
        public int appId;
    }
    /**
     * 查询设置
     *
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/setting_by_fw")
    public RestResult SettingByFwParam(@RequestBody SettingByFwParam param, HttpServletRequest request) {
        return setting(0, param.appId, 0, request);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SettingByAppParam {
        public int appId;
    }
    /**
     * 查询设置
     *
     * @return
     */
    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/setting_by_app")
    public RestResult setting(@RequestBody SettingByAppParam param, HttpServletRequest request) {
        return setting(0, 0, param.appId, request);
    }

    public RestResult setting(int mpId, int fwId, int appId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        TokenCache.Data tokenData=null;
        if (token != null) {
            token = token.replace("Bearer ", "");
            tokenData = tokenCache.get(token);
        }

        String tel = null, lowVolume = null,logoImagePath = null, attentionImagePath= null, weiXinId =null ,alipayId =null ,mpName = null;

        if (tokenData != null && tokenData.customerId != 0) {
            Customer customer = customerService.find(tokenData.customerId);
            if (customer != null ) {
                if (customer.getAgentId() != null) {
                    String agentTel = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.SYSTEM_TEL.getValue(), customer.getAgentId());
                    String agentLowVolume = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.LOW_VOLUME.getValue(), customer.getAgentId());
                    if (StringUtils.isNotEmpty(agentTel)) {
                        tel = agentTel;
                    }
                    if (StringUtils.isNotEmpty(agentLowVolume)) {
                        lowVolume = agentLowVolume;
                    }
                }
            }
        }

        if (StringUtils.isEmpty(tel)) {
            if (mpId > 0) {
                Weixinmp weixinmp = weixinmpService.find(mpId);
                if (weixinmp != null) {
                    tel = weixinmp.getSystemTel();
                    weiXinId = weixinmp.getWeiXinId();
                    attentionImagePath = weixinmp.getAttentionImagePath();
                    logoImagePath = weixinmp.getLogoImagePath();
                    mpName = weixinmp.getAppName();
                }

            } else if (fwId > 0) {
                Alipayfw alipayfw = alipayfwService.find(fwId);
                if (alipayfw != null) {
                    tel = alipayfw.getSystemTel();
                    alipayId = alipayfw.getAlipayId();
                    attentionImagePath = alipayfw.getAttentionImagePath();
                    logoImagePath = alipayfw.getLogoImagePath();
                }
            } else if (appId > 0) {
                Phoneapp phoneapp = phoneappService.find(appId);
                if (phoneapp != null) {
                    tel = phoneapp.getSystemTel();
                    logoImagePath = phoneapp.getLogoImagePath();
                }
            }
        }

        if (StringUtils.isEmpty(tel)) {
            tel = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.SYSTEM_TEL.getValue());
        }

        if (StringUtils.isEmpty(lowVolume)) {
            lowVolume = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.LOW_VOLUME.getValue());
        }

        String staticUrl = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.STATIC_URL.getValue());
        Map map = new HashMap();
        map.put("tel", tel);
        map.put("staticUrl", staticUrl);
        //默认获取系统
        String aboutUs = staticUrl + ConstEnum.RichText.ABOUT_US.getUrl();
        String registerProtocol = staticUrl + ConstEnum.RichText.USER_PROTOCOL.getUrl();

        //如果存在用户，用户存在运营商id，对应运营商是自平台类型，获取对应平台的配置
        if (mpId > 0) {
            aboutUs = staticUrl + "/static/rich_text/" + String.format("mp_%d_%d.html", 1, mpId);
            registerProtocol = staticUrl + "/static/rich_text/" + String.format("mp_%d_%d.html", 2, mpId);
            map.put("weiXinId", weiXinId);
            map.put("mpName", mpName);
            map.put("attentionImagePath", attentionImagePath);
            map.put("logoImagePath", logoImagePath);
        }
        if (fwId > 0) {
            aboutUs = staticUrl + "/static/rich_text/" + String.format("fw_%d_%d.html", 1, fwId);
            registerProtocol = staticUrl + "/static/rich_text/" + String.format("fw_%d_%d.html", 2, fwId);
            map.put("alipayId", alipayId);
            map.put("attentionImagePath", attentionImagePath);
            map.put("logoImagePath", logoImagePath);
        }
        if (appId > 0) {
            aboutUs = staticUrl + "/static/rich_text/" + String.format("app_%d_%d.html", 1, appId);
            registerProtocol = staticUrl + "/static/rich_text/" + String.format("app_%d_%d.html", 2, appId);
            map.put("logoImagePath", logoImagePath);
        }

        map.put("aboutUs", aboutUs);
        map.put("registerProtocol", registerProtocol);
        map.put("faq", null);
        map.put("lowVolume", Integer.parseInt(lowVolume));

        List<Map> customerNoticeMessageTypeList = new ArrayList<Map>();
        for (CustomerNoticeMessage.Type type : CustomerNoticeMessage.Type.values()) {
            Map line = new HashMap();
            line.put("value", type.getValue());
            line.put("name", type.getName());
            customerNoticeMessageTypeList.add(line);
        }
        map.put("customerNoticeMessageTypeList", customerNoticeMessageTypeList);

        List<Map> userNoticeMessageTypeList = new ArrayList<Map>();
        for (UserNoticeMessage.Type type : UserNoticeMessage.Type.values()) {
            Map line = new HashMap();
            line.put("value", type.getValue());
            line.put("name", type.getName());
            userNoticeMessageTypeList.add(line);
        }
        map.put("userNoticeMessageTypeList", userNoticeMessageTypeList);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
    }

    public static class MonthCalendarParam {
        public String month[];
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/month_calendar.htm")
    public RestResult monthCalendar(@Valid @RequestBody MonthCalendarParam param) throws IOException {
        List data = new ArrayList();

        for(String e : param.month) {
            String key = CacheKey.key(CacheKey.K_MONTH_V_MONTH_DAY, e);
            List<Day> value = (List<Day>) memCachedClient.get(key);

            if(value == null || value.isEmpty()) {
                value = AppUtils.getMonthCalendar(e);
                memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
            }

            Map line = new HashMap();
            data.add(line);
            line.put("month", e);

            List dayList = new ArrayList();
            line.put("list", dayList);
            for(Day day : value) {
                Map map = new HashMap();
                map.put("day", day.day);
                map.put("holiday", day.holiday ? 1 : 0);
                map.put("lunar", day.lunar);

                int workDay = 0;
                if(day.workDay == Day.DAY_TYPE_HOLIDAY) {
                    workDay = 0;
                } else if(day.workDay == Day.DAY_TYPE_APPEND) {
                    workDay = 2;
                } else if(day.workDay == Day.DAY_TYPE_NORMAL) {
                    workDay = 1;
                }

                map.put("workDay", workDay);
                dayList.add(map);
            }
        }

        return RestResult.dataResult(0, null, data);
    }
}
