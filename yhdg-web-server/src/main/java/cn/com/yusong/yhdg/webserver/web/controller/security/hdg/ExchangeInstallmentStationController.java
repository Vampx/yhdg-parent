package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentCabinet;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentStation;
import cn.com.yusong.yhdg.common.domain.hdg.Station;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentCabinetService;
import cn.com.yusong.yhdg.webserver.service.hdg.ExchangeInstallmentStationService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/exchange_installment_station")
public class ExchangeInstallmentStationController extends SecurityController {

    @Autowired
    ExchangeInstallmentStationService exchangeInstallmentStationService;

    @SecurityControl(limits = "hdg.ExchangeInstallmentStation:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model,String agentId,String urlPid, String settingId) {
        model.addAttribute(MENU_CODE_NAME, "hdg.ExchangeInstallmentStation:list");
        model.addAttribute("isActive", ConstEnum.Flag.values());
        model.addAttribute("agentId", agentId);
        model.addAttribute("urlPid", urlPid);
        model.addAttribute("settingId", settingId);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Station search) {
        return PageResult.successResult(exchangeInstallmentStationService.findPage(search));
    }

    @RequestMapping("add_installment_station")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult addInstallmentCabinet(Model model, Long settingId,String[] stationIds){
        return exchangeInstallmentStationService.insert(settingId,stationIds);
    }

    @RequestMapping("delect_installment_station")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delectInstallmentCabinet(Model model, ExchangeInstallmentStation exchangeInstallmentStation){
        return exchangeInstallmentStationService.deleteStationId(exchangeInstallmentStation);
    }


    @RequestMapping(value = "index_installment_station.htm")
    public void indexInstallmentStation(Model model, String settingId) {
        model.addAttribute("isActive", ConstEnum.Flag.values());
        model.addAttribute("settingId", settingId);
    }

    @RequestMapping("page_installment_station.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult pageInstallmentStation(Station search) {
        return PageResult.successResult(exchangeInstallmentStationService.findPageInstallmentStation(search));
    }

}
