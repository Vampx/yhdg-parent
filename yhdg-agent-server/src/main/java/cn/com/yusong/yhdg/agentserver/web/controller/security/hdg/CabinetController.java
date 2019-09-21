package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.biz.server.ClientBizUtils;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentBatteryTypeService;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentService;
import cn.com.yusong.yhdg.agentserver.service.basic.AgentSystemConfigService;
import cn.com.yusong.yhdg.agentserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.agentserver.service.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.yms.TerminalService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.yms.Terminal;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.HttpClientUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping(value = "/security/hdg/cabinet")
public class CabinetController extends SecurityController {
    @Autowired
    CabinetService cabinetService;
    @Autowired
    ShopService shopService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    AgentService agentService;
    @Autowired
    CabinetPropertyService cabinetPropertyService;
    @Autowired
    TerminalService terminalService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    AgentBatteryTypeService agentBatteryTypeService;
    @Autowired
    CabinetDayDegreeStatsService cabinetDayDegreeStatsService;
    @Autowired
    VipPriceShopService vipPriceShopService;

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "tree.htm")
    public void tree(String dummy, Integer agentId, HttpServletResponse response) throws Exception {
        response.setContentType(ConstEnum.ContentType.JSON.getValue());
        Set<Integer> checkedSet = Collections.emptySet();
        cabinetService.tree(checkedSet, dummy, agentId, response.getOutputStream());
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON_ARRAY)
    @RequestMapping(value = "cabinet_list_tree.htm")
    public void cabinetListTree(Long cabinetCompanyId, HttpServletResponse response) throws Exception {
        cabinetService.cabinetListTree(cabinetCompanyId, response.getOutputStream());
    }

    @SecurityControl(limits = "hdg.Cabinet:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.Cabinet:list");
    }

    @ResponseBody
    @ViewModel(ViewModel.JSON)
    @RequestMapping(value = "unique.htm")
    public ExtResult unique(String id) {
        return cabinetService.findUnique(id);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "unbind_cabinet_page.htm")
    public void unbindCabinetPage(Model model, Integer agentId, Integer batteryType) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("batteryType", batteryType);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "unbind_shop_cabinet_page.htm")
    public void unbindShopCabinetPage(Model model, Integer agentId, String shopId) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("shopId", shopId);
    }

    @RequestMapping(value = "cabinet_detail.htm")
    public String cabinetDetail(Model model, String id, HttpServletRequest request) {
        model.addAttribute("id", id);
        Cabinet entity = cabinetService.find(id);
        model.addAttribute("entity", entity);
        //查找用电量
        CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findByCabinetId(id);
        if (cabinetDayDegreeStats != null) {
            model.addAttribute("endNum", cabinetDayDegreeStats.getEndNum());
        } else {
            model.addAttribute("endNum", 0);
        }
        List<CabinetBox> cabinetBoxList=cabinetBoxService.findBySubcabinet(id);
        model.addAttribute("cabinetBoxList", cabinetBoxList);
        return null;
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Cabinet search) {
        return PageResult.successResult(cabinetService.findPage(search));
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "vip_price_cabinet_page.htm")
    public void vipPriceCabinetPage(Model model, Integer agentId) {
        model.addAttribute("agentId", agentId);
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, String id) {
        List<AgentBatteryType> agentBatteryTypeList = agentBatteryTypeService.findListByCabinetId(id);
        if (agentBatteryTypeList.size() == 0) {
            model.addAttribute("batteryTypeListColourFlag", ConstEnum.Flag.FALSE.getValue());
        } else {
            model.addAttribute("batteryTypeListColourFlag", ConstEnum.Flag.TRUE.getValue());
        }
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/edit";
    }

    @RequestMapping(value = "edit_battery_type.htm")
    public String editCabinet(Model model, String cabinetId, Integer agentId) {
        model.addAttribute("cabinetId", cabinetId);
        model.addAttribute("agentId", agentId);
        return "/security/hdg/cabinet/edit_battery_type";
    }

    @RequestMapping(value = "edit_ratio.htm")
    public String editRatio(Model model, String cabinetId, Integer agentId) {
        Cabinet entity = cabinetService.find(cabinetId);
        model.addAttribute("entity", entity);
        model.addAttribute("cabinetId", cabinetId);
        model.addAttribute("agentId", agentId);
        return "/security/hdg/cabinet/edit_ratio";
    }

    @RequestMapping(value = "edit_basic.htm")
    public String editBasic(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("SubtypeEnum", Cabinet.Subtype.values());
            model.addAttribute("ViewTypeEnum", Cabinet.ViewType.values());
            model.addAttribute("entity", entity);
            model.addAttribute("activeStatusEnum", Cabinet.ActiveStatus.values());
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
                String beginTime = entity.getWorkTime().substring(0, pattern.length());
                String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
        }
        return "/security/hdg/cabinet/edit_basic";
    }

    @RequestMapping(value = "edit_online.htm")
    public String editOnline(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/edit_online";
    }

    @RequestMapping("batch_bind_battery_type.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult batchBindBatteryType(String[] cabinetIdList, Integer batteryType) {
        int count = 0;
        for (String e : cabinetIdList) {
            count += cabinetService.bindBatteryType(e, batteryType);
        }
        return ExtResult.successResult(String.format("成功绑定%d个电池类型", count));
    }

    @RequestMapping("batch_bind_shop.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult batchBindShop(String[] cabinetIdList, String shopId) {
        int count = 0;
        for (String e : cabinetIdList) {
            count += cabinetService.bindShop(e, shopId);
        }
        VipPriceShop vipPriceShop = vipPriceShopService.findByShopId(shopId);
        if (vipPriceShop != null) {
            cabinetService.updateShopPrice(vipPriceShop.getId(), shopId);
        }else {
            cabinetService.updateShopPriceByCabint(shopId);
        }
        return ExtResult.successResult(String.format("成功绑定%d个门店", count));
    }

    @RequestMapping("unbind_battery_type.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult unbindBatteryType(String cabinetId, Integer batteryType) {
        return cabinetService.unbindBatteryType(cabinetId, batteryType);
    }

    @RequestMapping("unbind_shop.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult unbindShop(String cabinetId) {
        return cabinetService.unbindShop(cabinetId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "unbind_estate_cabinet_page.htm")
    public void unbindEstateCabinetPage(Model model, Integer agentId, String estateId) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("estateId", estateId);
    }

    @RequestMapping("batch_bind_estate.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult batchBindEstate(String[] cabinetIdList, String estateId) {
        int count = 0;
        for (String e : cabinetIdList) {
            count += cabinetService.bindEstate(e, estateId);
        }
        return ExtResult.successResult(String.format("成功绑定%d个物业", count));
    }

    @RequestMapping("unbind_estate.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult unbindEstate(String cabinetId) {
        return cabinetService.unbindEstate(cabinetId);
    }

    @RequestMapping("update_basic.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateBasic(Cabinet entity) {
        ExtResult result = cabinetService.updateBasic(entity);

        Cabinet cabinet = cabinetService.find(entity.getId());
        if (StringUtils.isNotEmpty(cabinet.getTerminalId())) {
            Terminal terminal = terminalService.find(cabinet.getTerminalId());
            if (terminal != null) {
                ClientBizUtils.noticeCabinetConfigUpdate(appConfig, terminal.getId(), cabinet.getAddress(), cabinet.getTel());
            }
        }
        return result;
    }

    @RequestMapping("update_ratio.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateRatio(String id, Integer platformRatio, Integer agentRatio, Integer provinceAgentRatio, Integer cityAgentRatio, Integer shopRatio) {
        ExtResult result = cabinetService.updateRatio(id, platformRatio, agentRatio, provinceAgentRatio, cityAgentRatio, shopRatio);
        return result;
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(String id) {
        return cabinetService.delete(id);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            Integer agentId = entity.getAgentId();
            if (agentId != null && agentId != 0) {
                entity.setAgentName(agentService.find(agentId).getAgentName());
            }
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/view";
    }

    @RequestMapping(value = "edit_image.htm")
    public String editImage(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/edit_image";
    }

    @RequestMapping(value = "image.htm", method = RequestMethod.GET)
    public void image(String num, Model model){
        model.addAttribute("num", num);

    }

    @RequestMapping(value = "image.htm", method = RequestMethod.POST)
    public String image(@RequestParam("file") MultipartFile file, int num, Model model) throws IOException {
        HttpClientUtils.HttpResp httpResp = cabinetService.uploadImage(file, num);
        if (httpResp.status / 100 != 2) {
            model.addAttribute("message", "上传静态服务器失败");
            model.addAttribute("success", false);
            return "/security/hdg/cabinet/image_response";
        }
        Map map = (Map) AppUtils.decodeJson(httpResp.content, Map.class);
        Map data = (Map) map.get("data");

        String imagePath = (String) data.get("filePath");

        model.addAttribute("success", true);
        model.addAttribute("filePath", imagePath);
        model.addAttribute("fileName", file.getOriginalFilename());
        model.addAttribute("num", num);
        return "/security/hdg/cabinet/image_response";
    }

    @RequestMapping("update_image.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateImage(Cabinet cabinet) throws IOException {
        return cabinetService.updateImage(cabinet);
    }

    @RequestMapping(value = "edit_new_location.htm")
    public String editLocation(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/edit_new_location";
    }

    @RequestMapping("update_location.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateLocation(Cabinet entity) {
        ExtResult result = cabinetService.updateLocation(entity);
        Cabinet cabinet = cabinetService.find(entity.getId());
        if (StringUtils.isNotEmpty(cabinet.getTerminalId())) {
            Terminal terminal = terminalService.find(cabinet.getTerminalId());
            if (terminal != null) {
                ClientBizUtils.noticeCabinetConfigUpdate(appConfig, terminal.getId(), cabinet.getAddress(), cabinet.getTel());
            }
        }

        return result;
    }

    @RequestMapping("update_new_location.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateNewLocation(Cabinet entity) {
        ExtResult result = cabinetService.updateNewLocation(entity);
        Cabinet cabinet = cabinetService.find(entity.getId());
        if (StringUtils.isNotEmpty(cabinet.getTerminalId())) {
            Terminal terminal = terminalService.find(cabinet.getTerminalId());
            if (terminal != null) {
                ClientBizUtils.noticeCabinetConfigUpdate(appConfig, terminal.getId(), cabinet.getAddress(), cabinet.getTel());
            }
        }
        return result;
    }

    /**
     * 配置信息
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "edit_property.htm")
    public String editProperty(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("list", cabinetPropertyService.findByCabinet(id));
        }
        return "/security/hdg/cabinet/edit_property";
    }

    @RequestMapping("update_property.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateProperty(String id, int[] active, String[] property, String[] value) {
        int total = cabinetPropertyService.insert(id, active, property, value);
        if (total == 0) {
            return ExtResult.failResult("修改失败！");
        }
        return ExtResult.successResult();
    }


    @RequestMapping(value = "view_new_location.htm")
    public String viewNewLocation(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/view_new_location";
    }

    /**
     * 配置信息
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "view_property.htm")
    public String viewProperty(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("list", cabinetPropertyService.findByCabinet(id));
        }
        return "/security/hdg/cabinet/view_property";
    }

    @RequestMapping(value = "view_image.htm")
    public String viewImage(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/view_image";
    }

    @RequestMapping(value = "view_ratio.htm")
    public String viewShop(Model model, String cabinetId, Integer agentId) {
        Cabinet entity = cabinetService.find(cabinetId);
        model.addAttribute("entity", entity);
        model.addAttribute("cabinetId", cabinetId);
        model.addAttribute("agentId", agentId);
        return "/security/hdg/cabinet/view_ratio";
    }

    @RequestMapping(value = "view_shop.htm")
    public String viewShop(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if (entity.getShopId() != null) {
            Shop shop = shopService.find(entity.getShopId());
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(shop.getWorkTime()) && !StringUtils.trim(shop.getWorkTime()).equals("-")) {
                String beginTime = shop.getWorkTime().substring(0, pattern.length());
                String endTime = (shop.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
            model.addAttribute("shop", shop);
        }
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/view_shop";
    }


    @RequestMapping(value = "view_battery_type.htm")
    public String viewBatteryType(Model model, String cabinetId, Integer agentId) {
        model.addAttribute("cabinetId", cabinetId);
        model.addAttribute("agentId", agentId);
        return "/security/hdg/cabinet/view_battery_type";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        Map map = new HashMap();
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("SubtypeEnum", Cabinet.Subtype.values());
            model.addAttribute("ViewTypeEnum", Cabinet.ViewType.values());
            model.addAttribute("entity", entity);
            model.addAttribute("activeStatusEnum", Cabinet.ActiveStatus.values());
            String pattern= Constant.HOUR_MINUTE;
            if (StringUtils.isNotEmpty(entity.getWorkTime()) && !StringUtils.trim(entity.getWorkTime()).equals("-")) {
                String beginTime = entity.getWorkTime().substring(0, pattern.length());
                String endTime = (entity.getWorkTime().substring(pattern.length() + 1));
                model.addAttribute("beginTime", beginTime);
                model.addAttribute("endTime", endTime);
            }
            Agent agent = agentService.find(entity.getAgentId());

            String url = systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.WEIXIN_URL.getValue());
            map.put("qrcode", String.format(ConstEnum.Qrcode.QRCODE_CABINET.getFormat(), url, entity.getId()));
            model.addAttribute("qrCodeAddress", map.get("qrcode"));
        }
        return "/security/hdg/cabinet/view_basic";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_cabinets.htm")
    public void selectPrice(Model model, Integer agentId) {
        if (agentId != null) {
            model.addAttribute("agentId", agentId);
        } else {
            model.addAttribute("agentId", null);
        }
    }

    @RequestMapping(value = "switch_cabinet.htm")
    public void switchCabinet(Model model) {
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_cabinet.htm")
    public void selectCabinet(String terminalId, Model model) {
        model.addAttribute("terminalId", terminalId);
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "select_new_cabinet.htm")
    public void selectNewCabinet(Model model, Integer agentId, String subcabinetId, String cabinetId) {
        model.addAttribute("agentId", agentId);
        model.addAttribute("subcabinetId", subcabinetId);
        model.addAttribute("cabinetId", cabinetId);
    }

    @RequestMapping("update_online.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateOnline(String id, Integer foregiftMoney, Integer rentMoney, Integer rentPeriodType, Date rentExpireTime, Integer activePlatformDeduct, Integer platformDeductMoney, Date platformDeductExpireTime) {
        return cabinetService.updateOnline(id, foregiftMoney, rentMoney, rentPeriodType, rentExpireTime, activePlatformDeduct, platformDeductMoney, platformDeductExpireTime);
    }

    @RequestMapping(value = "view_online.htm")
    public String viewOnline(Model model, String id) {
        Cabinet entity = cabinetService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/cabinet/view_online";
    }

}
