package cn.com.yusong.yhdg.agentserver.web.controller.security.yms;

import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
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

    @SecurityControl(limits = "yms.NotAssociatedTermial:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "yms.NotAssociatedTermial:list");
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
