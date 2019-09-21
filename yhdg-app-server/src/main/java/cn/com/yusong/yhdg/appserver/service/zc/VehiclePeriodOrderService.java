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
public class VehiclePeriodOrderService extends AbstractService {

    @Autowired
	VehiclePeriodOrderMapper vehiclePeriodOrderMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    WeixinPayOrderMapper weixinPayOrderMapper;
    @Autowired
    WeixinmpPayOrderMapper weixinmpPayOrderMapper;
    @Autowired
    PacketPeriodOrderRefundMapper packetPeriodOrderRefundMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    ActivityCustomerMapper activityCustomerMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    PacketPeriodActivityMapper packetPeriodActivityMapper;
    @Autowired
    InsuranceMapper insuranceMapper;
    @Autowired
    InsuranceOrderMapper insuranceOrderMapper;
    @Autowired
    CustomerPayTrackMapper customerPayTrackMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    CustomerMultiOrderDetailMapper customerMultiOrderDetailMapper;
    @Autowired
    RentPriceMapper rentPriceMapper;
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
    VehicleVipPriceCustomerMapper vehicleVipPriceCustomerMapper;
    @Autowired
    VehicleVipPriceMapper vehicleVipPriceMapper;
    @Autowired
    CustomerVehicleInfoMapper customerVehicleInfoMapper;

    public VehiclePeriodOrder find(String id) {
        return vehiclePeriodOrderMapper.find(id);
    }

    public VehiclePeriodOrder findLastEndTime(long customerId) {
        return vehiclePeriodOrderMapper.findLastEndTime(customerId, VehiclePeriodOrder.Status.USED.getValue());
    }
    public List<VehiclePeriodOrder> findListByNoUsed(long customerId) {
        return vehiclePeriodOrderMapper.findListByCustomerIdAndStatus(customerId, VehiclePeriodOrder.Status.NOT_USE.getValue());
    }

    public List<VehiclePeriodOrder> findList(long customerId, int offset, int limit, Integer status) {
        return vehiclePeriodOrderMapper.findList(customerId, offset, limit, status);
    }

    public VehiclePeriodOrder findOneEnabled(long customerId, int agentId, int modelId) {
        VehiclePeriodOrder vehiclePeriodOrder = vehiclePeriodOrderMapper.findOneEnabled(customerId, VehiclePeriodOrder.Status.USED.getValue(), agentId, modelId);
        if (vehiclePeriodOrder == null) {
            vehiclePeriodOrder = vehiclePeriodOrderMapper.findOneEnabled(customerId, VehiclePeriodOrder.Status.NOT_USE.getValue(),  agentId, modelId);
        }
        return vehiclePeriodOrder;
    }


    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByThird(long customerId, int vipPriceId, long priceId, int price,
                                 long couponTicketId, ConstEnum.PayType payType) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

        RentPrice rentPrice = null;
        VehicleVipPrice vehicleVipPrice = null;
        if (vipPriceId == 0) {
            rentPrice = rentPriceMapper.find(priceId);
            if (rentPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (rentPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
        } else if (vipPriceId != 0 ) {
            vehicleVipPrice = vehicleVipPriceMapper.find(vipPriceId);
            if (vehicleVipPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (vehicleVipPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
        }

        Agent agent = null;
        if (vipPriceId == 0) {
            agent = agentMapper.find(rentPrice.getAgentId());
        } else if (vipPriceId != 0 ) {
            agent = agentMapper.find(vehicleVipPrice.getAgentId());
        }

        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        //根据套餐类型生成换电或租电押金租金订单
        PriceSetting priceSetting = null;
        if (vipPriceId == 0) {
            priceSetting = priceSettingMapper.find(rentPrice.getPriceSettingId());
        } else if (vipPriceId != 0 ) {
            priceSetting = priceSettingMapper.find(vehicleVipPrice.getPriceSettingId().longValue());
        }

        //查询优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (couponTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(couponTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }
            ticketMoney = customerCouponTicket.getMoney();
            ticketName = customerCouponTicket.getTicketName();
        }

        //查询套餐
        if (vipPriceId == 0) {
            if (priceId != 0) {
                if (rentPrice.getRentPrice() != price) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
                }
                dayCount = rentPrice.getDayCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        } else if (vipPriceId != 0 ) {
            if (vehicleVipPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
            dayCount = vehicleVipPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(customerId);
        if (customerVehicleInfo == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户未缴纳租车押金");
        }

        Shop shop = shopMapper.find(customerVehicleInfo.getBalanceShopId());
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }


        VehiclePeriodOrder vehiclePeriodOrder = new VehiclePeriodOrder();
        vehiclePeriodOrder.setPartnerId(customer.getPartnerId());
        vehiclePeriodOrder.setAgentId(agent.getId());
        vehiclePeriodOrder.setAgentName(agent.getAgentName());
        vehiclePeriodOrder.setShopId(shop.getId());
        vehiclePeriodOrder.setShopName(shop.getShopName());
        vehiclePeriodOrder.setAgentCode(agent.getAgentCode());
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_PAY.getValue());
        vehiclePeriodOrder.setModelId(priceSetting.getModelId());
        vehiclePeriodOrder.setBatteryType(priceSetting.getBatteryType());

        vehiclePeriodOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER));
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
        } else if (vipPriceId != 0 ) {
            vehiclePeriodOrder.setPrice(vehicleVipPrice.getVehicleRentPrice() != null ? vehicleVipPrice.getVehicleRentPrice() : 0);
            vehiclePeriodOrder.setMoney(vehicleVipPrice.getVehicleRentPrice() != null ? vehicleVipPrice.getVehicleRentPrice() : 0);
        }

