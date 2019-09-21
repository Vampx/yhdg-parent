package cn.com.yusong.yhdg.webserver.web.controller.security.zc;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import cn.com.yusong.yhdg.webserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.service.zc.ShopRentCarService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/zc/shop_rent_car")
public class ShopRentCarController extends SecurityController {
    @Autowired
    ShopRentCarService shopRentCarService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    SystemConfigService systemConfigService;

    @SecurityControl(limits = "zc.ShopRentCar:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.ShopRentCar:list");
    }

    @ResponseBody
    @RequestMapping("find_shop.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findShop(String shopId) {
        Shop shop = shopRentCarService.find(shopId);
        return DataResult.successResult(shop);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "vehicle_vip_price_shop_page.htm")
    public void vehicleVipPriceShopPage(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }
    
    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult rentCarPage(Shop search) {
        return PageResult.successResult(shopRentCarService.Page(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        String id = shopRentCarService.findMaxId();
        model.addAttribute("activeStatusEnum", Shop.ActiveStatus.values());
        model.addAttribute("id", id);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Shop entity) {
        return shopRentCarService.insert(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, String id) {
        model.addAttribute("id", id);
        Shop entity = shopRentCarService.find(id);
        model.addAttribute("agentId", entity.getAgentId());
        return "/security/zc/shop_rent_car/edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, String id) {
        Shop entity = shopRentCarService.find(id);
        List<Cabinet> cabinetList = cabinetService.findListByShopId(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
                String beginTime = entity.getWorkTime().substring(0, pattern.length());
                String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
            entity.setCabinetList(cabinetList);
            model.addAttribute("entity", entity);
            model.addAttribute("activeStatusEnum", Shop.ActiveStatus.values());
        }
        return "/security/zc/shop_rent_car/edit_basic";
    }

    @RequestMapping(value = "edit_payee.htm")
    public void editPayee(Model model, String id, Integer agentId) {
        Shop entity = shopRentCarService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("entity", entity);
    }

    @RequestMapping(value = "edit_ratio.htm")
    public String editShop(Model model, String id, Integer agentId) {
        Shop entity = shopRentCarService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("agentId", agentId);
        return "/security/zc/shop_rent_car/edit_ratio";
    }

    @RequestMapping(value = "edit_location.htm")
    public String editLocation(Model model, String id) {
        Shop entity = shopRentCarService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/shop_rent_car/edit_location";
    }
    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "vip_price_shop_page.htm")
    public void vipPriceShopPage(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }


    @RequestMapping(value = "add_location.htm")
    public String addLocation(Model model) {
        model.addAttribute("lng", Constant.LNG);
        model.addAttribute("lat", Constant.LAT);
        return "/security/zc/shop_rent_car/add_location";
    }

    @RequestMapping(value = "edit_image.htm")
    public String editImage(Model model, String id) {
        Shop entity = shopRentCarService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/shop_rent_car/edit_image";
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        Shop entity = shopRentCarService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/shop_rent_car/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        Shop entity = shopRentCarService.find(id);
        List<Cabinet> cabinetList = cabinetService.findListByShopId(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
                String beginTime = entity.getWorkTime().substring(0, pattern.length());
                String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
            entity.setCabinetList(cabinetList);
            model.addAttribute("entity", entity);
            model.addAttribute("activeStatusEnum", Shop.ActiveStatus.values());

            Map map = new HashMap();
            String url = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue());
            map.put("qrcode", String.format(ConstEnum.Qrcode.QRCODE_SHOP.getFormat(), url, entity.getId()));
            model.addAttribute("qrCodeAddress", map.get("qrcode"));

        }
        return "/security/zc/shop_rent_car/view_basic";
    }

    @RequestMapping(value = "view_ratio.htm")
    public String viewShop(Model model, String id, Integer agentId) {
        Shop entity = shopRentCarService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("agentId", agentId);
        return "/security/zc/shop_rent_car/view_ratio";
    }

    @RequestMapping(value = "image.htm", method = RequestMethod.GET)
    public void image(String num, Model model){
        model.addAttribute("num", num);

    }

    @RequestMapping(value = "image.htm", method = RequestMethod.POST)
    public String image(@RequestParam("file") MultipartFile file, int num, Model model) throws IOException {
        HttpClientUtils.HttpResp httpResp = shopRentCarService.uploadImage(file, num);
        if (httpResp.status / 100 != 2) {
            model.addAttribute("message", "上传静态服务器失败");
            model.addAttribute("success", false);
            return "/security/zc/shop_rent_car/image_response";
        }
        Map map = (Map) AppUtils.decodeJson(httpResp.content, Map.class);
        Map data = (Map) map.get("data");

        String imagePath1 = (String) data.get("filePath1");
        String imagePath2 = (String) data.get("filePath2");
        String fileName1 = (String) data.get("fileName1");
        String fileName2 = (String) data.get("fileName2");

        model.addAttribute("success", true);
        model.addAttribute("filePath1", imagePath1);
        model.addAttribute("filePath2", imagePath2);
        model.addAttribute("fileName1", fileName1);
        model.addAttribute("fileName2", fileName2);
        model.addAttribute("num", num);
        return "/security/zc/shop_rent_car/image_response";
    }

    @RequestMapping("update_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateImage(Shop shop) throws IOException {
        return shopRentCarService.updateImage(shop);
    }

    @RequestMapping(value = "view_location.htm")
    public String viewLocation(Model model, String id) {
        Shop entity = shopRentCarService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/shop_rent_car/view_location";
    }

    @RequestMapping(value = "view_image.htm")
    public String viewImage(Model model, String id) {
        Shop entity = shopRentCarService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/shop_rent_car/view_image";
    }

    @RequestMapping(value = "view_payee.htm")
    public String viewPayee(Model model, String id, Integer agentId) {
        Shop entity = shopRentCarService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("entity", entity);
        return "/security/hdg/shop/view_payee";
    }

    @RequestMapping("update_basic.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasic(Shop entity) {
        return shopRentCarService.updateBasic(entity);
    }

    @RequestMapping("update_ratio.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateRatio(String id, Integer zcPlatformRatio, Integer zcAgentRatio, Integer zcProvinceAgentRatio, Integer zcCityAgentRatio, Integer zcShopRatio, Integer zcShopFixedMoney) {
        ExtResult result = shopRentCarService.updateRatio(id, zcPlatformRatio, zcAgentRatio, zcProvinceAgentRatio, zcCityAgentRatio, zcShopRatio, zcShopFixedMoney);
        return result;
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(String id) {
        return shopRentCarService.delete(id);
    }

    @RequestMapping("update_new_location.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateNewLocation(Shop entity) {
        return shopRentCarService.updateNewLocation(entity);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_shop.htm")
    public void selectShop(Model model, Integer agentId) {
        if (agentId != null) {
            model.addAttribute("agentId", agentId);
        } else {
            model.addAttribute("agentId", null);
        }
    }

    @RequestMapping("set_pay_people_update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult setPayPeopleUpdate(Shop entity) {
        return  shopRentCarService.setPayPeopleUpdate(entity);
    }

    @RequestMapping("update_pay_people.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updatePayPeople(String id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        shopRentCarService.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, CodecUtils.password(payPassword));
        return ExtResult.successResult();
    }
}
