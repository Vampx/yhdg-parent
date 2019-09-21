package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.VoiceConfig;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.VoiceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/security/basic/voice_config")
public class VoiceConfigController extends SecurityController {
    @Autowired
    private VoiceConfigService voiceConfigService;

    @SecurityControl(limits = "basic.VoiceConfig:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, HttpSession httpSession) {
        model.addAttribute("partnerId", null);
        model.addAttribute(MENU_CODE_NAME, "basic.VoiceConfig:list");
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("add.htm")
    public void add(Model model) {
        model.addAttribute("typeList", VoiceConfig.Type.values());
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "edit.htm")
    public String edit(int id, Model model) {
        VoiceConfig voiceConfig = voiceConfigService.find(id);
        model.addAttribute("entity", voiceConfig);
        return "/security/basic/voice_config/edit";
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VoiceConfig search) {
        return PageResult.successResult(voiceConfigService.findPage(search));
    }


    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "create.htm")
    public ExtResult create(VoiceConfig entity) {
        if (entity.getAgentId() == null) {
            return ExtResult.failResult("运营商不能为空");
        }

        voiceConfigService.insert(entity);
        return ExtResult.successResult();
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "update.htm")
    public ExtResult update(VoiceConfig entity) {
        voiceConfigService.update(entity);
        return ExtResult.successResult();
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(int id, Model model) {
        VoiceConfig voiceConfig = voiceConfigService.find(id);
        model.addAttribute("entity", voiceConfig);
        return "/security/basic/voice_config/view";
    }

}
