package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplateDetail;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.basic.MpPushMessageTemplateDetailService;
import cn.com.yusong.yhdg.webserver.service.basic.MpPushMessageTemplateService;
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
@RequestMapping(value = "/security/basic/mp_push_message_template")
public class MpPushMessageTemplateController extends SecurityController {
    @Autowired
    MpPushMessageTemplateService mpPushMessageTemplateService;
    @Autowired
    AgentService agentService;
    @Autowired
    MpPushMessageTemplateDetailService mpPushMessageTemplateDetailService;
    @SecurityControl(limits = "basic.MpPushMessageTemplate:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer flag) {
        model.addAttribute(MENU_CODE_NAME, "basic.MpPushMessageTemplate:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(MpPushMessageTemplate search) {
        return PageResult.successResult(mpPushMessageTemplateService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, int weixinmpId, int id) {
        MpPushMessageTemplate entity = mpPushMessageTemplateService.find(weixinmpId, id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/mp_push_message_template/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, int weixinmpId, int id) {
        MpPushMessageTemplate entity = mpPushMessageTemplateService.find(weixinmpId, id);
        List<MpPushMessageTemplateDetail> detail=mpPushMessageTemplateDetailService.findByTemplateId(entity.getWeixinmpId(),entity.getId());

        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("detailList", detail);

        }
        return "/security/basic/mp_push_message_template/view_basic";
    }

    @RequestMapping(value = "edit.htm", method = RequestMethod.GET)
    public String edit1(Model model, int weixinmpId, int id) {
        MpPushMessageTemplate template = mpPushMessageTemplateService.find(weixinmpId, id);
        List<MpPushMessageTemplateDetail> templateDetailList = mpPushMessageTemplateDetailService.findByTemplateId(weixinmpId, id);
        if (template == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("template", template);
        model.addAttribute("templateDetailList", templateDetailList);
        return "/security/basic/mp_push_message_template/edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, int weixinmpId, int id) {
        MpPushMessageTemplate entity = mpPushMessageTemplateService.find(weixinmpId, id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/mp_push_message_template/edit_basic";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(String data) throws IOException, ParseException {
        return mpPushMessageTemplateService.update(data);
    }

    @RequestMapping(value = "add.htm")
    public String add(Model model) {
        return "/security/basic/mp_push_message_template/add";
    }

    @RequestMapping("insert.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public DataResult insert(MpPushMessageTemplate template) {
        return mpPushMessageTemplateService.insert(template);
    }

    @RequestMapping("view_push_message.htm")
    public String viewPushMessage(Model model, long userId) {
        model.addAttribute("userId", userId);
        return "/security/basic/mp_push_message_template/view_push_message";
    }

    @RequestMapping("agent_user_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult agentUserPage(MpPushMessageTemplate search) {

        return PageResult.successResult(mpPushMessageTemplateService.findByUserPage(search));
    }

    @RequestMapping("update_user_mp_push.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateUserMpPush(String[] idList, long userId) {
        return mpPushMessageTemplateService.updateUserMpPush(idList,userId);
    }
}
