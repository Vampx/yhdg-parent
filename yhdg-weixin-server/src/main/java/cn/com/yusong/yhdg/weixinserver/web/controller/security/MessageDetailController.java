package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value = "/message_detail")
public class MessageDetailController extends SuperController {

    private final static Logger log = LogManager.getLogger(MessageDetailController.class);

    @NotLogin
    @RequestMapping(value = "/index.htm")
    public void jump2(int appId, String openId, String secondOpenId, String url, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("appId = {}, url = {}, openId = {}, secondOpenId", appId, url, openId, secondOpenId);
        }
        if (log.isDebugEnabled()) {
            log.debug("User-Agent: {}", request.getHeader("User-Agent"));
        }

        String unionId = String.format("%d:%s:%s", appId, openId, secondOpenId);

        url += url.contains("?") ? "&" : "?";

        String location = String.format("%s%sopenId=%s&appId=%d&qrcode=STATE",
                appConfig.contextPath,
                url,
                unionId,
                appId);

        if(log.isDebugEnabled()) {
            log.debug("mpSnsApiBase redirect location : {}", location);
        }

        Weixinmp weixinmp = weixinmpService.find(appId);
        if (weixinmp == null) {
            responseText(response, "公众号配置不存在");
            return;
        }
        if (weixinmp.getPageType() == Weixinmp.PageType.DEFAULT.getValue()) {
            writeCookie(0, response);
        } else {
            writeCookie(weixinmp.getId(), response);
        }
        writeOpenId(unionId, response);
        response.sendRedirect(location);
    }


    protected void writeOpenId(String openId, HttpServletResponse response) {
        Cookie cookie = new Cookie(Constant.COOKIE_NAME_OPEN_ID, openId);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }
}
