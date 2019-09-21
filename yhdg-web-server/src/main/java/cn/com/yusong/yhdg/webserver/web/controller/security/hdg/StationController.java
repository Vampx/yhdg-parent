package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.CustomerInstallmentRecordPayDetail;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.webserver.service.basic.UserService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.service.hdg.StationBizUserService;
import cn.com.yusong.yhdg.webserver.service.hdg.StationDistributionService;
import cn.com.yusong.yhdg.webserver.service.hdg.StationService;
import cn.com.yusong.yhdg.webserver.utils.StatsXlsUtils;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.bcel.verifier.statics.LONG_Upper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/hdg/station")
public class StationController extends SecurityController {
    @Autowired
    StationService stationService;
    @Autowired
    AgentService agentService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    StationDistributionService stationDistributionService;
    @Autowired
    StationBizUserService stationBizUserService;
    @Autowired
    UserService userService;
    @Autowired
    SystemConfigService systemConfigService;

    @SecurityControl(limits = "hdg.Station:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.Station:list");
    }

    @ResponseBody
    @RequestMapping("find_station.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findStation(String stationId) {
        Station station = stationService.find(stationId);
        return DataResult.successResult(station);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Station search) {
        return PageResult.successResult(stationService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        String id = stationService.findMaxId();
        model.addAttribute("activeStatusEnum", Station.ActiveStatus.values());
        model.addAttribute("id", id);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Station entity) {
        return stationService.insert(entity);
    }

    @RequestMapping(value = "station_detail.htm")
    public void stationDetail(Model model, String id, HttpServletRequest request) {
        model.addAttribute("id", id);
        Station entity = stationService.find(id);
        model.addAttribute("entity", entity);
        String pattern= Constant.HOUR_MINUTE;
        if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
            String beginTime = entity.getWorkTime().substring(0, pattern.length());
            String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
            model.addAttribute("beginTime", beginTime);
            model.addAttribute("endTime", endTime);
        }

        List<StationBizUser> bizUserList = stationBizUserService.findListByStationId(id);
        if (bizUserList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (StationBizUser stationBizUser : bizUserList) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                User user = userService.find(stationBizUser.getUserId());
                sb.append(user.getFullname() != null ? user.getFullname() : user.getLoginName());
            }
            String fullnameList = sb.toString();
            model.addAttribute("fullnameList", fullnameList);
        } else {
            model.addAttribute("fullnameList", null);
        }

        model.addAttribute("activeStatusEnum", Station.ActiveStatus.values());
        model.addAttribute("deptTypeEnum", StationDistribution.DeptType.values());

        Map map = new HashMap();
        String url = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue());
        map.put("qrcode", String.format(ConstEnum.Qrcode.QRCODE_STATION.getFormat(), url, entity.getId()));
        model.addAttribute("qrCodeAddress", map.get("qrcode"));

    }
    @ResponseBody
    @RequestMapping("find_biz_user.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findBizUser(String stationId) {
        List<StationBizUser> bizUserList = stationBizUserService.findListByStationId(stationId);
        StringBuilder sb = new StringBuilder();
        if (bizUserList.size() > 0) {
            for (StationBizUser stationBizUser : bizUserList) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                User user = userService.find(stationBizUser.getUserId());
                sb.append(user.getFullname() != null ? user.getFullname() : user.getLoginName());
            }
        }
        return DataResult.successResult(sb.toString());
    }
    @RequestMapping(value = "edit_location.htm")
    public String editLocation(Model model, String id) {
        Station entity = stationService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/station/edit_location";
    }

    @RequestMapping(value = "add_location.htm")
    public String addLocation(Model model) {
        model.addAttribute("lng", Constant.LNG);
        model.addAttribute("lat", Constant.LAT);
        return "/security/hdg/station/add_location";
    }

    @RequestMapping(value = "image.htm", method = RequestMethod.GET)
    public void image(String num, Model model){
        model.addAttribute("num", num);

    }

    @RequestMapping("update_basic.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasic(Station entity) {
        return stationService.updateBasic(entity);
    }

    @RequestMapping("update_pay_people.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updatePayPeople(String id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        stationService.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, payPassword);
        return ExtResult.successResult();
    }

    @RequestMapping(value = "setting_pay_people.htm")
    public void settingPayPeople(Model model, String stationId) {
        Station entity = stationService.find(stationId);
        model.addAttribute("entity", entity);
        model.addAttribute("stationId", stationId);
    }

    @RequestMapping(value = "setting_station_distribution.htm")
    public void settingStationDistribution(Model model, String stationId) {
        List<StationDistribution> stationDistributionList = stationDistributionService.findByStationId(stationId);
        Station entity = stationService.find(stationId);
        model.addAttribute("entity", entity);
        model.addAttribute("stationId", stationId);
        model.addAttribute("stationDistributionList", stationDistributionList);
        model.addAttribute("deptTypeEnum", StationDistribution.DeptType.values());

    }

    @RequestMapping("update_station_distribution.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateStationDistribution(String stationId, Integer [] deptTypeList,
                                               Integer [] isNotFixedList,
                                               Integer [] isFixedList,
                                               Integer [] isFixedPercentList,
                                               Double [] moneyList,
                                               Double [] percentList,
                                               Integer [] numList,
                                               Integer [] operateIdList) {

        return  stationDistributionService.createOrUpdate(stationId, deptTypeList,
                isNotFixedList, isFixedList, isFixedPercentList, moneyList, percentList, numList, operateIdList);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(String id) {
        return stationService.delete(id);
    }

    @RequestMapping("update_new_location.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateNewLocation(Station entity) {
        return stationService.updateNewLocation(entity);
    }

    @RequestMapping(value = "setting_station_role.htm")
    public void settingStationRole(Model model, String stationId) {
        Station entity = stationService.find(stationId);
        model.addAttribute("entity", entity);
        model.addAttribute("stationId", stationId);
    }

    @RequestMapping(value = "setting_station_user.htm")
    public void settingStationUser(Model model, String stationId) {
        Station entity = stationService.find(stationId);
        model.addAttribute("entity", entity);
        model.addAttribute("stationId", stationId);
    }


    @RequestMapping(value = "delete_station_user.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult deleteStationUser(String stationId) {
        return stationBizUserService.delete(stationId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "vip_price_station_page.htm")
    public void vipPriceStationPage(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "price_station_page.htm")
    public void priceStationPage(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }

    //收款人(导出Excel)
    @RequestMapping("pay_people_export_excel.htm")
    public void payPeopleExportExcel(String stationId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File writeFile = new File(String.format("%s/%s", getAppConfig().appDir.getPath(),"收款人信息.xls"));
        OutputStream os = new FileOutputStream(writeFile);
        StatsXlsUtils.writePayPeopleReport(stationService.findListByStationId(stationId), os);
        downloadSupport(writeFile, request, response, "收款人信息.xls");
    }

    //站点分成(导出Excel)
    @RequestMapping("distribution_export_excel.htm")
    public void distributionExportExcel(String stationId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File writeFile = new File(String.format("%s/%s", getAppConfig().appDir.getPath(),"站点分成信息.xls"));
        OutputStream os = new FileOutputStream(writeFile);
        StatsXlsUtils.writeStationDistributionReport(stationDistributionService.findByStationIdReport(stationId), os);
        downloadSupport(writeFile, request, response, "站点分成信息.xls");
    }
}
