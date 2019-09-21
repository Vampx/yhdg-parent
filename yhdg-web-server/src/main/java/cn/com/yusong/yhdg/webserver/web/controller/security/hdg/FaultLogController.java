package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.FaultLog;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.webserver.service.hdg.FaultLogService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 故障日志
 */
@Controller
@RequestMapping(value = "/security/hdg/fault_log")
public class FaultLogController extends SecurityController {

    @Autowired
    FaultLogService faultLogService;
    @Autowired
    DictItemService dictItemService;

    @SecurityControl(limits = "hdg.FaultLog:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer status, Integer grade) {
        model.addAttribute("FaultTypeEnum", FaultLog.FaultType.values());
        model.addAttribute("FaultLevelEnum", FaultLog.FaultLevel.values());
        model.addAttribute("StatusEnum", FaultLog.Status.values());
        model.addAttribute("brandList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_BRAND.getValue()));
        if (grade == null) {
            model.addAttribute("grade", -1);
        }else {
            model.addAttribute("grade", grade);
        }
        model.addAttribute(MENU_CODE_NAME, "hdg.FaultLog:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(FaultLog search, Integer grade) {
        if (grade != null && grade == ConstEnum.Flag.FALSE.getValue()) {
            search.setFaultLevel(ConstEnum.Flag.FALSE.getValue());
        }
        if (search.getBatteryDetailFlag() != null && search.getBatteryDetailFlag() == 1) {
            List<Integer> batteryFaultList = Arrays.asList(FaultLog.FaultType.CODE_1.getValue(),
                    FaultLog.FaultType.CODE_2.getValue(),
                    FaultLog.FaultType.CODE_3.getValue(),
                    FaultLog.FaultType.CODE_4.getValue(),
                    FaultLog.FaultType.CODE_5.getValue(),
                    FaultLog.FaultType.CODE_6.getValue(),
                    FaultLog.FaultType.CODE_7.getValue(),
                    FaultLog.FaultType.CODE_8.getValue(),
                    FaultLog.FaultType.CODE_9.getValue(),
                    FaultLog.FaultType.CODE_10.getValue(),
                    FaultLog.FaultType.CODE_11.getValue(),
                    FaultLog.FaultType.CODE_12.getValue(),
                    FaultLog.FaultType.CODE_13.getValue(),
                    FaultLog.FaultType.CODE_14.getValue(),
                    FaultLog.FaultType.CODE_15.getValue(),
                    FaultLog.FaultType.CODE_20.getValue());
            search.setBatteryFaultList(batteryFaultList);
        }
        return PageResult.successResult(faultLogService.findPage(search));
    }

    @RequestMapping("find.htm")
    public void find(Long id, Model model) {
        model.addAttribute("entity", faultLogService.find(id));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        FaultLog entity = faultLogService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("id", id);
            model.addAttribute("entity", entity);
            model.addAttribute("FaultTypeEnum", FaultLog.FaultType.values());
            model.addAttribute("StatusEnum", FaultLog.Status.values());
            model.addAttribute("FaultLevelEnum", FaultLog.FaultLevel.values());
        }
        return "/security/hdg/fault_log/view";
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Long id) {
        FaultLog entity = faultLogService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/fault_log/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(FaultLog entity) {
        return faultLogService.update(entity);
    }

    @RequestMapping("findCount.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public DataResult findCount() {
        return DataResult.successResult(faultLogService.findCount(FaultLog.Status.WAIT_PROCESS.getValue()));
    }

    @RequestMapping("batch_edit_status.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult batchEditStatus(Long[] faultLogIds, HttpSession httpSession) {
        String username = getSessionUser(httpSession).getUsername();
        return faultLogService.updateStatus(faultLogIds, username);
    }

}
