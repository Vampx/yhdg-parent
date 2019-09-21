//package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;
//
//import cn.com.yusong.yhdg.common.annotation.ViewModel;
//import cn.com.yusong.yhdg.common.entity.json.PageResult;
//import cn.com.yusong.yhdg.agentserver.service.hdg.VehicleOrderDetailService;
//import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//
///**
// * 租车订单明细详情
// */
//@Controller
//@RequestMapping(value = "/security/hdg/vehicle_order_detail")
//public class VehicleOrderDetailController extends SecurityController {
//    @Autowired
//    VehicleOrderDetailService batteryChargeRecordDetailService;
//
//    @RequestMapping(value = "view_list_order_detail.htm")
//    public void index(Model model, String orderId) {
//        model.addAttribute("orderId", orderId);
//    }
//
//    @RequestMapping("page.htm")
//    @ViewModel(ViewModel.JSON)
//    @ResponseBody
//    public PageResult page(VehicleOrderDetail search) {
//        return PageResult.successResult(batteryChargeRecordDetailService.findPage(search));
//    }
//}
