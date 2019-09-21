package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.basic.*;
import cn.com.yusong.yhdg.appserver.persistence.hdg.*;
import cn.com.yusong.yhdg.appserver.service.AbstractService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.NewBoxNum;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CabinetService extends AbstractService {

    static Logger log = LogManager.getLogger(CabinetService.class);

    @Autowired
    MemCachedClient memCachedClient;

    @Autowired
    AreaCache areaCache;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    ExchangePriceTimeMapper exchangePriceTimeMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    AgentSystemConfigMapper agentSystemConfigMapper;
    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;
    @Autowired
    BespeakOrderMapper bespeakOrderMapper;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;
    @Autowired
    VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    VipPacketPeriodPriceMapper vipPacketPeriodPriceMapper;
    @Autowired
    VipPriceCabinetMapper vipPriceCabinetMapper;

    public Cabinet find(String id) {
        return (Cabinet) setAreaProperties(areaCache, cabinetMapper.find(id));
    }

    public Cabinet findByTerminalId(String terminalId) {
        return cabinetMapper.findByTerminalId(terminalId);
    }

    public List<Cabinet> findNearest(List<Integer> agentId, String geoHash, double lng, double lat, String keyword, Integer provinceId, Integer cityId, Integer viewType, String unSharedCabinetId, int offset, int limit) {
        return (List<Cabinet>) setAreaProperties(areaCache,
                cabinetMapper.findNearest(agentId,
                        CabinetBox.BoxStatus.EMPTY.getValue(),
                        CabinetBox.BoxStatus.FULL.getValue(),
                        Battery.Status.IN_BOX.getValue(),
                        geoHash,
                        keyword,
                        lng,
                        lat,
                        provinceId,
                        cityId,
                        Cabinet.UpLineStatus.ONLINE.getValue(),
                        viewType,
                        unSharedCabinetId,
                        offset, limit
                )
        );
    }

    public int findFullCount(String cabinetId) {
        return cabinetMapper.findFullCount(CabinetBox.BoxStatus.FULL.getValue(),
                Battery.Status.IN_BOX.getValue(), cabinetId);
    }


    public int findCountByDispatcher(long dispatcherId) {
        return cabinetMapper.findCountByDispatcher(dispatcherId);
    }

    public List<Cabinet> findList(long dispatcherId) {
        List<Cabinet> cabinetList = cabinetMapper.findList(dispatcherId);
        setAreaProperties(areaCache, cabinetList);
        return cabinetList;
    }

    public int findBatteryCountByDispatcher(long dispatcherId) {
        return cabinetMapper.findBatteryCountByDispatcher(dispatcherId);
    }

    public int findNotFullBatteryCountByDispatcher(long dispatcherId) {
        return cabinetMapper.findNotFullBatteryCountByDispatcher(dispatcherId);
    }

    public int findBatteryCountByCabinet(String cabinetId) {
        return cabinetMapper.findBatteryCountByCabinet(cabinetId);
    }

    public int findNotFullBatteryCountByCabinet(String cabinetId) {
        return cabinetMapper.findNotFullBatteryCountByCabinet(cabinetId);
    }

    public int findOnlineSubcabinetCountByCabinet(String cabinetId) {
        return cabinetMapper.findOnlineSubcabinetCountByCabinet(cabinetId);
    }

    public int findOfflineSubcabinetCountByCabinet(String cabinetId) {
        return cabinetMapper.findOfflineSubcabinetCountByCabinet(cabinetId);
    }

    public List<Cabinet> findByCabinetIdList(String cabinetId){
        return  cabinetMapper.findIdByCabinet(cabinetId);
    }

    public List<String> findIdByCabinetId(String cabinetId){
        return  cabinetMapper.findIdByCabinetId(cabinetId);
    }

    public RestResult loopExchangeBattery(long customerId, String cabinetId, String oldBoxNum, String newBoxNum, int type, Boolean isBalance) {
        Map root = new HashMap();
        Map data = new HashMap();
        root.put("data", data);

        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        Cabinet cabinet = cabinetMapper.find(cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "柜子不存在");
        }

        //取新电
        if(type == 1) {
            if (StringUtils.isNotEmpty(oldBoxNum)) {
                String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, cabinet.getId(), oldBoxNum);
                NewBoxNum cacheValue = (NewBoxNum) memCachedClient.get(cachekey);
                if (cacheValue != null) {
                    memCachedClient.delete(cachekey);
                    if (log.isDebugEnabled()) {
                        log.debug("delete cache K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId={}, boxNum={}, NewBoxNum={}", cabinet.getId(), oldBoxNum, cacheValue);
                    }
                }
            }

            CabinetBox newBox = cabinetBoxMapper.find(cabinetId, newBoxNum);
            if (newBox.getBatteryId() != null) { //格口中有电池
                Battery battery = batteryMapper.find(newBox.getBatteryId());
                if (newBox.getIsOpen() != ConstEnum.Flag.TRUE.getValue()) {
                    root.put("step", "opening_new_battery");
                } else {
                    root.put("step", "wait_take_battery");
                }
                data.put("batteryId", battery.getId());
                data.put("batteryCode", battery.getCode());
                data.put("customerFullname", customer.getFullname());
                data.put("batteryTypeName", findBatteryType(battery.getType()).getTypeName());
                data.put("volume", battery.getVolume());
                data.put("boxNum", battery.getBoxNum());
                return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);
            } else { //格口没有电池 说明电池已经取出 流程结束
                root.put("step", "wait_put_battery");
                data.put("customerFullname", customer.getFullname());
                return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);
            }
        }

        //放旧电
        if(type == 2) {
            CabinetBox oldBox = cabinetBoxMapper.find(cabinetId, oldBoxNum);
            if (oldBox.getBatteryId() != null) { //格口中有电池
                Battery battery = batteryMapper.find(oldBox.getBatteryId());
                if (battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) { //电池状态待付款 说明已经入箱
                    BatteryOrder batteryOrder = batteryOrderMapper.find(battery.getOrderId());

                    //如果电池是入箱未付款状态，并且不在轮询中参加计算，直接返回
                    if(!isBalance){
                        root.put("step", "calculate_price");
                        data.put("customerFullname", customer.getFullname());
                        data.put("batteryId", battery.getId());
                        data.put("batteryCode", battery.getCode());
                        data.put("batteryTypeName", findBatteryType(battery.getType()).getTypeName());
                        data.put("volume", battery.getVolume());
                        data.put("orderId", battery.getOrderId());
                        data.put("boxNum", battery.getBoxNum());
                        data.put("message",batteryOrder.getErrorMessage());
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);
                    }

                    //判断格口中电池是否与用户绑定电池一致，不一致直接抛出
                    if(customerExchangeBatteryMapper.existsByCustomer(customer.getId(), battery.getId()) == 0){
                        return RestResult.result(RespCode.CODE_2.getValue(), "放入电池与用户绑定电池不一致");
                    }

                    //判断是否有满箱，没满箱不会继续走下去
                    String bespeakBoxNum = null;
                    BespeakOrder bespeakOrder = bespeakOrderMapper.findSuccessByCustomer(customer.getId(), BespeakOrder.Status.SUCCESS.getValue());
                    if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(cabinetId)){
                        bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
                    }
                    Integer batteryType = null;
                    CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoMapper.find(customerId);
                    if(customerExchangeInfo != null){
                        batteryType = customerExchangeInfo.getBatteryType();
                    }else{
                        ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(cabinet.getAgentId(), customerId);
                        if(exchangeWhiteList != null){
                            batteryType = exchangeWhiteList.getBatteryType();
                        }
                    }
                    if(batteryType == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "客户没有设置电池类型");
                    }
                    CabinetBox cabinetBox =  cabinetBoxMapper.findOneFull(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(),
                            cabinetId, battery.getId(), batteryType, bespeakBoxNum);
                    if(cabinetBox == null){
                        return RestResult.result(RespCode.CODE_2.getValue(), "没有满箱电池");
                    }

                    //如果这里有错误消息 并且价格是空 可能价格计算失败 要进行重新计算价格
                    Result result = null;
                    if (StringUtils.isNotEmpty(batteryOrder.getErrorMessage()) && batteryOrder.getPrice() == null) {
                        result = calculatePrice(customer, batteryOrder, cabinet, battery);
                        if (StringUtils.isNotEmpty(result.errorMessage)) {  //再次计算失败 直接返回
                            return RestResult.result(RespCode.CODE_2.getValue(), result.errorMessage);
                        } else { //计算成功
                            batteryOrder = batteryOrderMapper.find(battery.getOrderId()); //如果计算成功会更新价格 这里再次查询订单信息
                        }
                    }

                    if (batteryOrder.getPrice() == null) { //检测到电池
                        root.put("step", "calculate_price");
                        data.put("customerFullname", customer.getFullname());
                        data.put("batteryId", battery.getId());
                        data.put("batteryCode", battery.getCode());
                        data.put("batteryTypeName", findBatteryType(battery.getType()).getTypeName());
                        data.put("volume", battery.getVolume());
                        data.put("orderId", battery.getOrderId());
                        data.put("boxNum", battery.getBoxNum());
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);

                    } else if (batteryOrder.getPrice() == 0 || batteryOrder.getOrderStatus() == BatteryOrder.OrderStatus.PAY.getValue()) { //需要打开空箱
                        root.put("step", "opening_new_battery");
                        data.put("customerFullname", customer.getFullname());
                        data.put("batteryId", battery.getId());
                        data.put("batteryCode", battery.getCode());
                        data.put("customerFullname", customer.getFullname());
                        data.put("batteryTypeName", findBatteryType(battery.getType()).getTypeName());
                        data.put("volume", battery.getVolume());
                        data.put("cabinetId", battery.getCabinetId());
                        data.put("boxNum", battery.getBoxNum());

                        if (result != null && result.payOk) {
                            data.put("need_open_battery", "yes");
                        }

                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);

                    } else if (batteryOrder.getPrice() > 0 && batteryOrder.getOrderStatus() == BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue()) { //待付款
                        root.put("step", "wait_pay");
                        data.put("customerFullname", customer.getFullname());
                        data.put("batteryId", battery.getId());
                        data.put("batteryTypeName", findBatteryType(battery.getType()).getTypeName());
                        data.put("volume", battery.getVolume());
                        data.put("money", batteryOrder.getPrice());
                        data.put("orderId", battery.getOrderId());
                        data.put("cabinetId", batteryOrder.getPutCabinetId());
                        data.put("cabinetName", batteryOrder.getPutCabinetName());
                        data.put("boxNum", batteryOrder.getPutBoxNum());
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);
                    }
                } else if (battery.getStatus() == Battery.Status.IN_BOX.getValue() || battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) { //说明旧电归还完成
                    String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, cabinet.getId(), oldBox.getBoxNum());
                    NewBoxNum cacheValue = (NewBoxNum) memCachedClient.get(cachekey);
                    if (cacheValue == null) {
                        root.put("step", "opening_new_battery");
                        data.put("customerFullname", customer.getFullname());
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);

                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("get cache K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId={}, boxNum={}, NewBoxNum={}", cabinet.getId(), oldBox.getBoxNum(), cacheValue);
                        }
                        String nextBoxNum = cacheValue.newBoxNum;

                        CabinetBox newBox = cabinetBoxMapper.find(cabinet.getId(), nextBoxNum);
                        if(newBox.getBatteryId() == null){
                            if (log.isDebugEnabled()) {
                                log.debug("电池已取出，格口为空, cabinetId: {},  newBox: {}", cabinet.getId(), nextBoxNum);
                            }
                            return RestResult.dataResult(RespCode.CODE_2.getValue(), "电池已取出", root);
                        }
                        Battery newBattery = batteryMapper.find(newBox.getBatteryId());
                        root.put("step", "wait_take_battery");
                        data.put("customerFullname", customer.getFullname());
                        data.put("isOpen", newBox.getIsOpen());
                        data.put("batteryId", newBox.getBatteryId());
                        data.put("batteryTypeName", findBatteryType(newBattery.getType()).getTypeName());
                        data.put("volume", newBattery.getVolume());
                        data.put("boxNum", newBattery.getBoxNum());
                        data.put("cabinetId", newBox.getCabinetId());
                        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);
                    }
                }

            } else { //格口中没有电池 等待放入
                root.put("step", "wait_put_battery");
                data.put("customerFullname", customer.getFullname());
                return RestResult.dataResult(RespCode.CODE_0.getValue(), "", root);
            }
        }

        return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效业务类型", root);
    }

    private Result calculatePrice(Customer customer, BatteryOrder batteryOrder, Cabinet cabinet, Battery battery) {
        Result result = new Result();
        result.payOk = false;
        PacketPeriodOrder packetPeriodOrder = null;

        packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customer.getId(), PacketPeriodOrder.Status.USED.getValue(), batteryOrder.getAgentId(), batteryOrder.getBatteryType());
        if (packetPeriodOrder == null) {
            packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customer.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), batteryOrder.getAgentId(), batteryOrder.getBatteryType());
        }
        int payType = ConstEnum.PayType.PACKET.getValue();

        ExchangeWhiteList exchangeWhiteList = exchangeWhiteListMapper.findByCustomer(batteryOrder.getAgentId(), customer.getId());
        if (exchangeWhiteList != null) { //如果是白名单客户
            result.payOk = true;
            payType = ConstEnum.PayType.BALANCE.getValue();
        } else {
            if (packetPeriodOrder == null) {

                ExchangePriceTime exchangePriceTime = exchangePriceTimeMapper.findByBatteryType(batteryOrder.getAgentId(), batteryOrder.getBatteryType());
                if (exchangePriceTime == null  || exchangePriceTime.getActiveSingleExchange() == null || exchangePriceTime.getActiveSingleExchange() == ConstEnum.Flag.FALSE.getValue()) {
                    batteryOrderMapper.updateErrorMessage(batteryOrder.getId(), new Date(), String.format("客户没有可用包时段套餐", cabinet.getId()));
                    return result;
                } else {
                    int money = 0;
                    //开启按次取按次价格
                    if (exchangePriceTime != null && exchangePriceTime.getVolumePrice() != null) {
                        money = Math.abs(battery.getVolume() - batteryOrder.getInitVolume()) * exchangePriceTime.getVolumePrice();
                    } else {
                        money = exchangePriceTime.getTimesPrice();
                    }

                    if (money == 0) {
                        result.payOk = true;
                        payType = ConstEnum.PayType.BALANCE.getValue();
                    } else {
                        batteryOrderMapper.updatePrice(battery.getOrderId(), money, money);
                    }
                }

            } else { //有包月套餐的
                if (packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.NOT_USE.getValue()) {
                    Date beginTime = new Date();
                    Date endTime =DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, packetPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH),-1);
                    int eft = packetPeriodOrderMapper.updateStatus(packetPeriodOrder.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), PacketPeriodOrder.Status.USED.getValue(), beginTime, endTime);
                    if (eft > 0) {
                        Agent agent = agentMapper.find(packetPeriodOrder.getAgentId());
                        handleLaxinCustomerByMonth(agent, customer, packetPeriodOrder.getMoney());
                    }
                }

                result.payOk = true;
            }
        }


        if (result.payOk) {
            if (packetPeriodOrder != null) {
                packetPeriodOrderMapper.updateOrderCount(packetPeriodOrder.getId());
            }
            String packetPeriodOrderId = packetPeriodOrder != null ? packetPeriodOrder.getId() : null;
            customerExchangeBatteryMapper.clearBattery(customer.getId(), batteryOrder.getBatteryId());

            batteryMapper.clearCustomer(battery.getId(), Battery.Status.IN_BOX.getValue());

            // 清空格口状态
            CabinetBox cabinetBox = cabinetBoxMapper.find(batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
            if(cabinetBox.getBatteryId() != null && cabinetBox.getBoxStatus() == CabinetBox.BoxStatus.CUSTOMER_USE.getValue()){
                cabinetBoxMapper.unlockBox(batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), CabinetBox.BoxStatus.CUSTOMER_USE.getValue(), CabinetBox.BoxStatus.FULL.getValue());

            }

            batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_PAY_OLD.getValue(), customer.getId(), customer.getMobile(), customer.getFullname(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));

            batteryOrderMapper.payOk2(batteryOrder.getId(), BatteryOrder.OrderStatus.PAY.getValue(), payType,
                    new Date(), packetPeriodOrderId, 0, 0);

            if (batteryOrder.getPayTimeoutFaultLogId() != null) {
                faultLogMapper.handle(batteryOrder.getPayTimeoutFaultLogId(),
                        FaultLog.HandleType.SYSTEM.getValue(),
                        null,
                        null,
                        new Date(),
                        FaultLog.Status.WAIT_PROCESS.getValue(),
                        FaultLog.Status.PROCESSED.getValue()
                );
            }
        }

        return result;
    }

    public RestResult subcabinetBattery(String subcabinetId) {
        Cabinet subcabinet = cabinetMapper.find(subcabinetId);
        if (subcabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        //可用电池数
        int fullCount = cabinetBoxMapper.findFullCount(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), subcabinet.getId());

        Map data = new HashMap();
        data.put("fullCount", fullCount);
        data.put("cabinetId", subcabinet.getId());
        data.put("cabinetName", subcabinet.getCabinetName());
        data.put("isOnline", subcabinet.getIsOnline());

        List boxList = new ArrayList();
        final int EMPTY_BOX = 1, LESS_THAN = 2, MORE_THAN = 3, NOT_OUT = 4, FULL_VOLTAGE = 5, NOT_ONLINE = 6, PROHIBIT = 7, ABNORMAL = 8;      //1 空箱 2 电量小于50% 3 电量大于60%小于可换电量 4 不可取出(未满和客户的电池) 5 满电状态 6 未上线

        List<CabinetBox> subcabinetBoxList = cabinetBoxMapper.findBySubcabinetId(subcabinetId);
        if (!subcabinetBoxList.isEmpty()) {
            for (CabinetBox box : subcabinetBoxList) {
                Map boxMap = new HashMap();
                int status = EMPTY_BOX;
                if (box.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
                    status = PROHIBIT;// 7 禁用
                }else if (box.getBoxStatus()  == CabinetBox.BoxStatus.BESPEAK.getValue()
                        || box.getBoxStatus()  == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()
                        || box.getBoxStatus()  == CabinetBox.BoxStatus.FULL_LOCK.getValue()
                        || box.getBoxStatus()  == CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
                    status = NOT_OUT; // 4 不可取出 锁定
                }else if (box.getBatteryId() == null) {
                    boxMap.put("boxNum", box.getBoxNum());
                    boxMap.put("volume", 0);
                    boxMap.put("status", status);
                    boxMap.put("chargeStatus", 1);
                    boxList.add(boxMap);
                    continue;
                }  else if ((box.getBoxStatus() == CabinetBox.BoxStatus.FULL.getValue() || box.getBoxStatus() == CabinetBox.BoxStatus.CUSTOMER_USE.getValue()) &&
                        (box.getBatteryStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()
                                || box.getBatteryStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue())) {
                    status = NOT_OUT; // 4 不可取出(未满和客户的电池)
                } else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
                        && box.getVolume() < 50) {
                    status = LESS_THAN;
                } else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()) {
                    if (box.getBatteryId() != null) {
                        Battery battery = batteryMapper.find(box.getBatteryId());
                        if (battery.getUpLineStatus() == BatteryInstallRecord.Status.NOTONLINE.getValue()) {
                            status = NOT_ONLINE;
                        }else if(battery.getIsNormal() == ConstEnum.Flag.FALSE.getValue()) {
                            status = ABNORMAL;//8 异常标识电池
                        }else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
                                && box.getVolume() >=50 && box.getVolume() < battery.getChargeCompleteVolume()) {
                            status = MORE_THAN;
                        }else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
                                && box.getVolume() >= battery.getChargeCompleteVolume()) {
                            status = FULL_VOLTAGE;
                        }
                    }
                }

                boxMap.put("boxNum", box.getBoxNum());
                boxMap.put("volume", box.getVolume() == null ? 0 : box.getVolume());
                boxMap.put("status", status);
                int chargeStatus = box.getChargeStatus() != null && box.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue() ? 2 : 1;
