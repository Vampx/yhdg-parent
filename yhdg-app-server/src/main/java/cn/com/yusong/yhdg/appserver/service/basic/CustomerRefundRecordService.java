package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.CustomerMultiOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.basic.PushMetaDataMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CustomerForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.CustomerRefundRecordMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.InsuranceOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.GroupOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.VehicleForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zc.VehiclePeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentInsuranceOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.InsuranceOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.zc.GroupOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehicleForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zc.VehiclePeriodOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentInsuranceOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.acl.Group;
import java.util.Date;
import java.util.List;

@Service
public class CustomerRefundRecordService extends AbstractService {

    @Autowired
    private CustomerRefundRecordMapper customerRefundRecordMapper;
    @Autowired
    private PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    private CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    private InsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    private CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    private VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    private RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    private RentInsuranceOrderMapper rentInsuranceOrderMapper;
    @Autowired
    private PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    private VehicleForegiftOrderMapper vehicleForegiftOrderMapper;
    @Autowired
    private GroupOrderMapper groupOrderMapper;

    @Transactional(rollbackFor = Throwable.class)
    public RestResult addHdRecordAndUpdateStatus(Customer customer, List<CustomerForegiftOrder> foregiftOrderList, List<PacketPeriodOrder> packetPeriodOrderList, List<InsuranceOrder> insuranceOrderList, List<CustomerMultiOrder> customerMultiOrderList) {
        String orderId = newOrderId(OrderId.OrderIdType.CUSTOMER_REFUND_RECORD);
        int updateRefund = 0;
        Date date = new Date();
        for (CustomerForegiftOrder order : foregiftOrderList) {
            int refundMoney = order.getMoney() + (order.getDeductionTicketMoney()==null?0:order.getDeductionTicketMoney());
            CustomerRefundRecord refundRecord = insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), order.getId(), refundMoney, orderId, CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue(), date);
            updateRefund = customerForegiftOrderMapper.updateRefund(order.getId(), date, null, CustomerForegiftOrder.Status.APPLY_REFUND.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("customerForegiftOrderMapper.updateRefund 退款 押金订单状态出错");
            }
        }
        for (PacketPeriodOrder order : packetPeriodOrderList) {
            updateRefund = packetPeriodOrderMapper.updateRefund(order.getId(), PacketPeriodOrder.Status.APPLY_REFUND.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("packetPeriodOrderMapper.updateRefund 退款 包时段订单状态出错");
            }
            insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), order.getId(), order.getMoney(), orderId, CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue(), date);
        }
        for (InsuranceOrder order : insuranceOrderList) {
            updateRefund = insuranceOrderMapper.updateRefund(order.getId(), InsuranceOrder.Status.APPLY_REFUND.getValue(), InsuranceOrder.Status.PAID.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("insuranceOrderMapper.updateRefund 退款 保险订单状态出错");
            }
            insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), order.getId(), order.getMoney(), orderId, CustomerRefundRecord.SourceType.HDGINSURANCE.getValue(), date);
        }
        for (CustomerMultiOrder order : customerMultiOrderList) {
            updateRefund = customerMultiOrderMapper.updateRefund(order.getId(), CustomerMultiOrder.Status.APPLY_REFUND.getValue(), CustomerMultiOrder.Status.IN_PAY.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("customerMultiOrder.updateRefund 退款 多通道订单状态出错");
            }
            insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), String.format("%d", order.getId()), order.getTotalMoney() - order.getDebtMoney(), orderId, CustomerRefundRecord.SourceType.HDMULTI.getValue(), date);
        }
        if (updateRefund == 1) {
            //申请退款推送
            PushMetaData metaData = new PushMetaData();
            metaData.setSourceId(orderId);
            metaData.setSourceType(PushMessage.SourceType.CUSTOMER_APPLY_FOREGIFT_REFUND_SUCCESS.getValue());
            metaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(metaData);

            if (customerMapper.updateHdRefundStatus(customer.getId(), Customer.HdRefundStatus.APPLY_REFUND.getValue()) == 0) {
                throw new IllegalArgumentException("customerMapper.updateRefundStatus 退款 修改客户状态出错");
            }
            return RestResult.SUCCESS;
        }
        return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult addZdRecordAndUpdateStatus(Customer customer, List<RentForegiftOrder> foregiftOrderList, List<RentPeriodOrder> rentPeriodOrderList, List<RentInsuranceOrder> rentInsuranceOrderList, List<CustomerMultiOrder> customerMultiOrderList) {
        String orderId = newOrderId(OrderId.OrderIdType.CUSTOMER_REFUND_RECORD);
        int updateRefund = 0;
        Date date = new Date();
        for (RentForegiftOrder order : foregiftOrderList) {
            int refundMoney = order.getMoney() + (order.getDeductionTicketMoney()==null?0:order.getDeductionTicketMoney());
            CustomerRefundRecord refundRecord = insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), order.getId(), refundMoney, orderId, CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue(), date);
            updateRefund = rentForegiftOrderMapper.updateRefund(order.getId(), date, null, RentForegiftOrder.Status.APPLY_REFUND.getValue(), RentForegiftOrder.Status.PAY_OK.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("rentForegiftOrderMapper.updateRefund 退款 押金订单状态出错");
            }
        }
        for (RentPeriodOrder order : rentPeriodOrderList) {
            updateRefund = rentPeriodOrderMapper.updateRefund(order.getId(), RentPeriodOrder.Status.APPLY_REFUND.getValue(), RentPeriodOrder.Status.NOT_USE.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("rentPeriodOrderMapper.updateRefund 退款 包时段订单状态出错");
            }
            insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), order.getId(), order.getMoney(), orderId, CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue(), date);
        }
        for (RentInsuranceOrder order : rentInsuranceOrderList) {
            updateRefund = rentInsuranceOrderMapper.updateRefund(order.getId(), RentInsuranceOrder.Status.APPLY_REFUND.getValue(), RentInsuranceOrder.Status.PAID.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("rentInsuranceOrderMapper.updateRefund 退款 保险订单状态出错");
            }
            insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), order.getId(), order.getMoney(), orderId, CustomerRefundRecord.SourceType.ZDINSURANCE.getValue(), date);
        }
        for (CustomerMultiOrder order : customerMultiOrderList) {
            updateRefund = customerMultiOrderMapper.updateRefund(order.getId(), CustomerMultiOrder.Status.APPLY_REFUND.getValue(), CustomerMultiOrder.Status.IN_PAY.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("customerMultiOrder.updateRefund 退款 多通道订单状态出错");
            }
            insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), String.format("%d", order.getId()), order.getTotalMoney() - order.getDebtMoney(), orderId, CustomerRefundRecord.SourceType.ZDMULTI.getValue(), date);
        }
        if (updateRefund == 1) {
            if (customerMapper.updateZdRefundStatus(customer.getId(), Customer.ZdRefundStatus.APPLY_REFUND.getValue()) == 0) {
                throw new IllegalArgumentException("customerMapper.updateRefundStatus 退款 修改客户状态出错");
            }
            return RestResult.SUCCESS;
        }
        return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult addZcRecordAndUpdateStatus(Customer customer, List<GroupOrder> groupOrderList, List<VehicleForegiftOrder> vehicleForegiftOrderList, List<VehiclePeriodOrder> vehiclePeriodOrderList, List<CustomerForegiftOrder> customerForegiftOrderList, List<RentForegiftOrder> rentForegiftOrderList, List<PacketPeriodOrder> packetPeriodOrderList, List<RentPeriodOrder> rentPeriodOrderList, List<CustomerMultiOrder> customerMultiOrderList) {
        String orderId = newOrderId(OrderId.OrderIdType.CUSTOMER_REFUND_RECORD);
        int updateRefund = 0;
        Date date = new Date();
        for (GroupOrder groupOrder : groupOrderList) {
            CustomerRefundRecord refundRecord = insertRecord(customer, groupOrder.getAgentId(), groupOrder.getAgentName(), groupOrder.getAgentCode(), groupOrder.getCustomerId(), groupOrder.getId(), groupOrder.getCanRefundMoney(), orderId, CustomerRefundRecord.SourceType.ZCGROUP.getValue(), date);
            updateRefund = groupOrderMapper.updateRefund(groupOrder.getId(), refundRecord.getCreateTime(), null, GroupOrder.Status.APPLY_REFUND.getValue(), GroupOrder.Status.PAY_OK.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("groupOrderMapper.updateRefund 退款 组合订单状态出错");
            }
        }
        for (VehicleForegiftOrder vehicleForegiftOrder : vehicleForegiftOrderList) {
            updateRefund = vehicleForegiftOrderMapper.updateRefund(vehicleForegiftOrder.getId(), VehicleForegiftOrder.Status.APPLY_REFUND.getValue(), VehicleForegiftOrder.Status.PAY_OK.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("vehicleForegiftOrderMapper.updateRefund 退款 押金订单状态出错");
            }
        }
        for (VehiclePeriodOrder vehiclePeriodOrder : vehiclePeriodOrderList) {
            updateRefund = vehiclePeriodOrderMapper.updateRefund(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.APPLY_REFUND.getValue(), VehiclePeriodOrder.Status.NOT_USE.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("vehiclePeriodOrderMapper.updateRefund 退款 租金订单状态出错");
            }
        }
        for (CustomerForegiftOrder customerForegiftOrder : customerForegiftOrderList) {
            updateRefund = customerForegiftOrderMapper.updateRefund(customerForegiftOrder.getId(), date, null, CustomerForegiftOrder.Status.APPLY_REFUND.getValue(), CustomerForegiftOrder.Status.PAY_OK.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("customerForegiftOrderMapper.updateRefund 退款 押金订单状态出错");
            }
        }
        for (RentForegiftOrder rentForegiftOrder : rentForegiftOrderList) {
            updateRefund = rentForegiftOrderMapper.updateRefund(rentForegiftOrder.getId(), date, null, RentForegiftOrder.Status.APPLY_REFUND.getValue(), RentForegiftOrder.Status.PAY_OK.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("rentForegiftOrderMapper.updateRefund 退款 押金订单状态出错");
            }
        }
        for (PacketPeriodOrder packetPeriodOrder : packetPeriodOrderList) {
            updateRefund = packetPeriodOrderMapper.updateRefund(packetPeriodOrder.getId(), PacketPeriodOrder.Status.APPLY_REFUND.getValue(), PacketPeriodOrder.Status.NOT_USE.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("packetPeriodOrderMapper.updateRefund 退款 押金订单状态出错");
            }
        }
        for (RentPeriodOrder rentPeriodOrder : rentPeriodOrderList) {
            updateRefund = rentPeriodOrderMapper.updateRefund(rentPeriodOrder.getId(), RentPeriodOrder.Status.APPLY_REFUND.getValue(), RentPeriodOrder.Status.NOT_USE.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("rentPeriodOrderMapper.updateRefund 退款 押金订单状态出错");
            }
        }
        for (CustomerMultiOrder order : customerMultiOrderList) {
            updateRefund = customerMultiOrderMapper.updateRefund(order.getId(), CustomerMultiOrder.Status.APPLY_REFUND.getValue(), CustomerMultiOrder.Status.IN_PAY.getValue());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("customerMultiOrder.updateRefund 退款 多通道订单状态出错");
            }
            insertRecord(customer, order.getAgentId(), order.getAgentName(), order.getAgentCode(), order.getCustomerId(), String.format("%d", order.getId()), order.getTotalMoney() - order.getDebtMoney(), orderId, CustomerRefundRecord.SourceType.ZCMULTI.getValue(), date);
        }
        if (updateRefund == 1) {
            return RestResult.SUCCESS;
        }
        return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
    }

    private CustomerRefundRecord insertRecord(Customer customer, Integer agentId, String agentName, String agentCode, Long customerId, String id, Integer money, String orderId, Integer sourceType, Date date) {
        CustomerRefundRecord refundRecord = new CustomerRefundRecord();
        refundRecord.setPartnerId(customer.getPartnerId());
        refundRecord.setAgentId(agentId);
        refundRecord.setAgentName(agentName);
        refundRecord.setAgentCode(agentCode);
        refundRecord.setOrderId(orderId);

        refundRecord.setCustomerId(customerId);
        refundRecord.setMobile(customer.getMobile());
        refundRecord.setFullname(customer.getFullname());
        refundRecord.setSourceType(sourceType);
        refundRecord.setSourceId(id);

        refundRecord.setRefundMoney(money);
        refundRecord.setCreateTime(date);
        refundRecord.setStatus(CustomerRefundRecord.Status.APPLY.getValue());

        customerRefundRecordMapper.insert(refundRecord);

        return refundRecord;
    }

    public List<CustomerRefundRecord> findListByCustomerIdAndStatus(long customerId, Integer status, Integer offset, Integer limit, int type) {
        return customerRefundRecordMapper.findListByCustomerIdAndStatus(customerId, status, offset, limit, type);
    }

    public List<CustomerRefundRecord> findListByorderId(long customerId, String orderId) {
        return customerRefundRecordMapper.findListByorderId(customerId, orderId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult updateHdOrderAndRefundStatus(List<CustomerRefundRecord> refundList) {
        int updateRefund = 0;
        for (CustomerRefundRecord record : refundList) {
            updateRefund = customerRefundRecordMapper.updateRefund(record.getId(), CustomerRefundRecord.Status.CANCEL.getValue(), null, new Date());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("customerRefundRecordMapper.updateRefund 取消退款出错");
            }
            if (record.getSourceType() == CustomerRefundRecord.SourceType.HDGFOREGIFT.getValue()) {
                updateRefund = customerForegiftOrderMapper.updateRefund(record.getSourceId(), record.getRefundTime(), null, CustomerForegiftOrder.Status.PAY_OK.getValue(), null);
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("customerForegiftOrderMapper.updateRefund 取消退款出错");
                }
            } else if (record.getSourceType() == CustomerRefundRecord.SourceType.HDGPACKETPERIOD.getValue()) {
//                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(record.getSourceId());
//                if(packetPeriodOrder.getBeginTime() != null){
//                    updateRefund = packetPeriodOrderMapper.updateRefund(record.getSourceId(), PacketPeriodOrder.Status.USED.getValue(), null);
//                }else {
                    updateRefund = packetPeriodOrderMapper.updateRefund(record.getSourceId(), PacketPeriodOrder.Status.NOT_USE.getValue(), null);
//                }
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("packetPeriodOrderMapper.updateRefund 取消退款出错");
                }
            } else if (record.getSourceType() == CustomerRefundRecord.SourceType.HDGINSURANCE.getValue()) {
                updateRefund = insuranceOrderMapper.updateRefund(record.getSourceId(), InsuranceOrder.Status.PAID.getValue(), null);
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("insuranceOrderMapper.updateRefund 取消退款出错");
                }
            } else if (record.getSourceType() == CustomerRefundRecord.SourceType.HDMULTI.getValue()){
                updateRefund = customerMultiOrderMapper.updateRefund(Long.parseLong(record.getSourceId()), CustomerMultiOrder.Status.IN_PAY.getValue(), null);
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("customerMultiOrderMapper.updateRefund 取消多通道退款出错");
                }
            }
        }
        if (updateRefund == 1) {
            if (customerMapper.updateHdRefundStatus(refundList.get(0).getCustomerId(), Customer.HdRefundStatus.NORMAL.getValue()) == 0) {
                throw new IllegalArgumentException("customerMapper.updateRefundStatus 退款 修改客户状态出错");
            }
            return RestResult.SUCCESS;
        }
        return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult updateZdOrderAndRefundStatus(List<CustomerRefundRecord> refundList) {
        int updateRefund = 0;
        for (CustomerRefundRecord record : refundList) {
            updateRefund = customerRefundRecordMapper.updateRefund(record.getId(), CustomerRefundRecord.Status.CANCEL.getValue(), null, new Date());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("customerRefundRecordMapper.updateRefund 取消退款出错");
            }
            if (record.getSourceType() == CustomerRefundRecord.SourceType.ZDFOREGIFT.getValue()) {
                updateRefund = rentForegiftOrderMapper.updateRefund(record.getSourceId(), record.getRefundTime(), null, CustomerForegiftOrder.Status.PAY_OK.getValue(), null);
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("rentForegiftOrderMapper.updateRefund 取消退款出错");
                }
            } else if (record.getSourceType() == CustomerRefundRecord.SourceType.ZDPACKETPERIOD.getValue()) {
//                PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(record.getSourceId());
//                if(packetPeriodOrder.getBeginTime() != null){
//                    updateRefund = packetPeriodOrderMapper.updateRefund(record.getSourceId(), PacketPeriodOrder.Status.USED.getValue(), null);
//                }else {
                    updateRefund = rentPeriodOrderMapper.updateRefund(record.getSourceId(), PacketPeriodOrder.Status.NOT_USE.getValue(), null);
//                }
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("rentPeriodOrderMapper.updateRefund 取消退款出错");
                }
            } else if (record.getSourceType() == CustomerRefundRecord.SourceType.ZDINSURANCE.getValue()) {
                updateRefund = rentInsuranceOrderMapper.updateRefund(record.getSourceId(), InsuranceOrder.Status.PAID.getValue(), null);
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("rentInsuranceOrderMapper.updateRefund 取消退款出错");
                }
            } else if (record.getSourceType() == CustomerRefundRecord.SourceType.ZDMULTI.getValue()){
                updateRefund = customerMultiOrderMapper.updateRefund(Long.parseLong(record.getSourceId()), CustomerMultiOrder.Status.IN_PAY.getValue(), null);
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("customerMultiOrderMapper.updateRefund 取消多通道退款出错");
                }
            }
        }
        if (updateRefund == 1) {
            if (customerMapper.updateZdRefundStatus(refundList.get(0).getCustomerId(), Customer.ZdRefundStatus.NORMAL.getValue()) == 0) {
                throw new IllegalArgumentException("customerMapper.updateRefundStatus 退款 修改客户状态出错");
            }
            return RestResult.SUCCESS;
        }
        return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult updateZcOrderAndRefundStatus(List<CustomerRefundRecord> refundList) {
        int updateRefund = 0;
        for (CustomerRefundRecord record : refundList) {
            updateRefund = customerRefundRecordMapper.updateRefund(record.getId(), CustomerRefundRecord.Status.CANCEL.getValue(), null, new Date());
            if (updateRefund == 0) {
                throw new IllegalArgumentException("customerRefundRecordMapper.updateRefund 取消退款出错");
            }
            if (record.getSourceType() == CustomerRefundRecord.SourceType.ZCMULTI.getValue()) {
                updateRefund = customerMultiOrderMapper.updateRefund(Long.parseLong(record.getSourceId()), CustomerMultiOrder.Status.IN_PAY.getValue(), CustomerMultiOrder.Status.APPLY_REFUND.getValue());
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("customerMultiOrderMapper.updateRefund 取消退款出错");
                }
            }
            if (record.getSourceType() == CustomerRefundRecord.SourceType.ZCGROUP.getValue()) {
                updateRefund = groupOrderMapper.updateRefund(record.getSourceId(), null, null, GroupOrder.Status.PAY_OK.getValue(), null);
                if (updateRefund == 0) {
                    throw new IllegalArgumentException("groupOrderMapper.updateRefund 取消退款出错");
                }
                GroupOrder groupOrder = groupOrderMapper.find(record.getSourceId());
                if (groupOrder == null) {
                    throw new IllegalArgumentException("组合订单不存在");
                }
                if (StringUtils.isNotEmpty(groupOrder.getVehicleForegiftId())) {
                    VehicleForegiftOrder vehicleForegiftOrder = vehicleForegiftOrderMapper.find(groupOrder.getVehicleForegiftId());
                    if (vehicleForegiftOrder != null && vehicleForegiftOrder.getStatus() == VehicleForegiftOrder.Status.APPLY_REFUND.getValue()) {
                        updateRefund = vehicleForegiftOrderMapper.updateRefund(vehicleForegiftOrder.getId(), VehicleForegiftOrder.Status.PAY_OK.getValue(), VehicleForegiftOrder.Status.APPLY_REFUND.getValue());
                        if (updateRefund == 0) {
                            throw new IllegalArgumentException("vehicleForegiftOrderMapper.updateRefund 取消退款出错");
                        }
                    }
                }
                if (StringUtils.isNotEmpty(groupOrder.getVehiclePeriodId())) {
                    VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderMapper.find(groupOrder.getVehiclePeriodId());
                    if (vehiclePeriodOrder != null && vehiclePeriodOrder.getStatus() == VehiclePeriodOrder.Status.APPLY_REFUND.getValue()) {
                        updateRefund = vehiclePeriodOrderMapper.updateRefund(vehiclePeriodOrder.getId(), VehiclePeriodOrder.Status.NOT_USE.getValue(), VehiclePeriodOrder.Status.APPLY_REFUND.getValue());
                        if (updateRefund == 0) {
                            throw new IllegalArgumentException("vehiclePeriodOrderMapper.updateRefund 取消退款出错");
                        }
                    }
                }
                if (StringUtils.isNotEmpty(groupOrder.getBatteryForegiftId())) {
                    if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                        CustomerForegiftOrder customerForegiftOrder = customerForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
                        if (customerForegiftOrder != null && customerForegiftOrder.getStatus() == CustomerForegiftOrder.Status.APPLY_REFUND.getValue()) {
                            updateRefund = customerForegiftOrderMapper.updateRefund(customerForegiftOrder.getId(), null, null, CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
                            if (updateRefund == 0) {
                                throw new IllegalArgumentException("customerForegiftOrderMapper.updateRefund 取消退款出错");
                            }
                        }
                    } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                        RentForegiftOrder rentForegiftOrder = rentForegiftOrderMapper.find(groupOrder.getBatteryForegiftId());
                        if (rentForegiftOrder != null && rentForegiftOrder.getStatus() == RentForegiftOrder.Status.APPLY_REFUND.getValue()) {
                            updateRefund = rentForegiftOrderMapper.updateRefund(rentForegiftOrder.getId(), null, null, RentForegiftOrder.Status.PAY_OK.getValue(), RentForegiftOrder.Status.APPLY_REFUND.getValue());
                            if (updateRefund == 0) {
                                throw new IllegalArgumentException("rentForegiftOrderMapper.updateRefund 取消退款出错");
                            }
                        }
                    }
                }
                if (StringUtils.isNotEmpty(groupOrder.getBatteryRentId())) {
                    if (groupOrder.getCategory() == Battery.Category.EXCHANGE.getValue()) {
                        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(groupOrder.getBatteryRentId());
                        if (packetPeriodOrder != null && packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.APPLY_REFUND.getValue()) {
                            updateRefund = packetPeriodOrderMapper.updateRefund(packetPeriodOrder.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), PacketPeriodOrder.Status.APPLY_REFUND.getValue());
                            if (updateRefund == 0) {
                                throw new IllegalArgumentException("packetPeriodOrderMapper.updateRefund 取消退款出错");
                            }
                        }
                    } else if (groupOrder.getCategory() == Battery.Category.RENT.getValue()) {
                        RentPeriodOrder rentPeriodOrder = rentPeriodOrderMapper.find(groupOrder.getBatteryRentId());
                        if (rentPeriodOrder != null && rentPeriodOrder.getStatus() == RentPeriodOrder.Status.APPLY_REFUND.getValue()) {
                            updateRefund = rentPeriodOrderMapper.updateRefund(rentPeriodOrder.getId(), RentPeriodOrder.Status.NOT_USE.getValue(), RentPeriodOrder.Status.APPLY_REFUND.getValue());
                            if (updateRefund == 0) {
                                throw new IllegalArgumentException("rentPeriodOrderMapper.updateRefund 取消退款出错");
                            }
                        }
                    }
                }
            }
        }
        if (updateRefund == 1) {
            return RestResult.SUCCESS;
        }
        return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
    }

}
