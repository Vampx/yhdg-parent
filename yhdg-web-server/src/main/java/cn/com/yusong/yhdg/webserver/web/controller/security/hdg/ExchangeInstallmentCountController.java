package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCount;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentCountService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/security/hdg/exchange_installment_count")
public class ExchangeInstallmentCountController extends SecurityController {

    @Autowired
    ExchangeInstallmentCountService exchangeInstallmentCountService;

    //****2019年9月2日15:40:46 新加
    @RequestMapping("add_installment_count")
    public String addInstallmentCount(Model model ,String urlPid,String settingId){
        model.addAttribute("urlPid",urlPid);
        model.addAttribute("settingId",settingId);
        return "/security/hdg/exchange_installment_setting/add_installment_count";
    }
    @RequestMapping("edit_installment_count")
    public String updateStandardNum(Model model , ExchangeInstallmentCount exchangeInstallmentCount,String urlPid){
        model.addAttribute("urlPid",urlPid);
        if(exchangeInstallmentCount.getId()!=null){
            ExchangeInstallmentCount exchangeInstallmentCount1 = exchangeInstallmentCountService.find(exchangeInstallmentCount.getId());
            if(exchangeInstallmentCount1!=null){
                exchangeInstallmentCount=exchangeInstallmentCount1;
            }
        }
        model.addAttribute("entity",exchangeInstallmentCount);
        return "/security/hdg/exchange_installment_setting/edit_installment_count";
    }

    @RequestMapping("insert_installment_count")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult insertInstallmentCount(Model model,ExchangeInstallmentCount exchangeInstallmentCount) {
        return exchangeInstallmentCountService.insert(exchangeInstallmentCount);
    }

    @RequestMapping("delect_installment_count")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delectInstallmentCount(Model model,Long id) {
        return exchangeInstallmentCountService.delete(id);
    }
    @RequestMapping("update_installment_count")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateInstallmentCount(Model model,ExchangeInstallmentCount exchangeInstallmentCount) {
        return exchangeInstallmentCountService.update(exchangeInstallmentCount);
    }

}
