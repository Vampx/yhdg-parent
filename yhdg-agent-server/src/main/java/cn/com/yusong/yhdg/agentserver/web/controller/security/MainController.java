package cn.com.yusong.yhdg.agentserver.web.controller.security;

import cn.com.yusong.yhdg.agentserver.service.MainService;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.utils.AuthImgUtils;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.AppConstant;
import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.*;
import cn.com.yusong.yhdg.agentserver.service.hdg.*;
import cn.com.yusong.yhdg.agentserver.utils.AppUtils;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

@Controller
@RequestMapping(value = "/security/main")
public class MainController extends SecurityController {
    final static Logger log = LogManager.getLogger(MainController.class);
    @Autowired
    AgentService agentService;
    @Autowired
    AgentDayStatsService agentDayStatsService;
    @Autowired
    RoleService roleService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CustomerService customerService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    FeedbackService feedbackService;
    @Autowired
    FaultLogService faultLogService;
    @Autowired
    FaultFeedbackService faultFeedbackService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    CustomerDepositOrderService customerDepositOrderService;
    @Autowired
    MainService mainService;
//    @Autowired
//    AgentConfigService agentConfigService;
    //@Autowired
    //PassportService passportService;
    //@Autowired
    //MemCachedClient memCachedClient;
//    @Autowired
//    EasemobRestFactory easemobRestFactory;
//    @Autowired
//    PushMetaDataService pushMetaDataService;

    @NotLogin
    @RequestMapping(value = "jump.htm")
    public void jump(Model model, String step) {
        model.addAttribute("step", step);
    }

    @NotLogin
    @RequestMapping(value = "login.htm", method = RequestMethod.GET)
    public void login(Model model) {
        model.addAttribute("configValue", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEB_LOGIN_URL.getValue()));
    }

    @NotLogin
    @RequestMapping(value = "auth_img.htm")
    public void authImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        AuthImgUtils.AuthImg authImg = AuthImgUtils.getAuthImg();
        HttpSession session = request.getSession(true);
        session.setAttribute(Constant.SESSION_AUTH_CODE, authImg.authCode);

        response.getOutputStream().write(authImg.buf);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("preview.htm")
    public String preView(String path, Model model) {
        String videoSuffix = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.VIDEO_SUFFIX.getValue());
        String suffix = AppUtils.getFileSuffix(path);
        model.addAttribute("path", path);
        model.addAttribute("staticUrl", appConfig.getStaticUrl());
        if (videoSuffix.indexOf(suffix) != -1) {
            //视频
            return "/security/main/video_preview";
        } else {
            //图片
            return "/security/main/image_preview";
        }
    }

    @NotLogin
    @RequestMapping(value = "login.htm", method = RequestMethod.POST)
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult login(String username, String password, String authCode, HttpSession session, HttpServletRequest request) {

        return mainService.login(username, password, authCode, session);
    }

    @NotLogin
    @RequestMapping(value = "logout.htm")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        if (session != null) {
            session.removeAttribute(Constant.SESSION_KEY_USER);
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/security/main/login.htm");
    }

    @RequestMapping(value = "index.htm")
    public void index(HttpSession httpSession, Model model) {
        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_00.getValue());
        SessionUser sessionUser = getSessionUser(httpSession);
        Agent agent = agentService.find(sessionUser.getAgentId());
        if (agent != null) {
            sessionUser.setAgentName(agent.getAgentName());
            sessionUser.setAgentTel(agent.getTel());
            sessionUser.setAgentMemo(agent.getMemo());
            sessionUser.setModuleId(0);
        }
        Calendar calendar = Calendar.getInstance();
        //今天格式日期
        String todayStatsDate = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd");
        //昨天格式日期
        calendar.add(Calendar.DATE, -1);
        String yesterdayStatsDate = DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd");
        AgentDayStats todayAgentDayStats = agentDayStatsService.findByDate(sessionUser.getAgentId(),todayStatsDate);
        AgentDayStats yesterdayAgentDayStats = agentDayStatsService.findByDate(sessionUser.getAgentId(),yesterdayStatsDate);

        if (yesterdayAgentDayStats != null) {
            //昨日活动客户数
            model.addAttribute("yesterdayActiveCustomerCount", yesterdayAgentDayStats.getActiveCustomerCount());
            //昨日新增订单
            model.addAttribute("yesterdayBatteryOrderCount", yesterdayAgentDayStats.getOrderCount());
        }
        if (todayAgentDayStats != null) {
            //活动客户数
            model.addAttribute("todayActiveCustomerCount", todayAgentDayStats.getActiveCustomerCount());
            //今日新增订单
            model.addAttribute("todayBatteryOrderCount", todayAgentDayStats.getOrderCount());

            //运营商今日收入
            model.addAttribute("todayMoney", todayAgentDayStats.getIncome());
            //运营商今日退款
            model.addAttribute("refundTodayMoney", todayAgentDayStats.getRefundExchangeMoney());

            //换电
            model.addAttribute("batteryOrderMoney", todayAgentDayStats.getAgentExchangeMoney());
            //套餐
            model.addAttribute("packetPeriodMoney", todayAgentDayStats.getAgentPacketPeriodMoney());
            //收入比率
            model.addAttribute("incomeRatio", todayAgentDayStats.getAgentExchangeMoney() + todayAgentDayStats.getAgentPacketPeriodMoney());

            //退款换电
            model.addAttribute("batteryOrderRefundMoney", todayAgentDayStats.getAgentRefundExchangeMoney());
            //退款套餐
            model.addAttribute("packetPeriodRefundMoney", todayAgentDayStats.getAgentRefundPacketPeriodMoney());
            //退款比率
            model.addAttribute("refundRatio", todayAgentDayStats.getAgentRefundExchangeMoney() + todayAgentDayStats.getAgentRefundPacketPeriodMoney());
        }

        //总站点数
        model.addAttribute("cabinetCount", cabinetService.findCabinetCount(sessionUser.getAgentId()));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "password.htm", method = RequestMethod.GET)
    public void password() {
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "password.htm", method = RequestMethod.POST)
    public ExtResult password(String oldPassword, String password, HttpSession session) {
        SessionUser sessionUser = getSessionUser(session);
        int effect = userService.updatePassword(sessionUser.getId(), CodecUtils.password(oldPassword), CodecUtils.password(password));
        if (effect == 0) {
            return ExtResult.failResult("原密码错误");
        } else {
            return ExtResult.successResult();
        }

    }

    @RequestMapping(value = "agent_tree.htm")
    public void agentTree(HttpSession httpSession, HttpServletResponse response) throws IOException {
        OutputStream stream = response.getOutputStream();
        stream.write(getSessionUser(httpSession).getAgentTree());
        stream.flush();
    }

