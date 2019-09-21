package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;


import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerForegiftOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerRefundRecordService;
import cn.com.yusong.yhdg.agentserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.agentserver.service.zc.CustomerVehicleInfoService;
import cn.com.yusong.yhdg.agentserver.service.zc.GroupOrderService;
import cn.com.yusong.yhdg.agentserver.service.zc.VehicleForegiftOrderService;
import cn.com.yusong.yhdg.agentserver.service.zc.VehiclePeriodOrderService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.agentserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerRefundRecord;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import com.alipay.api.AlipayApiException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/zc/group_order")
public class GroupOrderController extends SecurityController {

    @Autowired
    GroupOrderService groupOrderService;
    @Autowired
    VehicleForegiftOrderService vehicleForegiftOrderService;
    @Autowired
    VehiclePeriodOrderService vehiclePeriodOrderService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    CustomerRefundRecordService customerRefundRecordService;
    @Autowired
    CustomerVehicleInfoService customerVehicleInfoService;

    @SecurityControl(limits = "zc.GroupOrder:list")
    @RequestMapping(value = "index.htm")

    public void index(Model model) {
        model.addAttribute("statusList", GroupOrder.Status.values());
        model.addAttribute(MENU_CODE_NAME, "zc.GroupOrder:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(GroupOrder search) {
        return PageResult.successResult(groupOrderService.findPage(search));
    }


    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "edit_money.htm")
    public void editMoney(Model model, Integer refundableMoney, Integer realRefundMoney) {
        model.addAttribute("refundableMoney", refundableMoney);
        model.addAttribute("realRefundMoney", realRefundMoney);
    }

