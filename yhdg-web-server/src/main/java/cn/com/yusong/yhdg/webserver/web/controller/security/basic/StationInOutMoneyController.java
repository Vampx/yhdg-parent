package cn.com.yusong.yhdg.webserver.web.controller.security.basic;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.StationInOutMoney;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.StationInOutMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/basic/station_in_out_money")
public class StationInOutMoneyController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(StationInOutMoneyController.class);

    @Autowired
    StationInOutMoneyService stationInOutMoneyService;

    @RequestMapping(value = "index.htm")
    public void index(Model model, String shopId) {
        model.addAttribute("bizTypeEnum", StationInOutMoney.BizType.values());
        model.addAttribute("typeEnum", StationInOutMoney.Type.values());
        model.addAttribute("shopId", shopId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(StationInOutMoney search) {
        return PageResult.successResult(stationInOutMoneyService.findPage(search));
    }

}
