package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.web.controller.AbstractController;
import cn.com.yusong.yhdg.weixinserver.config.AppConfig;
import cn.com.yusong.yhdg.weixinserver.constant.AppConstant;
import cn.com.yusong.yhdg.weixinserver.entity.SessionUser;
import cn.com.yusong.yhdg.weixinserver.service.basic.*;
import cn.com.yusong.yhdg.weixinserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.weixinserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.weixinserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.weixinserver.service.hdg.StationService;
import cn.com.yusong.yhdg.weixinserver.service.zc.VehicleService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SuperController extends AbstractController {

    private static final Logger log = LogManager.getLogger(SuperController.class);

    @Autowired
    protected AppConfig appConfig;
    @Autowired
    protected PartnerFwOpenIdService partnerFwOpenIdService;
    @Autowired
    protected AlipayfwOpenIdService alipayfwOpenIdService;
    @Autowired
    protected AlipayfwSubscribeService fwSubscribeService;
    @Autowired
    protected WxMpServiceHolder wxMpServiceHolder;
    @Autowired
    protected PartnerMpOpenIdService partnerMpOpenIdService;
    @Autowired
    protected WeixinmpOpenIdService weixinmpOpenIdService;
    @Autowired
    protected AlipayfwClientHolder alipayClientHolder;
    @Autowired
    protected WeixinmpSubscribeService mpSubscribeService;
    @Autowired
    protected AgentService agentService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    protected PartnerService partnerService;
    @Autowired
    protected WeixinmpService weixinmpService;
    @Autowired
    protected AlipayfwService alipayfwService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ShopService shopService;
    @Autowired
    StationService stationService;

    @ModelAttribute
    public void addPid(String pid, Model model) {
        model.addAttribute("pid", pid);
    }

    public AppConfig getAppConfig() {
        return appConfig;
    }

    protected SessionUser getSessionUser(HttpSession httpSession) {
        return (SessionUser) httpSession.getAttribute(AppConstant.SESSION_KEY_USER);
    }

    public String getQrcodeName() {
        return "qrcode";
    }



    protected String getAppHtmlPath(int appId, String htmlPath) {
        return htmlPath;
//        if(appId == Constant.SYSTEM_PARTNER_ID) {
//            return htmlPath;
//        } else {
//            return htmlPath.replace("/v_vee/", String.format("/v_vee_%d/", appId));
//        }
    }

    protected String[] splitUnionOpenId(String unionOpenId) {
        String[] value = StringUtils.split(unionOpenId, ":");
        if(value.length == 3) {
            return value;
        } else {
            return new String[] {value[0], value[1], null};
        }
    }

    protected String staticUrl(String webPath) {
        if (StringUtils.isEmpty(webPath)) {
            return webPath;
        } else {
            return getAppConfig().staticUrl + webPath;
        }
    }

    public int getBrowserType(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");

        if (StringUtils.isNotEmpty(userAgent)) {
            if (userAgent.indexOf("MicroMessenger") >= 0) {
                return AppConstant.CLIENT_TYPE_MP;
            }
            if (userAgent.indexOf("AliApp") >= 0) {
                return AppConstant.CLIENT_TYPE_FW;
            }

        }
        return 0;
    }

    protected void writeCookie(int appId, HttpServletResponse response) {
        Cookie cookie = new Cookie(Constant.COOKIE_NAME_APP_ID, String.valueOf(appId));
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    protected void responseText(HttpServletResponse response, String text) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        if (StringUtils.isNotEmpty(text)) {
            response.getOutputStream().write(text.getBytes("UTF-8"));
        }
    }
}
