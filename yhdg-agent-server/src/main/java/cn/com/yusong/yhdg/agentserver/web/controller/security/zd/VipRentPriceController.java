package cn.com.yusong.yhdg.agentserver.web.controller.security.zd;


import cn.com.yusong.yhdg.agentserver.service.zd.VipRentBatteryForegiftService;
import cn.com.yusong.yhdg.agentserver.service.zd.VipRentPriceService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zd.VipRentPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zd/vip_rent_price")
public class VipRentPriceController extends SecurityController {

    @Autowired
    VipRentPriceService vipRentPriceService;
    @Autowired
    VipRentBatteryForegiftService vipRentBatteryForegiftService;

    @SecurityControl(limits = "zd.VipRentPrice:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zd.VipRentPrice:list");
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VipRentPrice search) {
        return PageResult.successResult(vipRentPriceService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        VipRentPrice entity = vipRentPriceService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/vip_rent_price/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(VipRentPrice activity) {
        return vipRentPriceService.create(activity);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(VipRentPrice activity) {
        return vipRentPriceService.update(activity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return vipRentPriceService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        VipRentPrice entity = vipRentPriceService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zd/vip_rent_price/view";
    }

}
