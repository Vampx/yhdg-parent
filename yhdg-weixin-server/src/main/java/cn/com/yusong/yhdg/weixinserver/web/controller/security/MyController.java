package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
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
@RequestMapping(value = "/my")
public class MyController extends AgentBaseController {
    private final static Logger log = LogManager.getLogger(MyController.class);

    public static final String MP_PATH = "/v_vee/myPage";
    public static final String FW_PATH = "/v_vee/myPage";

    @NotLogin
    @RequestMapping(value = "/index.htm")
    public void index(HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("User-Agent: {}", request.getHeader("User-Agent"));
        }
        int appId = 1;
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            systemMpIndex(appId, MP_PATH, "my", "STATE", httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwIndex(appId, FW_PATH, "my", "STATE", httpSession, response);
        }
    }

    @NotLogin
    @RequestMapping(value = "/index_{appId}.htm")
    public void index(@PathVariable("appId")int appId, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("User-Agent: {}", request.getHeader("User-Agent"));
        }
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            systemMpIndex(appId, MP_PATH, "my", "STATE", httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwIndex(appId, FW_PATH, "my", "STATE", httpSession, response);
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
            systemMpSnsApiBase(appId, code, state, MP_PATH, "my", httpSession, response);
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            systemFwSnsApiBase(appId, auth_code, state, FW_PATH, "my", httpSession, response);
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
