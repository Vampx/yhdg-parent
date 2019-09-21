package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.PartnerInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.basic.PartnerInOutMoneyService;
import cn.com.yusong.yhdg.webserver.service.basic.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/basic/partner_in_out_money")
public class PartnerInOutMoneyController extends SecurityController {

    @Autowired
    PartnerInOutMoneyService partnerInOutMoneyService;

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping("view_record.htm")
    public void viewRecord(Long id, Model model) {
        PartnerInOutMoney partnerInOutMoney = partnerInOutMoneyService.find(id);
        model.addAttribute("entity", partnerInOutMoney);
    }
}
