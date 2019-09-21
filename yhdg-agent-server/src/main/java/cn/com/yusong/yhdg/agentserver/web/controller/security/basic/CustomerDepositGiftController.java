package cn.com.yusong.yhdg.agentserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerDepositGift;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.agentserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerDepositGiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/security/basic/customer_deposit_gift")
public class CustomerDepositGiftController extends SecurityController {

    @Autowired
    CustomerDepositGiftService customerDepositGiftService;

//    @SecurityControl(limits = OperCodeConst.CODE_1_8_4_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer partnerId) {

        List<CustomerDepositGift> customerDepositGiftList = customerDepositGiftService.findPartnerId(partnerId);

        model.addAttribute("customerGiftList", customerDepositGiftList);
        if (partnerId != null) {
            model.addAttribute("partnerId", partnerId);
        }
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_01_08_04.getValue());
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(int[] partnerId, float[] money, float[] gift) {
        return customerDepositGiftService.update(partnerId, money, gift);
    }
}
