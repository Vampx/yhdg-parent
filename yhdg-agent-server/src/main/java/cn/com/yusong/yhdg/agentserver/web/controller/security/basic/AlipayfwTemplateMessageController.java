package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AlipayfwTemplateMessage;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.AlipayfwTemplateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/alipayfw_template_message")
public class AlipayfwTemplateMessageController extends SecurityController {
    @Autowired
    AlipayfwTemplateMessageService alipayfwTemplateMessageService;

//    @SecurityControl(limits = OperCodeConst.CODE_5_4_2_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_04_02.getValue());

        model.addAttribute("messageStatusEnum", AlipayfwTemplateMessage.MessageStatus.values());
        model.addAttribute("sourceTypeEnum", AlipayfwTemplateMessage.SourceType.values());
        model.addAttribute("typeEnum", ConstEnum.AppPushMessageFwTemplateType.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(AlipayfwTemplateMessage search) {
        return PageResult.successResult(alipayfwTemplateMessageService.findPage(search));
    }


    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        AlipayfwTemplateMessage entity = alipayfwTemplateMessageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("id", id);
            model.addAttribute("entity", entity);
            model.addAttribute("messageStatusEnum", AlipayfwTemplateMessage.MessageStatus.values());
            model.addAttribute("sourceTypeEnum", AlipayfwTemplateMessage.SourceType.values());
        }
        return "/security/basic/alipayfw_template_message/view";
    }

    @RequestMapping(value = "view_info.htm")
    public void viewInfo(Model model, String sourceId) {
        model.addAttribute("sourceId",sourceId);
        model.addAttribute("sourceTypeEnum", AlipayfwTemplateMessage.SourceType.values());
        model.addAttribute("SourceType", AlipayfwTemplateMessage.SourceType.CHARGER_ORDER.getValue());
        model.addAttribute("typeEnum", ConstEnum.AppPushMessageFwTemplateType.values());
        model.addAttribute("messageStatusEnum", AlipayfwTemplateMessage.MessageStatus.values());
    }

}
