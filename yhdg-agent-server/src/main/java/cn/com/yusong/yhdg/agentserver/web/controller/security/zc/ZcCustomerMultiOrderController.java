package cn.com.yusong.yhdg.agentserver.web.controller.security.zc;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerMultiOrderService;
import cn.com.yusong.yhdg.agentserver.service.basic.CustomerRefundRecordService;
import cn.com.yusong.yhdg.agentserver.service.zc.CustomerVehicleInfoService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerRefundRecord;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import com.alipay.api.AlipayApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/zc/zc_customer_multi_order")
public class ZcCustomerMultiOrderController extends SecurityController {

    @Autowired
    CustomerMultiOrderService customerMultiOrderService;
    @Autowired
    CustomerRefundRecordService customerRefundRecordService;
    @Autowired
    CustomerVehicleInfoService customerVehicleInfoService;

    @SecurityControl(limits = "zc.ZcCustomerMultiOrder:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zc.ZcCustomerMultiOrder:list");
        model.addAttribute("statusEnum", CustomerMultiOrder.Status.values());
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(CustomerMultiOrder customerMultiOrder) {
        return PageResult.successResult(customerMultiOrderService.findPage(customerMultiOrder));
    }

    @RequestMapping(value = "audit.htm")
    public String audit(Model model, Long id) {
        CustomerMultiOrder dbCustomerMultiOrder = customerMultiOrderService.find(id);
        if (dbCustomerMultiOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        CustomerRefundRecord customerRefundRecord = customerRefundRecordService.findOneByStatus(dbCustomerMultiOrder.getCustomerId(), CustomerRefundRecord.Status.APPLY.getValue(), CustomerRefundRecord.SourceType.ZCMULTI.getValue());
        if (customerRefundRecord == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }else {
            CustomerMultiOrder customerMultiOrder = customerMultiOrderService.find(Long.parseLong(customerRefundRecord.getSourceId()));
            if (customerMultiOrder != null && customerMultiOrder.getStatus() == CustomerMultiOrder.Status.APPLY_REFUND.getValue()) {
                customerMultiOrder.setRefundRecordId(customerRefundRecord.getId());
                customerMultiOrder.setSourceType(customerRefundRecord.getSourceType());
                customerMultiOrder.setRefundableMoney(customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney());
                customerMultiOrder.setPayOrderMoney(customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney());
                model.addAttribute("customerId", customerMultiOrder.getCustomerId());
                model.addAttribute("entity", customerMultiOrder);
                model.addAttribute("applyRefundTime", customerMultiOrder.getCreateTime());
            }
        }
        return "/security/zc/zc_customer_multi_order/audit";
    }

    @RequestMapping(value = "refund.htm")
    public String refund(Model model, Long id) {
        CustomerMultiOrder customerMultiOrder = customerMultiOrderService.find(id);
        if (customerMultiOrder == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        }
        customerMultiOrder.setSourceType(CustomerRefundRecord.SourceType.ZCMULTI.getValue());
        customerMultiOrder.setRefundableMoney(customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney());

        model.addAttribute("entity", customerMultiOrder);
        model.addAttribute("customerId", customerMultiOrder.getCustomerId());
        return "/security/zc/zc_customer_multi_order/refund";
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

        DataResult dataResult = customerMultiOrderService.refund(userName, refundType, sourceType, sourceId, refundMoney, refundRecordId, null, false);
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
        int sourceType = Integer.parseInt(auditMap.get("sourceType").toString());
        String sourceId = auditMap.get("sourceId").toString();
        Integer refundMoney = Integer.parseInt(auditMap.get("refundMoney").toString());

        ExtResult extResult = customerMultiOrderService.refund(userName, refundType, sourceType, sourceId, refundMoney, null, orderId, false);

        if(extResult.isSuccess()){
            message += String.format("%s%s退款成功;",CustomerRefundRecord.SourceType.getName(sourceType), sourceId);
        }else{
            message += String.format("%s%s退款失败(%s);",CustomerRefundRecord.SourceType.getName(sourceType), sourceId, extResult.getMessage());
        }
        return  ExtResult.successResult(message);
    }

    @RequestMapping(value = "view.htm")
    public String view(Model model, long id) {
        CustomerMultiOrder entity = customerMultiOrderService.find(id);
        if(entity == null) {
            return SEGMENT_RECORD_NOT_FOUND;
        } else {
            model.addAttribute("entity", entity);
        }
        model.addAttribute("id", id);
        return "/security/zc/zc_customer_multi_order/view";
    }

    @RequestMapping(value = "view_basic.htm")
    public String viewBasic(Model model, Long id) {
        CustomerMultiOrder customerMultiOrder = customerMultiOrderService.find(id);
        model.addAttribute("entity", customerMultiOrder);
        model.addAttribute("statusEnum", CustomerMultiOrder.Status.values());
        return "/security/zc/zc_customer_multi_order/view_basic";
    }

    @RequestMapping(value = "view_customer_multi_order_detail.htm")
    public String viewCustomerMultiOrderDetail(Model model, Long orderId) {
        model.addAttribute("orderId", orderId);
        return "/security/zc/zc_customer_multi_order/view_customer_multi_order_detail";
    }

    @RequestMapping(value = "view_customer_multi_pay_detail.htm")
    public String viewCustomerMultiPayDetail(Model model, Long orderId) {
        model.addAttribute("orderId", orderId);
        return "/security/zc/zc_customer_multi_order/view_customer_multi_pay_detail";
    }


}
