package cn.com.yusong.yhdg.webserver.web.controller.security;

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
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.AppConstant;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.*;
import cn.com.yusong.yhdg.webserver.service.hdg.*;
import cn.com.yusong.yhdg.webserver.utils.AppUtils;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/security/main")
public class MainController extends SecurityController {
    final static Logger log = LogManager.getLogger(MainController.class);
    @Autowired
    AgentService agentService;
    @Autowired
    PlatformDayStatsService platformDayStatsService;
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
    PersonService personService;
    @Autowired
    PartService partService;
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
        model.addAttribute("configValue", systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.AGENT_LOGIN_URL.getValue()));
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
        SessionUser sessionUser = new SessionUser();
        Person dbPerson = personService.findByMobile(username);
        //登录名为手机号，并且在新用户表中注册，走新的登录方式
        if (dbPerson != null) {
            Person person = personService.findByLoginInfo(username, password);
            if (person == null) {
                return ExtResult.failResult("用户名或密码错误");
            }
            if (person.getIsActive() == null || person.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
                return ExtResult.failResult("用户未启用");
            }
            List<Part> partList = partService.findList(person.getMobile(), Part.PartType.PLATFORM.getValue());
            if (partList.size() == 0) {
                return ExtResult.failResult("该用户没有平台用户角色");
            }
            if (person.getIsProtected() != null) {
                sessionUser.setPlatformAdmin(person.getIsProtected() == ConstEnum.Flag.TRUE.getValue());
            }
            personService.updateLoginTime(person.getId());
            sessionUser.setId(person.getId());
            sessionUser.setUsername(person.getFullname());
            sessionUser.setPortrait(appConfig.staticUrl + person.getPhotoPath());
            sessionUser.setType(1);
            if (person.getAgentId() != null) {
                sessionUser.setAgentId(person.getAgentId());
            }
            if (person.getIsAdmin() != null) {
                sessionUser.setAdmin(person.getIsAdmin() == ConstEnum.Flag.TRUE.getValue());
            }
            session.setAttribute(Constant.SESSION_KEY_USER, sessionUser);
            Part part = partList.get(0);
            sessionUser.setRoleName(part.getPartName());
            sessionUser.setOperCodeSet(partService.loadOperCode(part.getId()));
            return ExtResult.successResult();
        }else{
            return oldLoginMethod(username, password, session, sessionUser);
        }
    }

    private ExtResult oldLoginMethod(String username, String password, HttpSession session, SessionUser sessionUser) {
        User user = userService.findByLoginInfo(username, password);
        if (user == null) {
            return ExtResult.failResult("用户名或密码错误");
        }
        if (user.getIsActive() == null || user.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
            return ExtResult.failResult("用户未启用");
        }
        if (user.getAccountType() != User.AccountType.PLATFORM.getValue()) {
            return ExtResult.failResult("仅限平台用户登录");
        }
        if (user.getIsProtected() != null) {
            sessionUser.setPlatformAdmin(user.getIsProtected() == ConstEnum.Flag.TRUE.getValue());
        }
        userService.updateLoginTime(user.getId());
        sessionUser.setId(user.getId());
        //sessionUser.setUsername(user.getLoginName());2019年9月18日17:32:13 将登陆账号LoginName改为全名Fullname
        sessionUser.setUsername(user.getFullname());
        sessionUser.setDeptName(user.getDeptName());
        sessionUser.setPortrait(user.getPhotoPath());
        sessionUser.setType(1);
        if (user.getAgentId() != null) {
            sessionUser.setAgentId(user.getAgentId());
        }
        if (user.getIsAdmin() != null) {
            sessionUser.setAdmin(user.getIsAdmin() == ConstEnum.Flag.TRUE.getValue());
        }

        session.setAttribute(Constant.SESSION_KEY_USER, sessionUser);
        if (user.getRoleId() != null) {
            Role role = roleService.find(user.getRoleId());
            sessionUser.setRoleName(role.getRoleName());
            sessionUser.setOperCodeSet(roleService.loadOperCode(user.getRoleId()));
        }
        return ExtResult.successResult();
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
        PlatformDayStats todayPlatformDayStats = platformDayStatsService.findByDate(todayStatsDate);
        PlatformDayStats yesterdayPlatformDayStats = platformDayStatsService.findByDate(yesterdayStatsDate);
        if (yesterdayPlatformDayStats != null) {
            //昨日新增客户数
            model.addAttribute("yesterdayCustomerCount", yesterdayPlatformDayStats.getIncrementCustomerCount());
            //昨日新增订单
            model.addAttribute("yesterdayBatteryOrderCount", yesterdayPlatformDayStats.getIncrementExchangeCount());
            //昨日新增设备总数
            model.addAttribute("yesterdayCabinetCount", yesterdayPlatformDayStats.getIncrementCabinetCount());
        }
        if (todayPlatformDayStats != null) {
            //客户总数
            model.addAttribute("customerCount", todayPlatformDayStats.getTotalCustomerCount());
            //今日新增客户数
            model.addAttribute("todayCustomerCount", todayPlatformDayStats.getIncrementCustomerCount());
            //总订单数
            model.addAttribute("batteryOrderCount", todayPlatformDayStats.getTotalExchangeCount());
            //今日新增订单
            model.addAttribute("todayBatteryOrderCount", todayPlatformDayStats.getIncrementExchangeCount());
            //设备总数
            model.addAttribute("cabinetCount", todayPlatformDayStats.getTotalCabinetCount());
            //今日新增设备总数
            model.addAttribute("todayCabinetCount", todayPlatformDayStats.getIncrementCabinetCount());
            //总建议数
            model.addAttribute("feedbackCount", todayPlatformDayStats.getTotalFeedbackCount());
            //今日新增建议数
            model.addAttribute("todayFeedbackCount", todayPlatformDayStats.getIncrementFeedbackCount());
            //电池故障数
            model.addAttribute("batteryFaultFeedbackCount", faultFeedbackService.findFaultFeedbackCount(FaultFeedback.FaultType.BATTERY_FAULT.getValue()));
            //设备故障数
            model.addAttribute("cabinetFaultFeedbackCount", faultFeedbackService.findFaultFeedbackCount(FaultFeedback.FaultType.CABINET_FAULT.getValue()));
            //总押金
            model.addAttribute("foregiftMoney", todayPlatformDayStats.getTotalForegiftMoney());
            //总充值
            model.addAttribute("depositMoney", todayPlatformDayStats.getTotalDepositMoney());
            //总套餐
            model.addAttribute("packetPeriodMoney", todayPlatformDayStats.getTotalPacketPeriodMoney());
            //总换电
            model.addAttribute("batteryOrderMoney", todayPlatformDayStats.getTotalExchangeMoney());
            //收入总比率
            model.addAttribute("incomeRatio", todayPlatformDayStats.getTotalForegiftMoney() + todayPlatformDayStats.getTotalDepositMoney() + todayPlatformDayStats.getTotalPacketPeriodMoney() + todayPlatformDayStats.getTotalExchangeMoney());

            //总退款押金
            model.addAttribute("foregiftOrderRefundMoney", todayPlatformDayStats.getTotalRefundForegiftMoney());
            //总退款充值
            model.addAttribute("depositRefundMoney", todayPlatformDayStats.getTotalRefundDepositMoney());
            //总退款套餐
            model.addAttribute("packetPeriodRefundMoney", todayPlatformDayStats.getTotalRefundPacketPeriodMoney());
            //总退款换电
            model.addAttribute("batteryOrderRefundMoney", todayPlatformDayStats.getTotalRefundExchangeMoney());
            //退款总比率
            model.addAttribute("refundRatio", todayPlatformDayStats.getTotalRefundForegiftMoney() + todayPlatformDayStats.getTotalRefundPacketPeriodMoney());

            //平台总收入
            model.addAttribute("sumMoney", todayPlatformDayStats.getTotalPlatformIncome() + todayPlatformDayStats.getTotalForegiftMoney() + todayPlatformDayStats.getTotalDepositMoney() + todayPlatformDayStats.getTotalPacketPeriodMoney() + todayPlatformDayStats.getTotalExchangeMoney());
            //平台总退款
            model.addAttribute("refundTotalMoney", todayPlatformDayStats.getTotalRefundMoney());
        }

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
