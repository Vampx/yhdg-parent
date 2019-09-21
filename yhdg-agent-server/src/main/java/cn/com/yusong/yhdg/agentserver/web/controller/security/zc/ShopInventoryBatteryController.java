package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;

import cn.com.yusong.yhdg.agentserver.service.zc.ShopInventoryBatteryService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/security/zc/shop_inventory_battery")
@Controller
public class ShopInventoryBatteryController extends SecurityController {

    @Autowired
    ShopInventoryBatteryService shopInventoryBatteryService;

    @SecurityControl(limits = "zc.ShopInventoryBattery:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.ShopInventoryBattery:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ShopStoreBattery search) {
        return PageResult.successResult(shopInventoryBatteryService.findPage(search));
    }

    @RequestMapping("unbind_battery.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult UnbindBattery(Long[] idList) {
        int count = 0;
        for (Long e : idList) {
            count += shopInventoryBatteryService.UnbindBattery(e);
        }
        return ExtResult.successResult(String.format("成功解绑%d个电池", count));
    }
}