    @RequestMapping(value = "audit.htm")
    public String audit(Model model, String id) {
        GroupOrder dbGroupOrder = groupOrderService.find(id);
        if (dbGroupOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        String groupOrderId = null;
        GroupOrder groupOrder = null;
        List<CustomerRefundRecord> customerRefundRecordList = customerRefundRecordService.findByCustomerId(dbGroupOrder.getCustomerId(), CustomerRefundRecord.Status.APPLY.getValue());
        for (CustomerRefundRecord customerRefundRecord : customerRefundRecordList) {
            if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.ZCGROUP.getValue()) {
                model.addAttribute("refundRecordId", customerRefundRecord.getId());
                model.addAttribute("sourceType", customerRefundRecord.getSourceType());
                model.addAttribute("sourceId", customerRefundRecord.getSourceId());
                groupOrderId = customerRefundRecord.getSourceId();
            }
        }
        if (StringUtils.isNotEmpty(groupOrderId)) {
            groupOrder = groupOrderService.find(groupOrderId);
        }
        if (groupOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }

        //押金可退金额
        int foregiftRefundableMoney = 0;
        //押金支付金额
        int foregiftPayMoney = 0;
        //租金可退金额
        int packetRefundableMoney = 0;
        //租金支付金额
        int packetPayMoney = 0;
        //总可退金额
        int refundableMoney = 0;
        if (StringUtils.isNotEmpty(groupOrder.getVehicleForegiftId())) {
            VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderService.find(groupOrder.getVehicleForegiftId());
            if (vehicleForegiftOrder != null && vehicleForegiftOrder.getStatus() == VehicleForegiftOrder.Status.APPLY_REFUND.getValue()) {
                foregiftRefundableMoney += groupOrder.getVehicleForegiftMoney();
                refundableMoney += groupOrder.getVehicleForegiftMoney();
                foregiftPayMoney += groupOrder.getVehicleForegiftMoney();
                model.addAttribute("vehicleForegiftOrder", vehicleForegiftOrder);
            }
        }
        if (StringUtils.isNotEmpty(groupOrder.getBatteryForegiftId())) {
            if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderService.find(groupOrder.getBatteryForegiftId());
                if (customerForegiftOrder != null && customerForegiftOrder.getStatus() == CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) {
                    foregiftRefundableMoney += groupOrder.getBatteryForegiftMoney();
                    refundableMoney += groupOrder.getBatteryForegiftMoney();
                    foregiftPayMoney += groupOrder.getBatteryForegiftMoney();
                    model.addAttribute("batteryForegiftOrder", customerForegiftOrder);
                }
            } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.find(groupOrder.getBatteryForegiftId());
                if (rentForegiftOrder != null && rentForegiftOrder.getStatus() == RentForegiftOrder.Status.APPLY_REFUND.getValue()) {
                    foregiftRefundableMoney += groupOrder.getBatteryForegiftMoney();
                    refundableMoney += groupOrder.getBatteryForegiftMoney();
                    foregiftPayMoney += groupOrder.getBatteryForegiftMoney();
                    model.addAttribute("batteryForegiftOrder", rentForegiftOrder);
                }
            }
        }
        foregiftRefundableMoney += (groupOrder.getDeductionTicketMoney() == null ? 0 : groupOrder.getDeductionTicketMoney());
        refundableMoney += (groupOrder.getDeductionTicketMoney() == null ? 0 : groupOrder.getDeductionTicketMoney());

        if (StringUtils.isNotEmpty(groupOrder.getVehiclePeriodId())) {
            VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderService.find(groupOrder.getVehiclePeriodId());
            if (vehiclePeriodOrder != null && vehiclePeriodOrder.getStatus() == VehiclePeriodOrder.Status.APPLY_REFUND.getValue()) {
                packetRefundableMoney += groupOrder.getVehiclePeriodMoney();
                refundableMoney += groupOrder.getVehiclePeriodMoney();
                packetPayMoney += groupOrder.getVehiclePeriodMoney();
                model.addAttribute("vehiclePeriodOrder", vehiclePeriodOrder);
            }
        }
        if (StringUtils.isNotEmpty(groupOrder.getBatteryRentId())) {
            if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.find(groupOrder.getBatteryRentId());
                if (packetPeriodOrder != null && packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.APPLY_REFUND.getValue()) {
                    packetRefundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    refundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    packetPayMoney += groupOrder.getBatteryRentPeriodMoney();
                    model.addAttribute("batteryPeriodOrder", packetPeriodOrder);
                }
            } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.find(groupOrder.getBatteryRentId());
                if (rentPeriodOrder != null && rentPeriodOrder.getStatus() == RentPeriodOrder.Status.APPLY_REFUND.getValue()) {
                    packetRefundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    refundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    packetPayMoney += groupOrder.getBatteryRentPeriodMoney();
                    model.addAttribute("batteryPeriodOrder", rentPeriodOrder);
                }
            }
        }
        int vehicleForegiftPrice = groupOrder.getVehicleForegiftPrice() == null ? 0 : groupOrder.getVehicleForegiftPrice();
        int vehiclePeriodPrice = groupOrder.getVehiclePeriodPrice() == null ? 0 : groupOrder.getVehiclePeriodPrice();
        int batteryForegiftPrice = groupOrder.getBatteryForegiftPrice() == null ? 0 : groupOrder.getBatteryForegiftPrice();
        int batteryPeriodPrice = groupOrder.getBatteryRentPeriodPrice() == null ? 0 : groupOrder.getBatteryRentPeriodPrice();
        model.addAttribute("refundableMoney", refundableMoney);
        model.addAttribute("foregiftRefundableMoney", foregiftRefundableMoney);
        model.addAttribute("packetRefundableMoney", packetRefundableMoney);
        model.addAttribute("foregiftPayMoney", foregiftPayMoney);
        model.addAttribute("packetPayMoney", packetPayMoney);
        model.addAttribute("foregiftPrice", vehicleForegiftPrice + batteryForegiftPrice);
        model.addAttribute("periodPrice", vehiclePeriodPrice + batteryPeriodPrice);
        model.addAttribute("entity", groupOrder);
        return "/security/zc/group_order/audit";
    }

    @RequestMapping(value = "refund.htm")
    public String refund(Model model, String id) {
        GroupOrder groupOrder = groupOrderService.find(id);
        if (groupOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        //押金可退金额
        int foregiftRefundableMoney = 0;
        //押金支付金额
        int foregiftPayMoney = 0;
        //租金可退金额
        int packetRefundableMoney = 0;
        //租金支付金额
        int packetPayMoney = 0;
        //总可退金额
        int refundableMoney = 0;
        if (StringUtils.isNotEmpty(groupOrder.getVehicleForegiftId())) {
            VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderService.find(groupOrder.getVehicleForegiftId());
            if (vehicleForegiftOrder != null && (vehicleForegiftOrder.getStatus() == VehicleForegiftOrder.Status.PAY_OK.getValue() || vehicleForegiftOrder.getStatus() == VehicleForegiftOrder.Status.APPLY_REFUND.getValue())) {
                foregiftRefundableMoney += groupOrder.getVehicleForegiftMoney();
                refundableMoney += groupOrder.getVehicleForegiftMoney();
                foregiftPayMoney += groupOrder.getVehicleForegiftMoney();
            }
        }
        if (StringUtils.isNotEmpty(groupOrder.getBatteryForegiftId())) {
            if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderService.find(groupOrder.getBatteryForegiftId());
                if (customerForegiftOrder != null && (customerForegiftOrder.getStatus() == CustomerForegiftOrder.Status.PAY_OK.getValue() || customerForegiftOrder.getStatus() == CustomerForegiftOrder.Status.APPLY_REFUND.getValue())) {
                    foregiftRefundableMoney += groupOrder.getBatteryForegiftMoney();
                    refundableMoney += groupOrder.getBatteryForegiftMoney();
                    foregiftPayMoney += groupOrder.getBatteryForegiftMoney();
                }
            } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.find(groupOrder.getBatteryForegiftId());
                if (rentForegiftOrder != null && (rentForegiftOrder.getStatus() == RentForegiftOrder.Status.PAY_OK.getValue() || rentForegiftOrder.getStatus() == RentForegiftOrder.Status.APPLY_REFUND.getValue())) {
                    foregiftRefundableMoney += groupOrder.getBatteryForegiftMoney();
                    refundableMoney += groupOrder.getBatteryForegiftMoney();
                    foregiftPayMoney += groupOrder.getBatteryForegiftMoney();
                }
            }
        }
        foregiftRefundableMoney += (groupOrder.getDeductionTicketMoney() == null ? 0 : groupOrder.getDeductionTicketMoney());
        refundableMoney += (groupOrder.getDeductionTicketMoney() == null ? 0 : groupOrder.getDeductionTicketMoney());

        if (StringUtils.isNotEmpty(groupOrder.getVehiclePeriodId())) {
            VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderService.find(groupOrder.getVehiclePeriodId());
            if (vehiclePeriodOrder != null && (vehiclePeriodOrder.getStatus() == VehiclePeriodOrder.Status.NOT_USE.getValue() || vehiclePeriodOrder.getStatus() == VehiclePeriodOrder.Status.USED.getValue() || vehiclePeriodOrder.getStatus() == VehiclePeriodOrder.Status.APPLY_REFUND.getValue())) {
                packetRefundableMoney += groupOrder.getVehiclePeriodMoney();
                refundableMoney += groupOrder.getVehiclePeriodMoney();
                packetPayMoney += groupOrder.getVehiclePeriodMoney();
            }
        }
        if (StringUtils.isNotEmpty(groupOrder.getBatteryRentId())) {
            if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.find(groupOrder.getBatteryRentId());
                if (packetPeriodOrder != null && (packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.NOT_USE.getValue() || packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.USED.getValue() || packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.APPLY_REFUND.getValue())) {
                    packetRefundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    refundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    packetPayMoney += groupOrder.getBatteryRentPeriodMoney();
                }
            } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.find(groupOrder.getBatteryRentId());
                if (rentPeriodOrder != null && (rentPeriodOrder.getStatus() == RentPeriodOrder.Status.NOT_USE.getValue() || rentPeriodOrder.getStatus() == RentPeriodOrder.Status.USED.getValue() || rentPeriodOrder.getStatus() == RentPeriodOrder.Status.APPLY_REFUND.getValue())) {
                    packetRefundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    refundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    packetPayMoney += groupOrder.getBatteryRentPeriodMoney();
                }
            }
        }
        int vehicleForegiftPrice = groupOrder.getVehicleForegiftPrice() == null ? 0 : groupOrder.getVehicleForegiftPrice();
        int vehiclePeriodPrice = groupOrder.getVehiclePeriodPrice() == null ? 0 : groupOrder.getVehiclePeriodPrice();
        int batteryForegiftPrice = groupOrder.getBatteryForegiftPrice() == null ? 0 : groupOrder.getBatteryForegiftPrice();
        int batteryPeriodPrice = groupOrder.getBatteryRentPeriodPrice() == null ? 0 : groupOrder.getBatteryRentPeriodPrice();
        model.addAttribute("refundableMoney", refundableMoney);
        model.addAttribute("foregiftRefundableMoney", foregiftRefundableMoney);
        model.addAttribute("packetRefundableMoney", packetRefundableMoney);
        model.addAttribute("foregiftPayMoney", foregiftPayMoney);
        model.addAttribute("packetPayMoney", packetPayMoney);
        model.addAttribute("foregiftPrice", vehicleForegiftPrice + batteryForegiftPrice);
        model.addAttribute("periodPrice", vehiclePeriodPrice + batteryPeriodPrice);
        model.addAttribute("entity", groupOrder);
        return "/security/zc/group_order/refund";
    }

    @RequestMapping("audit_money.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult auditMoney(HttpSession httpSession, String data) throws AlipayApiException, IOException {
        SessionUser sessionUser = getSessionUser(httpSession);
        String userName = sessionUser.getUsername();
        String message = "";
        Map auditMap = (Map) YhdgUtils.decodeJson(data,Map.class);
        if(auditMap == null){
            return ExtResult.failResult("传参为空！");
        }

        if(auditMap.get("refundType") == null){
            return ExtResult.failResult("退款方式不能为空！");
        }
        int refundType = Integer.parseInt(auditMap.get("refundType").toString());

        Long refundRecordId = Long.parseLong(auditMap.get("refundRecordId").toString());
        Integer sourceType = Integer.parseInt(auditMap.get("sourceType").toString());
        String sourceId = auditMap.get("sourceId").toString();
        Integer refundMoney = Integer.parseInt(auditMap.get("refundMoney").toString());
        Integer refundableMoney = Integer.parseInt(auditMap.get("refundableMoney").toString());

        DataResult dataResult = groupOrderService.refund(userName, refundType, sourceType, sourceId, refundMoney, refundableMoney, refundRecordId, null, false);
        String ptPayOrderId = null;
        if (dataResult.getData() != null) {
            Map m = (Map) dataResult.getData();
            Object objId = m.get("ptPayOrderId");
            if (objId != null) {
                ptPayOrderId = (String) objId;
            }
        }
        if (dataResult.isSuccess()) {
            customerRefundRecordService.updateZcStatus(refundRecordId, refundType, ptPayOrderId,
                    CustomerRefundRecord.Status.FINISH.getValue(),
                    refundMoney, new Date());

            message += String.format("%s%s退款成功;", CustomerRefundRecord.SourceType.getName(sourceType), sourceId);
        } else {
            customerRefundRecordService.updateZcStatus(refundRecordId, refundType, ptPayOrderId,
                    CustomerRefundRecord.Status.FAIL.getValue(),
                    refundMoney, new Date());


            message += String.format("%s%s退款失败(%s);", CustomerRefundRecord.SourceType.getName(sourceType), sourceId, dataResult.getMessage());
        }
        return  ExtResult.successResult(message);
    }

    @RequestMapping("refund_money.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult refundMoney(HttpSession httpSession, String data) throws AlipayApiException, IOException {
        SessionUser sessionUser = getSessionUser(httpSession);
        String userName = sessionUser.getUsername();
        String message = "";
        Map auditMap = (Map) YhdgUtils.decodeJson(data,Map.class);
        if(auditMap == null){
            return ExtResult.failResult("传参为空！");
        }

        if(auditMap.get("customerId") == null){
            return ExtResult.failResult("用户不能空！");
        }
        Long customerId = Long.parseLong(auditMap.get("customerId").toString());

        //判断用户是否存在审核中订单，存在不让进行退款操作
        if(customerRefundRecordService.existZcRefund(customerId) > 0){
            return ExtResult.failResult("用户存在待审核中订单，不允许退款操作！");
        }
        if(auditMap.get("refundType") == null){
            return ExtResult.failResult("退款方式不能为空！");
        }
        String orderId = customerRefundRecordService.newOrderId();

        int refundType = Integer.parseInt(auditMap.get("refundType").toString());
        Integer sourceType = CustomerRefundRecord.SourceType.ZCGROUP.getValue();
        String sourceId = auditMap.get("sourceId").toString();
        Integer refundMoney = Integer.parseInt(auditMap.get("refundMoney").toString());
        Integer refundableMoney = Integer.parseInt(auditMap.get("refundableMoney").toString());

        ExtResult extResult = groupOrderService.refund(userName, refundType, sourceType, sourceId, refundMoney, refundableMoney, null, orderId, false);

        if(extResult.isSuccess()){
            message += String.format("%s%s退款成功;",CustomerRefundRecord.SourceType.getName(sourceType), sourceId);
        }else{
            message += String.format("%s%s退款失败(%s);",CustomerRefundRecord.SourceType.getName(sourceType), sourceId, extResult.getMessage());
        }
        return  ExtResult.successResult(message);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, String id) {
        GroupOrder entity = groupOrderService.find(id);
        if (entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        return "/security/zc/group_order/view";
    }

}
