package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryParameter;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryParameterService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;

/**
 * 电池参数
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_parameter")
public class BatteryParameterController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(BatteryParameterController.class);

    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryParameterService batteryParameterService;



    @RequestMapping(value = "index.htm")
    public String parameter(Model model, String id) {
        model.addAttribute("id", id);
        return "/security/hdg/battery_parameter/index";
    }

    @RequestMapping(value = "param_batch_edit.htm")
    public String paramBatchEdit(Model model, String[] ids) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < ids.length; i++) {
            if (i != ids.length - 1) {
                builder.append(ids[i] + ",");
            } else {
                builder.append(ids[i]);
            }
        }
        String idsData = builder.toString();
        model.addAttribute("idsData", idsData);
        model.addAttribute("HardOcTrip", BatteryParameter.HardOcTrip.values());
        model.addAttribute("HardOcDelay", BatteryParameter.HardOcDelay.values());
        model.addAttribute("ScTrip", BatteryParameter.SCTrip.values());
        model.addAttribute("ScDelay", BatteryParameter.SCDelay.values());
        model.addAttribute("HardOvDelay", BatteryParameter.HardOvDelay.values());
        model.addAttribute("HardUvDelay", BatteryParameter.HardUvDelay.values());
        return "/security/hdg/battery_parameter/param_batch_edit";
    }

    @RequestMapping(value = "parameter_basic.htm")
    public String parameterBasic(Model model, String id) {
        BatteryParameter entity = batteryParameterService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("HardOcTrip", BatteryParameter.HardOcTrip.values());
            model.addAttribute("HardOcDelay", BatteryParameter.HardOcDelay.values());
            model.addAttribute("ScTrip", BatteryParameter.SCTrip.values());
            model.addAttribute("ScDelay", BatteryParameter.SCDelay.values());
            model.addAttribute("HardOvDelay", BatteryParameter.HardOvDelay.values());
            model.addAttribute("HardUvDelay", BatteryParameter.HardUvDelay.values());
            model.addAttribute("FunctionList", entity.getFunctionList());
        }
        model.addAttribute("id", id);
        return "/security/hdg/battery_parameter/parameter_basic";
    }

    @RequestMapping(value = "parameter_edit.htm")
    public String parameterEdit(Model model, String id) {
        BatteryParameter entity = batteryParameterService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("HardOcTrip", BatteryParameter.HardOcTrip.values());
            model.addAttribute("HardOcDelay", BatteryParameter.HardOcDelay.values());
            model.addAttribute("ScTrip", BatteryParameter.SCTrip.values());
            model.addAttribute("ScDelay", BatteryParameter.SCDelay.values());
            model.addAttribute("HardOvDelay", BatteryParameter.HardOvDelay.values());
            model.addAttribute("HardUvDelay", BatteryParameter.HardUvDelay.values());
            model.addAttribute("FunctionList", entity.getFunctionList());
            model.addAttribute("NtcList", entity.getNtcList());
            model.addAttribute("OCVTableList", entity.getOCVTableList());

            //延时处理
            if(entity.getCellOvDelay() != null){
                entity.setCellOvDelay(entity.getCellOvDelay() / 10);
            }
            if(entity.getCellUvDelay() != null){
                entity.setCellUvDelay(entity.getCellUvDelay() / 10);
            }
            if(entity.getPackOvDelay() != null){
                entity.setPackOvDelay(entity.getPackOvDelay() / 10);
            }
            if(entity.getPackUvDelay() != null){
                entity.setPackUvDelay(entity.getPackUvDelay() / 10);
            }
            if(entity.getChgOtDelay() != null){
                entity.setChgOtDelay(entity.getChgOtDelay() / 10);
            }
            if(entity.getChgUtDelay() != null){
                entity.setChgUtDelay(entity.getChgUtDelay() / 10);
            }
            if(entity.getDsgOtDelay() != null){
                entity.setDsgOtDelay(entity.getDsgOtDelay() / 10);
            }
            if(entity.getDsgUtDelay() != null){
                entity.setDsgUtDelay(entity.getDsgUtDelay() / 10);
            }
            if(entity.getChgOcDelay() != null){
                entity.setChgOcDelay(entity.getChgOcDelay() / 10);
            }
            if(entity.getDsgOcDelay() != null){
                entity.setDsgOcDelay(entity.getDsgOcDelay() / 10);
            }
            //充电过流释放时间
            if (entity.getChgOcRelease() != null) {
                entity.setChgOcRelease(entity.getChgOcRelease() / 10);
            }
            //放电过流释放时间
            if (entity.getDsgOcRelease() != null) {
                entity.setDsgOcRelease(entity.getDsgOcRelease() / 10);
            }

            if(entity.getMfd() != null){
                String[] mfdStrs = entity.getMfd().split("/");
                if(mfdStrs.length == 3){
                    model.addAttribute("mfd_yy", mfdStrs[0]);
                    model.addAttribute("mfd_mm", mfdStrs[1]);
                    model.addAttribute("mfd_dd", mfdStrs[2]);
                }
            }
        }
        model.addAttribute("id", id);
        return "/security/hdg/battery_parameter/parameter_edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(BatteryParameter entity, HttpSession httpSession) {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        ExtResult result = batteryParameterService.update(entity,username);
        return result;
    }

    @RequestMapping("batch_update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult batchUpdate(BatteryParameter entity, HttpSession httpSession) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        SessionUser sessionUser = getSessionUser(httpSession);
        String username = sessionUser.getUsername();
        ExtResult result = batteryParameterService.batchUpdate(entity,username);
        return result;
    }
}
