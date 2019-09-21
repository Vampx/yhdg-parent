package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.service.basic.FwPushMessageTemplateDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/fw_push_message_template_detail")
public class FwPushMessageTemplateDetailController extends SecurityController {
    @Autowired
    FwPushMessageTemplateDetailService fwPushMessageTemplateDetailService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer templateId, Integer alipayfwId, Integer editFlag){
        model.addAttribute("templateId", templateId);
        model.addAttribute("editFlag", editFlag);
        model.addAttribute("alipayfwId", alipayfwId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(FwPushMessageTemplateDetail search) {
        return PageResult.successResult(fwPushMessageTemplateDetailService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int alipayfwId, String id, int templateId) {
        FwPushMessageTemplateDetail entity = fwPushMessageTemplateDetailService.find(id, alipayfwId, templateId);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/fw_push_message_template_detail/view";
    }

    @RequestMapping(value = "edit.htm", method = RequestMethod.GET)
    public String edit(Model model, int alipayfwId, String id, int templateId) {
        FwPushMessageTemplateDetail entity = fwPushMessageTemplateDetailService.find(id, alipayfwId, templateId);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/fw_push_message_template_detail/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(FwPushMessageTemplateDetail detail) {
        return fwPushMessageTemplateDetailService.update(detail);
    }

    @RequestMapping(value = "add.htm", method = RequestMethod.GET)
    public String add(Model model, int alipayfwId, int templateId) {
        model.addAttribute("alipayfwId", alipayfwId);
        model.addAttribute("templateId", templateId);
        return "/security/basic/fw_push_message_template_detail/add";
    }

    @RequestMapping("insert.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult insert(FwPushMessageTemplateDetail detail) {
        return fwPushMessageTemplateDetailService.insert(detail);
    }

}
