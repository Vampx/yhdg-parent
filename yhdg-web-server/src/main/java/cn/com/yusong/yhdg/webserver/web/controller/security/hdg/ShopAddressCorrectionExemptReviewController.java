package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.hdg.ShopAddressCorrectionExemptReview;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopAddressCorrectionExemptReviewService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 设备位置纠错免审人员
 */
@Controller
@RequestMapping(value = "/security/hdg/shop_address_correction_exempt_review")
public class ShopAddressCorrectionExemptReviewController extends SecurityController {

    @Autowired
    ShopAddressCorrectionExemptReviewService shopAddressCorrectionExemptReviewService;

//    @SecurityControl(limits = OperCodeConst.CODE_4_1_9_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_04_01_09.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(ShopAddressCorrectionExemptReview search) {
        return PageResult.successResult(shopAddressCorrectionExemptReviewService.findPage(search));
    }

    @RequestMapping(value = "add.htm")
    public void add(Model model) {

    }

    @RequestMapping("create.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult create(ShopAddressCorrectionExemptReview entity) {
        return  shopAddressCorrectionExemptReviewService.create(entity);
    }

    @RequestMapping("delete.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult delete(Long id) {
        return shopAddressCorrectionExemptReviewService.delete(id);
    }

}
