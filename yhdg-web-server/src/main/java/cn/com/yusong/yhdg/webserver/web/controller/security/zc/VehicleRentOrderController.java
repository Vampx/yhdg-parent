package cn.com.yusong.yhdg.webserver.web.controller.security.zc;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.ShopStoreBattery;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.webserver.service.basic.AlipayPayOrderService;
import cn.com.yusong.yhdg.webserver.service.basic.WeixinPayOrderService;
import cn.com.yusong.yhdg.webserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.webserver.service.hdg.ShopStoreBatteryService;
import cn.com.yusong.yhdg.webserver.service.zd.RentOrderService;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/security/zc/vehicle_rent_order")
public class VehicleRentOrderController extends SecurityController {
    @Autowired
    private RentOrderService rentOrderService;
    @Autowired
    private AlipayPayOrderService alipayPayOrderService;
    @Autowired
    private WeixinPayOrderService weixinPayOrderService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    ShopStoreBatteryService shopStoreBatteryService;

    @SecurityControl(limits = "zc.VehicleRentOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model,Integer vehicleOrderFlag) {
        model.addAttribute(MENU_CODE_NAME, "zc.VehicleRentOrder:list");
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", RentOrder.Status.values());
        model.addAttribute("usedStatus", RentOrder.Status.RENT.getValue());
        model.addAttribute("APPLY_REFUND", RentOrder.Status.BACK.getValue());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(RentOrder search) {
        search.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());
        return PageResult.successResult(rentOrderService.findPage(search));
    }
    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        RentOrder entity = rentOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }

        return "/security/zc/vehicle_rent_order/view";
    }


    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, String id) {
        RentOrder entity = rentOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
        model.addAttribute("StatusEnum", RentOrder.Status.values());
        return "/security/zc/vehicle_rent_order/view_basic";
    }



    @ResponseBody
    @RequestMapping("updateStatus.htm")
    @ViewModel(ViewModel.JSON)
    public ExtResult updateStatusById(String id){
        return rentOrderService.updateStatusById(id);
    }

    @RequestMapping("exchange_battery.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult exchangeBattery(String id, String batteryId) {
        Battery newBattery = batteryService.find(batteryId);
        if(newBattery==null ){
            return ExtResult.failResult("电池不存在");
        }
        ShopStoreBattery newShopStoreBattery  = shopStoreBatteryService.findByBattery(newBattery.getId());
        if(newShopStoreBattery!=null){
            return ExtResult.failResult("新电池已绑定门店");
        }
        return rentOrderService.exchangeBattery(id, batteryId);
    }
}
