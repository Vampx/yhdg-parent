package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.ShopAddressCorrection;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopAddressCorrectionService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设备位置纠错
 */
@Controller
@RequestMapping(value = "/security/hdg/shop_address_correction")
public class ShopAddressCorrectionController extends SecurityController {

    @Autowired
    ShopAddressCorrectionService shopAddressCorrectionService;

//    @SecurityControl(limits = OperCodeConst.CODE_4_1_8_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_04_01_08.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ShopAddressCorrection search) {
        return PageResult.successResult(shopAddressCorrectionService.findPage(search));
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, Long id) {
        ShopAddressCorrection entity = shopAddressCorrectionService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
            model.addAttribute("status", ShopAddressCorrection.Status.getName(entity.getStatus()));
        }
        return "/security/hdg/shop_address_correction/view";
    }

    @RequestMapping(value = "edit.htm")
    public String edit(Model model, Long id){
        ShopAddressCorrection entity = shopAddressCorrectionService.find(id);
        if (entity == null){
            return SEGMENT_RECORD_NOT_FOUND;
        }else{
            model.addAttribute("entity", entity);
        }
        return "/security/hdg/shop_address_correction/edit";
    }

    @RequestMapping("update.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult updateStatus(Long id, int status) {
        ShopAddressCorrection entity = shopAddressCorrectionService.find(id);
        entity.setStatus(status);
        return shopAddressCorrectionService.updateStatus(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return shopAddressCorrectionService.delete(id);
    }

}
