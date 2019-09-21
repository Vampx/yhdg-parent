package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.WeixinmpTemplateMessage;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.WeixinmpTemplateMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value = "/security/basic/weixinmp_template_message")
public class WeixinmpTemplateMessageController extends SecurityController {
    @Autowired
    WeixinmpTemplateMessageService weixinmpTemplateMessageService;

//    @SecurityControl(limits = OperCodeConst.CODE_5_3_2_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_03_02.getValue());

        model.addAttribute("messageStatusEnum", WeixinmpTemplateMessage.MessageStatus.values());
        model.addAttribute("sourceTypeEnum", WeixinmpTemplateMessage.SourceType.values());
        model.addAttribute("typeEnum", ConstEnum.AppPushMessageTemplateType.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(WeixinmpTemplateMessage search) {
        return PageResult.successResult(weixinmpTemplateMessageService.findPage(search));
    }


    @RequestMapping(value = "view.htm")
    public String view(Model model, int id) {
        WeixinmpTemplateMessage entity = weixinmpTemplateMessageService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("id", id);
            model.addAttribute("entity", entity);
            model.addAttribute("messageStatusEnum", WeixinmpTemplateMessage.MessageStatus.values());
            model.addAttribute("sourceTypeEnum", WeixinmpTemplateMessage.SourceType.values());
        }
        return "/security/basic/weixinmp_template_message/view";
    }

    @RequestMapping(value = "view_info.htm")
    public void viewInfo(Model model, String sourceId) {
        model.addAttribute("sourceId", sourceId);
        model.addAttribute("SourceType", WeixinmpTemplateMessage.SourceType.SYSTEM.getValue());
        model.addAttribute("messageStatusEnum", WeixinmpTemplateMessage.MessageStatus.values());
        model.addAttribute("sourceTypeEnum", WeixinmpTemplateMessage.SourceType.values());
        model.addAttribute("typeEnum", ConstEnum.AppPushMessageTemplateType.values());
    }
}
