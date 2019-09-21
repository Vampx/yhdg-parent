package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.zc.ShopStoreVehicleBattery;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopStoreBatteryService;
import cn.com.yusong.yhdg.webserver.service.zc.ShopStoreVehicleBatteryService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/security/hdg/shop_store_battery")
@Controller
public class ShopStoreBatteryController extends SecurityController {

    @Autowired
    ShopStoreBatteryService shopStoreBatteryService;
    @Autowired
    ShopStoreVehicleBatteryService shopStoreVehicleBatteryService;

    @SecurityControl(limits = "hdg.ShopStoreBattery:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.ShopStoreBattery:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ShopStoreBattery search) {
        return PageResult.successResult(shopStoreBatteryService.findPage(search));
    }

    @RequestMapping("unbind_battery.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult UnbindBattery(Long[] idList) {
        int count = 0;
        for (Long e : idList) {
            ShopStoreBattery shopStoreBattery = shopStoreBatteryService.find(e);
            ShopStoreVehicleBattery shopStoreVehicleBattery = shopStoreVehicleBatteryService.findByBatteryId(shopStoreBattery.getBatteryId());
            if (shopStoreVehicleBattery != null) {
                return ExtResult.failResult("电池已绑定车辆不可解绑");
            }
            count += shopStoreBatteryService.UnbindBattery(e);
        }
        return ExtResult.successResult(String.format("成功解绑%d个电池", count));
    }
}
