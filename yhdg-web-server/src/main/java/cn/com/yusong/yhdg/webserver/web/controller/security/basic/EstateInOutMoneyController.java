package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.EstateInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.EstateInOutMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/estate_in_out_money")
public class EstateInOutMoneyController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(EstateInOutMoneyController.class);

    @Autowired
    EstateInOutMoneyService estateInOutMoneyService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, String estateId) {
        model.addAttribute("bizTypeEnum", EstateInOutMoney.BizType.values());
        model.addAttribute("typeEnum", EstateInOutMoney.Type.values());
        model.addAttribute("estateId", estateId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(EstateInOutMoney search) {
        return PageResult.successResult(estateInOutMoneyService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        EstateInOutMoney estateInOutMoney = estateInOutMoneyService.find(id);
        model.addAttribute("entity", estateInOutMoney);
        return "/security/basic/estate_in_out_money/view";
    }

    @RequestMapping(value = "alert_page.htm")
    public void alertPage(Model model, String estateId) {
        model.addAttribute("estateId", estateId);
        model.addAttribute("typeEnum", EstateInOutMoney.Type.values());
    }

}
