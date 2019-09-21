package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.*;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(value = "/security/hdg/cabinet_control")
public class CabinetControlController extends SecurityController {
    @Autowired
    CabinetService cabinetService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_8_2_1)
    @RequestMapping(value = "index.htm")
    public void batteryControl(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_08_02.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult cabinetControlPage(Cabinet search) {
        return PageResult.successResult(cabinetService.findPageControl(search));
    }

    @RequestMapping(value ="view_subcabinet.htm")
    public void viewSubinet(Model model, String cabinetId, int viewFlag) {
        model.addAttribute("cabinetId", cabinetId);
        model.addAttribute("viewFlag", viewFlag);
    }


    @RequestMapping("stats_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult statsPage(CabinetBox cabinetBox) {
        return PageResult.successResult(cabinetService.statsPage(cabinetBox));
    }

    @RequestMapping(value ="view.htm")
    public void viewBox(Model model, String cabinetId, int viewFlag) {
        model.addAttribute("cabinetId", cabinetId);
        model.addAttribute("viewFlag", viewFlag);
    }

    //@SecurityControl(limits = OperCodeConst.CODE_2_5_3_1)
    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "control_pic.htm")
    public void batteryControlPic(Model model, Cabinet cabinet) {
        model.addAttribute("cabinetCount", cabinetService.findCabinetCount());
        model.addAttribute("page", cabinetService.findPage(cabinet));
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_05_03.getValue());
    }

    @ResponseBody
    @RequestMapping("find_battery_list.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findBatteryList(String [] cabinetId) {
        if (cabinetId != null && cabinetId.length > 0) {
            Map<String, List<CabinetState>> list = cabinetService.findBatteryStateList(cabinetId);
            return DataResult.successResult(list);
        } else {
            return DataResult.successResult("");
        }
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("index_cabinet.htm")
    public String indexInside(Model model, Cabinet cabinet){
        model.addAttribute("page", cabinetService.findPage(cabinet));
        return "/security/hdg/cabinet_control/index_cabinet";
    }

}