        vehiclePeriodOrderMapper.insert(vehiclePeriodOrder);

        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {

            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agent.getId());
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setScanCabinetId(null);
            packetPeriodOrder.setCabinetId(null);
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
            packetPeriodOrder.setCustomerId(customerId);
            packetPeriodOrder.setShopId(shop.getId());
            packetPeriodOrder.setShopName(shop.getShopName());
            packetPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());

            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());
            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setOrderCount(0);

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                packetPeriodOrder.setPrice(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
                packetPeriodOrder.setMoney(rentPrice.getBatteryRentPrice() != null ? rentPrice.getBatteryRentPrice() : 0);
            } else if (vipPriceId != 0 ) {
                packetPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
                packetPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
            }

            packetPeriodOrder.setDayCount(dayCount);
            packetPeriodOrder.setLimitCount(limitCount);
            packetPeriodOrder.setDayLimitCount(dayLimitCount);
            packetPeriodOrder.setOrderCount(0);

            packetPeriodOrderMapper.insert(packetPeriodOrder);

        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {


            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(RentPeriodOrder.Status.NOT_PAY.getValue());
            rentPeriodOrder.setCustomerId(customerId);
            rentPeriodOrder.setConsumeDepositBalance(0);
            rentPeriodOrder.setConsumeGiftBalance(0);
            rentPeriodOrder.setShopId(shop.getId());
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
            } else if (vipPriceId != 0 ) {
                rentPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
                rentPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice() != null ? vehicleVipPrice.getBatteryRentPrice() : 0);
            }

            rentPeriodOrderMapper.insert(rentPeriodOrder);

            //清空缓存
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
            memCachedClient.delete(key);
        }

        //生成组合订单
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_GROUP_ORDER));
        groupOrder.setPartnerId(customer.getPartnerId());
        groupOrder.setAgentId(agent.getId());
        groupOrder.setAgentName(agent.getAgentName());
        groupOrder.setAgentCode(agent.getAgentCode());
        groupOrder.setShopId(shop.getId());
        groupOrder.setShopName(shop.getShopName());
        groupOrder.setModelId(priceSetting.getModelId());
        groupOrder.setBatteryType(priceSetting.getBatteryType());

        groupOrder.setRentPriceId(priceId);
        groupOrder.setVipPriceId(vipPriceId);
        //类型
        groupOrder.setCategory(priceSetting.getCategory());
        groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
        //组合支付 押金+租金
        groupOrder.setPrice(price);
        //抵扣后
        groupOrder.setMoney(packetMoney);

        //实际支付金额
        groupOrder.setForegiftMoney(0);
        groupOrder.setRentPeriodMoney(packetMoney);

        //计算押金抵扣比例
        //double foregiftRatio = Double.valueOf(AppUtils.toDouble(foregiftMoney,foregiftPrice));
        //计算租金金抵扣比例
        double packetRatio = Double.valueOf(AppUtils.toDouble(packetMoney,price));

        /*车辆押金*/
        groupOrder.setVehicleForegiftId(null);
        groupOrder.setVehicleForegiftMoney(0);
        groupOrder.setVehicleForegiftPrice(0);
        /*车辆租金*/
        groupOrder.setVehiclePeriodId(vehiclePeriodOrder.getId());
        groupOrder.setVehiclePeriodMoney((int) (vehiclePeriodOrder.getMoney() * packetRatio));
        groupOrder.setVehiclePeriodPrice(vehiclePeriodOrder.getMoney());

        //车辆租期 电池租期
        if (vipPriceId == 0) {
            groupOrder.setVehicleDayCount(rentPrice.getDayCount());
            groupOrder.setBatteryDayCount(rentPrice.getDayCount());
        } else if (vipPriceId != 0 ) {
            groupOrder.setVehicleDayCount(vehicleVipPrice.getDayCount());
            groupOrder.setBatteryDayCount(vehicleVipPrice.getDayCount());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            //换电电池押金
            groupOrder.setBatteryForegiftId(null);
            groupOrder.setBatteryForegiftMoney(0);
            groupOrder.setBatteryForegiftPrice(0);
            //换电电池租金
            groupOrder.setBatteryRentId(packetPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(price - vehiclePeriodOrder.getMoney());
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            //租电电池押金
            groupOrder.setBatteryForegiftId(null);
            groupOrder.setBatteryForegiftMoney(0);
            groupOrder.setBatteryForegiftPrice(0);
            //租电电池租金
            groupOrder.setBatteryRentId(rentPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(price - vehiclePeriodOrder.getMoney());
        }


        //绑定租金优惠券
        groupOrder.setRentTicketMoney(price - packetMoney);
        groupOrder.setRentTicketName(ticketName);
        groupOrder.setRentCouponTicketId(couponTicketId == 0 ? null : couponTicketId);

        groupOrder.setCustomerId(customerId);
        groupOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        groupOrder.setPayType(payType.getValue());
        groupOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        groupOrder.setCreateTime(new Date());
        groupOrder.setPayTime(new Date());

        groupOrderMapper.insert(groupOrder);

        String memo = String.format("租金:%.2f", packetMoney / 100.0);
        if(payType == ConstEnum.PayType.WEIXIN) {
            WeixinPayOrder weixinPayOrder = new WeixinPayOrder();
            weixinPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXIN_PAY_ORDER));
            weixinPayOrder.setPartnerId(customer.getPartnerId());
            weixinPayOrder.setCustomerId(customerId);
            weixinPayOrder.setMoney(packetMoney);
            weixinPayOrder.setSourceType(PayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue());
            weixinPayOrder.setSourceId(groupOrder.getId());
            weixinPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinPayOrder.setMemo(memo);
            weixinPayOrder.setCreateTime(new Date());
            weixinPayOrder.setAgentId(groupOrder.getAgentId());
            weixinPayOrderMapper.insert(weixinPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinPayOrder);

        } else if(payType == ConstEnum.PayType.WEIXIN_MP) {
            WeixinmpPayOrder weixinmpPayOrder = new WeixinmpPayOrder();
            weixinmpPayOrder.setId(newOrderId(OrderId.OrderIdType.WEIXINMP_PAY_ORDER));
            weixinmpPayOrder.setPartnerId(customer.getPartnerId());
            weixinmpPayOrder.setAgentId(groupOrder.getAgentId());
            weixinmpPayOrder.setCustomerId(customerId);
            weixinmpPayOrder.setMobile(customer.getMobile());
            weixinmpPayOrder.setCustomerName(customer.getFullname());
            weixinmpPayOrder.setMoney(packetMoney);
            weixinmpPayOrder.setSourceType(PayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue());
            weixinmpPayOrder.setSourceId(groupOrder.getId());
            weixinmpPayOrder.setOrderStatus(PayOrder.Status.INIT.getValue());
            weixinmpPayOrder.setMemo(memo);
            weixinmpPayOrder.setCreateTime(new Date());
            weixinmpPayOrder.setAgentId(packetPeriodOrder.getAgentId());
            weixinmpPayOrderMapper.insert(weixinmpPayOrder);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, weixinmpPayOrder);

        } else if(payType == ConstEnum.PayType.ALIPAY) {
            Map map = super.payByAlipay(customer.getPartnerId(), groupOrder.getId(), packetMoney, customerId, AlipayPayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue(), "租车套餐订单支付", "租车套餐订单支付");
            map.put("orderId", groupOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else if(payType == ConstEnum.PayType.ALIPAY_FW) {
            Map map = super.payByAlipayfw(customer.getPartnerId(), groupOrder.getAgentId(), groupOrder.getId(), packetMoney, customerId, AlipayPayOrder.SourceType.CUSTOMER_RENT_GROUP_RENT_MONEY_PAY.getValue(), "租车套餐订单支付", "租车套餐订单支付");
            map.put("orderId", groupOrder.getId());
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else {
            throw new IllegalArgumentException("invalid payType(" + payType + ")");
        }

    }


    @Transactional(rollbackFor = Throwable.class)
    public RestResult payBalance(long customerId, int vipPriceId, long priceId, int price, long couponTicketId) {
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

        RentPrice rentPrice = null;
        VehicleVipPrice vehicleVipPrice = null;

        if (vipPriceId == 0) {
            rentPrice = rentPriceMapper.find(priceId);
            if (rentPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (rentPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
        } else if (vipPriceId != 0 ) {
            vehicleVipPrice = vehicleVipPriceMapper.find(vipPriceId);
            if (vehicleVipPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (vehicleVipPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
        }
        Agent agent = null;
        if (vipPriceId == 0) {
            agent = agentMapper.find(rentPrice.getAgentId());
        } else if (vipPriceId != 0 ) {
            agent = agentMapper.find(vehicleVipPrice.getAgentId());
        }

        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        //根据套餐类型生成换电或租电押金租金订单
        PriceSetting priceSetting = null;
        if (vipPriceId == 0) {
            priceSetting = priceSettingMapper.find(rentPrice.getPriceSettingId());
        } else if (vipPriceId != 0 ) {
            priceSetting = priceSettingMapper.find(vehicleVipPrice.getPriceSettingId().longValue());
        }

        //查询优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (couponTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(couponTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }
            ticketMoney = customerCouponTicket.getMoney();
            ticketName = customerCouponTicket.getTicketName();
        }

        //查询套餐
        if (vipPriceId == 0) {
            if (priceId != 0) {
                if (rentPrice.getRentPrice() != price) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
                }
                dayCount = rentPrice.getDayCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        } else if (vipPriceId != 0 ) {
            if (vehicleVipPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
            dayCount = vehicleVipPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        List<Integer> balanceList = new ArrayList<Integer>();

        RestResult restResult = updateCustomerBalance(customer, packetMoney, balanceList);
        if (restResult.getCode() != RespCode.CODE_0.getValue()) {
            throw new BalanceNotEnoughException();
        }

        Date beginTime = null;
        Date endTime = null;
        int status = VehiclePeriodOrder.Status.NOT_USE.getValue();
        //没有使用中电池，为使用变为已经使用
        VehiclePeriodOrder usedOrder = vehiclePeriodOrderMapper.findOneEnabled(customer.getId(), VehiclePeriodOrder.Status.USED.getValue(), agent.getId(), priceSetting.getModelId());
        if (usedOrder == null) {
            beginTime = new Date();
            endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, dayCount), Calendar.DAY_OF_MONTH), -1);
            status = VehiclePeriodOrder.Status.USED.getValue();
        }

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(customerId);
        if (customerVehicleInfo == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户未缴纳租车押金");
        }

        Shop shop = shopMapper.find(customerVehicleInfo.getBalanceShopId());
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        VehiclePeriodOrder vehiclePeriodOrder = new VehiclePeriodOrder();
        vehiclePeriodOrder.setPartnerId(customer.getPartnerId());
        vehiclePeriodOrder.setAgentId(agent.getId());
        vehiclePeriodOrder.setModelId(priceSetting.getModelId());
        vehiclePeriodOrder.setBatteryType(priceSetting.getBatteryType());
        vehiclePeriodOrder.setAgentName(agent.getAgentName());
        vehiclePeriodOrder.setAgentCode(agent.getAgentCode());

        vehiclePeriodOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER));
        vehiclePeriodOrder.setStatus(status);
        vehiclePeriodOrder.setCustomerId(customerId);
        vehiclePeriodOrder.setBeginTime(beginTime);
        vehiclePeriodOrder.setEndTime(endTime);
        vehiclePeriodOrder.setShopId(shop.getId());
        vehiclePeriodOrder.setShopName(shop.getShopName());

        vehiclePeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        vehiclePeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
        vehiclePeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        vehiclePeriodOrder.setCreateTime(new Date());
        vehiclePeriodOrder.setPayTime(new Date());
        vehiclePeriodOrder.setDayCount(dayCount);

        //保存租车套餐租金车辆金额
        if (vipPriceId == 0) {
            vehiclePeriodOrder.setPrice(rentPrice.getVehicleRentPrice());
            vehiclePeriodOrder.setMoney(rentPrice.getVehicleRentPrice());
        } else if (vipPriceId != 0 ) {
            vehiclePeriodOrder.setPrice(vehicleVipPrice.getVehicleRentPrice());
            vehiclePeriodOrder.setMoney(vehicleVipPrice.getVehicleRentPrice());
        }

        vehiclePeriodOrderMapper.insert(vehiclePeriodOrder);

        if (priceId != 0) {
            //租金赠送
            Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();
            String sourceId = vehiclePeriodOrder.getId();
            Integer sourceType = OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER.getValue();

            super.giveCouponTicket(sourceId, ConstEnum.Category.VEHICLE.getValue(), sourceType, type, dayCount, agent.getId(), CustomerCouponTicket.TicketType.RENT.getValue(), vehiclePeriodOrder.getCustomerMobile());
        }


        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {

            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agent.getId());
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());

            packetPeriodOrder.setScanCabinetId(null);
            packetPeriodOrder.setCabinetId(null);
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_USE.getValue());
            packetPeriodOrder.setCustomerId(customerId);
            packetPeriodOrder.setShopId(shop.getId());
            packetPeriodOrder.setShopName(shop.getShopName());

            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                packetPeriodOrder.setPrice(rentPrice.getBatteryRentPrice());
                packetPeriodOrder.setMoney(rentPrice.getBatteryRentPrice());
            } else if (vipPriceId != 0 ) {
                packetPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice());
                packetPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice());
            }

            packetPeriodOrder.setPayTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);
            packetPeriodOrder.setLimitCount(limitCount);
            packetPeriodOrder.setDayLimitCount(dayLimitCount);
            packetPeriodOrder.setOrderCount(0);

            packetPeriodOrderMapper.insert(packetPeriodOrder);

        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            CustomerRentInfo customerRentInfo = customerRentInfoMapper.find(customer.getId());

            Date beginTime1 = null;
            Date endTime1 = null;
            int status1 = RentPeriodOrder.Status.NOT_USE.getValue();
            //没有使用中电池，为使用变为已经使用
            RentPeriodOrder usedOrder1 = rentPeriodOrderMapper.findOneEnabled(customer.getId(), RentPeriodOrder.Status.USED.getValue(), agent.getId(),  customerRentInfo.getBatteryType());
            if (usedOrder1 == null) {
                beginTime1 = new Date();
                endTime1 = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime1, dayCount), Calendar.DAY_OF_MONTH), -1);
                status1 = RentPeriodOrder.Status.USED.getValue();
            }

            rentPeriodOrder.setPartnerId(customer.getPartnerId());
            rentPeriodOrder.setAgentId(agent.getId());
            rentPeriodOrder.setAgentName(agent.getAgentName());
            rentPeriodOrder.setId(newOrderId(OrderId.OrderIdType.RENT_PERIOD_ORDER));
            rentPeriodOrder.setStatus(status1);
            rentPeriodOrder.setBeginTime(beginTime1);
            rentPeriodOrder.setEndTime(endTime1);
            rentPeriodOrder.setCustomerId(customerId);
            rentPeriodOrder.setConsumeDepositBalance(0);
            rentPeriodOrder.setConsumeGiftBalance(0);
            rentPeriodOrder.setShopId(shop.getId());
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
                rentPeriodOrder.setPrice(rentPrice.getBatteryRentPrice());
                rentPeriodOrder.setMoney(rentPrice.getBatteryRentPrice());
            } else if (vipPriceId != 0 ) {
                rentPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice());
                rentPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice());
            }

            rentPeriodOrderMapper.insert(rentPeriodOrder);

            //清空缓存
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
            memCachedClient.delete(key);

        }

        if (couponTicketId != 0) {
            if (customerCouponTicketMapper.useTicket(couponTicketId,
                    new Date(),
                    CustomerCouponTicket.Status.NOT_USER.getValue(),
                    CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("优惠券不可用");
            }
        }

        //余额充值，套餐充值成功,推送
        PushMetaData metaData = new PushMetaData();
        metaData.setSourceId(packetPeriodOrder.getId());
        metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
        metaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(metaData);

        //生成组合订单
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_GROUP_ORDER));
        groupOrder.setPartnerId(customer.getPartnerId());
        groupOrder.setAgentId(agent.getId());
        groupOrder.setAgentName(agent.getAgentName());
        groupOrder.setAgentCode(agent.getAgentCode());
        groupOrder.setShopId(shop.getId());
        groupOrder.setShopName(shop.getShopName());
        groupOrder.setModelId(priceSetting.getModelId());
        groupOrder.setBatteryType(priceSetting.getBatteryType());

        groupOrder.setRentPriceId(priceId);
        groupOrder.setVipPriceId(vipPriceId);
        //类型
        groupOrder.setCategory(priceSetting.getCategory());
        groupOrder.setStatus(GroupOrder.Status.PAY_OK.getValue());
        //组合支付 押金+租金
        groupOrder.setPrice(price);
        //抵扣后
        groupOrder.setMoney(packetMoney);

        //实际支付金额
        groupOrder.setForegiftMoney(0);
        groupOrder.setRentPeriodMoney(packetMoney);

        //计算押金抵扣比例
        //double foregiftRatio = Double.valueOf(AppUtils.toDouble(foregiftMoney,foregiftPrice));
        //计算租金金抵扣比例
        double packetRatio = Double.valueOf(AppUtils.toDouble(packetMoney,price));

        /*车辆押金*/
        groupOrder.setVehicleForegiftId(null);
        groupOrder.setVehicleForegiftMoney(0);
        groupOrder.setVehicleForegiftPrice(0);
        /*车辆租金*/
        groupOrder.setVehiclePeriodId(vehiclePeriodOrder.getId());
        groupOrder.setVehiclePeriodMoney((int) (vehiclePeriodOrder.getMoney() * packetRatio));
        groupOrder.setVehiclePeriodPrice(vehiclePeriodOrder.getMoney());

        //车辆租期 电池租期
        if (vipPriceId == 0) {
            groupOrder.setVehicleDayCount(rentPrice.getDayCount());
            groupOrder.setBatteryDayCount(rentPrice.getDayCount());
        } else if (vipPriceId != 0 ) {
            groupOrder.setVehicleDayCount(vehicleVipPrice.getDayCount());
            groupOrder.setBatteryDayCount(vehicleVipPrice.getDayCount());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            //换电电池押金
            groupOrder.setBatteryForegiftId(null);
            groupOrder.setBatteryForegiftMoney(0);
            groupOrder.setBatteryForegiftPrice(0);
            //换电电池租金
            groupOrder.setBatteryRentId(packetPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(price - vehiclePeriodOrder.getMoney());
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            //租电电池押金
            groupOrder.setBatteryForegiftId(null);
            groupOrder.setBatteryForegiftMoney(0);
            groupOrder.setBatteryForegiftPrice(0);
            //租电电池租金
            groupOrder.setBatteryRentId(rentPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(price - vehiclePeriodOrder.getMoney());
        }


        //绑定租金优惠券
        groupOrder.setRentTicketMoney(price - packetMoney);
        groupOrder.setRentTicketName(ticketName);
        groupOrder.setRentCouponTicketId(couponTicketId == 0 ? null : couponTicketId);

        groupOrder.setCustomerId(customerId);
        groupOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        groupOrder.setPayType(ConstEnum.PayType.BALANCE.getValue());
        groupOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        groupOrder.setCreateTime(new Date());
        groupOrder.setPayTime(new Date());

        groupOrderMapper.insert(groupOrder);

        //客户购买租金续租消费轨迹
        CustomerPayTrack customerPayTrack = new CustomerPayTrack();
        customerPayTrack.setAgentId(agent.getId());
        customerPayTrack.setCustomerId(customer.getId());
        customerPayTrack.setCustomerFullname(customer.getFullname());
        customerPayTrack.setCustomerMobile(customer.getMobile());
        customerPayTrack.setTrackType(CustomerPayTrack.TrackType.RENT.getValue());
        customerPayTrack.setMemo(StringUtils.replaceEach("租车续租，租金：${packetPrice}。",
                new String[]{"${packetPrice}"},
                new String[]{String.format("%.2f元", 1d * price / 100.0)}));
        customerPayTrack.setCreateTime(new Date());
        customerPayTrackMapper.insert(customerPayTrack);

        return RestResult.SUCCESS;
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult payByMulti(long customerId, int vipPriceId, long priceId, int price,
                                 long couponTicketId) {

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在");
        }

        String ticketName = null;//优惠券名称
        int ticketMoney = 0;//优惠券金额
        int packetMoney = 0; //套餐支付金额
        int dayCount = 0;
        Integer limitCount = null;
        Integer dayLimitCount = null;

        RentPrice rentPrice = null;
        VehicleVipPrice vehicleVipPrice = null;

        if (vipPriceId == 0) {
            rentPrice = rentPriceMapper.find(priceId);
            if (rentPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (rentPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
        } else if (vipPriceId != 0 ) {
            vehicleVipPrice = vehicleVipPriceMapper.find(vipPriceId);
            if (vehicleVipPrice == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "套餐设置不存在");
            }

            if (vehicleVipPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
        }
        Agent agent = null;
        if (vipPriceId == 0) {
            agent = agentMapper.find(rentPrice.getAgentId());
        } else if (vipPriceId != 0 ) {
            agent = agentMapper.find(vehicleVipPrice.getAgentId());
        }

        if (agent == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "运营商不存在");
        }

        //根据套餐类型生成换电或租电押金租金订单
        PriceSetting priceSetting = null;
        if (vipPriceId == 0) {
            priceSetting = priceSettingMapper.find(rentPrice.getPriceSettingId());
        } else if (vipPriceId != 0 ) {
            priceSetting = priceSettingMapper.find(vehicleVipPrice.getPriceSettingId().longValue());
        }

        //查询优惠券
        CustomerCouponTicket customerCouponTicket = null;
        if (couponTicketId != 0) {
            customerCouponTicket = customerCouponTicketMapper.find(couponTicketId);
            if (customerCouponTicket == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "该用户没有此优惠券");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.USED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已使用");
            }
            if (customerCouponTicket.getStatus() == CustomerCouponTicket.Status.EXPIRED.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "优惠券已过期");
            }
            ticketMoney = customerCouponTicket.getMoney();
            ticketName = customerCouponTicket.getTicketName();
        }

        //查询套餐
        if (vipPriceId == 0) {
            if (priceId != 0) {
                if (rentPrice.getRentPrice() != price) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
                }
                dayCount = rentPrice.getDayCount();

                if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                    packetMoney = price - ticketMoney;
                }
            }
        } else if (vipPriceId != 0 ) {
            if (vehicleVipPrice.getRentPrice() != price) {
                return RestResult.result(RespCode.CODE_2.getValue(), "租金价格不正确，请确认");
            }
            dayCount = vehicleVipPrice.getDayCount();

            if (price >= ticketMoney) { //套餐价格大于 优惠券金额
                packetMoney = price - ticketMoney;
            }
        }

        CustomerVehicleInfo customerVehicleInfo = customerVehicleInfoMapper.find(customerId);
        if (customerVehicleInfo == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户未缴纳租车押金");
        }

        Shop shop = shopMapper.find(customerVehicleInfo.getBalanceShopId());
        if (shop == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
        }

        //创建多通道支付订单
        CustomerMultiOrder customerMultiOrder = createCustomerMultiOrder(agent, customer, packetMoney, CustomerMultiOrder.Type.ZC.getValue());
        int num = customerMultiOrderDetailMapper.countByOrderId(customerMultiOrder.getId());

        VehiclePeriodOrder vehiclePeriodOrder = new VehiclePeriodOrder();
        vehiclePeriodOrder.setPartnerId(customer.getPartnerId());
        vehiclePeriodOrder.setAgentId(agent.getId());
        vehiclePeriodOrder.setAgentName(agent.getAgentName());
        vehiclePeriodOrder.setAgentCode(agent.getAgentCode());
        vehiclePeriodOrder.setModelId(priceSetting.getModelId());
        vehiclePeriodOrder.setBatteryType(priceSetting.getBatteryType());
        vehiclePeriodOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER));
        vehiclePeriodOrder.setStatus(VehiclePeriodOrder.Status.NOT_PAY.getValue());
        vehiclePeriodOrder.setCustomerId(customerId);

        vehiclePeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        vehiclePeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
        vehiclePeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        vehiclePeriodOrder.setCreateTime(new Date());
        vehiclePeriodOrder.setPayTime(new Date());
        vehiclePeriodOrder.setDayCount(dayCount);

        //保存租车套餐租金车辆金额
        if (vipPriceId == 0) {
            vehiclePeriodOrder.setPrice(rentPrice.getVehicleRentPrice());
            vehiclePeriodOrder.setMoney(rentPrice.getVehicleRentPrice());
        } else if (vipPriceId != 0 ) {
            vehiclePeriodOrder.setPrice(vehicleVipPrice.getVehicleRentPrice());
            vehiclePeriodOrder.setMoney(vehicleVipPrice.getVehicleRentPrice());
        }

        vehiclePeriodOrder.setShopId(shop.getId());
        vehiclePeriodOrder.setShopName(shop.getShopName());
        vehiclePeriodOrderMapper.insert(vehiclePeriodOrder);

        if (priceId != 0) {
            //租金赠送
            Integer type = CustomerCouponTicketGift.Type.BUY_RENT.getValue();
            String sourceId = vehiclePeriodOrder.getId();
            Integer sourceType = OrderId.OrderIdType.VEHICLE_PACKET_PERIOD_ORDER.getValue();

            super.giveCouponTicket(sourceId, ConstEnum.Category.VEHICLE.getValue(), sourceType, type, dayCount, agent.getId(), CustomerCouponTicket.TicketType.RENT.getValue(), vehiclePeriodOrder.getCustomerMobile());
        }


        PacketPeriodOrder packetPeriodOrder = new PacketPeriodOrder();
        RentPeriodOrder rentPeriodOrder = new RentPeriodOrder();

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {

            packetPeriodOrder.setConsumeDepositBalance(0);
            packetPeriodOrder.setConsumeGiftBalance(0);
            packetPeriodOrder.setOrderCount(0);
            packetPeriodOrder.setBatteryType(priceSetting.getBatteryType());
            packetPeriodOrder.setPartnerId(customer.getPartnerId());
            packetPeriodOrder.setAgentId(agent.getId());
            packetPeriodOrder.setAgentName(agent.getAgentName());
            packetPeriodOrder.setVehicleOrderFlag(ConstEnum.Flag.TRUE.getValue());

            packetPeriodOrder.setScanCabinetId(null);
            packetPeriodOrder.setCabinetId(null);
            packetPeriodOrder.setId(newOrderId(OrderId.OrderIdType.PACKET_PERIOD_ORDER));
            packetPeriodOrder.setStatus(PacketPeriodOrder.Status.NOT_PAY.getValue());
            packetPeriodOrder.setCustomerId(customerId);

            packetPeriodOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
            packetPeriodOrder.setPayType(ConstEnum.PayType.VEHICLE_GROUP.getValue());
            packetPeriodOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
            packetPeriodOrder.setCreateTime(new Date());

            //保存租车套餐租金电池金额
            if (vipPriceId == 0) {
                packetPeriodOrder.setPrice(rentPrice.getBatteryRentPrice());
                packetPeriodOrder.setMoney(rentPrice.getBatteryRentPrice());
            } else if (vipPriceId != 0 ) {
                packetPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice());
                packetPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice());
            }

            packetPeriodOrder.setPayTime(new Date());
            packetPeriodOrder.setDayCount(dayCount);
            packetPeriodOrder.setLimitCount(limitCount);
            packetPeriodOrder.setDayLimitCount(dayLimitCount);
            packetPeriodOrder.setOrderCount(0);

            packetPeriodOrder.setShopId(shop.getId());
            packetPeriodOrder.setShopName(shop.getShopName());
            packetPeriodOrderMapper.insert(packetPeriodOrder);

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
                rentPeriodOrder.setPrice(rentPrice.getBatteryRentPrice());
                rentPeriodOrder.setMoney(rentPrice.getBatteryRentPrice());
            } else if (vipPriceId != 0 ) {
                rentPeriodOrder.setPrice(vehicleVipPrice.getBatteryRentPrice());
                rentPeriodOrder.setMoney(vehicleVipPrice.getBatteryRentPrice());
            }

            rentPeriodOrder.setShopId(shop.getId());
            rentPeriodOrder.setShopName(shop.getShopName());
            rentPeriodOrderMapper.insert(rentPeriodOrder);

            //清空缓存
            String key = CacheKey.key(CacheKey.K_CUSROMER_V_EXPIRE_TIME, rentPeriodOrder.getCustomerId());
            memCachedClient.delete(key);

        }

        if (couponTicketId != 0) {
            if (customerCouponTicketMapper.useTicket(couponTicketId,
                    new Date(),
                    CustomerCouponTicket.Status.NOT_USER.getValue(),
                    CustomerCouponTicket.Status.USED.getValue()) == 0) {
                throw new CouponTicketNotAvailableException("优惠券不可用");
            }
        }

        //余额充值，套餐充值成功,推送
        PushMetaData metaData = new PushMetaData();
        metaData.setSourceId(packetPeriodOrder.getId());
        metaData.setSourceType(PushMessage.SourceType.CUSTOMER_PACKET_PERIOD_ORDER_PAY_SUCCESS.getValue());
        metaData.setCreateTime(new Date());
        pushMetaDataMapper.insert(metaData);

        //生成组合订单
        GroupOrder groupOrder = new GroupOrder();
        groupOrder.setId(newOrderId(OrderId.OrderIdType.VEHICLE_GROUP_ORDER));
        groupOrder.setPartnerId(customer.getPartnerId());
        groupOrder.setAgentId(agent.getId());
        groupOrder.setAgentName(agent.getAgentName());
        groupOrder.setAgentCode(agent.getAgentCode());
        groupOrder.setModelId(priceSetting.getModelId());
        groupOrder.setBatteryType(priceSetting.getBatteryType());

        groupOrder.setRentPriceId(priceId);
        groupOrder.setVipPriceId(vipPriceId);
        //类型
        groupOrder.setCategory(priceSetting.getCategory());
        groupOrder.setStatus(GroupOrder.Status.WAIT_PAY.getValue());
        //组合支付 押金+租金
        groupOrder.setPrice(price);
        //抵扣后
        groupOrder.setMoney(packetMoney);

        //实际支付金额
        groupOrder.setForegiftMoney(0);
        groupOrder.setRentPeriodMoney(packetMoney);

        //计算押金抵扣比例
        //double foregiftRatio = Double.valueOf(AppUtils.toDouble(foregiftMoney,foregiftPrice));
        //计算租金金抵扣比例
        double packetRatio = Double.valueOf(AppUtils.toDouble(packetMoney,price));

        /*车辆押金*/
        groupOrder.setVehicleForegiftId(null);
        groupOrder.setVehicleForegiftMoney(0);
        groupOrder.setVehicleForegiftPrice(0);
        /*车辆租金*/
        groupOrder.setVehiclePeriodId(vehiclePeriodOrder.getId());
        groupOrder.setVehiclePeriodMoney((int) (vehiclePeriodOrder.getMoney() * packetRatio));
        groupOrder.setVehiclePeriodPrice(vehiclePeriodOrder.getMoney());

        //车辆租期 电池租期
        if (vipPriceId == 0) {
            groupOrder.setVehicleDayCount(rentPrice.getDayCount());
            groupOrder.setBatteryDayCount(rentPrice.getDayCount());
        } else if (vipPriceId != 0 ) {
            groupOrder.setVehicleDayCount(vehicleVipPrice.getDayCount());
            groupOrder.setBatteryDayCount(vehicleVipPrice.getDayCount());
        }

        if (priceSetting.getCategory() == PriceSetting.Category.EXCHANGE.getValue()) {
            //换电电池押金
            groupOrder.setBatteryForegiftId(null);
            groupOrder.setBatteryForegiftMoney(0);
            groupOrder.setBatteryForegiftPrice(0);
            //换电电池租金
            groupOrder.setBatteryRentId(packetPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(price - vehiclePeriodOrder.getMoney());
        } else if (priceSetting.getCategory() == PriceSetting.Category.RENT.getValue()) {
            //租电电池押金
            groupOrder.setBatteryForegiftId(null);
            groupOrder.setBatteryForegiftMoney(0);
            groupOrder.setBatteryForegiftPrice(0);
            //租电电池租金
            groupOrder.setBatteryRentId(rentPeriodOrder.getId());
            groupOrder.setBatteryRentPeriodMoney(packetMoney - (int) (vehiclePeriodOrder.getMoney() * packetRatio));
            groupOrder.setBatteryRentPeriodPrice(price - vehiclePeriodOrder.getMoney());
        }


        //绑定租金优惠券
        groupOrder.setRentTicketMoney(price - packetMoney);
        groupOrder.setRentTicketName(ticketName);
        groupOrder.setRentCouponTicketId(couponTicketId == 0 ? null : couponTicketId);

        groupOrder.setCustomerId(customerId);
        groupOrder.setCustomerMobile(customer.getMobile() == null ? "" : customer.getMobile());
        groupOrder.setPayType(ConstEnum.PayType.MULTI_CHANNEL.getValue());
        groupOrder.setCustomerFullname(customer.getFullname() == null ? "" : customer.getFullname());
        groupOrder.setCreateTime(new Date());
        groupOrder.setPayTime(new Date());

        groupOrder.setShopId(shop.getId());
        groupOrder.setShopName(shop.getShopName());
        groupOrderMapper.insert(groupOrder);

        CustomerMultiOrderDetail customerMultiOrderDetail = new CustomerMultiOrderDetail();
        customerMultiOrderDetail.setOrderId(customerMultiOrder.getId());
        customerMultiOrderDetail.setNum(++num);
        customerMultiOrderDetail.setSourceId(groupOrder.getId());
        customerMultiOrderDetail.setSourceType(CustomerMultiOrderDetail.SourceType.ZCGROUP.getValue());
        customerMultiOrderDetail.setMoney(groupOrder.getMoney());
        customerMultiOrderDetailMapper.insert(customerMultiOrderDetail);

        NotNullMap map = new NotNullMap();
        map.putString("groupOrderId", groupOrder.getId());
        map.putString("packetOrderId", vehiclePeriodOrder==null?"":vehiclePeriodOrder.getId());
        map.putLong("orderId", customerMultiOrder.getId());
        map.putInteger("money", customerMultiOrder.getTotalMoney());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
    }

}