package cn.com.yusong.yhdg.webserver.web.controller.security.basic;


import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.service.basic.TerminalUpgradePackDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/screen_upgrade_pack_detail")
public class ScreenUpgradePackDetailController extends SecurityController {

    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;

    @RequestMapping(value = "index.htm")
    public void index(Model model) {
       // model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_01_05.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageScreen(TerminalUpgradePackDetail terminalUpgradePackDetail) {
        Page page = terminalUpgradePackDetailService.findPageScreen(terminalUpgradePackDetail);
        return PageResult.successResult(page);
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(@RequestParam int upgradePackId, @RequestParam String[] terminalId) {
        return terminalUpgradePackDetailService.create(upgradePackId, terminalId);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(@RequestParam int upgradePackId,@RequestParam String terminalId) {
        return terminalUpgradePackDetailService.delete(upgradePackId, terminalId);
    }
}
