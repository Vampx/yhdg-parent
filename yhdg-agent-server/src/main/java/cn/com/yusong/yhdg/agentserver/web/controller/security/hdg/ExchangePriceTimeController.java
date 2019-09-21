package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangePriceTime;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.agentserver.service.hdg.ExchangePriceTimeService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 换电价格
 */
@Controller
@RequestMapping(value = "/security/hdg/exchange_price_time")
public class ExchangePriceTimeController extends SecurityController {

    @Autowired
    ExchangePriceTimeService exchangePriceTimeService;

    @RequestMapping(value = "index.htm")
    public void index(Long priceId,int editFlag, Model model) {
        model.addAttribute("priceId", priceId);
        model.addAttribute("editFlag", editFlag);
    }

    @RequestMapping("add.htm")
    @ViewModel(ViewModel.INNER_PAGE)
    public void add(Long priceId, Model model) {
        model.addAttribute("priceId", priceId);
    }

    @RequestMapping("update")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(ExchangePriceTime entity) {
        ExchangePriceTime exchangePriceTime = exchangePriceTimeService.findByBatteryType(entity.getBatteryType(), entity.getAgentId());
        if (exchangePriceTime == null) {
            exchangePriceTimeService.insert(entity);
            return ExtResult.successResult();
        } else {
            return exchangePriceTimeService.update(entity);
        }
    }
}
