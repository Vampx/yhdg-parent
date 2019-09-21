//package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;
//
//import cn.com.yusong.yhdg.common.annotation.ViewModel;
//import cn.com.yusong.yhdg.common.constant.ConstEnum;
//import cn.com.yusong.yhdg.common.entity.json.PageResult;
//import cn.com.yusong.yhdg.agentserver.service.hdg.VehicleOrderService;
//import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
///**
// * 门店租车订单
// */
//@Controller
//@RequestMapping(value = "/security/hdg/shop_vehicle_order")
//public class ShopVehicleOrderController extends SecurityController {
//    @Autowired
//    VehicleOrderService rentVehicleOrderService;
//
//
////    @SecurityControl(limits = OperCodeConst.CODE_4_1_5_1)
//    @RequestMapping(value = "index.htm")
//    public void index(Model model) {
////        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_04_01_05.getValue());
//        model.addAttribute("StatusEnum", VehicleOrder.Status.values());
//        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
//    }
//
//    @RequestMapping("page.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public PageResult page(VehicleOrder search) {
//        search.setQueryFlag(ConstEnum.Flag.TRUE.getValue());
//        return PageResult.successResult(rentVehicleOrderService.findPage(search));
//    }
//
//    /**
//     * 根据车辆id查询查询租车信息
//     * @param model
//     * @param vehicleId
//     */
//    @ViewModel(ViewModel.INNER_PAGE)
//    @RequestMapping(value = "select_vehicle_order_page.htm")
//    public void selectVehicleOrderPage(Model model, Long vehicleId) {
//        model.addAttribute("vehicleId", vehicleId);
//    }
//
//    @RequestMapping(value = "view.htm")
//    public String view(Model model, String id) {
//        VehicleOrder entity = rentVehicleOrderService.find(id);
//        if(entity == null) {
//            return SEGMENT_RECORD_NOT_FOUND;
//        } else {
//            model.addAttribute("entity", entity);
//        }
//        model.addAttribute("StatusEnum", VehicleOrder.Status.values());
//        return "/security/hdg/shop_vehicle_order/view";
//    }
//
//}
