package cn.com.yusong.yhdg.weixinserver.web.controller.security;

import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.weixinserver.constant.AppConstant;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping(value = "/cabinet_login_qrcode")
public class CabinetLoginQrcodeController extends BaseController {

    private final static Logger log = LogManager.getLogger(CabinetLoginQrcodeController.class);
    public static final String MP_PATH = "/v_vee/indexList";
    public static final String FW_PATH = "/v_vee/indexList";

    @NotLogin
    @RequestMapping(value = "/index.htm")
    public void index(String v, HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("v = {}", v);
        }

        String url = null;
        int browserType = getBrowserType(request);
        if (browserType == AppConstant.CLIENT_TYPE_MP) {
            url = appConfig.getDomainUrl() + MP_PATH;
        } else if (browserType == AppConstant.CLIENT_TYPE_FW) {
            url = appConfig.getDomainUrl() + FW_PATH;
        }

        if(StringUtils.isNotEmpty(url)) {
            response.setStatus(302);
            response.setHeader("Location", url);
        }
    }
}
