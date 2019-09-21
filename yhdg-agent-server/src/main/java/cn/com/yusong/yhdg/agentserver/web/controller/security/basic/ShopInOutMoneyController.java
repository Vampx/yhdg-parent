package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.ShopInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.service.basic.ShopInOutMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/shop_in_out_money")
public class ShopInOutMoneyController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(ShopInOutMoneyController.class);

    @Autowired
    ShopInOutMoneyService shopInOutMoneyService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, String shopId) {
        model.addAttribute("bizTypeEnum", ShopInOutMoney.BizType.values());
        model.addAttribute("typeEnum", ShopInOutMoney.Type.values());
        model.addAttribute("shopId", shopId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ShopInOutMoney search) {
        return PageResult.successResult(shopInOutMoneyService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        ShopInOutMoney shopInOutMoney = shopInOutMoneyService.find(id);
        model.addAttribute("entity", shopInOutMoney);
        return "/security/basic/shop_in_out_money/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "alert_page.htm")
    public void alertPage(Model model, String shopId) {
        model.addAttribute("shopId", shopId);
        model.addAttribute("typeEnum", ShopInOutMoney.Type.values());
    }

}
