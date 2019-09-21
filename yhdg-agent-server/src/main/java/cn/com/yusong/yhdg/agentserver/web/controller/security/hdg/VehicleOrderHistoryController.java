//package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;
//
//import cn.com.yusong.yhdg.common.annotation.ViewModel;
//import cn.com.yusong.yhdg.common.constant.ConstEnum;
//import cn.com.yusong.yhdg.common.entity.json.PageResult;
//import cn.com.yusong.yhdg.agentserver.service.hdg.VehicleOrderHistoryService;
//import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.servlet.http.HttpServletResponse;
//
///**
// * 换电订单
// */
//@Controller
//@RequestMapping(value = "/security/hdg/vehicle_order_history")
//public class VehicleOrderHistoryController extends SecurityController {
//
//    @Autowired
//    VehicleOrderHistoryService vehicleOrderHistoryService;
//
//    @ResponseBody
//    @ViewModel(ViewModel.JSON_ARRAY)
//    @RequestMapping(value = "tree.htm")
//    public void tree(HttpServletResponse response) throws Exception {
//        response.setContentType(ConstEnum.ContentType.JSON.getValue());
//        vehicleOrderHistoryService.tree(response.getOutputStream());
//    }
//
////    @SecurityControl(limits = OperCodeConst.CODE_2_4_6_1)
//    @RequestMapping(value = "index.htm")
//    public void index(Model model) {
////        model.addAttribute(MENU_CODE_NAME, AppConstEnum.Menu.MENU_02_04_05.getValue());
//        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
//    }
//
//    @RequestMapping(value = "advanced_query.htm")
//    public void advancedQuery(Model model) {
//        model.addAttribute("StatusEnum", VehicleOrder.Status.values());
//        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
//    }
//
//    @RequestMapping("page.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public PageResult page(VehicleOrder search) {
//        return PageResult.successResult(vehicleOrderHistoryService.findPage(search));
//    }
//
//    @RequestMapping(value = "view.htm")
//    public String view(Model model, String id,String suffix) {
//        VehicleOrder entity = vehicleOrderHistoryService.find(id,suffix);
//        if (entity == null) {
//            return SEGMENT_RECORD_NOT_FOUND;
//        } else {
//            model.addAttribute("entity", entity);
//        }
//        model.addAttribute("id", id);
//        model.addAttribute("suffix", suffix);
//
//        return "/security/hdg/vehicle_order_history/view";
//    }
//    @RequestMapping(value = "view_basic.htm")
//    public String viewBasic(Model model, String id,String suffix) {
//        VehicleOrder entity = vehicleOrderHistoryService.find(id,suffix);
//        if (entity == null) {
//            return SEGMENT_RECORD_NOT_FOUND;
//        } else {
//            model.addAttribute("entity", entity);
//        }
//        model.addAttribute("PayTypeEnum", ConstEnum.PayType.values());
//        model.addAttribute("StatusEnum", VehicleOrder.Status.values());
//        return "/security/hdg/vehicle_order_history/view_basic";
//    }
//
//}
