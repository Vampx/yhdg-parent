package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.DictCategory;
import cn.com.yusong.yhdg.common.domain.hdg.UnregisterBattery;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.webserver.service.hdg.UnregisterBatteryService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/unregister_battery")
public class UnregisterBatteryController extends SecurityController {
    @Autowired
    UnregisterBatteryService unregisterBatteryService;
    @Autowired
    DictItemService dictItemService;

//    @SecurityControl(limits = OperCodeConst.CODE_2_6_5_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_06_05.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(UnregisterBattery search) {
        return PageResult.successResult(unregisterBatteryService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        UnregisterBattery entity = unregisterBatteryService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/unregister_battery/view";
    }

    @RequestMapping(value = "bound_card.htm")
    public String boundCard(Model model, String id) {
        UnregisterBattery entity = unregisterBatteryService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
      //  model.addAttribute("batteryTypeList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue()));
        model.addAttribute("brandList", dictItemService.findByCategory(DictCategory.CategoryType.BATTERY_BRAND.getValue()));
        return "/security/hdg/unregister_battery/bound_card";
    }
}
