package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.MpPushMessageTemplateDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/mp_push_message_template_detail")
public class MpPushMessageTemplateDetailController extends SecurityController {
    @Autowired
    MpPushMessageTemplateDetailService mpPushMessageTemplateDetailService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer templateId, Integer weixinmpId, Integer editFlag){
        model.addAttribute("templateId", templateId);
        model.addAttribute("editFlag", editFlag);
        model.addAttribute("weixinmpId", weixinmpId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(MpPushMessageTemplateDetail search) {
        return PageResult.successResult(mpPushMessageTemplateDetailService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int weixinmpId, String id, int templateId) {
        MpPushMessageTemplateDetail template = mpPushMessageTemplateDetailService.find(id, weixinmpId, templateId);
        if(template == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", template);
        }
        return "/security/basic/mp_push_message_template_detail/view";
    }

    @RequestMapping(value = "edit.htm", method = RequestMethod.GET)
    public String edit(Model model, int weixinmpId, String id, int templateId) {
        MpPushMessageTemplateDetail template = mpPushMessageTemplateDetailService.find(id, weixinmpId, templateId);
        if(template == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", template);
        return "/security/basic/mp_push_message_template_detail/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(MpPushMessageTemplateDetail detail) {
        return mpPushMessageTemplateDetailService.update(detail);
    }

    @RequestMapping(value = "add.htm", method = RequestMethod.GET)
    public String add(Model model, int weixinmpId, int templateId) {
        model.addAttribute("weixinmpId", weixinmpId);
        model.addAttribute("templateId", templateId);
        return "/security/basic/mp_push_message_template_detail/add";
    }

    @RequestMapping("insert.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult insert(MpPushMessageTemplateDetail detail) {
        return mpPushMessageTemplateDetailService.insert(detail);
    }

}
