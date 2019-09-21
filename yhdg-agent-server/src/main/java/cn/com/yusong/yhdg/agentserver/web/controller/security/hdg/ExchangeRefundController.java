package cn.com.yusong.yhdg.agentserver.web.controller.security.hdg;

import cn.com.yusong.yhdg.agentserver.entity.SessionUser;
import cn.com.yusong.yhdg.agentserver.service.basic.*;
import cn.com.yusong.yhdg.agentserver.service.hdg.InsuranceOrderService;
import cn.com.yusong.yhdg.agentserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.agentserver.service.hdg.ExchangeRefundService;
import cn.com.yusong.yhdg.agentserver.web.controller.security.basic.SecurityController;
import cn.com.yusong.yhdg.common.annotation.SecurityControl;
import cn.com.yusong.yhdg.common.annotation.ViewModel;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.json.PageResult;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
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
@RequestMapping(value = "/security/hdg/exchange_refund")
public class ExchangeRefundController extends SecurityController {
    static Logger log = LoggerFactory.getLogger(ExchangeRefundController.class);

    @Autowired
    ExchangeRefundService refundService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    CustomerRefundRecordService customerRefundRecordService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    InsuranceOrderService insuranceOrderService;
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

    @SecurityControl(limits = "hdg.ExchangeRefund:list")
    @RequestMapping(value = "index.htm")
    public void index(Model model) {
        model.addAttribute(MENU_CODE_NAME, "hdg.ExchangeRefund:list");
    }

    @RequestMapping("page.htm")
    @ViewModel(ViewModel.JSON)
    @ResponseBody
    public PageResult page(Customer search) {
        return PageResult.successResult(refundService.findPage(search));
    }

