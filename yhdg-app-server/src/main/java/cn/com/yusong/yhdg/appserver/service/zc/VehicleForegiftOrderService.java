package cn.com.yusong.yhdg.appserver.service.zc;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.persistence.zc.*;
import cn.com.yusong.yhdg.appserver.persistence.zd.CustomerRentInfoMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentForegiftOrderMapper;
import cn.com.yusong.yhdg.appserver.persistence.zd.RentPeriodOrderMapper;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.domain.zd.CustomerRentInfo;
import cn.com.yusong.yhdg.common.domain.zd.RentForegiftOrder;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodOrder;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.exception.BalanceNotEnoughException;
import cn.com.yusong.yhdg.common.exception.CouponTicketNotAvailableException;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class VehicleForegiftOrderService extends AbstractService {

    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    ActivityCustomerMapper activityCustomerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    CustomerMultiOrderMapper customerMultiOrderMapper;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;
    @Autowired
    VehicleForegiftOrderMapper vehicleForegiftOrderMapper;
    @Autowired
    RentPriceMapper rentPriceMapper;
    @Autowired
    VehicleVipPriceMapper vehicleVipPriceMapper;
    @Autowired
    VehicleVipPriceCustomerMapper vehicleVipPriceCustomerMapper;
    @Autowired
    VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    GroupOrderMapper groupOrderMapper;
    @Autowired
    PriceSettingMapper priceSettingMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    RentPeriodOrderMapper rentPeriodOrderMapper;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    IdCardAuthRecordMapper idCardAuthRecordMapper;
    @Autowired
    CustomerRentInfoMapper customerRentInfoMapper;
    @Autowired
    VehicleMapper vehicleMapper;

    public VehicleForegiftOrder find(String orderId) {
        return vehicleForegiftOrderMapper.find(orderId);
    }

    public List<VehicleForegiftOrder> findListByCustomerIdAndStatus(long customerId, int status) {
        return vehicleForegiftOrderMapper.findListByCustomerIdAndStatus(customerId, status);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(long customerId, String shopId, long priceId, int vipPriceId,
                                 int foregiftPrice, int packetPrice, long foregiftTicketId,
                                 long packetTicketId, long deductionTicketId,
                                 ConstEnum.PayType payType) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Shop shop = shopMapper.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        RentPrice rentPrice = null;
        VehicleVipPrice vehicleVipPrice = null;
        if (vipPriceId == 0) {
            rentPrice = rentPriceMapper.find(priceId);
            if (rentPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (rentPrice.getForegiftPrice() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "押金价格不正确，请确认");
            }
        } else if (vipPriceId != 0) {
            vehicleVipPrice = vehicleVipPriceMapper.find(vipPriceId);
            if (vehicleVipPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "VIP套餐设置不存在");
            }
            if (vehicleVipPrice.getForegiftPrice() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "押金价格不正确，请确认");
            }
        }

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(customerId);
        if (customerVehicleInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买租车押金");
        }

        PriceSetting priceSetting = null;
        //根据套餐类型生成换电或租电押金租金订单
        if (vipPriceId == 0) {
            priceSetting = priceSettingMapper.find(rentPrice.getPriceSettingId());
        } else if (vipPriceId != 0) {
            priceSetting = priceSettingMapper.find(vehicleVipPrice.getPriceSettingId().longValue());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买换电电池押金");
            }
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
            if (customerRentInfo != null) {
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买租电电池押金");
            }
        }

        Agent agent = null;
        if (vipPriceId == 0) {
            agent = agentMapper.find(rentPrice.getAgentId());
        } else if (vipPriceId != 0) {
            agent = agentMapper.find(vehicleVipPrice.getAgentId());
        }

        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        String deductionTicketName = null;//抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String packetTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int packetTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int packetMoney = 0; //租金包时段套餐 支付 金额（转换后）

        int dayCount = 0;

        foregiftMoney = foregiftPrice;


        //查询抵扣券优惠券
        CustomerCouponTicket deductionTicket = null;
        if (deductionTicketId != 0) {
            deductionTicket = customerCouponTicketMapper.find(deductionTicketId);
            if (deductionTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有抵扣券");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该抵扣券已使用");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已过期");
            }
            //抵扣券金额
            deductionTicketMoney = deductionTicket.getMoney();
            //抵扣券名称
            deductionTicketName = deductionTicket.getTicketName();

        }

        //查询押金优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (foregiftTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(foregiftTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有押金优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已过期");
            }
            //押金优惠券金额
            foregiftTicketMoney = customerCouponTicket.getMoney();
            //押金优惠券名称
            foregiftTicketName = customerCouponTicket.getTicketName();
        }

        //查询租金优惠券
        CustomerCouponTicket rentCouponTicket = null;
        if (packetTicketId != 0) {
            rentCouponTicket = customerCouponTicketMapper.find(packetTicketId);
            if (rentCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有租金优惠券");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已使用");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已过期");
            }
            //租金优惠券金额
            packetTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            packetTicketName = rentCouponTicket.getTicketName();
        }

        //查询套餐
        if (vipPriceId == 0) {
            if (priceId != 0) {
                if (rentPrice.getRentPrice() != packetPrice) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
                }
                dayCount = rentPrice.getDayCount();

                if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                    packetMoney = packetPrice - packetTicketMoney;
                }
            }
        } else if (vipPriceId != 0) {
            if (vehicleVipPrice.getRentPrice() != packetPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
            dayCount = vehicleVipPrice.getDayCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }


        int realDeductionTicketMoney = 0;// 实际使用抵扣券金额
        int realforegiftTicketMoney = 0;// 实际使用押金优惠券金额

        //押金券不为空 计算抵扣费用
        if (foregiftTicketId != 0 && foregiftTicketMoney > 0) {

            //押金金额大于押金优惠券金额
            if (foregiftMoney >= foregiftTicketMoney) {
                foregiftMoney = foregiftMoney - foregiftTicketMoney;
                realforegiftTicketMoney = foregiftTicketMoney;
            } else {
                realforegiftTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        //抵扣券不为空 计算抵扣费用
        if (deductionTicketId != 0 && deductionTicketMoney > 0) {
            //押金金额大于抵扣券金额
            if (foregiftMoney >= deductionTicketMoney) {
                foregiftMoney = foregiftMoney - deductionTicketMoney;
                realDeductionTicketMoney = deductionTicketMoney;
            } else {
                realDeductionTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        int totalMoney = packetMoney + foregiftMoney;


        VehiclePeriodOrder vehiclePeriodOrder = new VehiclePeriodOrder();
        vehiclePeriodOrder.setModelId(priceSetting.getModelId());
        vehiclePeriodOrder.setShopId(shopId);
        vehiclePeriodOrder.setShopName(shop.getShopName());
        vehiclePeriodOrder.setPartnerId(customer.getPartnerId());
        vehiclePeriodOrder.setAgentId(agent.getId());
        vehiclePeriodOrder.setAgentName(agent.getAgentName());
        vehiclePeriodOrder.setAgentCode(agent.getAgentCode());
        vehiclePeriodOrder.setBatteryType(priceSetting.getBatteryType());

        vehiclePeriodOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER));
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_PAY.getValue());
        vehiclePeriodOrder.setCustomerId(customerId);

        vehiclePeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        vehiclePeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
        vehiclePeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        vehiclePeriodOrder.setCreateTime(new Date());
        vehiclePeriodOrder.setDayCount(dayCount);

        //保存租车套餐租金车辆金额
        if (vipPriceId == 0) {
            vehiclePeriodOrder.setPrice(rentPrice.getVehicleRentPrice() != null ? rentPrice.getVehicleRentPrice() : 0);
            vehiclePeriodOrder.setMoney(rentPrice.getVehicleRentPrice() != null ? rentPrice.getVehicleRentPrice() : 0);
        } else if (vipPriceId != 0) {
            vehiclePeriodOrder.setPrice(vehicleVipPrice.getVehicleRentPrice() != null ? vehicleVipPrice.getVehicleRentPrice() : 0);
            vehiclePeriodOrder.setMoney(vehicleVipPrice.getVehicleRentPrice() != null ? vehicleVipPrice.getVehicleRentPrice() : 0);
        }

        vehiclePeriodOrderMapper.insert(vehiclePeriodOrder);

        VehicleForegiftOrder vehicleForegiftOrder = new VehicleForegiftOrder();
        vehicleForegiftOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_CUSTOMER_FORGIFT_ORDER));
        vehicleForegiftOrder.setModelId(priceSetting.getModelId());
        vehicleForegiftOrder.setShopId(shopId);
        vehicleForegiftOrder.setShopName(shop.getShopName());
        vehicleForegiftOrder.setPartnerId(customer.getPartnerId());
        vehicleForegiftOrder.setAgentId(agent.getId());
        vehicleForegiftOrder.setAgentName(agent.getAgentName());
        vehicleForegiftOrder.setAgentCode(agent.getAgentCode());
        vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.WAIT_PAY.getValue());
        vehicleForegiftOrder.setBatteryType(priceSetting.getBatteryType());

        //保存租车套餐租金车辆金额
        if (vipPriceId == 0) {
            vehicleForegiftOrder.setPrice(rentPrice.getVehicleForegiftPrice() != null ? rentPrice.getVehicleForegiftPrice() : 0);
            vehicleForegiftOrder.setMoney(rentPrice.getVehicleForegiftPrice() != null ? rentPrice.getVehicleForegiftPrice() : 0);
        } else if (vipPriceId != 0) {
            vehicleForegiftOrder.setPrice(vehicleVipPrice.getVehicleForegiftPrice() != null ? vehicleVipPrice.getVehicleForegiftPrice() : 0);
            vehicleForegiftOrder.setMoney(vehicleVipPrice.getVehicleForegiftPrice() != null ? vehicleVipPrice.getVehicleForegiftPrice() : 0);
        }

        vehicleForegiftOrder.setCustomerId(customerId);
        vehicleForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        vehicleForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
        vehicleForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        vehicleForegiftOrder.setCreateTime(new Date());

        vehicleForegiftOrderMapper.insert(vehicleForegiftOrder);


        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
        CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();
        RentForegiftOrder rentForegiftOrder = new RentForegiftOrder();

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {

            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agent.getId());
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setShopId(shopId);
            packetPeriodOrder.setShopName(shop.getShopName());
            packetPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());

            packetPeriodOrder.setCustomerId(customerId);
            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                packetPeriodOrder.setPrice(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
                packetPeriodOrder.setMoney(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
            } else if (vipPriceId != 0) {
                packetPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
                packetPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
            }


            packetPeriodOrderMapper.insert(packetPeriodOrder);

            customerForegiftOrder.setId(newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER));
            customerForegiftOrder.setPartnerId(customer.getPartnerId());
            customerForegiftOrder.setAgentId(agent.getId());
            customerForegiftOrder.setAgentName(agent.getAgentName());
            customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
            customerForegiftOrder.setConsumeDepositBalance(0);
            customerForegiftOrder.setConsumeGiftBalance(0);
            customerForegiftOrder.setShopId(shopId);
            customerForegiftOrder.setShopName(shop.getShopName());
            customerForegiftOrder.setBatteryType(priceSetting.getBatteryType());
            customerForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            //保存租车套餐押金电池金额
            if (vipPriceId == 0) {
                customerForegiftOrder.setPrice(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
                customerForegiftOrder.setMoney(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
            } else if (vipPriceId != 0) {
                customerForegiftOrder.setPrice(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
                customerForegiftOrder.setMoney(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
            }

            customerForegiftOrder.setCustomerId(customerId);
            customerForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            customerForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            customerForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            customerForegiftOrder.setCreateTime(new Date());

            customerForegiftOrderMapper.insert(customerForegiftOrder);

        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {


            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
            rentPeriodOrder.setCustomerId(customerId);
            rentPeriodOrder.setConsumeDepositBalance(0);
            rentPeriodOrder.setConsumeGiftBalance(0);
            rentPeriodOrder.setShopId(shopId);
            rentPeriodOrder.setShopName(shop.getShopName());
            rentPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            rentPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            rentPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            rentPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            rentPeriodOrder.setCreateTime(new Date());
            rentPeriodOrder.setDayCount(dayCount);

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                rentPeriodOrder.setPrice(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
                rentPeriodOrder.setMoney(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
            } else if (vipPriceId != 0) {
                rentPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
                rentPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
            }

            rentPeriodOrderMapper.insert(rentPeriodOrder);

            //清空缓存
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
            memCachedClient.delete(key);

            rentForegiftOrder.setId(newOrderId(OrderId.OrderIdType.RENT_FORGIFT_ORDER));
            rentForegiftOrder.setPartnerId(customer.getPartnerId());
            rentForegiftOrder.setAgentId(agent.getId());
            rentForegiftOrder.setAgentName(agent.getAgentName());
            rentForegiftOrder.setStatus(RentForegiftOrder.Status.WAIT_PAY.getValue());
            rentForegiftOrder.setConsumeDepositBalance(0);
            rentForegiftOrder.setConsumeGiftBalance(0);
            rentForegiftOrder.setShopId(shopId);
            rentForegiftOrder.setShopName(shop.getShopName());
            rentForegiftOrder.setBatteryType(priceSetting.getBatteryType());
            rentForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            //保存租车套餐押金电池金额
            if (vipPriceId == 0) {
                rentForegiftOrder.setPrice(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
                rentForegiftOrder.setMoney(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
            } else if (vipPriceId != 0) {
                rentForegiftOrder.setPrice(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
                rentForegiftOrder.setMoney(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
            }

            rentForegiftOrder.setCustomerId(customerId);
            rentForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            rentForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            rentForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            rentForegiftOrder.setCreateTime(new Date());

            rentForegiftOrderMapper.insert(rentForegiftOrder);
        }

        //生成组合订单
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_GROUP_ORDER));
        groupOrder.setModelId(priceSetting.getModelId());
        groupOrder.setShopId(shopId);
        groupOrder.setShopName(shop.getShopName());
        groupOrder.setPartnerId(customer.getPartnerId());
        groupOrder.setAgentId(agent.getId());
        groupOrder.setAgentName(agent.getAgentName());
        groupOrder.setAgentCode(agent.getAgentCode());
        groupOrder.setBatteryType(priceSetting.getBatteryType());

        groupOrder.setRentPriceId(priceId);
        groupOrder.setVipPriceId(vipPriceId);
        //类型
        groupOrder.setCategory(priceSetting.getCategory());
        groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
        //组合支付 押金+租金
        groupOrder.setPrice(foregiftPrice + packetPrice);
        //抵扣后
        groupOrder.setMoney(foregiftMoney + packetMoney);

        //实际支付金额
        groupOrder.setForegiftMoney(foregiftMoney);
        groupOrder.setRentPeriodMoney(packetMoney);

        //计算押金抵扣比例
        double foregiftRatio = Double.valueOf(AppUtils.toDouble(foregiftMoney,foregiftPrice));
        //计算租金金抵扣比例
        double packetRatio = Double.valueOf(AppUtils.toDouble(packetMoney,packetPrice));

        /*车辆押金*/
        groupOrder.setVehicleForegiftId(vehicleForegiftOrder.getId());
        groupOrder.setVehicleForegiftMoney((int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
        groupOrder.setVehicleForegiftPrice(vehicleForegiftOrder.getMoney());
        /*车辆租金*/
        groupOrder.setVehiclePeriodId(vehiclePeriodOrder.getId());
        groupOrder.setVehiclePeriodMoney((int) (vehiclePeriodOrder.getMoney() * packetRatio));
        groupOrder.setVehiclePeriodPrice(vehiclePeriodOrder.getMoney());

        //车辆租期 电池租期
        if (vipPriceId == 0) {
            groupOrder.setVehicleDayCount(rentPrice.getDayCount());
            groupOrder.setBatteryDayCount(rentPrice.getDayCount());
        } else if (vipPriceId != 0) {
            groupOrder.setVehicleDayCount(vehicleVipPrice.getDayCount());
            groupOrder.setBatteryDayCount(vehicleVipPrice.getDayCount());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            //换电电池押金
            groupOrder.setBatteryForegiftId(customerForegiftOrder.getId());
            groupOrder.setBatteryForegiftMoney(foregiftMoney - (int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
            groupOrder.setBatteryForegiftPrice(foregiftPrice - vehicleForegiftOrder.getMoney());
            //换电电池租金
            groupOrder.setBatteryRentId(packetPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(packetPrice - vehiclePeriodOrder.getMoney());
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            //租电电池押金
            groupOrder.setBatteryForegiftId(rentForegiftOrder.getId());
            groupOrder.setBatteryForegiftMoney(foregiftMoney - (int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
            groupOrder.setBatteryForegiftPrice(foregiftPrice - vehicleForegiftOrder.getMoney());
            //租电电池租金
            groupOrder.setBatteryRentId(rentPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(packetPrice - vehiclePeriodOrder.getMoney());
        }
        //绑定租金优惠券
        groupOrder.setRentTicketMoney(packetPrice - packetMoney);
        groupOrder.setRentTicketName(packetTicketName);
        groupOrder.setRentCouponTicketId(packetTicketId == 0 ? null : packetTicketId);

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            groupOrder.setForegiftCouponTicketId(foregiftTicketId);
            groupOrder.setForegiftTicketName(foregiftTicketName);
            groupOrder.setForegiftTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存抵扣券
        groupOrder.setDeductionTicketMoney(0);
        if (deductionTicketId != 0) {
            groupOrder.setDeductionTicketId(deductionTicketId);
            groupOrder.setDeductionTicketName(deductionTicketName);
            groupOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额
        }

        groupOrder.setCustomerId(customerId);
        groupOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        groupOrder.setPayType(payType.getValue());
        groupOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        groupOrder.setCreateTime(new Date());
        groupOrder.setPayTime(new Date());

        groupOrderMapper.insert(groupOrder);

        String sourceId = groupOrder.getId();

        String memo = String.format("押金:%.2f, 租金:%.2f", foregiftMoney / 100.0, packetMoney / 100.0);
        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customerId);
            weixinPayOrder.setMoney(totalMoney);
            weixinPayOrder.setSourceType(PayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue());
            weixinPayOrder.setSourceId(sourceId);
            weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinPayOrder.setMemo(memo);
            weixinPayOrder.setCreateTime(new Date());
            weixinPayOrderMapper.insert(weixinPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);
        } else if(payType == ConstEnum.PayType.WEIXIN_MP) {
            WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
            weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
            weixinmpPayOrder.setPartnerId(customer.getPartnerId());
            weixinmpPayOrder.setAgentId(customerForegiftOrder.getAgentId());
            weixinmpPayOrder.setCustomerId(customerId);
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setMoney(totalMoney);
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue());
            weixinmpPayOrder.setSourceId(sourceId);
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);
        } else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), sourceId, totalMoney, customerId, AlipayPayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue(), "租车组合订单支付", "租车组合订单支付", memo);
            map.put("foregiftOrderId", vehicleForegiftOrder.getId());
            map.put("packetOrderId", vehiclePeriodOrder == null ? "" : vehiclePeriodOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(customer.getPartnerId(), customerForegiftOrder.getAgentId(), sourceId, totalMoney, customerId, AlipayPayOrder.SourceType.CUSTOMER_RENT_GROUP_MONEY_PAY.getValue(), "租车组合订单支付", "租车组合订单支付", memo);
            map.put("foregiftOrderId", vehicleForegiftOrder.getId());
            map.put("packetOrderId", vehiclePeriodOrder == null ? "" : vehiclePeriodOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(long customerId, String shopId, long priceId, int vipPriceId,
                                 int foregiftPrice, int packetPrice, long foregiftTicketId,
                                 long packetTicketId, long deductionTicketId) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        Shop shop = shopMapper.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        RentPrice rentPrice = null;
        VehicleVipPrice vehicleVipPrice = null;
        if (vipPriceId == 0) {
            rentPrice = rentPriceMapper.find(priceId);
            if (rentPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (rentPrice.getForegiftPrice() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "押金价格不正确，请确认");
            }
        } else if (vipPriceId != 0) {
            vehicleVipPrice = vehicleVipPriceMapper.find(vipPriceId);
            if (vehicleVipPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "VIP套餐设置不存在");
            }

            if (vehicleVipPrice.getForegiftPrice() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "押金价格不正确，请确认");
            }
        }

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(customerId);
        if (customerVehicleInfo != null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户已购买租车押金");
        }

        //根据套餐类型生成换电或租电押金租金订单
        PriceSetting priceSetting = null;
        if (vipPriceId == 0) {
            priceSetting = priceSettingMapper.find(rentPrice.getPriceSettingId());
        } else if (vipPriceId != 0) {
            priceSetting = priceSettingMapper.find(vehicleVipPrice.getPriceSettingId().longValue());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已购买换电电池押金");
            }
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
            if (customerRentInfo != null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已购买租电电池押金");
            }
        }

        Agent agent = null;
        if (vipPriceId == 0) {
            agent = agentMapper.find(rentPrice.getAgentId());
        } else if (vipPriceId != 0) {
            agent = agentMapper.find(vehicleVipPrice.getAgentId());
        }

        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        String deductionTicketName = null;//抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String packetTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int packetTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int packetMoney = 0; //租金包时段套餐 支付 金额（转换后）

        int dayCount = 0;

        foregiftMoney = foregiftPrice;

        //查询抵扣券优惠券
        CustomerCouponTicket deductionTicket = null;
        if (deductionTicketId != 0) {
            deductionTicket = customerCouponTicketMapper.find(deductionTicketId);
            if (deductionTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有抵扣券");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该抵扣券已使用");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已过期");
            }
            //抵扣券金额
            deductionTicketMoney = deductionTicket.getMoney();
            //抵扣券名称
            deductionTicketName = deductionTicket.getTicketName();

        }

        //查询押金优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (foregiftTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(foregiftTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有押金优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已过期");
            }
            //押金优惠券金额
            foregiftTicketMoney = customerCouponTicket.getMoney();
            //押金优惠券名称
            foregiftTicketName = customerCouponTicket.getTicketName();
        }

        //查询租金优惠券
        CustomerCouponTicket rentCouponTicket = null;
        if (packetTicketId != 0) {
            rentCouponTicket = customerCouponTicketMapper.find(packetTicketId);
            if (rentCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有租金优惠券");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已使用");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已过期");
            }
            //租金优惠券金额
            packetTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            packetTicketName = rentCouponTicket.getTicketName();
        }

        //查询套餐
        if (vipPriceId == 0) {
            if (priceId != 0) {
                if (rentPrice.getRentPrice() != packetPrice) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
                }
                dayCount = rentPrice.getDayCount();

                if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                    packetMoney = packetPrice - packetTicketMoney;
                }
            }
        } else if (vipPriceId != 0) {
            if (vehicleVipPrice.getRentPrice() != packetPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
            dayCount = vehicleVipPrice.getDayCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }

        int realDeductionTicketMoney = 0;// 实际使用抵扣券金额
        int realforegiftTicketMoney = 0;// 实际使用押金优惠券金额

        //押金券不为空 计算抵扣费用
        if (foregiftTicketId != 0 && foregiftTicketMoney > 0) {

            //押金金额大于押金优惠券金额
            if (foregiftMoney >= foregiftTicketMoney) {
                foregiftMoney = foregiftMoney - foregiftTicketMoney;
                realforegiftTicketMoney = foregiftTicketMoney;
            } else {
                realforegiftTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        //抵扣券不为空 计算抵扣费用
        if (deductionTicketId != 0 && deductionTicketMoney > 0) {
            //押金金额大于抵扣券金额
            if (foregiftMoney >= deductionTicketMoney) {
                foregiftMoney = foregiftMoney - deductionTicketMoney;
                realDeductionTicketMoney = deductionTicketMoney;
            } else {
                realDeductionTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        int totalMoney = packetMoney + foregiftMoney;

        if ((customer.getBalance() + customer.getGiftBalance()) < totalMoney) {
            return RestResult.result(RespCode.CODE_2.getValue(), "余额不足");
        }

        RestResult restResult = updateCustomerBalance(customer, totalMoney, new ArrayList<Integer>());
        if (restResult.getCode() != RespCode.CODE_0.getValue()) {
            throw new BalanceNotEnoughException();
        }

        if (deductionTicketId != 0 && realDeductionTicketMoney >= 0) {
            if (customerCouponTicketMapper.useTicket(deductionTicketId, new Date(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("抵扣券已被使用或已过期");
            }
        }

        if (foregiftTicketId != 0 && realforegiftTicketMoney >= 0) {
            if (customerCouponTicketMapper.useTicket(foregiftTicketId, new Date(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("押金券已被使用或已过期");
            }
        }

        if (packetTicketId != 0 && packetPrice >= 0) {
            if (customerCouponTicketMapper.useTicket(packetTicketId, new Date(), CustomerCouponTicket.Status.NOT_USER.getValue(), CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("租金券已被使用或已过期");
            }
        }

        //扣款成功获取最新余额
        int balance = customerMapper.find(customerId).getBalance();


        VehiclePeriodOrder vehiclePeriodOrder = new VehiclePeriodOrder();
        vehiclePeriodOrder.setModelId(priceSetting.getModelId());
        vehiclePeriodOrder.setShopId(shopId);
        vehiclePeriodOrder.setShopName(shop.getShopName());
        vehiclePeriodOrder.setPartnerId(customer.getPartnerId());
        vehiclePeriodOrder.setAgentId(agent.getId());
        vehiclePeriodOrder.setAgentName(agent.getAgentName());
        vehiclePeriodOrder.setAgentCode(agent.getAgentCode());
        vehiclePeriodOrder.setBatteryType(priceSetting.getBatteryType());


        vehiclePeriodOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER));
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_USE.getValue());
        vehiclePeriodOrder.setCustomerId(customerId);

        vehiclePeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        vehiclePeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
        vehiclePeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        vehiclePeriodOrder.setCreateTime(new Date());
        vehiclePeriodOrder.setPayTime(new Date());
        vehiclePeriodOrder.setDayCount(dayCount);

        //保存租车套餐租金车辆金额
        if (vipPriceId == 0) {
            vehiclePeriodOrder.setPrice(rentPrice.getVehicleRentPrice() != null ? rentPrice.getVehicleRentPrice() : 0);
            vehiclePeriodOrder.setMoney(rentPrice.getVehicleRentPrice() != null ? rentPrice.getVehicleRentPrice() : 0);
        } else if (vipPriceId != 0) {
            vehiclePeriodOrder.setPrice(vehicleVipPrice.getVehicleRentPrice() != null ? vehicleVipPrice.getVehicleRentPrice() : 0);
            vehiclePeriodOrder.setMoney(vehicleVipPrice.getVehicleRentPrice() != null ? vehicleVipPrice.getVehicleRentPrice() : 0);
        }

        vehiclePeriodOrderMapper.insert(vehiclePeriodOrder);

        if (priceId != 0) {
            //租金赠送
            Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();
            String sourceId = vehiclePeriodOrder.getId();
            Integer sourceType = OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER.getValue();

            super.giveCouponTicket(sourceId, ConstEnum.Category.VEHICLE.getValue(), sourceType, type, dayCount, agent.getId(), CustomerCouponTicket.TicketType.RENT.getValue(), vehiclePeriodOrder.getCustomerMobile());
        }

        VehicleForegiftOrder vehicleForegiftOrder = new VehicleForegiftOrder();
        vehicleForegiftOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_CUSTOMER_FORGIFT_ORDER));
        vehicleForegiftOrder.setModelId(priceSetting.getModelId());
        vehicleForegiftOrder.setShopId(shopId);
        vehicleForegiftOrder.setShopName(shop.getShopName());
        vehicleForegiftOrder.setPartnerId(customer.getPartnerId());
        vehicleForegiftOrder.setAgentId(agent.getId());
        vehicleForegiftOrder.setAgentName(agent.getAgentName());
        vehicleForegiftOrder.setAgentCode(agent.getAgentCode());
        vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.PAY_OK.getValue());
        vehicleForegiftOrder.setBatteryType(priceSetting.getBatteryType());

        //保存租车套餐租金车辆金额
        if (vipPriceId == 0) {
            vehicleForegiftOrder.setPrice(rentPrice.getVehicleForegiftPrice() != null ? rentPrice.getVehicleForegiftPrice() : 0);
            vehicleForegiftOrder.setMoney(rentPrice.getVehicleForegiftPrice() != null ? rentPrice.getVehicleForegiftPrice() : 0);
        } else if (vipPriceId != 0) {
            vehicleForegiftOrder.setPrice(vehicleVipPrice.getVehicleForegiftPrice() != null ? vehicleVipPrice.getVehicleForegiftPrice() : 0);
            vehicleForegiftOrder.setMoney(vehicleVipPrice.getVehicleForegiftPrice() != null ? vehicleVipPrice.getVehicleForegiftPrice() : 0);
        }

        vehicleForegiftOrder.setCustomerId(customerId);
        vehicleForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        vehicleForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
        vehicleForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        vehicleForegiftOrder.setCreateTime(new Date());
        vehicleForegiftOrder.setPayTime(new Date());

        vehicleForegiftOrderMapper.insert(vehicleForegiftOrder);

        //运营商首单赠送押金券
        int count = vehicleForegiftOrderMapper.findCountByCustomerId(vehicleForegiftOrder.getId(), agent.getId(), customer.getId(), VehicleForegiftOrder.Status.WAIT_PAY.getValue());
        if (count == 0) {
            //押金赠送
            Integer type = CustomerCouponTicketGift.Type.BUY_FOREGIFT.getValue();
            String sourceId = vehicleForegiftOrder.getId();
            Integer sourceType = OrderId.OrderIdType.VEHICLE_CUSTOMER_FORGIFT_ORDER.getValue();

            super.giveCouponTicket(sourceId, ConstEnum.Category.VEHICLE.getValue(), sourceType, type, dayCount, agent.getId(), CustomerCouponTicket.TicketType.FOREGIFT.getValue(), customer.getMobile());
        }


        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
        CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();
        RentForegiftOrder rentForegiftOrder = new RentForegiftOrder();

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {

            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agent.getId());
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setShopId(shopId);
            packetPeriodOrder.setShopName(shop.getShopName());
            packetPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            packetPeriodOrder.setCustomerId(customerId);
            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());
            packetPeriodOrder.setPayTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                packetPeriodOrder.setPrice(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
                packetPeriodOrder.setMoney(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
            } else if (vipPriceId != 0) {
                packetPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
                packetPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
            }

            packetPeriodOrderMapper.insert(packetPeriodOrder);

            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买换电电池押金");
            }

            customerForegiftOrder.setId(newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER));
            customerForegiftOrder.setPartnerId(customer.getPartnerId());
            customerForegiftOrder.setAgentId(agent.getId());
            customerForegiftOrder.setAgentName(agent.getAgentName());
            customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.PAY_OK.getValue());
            customerForegiftOrder.setConsumeDepositBalance(0);
            customerForegiftOrder.setConsumeGiftBalance(0);
            customerForegiftOrder.setShopId(shopId);
            customerForegiftOrder.setShopName(shop.getShopName());
            customerForegiftOrder.setBatteryType(priceSetting.getBatteryType());
            customerForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            //保存租车套餐押金电池金额
            if (vipPriceId == 0) {
                customerForegiftOrder.setPrice(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
                customerForegiftOrder.setMoney(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
            } else if (vipPriceId != 0) {
                customerForegiftOrder.setPrice(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
                customerForegiftOrder.setMoney(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
            }

            customerForegiftOrder.setCustomerId(customerId);
            customerForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            customerForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            customerForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            customerForegiftOrder.setCreateTime(new Date());
            customerForegiftOrder.setPayTime(new Date());

            customerForegiftOrderMapper.insert(customerForegiftOrder);

            //更新用户押金
            customerExchangeInfo = new CustomerExchangeInfo();
            customerExchangeInfo.setId(customerId);
            customerExchangeInfo.setAgentId(agent.getId());
            customerExchangeInfo.setBatteryType(priceSetting.getBatteryType());
            customerExchangeInfo.setForegift(customerForegiftOrder.getMoney());
            customerExchangeInfo.setForegiftOrderId(customerForegiftOrder.getId());
            customerExchangeInfo.setBalanceCabinetId(null);
            customerExchangeInfo.setBalanceShopId(customerForegiftOrder.getShopId());
            customerExchangeInfo.setVehicleForegiftFlag(ConstEnum.Flag.TRUE.getValue());
            customerExchangeInfo.setCreateTime(new Date());

            customerExchangeInfoMapper.insert(customerExchangeInfo);

            //回写用户运营商id
            customerMapper.updateAgent(customerForegiftOrder.getCustomerId(), customerForegiftOrder.getAgentId());

            //更新用户换电押金状态
            customerMapper.updateHdForegiftStatus(customerForegiftOrder.getCustomerId(), Customer.HdForegiftStatus.PAID.getValue());

        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {

            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, dayCount), Calendar.DAY_OF_MONTH), -1);

            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_USE.getValue());
            rentPeriodOrder.setBeginTime(beginTime);
            rentPeriodOrder.setEndTime(endTime);
            rentPeriodOrder.setCustomerId(customerId);
            rentPeriodOrder.setConsumeDepositBalance(0);
            rentPeriodOrder.setConsumeGiftBalance(0);
            rentPeriodOrder.setShopId(shopId);
            rentPeriodOrder.setShopName(shop.getShopName());
            rentPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            rentPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            rentPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            rentPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            rentPeriodOrder.setCreateTime(new Date());
            rentPeriodOrder.setPayTime(new Date());
            rentPeriodOrder.setDayCount(dayCount);

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                rentPeriodOrder.setPrice(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
                rentPeriodOrder.setMoney(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
            } else if (vipPriceId != 0) {
                rentPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
                rentPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
            }

            rentPeriodOrderMapper.insert(rentPeriodOrder);

            //清空缓存
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
            memCachedClient.delete(key);

            CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
            if (customerRentInfo != null) {
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买租电电池押金");
            }

            rentForegiftOrder.setId(newOrderId(OrderId.OrderIdType.RENT_FORGIFT_ORDER));
            rentForegiftOrder.setPartnerId(customer.getPartnerId());
            rentForegiftOrder.setAgentId(agent.getId());
            rentForegiftOrder.setAgentName(agent.getAgentName());
            rentForegiftOrder.setStatus(RentForegiftOrder.Status.PAY_OK.getValue());
            rentForegiftOrder.setConsumeDepositBalance(0);
            rentForegiftOrder.setConsumeGiftBalance(0);
            rentForegiftOrder.setShopId(shopId);
            rentForegiftOrder.setShopName(shop.getShopName());
            rentForegiftOrder.setBatteryType(priceSetting.getBatteryType());
            rentForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());

            //保存租车套餐押金电池金额
            if (vipPriceId == 0) {
                rentForegiftOrder.setPrice(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
                rentForegiftOrder.setMoney(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
            } else if (vipPriceId != 0) {
                rentForegiftOrder.setPrice(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
                rentForegiftOrder.setMoney(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
            }

            rentForegiftOrder.setCustomerId(customerId);
            rentForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            rentForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            rentForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            rentForegiftOrder.setCreateTime(new Date());
            rentForegiftOrder.setPayTime(new Date());

            rentForegiftOrderMapper.insert(rentForegiftOrder);

            //更新用户押金
            customerRentInfo = new CustomerRentInfo();
            customerRentInfo.setId(customerId);
            customerRentInfo.setAgentId(agent.getId());
            customerRentInfo.setBatteryType(priceSetting.getBatteryType());
            customerRentInfo.setForegift(rentForegiftOrder.getMoney());
            customerRentInfo.setForegiftOrderId(rentForegiftOrder.getId());
            customerRentInfo.setBalanceShopId(rentForegiftOrder.getShopId());
            customerRentInfo.setVehicleForegiftFlag(ConstEnum.Flag.TRUE.getValue());
            customerRentInfo.setCreateTime(new Date());

            customerRentInfoMapper.insert(customerRentInfo);

            //回写用户运营商id
            customerMapper.updateAgent(rentForegiftOrder.getCustomerId(), rentForegiftOrder.getAgentId());

            //更新客户租电押金状态
            customerMapper.updateZdForegiftStatus(rentForegiftOrder.getCustomerId(), Customer.ZdForegiftStatus.PAID.getValue());
        }

        //生成组合订单
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_GROUP_ORDER));
        groupOrder.setModelId(priceSetting.getModelId());
        groupOrder.setShopId(shopId);
        groupOrder.setShopName(shop.getShopName());
        groupOrder.setPartnerId(customer.getPartnerId());
        groupOrder.setAgentId(agent.getId());
        groupOrder.setAgentName(agent.getAgentName());
        groupOrder.setAgentCode(agent.getAgentCode());
        groupOrder.setBatteryType(priceSetting.getBatteryType());

        groupOrder.setRentPriceId(priceId);
        groupOrder.setVipPriceId(vipPriceId);
        //类型
        groupOrder.setCategory(priceSetting.getCategory());
        groupOrder.setStatus(GroupOrder.Status.PAY_OK.getValue());
        //组合支付 押金+租金
        groupOrder.setPrice(foregiftPrice + packetPrice);
        //抵扣后
        groupOrder.setMoney(foregiftMoney + packetMoney);

        //实际支付金额
        groupOrder.setForegiftMoney(foregiftMoney);
        groupOrder.setRentPeriodMoney(packetMoney);

        //计算押金抵扣比例
        double foregiftRatio = Double.valueOf(AppUtils.toDouble(foregiftMoney,foregiftPrice));
        //计算租金金抵扣比例
        double packetRatio = Double.valueOf(AppUtils.toDouble(packetMoney,packetPrice));

        /*车辆押金*/
        groupOrder.setVehicleForegiftId(vehicleForegiftOrder.getId());
        groupOrder.setVehicleForegiftMoney((int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
        groupOrder.setVehicleForegiftPrice(vehicleForegiftOrder.getMoney());
        /*车辆租金*/
        groupOrder.setVehiclePeriodId(vehiclePeriodOrder.getId());
        groupOrder.setVehiclePeriodMoney((int) (vehiclePeriodOrder.getMoney() * packetRatio));
        groupOrder.setVehiclePeriodPrice(vehiclePeriodOrder.getMoney());

        //车辆租期 电池租期
        if (vipPriceId == 0) {
            groupOrder.setVehicleDayCount(rentPrice.getDayCount());
            groupOrder.setBatteryDayCount(rentPrice.getDayCount());
        } else if (vipPriceId != 0) {
            groupOrder.setVehicleDayCount(vehicleVipPrice.getDayCount());
            groupOrder.setBatteryDayCount(vehicleVipPrice.getDayCount());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            //换电电池押金
            groupOrder.setBatteryForegiftId(customerForegiftOrder.getId());
            groupOrder.setBatteryForegiftMoney(foregiftMoney - (int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
            groupOrder.setBatteryForegiftPrice(foregiftPrice - vehicleForegiftOrder.getMoney());
            //换电电池租金
            groupOrder.setBatteryRentId(packetPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(packetPrice - vehiclePeriodOrder.getMoney());
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            //租电电池押金
            groupOrder.setBatteryForegiftId(rentForegiftOrder.getId());
            groupOrder.setBatteryForegiftMoney(foregiftMoney - (int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
            groupOrder.setBatteryForegiftPrice(foregiftPrice - vehicleForegiftOrder.getMoney());
            //租电电池租金
            groupOrder.setBatteryRentId(rentPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(packetPrice - vehiclePeriodOrder.getMoney());
        }

        //绑定租金优惠券
        groupOrder.setRentTicketMoney(packetPrice - packetMoney);
        groupOrder.setRentTicketName(packetTicketName);
        groupOrder.setRentCouponTicketId(packetTicketId == 0 ? null : packetTicketId);

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            groupOrder.setForegiftCouponTicketId(foregiftTicketId);
            groupOrder.setForegiftTicketName(foregiftTicketName);
            groupOrder.setForegiftTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存抵扣券
        groupOrder.setDeductionTicketMoney(0);
        if (deductionTicketId != 0) {
            groupOrder.setDeductionTicketId(deductionTicketId);
            groupOrder.setDeductionTicketName(deductionTicketName);
            groupOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额
        }

        groupOrder.setCustomerId(customerId);
        groupOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        groupOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        groupOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        groupOrder.setCreateTime(new Date());
        groupOrder.setPayTime(new Date());

        if (groupOrder.getMoney() > 0) {
            CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
            inOutMoney.setCustomerId(groupOrder.getCustomerId());
            inOutMoney.setMoney(-groupOrder.getMoney());
            inOutMoney.setBizType(CustomerInOutMoney.BizType.OUT_CUSTOMER_VEHICLE_GROUP_PAY.getValue());
            inOutMoney.setBizId(groupOrder.getId());
            inOutMoney.setType(CustomerInOutMoney.Type.OUT.getValue());
            inOutMoney.setBalance(balance);
            inOutMoney.setCreateTime(new Date());
            customerInOutMoneyMapper.insert(inOutMoney);
        }

        groupOrderMapper.insert(groupOrder);

        //更新租车关联用户信息
        customerVehicleInfo = new CustomerVehicleInfo();
        customerVehicleInfo.setId(customerId);
        customerVehicleInfo.setAgentId(agent.getId());
        customerVehicleInfo.setForegift(vehicleForegiftOrder.getMoney());
        customerVehicleInfo.setForegiftOrderId(vehicleForegiftOrder.getId());
        customerVehicleInfo.setRentPriceId(priceId);
        customerVehicleInfo.setVipPriceId(vipPriceId);
        customerVehicleInfo.setCategory(priceSetting.getCategory());
        customerVehicleInfo.setModelId(priceSetting.getModelId());
        customerVehicleInfo.setBalanceShopId(shopId);
        customerVehicleInfo.setBatteryType(priceSetting.getBatteryType());
        customerVehicleInfo.setCreateTime(new Date());

        customerVehicleInfoMapper.insert(customerVehicleInfo);

        //回写用户运营商id
        customerMapper.updateAgent(vehicleForegiftOrder.getCustomerId(), vehicleForegiftOrder.getAgentId());

        //余额支付押金成功后，推送
        PushMetaData metaData = new PushMetaData();
        metaData.setSourceId(vehicleForegiftOrder.getId());
        metaData.setSourceType(PushMessage.SourceType.CUSTOMER_FOREGIFT_PAY_SUCCESS.getValue());
        metaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(metaData);

        //套餐充值成功,推送
        if(vehiclePeriodOrder != null){
            metaData = new PushMetaData();
            metaData.setSourceId(vehiclePeriodOrder.getId());
            metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
            metaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(metaData);
        }

        //身份证认证记录状态是空 或者 未生成
        if (customer.getIdCardAuthRecordStatus() == null || customer.getIdCardAuthRecordStatus() == Customer.IdCardAuthRecordStatus.NOT.getValue()) {
            String idCardAuthMoneyValue = findConfigValue(ConstEnum.SystemConfigKey.ID_CARD_AUTH_MONEY.getValue());
            int idCardAuthMoney = 0;
            if (StringUtils.isNotEmpty(idCardAuthMoneyValue)) {
                idCardAuthMoney = (int) (Float.parseFloat(idCardAuthMoneyValue) * 100);
            }

            if (idCardAuthMoney > 0) {
                IdCardAuthRecord record = new IdCardAuthRecord();
                record.setAgentId(agent.getId());
                record.setAgentName(agent.getAgentName());
                record.setAgentCode(agent.getAgentCode());
                record.setCustomerId(customer.getId());
                record.setMobile(customer.getMobile());
                record.setFullname(customer.getFullname());
                record.setMoney(idCardAuthMoney);
                record.setStatus(ConstEnum.PayStatus.NO_PAY.getValue());
                record.setCreateTime(new Date());
                idCardAuthRecordMapper.insert(record);
                customerMapper.updateIdCardAuthRecordStats(customer.getId(), Customer.IdCardAuthRecordStatus.CREATED.getValue());
            } else {
                customerMapper.updateIdCardAuthRecordStats(customer.getId(), Customer.IdCardAuthRecordStatus.AVOID.getValue());
            }
        }

        //客户购买押金租金消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customerId);
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.FOREGIFT_RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租车，押金: ${foregiftMoney}，租金: ${packetMoney}。",
                new String[]{"${foregiftMoney}", "${packetMoney}"},
                new String[]{String.format("%.2f元", 1d * foregiftMoney / 100.0), String.format("%.2f元", 1d * packetMoney / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);


        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByMulti(long customerId, String shopId, long priceId, int vipPriceId,
                                 int foregiftPrice, int packetPrice, long foregiftTicketId,
                                 long packetTicketId, long deductionTicketId) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }
        Shop shop = shopMapper.find(shopId);
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        RentPrice rentPrice = null;
        VehicleVipPrice vehicleVipPrice = null;
        if (vipPriceId == 0) {
            rentPrice = rentPriceMapper.find(priceId);
            if (rentPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (rentPrice.getForegiftPrice() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "押金价格不正确，请确认");
            }
        } else if (vipPriceId != 0) {
            vehicleVipPrice = vehicleVipPriceMapper.find(vipPriceId);
            if (vehicleVipPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "VIP套餐设置不存在");
            }
            if (vehicleVipPrice.getForegiftPrice() != foregiftPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "押金价格不正确，请确认");
            }
        }

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(customerId);
        if (customerVehicleInfo != null) {
            return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买租车押金");
        }

        PriceSetting priceSetting = null;
        //根据套餐类型生成换电或租电押金租金订单
        if (vipPriceId == 0) {
            priceSetting = priceSettingMapper.find(rentPrice.getPriceSettingId());
        } else if (vipPriceId != 0) {
            priceSetting = priceSettingMapper.find(vehicleVipPrice.getPriceSettingId().longValue());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
            if (customerExchangeInfo != null) {
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买换电电池押金");
            }
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customerId);
            if (customerRentInfo != null) {
                return RestResult.result(RespCode.CODE_1.getValue(), "用户已购买租电电池押金");
            }
        }

        Agent agent = null;
        if (vipPriceId == 0) {
            agent = agentMapper.find(rentPrice.getAgentId());
        } else if (vipPriceId != 0) {
            agent = agentMapper.find(vehicleVipPrice.getAgentId());
        }

        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        String deductionTicketName = null;//抵扣券名称
        String foregiftTicketName = null;//押金优惠券名称
        String packetTicketName = null;//租金优惠券名称

        int deductionTicketMoney = 0; //抵扣券金额
        int foregiftTicketMoney = 0;//押金优惠券金额
        int packetTicketMoney = 0; //租金优惠券金额

        int foregiftMoney = 0;//协议提交上来的实际押金金额（转换后）
        int packetMoney = 0; //租金包时段套餐 支付 金额（转换后）

        int dayCount = 0;

        foregiftMoney = foregiftPrice;


        //查询抵扣券优惠券
        CustomerCouponTicket deductionTicket = null;
        if (deductionTicketId != 0) {
            deductionTicket = customerCouponTicketMapper.find(deductionTicketId);
            if (deductionTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有抵扣券");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该抵扣券已使用");
            }
            if (deductionTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该电池抵扣券已过期");
            }
            //抵扣券金额
            deductionTicketMoney = deductionTicket.getMoney();
            //抵扣券名称
            deductionTicketName = deductionTicket.getTicketName();

        }

        //查询押金优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (foregiftTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(foregiftTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有押金优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该押金优惠券已过期");
            }
            //押金优惠券金额
            foregiftTicketMoney = customerCouponTicket.getMoney();
            //押金优惠券名称
            foregiftTicketName = customerCouponTicket.getTicketName();
        }

        //查询租金优惠券
        CustomerCouponTicket rentCouponTicket = null;
        if (packetTicketId != 0) {
            rentCouponTicket = customerCouponTicketMapper.find(packetTicketId);
            if (rentCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有租金优惠券");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已使用");
            }
            if (rentCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该租金优惠券已过期");
            }
            //租金优惠券金额
            packetTicketMoney = rentCouponTicket.getMoney();
            //租金优惠券名称
            packetTicketName = rentCouponTicket.getTicketName();
        }

        //查询套餐
        if (vipPriceId == 0) {
            if (priceId != 0) {
                if (rentPrice.getRentPrice() != packetPrice) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
                }
                dayCount = rentPrice.getDayCount();

                if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                    packetMoney = packetPrice - packetTicketMoney;
                }
            }
        } else if (vipPriceId != 0) {
            if (vehicleVipPrice.getRentPrice() != packetPrice) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
            dayCount = vehicleVipPrice.getDayCount();

            if (packetPrice >= packetTicketMoney) { //套餐价格大于 租金优惠券金额
                packetMoney = packetPrice - packetTicketMoney;
            }
        }


        int realDeductionTicketMoney = 0;// 实际使用抵扣券金额
        int realforegiftTicketMoney = 0;// 实际使用押金优惠券金额

        //押金券不为空 计算抵扣费用
        if (foregiftTicketId != 0 && foregiftTicketMoney > 0) {

            //押金金额大于押金优惠券金额
            if (foregiftMoney >= foregiftTicketMoney) {
                foregiftMoney = foregiftMoney - foregiftTicketMoney;
                realforegiftTicketMoney = foregiftTicketMoney;
            } else {
                realforegiftTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        //抵扣券不为空 计算抵扣费用
        if (deductionTicketId != 0 && deductionTicketMoney > 0) {
            //押金金额大于抵扣券金额
            if (foregiftMoney >= deductionTicketMoney) {
                foregiftMoney = foregiftMoney - deductionTicketMoney;
                realDeductionTicketMoney = deductionTicketMoney;
            } else {
                realDeductionTicketMoney = foregiftMoney;
                foregiftMoney = 0;
            }
        }

        int totalMoney = packetMoney + foregiftMoney;

        //创建多通道支付订单
        CustomerMultiOrder customerMultiOrder = createCustomerMultiOrder(agent, customer, totalMoney, CustomerMultiOrder.Type.ZC.getValue());
        int num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());

        VehiclePeriodOrder vehiclePeriodOrder = new VehiclePeriodOrder();
        vehiclePeriodOrder.setModelId(priceSetting.getModelId());
        vehiclePeriodOrder.setShopId(shopId);
        vehiclePeriodOrder.setShopName(shop.getShopName());
        vehiclePeriodOrder.setPartnerId(customer.getPartnerId());
        vehiclePeriodOrder.setAgentId(agent.getId());
        vehiclePeriodOrder.setAgentName(agent.getAgentName());
        vehiclePeriodOrder.setAgentCode(agent.getAgentCode());
        vehiclePeriodOrder.setBatteryType(priceSetting.getBatteryType());


        vehiclePeriodOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER));
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_PAY.getValue());
        vehiclePeriodOrder.setCustomerId(customerId);

        vehiclePeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        vehiclePeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
        vehiclePeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        vehiclePeriodOrder.setCreateTime(new Date());
        vehiclePeriodOrder.setDayCount(dayCount);

        //保存租车套餐租金车辆金额
        if (vipPriceId == 0) {
            vehiclePeriodOrder.setPrice(rentPrice.getVehicleRentPrice() != null ? rentPrice.getVehicleRentPrice() : 0);
            vehiclePeriodOrder.setMoney(rentPrice.getVehicleRentPrice() != null ? rentPrice.getVehicleRentPrice() : 0);
        } else if (vipPriceId != 0) {
            vehiclePeriodOrder.setPrice(vehicleVipPrice.getVehicleRentPrice() != null ? vehicleVipPrice.getVehicleRentPrice() : 0);
            vehiclePeriodOrder.setMoney(vehicleVipPrice.getVehicleRentPrice() != null ? vehicleVipPrice.getVehicleRentPrice() : 0);
        }

        vehiclePeriodOrderMapper.insert(vehiclePeriodOrder);

        VehicleForegiftOrder vehicleForegiftOrder = new VehicleForegiftOrder();
        vehicleForegiftOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_CUSTOMER_FORGIFT_ORDER));
        vehicleForegiftOrder.setModelId(priceSetting.getModelId());
        vehicleForegiftOrder.setShopId(shopId);
        vehicleForegiftOrder.setShopName(shop.getShopName());
        vehicleForegiftOrder.setPartnerId(customer.getPartnerId());
        vehicleForegiftOrder.setAgentId(agent.getId());
        vehicleForegiftOrder.setAgentName(agent.getAgentName());
        vehicleForegiftOrder.setAgentCode(agent.getAgentCode());
        vehicleForegiftOrder.setStatus(VehicleForegiftOrder.Status.WAIT_PAY.getValue());
        vehicleForegiftOrder.setBatteryType(priceSetting.getBatteryType());


        //保存租车套餐租金车辆金额
        if (vipPriceId == 0) {
            vehicleForegiftOrder.setPrice(rentPrice.getVehicleForegiftPrice() != null ? rentPrice.getVehicleForegiftPrice() : 0);
            vehicleForegiftOrder.setMoney(rentPrice.getVehicleForegiftPrice() != null ? rentPrice.getVehicleForegiftPrice() : 0);
        } else if (vipPriceId != 0) {
            vehicleForegiftOrder.setPrice(vehicleVipPrice.getVehicleForegiftPrice() != null ? vehicleVipPrice.getVehicleForegiftPrice() : 0);
            vehicleForegiftOrder.setMoney(vehicleVipPrice.getVehicleForegiftPrice() != null ? vehicleVipPrice.getVehicleForegiftPrice() : 0);
        }

        vehicleForegiftOrder.setCustomerId(customerId);
        vehicleForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        vehicleForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
        vehicleForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        vehicleForegiftOrder.setCreateTime(new Date());

        vehicleForegiftOrderMapper.insert(vehicleForegiftOrder);

        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
        CustomerForegiftOrder customerForegiftOrder = new CustomerForegiftOrder();
        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();
        RentForegiftOrder rentForegiftOrder = new RentForegiftOrder();

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {

            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agent.getId());
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setShopId(shopId);
            packetPeriodOrder.setShopName(shop.getShopName());
            packetPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            packetPeriodOrder.setCustomerId(customerId);
            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                packetPeriodOrder.setPrice(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
                packetPeriodOrder.setMoney(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
            } else if (vipPriceId != 0) {
                packetPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
                packetPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
            }


            packetPeriodOrderMapper.insert(packetPeriodOrder);

            customerForegiftOrder.setId(newOrderId(OrderId.OrderIdType.CUSTOMER_FORGIFT_ORDER));
            customerForegiftOrder.setPartnerId(customer.getPartnerId());
            customerForegiftOrder.setAgentId(agent.getId());
            customerForegiftOrder.setAgentName(agent.getAgentName());
            customerForegiftOrder.setStatus(CustomerForegiftOrder.Status.WAIT_PAY.getValue());
            customerForegiftOrder.setConsumeDepositBalance(0);
            customerForegiftOrder.setConsumeGiftBalance(0);
            customerForegiftOrder.setShopId(shopId);
            customerForegiftOrder.setShopName(shop.getShopName());
            customerForegiftOrder.setBatteryType(priceSetting.getBatteryType());
            customerForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            //保存租车套餐押金电池金额
            if (vipPriceId == 0) {
                customerForegiftOrder.setPrice(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
                customerForegiftOrder.setMoney(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
            } else if (vipPriceId != 0) {
                customerForegiftOrder.setPrice(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
                customerForegiftOrder.setMoney(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
            }

            customerForegiftOrder.setCustomerId(customerId);
            customerForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            customerForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            customerForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            customerForegiftOrder.setCreateTime(new Date());

            customerForegiftOrderMapper.insert(customerForegiftOrder);

        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {

            Date beginTime = new Date();
            Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, dayCount), Calendar.DAY_OF_MONTH), -1);

            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
            rentPeriodOrder.setBeginTime(beginTime);
            rentPeriodOrder.setEndTime(endTime);
            rentPeriodOrder.setCustomerId(customerId);
            rentPeriodOrder.setConsumeDepositBalance(0);
            rentPeriodOrder.setConsumeGiftBalance(0);
            rentPeriodOrder.setShopId(shopId);
            rentPeriodOrder.setShopName(shop.getShopName());
            rentPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            rentPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            rentPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            rentPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            rentPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            rentPeriodOrder.setCreateTime(new Date());
            rentPeriodOrder.setDayCount(dayCount);

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                rentPeriodOrder.setPrice(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
                rentPeriodOrder.setMoney(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
            } else if (vipPriceId != 0) {
                rentPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
                rentPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
            }

            rentPeriodOrderMapper.insert(rentPeriodOrder);

            //清空缓存
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
            memCachedClient.delete(key);

            rentForegiftOrder.setId(newOrderId(OrderId.OrderIdType.RENT_FORGIFT_ORDER));
            rentForegiftOrder.setPartnerId(customer.getPartnerId());
            rentForegiftOrder.setAgentId(agent.getId());
            rentForegiftOrder.setAgentName(agent.getAgentName());
            rentForegiftOrder.setStatus(RentForegiftOrder.Status.WAIT_PAY.getValue());
            rentForegiftOrder.setConsumeDepositBalance(0);
            rentForegiftOrder.setConsumeGiftBalance(0);
            rentForegiftOrder.setShopId(shopId);
            rentForegiftOrder.setShopName(shop.getShopName());
            rentForegiftOrder.setBatteryType(priceSetting.getBatteryType());
            rentForegiftOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());


            //保存租车套餐押金电池金额
            if (vipPriceId == 0) {
                rentForegiftOrder.setPrice(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
                rentForegiftOrder.setMoney(rentPrice.getBatteryForegiftPrice() != null ? rentPrice.getBatteryForegiftPrice() : 0);
            } else if (vipPriceId != 0) {
                rentForegiftOrder.setPrice(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
                rentForegiftOrder.setMoney(vehicleVipPrice.getBatteryForegiftPrice() != null ? vehicleVipPrice.getBatteryForegiftPrice() : 0);
            }

            rentForegiftOrder.setCustomerId(customerId);
            rentForegiftOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            rentForegiftOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            rentForegiftOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            rentForegiftOrder.setCreateTime(new Date());

            rentForegiftOrderMapper.insert(rentForegiftOrder);
        }

        //生成组合订单
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_GROUP_ORDER));
        groupOrder.setModelId(priceSetting.getModelId());
        groupOrder.setShopId(shopId);
        groupOrder.setShopName(shop.getShopName());
        groupOrder.setPartnerId(customer.getPartnerId());
        groupOrder.setAgentId(agent.getId());
        groupOrder.setAgentName(agent.getAgentName());
        groupOrder.setAgentCode(agent.getAgentCode());
        groupOrder.setBatteryType(priceSetting.getBatteryType());

        groupOrder.setRentPriceId(priceId);
        groupOrder.setVipPriceId(vipPriceId);
        //类型
        groupOrder.setCategory(priceSetting.getCategory());
        groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
        //组合支付 押金+租金
        groupOrder.setPrice(foregiftPrice + packetPrice);
        //抵扣后
        groupOrder.setMoney(foregiftMoney + packetMoney);

        //实际支付金额
        groupOrder.setForegiftMoney(foregiftMoney);
        groupOrder.setRentPeriodMoney(packetMoney);

        //计算押金抵扣比例
        double foregiftRatio = Double.valueOf(AppUtils.toDouble(foregiftMoney,foregiftPrice));
        //计算租金金抵扣比例
        double packetRatio = Double.valueOf(AppUtils.toDouble(packetMoney,packetPrice));

        /*车辆押金*/
        groupOrder.setVehicleForegiftId(vehicleForegiftOrder.getId());
        groupOrder.setVehicleForegiftMoney((int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
        groupOrder.setVehicleForegiftPrice(vehicleForegiftOrder.getMoney());
        /*车辆租金*/
        groupOrder.setVehiclePeriodId(vehiclePeriodOrder.getId());
        groupOrder.setVehiclePeriodMoney((int) (vehiclePeriodOrder.getMoney() * packetRatio));
        groupOrder.setVehiclePeriodPrice(vehiclePeriodOrder.getMoney());

        //车辆租期 电池租期
        if (vipPriceId == 0) {
            groupOrder.setVehicleDayCount(rentPrice.getDayCount());
            groupOrder.setBatteryDayCount(rentPrice.getDayCount());
        } else if (vipPriceId != 0) {
            groupOrder.setVehicleDayCount(vehicleVipPrice.getDayCount());
            groupOrder.setBatteryDayCount(vehicleVipPrice.getDayCount());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            //换电电池押金
            groupOrder.setBatteryForegiftId(customerForegiftOrder.getId());
            groupOrder.setBatteryForegiftMoney(foregiftMoney - (int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
            groupOrder.setBatteryForegiftPrice(foregiftPrice - vehicleForegiftOrder.getMoney());
            //换电电池租金
            groupOrder.setBatteryRentId(packetPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(packetPrice - vehiclePeriodOrder.getMoney());
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            //租电电池押金
            groupOrder.setBatteryForegiftId(rentForegiftOrder.getId());
            groupOrder.setBatteryForegiftMoney(foregiftMoney - (int) (vehicleForegiftOrder.getMoney() * foregiftRatio));
            groupOrder.setBatteryForegiftPrice(foregiftPrice - vehicleForegiftOrder.getMoney());
            //租电电池租金
            groupOrder.setBatteryRentId(rentPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(packetPrice - vehiclePeriodOrder.getMoney());
        }
        //绑定租金优惠券
        groupOrder.setRentTicketMoney(packetPrice - packetMoney);
        groupOrder.setRentTicketName(packetTicketName);
        groupOrder.setRentCouponTicketId(packetTicketId == 0 ? null : packetTicketId);

        //保存押金优惠券
        if (foregiftTicketId != 0) {
            groupOrder.setForegiftCouponTicketId(foregiftTicketId);
            groupOrder.setForegiftTicketName(foregiftTicketName);
            groupOrder.setForegiftTicketMoney(realforegiftTicketMoney);// 实际使用押金优惠券金额
        }

        //保存抵扣券
        groupOrder.setDeductionTicketMoney(0);
        if (deductionTicketId != 0) {
            groupOrder.setDeductionTicketId(deductionTicketId);
            groupOrder.setDeductionTicketName(deductionTicketName);
            groupOrder.setDeductionTicketMoney(realDeductionTicketMoney);// 实际使用抵扣券金额
        }

        groupOrder.setCustomerId(customerId);
        groupOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        groupOrder.setPayType(ConstEnum.PayType.MULTI_CHANNEL.getValue());
        groupOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        groupOrder.setCreateTime(new Date());
        groupOrder.setPayTime(new Date());

        groupOrderMapper.insert(groupOrder);

        CustomerMultiOrderDetail customerMultiOrderDetail = new CustomerMultiOrderDetail();
        customerMultiOrderDetail.setOrderId(customerMultiOrder.getId());
        customerMultiOrderDetail.setNum(++num);
        customerMultiOrderDetail.setSourceId(groupOrder.getId());
        customerMultiOrderDetail.setSourceType(CustomerMultiOrderDetail.SourceType.ZCGROUP.getValue());
        customerMultiOrderDetail.setMoney(totalMoney);
        customerMultiOrderDetailMapper.insert(customerMultiOrderDetail);

        NotNullMap map = new NotNullMap();
        map.putString("groupOrderId", groupOrder.getId());
        map.putString("foregiftOrderId", vehicleForegiftOrder.getId());
        map.putString("packetOrderId", vehiclePeriodOrder==null?"":vehiclePeriodOrder.getId());
        map.putLong("orderId", customerMultiOrder.getId());
        map.putInteger("money", customerMultiOrder.getTotalMoney());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

}
