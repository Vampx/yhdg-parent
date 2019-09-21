package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.weixinserver.constant.AppConstant;
import cn.com.yusong.yhdg.weixinserver.entity.SessionUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping(value = "/cabinet_qrcode")
public class CabinetQrcodeController extends AgentBaseController {

    private final static Logger log = LogManager.getLogger(CabinetQrcodeController.class);
    public static final String MP_PATH = "/v_vee/scan/scan?client=mp";
    public static final String FW_PATH = "/v_vee/scan/scan?client=fw";

    @NotLogin
    @RequestMapping(value = "/index.htm")
    public void index(String v, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("v = {}", v);
        }
        if (log.isDebugEnabled()) {
            log.debug("User-Agent: {}", request.getHeader("User-Agent"));
        }

        Cabinet cabinet = cabinetService.find(v);
        if (cabinet == null) {
            responseText(response, "二维码无效");
            return;
        }
        if (cabinet.getAgentId() == null) {
            responseText(response, "设备没有关联运营商");
            return;
        }

        Agent agent = agentService.find(cabinet.getAgentId());
        if (agent == null) {
            responseText(response, "运营商不存在");
            return;
        }

        int appId = 0;
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            if (agent.getWeixinmpId() == null) {
                responseText(response, "运营商没有绑定公众号");
                return;

            } else {
                appId = agent.getWeixinmpId();
            }
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            if (agent.getAlipayfwId() == null) {
                responseText(response, "运营商没有绑定生活号");
                return;

            } else {
                appId = agent.getAlipayfwId();
            }
        }

        if (appId > 0) {
            String url = String.format("%s/cabinet_qrcode/index_%d.htm?v=%s", appConfig.contextPath, appId, v);

            if (log.isDebugEnabled()) {
                log.debug("redirectUrl: {}", url);
            }
            response.sendRedirect(url);
        }

    }

    @NotLogin
    @RequestMapping(value = "/index_{appId}.htm")
    public void index(@PathVariable("appId")int appId, String v, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("v = {}", v);
        }
        if (log.isDebugEnabled()) {
            log.debug("User-Agent: {}", request.getHeader("User-Agent"));
        }
        v = "/cabinet_qrcode/index.htm?v=" + v;
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            systemMpIndex(appId, MP_PATH, "cabinet_qrcode", v, httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwIndex(appId, FW_PATH, "cabinet_qrcode", v, httpSession, response);
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
            systemMpSnsApiBase(appId, code, state, MP_PATH, "cabinet_qrcode", httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwSnsApiBase(appId, auth_code, state, FW_PATH, "cabinet_qrcode", httpSession, response);
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
}
