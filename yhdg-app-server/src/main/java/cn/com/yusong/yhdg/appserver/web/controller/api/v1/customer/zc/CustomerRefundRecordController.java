package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;

import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.CustomerForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.hdg.PacketPeriodOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.CustomerVehicleInfoService;
import cn.com.yusong.yhdg.appserver.service.zc.GroupOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.VehicleForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.zc.VehiclePeriodOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.CustomerRentBatteryService;
import cn.com.yusong.yhdg.appserver.service.zd.CustomerRentInfoService;
import cn.com.yusong.yhdg.appserver.service.zd.RentForegiftOrderService;
import cn.com.yusong.yhdg.appserver.service.zd.RentPeriodOrderService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.CustomerVehicleInfo;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentBattery;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.Map;

@Controller("api_v1_customer_zc_customer_refund_record")
@RequestMapping(value = "/api/v1/customer/zc/customer_refund_record")
public class CustomerRefundRecordController extends ApiController {
    static final Logger log = LogManager.getLogger(CustomerRefundRecordController.class);
    @Autowired
    private CustomerRefundRecordService customerRefundRecordService;
    @Autowired
    VehiclePeriodOrderService vehiclePeriodOrderService;
    @Autowired
    CustomerService customerService;
    @Autowired
    VehicleForegiftOrderService vehicleForegiftOrderService;
    @Autowired
    CustomerVehicleInfoService customerVehicleInfoService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    CustomerRentBatteryService customerRentBatteryService;
    @Autowired
    CustomerMultiOrderService customerMultiOrderService;
    @Autowired
    GroupOrderService groupOrderService;
    @Autowired
    CustomerForegiftOrderService customerForegiftOrderService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;
    @Autowired
    RentForegiftOrderService rentForegiftOrderService;
    @Autowired
    RentPeriodOrderService rentPeriodOrderService;