    @RequestMapping(value = "audit.htm")
    public String audit(Model model, Long id) {
        List<CustomerForegiftOrder> customerForegiftOrderList = new ArrayList<CustomerForegiftOrder>();
        List<PacketPeriodOrder> packetPeriodOrderList = new ArrayList<PacketPeriodOrder>();
        List<InsuranceOrder> insuranceOrderList = new ArrayList<InsuranceOrder>();
        List<CustomerMultiOrder> customerMultiOrderList = new ArrayList<CustomerMultiOrder>();

        List<CustomerRefundRecord> customerRefundRecordList = customerRefundRecordService.findByCustomerId(id, CustomerRefundRecord.Status.APPLY.getValue());
        if (customerRefundRecordList != null && customerRefundRecordList.size() > 0) {
            model.addAttribute("applyRefundTime", customerRefundRecordList.get(0).getCreateTime());
        }
        for (CustomerRefundRecord customerRefundRecord : customerRefundRecordList) {
            if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue()) {
                CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderService.find(customerRefundRecord.getSourceId());
                if (customerForegiftOrder != null && customerForegiftOrder.getStatus() == CustomerForegiftOrder.Status.APPLY_REFUND.getValue() && customerForegiftOrder.getPayType() != ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                    customerForegiftOrder.setRefundableMoney(customerForegiftOrder.getMoney() + (customerForegiftOrder.getDeductionTicketMoney() == null ? 0 : customerForegiftOrder.getDeductionTicketMoney()));
                    customerForegiftOrder.setRefundRecordId(customerRefundRecord.getId());
                    customerForegiftOrder.setRefundMoney(customerRefundRecord.getRefundMoney());
                    customerForegiftOrder.setSourceType(customerRefundRecord.getSourceType());
                    if (customerForegiftOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                        AlipayPayOrder alipayPayOrder = alipayPayOrderService.findBySourceId(customerForegiftOrder.getId());
                        customerForegiftOrder.setPayOrderId(alipayPayOrder.getId());
                        customerForegiftOrder.setHandleTime(alipayPayOrder.getHandleTime());
                        customerForegiftOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                        customerForegiftOrder.setMemo(alipayPayOrder.getMemo());
                    } else if (customerForegiftOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                        WeixinPayOrder weixinPayOrder = weixinPayOrderService.findBySourceId(customerForegiftOrder.getId());
                        customerForegiftOrder.setPayOrderId(weixinPayOrder.getId());
                        customerForegiftOrder.setHandleTime(weixinPayOrder.getHandleTime());
                        customerForegiftOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                        customerForegiftOrder.setMemo(weixinPayOrder.getMemo());
                    } else if (customerForegiftOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                        WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(customerForegiftOrder.getId());
                        customerForegiftOrder.setPayOrderId(weixinmpPayOrder.getId());
                        customerForegiftOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                        customerForegiftOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                        customerForegiftOrder.setMemo(weixinmpPayOrder.getMemo());
                    } else if (customerForegiftOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                        AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(customerForegiftOrder.getId());
                        customerForegiftOrder.setPayOrderId(alipayfwPayOrder.getId());
                        customerForegiftOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                        customerForegiftOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                        customerForegiftOrder.setMemo(alipayfwPayOrder.getMemo());
                    } else {
                        customerForegiftOrder.setHandleTime(customerForegiftOrder.getPayTime());
                        customerForegiftOrder.setPayOrderMoney(customerForegiftOrder.getMoney());
                    }
                    customerForegiftOrderList.add(customerForegiftOrder);
                }
            } else if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue()) {
                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.find(customerRefundRecord.getSourceId());
                if (packetPeriodOrder != null && packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.APPLY_REFUND.getValue() && packetPeriodOrder.getPayType() != ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                    packetPeriodOrder.setRefundRecordId(customerRefundRecord.getId());
                    packetPeriodOrder.setSourceType(customerRefundRecord.getSourceType());
                    packetPeriodOrder.setRefundableMoney(packetPeriodOrder.getMoney());
                    if (packetPeriodOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                        AlipayPayOrder alipayPayOrder = alipayPayOrderService.findBySourceId(packetPeriodOrder.getId());
                        packetPeriodOrder.setPayOrderId(alipayPayOrder.getId());
                        packetPeriodOrder.setHandleTime(alipayPayOrder.getHandleTime());
                        packetPeriodOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                        packetPeriodOrder.setMemo(alipayPayOrder.getMemo());
                    } else if (packetPeriodOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                        WeixinPayOrder weixinPayOrder = weixinPayOrderService.findBySourceId(packetPeriodOrder.getId());
                        packetPeriodOrder.setPayOrderId(weixinPayOrder.getId());
                        packetPeriodOrder.setHandleTime(weixinPayOrder.getHandleTime());
                        packetPeriodOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                        packetPeriodOrder.setMemo(weixinPayOrder.getMemo());
                    } else if (packetPeriodOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                        WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(packetPeriodOrder.getId());
                        packetPeriodOrder.setPayOrderId(weixinmpPayOrder.getId());
                        packetPeriodOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                        packetPeriodOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                        packetPeriodOrder.setMemo(weixinmpPayOrder.getMemo());
                    } else if (packetPeriodOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                        AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(packetPeriodOrder.getId());
                        packetPeriodOrder.setPayOrderId(alipayfwPayOrder.getId());
                        packetPeriodOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                        packetPeriodOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                        packetPeriodOrder.setMemo(alipayfwPayOrder.getMemo());
                    } else {
                        packetPeriodOrder.setHandleTime(packetPeriodOrder.getPayTime());
                        packetPeriodOrder.setPayOrderMoney(packetPeriodOrder.getMoney());
                    }
                    packetPeriodOrderList.add(packetPeriodOrder);
                }
            } else if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDGINSURANCE.getValue()) {
                InsuranceOrder insuranceOrder = insuranceOrderService.find(customerRefundRecord.getSourceId());
                if (insuranceOrder != null && insuranceOrder.getStatus() == InsuranceOrder.Status.APPLY_REFUND.getValue()) {
                    insuranceOrder.setRefundRecordId(customerRefundRecord.getId());
                    insuranceOrder.setSourceType(customerRefundRecord.getSourceType());
                    insuranceOrder.setRefundableMoney(insuranceOrder.getMoney());
                    if (insuranceOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                        AlipayPayOrder alipayPayOrder = alipayPayOrderService.findBySourceId(insuranceOrder.getId());
                        insuranceOrder.setPayOrderId(alipayPayOrder.getId());
                        insuranceOrder.setHandleTime(alipayPayOrder.getHandleTime());
                        insuranceOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                        insuranceOrder.setMemo(alipayPayOrder.getMemo());
                    } else if (insuranceOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                        WeixinPayOrder weixinPayOrder = weixinPayOrderService.findBySourceId(insuranceOrder.getId());
                        insuranceOrder.setPayOrderId(weixinPayOrder.getId());
                        insuranceOrder.setHandleTime(weixinPayOrder.getHandleTime());
                        insuranceOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                        insuranceOrder.setMemo(weixinPayOrder.getMemo());
                    } else if (insuranceOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                        WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(insuranceOrder.getId());
                        insuranceOrder.setPayOrderId(weixinmpPayOrder.getId());
                        insuranceOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                        insuranceOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                        insuranceOrder.setMemo(weixinmpPayOrder.getMemo());
                    } else if (insuranceOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                        AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(insuranceOrder.getId());
                        insuranceOrder.setPayOrderId(alipayfwPayOrder.getId());
                        insuranceOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                        insuranceOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                        insuranceOrder.setMemo(alipayfwPayOrder.getMemo());
                    } else {
                        insuranceOrder.setHandleTime(insuranceOrder.getPayTime());
                        insuranceOrder.setPayOrderMoney(insuranceOrder.getMoney());
                    }
                    insuranceOrderList.add(insuranceOrder);
                }
            } else if (customerRefundRecord.getSourceType() == CustomerRefundRecord.SourceType.HDMULTI.getValue()) {
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
        model.addAttribute("customerForegiftOrderList", customerForegiftOrderList);
        model.addAttribute("packetPeriodOrderList", packetPeriodOrderList);
        model.addAttribute("insuranceOrderList", insuranceOrderList);
        model.addAttribute("customerMultiOrderList", customerMultiOrderList);
        model.addAttribute("customerId", id);
        return "/security/hdg/exchange_refund/audit";
    }


    @RequestMapping(value = "refund.htm")
    public String refund(Model model, Long id) {
        List<CustomerForegiftOrder> customerForegiftOrderList = customerForegiftOrderService.findCanRefundByCustomerId(id);
        List<PacketPeriodOrder> packetPeriodOrderList = packetPeriodOrderService.findCanRefundByCustomerId(id);
        List<InsuranceOrder> insuranceOrderList = insuranceOrderService.findCanRefundByCustomerId(id);
        List<CustomerMultiOrder> customerMultiOrderList = customerMultiOrderService.findCanRefund(id, CustomerMultiOrder.Type.HD.getValue(), CustomerMultiOrder.Status.IN_PAY.getValue());

        List<CustomerForegiftOrder> customerForegiftOrders = new ArrayList<CustomerForegiftOrder>();
        for(CustomerForegiftOrder customerForegiftOrder: customerForegiftOrderList){
            customerForegiftOrder.setRefundableMoney(customerForegiftOrder.getMoney() + (customerForegiftOrder.getDeductionTicketMoney() == null ? 0 : customerForegiftOrder.getDeductionTicketMoney()));
            customerForegiftOrder.setSourceType(CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue());
            if (customerForegiftOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                AlipayPayOrder alipayPayOrder;
                alipayPayOrder = alipayPayOrderService.findBySourceId(customerForegiftOrder.getId());
                customerForegiftOrder.setPayOrderId(alipayPayOrder.getId());
                customerForegiftOrder.setHandleTime(alipayPayOrder.getHandleTime());
                customerForegiftOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                customerForegiftOrder.setMemo(alipayPayOrder.getMemo());
            } else if (customerForegiftOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                WeixinPayOrder weixinPayOrder;
                weixinPayOrder = weixinPayOrderService.findBySourceId(customerForegiftOrder.getId());
                customerForegiftOrder.setPayOrderId(weixinPayOrder.getId());
                customerForegiftOrder.setHandleTime(weixinPayOrder.getHandleTime());
                customerForegiftOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                customerForegiftOrder.setMemo(weixinPayOrder.getMemo());
            } else if (customerForegiftOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                WeixinmpPayOrder weixinmpPayOrder;
                weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(customerForegiftOrder.getId());
                customerForegiftOrder.setPayOrderId(weixinmpPayOrder.getId());
                customerForegiftOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                customerForegiftOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                customerForegiftOrder.setMemo(weixinmpPayOrder.getMemo());
            } else if (customerForegiftOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                AlipayfwPayOrder alipayfwPayOrder;
                alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(customerForegiftOrder.getId());
                customerForegiftOrder.setPayOrderId(alipayfwPayOrder.getId());
                customerForegiftOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                customerForegiftOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                customerForegiftOrder.setMemo(alipayfwPayOrder.getMemo());
            } else {
                customerForegiftOrder.setHandleTime(customerForegiftOrder.getPayTime());
                customerForegiftOrder.setPayOrderMoney(customerForegiftOrder.getMoney());
            }
            if (customerForegiftOrder.getPayType() != ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                customerForegiftOrders.add(customerForegiftOrder);
            }
        }
        List<PacketPeriodOrder> packetPeriodOrders = new ArrayList<PacketPeriodOrder>();
        for(PacketPeriodOrder packetPeriodOrder: packetPeriodOrderList){
            packetPeriodOrder.setSourceType(CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue());
            packetPeriodOrder.setRefundableMoney(packetPeriodOrder.getMoney());
            if (packetPeriodOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                AlipayPayOrder alipayPayOrder;
                alipayPayOrder = alipayPayOrderService.findBySourceId(packetPeriodOrder.getId());
                packetPeriodOrder.setPayOrderId(alipayPayOrder.getId());
                packetPeriodOrder.setHandleTime(alipayPayOrder.getHandleTime());
                packetPeriodOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                packetPeriodOrder.setMemo(alipayPayOrder.getMemo());
            } else if (packetPeriodOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                WeixinPayOrder weixinPayOrder;
                weixinPayOrder = weixinPayOrderService.findBySourceId(packetPeriodOrder.getId());
                packetPeriodOrder.setPayOrderId(weixinPayOrder.getId());
                packetPeriodOrder.setHandleTime(weixinPayOrder.getHandleTime());
                packetPeriodOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                packetPeriodOrder.setMemo(weixinPayOrder.getMemo());
            } else if (packetPeriodOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                WeixinmpPayOrder weixinmpPayOrder;
                weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(packetPeriodOrder.getId());
                packetPeriodOrder.setPayOrderId(weixinmpPayOrder.getId());
                packetPeriodOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                packetPeriodOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                packetPeriodOrder.setMemo(weixinmpPayOrder.getMemo());
            } else if (packetPeriodOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(packetPeriodOrder.getId());
                packetPeriodOrder.setPayOrderId(alipayfwPayOrder.getId());
                packetPeriodOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                packetPeriodOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                packetPeriodOrder.setMemo(alipayfwPayOrder.getMemo());
            } else {
                packetPeriodOrder.setHandleTime(packetPeriodOrder.getPayTime());
                packetPeriodOrder.setPayOrderMoney(packetPeriodOrder.getMoney());
            }
            if (packetPeriodOrder.getPayType() != ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                packetPeriodOrders.add(packetPeriodOrder);
            }
        }
        for(InsuranceOrder insuranceOrder: insuranceOrderList){
            insuranceOrder.setSourceType(CustomerRefundRecord.SourceType.HDGINSURANCE.getValue());
            insuranceOrder.setRefundableMoney(insuranceOrder.getMoney());
            if (insuranceOrder.getPayType() == ConstEnum.PayType.ALIPAY.getValue()) {
                AlipayPayOrder alipayPayOrder = alipayPayOrderService.findBySourceId(insuranceOrder.getId());
                insuranceOrder.setPayOrderId(alipayPayOrder.getId());
                insuranceOrder.setHandleTime(alipayPayOrder.getHandleTime());
                insuranceOrder.setPayOrderMoney(alipayPayOrder.getMoney());
                insuranceOrder.setMemo(alipayPayOrder.getMemo());
            } else if (insuranceOrder.getPayType() == ConstEnum.PayType.WEIXIN.getValue()) {
                WeixinPayOrder weixinPayOrder = weixinPayOrderService.findBySourceId(insuranceOrder.getId());
                insuranceOrder.setPayOrderId(weixinPayOrder.getId());
                insuranceOrder.setHandleTime(weixinPayOrder.getHandleTime());
                insuranceOrder.setPayOrderMoney(weixinPayOrder.getMoney());
                insuranceOrder.setMemo(weixinPayOrder.getMemo());
            } else if (insuranceOrder.getPayType() == ConstEnum.PayType.WEIXIN_MP.getValue()) {
                WeixinmpPayOrder weixinmpPayOrder = weixinmpPayOrderService.findBySourceId(insuranceOrder.getId());
                insuranceOrder.setPayOrderId(weixinmpPayOrder.getId());
                insuranceOrder.setHandleTime(weixinmpPayOrder.getHandleTime());
                insuranceOrder.setPayOrderMoney(weixinmpPayOrder.getMoney());
                insuranceOrder.setMemo(weixinmpPayOrder.getMemo());
            } else if (insuranceOrder.getPayType() == ConstEnum.PayType.ALIPAY_FW.getValue()) {
                AlipayfwPayOrder alipayfwPayOrder = alipayfwPayOrderService.findBySourceId(insuranceOrder.getId());
                insuranceOrder.setPayOrderId(alipayfwPayOrder.getId());
                insuranceOrder.setHandleTime(alipayfwPayOrder.getHandleTime());
                insuranceOrder.setPayOrderMoney(alipayfwPayOrder.getMoney());
                insuranceOrder.setMemo(alipayfwPayOrder.getMemo());
            } else {
                insuranceOrder.setHandleTime(insuranceOrder.getPayTime());
                insuranceOrder.setPayOrderMoney(insuranceOrder.getMoney());
            }
        }
        for(CustomerMultiOrder customerMultiOrder: customerMultiOrderList){
            customerMultiOrder.setSourceType(CustomerRefundRecord.SourceType.HDMULTI.getValue());
            customerMultiOrder.setRefundableMoney(customerMultiOrder.getTotalMoney()-customerMultiOrder.getDebtMoney());
        }

        model.addAttribute("customerForegiftOrderList", customerForegiftOrders);
        model.addAttribute("packetPeriodOrderList", packetPeriodOrders);
        model.addAttribute("insuranceOrderList", insuranceOrderList);
        model.addAttribute("customerMultiOrderList", customerMultiOrderList);
        model.addAttribute("customerId", id);
        return "/security/hdg/exchange_refund/refund";
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

            DataResult dataResult = refundService.refund(userName, refundType, sourceType, sourceId, refundMoney, refundRecordId, null, false);
            String ptPayOrderId = null;
            if(dataResult.getData() != null){
                Map m = (Map)dataResult.getData();
                Object objId = m.get("ptPayOrderId");
                if(objId != null) {
                    ptPayOrderId = (String)objId;
                }
            }
            if(dataResult.isSuccess()){
                customerRefundRecordService.updateHdStatus(refundRecordId,refundType,ptPayOrderId,
                        CustomerRefundRecord.Status.FINISH.getValue(),
                        refundMoney, new Date());

                message += String.format("%s%s退款成功;",CustomerRefundRecord.SourceType.getName(sourceType), sourceId);
            }else{
                customerRefundRecordService.updateHdStatus(refundRecordId,refundType,ptPayOrderId,
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
        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findByCustomerId(customerId);
        if (!batteryList.isEmpty()) {
            return ExtResult.failResult("客户持有电池无法退款！");
        }

        //判断用户是否存在审核中订单，存在不让进行退款操作
        if(customerRefundRecordService.existHdRefund(customerId) > 0){
            return ExtResult.failResult("用户存在待审核中订单，不允许退款操作！");
        }

        List <Map> orderList = (List)auditMap.get("orderList");
        String orderId = customerRefundRecordService.newOrderId();
        for(Map order : orderList){
            Integer sourceType = Integer.parseInt(order.get("sourceType").toString());
            String sourceId = order.get("sourceId").toString();
            Integer refundMoney = Integer.parseInt(order.get("refundMoney").toString());

            ExtResult extResult = refundService.refund(userName, refundType, sourceType, sourceId, refundMoney, null, orderId, false);
            if(extResult.isSuccess()){
                message += String.format("%s%s退款成功;",CustomerRefundRecord.SourceType.getName(sourceType), sourceId);
            }else{
                message += String.format("%s%s退款失败(%s);",CustomerRefundRecord.SourceType.getName(sourceType), sourceId, extResult.getMessage());
            }
        }
        return  ExtResult.successResult(message);
    }
}
