package cn.com.yusong.yhdg.webserver.web.controller.security.zc;


import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.zc.PriceSetting;
import cn.com.yusong.yhdg.common.domain.zc.RentPrice;
import cn.com.yusong.yhdg.common.domain.zd.RentBatteryType;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.webserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.webserver.service.zc.PriceSettingService;
import cn.com.yusong.yhdg.webserver.service.zc.RentPriceService;
import cn.com.yusong.yhdg.webserver.service.zd.RentBatteryTypeService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/security/zc/price_setting")
public class PriceSettingController extends SecurityController {
    @Autowired
    AgentBatteryTypeService agentBatteryTypeService;
    @Autowired
    RentBatteryTypeService rentBatteryTypeService;
    @Autowired
    PriceSettingService priceSettingService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    RentPriceService rentPriceService;

    @SecurityControl(limits = "zc.PriceSetting:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.PriceSetting:list");
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Set<Integer> checkedSet = Collections.emptySet();
        priceSettingService.tree(checkedSet, dummy, agentId, response.getOutputStream());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(PriceSetting search) {
        return PageResult.successResult(priceSettingService.findPage(search));
    }

    @RequestMapping("find_not_shop_price_page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult findNotShopPricePage(PriceSetting search) {
        return PageResult.successResult(priceSettingService.findNotShopPricePage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        model.addAttribute("CategoryEnum", PriceSetting.Category.values());
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(PriceSetting entity) {
        return priceSettingService.create(entity);
    }

    @ResponseBody
    @RequestMapping("find_hd_battery_count.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findHdBatteryCount(int agentId) {
        //运营商最大电池数
        int maxCount = 1;//默认1块电池
        String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue(), agentId);
        if(!StringUtils.isEmpty(maxCountStr)){
            maxCount = Integer.parseInt(maxCountStr);
        }
        return DataResult.successResult(maxCount);
    }

    @ResponseBody
    @RequestMapping("find_zd_battery_count.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findZdBatteryCount(int agentId) {
        int maxCount = 1;//默认1块电池
        String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.RENT_BATTERY_COUNT.getValue(), agentId);
        if(!StringUtils.isEmpty(maxCountStr)){
            maxCount = Integer.parseInt(maxCountStr);
        }
        return DataResult.successResult(maxCount);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, long id) {
        PriceSetting entity = priceSettingService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("CategoryEnum", PriceSetting.Category.values());
            List<RentPrice> rentPriceList = rentPriceService.findListByBatteryType(entity.getBatteryType(), entity.getAgentId(), entity.getId());
            model.addAttribute("rentPriceList", rentPriceList);
        }
        return "/security/zc/price_setting/edit";
    }

    @RequestMapping(value = "setting_battery_type.htm")
    public void settingBatteryType(Model model, Integer category, Integer agentId, Long priceSettingId) {
        List<AgentBatteryType> agentBatteryTypeList;
        List<RentBatteryType> rentBatteryTypeList;
        if (category == 1) {
            agentBatteryTypeList = agentBatteryTypeService.findBatteryListByAgentId(agentId);
            model.addAttribute("batteryTypeList", agentBatteryTypeList);
        } else {
            rentBatteryTypeList = rentBatteryTypeService.findBatteryListByAgentId(agentId);
            model.addAttribute("batteryTypeList", rentBatteryTypeList);
        }
        List<RentPrice> list = rentPriceService.findListBySetting(priceSettingId);
        if (list.size() > 0) {
            model.addAttribute("oneBatteryType", list.get(0).getBatteryType());
        } else {
            model.addAttribute("oneBatteryType", 0);
        }
        model.addAttribute("priceSettingId", priceSettingId);
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping(value = "setting_rent_price.htm")
    public void settingRentPrice(Model model, Integer batteryType, Integer agentId, Long priceSettingId) {
        List<RentPrice> rentPriceList = rentPriceService.findListByBatteryType(batteryType, agentId, priceSettingId);
        PriceSetting entity = priceSettingService.find(priceSettingId);
        model.addAttribute("rentPriceList", rentPriceList);
        model.addAttribute("agentId", agentId);
        model.addAttribute("category", entity.getCategory());
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult update(PriceSetting entity, Integer batteryType, String [] priceNameList,
                            Double [] foregiftPriceList, Double [] vehicleForegiftPriceList,
                            Double [] batteryForegiftPriceList,
                            Double [] rentPriceList,
                            Integer [] dayCountList,
                            Double [] vehicleRentPriceList,
                            Double [] batteryRentPriceList, Long [] priceIdList) {
        return priceSettingService.update(entity, batteryType, priceNameList, foregiftPriceList, vehicleForegiftPriceList,
                batteryForegiftPriceList, rentPriceList, dayCountList,vehicleRentPriceList, batteryRentPriceList, priceIdList);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(long id) {
        return priceSettingService.delete(id);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "show_price_setting.htm")
    public void showPriceSetting(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }
}
