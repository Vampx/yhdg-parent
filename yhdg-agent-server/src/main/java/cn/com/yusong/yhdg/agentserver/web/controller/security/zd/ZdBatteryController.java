package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.agentserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.agentserver.service.hdg.*;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import jxl.read.biff.BiffException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Controller("ZdBatteryController")
@RequestMapping(value = "/security/zd/battery")
public class ZdBatteryController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(ZdBatteryController.class);

    @Autowired
    BatteryService batteryService;
    @Autowired
    AgentService agentService;
    @Autowired
    DictItemService dictItemService;
    @Autowired
    BatteryOperateLogService batteryOperateLogService;
    @Autowired
    BatteryParameterService batteryParameterService;
    @Autowired
    BatteryTypeIncomeRatioService batteryTypeIncomeRatioService;
    @Autowired
    BatteryInstallRecordService batteryInstallRecordService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    BatteryReportService batteryReportService;

    @SecurityControl(limits = "zd.Battery:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zd.Battery:list");
        model.addAttribute("StatusEnum", Battery.Status.values());
        model.addAttribute("repairStatusList", Battery.RepairStatus.values());
        model.addAttribute("brandList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_BRAND.getValue()));
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Battery search) {
        search.setCategory(Battery.Category.RENT.getValue());
        return PageResult.successResult(batteryService.findPage(search));
    }

    @RequestMapping("page_upgrade.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageUpgrade(Battery search) {
        return PageResult.successResult(batteryService.pageUpgrade(search));
    }

    @RequestMapping(value = "select_battery.htm")
    public void selectBattery(Model model) {
    }

    @RequestMapping(value = "switch_battery.htm")
    public void switchBattery(Model model) {
    }

    @RequestMapping(value = "control.htm")
    public void batteryControl(Model model) {
        model.addAttribute("statusEnum", Battery.Status.values());
        model.addAttribute("chargeStatusEnum", Battery.ChargeStatus.values());
    }

    @RequestMapping("battery_control_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult batteryControlPage(Battery search) {
        return PageResult.successResult(batteryService.findPage(search));
    }

    @RequestMapping(value = "battery_detail_alert.htm")
    public void batteryDetailAlert(Model model, String id) {
        batteryDetail(model, id);
    }

    @RequestMapping(value = "battery_detail.htm")
    public void batteryDetail(Model model, String id) {
        model.addAttribute("id", id);
        model.addAttribute("brandList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_BRAND.getValue()));
        model.addAttribute("heartTypeEnum", BatteryParameter.HeartType.values());
        model.addAttribute("StatusEnum", Battery.Status.values());
        Battery battery = batteryService.find(id);
        model.addAttribute("entity", battery);
        model.addAttribute("shellCode", battery.getShellCode());

        BatteryParameter batteryParameter = batteryParameterService.find(id);
        List<String> tempList = new ArrayList<String>();
        if (StringUtils.isNotEmpty(battery.getTemp())) {
            String[] temps = battery.getTemp().split(",");
            for (int i = 0; i < temps.length; i++) {
                tempList.add(temps[i]);
            }
            model.addAttribute("tempList", tempList);
        }
        List<Map> mapList = new ArrayList<Map>();
        if (StringUtils.isNotEmpty(battery.getSingleVoltage())) {
            String[] singleVoltages = battery.getSingleVoltage().split(",");
            List<Integer> voltageList = new ArrayList<Integer>();
            for (int i = 0; i < singleVoltages.length; i++) {
                voltageList.add(Integer.parseInt(singleVoltages[i]));
            }
            int minVoltage = (int) Collections.min(voltageList);
            int maxVoltage = (int) Collections.max(voltageList);
            int averageVoltage = 0;
            int sum = 0;
            for (Integer voltage : voltageList) {
                sum += voltage;
            }
            averageVoltage = sum / voltageList.size();
            int voltageRange = maxVoltage - minVoltage;
            model.addAttribute("minVoltage", minVoltage);
            model.addAttribute("maxVoltage", maxVoltage);
            model.addAttribute("averageVoltage", averageVoltage);
            model.addAttribute("voltageRange", voltageRange);

            int i = 1;
            for (Integer voltage : voltageList) {
                HashMap<String, Integer> map = new HashMap<String, Integer>();
                map.put("no", i);
                map.put("voltage", voltage);
                map.put("maxVoltageRange", maxVoltage - voltage);
                map.put("minVoltageRange", voltage - minVoltage);
                mapList.add(map);
                i++;
            }
            model.addAttribute("mapList", mapList);
        }
        if (batteryParameter != null) {
            //查看电池上线状态
            BatteryInstallRecord batteryInstallRecord = batteryInstallRecordService.findByBatteryType(battery.getType(), battery.getAgentId());
            if (batteryInstallRecord != null && batteryInstallRecord.getStatus() == BatteryInstallRecord.Status.YESONLINE.getValue()) {
                //电池收费方式
                BatteryTypeIncomeRatio batteryTypeIncomeRatio = batteryTypeIncomeRatioService.findByBatteryType(battery.getType(), battery.getAgentId());
                if (batteryTypeIncomeRatio != null) {
                    batteryParameter.setRentPeriodType(batteryTypeIncomeRatio.getRentPeriodType());
                    batteryParameter.setRentPeriodMoney(batteryTypeIncomeRatio.getRentPeriodMoney());
                    batteryParameter.setRentExpireTime(batteryTypeIncomeRatio.getRentExpireTime());
                }
            }
            model.addAttribute("batteryParameter", batteryParameter);
            model.addAttribute("HardOcTrip", BatteryParameter.HardOcTrip.values());
            model.addAttribute("HardOcDelay", BatteryParameter.HardOcDelay.values());
            model.addAttribute("ScTrip", BatteryParameter.SCTrip.values());
            model.addAttribute("ScDelay", BatteryParameter.SCDelay.values());
            model.addAttribute("HardOvDelay", BatteryParameter.HardOvDelay.values());
            model.addAttribute("HardUvDelay", BatteryParameter.HardUvDelay.values());
            model.addAttribute("FunctionList", batteryParameter.getFunctionList());
            model.addAttribute("NtcList", batteryParameter.getNtcList());
            model.addAttribute("OCVTableList", batteryParameter.getOCVTableList());

            //延时处理
            if (batteryParameter.getCellOvDelay() != null) {
                batteryParameter.setCellOvDelay(batteryParameter.getCellOvDelay() / 10);
            }
            if (batteryParameter.getCellUvDelay() != null) {
                batteryParameter.setCellUvDelay(batteryParameter.getCellUvDelay() / 10);
            }
            if (batteryParameter.getPackOvDelay() != null) {
                batteryParameter.setPackOvDelay(batteryParameter.getPackOvDelay() / 10);
            }
            if (batteryParameter.getPackUvDelay() != null) {
                batteryParameter.setPackUvDelay(batteryParameter.getPackUvDelay() / 10);
            }
            if (batteryParameter.getChgOtDelay() != null) {
                batteryParameter.setChgOtDelay(batteryParameter.getChgOtDelay() / 10);
            }
            if (batteryParameter.getChgUtDelay() != null) {
                batteryParameter.setChgUtDelay(batteryParameter.getChgUtDelay() / 10);
            }
            if (batteryParameter.getDsgOtDelay() != null) {
                batteryParameter.setDsgOtDelay(batteryParameter.getDsgOtDelay() / 10);
            }
            if (batteryParameter.getDsgUtDelay() != null) {
                batteryParameter.setDsgUtDelay(batteryParameter.getDsgUtDelay() / 10);
            }
            if (batteryParameter.getChgOcDelay() != null) {
                batteryParameter.setChgOcDelay(batteryParameter.getChgOcDelay() / 10);
            }
            if (batteryParameter.getDsgOcDelay() != null) {
                batteryParameter.setDsgOcDelay(batteryParameter.getDsgOcDelay() / 10);
            }
            //充电过流释放时间
            if (batteryParameter.getChgOcRelease() != null) {
                batteryParameter.setChgOcRelease(batteryParameter.getChgOcRelease() / 10);
            }
            //放电过流释放时间
            if (batteryParameter.getDsgOcRelease() != null) {
                batteryParameter.setDsgOcRelease(batteryParameter.getDsgOcRelease() / 10);
            }

            if (batteryParameter.getMfd() != null) {
                String[] mfdStrs = batteryParameter.getMfd().split("/");
                if (mfdStrs.length == 3) {
                    model.addAttribute("mfd_yy", mfdStrs[0]);
                    model.addAttribute("mfd_mm", mfdStrs[1]);
                    model.addAttribute("mfd_dd", mfdStrs[2]);
                }
            }
        } else {
            model.addAttribute("batteryParameter", new BatteryParameter());
            model.addAttribute("HardOcTrip", BatteryParameter.HardOcTrip.values());
            model.addAttribute("HardOcDelay", BatteryParameter.HardOcDelay.values());
            model.addAttribute("ScTrip", BatteryParameter.SCTrip.values());
            model.addAttribute("ScDelay", BatteryParameter.SCDelay.values());
            model.addAttribute("HardOvDelay", BatteryParameter.HardOvDelay.values());
            model.addAttribute("HardUvDelay", BatteryParameter.HardUvDelay.values());
            model.addAttribute("FunctionList", new ArrayList<Map>());
            model.addAttribute("NtcList", new ArrayList<Map>());
            model.addAttribute("OCVTableList", new ArrayList<Map>());
            model.addAttribute("mfd_yy", 0);
            model.addAttribute("mfd_mm", 0);
            model.addAttribute("mfd_dd", 0);
        }
        if (tempList.size() == 0) {
            if (batteryParameter != null && StringUtils.isNotEmpty(batteryParameter.getTemp())) {
                String[] temps = batteryParameter.getTemp().split(",");
                for (int i = 0; i < temps.length; i++) {
                    tempList.add(temps[i]);
                }
                model.addAttribute("tempList", tempList);
            }
        }
        if (mapList.size() == 0) {
            if (batteryParameter != null && StringUtils.isNotEmpty(batteryParameter.getSingleVoltage())) {
                String[] singleVoltages = batteryParameter.getSingleVoltage().split(",");
                List<Integer> voltageList = new ArrayList<Integer>();
                for (int i = 0; i < singleVoltages.length; i++) {
                    voltageList.add(Integer.parseInt(singleVoltages[i]));
                }
                int minVoltage = (int) Collections.min(voltageList);
                int maxVoltage = (int) Collections.max(voltageList);
                int averageVoltage = 0;
                int sum = 0;
                for (Integer voltage : voltageList) {
                    sum += voltage;
                }
                averageVoltage = sum / voltageList.size();
                int voltageRange = maxVoltage - minVoltage;
                model.addAttribute("minVoltage", minVoltage);
                model.addAttribute("maxVoltage", maxVoltage);
                model.addAttribute("averageVoltage", averageVoltage);
                model.addAttribute("voltageRange", voltageRange);

                int i = 1;
                for (Integer voltage : voltageList) {
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    map.put("no", i);
                    map.put("voltage", voltage);
                    map.put("maxVoltageRange", maxVoltage - voltage);
                    map.put("minVoltageRange", voltage - minVoltage);
                    mapList.add(map);
                    i++;
                }
                model.addAttribute("mapList", mapList);
            }
        }

    }

    @RequestMapping(value = "module.htm")
    public void agent(int moduleId, String url, HttpSession httpSession, HttpServletResponse response) {
        SessionUser sessionUser = getSessionUser(httpSession);
        response.setStatus(302);
        response.setHeader("Location", url);
    }

    @RequestMapping(value = "battery_list.htm")
    public void batteryList(Model model, String subcabinetId) {
        model.addAttribute("subcabinetId", subcabinetId);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, String id) {
        Battery entity = batteryService.find(id);
        Map map = new HashMap();
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("repairStatusList", Battery.RepairStatus.values());
            model.addAttribute("brandList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_BRAND.getValue()));
            model.addAttribute("entity", entity);
            if (entity.getShellCode() != null || entity.getCode() != null) {
                if (entity.getShellCode() != null && entity.getShellCode().length() > 0) {
                    String url = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue());
                    map.put("qrcode", String.format(ConstEnum.Qrcode.QRCODE_BATTERY.getFormat(), url, entity.getShellCode()));
                    model.addAttribute("qrCodeAddress", map.get("qrcode"));
                } else {
                    String url = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue());
                    map.put("qrcode", String.format(ConstEnum.Qrcode.QRCODE_BATTERY.getFormat(), url, entity.getCode()));
                    model.addAttribute("qrCodeAddress", map.get("qrcode"));
                }
            } else {
                model.addAttribute("qrCodeAddress", "");
            }

            //   model.addAttribute("batteryTypeList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
        }
        return "/security/zd/battery/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(Battery entity) {
        return batteryService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(String id) {
        return batteryService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        model.addAttribute("id", id);
        return "/security/zd/battery/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        Battery entity = batteryService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Integer agentId = entity.getAgentId();
            if (agentId != null) {
                entity.setAgentName(agentService.find(agentId).getAgentName());
            }
            model.addAttribute("brandList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_BRAND.getValue()));
            model.addAttribute("entity", entity);
            model.addAttribute("StatusEnum", Battery.Status.values());
        }
        model.addAttribute("id", id);
        return "/security/zd/battery/view_basic";
    }

    @RequestMapping(value = "view_heartbeat.htm")
    public String viewHeartbeat(Model model, String id) {
        Battery entity = batteryService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        model.addAttribute("id", id);
        return "/security/zd/battery/view_heartbeat";
    }

    @RequestMapping(value = "view_status_info.htm")
    public String viewStatusInfo(Model model, String id) {
        Battery entity = batteryService.find(id);
        if(entity == null||entity.getStatus()==Battery.Status.NOT_USE.getValue()) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            String flag;
            if(entity.getStatus()==Battery.Status.CUSTOMER_OUT.getValue()||entity.getStatus()==Battery.Status.IN_BOX_CUSTOMER_USE.getValue()||entity.getStatus()==Battery.Status.IN_BOX_NOT_PAY.getValue()){
                flag="customer";
            }else if(entity.getStatus()==Battery.Status.KEEPER_OUT.getValue()){
                flag="keeper";
            }else{
                flag="box";
            }
            model.addAttribute("entity", entity);
            model.addAttribute("flag", flag);
        }
        model.addAttribute("id", id);
        return "/security/zd/battery/view_status_info";
    }

    @RequestMapping(value = "view_control.htm")
    public String viewControl(Model model, String id) {
        model.addAttribute("id", id);
        return "/security/zd/battery/view_control";
    }

    @RequestMapping(value = "view_control_basic.htm")
    public String viewControlBasic(Model model, String id) {
        Battery entity = batteryService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Integer agentId = entity.getAgentId();
            if (agentId != null) {
                entity.setAgentName(agentService.find(agentId).getAgentName());
            }
            model.addAttribute("entity", entity);
        }
        return "/security/zd/battery/view_control_basic";
    }

    @RequestMapping(value = "view_voltage.htm")
    public String viewVoltage(Model model, String id) {
        String voltage = batteryService.findSingleVoltage(id);
        if (StringUtils.isEmpty(voltage)) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            List<String> voltages = Arrays.asList(voltage.split(","));
            model.addAttribute("voltages", voltages);
        }
        return "/security/zd/battery/view_voltage";
    }

    @RequestMapping(value = "view_control_use.htm")
    public String viewControlUse(Model model, String id) {
        Battery entity = batteryService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Integer agentId = entity.getAgentId();
            if (agentId != null) {
                entity.setAgentName(agentService.find(agentId).getAgentName());
            }
            model.addAttribute("entity", entity);
        }
        return "/security/zd/battery/view_control_use";
    }

    @RequestMapping("upload_file.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void uploadFile(Model model) {
    }

    @RequestMapping(value = "btch_import_battery.htm", method = RequestMethod.POST)
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult btchImportBattery(@RequestParam("file") MultipartFile file, Integer agentId) throws IOException, BiffException {
        String outPath = String.format("%s/%s", getAppConfig().appDir.getPath(), file.getOriginalFilename());
        File outFile = new File(outPath);
        if (!outFile.exists()) {
            outFile.getParentFile().mkdirs();
        }

        FileOutputStream outputStream = new FileOutputStream(outFile);
        int result = IOUtils.copy(file.getInputStream(), outputStream);
        if (result <= 0) {
            return ExtResult.failResult("操作失败！");
        }
        File mFile = new File(outPath);
        return batteryService.btchImportBattery(mFile, agentId);
    }

    @RequestMapping(value = "view_battery_track.htm")
    public String viewBatteryTrack(String id, Model model) {
        //电池明细
        Battery entity = batteryService.find(id);
        model.addAttribute("entity", entity);
        BigDecimal bg = new BigDecimal(Double.valueOf(entity.getTotalDistance())/1000);
        double totalDistance = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        model.addAttribute("totalDistance", totalDistance);
        BatteryOperateLog batteryOperateLog = new BatteryOperateLog();
        batteryOperateLog.setBatteryId(id);
        List<BatteryOperateLog> batteryOperateLogList = batteryOperateLogService.findList(null, id);
        model.addAttribute("batteryOperateLogList", batteryOperateLogList);
        return "/security/zd/battery/view_battery_track";
    }

    @RequestMapping(value = "load_more.htm", method = RequestMethod.POST)
    public void loadMore(Long id, String batteryId, Model model){
        List<BatteryOperateLog> batteryOperateLogList = batteryOperateLogService.findList(id, batteryId);
        model.addAttribute("batteryOperateLogList", batteryOperateLogList);
    }

    @RequestMapping("bound_card.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult boundCard(Battery entity) {
        return batteryService.boundCard(entity);
    }

    @RequestMapping(value = "batch_edit_fullvolume.htm")
    public String batchEditFullvolume() {
        return "/security/zd/battery/batch_edit_fullvolume";
    }

    @RequestMapping("update_fullvolumes.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult updateFullvolumes(String[] batteryIds, Integer chargeCompleteVolume) {
        if (batteryIds.length <= 0) {
            return ExtResult.failResult("请选择终端");
        }

        return batteryService.updateFullVolume(batteryIds, chargeCompleteVolume);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_column.htm")
    public void selectColumn(Model model) {
        model.addAttribute("BatteryColumnEnum", Battery.BatteryColumn.values());
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//到出至excel
    public ExtResult exportExcel(Battery search, Integer[] columns) {
        search.setCategory(Battery.Category.RENT.getValue());
        File file = batteryService.exportExcel(search, columns);
        return DataResult.successResult((Object) ("/static/temp/" + file.getName()));
    }

    @RequestMapping("download.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().appDir, filePath);
        YhdgUtils.makeParentDir(file);
        String fileName = "电池信息明细.xls";
        downloadSupport(file, request, response, fileName);
    }

}
