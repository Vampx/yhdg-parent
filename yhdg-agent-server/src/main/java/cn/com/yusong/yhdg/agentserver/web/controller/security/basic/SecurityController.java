package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.web.controller.AbstractController;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.ImageConvertSizeService;
import cn.com.yusong.yhdg.agentserver.service.basic.MobileMessageService;
import cn.com.yusong.yhdg.agentserver.service.basic.MobileMessageTemplateService;
import cn.com.yusong.yhdg.agentserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentserver.utils.MediaUtils;
import jxl.format.UnderlineStyle;
import jxl.write.Alignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class SecurityController extends AbstractController {

    private final static Logger logger = LogManager.getLogger(SecurityController.class);

    protected static final String SEGMENT_RECORD_NOT_FOUND = "/security/main/segment_record_not_found";
    protected static final String PAGE_RECORD_NOT_FOUND = "/security/main/page_record_not_found";
    protected static final String SEGMENT_MESSAGE = "/security/main/segment_message";
    protected static final String SEGMENT_MAP_RECORD_NOT_FOUND = "/security/main/segment_map_record_not_found";

    protected static final String MENU_CODE_NAME = "menuCode";

    @Autowired
    protected AppConfig appConfig;
    @Autowired
    protected UserService userService;
    @Autowired
    protected MobileMessageTemplateService mobileMessageTemplateService;
    @Autowired
    protected MobileMessageService mobileMessageService;
    @Autowired
    protected ImageConvertSizeService imageConvertSizeService;

    public AppConfig getAppConfig() {
        return appConfig;
    }

    public String getUserQueryParam(HttpSession session) {
        return null;
    }

    @ModelAttribute
    public void addPid(String pid, Model model) {
        model.addAttribute("pid", pid);
    }

    protected File cutDown(File sourceFile, String uuid, String fileSuffix, int stand) throws IOException {
        File targetFile = new File(sourceFile.getParentFile(), uuid +"."+fileSuffix);
        MediaUtils.MediaInfo mediaInfo = MediaUtils.getImageInfo(sourceFile);

        mediaInfo = MediaUtils.getImageSnapshotSize(mediaInfo, stand);
        MediaUtils.makeImageSnapshot(sourceFile, targetFile, fileSuffix, mediaInfo.width, mediaInfo.height);
        return targetFile;
    }

    protected static WritableCellFormat getTitleStyle() throws WriteException {
        WritableFont wf2 = new WritableFont(WritableFont.TAHOMA, 16, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
        WritableCellFormat wc = new WritableCellFormat(wf2);
        // 设置居中
        wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wc.setAlignment(Alignment.CENTRE);
        return wc;
    }

    protected static WritableCellFormat getHeadStyle() throws WriteException {
        WritableFont wf2 = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
        WritableCellFormat wc = new WritableCellFormat(wf2);
        // 设置居中
        wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wc.setAlignment(Alignment.CENTRE);
        return wc;
    }

//    @NotLogin
//    @ExceptionHandler(Exception.class)
//    public ModelAndView exceptionHandler(Exception exception, HttpServletRequest request) throws IOException {
//        logger.error("系统异常", exception);
//
//        String msg = "系统出现错误, 请联系系统管理员。";
//        if(exception instanceof CannotCreateTransactionException) {
//            msg = "数据库无法连接，请联系管理员。";
//        }
//
//        ModelAndView result = new ModelAndView("main/exception");
//        result.addObject("msg", msg);
//        result.addObject("contextPath", request.getContextPath());
//        result.addObject("controller", this);
//        result.addObject("exceptionStackTrace", YcpsUtils.toString(exception));
//        return result;
//    }

    protected SessionUser getSessionUser(HttpServletRequest request) {
        return (SessionUser) request.getSession().getAttribute(Constant.SESSION_KEY_USER);
    }
    protected SessionUser getSessionUser(HttpSession httpSession) {
        return (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
    }

    protected void insertMobileMessage(Integer partnerId, int moduleId, int sourceType, String sourceId, String mobile, String content, Integer type, String variable, String templateCode) {
        MobileMessage mobileMessage = new MobileMessage();
        mobileMessage.setPartnerId(partnerId);
        mobileMessage.setModuleId(moduleId);
        mobileMessage.setSourceId(sourceId);
        mobileMessage.setSourceType(sourceType);
        mobileMessage.setMobile(mobile);
        mobileMessage.setContent(content);
        mobileMessage.setCreateTime(new Date());
        mobileMessage.setStatus(MobileMessage.MessageStatus.NOT.getValue());
        mobileMessage.setType(type);
        mobileMessage.setDelay(MobileMessage.DELAY_TIME_ZERO);
        mobileMessage.setVariable(variable);
        mobileMessage.setTemplateCode(templateCode);
        mobileMessageService.insert(mobileMessage);
    }
}
