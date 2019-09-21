package cn.com.yusong.yhdg.webserver.web.controller.security.zc;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.AgentDayStats;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.Vehicle;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_12;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_22;
import cn.com.yusong.yhdg.common.protocol.msg_tbit.Msg_WEB_32;
import cn.com.yusong.yhdg.common.utils.IdUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.webserver.constant.RespCode;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.entity.result.RestResult;
import cn.com.yusong.yhdg.webserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.webserver.service.zc.VehicleService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/zc/vehicle")
public class VehicleController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(VehicleController.class);

    @Autowired
    VehicleService vehicleService;
    @Autowired
    SystemConfigService systemConfigService;

    @SecurityControl(limits = "zc.Vehicle:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.Vehicle:list");
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Vehicle search) {
        return PageResult.successResult(vehicleService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "select_vehicle.htm")
    public void selectVehicle(Model model, Integer agentId, Integer modelId) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("modelId", modelId);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Integer id) {
        Vehicle entity = vehicleService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/vehicle/edit";
    }

    @RequestMapping(value = "batch_edit_up_line_status.htm")
    public String batchEditModel() {
        return "/security/zc/vehicle/batch_edit_up_line_status";
    }

    @RequestMapping("batch_update_up_line_status.htm")
    @ResponseBody
    @ViewModel(ViewModel.JSON)
    public ExtResult batchUpdateUpLineStatus(Integer[] vehicleIds, Integer agentId, Integer modelId) {
        if (vehicleIds.length <= 0) {
            return ExtResult.failResult("请选择车辆");
        }
        if (agentId == null) {
            return ExtResult.failResult("请选择运营商");
        }
        if (modelId == null) {
            return ExtResult.failResult("请选择车型");
        }
        return vehicleService.batchUpdateUpLineStatus(vehicleIds, modelId, agentId, Vehicle.UpLineStatus.ONLINE.getValue(), new Date());
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Vehicle activity) {
        return vehicleService.create(activity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(Vehicle activity) {
        return vehicleService.update(activity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return vehicleService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Integer id) {
        Vehicle entity = vehicleService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Map map = new HashMap();
            model.addAttribute("entity", entity);
            String url = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue());
            map.put("qrcode", String.format(ConstEnum.Qrcode.QRCODE_VEHICLE.getFormat(), url, entity.getVinNo()));
            model.addAttribute("qrCodeAddress", map.get("qrcode"));
        }
        return "/security/zc/vehicle/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, Integer id) {
        Vehicle entity = vehicleService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Map map = new HashMap();
            model.addAttribute("entity", entity);
            String url = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue());
            map.put("qrcode", String.format(ConstEnum.Qrcode.QRCODE_VEHICLE.getFormat(), url, entity.getVinNo()));
            model.addAttribute("qrCodeAddress", map.get("qrcode"));
        }
        return "/security/zc/vehicle/view_basic";
    }

    @RequestMapping("lock.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public RestResult lock(int id, int lockSwitch, HttpServletRequest request) throws InterruptedException {
        Vehicle entity = vehicleService.find(id);
        if (entity == null) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "车辆", null);
        }

        ClientBizUtils.SerialResult result = ClientBizUtils.lockVehicle(appConfig, entity.getVinNo(), lockSwitch);
        boolean ok = result.getCode() == RespCode.CODE_0.getValue();HttpSession httpSession = request.getSession();
        SessionUser sessionUser = null;
        if (httpSession != null) {
            sessionUser = (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
        }

        if (StringUtils.isNotEmpty(entity.getVinNo())) {
//            Cabinet cabinet = cabinetService.find(subcabinetBox.getCabinetId());
//            CabinetOperateLog operateLog = new CabinetOperateLog();
//            operateLog.setAgentId(cabinet.getAgentId());
//            operateLog.setCabinetId(cabinet.getId());
//            operateLog.setCabinetName(cabinet.getCabinetName());
//            operateLog.setBoxNum(boxNum);
//            operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
//            operateLog.setOperatorType(CabinetOperateLog.OperatorType.PLATFORM.getValue());
//            operateLog.setOperator(sessionUser.getUsername());


            if (ok) {
                Msg_WEB_12 resp = (Msg_WEB_12) result.getData();
//                operateLog.setContent("远程开箱成功, 箱门状态: " + (resp.boxStatus == 0 ? "开门" : "关门"));
//                operateLog.setCreateTime(new Date());
//                cabinetOperateLogService.insert(operateLog);

                entity.setLockSwitch(lockSwitch);
                if(lockSwitch == Vehicle.LockSwitch.NO.getValue()
                        || lockSwitch == Vehicle.LockSwitch.DISCHG_OPEN.getValue() ){
                    entity.setLockStatus(ConstEnum.Flag.FALSE.getValue());
                }else{
                    entity.setLockStatus(ConstEnum.Flag.TRUE.getValue());
                }
                vehicleService.updateLockStatus(entity);

            } else {
                if (result.getSerial() != -1) {
//                    operateLog.setContent("远程开箱失败, " + result.getMessage());
//                    operateLog.setCreateTime(new Date());
//                    cabinetOperateLogService.insert(operateLog);
                }
                return result;
            }
        }

        if (ok) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "操作成功", result.getData());
        } else {
            return result;
        }

    }

    @RequestMapping(value = "parameter.htm")
    public String parameter(Model model, Integer id) {
        Vehicle entity = vehicleService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("TBIT_Parameter", Vehicle.TBIT_Parameter.values());
        }
        return "/security/zc/vehicle/parameter";
    }

    @RequestMapping("query_parameter.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public RestResult queryParameter(int id, String parameter, HttpServletRequest request) throws InterruptedException {
        Vehicle entity = vehicleService.find(id);
        if (entity == null) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "车辆", null);
        }

        ClientBizUtils.SerialResult result = ClientBizUtils.quryParameter(appConfig, entity.getVinNo(), parameter);
        boolean ok = result.getCode() == RespCode.CODE_0.getValue();HttpSession httpSession = request.getSession();
        SessionUser sessionUser = null;
        if (httpSession != null) {
            sessionUser = (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
        }

        if (ok) {
            Msg_WEB_22 resp = (Msg_WEB_22) result.getData();
            return RestResult.dataResult(RespCode.CODE_0.getValue(), "操作成功", resp.returnValue);
        } else {
            return result;
        }
    }

    @RequestMapping("set_parameter.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public RestResult setParameter(int id, String parameter, String writeValue, HttpServletRequest request) throws InterruptedException {
        Vehicle entity = vehicleService.find(id);
        if (entity == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "车辆不存在！");
        }

        Integer enableWrite = Vehicle.TBIT_Parameter.getWrite(parameter);
        if(enableWrite == null || enableWrite == 0){
            return RestResult.result(RespCode.CODE_2.getValue(), "非可写参数！");
        }
        if(StringUtils.isEmpty(writeValue)){
            return RestResult.result(RespCode.CODE_2.getValue(), "写入值不能为空！");
        }
        String value = String.format("%s=%s",Vehicle.TBIT_Parameter.getName(parameter) ,writeValue);
        ClientBizUtils.SerialResult result = ClientBizUtils.setParameter(appConfig, entity.getVinNo(), value);
        boolean ok = result.getCode() == RespCode.CODE_0.getValue();HttpSession httpSession = request.getSession();
        SessionUser sessionUser = null;
        if (httpSession != null) {
            sessionUser = (SessionUser) httpSession.getAttribute(Constant.SESSION_KEY_USER);
        }

        if (ok) {
            return RestResult.result(RespCode.CODE_0.getValue(), "操作成功");
        } else {
            return result;
        }
    }

    @RequestMapping("export_excel.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody//导出模板至excel
    public ExtResult exportExcel(AgentDayStats search, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().tempDir, IdUtils.uuid());
        YhdgUtils.makeParentDir(file);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            WritableWorkbook wwb;
            wwb = Workbook.createWorkbook(outputStream);
            WritableSheet sheet = wwb.createSheet("车辆模板", 0);
            sheet.getSettings().setDefaultColumnWidth(20);
            sheet.setRowView(0, 400, false);
            /**
             * 设置题头
             */
            sheet.addCell(new Label(0, 0,"车辆模板", getTitleStyle()));
            int column = 0;
            sheet.addCell(new Label(column++, 1,"车架号", getHeadStyle()));
            sheet.mergeCells(0, 0, --column, 0);

            // 写入数据
            wwb.write();
            // 关闭文件
            wwb.close();
        } catch (Exception e) {
            log.error("upload_excel error", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
        return DataResult.successResult((Object) ("/static/temp/" + file.getName()));
    }

    @RequestMapping("download.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public void download(String filePath, HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = new File(getAppConfig().appDir, filePath);
        YhdgUtils.makeParentDir(file);
        String fileName = "车辆模板.xls";
        downloadSupport(file, request, response, fileName);
    }

    @RequestMapping("upload_file.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void uploadFile(Model model) {
    }

    @RequestMapping(value = "btch_import_store_vehicle.htm", method = RequestMethod.POST)
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult btchImportBattery(@RequestParam("file") MultipartFile file, Vehicle vehicle) throws IOException, BiffException {
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
        return vehicleService.btchImportBattery(mFile, vehicle);
    }
}