    /**
     * 11-查询可退款订单列表
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

//        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoService.findByCustomerId(customerId);
//        if (customerVehicleInfo != null && customerVehicleInfo.getVehicleId() != null) {
//            return RestResult.result(RespCode.CODE_2.getValue(), "此客户已租车辆，不能申请退还组合订单");
//        }

        List<CustomerExchangeBattery> customerExchangeBatteryList = customerExchangeBatteryService.findListByCustomer(customerId);
        if (customerExchangeBatteryList.size() > 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "此客户已租电池，不能申请退还组合订单");
        }

        List<GroupOrder> groupOrders = groupOrderService.findListByCustomerIdAndStatus(customerId, GroupOrder.Status.PAY_OK.getValue());
        List<Map> foregiftList = new ArrayList<Map>();
        List<Map> packetPeriodList = new ArrayList<Map>();
        for (GroupOrder groupOrder : groupOrders) {
            NotNullMap foregiftMap = new NotNullMap();
            NotNullMap packetMap = new NotNullMap();
            //押金可退金额
            int foregiftRefundableMoney = 0;
            //租金可退金额
            int packetRefundableMoney = 0;

            foregiftMap.put("groupOrderId", groupOrder.getId());
            foregiftMap.put("money", groupOrder.getForegiftMoney());
            foregiftMap.put("price", (groupOrder.getVehicleForegiftPrice() == null ? 0 : groupOrder.getVehicleForegiftPrice()) + (groupOrder.getBatteryForegiftPrice() == null ? 0 : groupOrder.getBatteryForegiftPrice()));
            foregiftMap.put("deductionTicketMoney", (groupOrder.getDeductionTicketMoney() == null ? 0 : groupOrder.getDeductionTicketMoney()));
            foregiftMap.put("vehicleForegiftOrderId", "");
            foregiftMap.put("batteryForegiftOrderId", "");
            if (StringUtils.isNotEmpty(groupOrder.getVehicleForegiftId())) {
                VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderService.find(groupOrder.getVehicleForegiftId());
                if (vehicleForegiftOrder != null && vehicleForegiftOrder.getStatus() == VehicleForegiftOrder.Status.PAY_OK.getValue()) {
                    foregiftMap.put("vehicleForegiftOrderId", vehicleForegiftOrder.getId());
                    foregiftRefundableMoney += groupOrder.getVehicleForegiftMoney();
                }
            }

            if (StringUtils.isNotEmpty(groupOrder.getBatteryForegiftId())) {
                if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                    CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderService.find(groupOrder.getBatteryForegiftId());
                    if (customerForegiftOrder != null && customerForegiftOrder.getStatus() == CustomerForegiftOrder.Status.PAY_OK.getValue()) {
                        foregiftMap.put("batteryForegiftOrderId", customerForegiftOrder.getId());
                        foregiftRefundableMoney += groupOrder.getBatteryForegiftMoney();
                    }
                } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                    RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.find(groupOrder.getBatteryForegiftId());
                    if (rentForegiftOrder != null && rentForegiftOrder.getStatus() == RentForegiftOrder.Status.PAY_OK.getValue()) {
                        foregiftMap.put("batteryForegiftOrderId", rentForegiftOrder.getId());
                        foregiftRefundableMoney += groupOrder.getBatteryForegiftMoney();
                    }
                }
            }
            int deductionMoney = (groupOrder.getDeductionTicketMoney() == null ? 0 : groupOrder.getDeductionTicketMoney());
            foregiftRefundableMoney += deductionMoney;
            foregiftMap.put("canRefundMoney", foregiftRefundableMoney);

            packetMap.put("groupOrderId", groupOrder.getId());
            packetMap.put("money", groupOrder.getRentPeriodMoney());
            packetMap.put("price", (groupOrder.getVehiclePeriodPrice() == null ? 0 : groupOrder.getVehiclePeriodPrice()) + (groupOrder.getBatteryRentPeriodPrice() == null ? 0 : groupOrder.getBatteryRentPeriodPrice()));
            packetMap.put("vehiclePeriodOrderId", "");
            packetMap.put("batteryPeriodOrderId", "");
            if (StringUtils.isNotEmpty(groupOrder.getVehiclePeriodId())) {
                VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderService.find(groupOrder.getVehiclePeriodId());
                if (vehiclePeriodOrder != null && vehiclePeriodOrder.getStatus() == VehiclePeriodOrder.Status.NOT_USE.getValue()) {
                    packetMap.put("vehiclePeriodOrderId", vehiclePeriodOrder.getId());
                    packetRefundableMoney += groupOrder.getVehiclePeriodMoney();
                }
            }

            if (StringUtils.isNotEmpty(groupOrder.getBatteryRentId())) {
                if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                    PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.find(groupOrder.getBatteryRentId());
                    if (packetPeriodOrder != null && packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.NOT_USE.getValue()) {
                        packetMap.put("batteryPeriodOrderId", packetPeriodOrder.getId());
                        packetRefundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    }
                } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                    RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.find(groupOrder.getBatteryRentId());
                    if (rentPeriodOrder != null && rentPeriodOrder.getStatus() == RentPeriodOrder.Status.NOT_USE.getValue()) {
                        packetMap.put("batteryPeriodOrderId", rentPeriodOrder.getId());
                        packetRefundableMoney += groupOrder.getBatteryRentPeriodMoney();
                    }
                }
            }
            packetMap.put("canRefundMoney", packetRefundableMoney);
            foregiftList.add(foregiftMap);
            packetPeriodList.add(packetMap);
        }


        List<CustomerMultiOrder> multiOrderList = customerMultiOrderService.findListByCustomerIdAndStatus(customerId, CustomerMultiOrder.Status.IN_PAY.getValue());
        List<Map> customerMultiOrderList = new ArrayList<Map>();
        for (CustomerMultiOrder order : multiOrderList) {
            if (order.getType() == CustomerMultiOrder.Type.ZC.getValue()) {
                NotNullMap line = new NotNullMap();
                line.put("id", order.getId());
                line.put("canRefundMoney", order.getTotalMoney() - order.getDebtMoney());
                line.put("price", order.getTotalMoney());
                customerMultiOrderList.add(line);
            }
        }
        List<Map> foregiftOrderList = new ArrayList<Map>();
        List<Map> packetPeriodOrderList = new ArrayList<Map>();
        for (Map map : foregiftList) {
            String vehicleForegiftOrderId = (String) map.get("vehicleForegiftOrderId");
            String batteryForegiftOrderId = (String) map.get("batteryForegiftOrderId");
            if (StringUtils.isEmpty(vehicleForegiftOrderId) && StringUtils.isEmpty(batteryForegiftOrderId)) {
                continue;
            }
            foregiftOrderList.add(map);
        }
        for (Map map : packetPeriodList) {
            String vehiclePeriodOrderId = (String) map.get("vehiclePeriodOrderId");
            String batteryPeriodOrderId = (String) map.get("batteryPeriodOrderId");
            if (StringUtils.isEmpty(vehiclePeriodOrderId) && StringUtils.isEmpty(batteryPeriodOrderId)) {
                continue;
            }
            packetPeriodOrderList.add(map);
        }
        NotNullMap data = new NotNullMap();
        data.put("foregiftOrderList", foregiftOrderList);
        data.put("packetPeriodOrderList", packetPeriodOrderList);
        data.put("customerMultiOrderList", customerMultiOrderList);
        return RestResult.dataResult(0, null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ApplyRefundParam {
        public List<Map> list;
        public String[] vehicleForegiftOrderIds;
        public String[] vehiclePacketPeriodOrderIds;
        public String[] batteryForegiftOrderIds;
        public String[] batteryPacketPeriodOrderIds;
        public long[] customerMultiOrderIds;
    }

    /**
     * 12-申请批量订单退款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/create.htm")
    public RestResult create(@Valid @RequestBody ApplyRefundParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Customer customer;
        customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoService.findByCustomerId(customerId);
//        if (customerVehicleInfo != null && customerVehicleInfo.getVehicleId() != null) {
//            return RestResult.result(RespCode.CODE_2.getValue(), "此客户已租车辆，不能申请退还租车押金");
//        }

        List<GroupOrder> groupOrderList = new ArrayList<GroupOrder>();
        for (Map map : param.list) {
            String groupOrderId = (String) map.get("groupOrderId");
            GroupOrder groupOrder = groupOrderService.find(groupOrderId);
            if (groupOrder == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "组合订单(" + groupOrderId + ")不存在");
            }
            if (groupOrder.getStatus() != GroupOrder.Status.PAY_OK.getValue()) {
                return RestResult.result(RespCode.CODE_1.getValue(), "客户租车组合订单订单(" + groupOrderId + ")状态为" + groupOrder.getStatusName() + "，不能申请退款");
            }
            Integer canRefundMoney = (Integer) map.get("canRefundMoney");
            groupOrder.setCanRefundMoney(canRefundMoney);
            groupOrderList.add(groupOrder);
        }

        GroupOrder groupOrder = null;
        List<VehicleForegiftOrder> vehicleForegiftOrderList = new ArrayList();
        if (param.vehicleForegiftOrderIds != null && param.vehicleForegiftOrderIds.length > 0) {
//            if (customerVehicleInfo == null) {
//                return RestResult.result(RespCode.CODE_2.getValue(), "客户没有租车押金，不能申请退还租车押金");
//            }
            for (String id : param.vehicleForegiftOrderIds) {
                groupOrder = groupOrderService.findByVehicleForegiftId(id);
                if (groupOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租车组合订单不存在");
                }
                if (groupOrder.getStatus() != GroupOrder.Status.PAY_OK.getValue()) {
                    return RestResult.result(RespCode.CODE_1.getValue(), "客户租车组合订单(" + groupOrder.getId() + ")状态为" + groupOrder.getStatusName() + "，不能申请退款");
                }
                VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderService.find(id);
                if (vehicleForegiftOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租车押金订单(" + id + ")不存在");
                } else {
                    if (vehicleForegiftOrder.getStatus() != VehicleForegiftOrder.Status.PAY_OK.getValue()) {
                        return RestResult.result(RespCode.CODE_1.getValue(), "客户租车押金订单(" + id + ")状态为" + vehicleForegiftOrder.getStatusName() + "，不能申请退款");
                    }
                }
                vehicleForegiftOrderList.add(vehicleForegiftOrder);
            }
        }

        List<VehiclePeriodOrder> vehiclePeriodOrderList = new ArrayList();
        if (param.vehiclePacketPeriodOrderIds != null && param.vehiclePacketPeriodOrderIds.length > 0) {
            for (String id : param.vehiclePacketPeriodOrderIds) {
                groupOrder = groupOrderService.findByVehiclePeriodId(id);
                if (groupOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租车组合订单不存在");
                }
                if (groupOrder.getStatus() != GroupOrder.Status.PAY_OK.getValue()) {
                    return RestResult.result(RespCode.CODE_1.getValue(), "客户租车组合订单(" + groupOrder.getId() + ")状态为" + groupOrder.getStatusName() + "，不能申请退款");
                }
                VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderService.find(id);
                if (vehiclePeriodOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租车租金订单(" + id + ")不存在");
                } else {
                    if (vehiclePeriodOrder.getStatus() != VehiclePeriodOrder.Status.NOT_USE.getValue()) {
                        return RestResult.result(RespCode.CODE_1.getValue(), "客户租车押金订单(" + id + ")状态为" + vehiclePeriodOrder.getStatusName() + "，不能申请退款");
                    }
                }
                vehiclePeriodOrderList.add(vehiclePeriodOrder);
            }
        }

        List<CustomerForegiftOrder> customerForegiftOrderList = new ArrayList();
        List<RentForegiftOrder> rentForegiftOrderList = new ArrayList();
        if (param.batteryForegiftOrderIds != null && param.batteryForegiftOrderIds.length > 0) {
            for (String batteryForegiftId : param.batteryForegiftOrderIds) {
                groupOrder = groupOrderService.findByBatteryForegiftId(batteryForegiftId);
                if (groupOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租车组合订单不存在");
                }
                if (groupOrder.getStatus() != GroupOrder.Status.PAY_OK.getValue()) {
                    return RestResult.result(RespCode.CODE_1.getValue(), "客户租车组合订单(" + groupOrder.getId() + ")状态为" + groupOrder.getStatusName() + "，不能申请退款");
                }
                if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                    CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderService.find(batteryForegiftId);
                    if (customerForegiftOrder == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "电池押金订单(" + batteryForegiftId + ")不存在");
                    }else {
                        if (customerForegiftOrder.getStatus() != CustomerForegiftOrder.Status.PAY_OK.getValue()) {
                            return RestResult.result(RespCode.CODE_1.getValue(), "客户电池押金订单(" + batteryForegiftId + ")状态为" + customerForegiftOrder.getStatusName() + "，不能申请退款");
                        }
                    }
                    customerForegiftOrderList.add(customerForegiftOrder);
                } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                    RentForegiftOrder rentForegiftOrder = rentForegiftOrderService.find(batteryForegiftId);
                    if (rentForegiftOrder == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "电池押金订单(" + batteryForegiftId + ")不存在");
                    } else {
                        if (rentForegiftOrder.getStatus() != RentForegiftOrder.Status.PAY_OK.getValue()) {
                            return RestResult.result(RespCode.CODE_1.getValue(), "客户电池押金订单(" + batteryForegiftId + ")状态为" + rentForegiftOrder.getStatusName() + "，不能申请退款");
                        }
                    }
                    rentForegiftOrderList.add(rentForegiftOrder);
                }
            }
        }

        List<PacketPeriodOrder> packetPeriodOrderList = new ArrayList();
        List<RentPeriodOrder> rentPeriodOrderList = new ArrayList();
        if (param.batteryPacketPeriodOrderIds != null && param.batteryPacketPeriodOrderIds.length > 0) {
            for (String batteryPacketPeriodId : param.batteryPacketPeriodOrderIds) {
                groupOrder = groupOrderService.findByBatteryPeriodId(batteryPacketPeriodId);
                if (groupOrder == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租车组合订单不存在");
                }
                if (groupOrder.getStatus() != GroupOrder.Status.PAY_OK.getValue()) {
                    return RestResult.result(RespCode.CODE_1.getValue(), "客户租车组合订单(" + groupOrder.getId() + ")状态为" + groupOrder.getStatusName() + "，不能申请退款");
                }
                if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                    PacketPeriodOrder packetPeriodOrder = packetPeriodOrderService.find(batteryPacketPeriodId);
                    if (packetPeriodOrder == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "电池租金订单(" + batteryPacketPeriodId + ")不存在");
                    }else {
                        if (packetPeriodOrder.getStatus() != PacketPeriodOrder.Status.NOT_USE.getValue()) {
                            return RestResult.result(RespCode.CODE_1.getValue(), "客户电池租金订单(" + batteryPacketPeriodId + ")状态为" + packetPeriodOrder.getStatusName() + "，不能申请退款");
                        }
                        packetPeriodOrderList.add(packetPeriodOrder);
                    }
                } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                    RentPeriodOrder rentPeriodOrder = rentPeriodOrderService.find(batteryPacketPeriodId);
                    if (rentPeriodOrder == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "电池租金订单(" + batteryPacketPeriodId + ")不存在");
                    } else {
                        if (rentPeriodOrder.getStatus() != RentPeriodOrder.Status.NOT_USE.getValue()) {
                            return RestResult.result(RespCode.CODE_1.getValue(), "客户电池租金订单(" + batteryPacketPeriodId + ")状态为" + rentPeriodOrder.getStatusName() + "，不能申请退款");
                        }
                        rentPeriodOrderList.add(rentPeriodOrder);
                    }
                }
            }
        }

        List<CustomerMultiOrder> customerMultiOrderList = new ArrayList();
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
            return customerRefundRecordService.addZcRecordAndUpdateStatus(customer, groupOrderList, vehicleForegiftOrderList, vehiclePeriodOrderList, customerForegiftOrderList, rentForegiftOrderList, packetPeriodOrderList, rentPeriodOrderList, customerMultiOrderList);
        } catch (IllegalArgumentException e) {
            log.error("customerRefundRecordService.addRecordAndUpdateStatus 申请退款异常 error", e);
            log.error("申请退款异常:{}", e.getMessage());
            return RestResult.result(RespCode.CODE_1.getValue(), e.getMessage());
        }

    }

    /**
     * 13-查询待审核退款申请总汇
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
                customerId, CustomerRefundRecord.Status.APPLY.getValue(), null, null, CustomerRefundRecord.Type.ZC.getValue());

        List<NotNullMap> list = new ArrayList();
        if (refundList != null && refundList.size() > 0) {
            int expectRefundMoney = 0;
            for (CustomerRefundRecord record : refundList) {
                if (record.getSourceType() == CustomerRefundRecord.SourceType.ZCGROUP.getValue()) {
                    expectRefundMoney += record.getRefundMoney();
                }
            }

            NotNullMap line = new NotNullMap();
            line.put("orderId", refundList.get(0).getOrderId());
            line.putDateTime("applyTime", refundList.get(0).getCreateTime());
            line.putDateTime("expectFinishTime", DateUtils.addDays(refundList.get(0).getCreateTime(), 7));
            line.put("expectRefundMoney", expectRefundMoney);

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
     * 14-查询退款记录
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
                customerId, param.status, param.offset, param.limit, CustomerRefundRecord.Type.ZC.getValue());

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
     * 15-取消退款申请
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
        for (CustomerRefundRecord customerRefundRecord : refundList) {
            if (customerRefundRecord.getStatus() != CustomerRefundRecord.Status.APPLY.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户退款记录(" + customerRefundRecord.getId() + ")状态为" + customerRefundRecord.getStatusName() + "，不能取消申请退款");
            }
        }

        try {
            //修改订单和退款记录状态
            return customerRefundRecordService.updateZcOrderAndRefundStatus(refundList);
        } catch (IllegalArgumentException e) {
            log.error("customerRefundRecordService.updateZdOrderAndRefundStatus 取消退款申请异常 error", e);
            log.error("申请退款异常:{}", e.getMessage());
            return RestResult.result(RespCode.CODE_1.getValue(), e.getMessage());
        }
    }
}