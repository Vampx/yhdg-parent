package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentInfo;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrder;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodOrderRefund;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerCouponTicketMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInOutMoneyMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.SystemBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.CabinetMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodOrderMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.PacketPeriodOrderRefundMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PacketPeriodOrderService extends AbstractService{

    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    private PacketPeriodOrderRefundMapper packetPeriodOrderRefundMapper;
    @Autowired
    private CustomerCouponTicketMapper customerCouponTicketMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerInOutMoneyMapper customerInOutMoneyMapper;
    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    ShopMapper shopMapper;

    public Map<String, Cabinet> cabinetMap = null;
    public Map<String, Shop> shopMap = null;

    public Page findPage(PacketPeriodOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(packetPeriodOrderMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<PacketPeriodOrder> list = packetPeriodOrderMapper.findPageResult(search);
        for (PacketPeriodOrder packetPeriodOrder: list) {
            Integer agentId = packetPeriodOrder.getAgentId();
            //设置运营商名称
            if (agentId != null) {
                packetPeriodOrder.setAgentName(findAgentInfo(packetPeriodOrder.getAgentId()).getAgentName());
            }
            //设置电池类型名称
            if (packetPeriodOrder.getBatteryType() != null) {
                packetPeriodOrder.setBatteryTypeName(findBatteryType(packetPeriodOrder.getBatteryType()).getTypeName());
            }
        }
        page.setResult(list);
        return page;
    }

    public Map findPageForClearing(PacketPeriodOrder search) {
        cabinetMap = new HashMap<String, Cabinet>();
        shopMap = new HashMap<String, Shop>();
        Integer totalMoney = 0;
        Map returnMap = new HashMap();
        List<PacketPeriodOrder> list = packetPeriodOrderMapper.findPageForClearingResult(search);
        for (PacketPeriodOrder packetPeriodOrder: list) {
            Integer agentId = packetPeriodOrder.getAgentId();
            if(packetPeriodOrder.getCabinetId() != null){
                //todo 取柜子分成比例
                Cabinet cabinet = getCabinet(packetPeriodOrder.getCabinetId());
                int ratio = cabinet.getShopRatio();
                int shopFixedMoney = (cabinet.getShopFixedMoney() == null ? 0 : cabinet.getShopFixedMoney());
                packetPeriodOrder.setRatio(ratio);
                packetPeriodOrder.setHopFixedMoney(shopFixedMoney);

                int ratioMoney = (int)Math.round(packetPeriodOrder.getMoney() * ratio  * 1d/100 );
                int fixedMoney = (int)Math.round(shopFixedMoney * 1d / 30 * dealDays(packetPeriodOrder.getDayCount()));
                int money = ratioMoney + fixedMoney;

                if(money > packetPeriodOrder.getMoney()){
                    money = packetPeriodOrder.getMoney();
                }
                packetPeriodOrder.setIntoMoney(money);
                totalMoney += money;
            }else {
                //todo 取门店分成比例
                Shop shop = getShop(packetPeriodOrder.getShopId());
                int ratio = shop.getShopRatio();
                int shopFixedMoney = (shop.getShopFixedMoney() == null ? 0 : shop.getShopFixedMoney());
                packetPeriodOrder.setRatio(ratio);
                packetPeriodOrder.setHopFixedMoney(shopFixedMoney);

                int ratioMoney = (int)Math.round(packetPeriodOrder.getMoney() * ratio  * 1d/100 );
                int fixedMoney = (int)Math.round(shopFixedMoney * 1d / 30 * dealDays(packetPeriodOrder.getDayCount()));
                int money = ratioMoney + fixedMoney;

                if(money > packetPeriodOrder.getMoney()){
                    money = packetPeriodOrder.getMoney();
                }
                packetPeriodOrder.setIntoMoney(money);
                totalMoney += money;
            }
            //设置运营商名称
            if (agentId != null) {
                packetPeriodOrder.setAgentName(findAgentInfo(packetPeriodOrder.getAgentId()).getAgentName());
            }
            //设置电池类型名称
            if (packetPeriodOrder.getBatteryType() != null) {
                packetPeriodOrder.setBatteryTypeName(findBatteryType(packetPeriodOrder.getBatteryType()).getTypeName());
            }
            if (packetPeriodOrder.getCabinetId() != null) {
                packetPeriodOrder.setCabinetName(getCabinet(packetPeriodOrder.getCabinetId()).getCabinetName());
            }
        }
        returnMap.put("list",list);
        returnMap.put("totalMoney",totalMoney);
        return returnMap;
    }

    public Page findPageForbalance(PacketPeriodOrder search) {
        Page page = search.buildPage();
        page.setTotalItems(packetPeriodOrderMapper.findPageForBalanceCount(search));
        search.setBeginIndex(page.getOffset());
        List<PacketPeriodOrder> list = packetPeriodOrderMapper.findPageForBalanceResult(search);
        for (PacketPeriodOrder packetPeriodOrder : list) {
            if (packetPeriodOrder.getAgentId() != null) {
                AgentInfo agentInfo = findAgentInfo(packetPeriodOrder.getAgentId());
                if (agentInfo != null) {
                    packetPeriodOrder.setAgentName(agentInfo.getAgentName());
                }
            }
            if (packetPeriodOrder.getBatteryType() != null) {
                SystemBatteryType batteryType = findBatteryType(packetPeriodOrder.getBatteryType());
                if (batteryType != null) {
                    packetPeriodOrder.setBatteryTypeName(batteryType.getTypeName());
                }
            }
        }
        page.setResult(list);
        return page;
    }

    public PacketPeriodOrder find(String id) {
        return packetPeriodOrderMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult refund(String userName, PacketPeriodOrder entity) {
//        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(entity.getId());
//        if (packetPeriodOrder == null) {
//            return ExtResult.failResult("订单不存在");
//        }
//
//        if (packetPeriodOrder.getStatus() != PacketPeriodOrder.Status.APPLY_REFUND.getValue()) {
//            return ExtResult.failResult("只有申请退款状态才可退款！");
//        }
//
//        if (entity.getRefundMoney() > packetPeriodOrder.getMoney()) {
//            return ExtResult.failResult("退款金额不能大于订单金额");
//        }
//
//        PacketPeriodOrderRefund orderRefund = new PacketPeriodOrderRefund();
//        orderRefund.setId(packetPeriodOrder.getId());
//        orderRefund.setRefundStatus(PacketPeriodOrderRefund.RefundStatus.REFUND_SUCCESS.getValue());
//        orderRefund.setRefundMoney(entity.getRefundMoney());
//        orderRefund.setRefundOperator(userName);
//        orderRefund.setRefundTime(new Date());
//        orderRefund.setMemo(entity.getMemo());
//        packetPeriodOrderRefundMapper.update(orderRefund);
//
//        packetPeriodOrderMapper.updateRefund(packetPeriodOrder.getId(),
//                entity.getRefundMoney(),
//                new Date(),
//                PacketPeriodOrder.Status.APPLY_REFUND.getValue(),
//                PacketPeriodOrder.Status.REFUND.getValue());
//
//        if (packetPeriodOrder.getCouponTicketId() != null) {
//            CustomerCouponTicket customerCouponTicket = customerCouponTicketMapper.find(packetPeriodOrder.getCouponTicketId());
//            if (customerCouponTicket != null) {
//                Date now = new Date();
//                if (now.getTime() < customerCouponTicket.getExpireTime().getTime()) {
//                    customerCouponTicketMapper.updateStatus(customerCouponTicket.getId(), CustomerCouponTicket.Status.USED.getValue(), CustomerCouponTicket.Status.NOT_USER.getValue());
//                } else {
//                    customerCouponTicketMapper.updateStatus(customerCouponTicket.getId(), CustomerCouponTicket.Status.USED.getValue(), CustomerCouponTicket.Status.EXPIRED.getValue());
//                }
//            }
//        }
//
//        int depositBalance = entity.getRefundMoney(), giftBalance = 0;
//        if (customerMapper.updateBalance(packetPeriodOrder.getCustomerId(), depositBalance, giftBalance) == 0) {
//            return ExtResult.failResult("更新账户余额出错");
//        }
//
//        CustomerInOutMoney inOutMoney = new CustomerInOutMoney();
//        inOutMoney.setCustomerId(packetPeriodOrder.getCustomerId());
//        inOutMoney.setMoney(depositBalance);
//        inOutMoney.setBizType(CustomerInOutMoney.BizType.IN_CUSTOMER_PACKET_PERIOD_ORDER_REFUND.getValue());
//        inOutMoney.setBizId(packetPeriodOrder.getId());
//        inOutMoney.setCreateTime(new Date());
//        customerInOutMoneyMapper.insert(inOutMoney);

        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult extendRent(PacketPeriodOrder entity, String operator) {
        PacketPeriodOrder dbPacketPeriodOrder = packetPeriodOrderMapper.find(entity.getId());
        //增加天数
        dbPacketPeriodOrder.setDayCount(dbPacketPeriodOrder.getDayCount() + entity.getDayCount());
        //延长过期时间
        Date newEndTime = DateUtils.addDays(dbPacketPeriodOrder.getEndTime(), entity.getDayCount());
        dbPacketPeriodOrder.setEndTime(newEndTime);
        //更新状态
        if (newEndTime.compareTo(new Date()) > 0) {
            if (dbPacketPeriodOrder.getStatus() == PacketPeriodOrder.Status.EXPIRED.getValue()) {
                dbPacketPeriodOrder.setStatus(PacketPeriodOrder.Status.USED.getValue());
            }
        }
        Date now = new Date();
        String nowDate = DateFormatUtils.format(now, Constant.DATE_TIME_FORMAT);
        String operatorMemo = "";
        if (StringUtils.isEmpty(dbPacketPeriodOrder.getOperatorMemo())) {
            operatorMemo = "" + operator + "在" + nowDate + "延长租期" + entity.getDayCount() + "天;" + "\n";
        } else {
            operatorMemo = dbPacketPeriodOrder.getOperatorMemo() + operator + "在" + nowDate + "延长租期" + entity.getDayCount() + "天;" +"\n";
        }

        int result = packetPeriodOrderMapper.extendRent(dbPacketPeriodOrder.getId(), dbPacketPeriodOrder.getDayCount(), dbPacketPeriodOrder.getEndTime(), dbPacketPeriodOrder.getStatus(), operatorMemo);
        if (result == 0) {
            return ExtResult.failResult("操作失败");
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult repulseRefund(String userName, PacketPeriodOrder entity) {
        if (StringUtils.isEmpty(entity.getMemo())) {
            return ExtResult.failResult("取消退款原因不能为空");
        }

        PacketPeriodOrder packetPeriodOrder = packetPeriodOrderMapper.find(entity.getId());
        if (packetPeriodOrder == null) {
            return ExtResult.failResult("订单不存在");
        }

        PacketPeriodOrderRefund orderRefund = new PacketPeriodOrderRefund();
        orderRefund.setId(packetPeriodOrder.getId());
        orderRefund.setRefundStatus(PacketPeriodOrderRefund.RefundStatus.REFUSE_REFUND.getValue());
        orderRefund.setRefundMoney(entity.getRefundMoney());
        orderRefund.setRefundOperator(userName);
        orderRefund.setRefundTime(new Date());
        orderRefund.setMemo(entity.getMemo());
        packetPeriodOrderRefundMapper.update(orderRefund);

        packetPeriodOrderMapper.updateRefund(packetPeriodOrder.getId(),
                packetPeriodOrder.getRefundMoney(),
                new Date(),
                PacketPeriodOrder.Status.REFUND.getValue());

        return ExtResult.successResult();
    }

    public List<PacketPeriodOrder> findCanRefundByCustomerId(Long customerId) {
        return packetPeriodOrderMapper.findCanRefundByCustomerId(customerId);
    }

    public Cabinet getCabinet(String id) {
        Cabinet cabinet = cabinetMap.get(id);
        if (cabinet == null) {
            cabinet = cabinetMapper.find(id);
            if(cabinet != null){
                cabinetMap.put(cabinet.getId(), cabinet);
            }
        }
        return cabinet;
    }

    public Shop getShop(String id) {
        Shop shop = shopMap.get(id);
        if (shop == null) {
            shop = shopMapper.find(id);
            if(shop != null){
                shopMap.put(shop.getId(), shop);
            }
        }
        return shop;
    }

    private int dealDays(int days){
        int returnDays = 0;
        if(days < 30){
            returnDays = days;
        }else if(days >= 30 && days < 60){
            returnDays = 30;
        }else if(days >= 60 && days < 90){
            returnDays = 60;
        }else if(days >= 90 && days < 180){
            returnDays = 90;
        }else if(days >= 180 && days < 360){
            returnDays = 180;
        }else if(days >= 360){
            returnDays = 360;
        }

        return returnDays;
    }


    public Page findBySoonExpiresPage(PacketPeriodOrder search,int expireDate) {
        Page page = search.buildPage();
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
         if(PacketPeriodOrder.ExpireDate.ONE_DAY.getValue()==expireDate){
             calendar.add(Calendar.DAY_OF_MONTH, +1);
        }else if(PacketPeriodOrder.ExpireDate.TWO_DAY.getValue()==expireDate){
             calendar.add(Calendar.DAY_OF_MONTH, +2);
        }else if(PacketPeriodOrder.ExpireDate.THREE_DAY.getValue()==expireDate){
             calendar.add(Calendar.DAY_OF_MONTH, +3);
        }
        if(expireDate!=PacketPeriodOrder.ExpireDate.All.getValue()){
            Date ThreeDaysdate = calendar.getTime();
            search.setCurrentTime(date);
            search.setCurrentThreeDaysTime(ThreeDaysdate);
            search.setStatus(PacketPeriodOrder.Status.USED.getValue());
        }else{
            search.setStatus(PacketPeriodOrder.Status.EXPIRED.getValue());
        }
        search.setBeginIndex(page.getOffset());
        page.setTotalItems(packetPeriodOrderMapper.findBySoonExpirePageCount(search));
        List<PacketPeriodOrder> list = packetPeriodOrderMapper.findBySoonExpirePageResult(search);
        for (PacketPeriodOrder packetPeriodOrder: list) {
            Integer agentId = packetPeriodOrder.getAgentId();
            //设置运营商名称
            if (agentId != null) {
                packetPeriodOrder.setAgentName(findAgentInfo(packetPeriodOrder.getAgentId()).getAgentName());
            }
            //设置电池类型名称
            if (packetPeriodOrder.getBatteryType() != null) {
                packetPeriodOrder.setBatteryTypeName(findBatteryType(packetPeriodOrder.getBatteryType()).getTypeName());
            }
        }
        page.setResult(list);
        return page;
    }
}
