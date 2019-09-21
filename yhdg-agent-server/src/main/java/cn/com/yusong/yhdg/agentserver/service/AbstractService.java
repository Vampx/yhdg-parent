package cn.com.yusong.yhdg.agentserver.service;


import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import jxl.format.UnderlineStyle;
import jxl.write.Alignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AbstractService extends cn.com.yusong.yhdg.common.service.AbstractService {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    SmsConfigMapper smsConfigMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    OrderIdMapper orderIdMapper;
    @Autowired
    DictItemMapper dictItemMapper;
    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;
    @Autowired
    protected AppConfig appConfig;
    @Autowired
    protected WxMpServiceHolder wxMpServiceHolder;
    @Autowired
    protected WxPayServiceHolder wxPayServiceHolder;
    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    CustomerCouponTicketGiftMapper customerCouponTicketGiftMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;
    @Autowired
    VipPriceCabinetMapper vipPriceCabinetMapper;
    @Autowired
    VipPacketPeriodPriceMapper vipPacketPeriodPriceMapper;
    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;
    @Autowired
    VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    ShopMapper shopMapper;

    public String findConfigValue(String id) {
        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, id);
        String value = (String) memCachedClient.get(key);
        if (value != null) {
            return value;
        }
        value = systemConfigMapper.findConfigValue(id);
        if (value != null) {
            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return value;
    }

    public String newOrderId(OrderId.OrderIdType type) {
        Calendar calendar = new GregorianCalendar();
        int suffix = calendar.get(Calendar.YEAR);
        OrderId orderId = new OrderId(type, suffix);
        orderIdMapper.insert(orderId);
        long id = orderId.getId();

        return newOrderId(id, calendar, type);
    }

    public void getAgentParentName (Agent agent) {
        agent.setParentName(agentMapper.find(agent.getParentId()).getAgentName());
    }

    public Partner findPartner(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_PARTNER, id);
        Partner partner = (Partner) memCachedClient.get(key);
        if(partner != null) {
            return partner;
        }
        partner =  partnerMapper.find(id);
        if(partner != null) {
            memCachedClient.set(key, partner, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return partner;
    }

    protected SmsConfigInfo findSmsConfigInfo(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_SMS_CONFIG_INFO, id);
        SmsConfigInfo info = (SmsConfigInfo) memCachedClient.get(key);
        if(info != null) {
            return info;
        }
        info = smsConfigMapper.findInfo(id);
        if(info != null) {
            memCachedClient.set(key, info, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return info;
    }

    protected List<DictItem> findByCategory(int categoryId) {
        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, categoryId);
        List<DictItem> v = (List<DictItem>) memCachedClient.get(key);
        if(v != null) {
            return v;
        }

        v = dictItemMapper.findByCategory(categoryId);
        memCachedClient.set(key, v, MemCachedConfig.CACHE_ONE_WEEK);
        return v;
    }

    protected SystemBatteryType findBatteryType(int batteryType) {
        String key = CacheKey.key(CacheKey.K_BATTERY_TYPE_V_TYPE_NAME, batteryType);
        SystemBatteryType value = (SystemBatteryType) memCachedClient.get(key);
        if (value != null) {
            return value;
        }
        value = systemBatteryTypeMapper.find(batteryType);
        if (value != null) {
            memCachedClient.set(key, value, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return value;
    }

    protected Map<String, String> findDictItemMap(int categoryId) {
        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_MAP, categoryId);
        Map<String, String> v = (Map<String, String>) memCachedClient.get(key);
        if(v != null) {
            return v;
        }

        v = new HashMap<String, String>();

        List<DictItem> itemList = dictItemMapper.findByCategory(categoryId);
        for(DictItem e : itemList) {
            v.put(e.getItemValue(), e.getItemName());
        }
        memCachedClient.set(key, v, MemCachedConfig.CACHE_ONE_WEEK);
        return v;
    }
    public AgentInfo findAgentInfo(int id) {
        String key = CacheKey.key(CacheKey.K_ID_V_AGENT_INFO, id);
        AgentInfo agentInfo = (AgentInfo) memCachedClient.get(key);
        if(agentInfo != null) {
            return agentInfo;
        }
        agentInfo =  agentMapper.findAgentInfo(id);
        if(agentInfo != null) {
            memCachedClient.set(key, agentInfo, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return agentInfo;
    }

    public CustomerCouponTicketGift findCustomerCouponTicketGift(int id) {
        String key = CacheKey.key(CacheKey.K_CUSTOMER_COUPON_TICKET_GIFT_ID, id);
        CustomerCouponTicketGift customerCouponTicketGift = (CustomerCouponTicketGift) memCachedClient.get(key);
        if(customerCouponTicketGift != null) {
            return customerCouponTicketGift;
        }
        customerCouponTicketGift =  customerCouponTicketGiftMapper.find(id);
        if(customerCouponTicketGift != null) {
            memCachedClient.set(key, customerCouponTicketGift, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return customerCouponTicketGift;
    }

    public Map<String, String> findDictItemMap(long categoryId) {

        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_MAP, categoryId);
        Map<String, String> dictItemMap = (Map<String, String> ) memCachedClient.get(key);
        if(dictItemMap != null) {
            return dictItemMap;
        }

        List<DictItem> itemList = dictItemMapper.findByCategory(categoryId);
        for(DictItem e : itemList) {
            dictItemMap.put(e.getItemValue(), e.getItemName());
        }

        memCachedClient.set(key, itemList, MemCachedConfig.CACHE_ONE_WEEK);
        return dictItemMap;
    }

    public static Date yesterdayBeginTime() {
        Date beginTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Calendar cal = new GregorianCalendar();
        cal.setTime(beginTime);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    public static Date yesterdayEndTime() {
        Date beginTime = DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        Date endTime = DateUtils.addSeconds(DateUtils.addDays(beginTime, 1), -1);
        Calendar cal = new GregorianCalendar();
        cal.setTime(endTime);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }

    protected static WritableCellFormat getHeadStyle() throws WriteException {
        WritableFont wf2 = new WritableFont(WritableFont.TAHOMA, 11, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
        WritableCellFormat wc = new WritableCellFormat(wf2);
        // 设置居中
        wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wc.setAlignment(Alignment.CENTRE);
        return wc;
    }

    protected static WritableCellFormat getTitleStyle() throws WriteException {
        WritableFont wf2 = new WritableFont(WritableFont.TAHOMA, 16, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK); // 定义格式 字体 下划线 斜体 粗体 颜色
        WritableCellFormat wc = new WritableCellFormat(wf2);
        // 设置居中
        wc.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
        wc.setAlignment(Alignment.CENTRE);
        return wc;
    }

    public int updatePrice(Integer agentId, String cabinetId) {
        Integer minPrice = null;
        Integer maxPrice = null;
        List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findByCabinetId(cabinetId);
        int packagePrice = 0;
        for (VipPriceCabinet vipPriceCabinet : vipPriceCabinetList) {
            VipPrice vipPrice = vipPriceMapper.findByIsActive(vipPriceCabinet.getPriceId(), new Date());
            if (vipPrice != null) {
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
        }
        if (maxPrice == null && minPrice == null) {
            List<CabinetBatteryType> typeList = cabinetBatteryTypeMapper.findListByCabinet(cabinetId);
            for (CabinetBatteryType type : typeList) {
                List<ExchangeBatteryForegift> foregiftList = exchangeBatteryForegiftMapper.findByAgent(agentId, type.getBatteryType());
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

    public int updateShopPrice(long vipPriceId, String shopId) {
        Integer minPrice = null;
        Integer maxPrice = null;
        int packagePrice = 0;
        VipPrice vipPrice = vipPriceMapper.findByIsActive(vipPriceId, new Date());
        if (vipPrice != null) {
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
            List<Cabinet> cabinetList = cabinetMapper.findListByShopId(shopId);
            for (Cabinet cabinet : cabinetList) {
                if (maxPrice == null && minPrice == null) {
                    minPrice = cabinet.getMinPrice();
                    maxPrice = cabinet.getMaxPrice();
                }
                if (minPrice > cabinet.getMinPrice()) {
                    minPrice = cabinet.getMinPrice();
                }
                if (maxPrice < cabinet.getMaxPrice()) {
                    maxPrice = cabinet.getMaxPrice();
                }
            }
        }
        return shopMapper.updatePrice(shopId, minPrice, maxPrice);
    }

    public int updateShopPriceByCabint(String shopId){
        Integer minPrice = null;
        Integer maxPrice = null;
        List<Cabinet> cabinetList = cabinetMapper.findListByShopId(shopId);
        for (Cabinet cabinet : cabinetList) {
            if (maxPrice == null && minPrice == null) {
                minPrice = cabinet.getMinPrice();
                maxPrice = cabinet.getMaxPrice();
            }
            if (minPrice > cabinet.getMinPrice()) {
                minPrice = cabinet.getMinPrice();
            }
            if (maxPrice < cabinet.getMaxPrice()) {
                maxPrice = cabinet.getMaxPrice();
            }
        }
        return shopMapper.updatePrice(shopId, minPrice, maxPrice);
    }

}