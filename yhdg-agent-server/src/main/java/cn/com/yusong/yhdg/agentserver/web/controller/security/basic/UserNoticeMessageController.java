package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.UserNoticeMessage;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.UserNoticeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/user_notice_message")
public class UserNoticeMessageController extends SecurityController {
    @Autowired
    UserNoticeMessageService userNoticeMessageService;

//    @SecurityControl(limits = OperCodeConst.CODE_5_2_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_02_04.getValue());
        model.addAttribute("typeEnum", UserNoticeMessage.Type.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(UserNoticeMessage search) {
        return PageResult.successResult(userNoticeMessageService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        model.addAttribute("typeEnum", UserNoticeMessage.Type.values());
        UserNoticeMessage entity = userNoticeMessageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/user_notice_message/view";
    }

}
