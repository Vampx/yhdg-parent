package cn.com.yusong.yhdg.webserver.web.controller.security.zc;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.domain.zc.VehicleVipPrice;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.webserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.webserver.service.zc.PriceSettingService;
import cn.com.yusong.yhdg.webserver.service.zc.RentPriceService;
import cn.com.yusong.yhdg.webserver.service.zc.VehicleVipPriceService;
import cn.com.yusong.yhdg.webserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/security/zc/vehicle_vip_price")
public class VehicleVipPriceController extends SecurityController {
    @Autowired
    AgentBatteryTypeService agentBatteryTypeService;
    @Autowired
    RentBatteryTypeService rentBatteryTypeService;
    @Autowired
    PriceSettingService priceSettingService;
    @Autowired
    VehicleVipPriceService vehicleVipPriceService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    RentPriceService rentPriceService;

    @SecurityControl(limits = "zc.VehicleVipPrice:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.VehicleVipPrice:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(VehicleVipPrice search) {
        return PageResult.successResult(vehicleVipPriceService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Integer id) {
        VehicleVipPrice entity = vehicleVipPriceService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/vehicle_vip_price/view";
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(VehicleVipPrice entity) {
        return vehicleVipPriceService.create(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Integer id) {
        VehicleVipPrice entity = vehicleVipPriceService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            PriceSetting priceSetting = priceSettingService.find(entity.getPriceSettingId().longValue());
            if (priceSetting != null) {
                entity.setCategory(priceSetting.getCategory());
            }
            model.addAttribute("entity", entity);
        }
        return "/security/zc/vehicle_vip_price/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(VehicleVipPrice entity) {
        return vehicleVipPriceService.update(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Integer id) {
        return vehicleVipPriceService.delete(id);
    }

}
