package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.MobileMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chen on 2017/10/30.
 */
@Controller
@RequestMapping(value = "/security/basic/mobile_message_template")
public class MobileMessageTemplateController extends SecurityController {
    @Autowired
    MobileMessageTemplateService mobileMessageTemplateService;

    @SecurityControl(limits = "basic.MobileMessageTemplate:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, String menuCode) {
        model.addAttribute(MENU_CODE_NAME, "basic.MobileMessageTemplate:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(MobileMessageTemplate search) {
        return PageResult.successResult(mobileMessageTemplateService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int partnerId, int id, Model model) {
        MobileMessageTemplate entity = mobileMessageTemplateService.find(partnerId, id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/mobile_message_template/edit";
    }

    @ViewModel(ViewModel.JSON)
    @RequestMapping("update.htm")
    @ResponseBody
    public ExtResult update(MobileMessageTemplate entity) {
        return mobileMessageTemplateService.update(entity);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(int partnerId, int id, Model model) {
        MobileMessageTemplate entity = mobileMessageTemplateService.find(partnerId, id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/mobile_message_template/view";
    }
}
