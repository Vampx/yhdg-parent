package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Resignation;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.ResignationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/security/basic/resignation")
public class ResignationController extends SecurityController {
    @Autowired
    ResignationService resignationService;

//    @SecurityControl(limits = OperCodeConst.CODE_1_1_7_1)
    @RequestMapping("index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_01_07.getValue());
        model.addAttribute("AUDIT", Resignation.State.AUDIT.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Resignation search) {
        return PageResult.successResult(resignationService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(long id, Model model) {
        Resignation resignation = resignationService.find(id);
        if (resignation == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", resignation);
        return "/security/basic/resignation/view";
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        Resignation entity = resignationService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        model.addAttribute("ADOPT", Resignation.State.ADOPT.getValue());
        model.addAttribute("REFUSE", Resignation.State.REFUSE.getValue());

        return "/security/basic/resignation/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(HttpSession httpSession, Resignation search) {
        SessionUser sessionUser = getSessionUser(httpSession);
        search.setOperator(sessionUser.getUsername());
        return resignationService.update(search);
    }

}
