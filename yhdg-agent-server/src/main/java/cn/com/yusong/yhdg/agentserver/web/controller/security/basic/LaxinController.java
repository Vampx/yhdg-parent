package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.LaxinService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Laxin;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/laxin")
public class LaxinController extends SecurityController {

    @Autowired
    LaxinService laxinService;

    @SecurityControl(limits = "basic.Laxin:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.Laxin:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Laxin search) {
        return PageResult.successResult(laxinService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add.htm")
    public void add(Model model) {
    }

    @ResponseBody
    @RequestMapping(value = "create.htm")
    public ExtResult create(Laxin laxin) {
        return laxinService.create(laxin);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int id, Model model) {
        Laxin laxin = laxinService.find(id);
        if(laxin == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", laxin);
        return "/security/basic/laxin/edit";
    }

    @ResponseBody
    @RequestMapping(value = "update.htm")
    public ExtResult update(Laxin laxin) {
        return laxinService.update(laxin);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(long id, Model model) {
        Laxin laxin = laxinService.find(id);
        if (laxin == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", laxin);
        return "/security/basic/laxin/view";
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return laxinService.delete(id);
    }
}