//    @RequestMapping(value = "switch_agent.htm")
//    public void switchAgent(int agentId, HttpSession httpSession, HttpServletResponse response) throws IOException {
//        SessionUser sessionUser = getSessionUser(httpSession);
//        sessionUser.switchAgent(agentId);
//
//        response.setStatus(302);
//        response.setHeader("Location", appConfig.contextPath + "/security/charger_agent/main/index.htm");
//    }


    @RequestMapping(value = "agent.htm")
    public void agent(int agentId, HttpSession httpSession, HttpServletResponse response) {
        Agent agent = agentService.find(agentId);
        SessionUser sessionUser = getSessionUser(httpSession);
        //sessionUser.setModuleId(ConstEnum.Module.BASIC.getValue());
        sessionUser.switchAgent(agent);
        response.setStatus(302);
        response.setHeader("Location", appConfig.contextPath + "/security/main/index.htm");

    }
    @RequestMapping(value = "module.htm")
    public void agent(int moduleId, String url, HttpSession httpSession, HttpServletResponse response) {
        SessionUser sessionUser = getSessionUser(httpSession);
        sessionUser.switchModule(moduleId);
        response.setStatus(302);
        response.setHeader("Location", url);
    }

    @RequestMapping(value = "portrait.htm", method = RequestMethod.GET)
    public void portrait() {
    }

    @RequestMapping(value = "portrait.htm", method = RequestMethod.POST)
    public String portrait(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        String uuid = IdUtils.uuid();
        String fileSuffix = YhdgUtils.getFileSuffix(file.getOriginalFilename());
        File sourceFile = new File(getAppConfig().tempDir, uuid + "." + fileSuffix);
        YhdgUtils.makeParentDir(sourceFile);
        file.transferTo(sourceFile);

        ImageConvertSize imageConvertSize = imageConvertSizeService.find(ImageConvertSize.Type.CUSTOMER_PORTRAIT.getValue());
        if (imageConvertSize == null) {
            model.addAttribute("message", "没有相关的图片标准");
            return SEGMENT_MESSAGE;
        }
        File targetFile = cutDown(sourceFile, IdUtils.uuid(), fileSuffix, imageConvertSize.getStand());
        sourceFile.delete();

        model.addAttribute("filePath", AppConstant.PATH_TEMP + targetFile.getName());
        model.addAttribute("fileName", file.getOriginalFilename());

        return "/security/main/portrait_response";
    }
}
