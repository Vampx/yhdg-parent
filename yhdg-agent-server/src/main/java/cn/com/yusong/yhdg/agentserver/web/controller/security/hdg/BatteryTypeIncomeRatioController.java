package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryTypeIncomeRatio;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.DictItemService;
import cn.com.yusong.yhdg.agentserver.service.hdg.BatteryTypeIncomeRatioService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/battery_type_income_ratio")
public class BatteryTypeIncomeRatioController extends SecurityController {

    @Autowired
    BatteryTypeIncomeRatioService batteryTypeIncomeRatioService;
    @Autowired
    DictItemService dictItemService;

//    @SecurityControl(limits = OperCodeConst.CODE_1_6_2_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//       model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_06_02.getValue());
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryTypeIncomeRatio search) {
        return PageResult.successResult(batteryTypeIncomeRatioService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        BatteryTypeIncomeRatio entity = batteryTypeIncomeRatioService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_type_income_ratio/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(BatteryTypeIncomeRatio batteryTypeIncomeRatio) {
        return batteryTypeIncomeRatioService.create(batteryTypeIncomeRatio);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(BatteryTypeIncomeRatio batteryTypeIncomeRatio) {
        return batteryTypeIncomeRatioService.update(batteryTypeIncomeRatio);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return batteryTypeIncomeRatioService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        BatteryTypeIncomeRatio entity = batteryTypeIncomeRatioService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/battery_type_income_ratio/view";
    }

}
