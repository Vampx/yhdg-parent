package cn.com.yusong.yhdg.webserver.web.controller.security.basic;


import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePackDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.service.basic.ChargerUpgradePackDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/charger_upgrade_pack_detail")
public class ChargerUpgradePackDetailController extends SecurityController {

    @Autowired
    ChargerUpgradePackDetailService chargerUpgradePackDetailService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
       // model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_01_05.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ChargerUpgradePackDetail chargerUpgradePackDetail) {
        Page page = chargerUpgradePackDetailService.findPage(chargerUpgradePackDetail);
        return PageResult.successResult(page);
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(@RequestParam int upgradePackId, @RequestParam String[] chargerId) {
        return chargerUpgradePackDetailService.create(upgradePackId, chargerId);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(@RequestParam int upgradePackId,@RequestParam String terminalId) {
        return chargerUpgradePackDetailService.delete(upgradePackId, terminalId);
    }
}
