package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodActivity;
import cn.com.yusong.yhdg.common.domain.hdg.VipExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.hdg.VipPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.hdg.PacketPeriodActivityService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipExchangeBatteryForegiftService;
import cn.com.yusong.yhdg.webserver.service.hdg.VipPriceService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/hdg/vip_price")
public class VipPriceController extends SecurityController {

    @Autowired
    VipPriceService vipPriceService;
    @Autowired
    VipExchangeBatteryForegiftService vipExchangeBatteryForegiftService;

    @SecurityControl(limits = "hdg.VipPrice:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model, Integer page, VipPrice search) {
        if (page == null) {
            page = 1;
        }
        model.addAttribute("page", page);
        model.addAttribute("search", search);
        model.addAttribute(MENU_CODE_NAME, "hdg.VipPrice:list");
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VipPrice search) {
        return PageResult.successResult(vipPriceService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id, int page, int rows) {
        VipPrice entity = vipPriceService.find(id);

        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("page", page);
            model.addAttribute("rows", rows);
        }
        return "/security/hdg/vip_price/edit";
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(VipPrice vipPrice,
                            Long [] vipExchangeIdList,
                            Long [] vipPacketPriceIdList,
                            String [] customerMobileList,
                            String [] cabinetIdList,
                            String [] stationIdList) {
        return vipPriceService.create(vipPrice, vipExchangeIdList,
                vipPacketPriceIdList, customerMobileList, cabinetIdList, stationIdList);
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(VipPrice vipPrice,
                            String [] customerMobileList,
                            String [] cabinetIdList,
                            String [] stationIdList) {
        return vipPriceService.update(vipPrice, customerMobileList, cabinetIdList, stationIdList);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(int id) {
        return vipPriceService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        VipPrice entity = vipPriceService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/vip_price/view";
    }

}
