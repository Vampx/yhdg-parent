package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.FwPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.FwPushMessageTemplateDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.FwPushMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@Controller
@RequestMapping(value = "/security/basic/fw_push_message_template")
public class FwPushMessageTemplateController extends SecurityController {
    @Autowired
    FwPushMessageTemplateService fwPushMessageTemplateService;
    @Autowired
    AgentService agentService;
    @Autowired
    FwPushMessageTemplateDetailService fwPushMessageTemplateDetailService;

    @SecurityControl(limits = "basic.FwPushMessageTemplate:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "basic.FwPushMessageTemplate:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(FwPushMessageTemplate search) {
        return PageResult.successResult(fwPushMessageTemplateService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int alipayfwId, int id) {
        FwPushMessageTemplate entity = fwPushMessageTemplateService.find(alipayfwId, id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/fw_push_message_template/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, int alipayfwId, int id) {
        FwPushMessageTemplate entity = fwPushMessageTemplateService.find(alipayfwId, id);

        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            List<FwPushMessageTemplateDetail> detailList=fwPushMessageTemplateDetailService.findByTemplateId(entity.getAlipayfwId(),entity.getId());
            model.addAttribute("entity", entity);
            model.addAttribute("detailList", detailList);
        }
        return "/security/basic/fw_push_message_template/view_basic";
    }

    @RequestMapping(value = "edit.htm", method = RequestMethod.GET)
    public String edit(Model model, int alipayfwId, int id) {
        FwPushMessageTemplate template = fwPushMessageTemplateService.find(alipayfwId, id);
        List<FwPushMessageTemplateDetail> templateDetailList = fwPushMessageTemplateDetailService.findByTemplateId(alipayfwId, id);
        if (template == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("templateDetailList", templateDetailList);
        model.addAttribute("template", template);
        return "/security/basic/fw_push_message_template/edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, int alipayfwId, int id) {
        FwPushMessageTemplate entity = fwPushMessageTemplateService.find(alipayfwId, id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/fw_push_message_template/edit_basic";
    }

//    @RequestMapping("update_basic.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public ExtResult updateBasic(Integer id, Integer alipayfwId, String templateName, String variable, String fwCode, Integer isActive, String memo) {
//        return fwPushMessageTemplateService.update(id, alipayfwId, templateName, variable, fwCode, isActive, memo);
//    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(String data) throws IOException, ParseException {
        return fwPushMessageTemplateService.update(data);
    }

    @RequestMapping(value = "add.htm")
    public String add(Model model) {
        return "/security/basic/fw_push_message_template/add";
    }

    @RequestMapping("insert.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public DataResult insert(FwPushMessageTemplate template) {
        return fwPushMessageTemplateService.insert(template);
    }

}
