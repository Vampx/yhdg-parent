package cn.com.yusong.yhdg.webserver.web.controller.security.zd;

import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.webserver.entity.SessionUser;
import cn.com.yusong.yhdg.webserver.service.basic.*;
import cn.com.yusong.yhdg.webserver.service.zd.*;
import cn.com.yusong.yhdg.webserver.web.controller.security.basic.SecurityController;
import com.alipay.api.AlipayApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/security/zd/rent_refund")
public class RentRefundController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(RentRefundController.class);

    @Autowired
    RentRefundService rentRefundService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    CustomerRefundRecordService customerRefundRecordService;
    @Autowired
    CustomerRentBatteryService customerRentBatteryService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    RentInsuranceOrderService rentInsuranceOrderService;
    @Autowired
    AlipayPayOrderService alipayPayOrderService;
    @Autowired
    WeixinPayOrderService weixinPayOrderService;
    @Autowired
    WeixinmpPayOrderService weixinmpPayOrderService;
    @Autowired
    AlipayfwPayOrderService alipayfwPayOrderService;
    @Autowired
    CustomerMultiPayDetailService customerMultiPayDetailService;
    @Autowired
    CustomerMultiOrderService customerMultiOrderService;

    @SecurityControl(limits = "zd.RentRefund:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "zd.RentRefund:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Customer search) {
        return PageResult.successResult(rentRefundService.findPage(search));
    }

    @RequestMapping(value = "audit.htm")
    public String audit(Model model, Long id) {
        List<RentForegiftOrder> rentForegiftOrderList = new ArrayList<RentForegiftOrder>();
        List<RentPeriodOrder> rentPeriodOrderList = new ArrayList<RentPeriodOrder>();
        List<RentInsuranceOrder> rentInsuranceOrderList = new ArrayList<RentInsuranceOrder>();
        List<CustomerMultiOrder> customerMultiOrderList = new ArrayList<CustomerMultiOrder>();

        List<CustomerRefundRecord> customerRefundRecordList = customerRefundRecordService.findByCustomerId(id, CustomerRefundRecord.Status.APPLY.getValue());
        if (customerRefundRecordList != null && customerRefundRecordList.size() > 0) {
            model.addAttribute("applyRefundTime", customerRefundRecordList.get(0).getCreateTime());
        }
        for (CustomerRefundRecord customerRefundRecord : customerRefundRecordList) {
            if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue()) {
                RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.find(customerRefundRecord.getSourceId());
                if (rentForegiftOrder != null && rentForegiftOrder.getStatus() == RentForegiftOrder.Status.APPLY_REFUND.getValue() && rentForegiftOrder.getPayType() != ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                    rentForegiftOrder.setRefundableMoney(rentForegiftOrder.getMoney() + (rentForegiftOrder.getDeductionTicketMoney() == null ? 0 : rentForegiftOrder.getDeductionTicketMoney()));
                    rentForegiftOrder.setRefundRecordId(customerRefundRecord.getId());
                    rentForegiftOrder.setSourceType(customerRefundRecord.getSourceType());
                    rentForegiftOrder.setRefundMoney(customerRefundRecord.getRefundMoney());
                    if (rentForegiftOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                        AlipayPayOrder alipayPayOrder = alipayPayOrderService.findBySourceId(rentForegiftOrder.getId());
                        rentForegiftOrder.setPayOrderId(alipayPayOrder.getId());
                        rentForegiftOrder.setHandleTime(alipayPayOrder.getHandleTime());
                        rentForegiftOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                        rentForegiftOrder.setMemo(alipayPayOrder.getMemo());
                    } else if (rentForegiftOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                        WeixinPayOrder weixinPayOrder = weixinPayOrderService.findBySourceId(rentForegiftOrder.getId());
                        rentForegiftOrder.setPayOrderId(weixinPayOrder.getId());
                        rentForegiftOrder.setHandleTime(weixinPayOrder.getHandleTime());
                        rentForegiftOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                        rentForegiftOrder.setMemo(weixinPayOrder.getMemo());
                    } else if (rentForegiftOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                        WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(rentForegiftOrder.getId());
                        rentForegiftOrder.setPayOrderId(weixinmpPayOrder.getId());
                        rentForegiftOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                        rentForegiftOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                        rentForegiftOrder.setMemo(weixinmpPayOrder.getMemo());
                    } else if (rentForegiftOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                        AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(rentForegiftOrder.getId());
                        rentForegiftOrder.setPayOrderId(alipayfwPayOrder.getId());
                        rentForegiftOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                        rentForegiftOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                        rentForegiftOrder.setMemo(alipayfwPayOrder.getMemo());
                    } else {
                        rentForegiftOrder.setHandleTime(rentForegiftOrder.getPayTime());
                        rentForegiftOrder.setPayOrderMoney(rentForegiftOrder.getMoney());
                    }
                    rentForegiftOrderList.add(rentForegiftOrder);
                }
            } else if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue()) {
                RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.find(customerRefundRecord.getSourceId());
                if (rentPeriodOrder != null && rentPeriodOrder.getStatus() == RentPeriodOrder.Status.APPLY_REFUND.getValue() && rentPeriodOrder.getPayType() != ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                    rentPeriodOrder.setRefundRecordId(customerRefundRecord.getId());
                    rentPeriodOrder.setSourceType(customerRefundRecord.getSourceType());
                    rentPeriodOrder.setRefundableMoney(rentPeriodOrder.getMoney());
                    if (rentPeriodOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                        AlipayPayOrder alipayPayOrder = alipayPayOrderService.findBySourceId(rentPeriodOrder.getId());
                        rentPeriodOrder.setPayOrderId(alipayPayOrder.getId());
                        rentPeriodOrder.setHandleTime(alipayPayOrder.getHandleTime());
                        rentPeriodOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                        rentPeriodOrder.setMemo(alipayPayOrder.getMemo());
                    } else if (rentPeriodOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                        WeixinPayOrder weixinPayOrder = weixinPayOrderService.findBySourceId(rentPeriodOrder.getId());
                        rentPeriodOrder.setPayOrderId(weixinPayOrder.getId());
                        rentPeriodOrder.setHandleTime(weixinPayOrder.getHandleTime());
                        rentPeriodOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                        rentPeriodOrder.setMemo(weixinPayOrder.getMemo());
                    } else if (rentPeriodOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                        WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(rentPeriodOrder.getId());
                        rentPeriodOrder.setPayOrderId(weixinmpPayOrder.getId());
                        rentPeriodOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                        rentPeriodOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                        rentPeriodOrder.setMemo(weixinmpPayOrder.getMemo());
                    } else if (rentPeriodOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                        AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(rentPeriodOrder.getId());
                        rentPeriodOrder.setPayOrderId(alipayfwPayOrder.getId());
                        rentPeriodOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                        rentPeriodOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                        rentPeriodOrder.setMemo(alipayfwPayOrder.getMemo());
                    } else {
                        rentPeriodOrder.setHandleTime(rentPeriodOrder.getPayTime());
                        rentPeriodOrder.setPayOrderMoney(rentPeriodOrder.getMoney());
                    }
                    rentPeriodOrderList.add(rentPeriodOrder);
                }
            } else if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.ZDINSURANCE.getValue()) {
                RentInsuranceOrder rentInsuranceOrder = rentInsuranceOrderService.find(customerRefundRecord.getSourceId());
                if (rentInsuranceOrder != null && rentInsuranceOrder.getStatus() == RentInsuranceOrder.Status.APPLY_REFUND.getValue()) {
                    rentInsuranceOrder.setRefundRecordId(customerRefundRecord.getId());
                    rentInsuranceOrder.setSourceType(customerRefundRecord.getSourceType());
                    rentInsuranceOrder.setRefundableMoney(rentInsuranceOrder.getMoney());
                    if (rentInsuranceOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                        AlipayPayOrder alipayPayOrder = alipayPayOrderService.findBySourceId(rentInsuranceOrder.getId());
                        rentInsuranceOrder.setPayOrderId(alipayPayOrder.getId());
                        rentInsuranceOrder.setHandleTime(alipayPayOrder.getHandleTime());
                        rentInsuranceOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                        rentInsuranceOrder.setMemo(alipayPayOrder.getMemo());
                    } else if (rentInsuranceOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                        WeixinPayOrder weixinPayOrder = weixinPayOrderService.findBySourceId(rentInsuranceOrder.getId());
                        rentInsuranceOrder.setPayOrderId(weixinPayOrder.getId());
                        rentInsuranceOrder.setHandleTime(weixinPayOrder.getHandleTime());
                        rentInsuranceOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                        rentInsuranceOrder.setMemo(weixinPayOrder.getMemo());
                    } else if (rentInsuranceOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                        WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(rentInsuranceOrder.getId());
                        rentInsuranceOrder.setPayOrderId(weixinmpPayOrder.getId());
                        rentInsuranceOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                        rentInsuranceOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                        rentInsuranceOrder.setMemo(weixinmpPayOrder.getMemo());
                    } else if (rentInsuranceOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                        AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(rentInsuranceOrder.getId());
                        rentInsuranceOrder.setPayOrderId(alipayfwPayOrder.getId());
                        rentInsuranceOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                        rentInsuranceOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                        rentInsuranceOrder.setMemo(alipayfwPayOrder.getMemo());
                    } else {
                        rentInsuranceOrder.setHandleTime(rentInsuranceOrder.getPayTime());
                        rentInsuranceOrder.setPayOrderMoney(rentInsuranceOrder.getMoney());
                    }
                    rentInsuranceOrderList.add(rentInsuranceOrder);
                }
            } else if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.ZDMULTI.getValue()) {
                CustomerMultiOrder customerMultiOrder = customerMultiOrderService.find(Long.parseLong(customerRefundRecord.getSourceId()));
                if (customerMultiOrder != null && customerMultiOrder.getStatus() == CustomerMultiOrder.Status.APPLY_REFUND.getValue()) {
                    customerMultiOrder.setRefundRecordId(customerRefundRecord.getId());
                    customerMultiOrder.setSourceType(customerRefundRecord.getSourceType());
                    customerMultiOrder.setRefundableMoney(customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney());
                    customerMultiOrder.setPayOrderMoney(customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney());
                    customerMultiOrderList.add(customerMultiOrder);
                }
            }
        }
        model.addAttribute("customerForegiftOrderList", rentForegiftOrderList);
        model.addAttribute("packetPeriodOrderList", rentPeriodOrderList);
        model.addAttribute("insuranceOrderList", rentInsuranceOrderList);
        model.addAttribute("customerMultiOrderList", customerMultiOrderList);
        model.addAttribute("customerId", id);
        return "/security/zd/rent_refund/audit";
    }


    @RequestMapping(value = "refund.htm")
    public String refund(Model model, Long id) {
        List<RentForegiftOrder> rentForegiftOrderList = rentForegiftOrderService.findCanRefundByCustomerId(id);
        List<RentPeriodOrder> rentPeriodOrderList = rentPeriodOrderService.findCanRefundByCustomerId(id);
        List<RentInsuranceOrder> rentInsuranceOrderList = rentInsuranceOrderService.findCanRefundByCustomerId(id);
        List<CustomerMultiOrder> customerMultiOrderList = customerMultiOrderService.findCanRefund(id, CustomerMultiOrder.Type.ZD.getValue(), CustomerMultiOrder.Status.IN_PAY.getValue());

        List<RentForegiftOrder> rentForegiftOrders = new ArrayList<RentForegiftOrder>();
        for(RentForegiftOrder rentForegiftOrder: rentForegiftOrderList){
            rentForegiftOrder.setRefundableMoney(rentForegiftOrder.getMoney() + (rentForegiftOrder.getDeductionTicketMoney() == null ? 0 : rentForegiftOrder.getDeductionTicketMoney()));
            rentForegiftOrder.setSourceType(CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue());
            if (rentForegiftOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                AlipayPayOrder alipayPayOrder;
                alipayPayOrder = alipayPayOrderService.findBySourceId(rentForegiftOrder.getId());
                rentForegiftOrder.setPayOrderId(alipayPayOrder.getId());
                rentForegiftOrder.setHandleTime(alipayPayOrder.getHandleTime());
                rentForegiftOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                rentForegiftOrder.setMemo(alipayPayOrder.getMemo());
            } else if (rentForegiftOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                WeixinPayOrder weixinPayOrder;
                weixinPayOrder = weixinPayOrderService.findBySourceId(rentForegiftOrder.getId());
                rentForegiftOrder.setPayOrderId(weixinPayOrder.getId());
                rentForegiftOrder.setHandleTime(weixinPayOrder.getHandleTime());
                rentForegiftOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                rentForegiftOrder.setMemo(weixinPayOrder.getMemo());
            } else if (rentForegiftOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                WeixinmpPayOrder weixinmpPayOrder;
                weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(rentForegiftOrder.getId());
                rentForegiftOrder.setPayOrderId(weixinmpPayOrder.getId());
                rentForegiftOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                rentForegiftOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                rentForegiftOrder.setMemo(weixinmpPayOrder.getMemo());
            } else if (rentForegiftOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                AlipayfwPayOrder alipayfwPayOrder;
                alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(rentForegiftOrder.getId());
                rentForegiftOrder.setPayOrderId(alipayfwPayOrder.getId());
                rentForegiftOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                rentForegiftOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                rentForegiftOrder.setMemo(alipayfwPayOrder.getMemo());
            } else {
                rentForegiftOrder.setHandleTime(rentForegiftOrder.getPayTime());
                rentForegiftOrder.setPayOrderMoney(rentForegiftOrder.getMoney());
            }
            if (rentForegiftOrder.getPayType() != ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                rentForegiftOrders.add(rentForegiftOrder);
            }
        }

        List<RentPeriodOrder> rentPeriodOrders = new ArrayList<RentPeriodOrder>();
        for(RentPeriodOrder rentPeriodOrder: rentPeriodOrderList){
            rentPeriodOrder.setSourceType(CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue());
            rentPeriodOrder.setRefundableMoney(rentPeriodOrder.getMoney());
            if (rentPeriodOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                AlipayPayOrder alipayPayOrder;
                alipayPayOrder = alipayPayOrderService.findBySourceId(rentPeriodOrder.getId());
                rentPeriodOrder.setPayOrderId(alipayPayOrder.getId());
                rentPeriodOrder.setHandleTime(alipayPayOrder.getHandleTime());
                rentPeriodOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                rentPeriodOrder.setMemo(alipayPayOrder.getMemo());
            } else if (rentPeriodOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                WeixinPayOrder weixinPayOrder;
                weixinPayOrder = weixinPayOrderService.findBySourceId(rentPeriodOrder.getId());
                rentPeriodOrder.setPayOrderId(weixinPayOrder.getId());
                rentPeriodOrder.setHandleTime(weixinPayOrder.getHandleTime());
                rentPeriodOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                rentPeriodOrder.setMemo(weixinPayOrder.getMemo());
            } else if (rentPeriodOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                WeixinmpPayOrder weixinmpPayOrder;
                weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(rentPeriodOrder.getId());
                rentPeriodOrder.setPayOrderId(weixinmpPayOrder.getId());
                rentPeriodOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                rentPeriodOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                rentPeriodOrder.setMemo(weixinmpPayOrder.getMemo());
            } else if (rentPeriodOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(rentPeriodOrder.getId());
                rentPeriodOrder.setPayOrderId(alipayfwPayOrder.getId());
                rentPeriodOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                rentPeriodOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                rentPeriodOrder.setMemo(alipayfwPayOrder.getMemo());
            } else {
                rentPeriodOrder.setHandleTime(rentPeriodOrder.getPayTime());
                rentPeriodOrder.setPayOrderMoney(rentPeriodOrder.getMoney());
            }
            if (rentPeriodOrder.getPayType() != ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                rentPeriodOrders.add(rentPeriodOrder);
            }
        }
        for(RentInsuranceOrder rentInsuranceOrder: rentInsuranceOrderList){
            rentInsuranceOrder.setSourceType(CustomerRefundRecord.SourceType.ZDINSURANCE.getValue());
            rentInsuranceOrder.setRefundableMoney(rentInsuranceOrder.getMoney());
            if (rentInsuranceOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                AlipayPayOrder alipayPayOrder = alipayPayOrderService.findBySourceId(rentInsuranceOrder.getId());
                rentInsuranceOrder.setPayOrderId(alipayPayOrder.getId());
                rentInsuranceOrder.setHandleTime(alipayPayOrder.getHandleTime());
                rentInsuranceOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                rentInsuranceOrder.setMemo(alipayPayOrder.getMemo());
            } else if (rentInsuranceOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                WeixinPayOrder weixinPayOrder = weixinPayOrderService.findBySourceId(rentInsuranceOrder.getId());
                rentInsuranceOrder.setPayOrderId(weixinPayOrder.getId());
                rentInsuranceOrder.setHandleTime(weixinPayOrder.getHandleTime());
                rentInsuranceOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                rentInsuranceOrder.setMemo(weixinPayOrder.getMemo());
            } else if (rentInsuranceOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(rentInsuranceOrder.getId());
                rentInsuranceOrder.setPayOrderId(weixinmpPayOrder.getId());
                rentInsuranceOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                rentInsuranceOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                rentInsuranceOrder.setMemo(weixinmpPayOrder.getMemo());
            } else if (rentInsuranceOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(rentInsuranceOrder.getId());
                rentInsuranceOrder.setPayOrderId(alipayfwPayOrder.getId());
                rentInsuranceOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                rentInsuranceOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                rentInsuranceOrder.setMemo(alipayfwPayOrder.getMemo());
            } else {
                rentInsuranceOrder.setHandleTime(rentInsuranceOrder.getPayTime());
                rentInsuranceOrder.setPayOrderMoney(rentInsuranceOrder.getMoney());
            }
        }
        for(CustomerMultiOrder customerMultiOrder: customerMultiOrderList){
            customerMultiOrder.setSourceType(CustomerRefundRecord.SourceType.ZDMULTI.getValue());
            customerMultiOrder.setRefundableMoney(customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney());
        }

        model.addAttribute("customerForegiftOrderList", rentForegiftOrders);
        model.addAttribute("packetPeriodOrderList", rentPeriodOrders);
        model.addAttribute("insuranceOrderList", rentInsuranceOrderList);
        model.addAttribute("customerMultiOrderList", customerMultiOrderList);
        model.addAttribute("customerId", id);
        return "/security/zd/rent_refund/refund";
    }

    @ViewModel(ViewModel.INNER_PAGE)
    @RequestMapping(value = "edit_money.htm")
    public void editMoney(Model model, Integer refundableMoney, Integer realRefundMoney) {
        model.addAttribute("refundableMoney", refundableMoney);
        model.addAttribute("realRefundMoney", realRefundMoney);
    }

    @RequestMapping("audit_money.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult audit(HttpSession httpSession, String data) throws AlipayApiException, IOException {
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

        if(auditMap.get("orderList") == null){
            return ExtResult.failResult("没有选择退款行！");
        }

        List <Map> orderList = (List)auditMap.get("orderList");
        for(Map order : orderList){
            Long refundRecordId = Long.parseLong(order.get("refundRecordId").toString());
            Integer sourceType = Integer.parseInt(order.get("sourceType").toString());
            String sourceId = order.get("sourceId").toString();
            Integer refundMoney = Integer.parseInt(order.get("refundMoney").toString());

            DataResult dataResult = rentRefundService.refund(userName, refundType, sourceType, sourceId, refundMoney, refundRecordId, null, false);
            String ptPayOrderId = null;
            if(dataResult.getData() != null){
                Map m = (Map)dataResult.getData();
                Object objId = m.get("ptPayOrderId");
                if(objId != null) {
                    ptPayOrderId = (String)objId;
                }
            }
            if(dataResult.isSuccess()){
                customerRefundRecordService.updateZdStatus(refundRecordId,refundType,ptPayOrderId,
                        CustomerRefundRecord.Status.FINISH.getValue(),
                        refundMoney, new Date());

                message += String.format("%s%s退款成功;",CustomerRefundRecord.SourceType.getName(sourceType), sourceId);
            }else{
                customerRefundRecordService.updateZdStatus(refundRecordId,refundType,ptPayOrderId,
                        CustomerRefundRecord.Status.FAIL.getValue(),
                        refundMoney, new Date());

                message += String.format("%s%s退款失败(%s);",CustomerRefundRecord.SourceType.getName(sourceType), sourceId, dataResult.getMessage());
            }
        }
        return  ExtResult.successResult(message);
    }

    @RequestMapping("refund_money.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public ExtResult refund(HttpSession httpSession, String data) throws AlipayApiException, IOException {
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

        if(auditMap.get("refundType") == null){
            return ExtResult.failResult("退款方式不能为空！");
        }
        int refundType = Integer.parseInt(auditMap.get("refundType").toString());

        if(auditMap.get("orderList") == null){
            return ExtResult.failResult("没有选择退款行！");
        }

        List<CustomerRentBattery> batteryList = customerRentBatteryService.findByCustomerId(customerId);
        if (!batteryList.isEmpty()) {
            return ExtResult.failResult("客户持有电池无法退款！");
        }

        //判断用户是否存在审核中订单，存在不让进行退款操作
        if(customerRefundRecordService.existZdRefund(customerId) > 0){
            return ExtResult.failResult("用户存在待审核中订单，不允许退款操作！");
        }

        List <Map> orderList = (List)auditMap.get("orderList");
        String orderId = customerRefundRecordService.newOrderId();
        for(Map order : orderList){
            Integer sourceType = Integer.parseInt(order.get("sourceType").toString());
            String sourceId = order.get("sourceId").toString();
            Integer refundMoney = Integer.parseInt(order.get("refundMoney").toString());

            ExtResult extResult = rentRefundService.refund(userName, refundType, sourceType, sourceId, refundMoney,null, orderId, false);
            if(extResult.isSuccess()){
                message += String.format("%s%s退款成功;",CustomerRefundRecord.SourceType.getName(sourceType), sourceId);
            }else{
                message += String.format("%s%s退款失败(%s);",CustomerRefundRecord.SourceType.getName(sourceType), sourceId, extResult.getMessage());
            }
        }
        return  ExtResult.successResult(message);
    }
}
