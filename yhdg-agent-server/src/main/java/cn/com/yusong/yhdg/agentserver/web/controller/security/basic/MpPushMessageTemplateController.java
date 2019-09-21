package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.MpPushMessageTemplate;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.basic.MpPushMessageTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/security/basic/mp_push_message_template")
public class MpPushMessageTemplateController extends SecurityController {
    @Autowired
    MpPushMessageTemplateService mpPushMessageTemplateService;
    @Autowired
    AgentService agentService;

//    @SecurityControl(limits = OperCodeConst.CODE_5_3_1_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer flag) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_03_01.getValue());
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
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/basic/mp_push_message_template/view_basic";
    }

    @RequestMapping(value = "edit.htm", method = RequestMethod.GET)
    public String edit(Model model, int weixinmpId, int id) {
        MpPushMessageTemplate entity = mpPushMessageTemplateService.find(weixinmpId, id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
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

    @RequestMapping("update_basic.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasic(Integer id, Integer weixinmpId, String templateName, String variable, String mpCode, Integer isActive, String memo) {
        return mpPushMessageTemplateService.update(id, weixinmpId, templateName, variable, mpCode, isActive, memo);
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

}
