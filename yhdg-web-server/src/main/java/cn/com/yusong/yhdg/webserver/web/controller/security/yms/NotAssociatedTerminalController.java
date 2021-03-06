package cn.com.yusong.yhdg.webserver.web.controller.security.yms;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 未关联终端
 */
@Controller
@RequestMapping(value = "/security/yms/not_associated_terminal")
public class NotAssociatedTerminalController extends SecurityController {
    @Autowired
    TerminalService terminalService;

    @SecurityControl(limits = OperCodeConst.CODE_6_2_2_1)
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_06_02_02.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Terminal terminal){
        return PageResult.successResult(terminalService.findNotAssociatedPage(terminal));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_not_associated_terminal.htm")
    public void selectActivityPrice(Model model) {
    }

}
