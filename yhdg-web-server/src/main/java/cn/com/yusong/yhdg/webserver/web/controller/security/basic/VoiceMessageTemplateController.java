package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.VoiceMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.VoiceMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/voice_message_template")
public class VoiceMessageTemplateController extends SecurityController {
    @Autowired
    VoiceMessageTemplateService voiceMessageTemplateService;

    @SecurityControl(limits = "basic.VoiceMessageTemplate:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, String menuCode) {
        model.addAttribute(MENU_CODE_NAME, "basic.VoiceMessageTemplate:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VoiceMessageTemplate search) {
        return PageResult.successResult(voiceMessageTemplateService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("edit.htm")
    public String edit(int partnerId, int id, Model model) {
        VoiceMessageTemplate entity = voiceMessageTemplateService.find(partnerId, id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/voice_message_template/edit";
    }

    @ViewModel(ViewModel.JSON)
    @RequestMapping("update.htm")
    @ResponseBody
    public ExtResult update(VoiceMessageTemplate entity) {
        return voiceMessageTemplateService.update(entity);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view.htm")
    public String view(int partnerId, int id, Model model) {
        VoiceMessageTemplate entity = voiceMessageTemplateService.find(partnerId, id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/voice_message_template/view";
    }
}
