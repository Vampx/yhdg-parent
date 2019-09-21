package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.service.hdg.CabinetAddressCorrectionService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetAddressCorrection;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设备位置纠错
 */
@Controller
@RequestMapping(value = "/security/hdg/cabinet_address_correction")
public class CabinetAddressCorrectionController extends SecurityController {

    @Autowired
    CabinetAddressCorrectionService cabinetAddressCorrectionService;

    @SecurityControl(limits = "hdg.CabinetAddressCorrection:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.CabinetAddressCorrection:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CabinetAddressCorrection search) {
        return PageResult.successResult(cabinetAddressCorrectionService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        CabinetAddressCorrection entity = cabinetAddressCorrectionService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("status", CabinetAddressCorrection.Status.getName(entity.getStatus()));
        }
        return "/security/hdg/cabinet_address_correction/view";
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Long id){
        CabinetAddressCorrection entity = cabinetAddressCorrectionService.find(id);
        if (entity == null){
            return SEGMENT_RECORD_NOT_FOUND;
        }else{
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet_address_correction/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateStatus(Long id, int status) {
        CabinetAddressCorrection entity = cabinetAddressCorrectionService.find(id);
        entity.setStatus(status);
        return cabinetAddressCorrectionService.updateStatus(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return cabinetAddressCorrectionService.delete(id);
    }

}
