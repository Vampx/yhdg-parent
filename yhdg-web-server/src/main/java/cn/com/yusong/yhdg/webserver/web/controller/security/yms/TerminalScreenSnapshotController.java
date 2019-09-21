package cn.com.yusong.yhdg.webserver.web.controller.security.yms;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.yms.TerminalScreenSnapshot;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.yms.TerminalScreenSnapshotService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/yms/terminal_screen_snapshot")
public class TerminalScreenSnapshotController extends SecurityController {

    @Autowired
    TerminalScreenSnapshotService terminalScreenSnapshotService;

//    @SecurityControl(limits = OperCodeConst.CODE_6_2_6_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model){
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_06_02_06.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(TerminalScreenSnapshot search){
        return PageResult.successResult(terminalScreenSnapshotService.findPage(search));
    }
}
