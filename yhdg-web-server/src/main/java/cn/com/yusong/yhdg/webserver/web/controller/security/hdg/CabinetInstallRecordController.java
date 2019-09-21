package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetInstallRecord;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetInstallRecordService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/security/hdg/cabinet_install_record")
public class CabinetInstallRecordController extends SecurityController{

    @Autowired
    CabinetInstallRecordService cabinetInstallRecordService;

    @SecurityControl(limits = "hdg.CabinetInstallRecord:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute("statusEnum", CabinetInstallRecord.Status.values());
       model.addAttribute(MENU_CODE_NAME, "hdg.CabinetInstallRecord:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetInstallRecord search) {
        return PageResult.successResult(cabinetInstallRecordService.findPage(search));
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        CabinetInstallRecord entity = cabinetInstallRecordService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("statusEnum", CabinetInstallRecord.Status.values());
        return "/security/hdg/cabinet_install_record/edit";
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        CabinetInstallRecord entity = cabinetInstallRecordService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("statusEnum", CabinetInstallRecord.Status.values());
        return "/security/hdg/cabinet_install_record/view";
    }

    @RequestMapping("update_up_line.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateUpLine(CabinetInstallRecord entity) {
        return cabinetInstallRecordService.updateUpLine(entity);
    }
}