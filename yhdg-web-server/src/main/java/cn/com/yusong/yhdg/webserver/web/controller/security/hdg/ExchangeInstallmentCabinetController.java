package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentCabinetService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/exchange_installment_cabinet")
public class ExchangeInstallmentCabinetController extends SecurityController {

    @Autowired
    ExchangeInstallmentCabinetService exchangeInstallmentCabinetService;

    @SecurityControl(limits = "hdg.ExchangeInstallmentCabinet:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model,String agentId,String urlPid, String settingId) {
        model.addAttribute(MENU_CODE_NAME, "hdg.ExchangeInstallmentCabinet:list");
        model.addAttribute("isActive", ConstEnum.Flag.values());
        model.addAttribute("agentId", agentId);
        model.addAttribute("urlPid", urlPid);
        model.addAttribute("settingId", settingId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Cabinet search) {
        return PageResult.successResult(exchangeInstallmentCabinetService.findPage(search));
    }

    @RequestMapping("add_installment_cabinet")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult addInstallmentCabinet(Model model, Long settingId,String[] cabinetIds){
        return exchangeInstallmentCabinetService.insert(settingId,cabinetIds);
    }

    @RequestMapping("delect_installment_cabinet")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delectInstallmentCabinet(Model model, ExchangeInstallmentCabinet exchangeInstallmentCabinet){
        return exchangeInstallmentCabinetService.deleteCabinetId(exchangeInstallmentCabinet);
    }


    @RequestMapping(value = "index_installment_cabint.htm")
    public void indexInstallmentCabint(Model model, String settingId) {
        model.addAttribute("isActive", ConstEnum.Flag.values());
        model.addAttribute("settingId", settingId);
    }

    @RequestMapping("page_installment_cabint.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageInstallmentCabint(Cabinet search) {
        return PageResult.successResult(exchangeInstallmentCabinetService.findPageInstallmentCabint(search));
    }

}
