package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerNoticeMessage;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerNoticeMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/customer_notice_message")
public class CustomerNoticeMessageController extends SecurityController {
    @Autowired
    CustomerNoticeMessageService customerNoticeMessageService;

//    @SecurityControl(limits = OperCodeConst.CODE_5_2_3_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_05_02_03.getValue());
        model.addAttribute("typeEnum", CustomerNoticeMessage.Type.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerNoticeMessage search) {
        return PageResult.successResult(customerNoticeMessageService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        model.addAttribute("typeEnum", CustomerNoticeMessage.Type.values());
        CustomerNoticeMessage entity = customerNoticeMessageService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        model.addAttribute("entity", entity);
        return "/security/basic/customer_notice_message/view";
    }

}
