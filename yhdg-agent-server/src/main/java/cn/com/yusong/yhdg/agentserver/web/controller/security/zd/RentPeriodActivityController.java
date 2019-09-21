package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;

import cn.com.yusong.yhdg.agentserver.service.zd.RentPeriodActivityService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodActivity;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zd/rent_period_activity")
public class RentPeriodActivityController extends SecurityController {
    @Autowired
    private RentPeriodActivityService rentPeriodActivityService;

    @SecurityControl(limits = "zd.RentPeriodActivity:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zd.RentPeriodActivity:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(RentPeriodActivity search) {
        return PageResult.successResult(rentPeriodActivityService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id, Integer editFlag) {
        model.addAttribute("editFlag", editFlag);
        RentPeriodActivity entity = rentPeriodActivityService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_period_activity/edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, long id) {
        RentPeriodActivity entity = rentPeriodActivityService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_period_activity/edit_basic";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(RentPeriodActivity activity) {
        return rentPeriodActivityService.create(activity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(RentPeriodActivity activity) {
        return rentPeriodActivityService.update(activity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return rentPeriodActivityService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        RentPeriodActivity entity = rentPeriodActivityService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_period_activity/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, long id) {
        RentPeriodActivity entity = rentPeriodActivityService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/rent_period_activity/view_basic";
    }
}
