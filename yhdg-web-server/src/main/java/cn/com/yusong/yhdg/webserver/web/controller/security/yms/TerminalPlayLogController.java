package cn.com.yusong.yhdg.webserver.web.controller.security.yms;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.yms.TerminalPlayLog;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.yms.TerminalPlayLogService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/security/yms/terminal_play_log")
public class TerminalPlayLogController extends SecurityController {
    @Autowired
    TerminalPlayLogService terminalPlayLogService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        terminalPlayLogService.tree(dummy, response.getOutputStream());
    }


    //@SecurityControl(limits = OperCodeConst.CODE_3_2_6_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) throws Exception {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_03_02_06.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(TerminalPlayLog search) {
        if (search.getSuffix() == null) {
            return PageResult.failResult("请重新加载");
        } else {
            return PageResult.successResult(terminalPlayLogService.findPage(search));
        }

    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(int id, String suffix, Model model) {
        TerminalPlayLog entity = terminalPlayLogService.find(id, suffix);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/yms/terminal_play_log/view";
    }


}
