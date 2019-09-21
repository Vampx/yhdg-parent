package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.agentserver.service.basic.LaxinSettingService;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.LaxinSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/laxin_setting")
public class LaxinSettingController extends SecurityController {

    @Autowired
    LaxinSettingService laxinService;

    @SecurityControl(limits = "basic.LaxinSetting:list")
    @RequestMapping("index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.LaxinSetting:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(LaxinSetting search) {
        return PageResult.successResult(laxinService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add.htm")
    public void add(Model model) {
    }

    @ResponseBody
    @RequestMapping(value = "create.htm")
    public ExtResult create(LaxinSetting laxin) {
        return laxinService.create(laxin);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int id, Model model) {
        LaxinSetting laxinSetting = laxinService.find(id);
        if(laxinSetting == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        model.addAttribute("entity", laxinSetting);
        return "/security/basic/laxin_setting/edit";
    }

    @ResponseBody
    @RequestMapping(value = "update.htm")
    public ExtResult update(LaxinSetting laxin) {
        return laxinService.update(laxin);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(long id, Model model) {
        LaxinSetting laxinSetting = laxinService.find(id);
        if (laxinSetting == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", laxinSetting);
        return "/security/basic/laxin_setting/view";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("qrcode.htm")
    public String qrcode(long id, Model model) {
        LaxinSetting laxinSetting = laxinService.find(id);
        if (laxinSetting == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        String url = String.format("%s/laxin_setting/join.htm?v=%d", getAppConfig().domainUrl, laxinSetting.getId());
        model.addAttribute("qrcode", url);
        return "/security/basic/laxin_setting/qrcode";
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return laxinService.delete(id);
    }
}
