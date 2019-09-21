package cn.com.yusong.yhdg.webserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.AlipayPayOrder;
import cn.com.yusong.yhdg.common.domain.basic.WeixinPayOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrderRefund;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryUtilize;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.constant.AppConstEnum;
import cn.com.yusong.yhdg.webserver.constant.OperCodeConst;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryOrderRefundService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryOrderService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryUtilizeService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 电池利用率统计
 */
@Controller
@RequestMapping(value = "/security/hdg/battery_utilize")
public class BatteryUtilizeController extends SecurityController {

    @Autowired
    BatteryUtilizeService batteryUtilizeService;

//    @SecurityControl(limits = OperCodeConst.CODE_8_5_1_1)
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
//        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_08_05_01.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(BatteryUtilize search) {
        return PageResult.successResult(batteryUtilizeService.findPage(search));
    }

}
