package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetOperateLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetOperateLogService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/cabinet_operate_log")
public class CabinetOperateLogController extends SecurityController {

    @Autowired
    CabinetOperateLogService cabinetOperateLogService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_3_5_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("operateTypeEnum", CabinetOperateLog.OperateType.values());
        model.addAttribute("operatorTypeEnum", CabinetOperateLog.OperatorType.values());
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_03_05.getValue());
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetOperateLog search) {
        return PageResult.successResult(cabinetOperateLogService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        CabinetOperateLog entity = cabinetOperateLogService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("operatorTypeEnum", CabinetOperateLog.OperatorType.values());
            model.addAttribute("operateTypeName", CabinetOperateLog.OperateType.getName(entity.getOperateType()));
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet_operate_log/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "/select_cabinet_operate_log.htm")
    public void selectCabinetOperateLog(Model model) {
        model.addAttribute("operateTypeEnum", CabinetOperateLog.OperateType.values());
        model.addAttribute("operatorTypeEnum", CabinetOperateLog.OperatorType.values());
    }
}
