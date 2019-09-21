package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.weixinserver.constant.AppConstant;
import cn.com.yusong.yhdg.weixinserver.entity.SessionUser;
import cn.com.yusong.yhdg.weixinserver.service.basic.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping(value = "/laxin_setting")
public class LaxinSettingController extends AgentBaseController {

    private final static Logger log = LogManager.getLogger(LaxinController.class);
    public static final String MP_PATH = "/laxin/addPullNew";
    public static final String FW_PATH = "/laxin/addPullNew";

    @Autowired
    CustomerService customerService;
    @Autowired
    LaxinService laxinService;
    @Autowired
    LaxinSettingService laxinSettingService;
    @Autowired
    LaxinCustomerService laxinCustomerService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;

    //1 未注册 2 已注册 3 失败

    public static final int STATUS_NOT_REGISTER = 1;
    public static final int STATUS_REGISTERED = 2;
    public static final int STATUS_FAIL = 3;

    @NotLogin
    @RequestMapping(value = "/join.htm")
    public void join1(String v, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("v = {}", v);
        }
        if (log.isDebugEnabled()) {
            log.debug("User-Agent: {}", request.getHeader("User-Agent"));
        }

        if (StringUtils.isEmpty(v) || !StringUtils.isNumeric(v)) {
            responseText(response, "拉新规则无效");
            return;
        }

        LaxinSetting laxinSetting = laxinSettingService.find(Long.parseLong(v));
        if (laxinSetting == null) {
            responseText(response, "拉新规则无效");
            return;
        }

        Agent agent = agentService.find(laxinSetting.getAgentId());
        if (agent == null) {
            responseText(response, "运营商不存在");
            return;
        }

        String targetUrl = null;
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            if (agent.getWeixinmpId() == null) {
                responseText(response, "运营商没有关联公众号");
                return;
            }
            response.sendRedirect( String.format("%s/laxin_setting/index_%d.htm?v=%s", appConfig.contextPath, agent.getWeixinmpId(), v));

        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            if (agent.getAlipayfwId() == null) {
                responseText(response, "运营商没有关联生活号");
                return;
            }
            response.sendRedirect( String.format("%s/laxin_setting/index_%d.htm?v=%s", appConfig.contextPath, agent.getAlipayfwId(), v));
        }
    }

    @NotLogin
    @RequestMapping(value = "/index_{appId}.htm")
    public void index(@PathVariable("appId")int appId, String v, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("User-Agent: {}", request.getHeader("User-Agent"));
        }
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            systemMpIndex(appId, MP_PATH, "laxin_setting", v, httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwIndex(appId, FW_PATH, "laxin_setting", v, httpSession, response);
        }
    }

    @NotLogin
    @RequestMapping(value = "/system_sns_api_base_{appId}.htm")
    public void systemSnsApiBase(@PathVariable("appId")int appId,  /*weixin*/String code,  /*alipay*/String auth_code, String state, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("User-Agent: {}", request.getHeader("User-Agent"));
        }
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            systemMpSnsApiBase(appId, code, state, MP_PATH, "laxin_setting", httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwSnsApiBase(appId, auth_code, state, FW_PATH, "laxin_setting", httpSession, response);
        }
    }

    @NotLogin
    @RequestMapping(value = "/{openId}/agent_sns_api_user_info_{appId}.htm")
    public void agentSnsApiUserInfo(@PathVariable("appId")int appId, @PathVariable("openId")String openId, /*weixin*/String code, /*alipay*/String auth_code, String state, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            agentMpSnsApiUserInfo(appId, openId, code, state, MP_PATH, httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            agentFwSnsApiUserInfo(appId, openId, auth_code, state, FW_PATH, httpSession, response);
        }
    }

    protected String buildRedirectUrl(String url, SessionUser sessionUser, String state) {

        int status = 0; //1 未注册 2 已注册 3 失败
        String msg = "";
        if (StringUtils.isEmpty(state) || !StringUtils.isNumeric(state)) {
            status = STATUS_FAIL;
            msg = "拉新规则无效";
        }

        long laxinSettingId = 0;
        LaxinSetting laxinSetting = laxinSettingService.find(Long.parseLong(state));
        if (laxinSetting == null) {
            status = STATUS_FAIL;
            msg = "拉新规则无效";
        } else {
            laxinSettingId = laxinSetting.getId();
        }

        Agent agent = agentService.find(laxinSetting.getAgentId());
        if (agent == null) {
            status = STATUS_FAIL;
            msg = "运营商不存在";
        }


        String mobile = "";
        if (status != STATUS_FAIL) {
            Customer customer = customerService.findByMpOpenId(agent.getPartnerId(), sessionUser.getOpenId());
            if (customer == null) {
                status = STATUS_NOT_REGISTER;
            } else {
                Laxin laxin = laxinService.findByAgentMobile(agent.getId(), customer.getMobile());
                if (laxin == null) {
                    mobile = customer.getMobile();
                    status = STATUS_NOT_REGISTER;
                } else {
                    status = STATUS_REGISTERED;
                    msg = "手机号已注册拉新人员";
                }
            }
        }

        url += url.contains("?") ? "&" : "?";
        url = String.format("%smobile=%s&laxinSettingId=%d&status=%d&msg=%s",
                url,
                mobile,
                laxinSettingId,
                status,
                AppUtils.encodeUrl(msg, Constant.ENCODING_UTF_8));

        return url;
    }

    @Override
    protected boolean forceSubscribe() {
        return false;
    }
}
