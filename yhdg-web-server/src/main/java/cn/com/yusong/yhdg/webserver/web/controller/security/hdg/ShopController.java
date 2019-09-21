package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.CodecUtils;
import cn.com.yusong.yhdg.webserver.service.basic.AgentService;
import cn.com.yusong.yhdg.webserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(value = "/security/hdg/shop")
public class ShopController extends SecurityController {
    @Autowired
    ShopService shopService;
    @Autowired
    AgentService agentService;
    @Autowired
    CabinetService cabinetService;

    @SecurityControl(limits = "hdg.Shop:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.Shop:list");
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Set<Integer> checkedSet = Collections.emptySet();
        shopService.tree(checkedSet, dummy, agentId, response.getOutputStream());
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "vehicle_tree.htm")
    public void Vehicle(String dummy, Integer agentId, Integer isVehicle, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Set<Integer> checkedSet = Collections.emptySet();
        shopService.vehicleTree(checkedSet, dummy, agentId, isVehicle, response.getOutputStream());
    }

    @ResponseBody
    @RequestMapping("find_shop.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult findShop(String shopId) {
        Shop shop = shopService.find(shopId);
        return DataResult.successResult(shop);
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Shop search) {
        return PageResult.successResult(shopService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {
        String id = shopService.findMaxId();
        model.addAttribute("activeStatusEnum", Shop.ActiveStatus.values());
        model.addAttribute("id", id);
    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(Shop entity) {
        return shopService.insert(entity);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, String id) {
        model.addAttribute("id", id);
        Shop entity = shopService.find(id);
        model.addAttribute("agentId", entity.getAgentId());
        return "/security/hdg/shop/edit";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, String id) {
        Shop entity = shopService.find(id);
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
        return "/security/hdg/shop/edit_basic";
    }

    @RequestMapping(value = "edit_payee.htm")
    public void editPayee(Model model, String id, Integer agentId) {
        Shop entity = shopService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("entity", entity);
    }

    @RequestMapping(value = "edit_ratio.htm")
    public String editShop(Model model, String id, Integer agentId) {
        Shop entity = shopService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("agentId", agentId);
        return "/security/hdg/shop/edit_ratio";
    }

    @RequestMapping(value = "edit_location.htm")
    public String editLocation(Model model, String id) {
        Shop entity = shopService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/shop/edit_location";
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
        return "/security/hdg/shop/add_location";
    }

    @RequestMapping(value = "edit_image.htm")
    public String editImage(Model model, String id) {
        Shop entity = shopService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/shop/edit_image";
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        Shop entity = shopService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/shop/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        Shop entity = shopService.find(id);
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
        return "/security/hdg/shop/view_basic";
    }

    @RequestMapping(value = "view_payee.htm")
    public String viewPayee(Model model, String id, Integer agentId) {
        Shop entity = shopService.find(id);
        model.addAttribute("agentId", agentId);
        model.addAttribute("entity", entity);
        return "/security/hdg/shop/view_payee";
    }

    @RequestMapping(value = "view_ratio.htm")
    public String viewShop(Model model, String id, Integer agentId) {
        Shop entity = shopService.find(id);
        model.addAttribute("entity", entity);
        model.addAttribute("agentId", agentId);
        return "/security/hdg/shop/view_ratio";
    }

    @RequestMapping(value = "image.htm", method = RequestMethod.GET)
    public void image(String num, Model model){
        model.addAttribute("num", num);

    }

    @RequestMapping(value = "view_location.htm")
    public String viewLocation(Model model, String id) {
        Shop entity = shopService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/shop/view_location";
    }

    @RequestMapping(value = "view_image.htm")
    public String viewImage(Model model, String id) {
        Shop entity = shopService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/shop/view_image";
    }

    @RequestMapping("update_basic.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasic(Shop entity) {
        return shopService.updateBasic(entity);
    }

    @RequestMapping("update_pay_people.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updatePayPeople(String id, String payPeopleName, String payPeopleMpOpenId, String payPeopleFwOpenId, String payPeopleMobile, String payPassword) {
        shopService.updatePayPeople(id, payPeopleName, payPeopleMpOpenId, payPeopleFwOpenId, payPeopleMobile, CodecUtils.password(payPassword));
        return ExtResult.successResult();
    }

    @RequestMapping("update_ratio.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateRatio(String id, Integer platformRatio, Integer agentRatio, Integer provinceAgentRatio, Integer cityAgentRatio, Integer shopRatio, Integer shopFixedMoney) {
        ExtResult result = shopService.updateRatio(id, platformRatio, agentRatio, provinceAgentRatio, cityAgentRatio, shopRatio, shopFixedMoney);
        return result;
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(String id) {
        return shopService.delete(id);
    }

    @RequestMapping("update_new_location.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateNewLocation(Shop entity) {
        return shopService.updateNewLocation(entity);
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

    @RequestMapping(value = "set_pay_people.htm")
    public String setPayPeople(Model model, String id) {
        model.addAttribute("id", id);
        Shop shop = shopService.find(id);
        model.addAttribute("agentId", shop.getAgentId());
        return "/security/hdg/shop/set_pay_people";
    }

}
