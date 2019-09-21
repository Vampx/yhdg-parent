package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.weixinserver.constant.AppConstant;
import cn.com.yusong.yhdg.weixinserver.entity.SessionUser;
import cn.com.yusong.yhdg.weixinserver.entity.result.RestResult;
import cn.com.yusong.yhdg.weixinserver.service.basic.*;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sun.management.resources.agent;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

@Controller
@RequestMapping(value = "/agent_company")
public class AgentCompanyController extends AgentBaseController {

    private final static Logger log = LogManager.getLogger(AgentCompanyController.class);
    public static final String MP_PATH = "/company/bind/newBinding";
    public static final String FW_PATH = "/company/bind/newBinding";

    @Autowired
    CustomerService customerService;
    @Autowired
    LaxinService laxinService;
    @Autowired
    LaxinCustomerService laxinCustomerService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    AgentCompanyService agentCompanyService;
    @Autowired
    AgentCompanyCustomerService agentCompanyCustomerService;

    //1 未注册 2 已绑定 3 绑定失败 4 绑定成功

    public static final int STATUS_NOT_REGISTER = 1;
    public static final int STATUS_BOUND = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_SUCCESS = 4;

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
            responseText(response, "运营公司id无效");
            return;
        }

        AgentCompany agentCompany = agentCompanyService.find(v);
        if (agentCompany == null) {
            responseText(response, "拉新账户无效");
            return;
        }

        Agent agent = agentService.find(agentCompany.getAgentId());
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
            response.sendRedirect( String.format("%s/agent_company/index_%d.htm?v=%s", appConfig.contextPath, agent.getWeixinmpId(), v));

        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            if (agent.getAlipayfwId() == null) {
                responseText(response, "运营商没有关联生活号");
                return;
            }
            response.sendRedirect( String.format("%s/agent_company/index_%d.htm?v=%s", appConfig.contextPath, agent.getAlipayfwId(), v));
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
            systemMpIndex(appId, MP_PATH, "agent_company", v, httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwIndex(appId, FW_PATH, "agent_company", v, httpSession, response);
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
            systemMpSnsApiBase(appId, code, state, MP_PATH, "agent_company", httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwSnsApiBase(appId, auth_code, state, FW_PATH, "agent_company", httpSession, response);
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
        AgentCompany agentCompany = null;
        int status = 0;
        long ticketId = 0;
        String msg = "";

        if (StringUtils.isEmpty(state) || !StringUtils.isNumeric(state)) {
            status = STATUS_FAIL;
            msg = "运营公司无效";
        } else {
            agentCompany = agentCompanyService.find(String.valueOf(state));
            if (agentCompany == null) {
                status = STATUS_FAIL;
                msg = "运营公司无效";
            }
        }
        Agent agent = agentService.find(agentCompany.getAgentId());
        if (agent == null) {
            status = STATUS_FAIL;
            msg = "运营商不存在";
        }

        if (status != STATUS_FAIL) {
            Customer customer = customerService.findByMpOpenId(agent.getPartnerId(), sessionUser.getOpenId());
            if (customer == null) {
                status = STATUS_NOT_REGISTER;
            } else {
                if (!customer.getAgentId().equals(agentCompany.getAgentId())) {
                    status = STATUS_FAIL;
                    msg = "骑手所属运营商与运营公司所属运营商不一致";
                }else{
                    AgentCompanyCustomer agentCompanyCustomer = agentCompanyCustomerService.findByCompanyMobile(agentCompany.getId(), customer.getMobile());
                    if (agentCompanyCustomer == null) {
                        AgentCompanyCustomer newAgentCompanyCustomer = new AgentCompanyCustomer();
                        newAgentCompanyCustomer.setAgentId(agent.getId());
                        newAgentCompanyCustomer.setAgentName(agent.getAgentName());
                        newAgentCompanyCustomer.setAgentCompanyId(agentCompany.getId());
                        newAgentCompanyCustomer.setCustomerId(customer.getId());
                        newAgentCompanyCustomer.setCustomerMobile(customer.getMobile());
                        newAgentCompanyCustomer.setCustomerFullname(customer.getFullname());
                        newAgentCompanyCustomer.setCreateTime(new Date());
                        agentCompanyCustomerService.insert(newAgentCompanyCustomer);
                        customerService.bindCompany(customer.getId(), agentCompany.getId());
                        status = STATUS_SUCCESS;
                        msg = "绑定成功";
                    } else {
                        status = STATUS_BOUND;
                        msg = "手机号已绑定运营公司";
                    }
                }
            }
        }

        url += url.contains("?") ? "&" : "?";
        url = String.format("%sticketId=%d&agentCompanyId=%s&status=%d&msg=%s",
                url,
                ticketId,
                agentCompany.getId(),
                status,
                AppUtils.encodeUrl(msg, Constant.ENCODING_UTF_8));
        return url;
    }

    @Override
    protected boolean forceSubscribe() {
        return false;
    }
}