//                if (box.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue() && box.getBatteryFullVolume() <= box.getVolume()) {
//                    chargeStatus = 1;
//                }
                boxMap.put("chargeStatus", chargeStatus);

                if (box.getBatteryId() != null) {
                    Battery battery = batteryMapper.find(box.getBatteryId());
                    boxMap.put("batteryType", battery.getType());
                }else{
                    boxMap.put("batteryType", null);
                }

                boxList.add(boxMap);
            }
        }
        data.put("boxList", boxList);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    public RestResult findAddressList() {
        List<Cabinet> addressList = cabinetMapper.findAddressList();
        Set<Map> maps = new LinkedHashSet<Map>();

        for (Cabinet cabinet : addressList) {
            Map province = new HashMap();
            Area area = findArea(cabinet.getProvinceId());
            province.put("provinceId", area.getId());
            province.put("provinceName", area.getAreaName());
            maps.add(province);
        }
        for (Map map : maps) {
            List<Map> cityList = new ArrayList<Map>();
            for (Cabinet cabinet : addressList) {
                if (map.get("provinceId").equals(cabinet.getProvinceId())) {
                    Map city = new HashMap();
                    Area area = findArea(cabinet.getCityId());
                    city.put("areaName", area.getAreaName());
                    city.put("areaId", area.getId());
                    city.put("lng", area.getLongitude());
                    city.put("lat", area.getLatitude());
                    cityList.add(city);
                }
            }
            map.remove("provinceId");
            map.put("cityList", cityList);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, maps);
    }

    public int updateToken(String id, String loginToken) {
        return cabinetMapper.updateToken(id, loginToken);
    }

    public int updateOperationFlag(String id, int operationFlag) {
        return cabinetMapper.updateOperationFlag(id, operationFlag);
    }

    public static class Result {
        public String errorMessage;
        public boolean priceOk;
        public boolean payOk;

        public Result(String errorMessage, boolean payOk) {
            this.errorMessage = errorMessage;
            this.payOk = payOk;
        }

        public Result() {
        }
    }

    public String getBatteryTypeName(Integer batteryType) {
        if (batteryType != null) {
            return findBatteryType(batteryType).getTypeName();
        } else {
            return null;
        }
    }

    public int updatePrice(Integer agentId, String cabinetId) {
        Integer minPrice = null;
        Integer maxPrice = null;
        List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findByCabinetId(cabinetId);
        for (VipPriceCabinet vipPriceCabinet : vipPriceCabinetList) {
            VipPrice vipPrice = vipPriceMapper.findByIsActive(vipPriceCabinet.getPriceId(), new Date());
            int packagePrice = 0;
            List<VipExchangeBatteryForegift> vipForegiftList = vipExchangeBatteryForegiftMapper.findByPriceId(vipPrice.getId());
            for (VipExchangeBatteryForegift vipForegift : vipForegiftList) {
                ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(vipForegift.getForegiftId());
                List<VipPacketPeriodPrice> vipPacketList = vipPacketPeriodPriceMapper.findByPriceIdAndForegiftId(vipPrice.getId(), vipForegift.getForegiftId());
                for (VipPacketPeriodPrice vipPacketPeriodPrice : vipPacketList) {
                    packagePrice = (exchangeBatteryForegift.getMoney() - vipForegift.getReduceMoney()) + vipPacketPeriodPrice.getPrice();
                    if (maxPrice == null && minPrice == null) {
                        maxPrice = packagePrice;
                        minPrice = packagePrice;
                    }
                    if (packagePrice > maxPrice) {
                        maxPrice = packagePrice;
                    }
                    if (packagePrice < minPrice) {
                        minPrice = packagePrice;
                    }
                }
            }
        }
        if (maxPrice == null && minPrice == null) {
            List<CabinetBatteryType> typeList = cabinetBatteryTypeMapper.findListByCabinet(cabinetId);
            for (CabinetBatteryType type : typeList) {
                List<ExchangeBatteryForegift> foregiftList = exchangeBatteryForegiftMapper.findByAgent(agentId, type.getBatteryType());
                int packagePrice = 0;
                for (ExchangeBatteryForegift foregift : foregiftList) {
                    List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceMapper.findList(agentId, type.getBatteryType(), foregift.getId());
                    for (PacketPeriodPrice price : packetPeriodPriceList) {
                        packagePrice = foregift.getMoney() + price.getPrice();
                        if (maxPrice == null && minPrice == null) {
                            maxPrice = packagePrice;
                            minPrice = packagePrice;
                        }
                        if (packagePrice > maxPrice) {
                            maxPrice = packagePrice;
                        }
                        if (packagePrice < minPrice) {
                            minPrice = packagePrice;
                        }
                    }
                }
            }
        }
        return cabinetMapper.updatePrice(cabinetId, minPrice, maxPrice);
    }
}
