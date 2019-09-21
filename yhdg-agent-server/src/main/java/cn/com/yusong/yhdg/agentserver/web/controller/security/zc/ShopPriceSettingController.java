package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;

import cn.com.yusong.yhdg.agentserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.agentserver.service.zc.ShopPriceSettingService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.zc.ShopPriceSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/security/zc/shop_price_setting")
@Controller
public class ShopPriceSettingController extends SecurityController {

    @Autowired
    ShopPriceSettingService shopPriceSettingService;
    @Autowired
    ShopService shopService;

    @SecurityControl(limits = "zc.ShopPriceSetting:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.ShopPriceSetting:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ShopPriceSetting search) {
        return PageResult.successResult(shopPriceSettingService.findPage(search));
    }

    @RequestMapping("find_by_shop_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult findByShopPage(ShopPriceSetting search) {
        return PageResult.successResult(shopPriceSettingService.findByShopPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }
    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(ShopPriceSetting shopPriceSetting) {
        return shopPriceSettingService.insert(shopPriceSetting);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Integer id) {
        shopPriceSettingService.delete(id);
        return ExtResult.successResult();
    }

    @RequestMapping(value = "select_shop_price_setting.htm")
    public void selectShopPriceSetting(Model model,String shopId) {
        model.addAttribute("shopId", shopId);
    }
}
