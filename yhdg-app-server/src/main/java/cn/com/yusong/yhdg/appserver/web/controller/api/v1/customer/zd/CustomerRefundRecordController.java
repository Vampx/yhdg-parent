package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zd;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerMultiOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.CustomerRentBatteryService;
import cn.com.yusong.yhdg.appserver.service.zd.CustomerRentInfoService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerRefundRecordService;
import cn.com.yusong.yhdg.appserver.service.zd.RentInsuranceOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerMultiOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.basic.CustomerRefundRecord;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller("api_v1_customer_zd_customer_refund_record")
@RequestMapping(value = "/api/v1/customer/zd/customer_refund_record")
public class CustomerRefundRecordController extends ApiController {
    static final Logger log = LogManager.getLogger(CustomerRefundRecordController.class);
    @Autowired
    private CustomerRefundRecordService customerRefundRecordService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    CustomerRentBatteryService customerRentBatteryService;
    @Autowired
    CustomerRentInfoService customerRentInfoService;
    @Autowired
    RentInsuranceOrderService rentInsuranceOrderService;
    @Autowired
    CustomerMultiOrderService customerMultiOrderService;

    /**
     * 18-查询可退款订单列表
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/find_can_refund_list.htm")
    public RestResult findCanRefundList() {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        if (customerRentBatteryService.exists(customerId) > 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此客户已租电池，不能申请退还电池押金");
        }

        List<RentForegiftOrder> foregiftOrders = rentForegiftOrderService.findListByCustomerIdAndStatus(customerId, RentForegiftOrder.Status.PAY_OK.getValue());
        List<NotNullMap> foregiftOrderList = new ArrayList();
        for (RentForegiftOrder order : foregiftOrders) {
            if (order.getPayType() == ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                continue;
            }
            NotNullMap line = new NotNullMap();
            line.put("id", order.getId());
            line.put("canRefundMoney", (order.getMoney() == null ? 0 : order.getMoney()) + (order.getDeductionTicketMoney() == null ? 0 : order.getDeductionTicketMoney()));

            line.put("money", order.getMoney());
            line.put("price", order.getPrice());
            line.put("ticketMoney", order.getTicketMoney());
            line.put("deductionTicketMoney", order.getDeductionTicketMoney());
            foregiftOrderList.add(line);
        }

        List<RentPeriodOrder> rentPeriodOrders = rentPeriodOrderService.findListByCustomerIdAndStatus(customerId, RentPeriodOrder.Status.NOT_USE.getValue());
        List<NotNullMap> rentPeriodOrderList = new ArrayList();
        for (RentPeriodOrder order : rentPeriodOrders) {
            if (order.getPayType() == ConstEnum.PayType.VEHICLE_GROUP.getValue()) {
                continue;
            }
            NotNullMap line = new NotNullMap();
            line.put("id", order.getId());
            line.put("money", order.getMoney());
            line.put("price", order.getPrice());
            line.put("ticketMoney", order.getTicketMoney());
            rentPeriodOrderList.add(line);
        }

//        List<RentInsuranceOrder> insuranceOrders = rentInsuranceOrderService.findListByCustomerIdAndStatus(customerId, RentInsuranceOrder.Status.PAID.getValue());
//        List<NotNullMap> insuranceOrderList = new ArrayList();
//        for (RentInsuranceOrder order : insuranceOrders) {
//            NotNullMap line = new NotNullMap();
//            line.put("id", order.getId());
//            line.put("money", order.getMoney());
//            line.put("price", order.getPrice());
//            insuranceOrderList.add(line);
//        }

        List<CustomerMultiOrder> multiOrderList = customerMultiOrderService.findListByCustomerIdAndStatus(customerId, CustomerMultiOrder.Status.IN_PAY.getValue());
        List<NotNullMap> customerMultiOrderList = new ArrayList();
        for (CustomerMultiOrder order : multiOrderList) {
            if (order.getType() == Battery.Category.RENT.getValue()) {
                NotNullMap line = new NotNullMap();
                line.put("id", order.getId());
                line.put("money", order.getTotalMoney() - order.getDebtMoney());
                line.put("price", order.getTotalMoney());
                line.put("ticketMoney", 0);
                customerMultiOrderList.add(line);
            }
        }

        NotNullMap data = new NotNullMap();
        data.put("foregiftOrderList", foregiftOrderList);
        data.put("rentPeriodOrderList", rentPeriodOrderList);
//        data.put("insuranceOrderList", insuranceOrderList);
        data.put("customerMultiOrderList", customerMultiOrderList);
        return RestResult.dataResult(0, null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApplyrefundParam {
        public String[] foregiftOrderIds;
        public String[] packetPeriodOrderIds;
        public String[] insuranceOrderIds;
        public long[] customerMultiOrderIds;
    }

    /**
     * 19-申请批量订单退款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/create.htm")
    public RestResult create(@Valid @RequestBody ApplyrefundParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer;
        customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        if (customerRentBatteryService.exists(customerId) > 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此客户已租电池，不能申请退还电池押金");
        }

        List<RentForegiftOrder> foregiftOrderList = new ArrayList();
        if (param.foregiftOrderIds != null && param.foregiftOrderIds.length>0) {
            CustomerRentInfo customerRentInfo = customerRentInfoService.find(customerId);
            if (customerRentInfo == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户没有电池押金，不能申请退还电池押金");
            }

            if (customerRentInfo != null && customerRentInfo.getVehicleForegiftFlag() != null && customerRentInfo.getVehicleForegiftFlag() == ConstEnum.Flag.TRUE.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户已交纳租车押金，不能申请退还电池押金");
            }

            for (String id : param.foregiftOrderIds) {
                RentForegiftOrder order = rentForegiftOrderService.find(id);
                if (order == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "押金订单(" + id + ")不存在");
                } else if (order.getStatus() != RentForegiftOrder.Status.PAY_OK.getValue()) {
                    return RestResult.result(RespCode.CODE_1.getValue(), "客户押金订单(" + id + ")状态为" + order.getStatusName() + "，不能申请退款");
                }
                foregiftOrderList.add(order);
            }
        }
        List<RentPeriodOrder> rentPeriodOrderList = new ArrayList();
        if (param.packetPeriodOrderIds != null && param.packetPeriodOrderIds.length>0) {
            for (String id : param.packetPeriodOrderIds) {
                RentPeriodOrder order = rentPeriodOrderService.find(id);
                if (order == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "包时段订单(" + id + ")不存在");
                }
                if (order.getStatus() != RentPeriodOrder.Status.NOT_USE.getValue()) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "包时段套餐(" + id + ")状态为" + order.getStatusName() + "，不能申请退款");
                }
                rentPeriodOrderList.add(order);
            }
        }
        List<RentInsuranceOrder> insuranceOrderList = new ArrayList();
        if (param.insuranceOrderIds != null && param.insuranceOrderIds.length>0) {
            for (String id : param.insuranceOrderIds) {
                RentInsuranceOrder order = rentInsuranceOrderService.find(id);
                if (order == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "保险订单(" + id + ")不存在");
                }
                if (order.getStatus() != RentInsuranceOrder.Status.PAID.getValue()) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "保险套餐(" + id + ")状态为" + order.getStatusName() + "，不能申请退款");
                }
                insuranceOrderList.add(order);
            }
        }

        List<CustomerMultiOrder> customerMultiOrderList = new ArrayList<CustomerMultiOrder>();
        if (param.customerMultiOrderIds != null && param.customerMultiOrderIds.length > 0) {
            for (long id : param.customerMultiOrderIds) {
                CustomerMultiOrder order = customerMultiOrderService.find(id);
                if (order == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "多通道订单(" + id + ")不存在");
                }
                if (order.getStatus() != CustomerMultiOrder.Status.IN_PAY.getValue()) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "多通道订单(" + id + ")状态为" + order.getStatusName() + "，不能申请退款");
                }
                customerMultiOrderList.add(order);
            }
        }

        try {
            //添加退款记录并修改订单状态
            return customerRefundRecordService.addZdRecordAndUpdateStatus(customer, foregiftOrderList, rentPeriodOrderList, insuranceOrderList, customerMultiOrderList);
        } catch (IllegalArgumentException e) {
            log.error("customerRefundRecordService.addRecordAndUpdateStatus 申请退款异常 error", e);
            log.error("申请退款异常:{}", e.getMessage());
            return RestResult.result(RespCode.CODE_1.getValue(), e.getMessage());
        }

    }

    /**
     * 20-查询待审核退款申请总汇
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/find_wait_audit_info.htm")
    public RestResult findWaitAuditInfo() {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        List<CustomerRefundRecord> refundList = customerRefundRecordService.findListByCustomerIdAndStatus(
                customerId, CustomerRefundRecord.Status.APPLY.getValue(), null, null, CustomerRefundRecord.Type.ZD.getValue());

        List<NotNullMap> list = new ArrayList();
        if (refundList != null && refundList.size() > 0) {
            int expectRefundMoney;
            int foregiftMoney = 0;
            int packetPeriodMoney = 0;
            int insuranceMoney = 0;
            int multiMoney = 0;
            for (CustomerRefundRecord record : refundList) {
                if (record.getSourceType() == CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue()) {
                    foregiftMoney += record.getRefundMoney();
                } else if (record.getSourceType() == CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue()) {
                    packetPeriodMoney += record.getRefundMoney();
                } else if (record.getSourceType() == CustomerRefundRecord.SourceType.ZDINSURANCE.getValue()) {
                    insuranceMoney += record.getRefundMoney();
                } else if (record.getSourceType() == CustomerRefundRecord.SourceType.ZDMULTI.getValue()) {
                    multiMoney += record.getRefundMoney();
                }
            }
            expectRefundMoney = foregiftMoney + packetPeriodMoney + insuranceMoney + multiMoney;

            NotNullMap line = new NotNullMap();
            line.put("orderId", refundList.get(0).getOrderId());
            line.putDateTime("applyTime", refundList.get(0).getCreateTime());
            line.putDateTime("expectFinishTime", DateUtils.addDays(refundList.get(0).getCreateTime(), 7));
            line.put("expectRefundMoney", expectRefundMoney);
            line.put("foregiftMoney", foregiftMoney);
            line.put("packetPeriodMoney", packetPeriodMoney);
            line.put("insuranceMoney", insuranceMoney);
            line.put("multiMoney", multiMoney);

            list.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public Integer status;
        public Integer offset;
        public Integer limit;
    }

    /**
     * 21-查询退款记录
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer;
        customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        List<CustomerRefundRecord> refundList = customerRefundRecordService.findListByCustomerIdAndStatus(
                customerId, param.status, param.offset, param.limit, CustomerRefundRecord.Type.ZD.getValue());

        List<NotNullMap> list = new ArrayList();
        if (refundList != null && refundList.size() > 0) {
            for (CustomerRefundRecord record : refundList) {
                NotNullMap line = new NotNullMap();
                line.put("id", record.getId());
                line.put("orderId", record.getOrderId());
                line.put("sourceId", record.getSourceId());
                line.put("sourceType", record.getSourceType());
                line.put("refundMoney", record.getRefundMoney());
                line.putDateTime("refundTime", record.getRefundTime());
                line.put("status", record.getStatus());

                list.add(line);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CancelParam {
        public String orderId;
    }

    /**
     * 22-取消退款申请
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/cancel.htm")
    public RestResult cancel(@Valid @RequestBody CancelParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer;
        customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        List<CustomerRefundRecord> refundList = customerRefundRecordService.findListByorderId(customerId, param.orderId);
        if (refundList.size() == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }
//        else {
//            for (CustomerRefundRecord record : refundList) {
//                if (record.getStatus() != CustomerRefundRecord.Status.APPLY.getValue()) {
//                    return RestResult.result(RespCode.CODE_2.getValue(), "退款订单(" + record.getOrderId() + ")状态为" + CustomerRefundRecord.Status.getName(record.getStatus()) + "，不能取消退款申请");
//                }
//            }
//        }

        try {
            //修改订单和退款记录状态
            return customerRefundRecordService.updateZdOrderAndRefundStatus(refundList);
        } catch (IllegalArgumentException e) {
            log.error("customerRefundRecordService.updateZdOrderAndRefundStatus 取消退款申请异常 error", e);
            log.error("申请退款异常:{}", e.getMessage());
            return RestResult.result(RespCode.CODE_1.getValue(), e.getMessage());
        }
    }
}