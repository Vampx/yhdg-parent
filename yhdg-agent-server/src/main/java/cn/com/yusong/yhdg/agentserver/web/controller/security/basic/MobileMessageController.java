package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessage;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.MobileMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by chen on 2017/10/28.
 */
@Controller
@RequestMapping(value = "/security/basic/mobile_message")
public class MobileMessageController extends SecurityController {
    @Autowired
    MobileMessageService mobileMessageService;

//    @SecurityControl(limits = OperCodeConst.CODE_5_1_3_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_01_03.getValue());
        model.addAttribute("messageStatusEnum", MobileMessage.MessageStatus.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(MobileMessage search) {
        return PageResult.successResult(mobileMessageService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        model.addAttribute("messageStatusEnum", MobileMessage.MessageStatus.values());
        MobileMessage entity = mobileMessageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/mobile_message/view";
    }

    @RequestMapping("send.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult send(long id) {
        int effect = mobileMessageService.updateStatus(id, MobileMessage.MessageStatus.NOT.getValue());
        if(effect > 0) {
            return ExtResult.successResult();
        } else {
            return ExtResult.failResult("记录不存在");
        }
    }

}
