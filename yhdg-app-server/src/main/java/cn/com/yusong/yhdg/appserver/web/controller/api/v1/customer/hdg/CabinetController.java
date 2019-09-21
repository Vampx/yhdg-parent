package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.biz.client.ClientBizUtils;
import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.entity.NewBoxNum;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller("api_v1_customer_hdg_cabinet")
@RequestMapping(value = "/api/v1/customer/hdg/cabinet")
public class CabinetController extends ApiController {

    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    AreaCache areaCache;
    @Autowired
    CustomerService customerService;
    @Autowired
    DictItemService dictItemService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CabinetOperateLogService cabinetOperateLogService;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    CabinetDynamicCodeCustomerService cabinetDynamicCodeCustomerService;
    @Autowired
    CabinetBatteryTypeService cabinetBatteryTypeService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    SystemBatteryTypeService systemBatteryTypeService;
    @Autowired
    ExchangeBatteryForegiftService exchangeBatteryForegiftService;
    @Autowired
    PacketPeriodPriceService packetPeriodPriceService;
    @Autowired
    CustomerWhitelistService customerWhitelistService;
    @Autowired
    WeixinmpService weixinmpService;
    @Autowired
    AlipayfwService alipayfwService;
    @Autowired
    PhoneappService phoneappService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    CabinetDayStatsService cabinetDayStatsService;
    @Autowired
    CabinetDayDegreeStatsService cabinetDayDegreeStatsService;
    @Autowired
    PacketPeriodOrderService packetPeriodOrderService;


    static final Logger log = LogManager.getLogger(CabinetController.class);

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NologinNearestParam {
        public int appId;
        public int areaId;
        public double lng;
        public double lat;
        public Integer offset;
        public Integer limit;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/nologin_nearest_by_mp")
    public RestResult nologinNearestByMp(@Valid @RequestBody NologinNearestParam param) {
        return nologinNearest(1, param);
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/nologin_nearest_by_fw")
    public RestResult nologinNearestByFw(@Valid @RequestBody NologinNearestParam param) {
        return nologinNearest(2, param);
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/nologin_nearest_by_app")
    public RestResult nologinNearestByApp(@Valid @RequestBody NologinNearestParam param) {
        return nologinNearest(3, param);
    }

    public RestResult nologinNearest(int type, NologinNearestParam param) {
        Area area = areaCache.get(param.areaId);
        if(area == null) {
            area = areaCache.get(param.areaId / 100 * 100);
            if (area == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }


        Area province = null, city = null;
        if(area.getAreaLevel() == 3) {
            city = areaCache.get(area.getParentId());
        } else if(area.getAreaLevel() == 2) {
            city = area;
        } else if(area.getAreaLevel() == 1) {
            province = area;
        }

        if (city != null) {
            province = areaCache.get(city.getParentId());
            if(province == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }
        if (province == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        if (param == null || param.lng == 0 || param.lat == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "经纬度错误");
        }

        String  geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);

        List<Integer> agentId = new ArrayList<Integer>();
        if (type == 1 && param.appId > 0) {
            Weixinmp weixinmp = weixinmpService.find(param.appId);
            if (weixinmp == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "公众号配置不存在");
            }
            agentId.addAll(agentService.findByWeixinmp(weixinmp.getId()));
            if (agentId.isEmpty()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "公众号没有关联运营商");
            }
        } else if (type == 2 && param.appId > 0) {
            Alipayfw alipayfw = alipayfwService.find(param.appId);
            if (alipayfw == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "生活号配置不存在");
            }
            agentId.addAll(agentService.findByAlipayfw(alipayfw.getId()));
            if (agentId.isEmpty()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "生活号没有关联运营商");
            }
        } else if (type == 3 && param.appId > 0) {
            Phoneapp phoneapp = phoneappService.find(param.appId);
            if (phoneapp == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "手机APP配置不存在");
            }
            agentId.addAll(agentService.findByPhoneapp(phoneapp.getId()));
            if (agentId.isEmpty()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "手机APP没有关联运营商");
            }
        }

        //移除智租
        if(agentId != null && !agentId.isEmpty()){
            Iterator<Integer> it_b=agentId.iterator();
            while(it_b.hasNext()){
                Integer a=it_b.next();
                if (a==23) {
                    it_b.remove();
                }
            }
        }

        List<Cabinet> list = cabinetService.findNearest(
                agentId,
                geoHash,
                param.lng,
                param.lat,
                null,
                province == null ? null : province.getId(),
                city == null ? null: city.getId(),
                null,
                null,
                param.offset,
                param.limit);

        List<Map> result = new ArrayList<Map>();
        for (Cabinet cabinet : list) {
            Map line = new HashMap();
            line.put("id", cabinet.getId());
            line.put("agentId", cabinet.getAgentId());
            line.put("cabinetName", cabinet.getCabinetName());
            line.put("address", cabinet.getAddress());
            line.put("lng", cabinet.getLng());
            line.put("lat", cabinet.getLat());
            line.put("tel", cabinet.getTel() != null ? cabinet.getTel() : "");
            line.put("workTime", cabinet.getWorkTime()!= null ? cabinet.getWorkTime() : "");
            line.put("isOnline", cabinet.getIsOnline() != null && cabinet.getIsOnline() > 0 ? 1 : 0);
            line.put("distance", cabinet.getDistance());
            line.put("fullCount", cabinet.getFullCount());
            line.put("chargeCount", cabinet.getChargeCount());
            line.put("lockCount", cabinet.getLockCount());
            line.put("emptyCount", cabinet.getEmptyCount());
            line.put("signal", cabinet.getCurrentSignal());
            line.put("networkType", cabinet.getNetworkType());
            line.put("upLineStatus", cabinet.getUpLineStatus());
            line.put("imagePath", staticImagePath(cabinet.getImagePath1()));
            line.put("reserveCount", cabinet.getReserveCount());
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NearestParam {
        public int areaId;
        public double lng;
        public double lat;
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "/nearest")
    public RestResult nearest(@Valid @RequestBody NearestParam param) {
        TokenCache.Data tokenData = getTokenData();
        Area area = areaCache.get(param.areaId);
        if(area == null) {
            area = areaCache.get(param.areaId / 100 * 100);
            if (area == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }


        Area province = null, city = null;
        if(area.getAreaLevel() == 3) {
            city = areaCache.get(area.getParentId());
        } else if(area.getAreaLevel() == 2) {
            city = area;
        } else if(area.getAreaLevel() == 1) {
            province = area;
        }

        if (city != null) {
            province = areaCache.get(city.getParentId());
            if(province == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }
        if (province == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        if (param == null || param.lng == 0 || param.lat == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "经纬度错误");
        }

        List<Integer> agentId = null;
        String geoHash = null;

        Customer customer = customerService.find(getTokenData().customerId);
        if(customer == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }


        //判断用户只能看到自己运营商下的柜子，如果没有运营商，只能看到自己平台下的柜子
        if (customer.getAgentId() == null) {
            agentId = new ArrayList();
            //判断公众号和生活号绑定的运营商
            if (tokenData.clientType == 1 && tokenData.appId > 0) {
                agentId.addAll(agentService.findByWeixinmp(tokenData.appId));
            } else if (tokenData.clientType  == 2 && tokenData.appId > 0) {
                agentId.addAll(agentService.findByAlipayfw(tokenData.appId));
            } else if (tokenData.clientType == 3 && tokenData.appId > 0) {
                agentId.addAll(agentService.findByPhoneapp(tokenData.appId));
            }else{
                agentId.addAll(agentService.findByPartner(customer.getPartnerId()));
            }
        } else {
            agentId = new ArrayList();
            agentId.add(customer.getAgentId());
        }

        //移除智租
        if(agentId != null && !agentId.isEmpty()){
            Iterator<Integer> it_b=agentId.iterator();
            while(it_b.hasNext()){
                Integer a=it_b.next();
                if (a==23) {
                    it_b.remove();
                }
            }
        }


        //判断是否是白名单用户，白名单没有距离限制
        int isWhite = customerWhitelistService.findByMobile(null, null, customer.getMobile());
        if (isWhite > 0) {
            //判断是否平台白名单，平台白名单没有设备限制
            if( customerWhitelistService.findByMobile(Constant.SYSTEM_PARTNER_ID, null, customer.getMobile()) > 0 ){
                agentId = null;
            }
        }else {
            //普通用户默认查找20公里内的柜子
            geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);
        }

        //如果是独享用户，只能看到独享柜，其他用户，无法看到独享柜
        String unSharedCabinetId = null;
        Integer viewType = null;
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
        if(customerExchangeInfo != null){
            viewType = Cabinet.ViewType.SHARED.getValue();
            if( customerExchangeInfo.getBalanceCabinetId() != null){
                Cabinet customerCabinet =  cabinetService.find( customerExchangeInfo.getBalanceCabinetId());
                if(customerCabinet != null && customerCabinet.getViewType() == Cabinet.ViewType.UNSHARED.getValue()){
                    unSharedCabinetId = customerCabinet.getId();
                    viewType = Cabinet.ViewType.UNSHARED.getValue();
                }
            }
        }

        List<Cabinet> list = cabinetService.findNearest(
                agentId,
                geoHash,
                param.lng,
                param.lat,
                null,
                province == null ? null : province.getId(),
                city == null ? null: city.getId(),
                viewType,
                unSharedCabinetId,
                param.offset,
                param.limit);

        List<Map> result = new ArrayList<Map>();
        for (Cabinet cabinet : list) {
            Map line = new HashMap();
            line.put("id", cabinet.getId());
            line.put("agentId", cabinet.getAgentId());
            line.put("cabinetName", cabinet.getCabinetName());
            line.put("address", cabinet.getAddress());
            line.put("lng", cabinet.getLng());
            line.put("lat", cabinet.getLat());
            line.put("tel", cabinet.getTel() != null ? cabinet.getTel() : "");
            line.put("workTime", cabinet.getWorkTime()!= null ? cabinet.getWorkTime() : "");
            line.put("isOnline", cabinet.getIsOnline() != null && cabinet.getIsOnline() > 0 ? 1 : 0);
            line.put("distance", cabinet.getDistance());
            line.put("fullCount", cabinet.getFullCount());
            line.put("chargeCount", cabinet.getChargeCount());
            line.put("lockCount", cabinet.getLockCount());
            line.put("emptyCount", cabinet.getEmptyCount());
            line.put("signal", cabinet.getCurrentSignal());
            line.put("networkType", cabinet.getNetworkType());
            line.put("upLineStatus", cabinet.getUpLineStatus());
            line.put("imagePath", staticImagePath(cabinet.getImagePath1()));
            line.put("reserveCount", cabinet.getReserveCount());
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchByKeywordParam {
        public int areaId;
        public double lng;
        public double lat;
        public String keyword;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/search_by_keyword")
    public RestResult searchKeyword(@Valid @RequestBody SearchByKeywordParam param) {
        TokenCache.Data tokenData = getTokenData();
        Area area = areaCache.get(param.areaId);
        if(area == null) {
            area = areaCache.get(param.areaId / 100 * 100);
            if (area == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }


        Area province = null, city = null;
        if(area.getAreaLevel() == 3) {
            city = areaCache.get(area.getParentId());
        } else if(area.getAreaLevel() == 2) {
            city = area;
        } else if(area.getAreaLevel() == 1) {
            province = area;
        }

        if (city != null) {
            province = areaCache.get(city.getParentId());
            if(province == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }
        if (province == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        List<Integer> agentId = null;
        String geoHash = null;

        Customer customer = customerService.find(tokenData.customerId);
        if(customer == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }

        //判断用户只能看到自己运营商下的柜子，如果没有运营商，只能看到自己平台下的柜子
        if (customer.getAgentId() == null) {
            agentId = new ArrayList();
            //判断公众号和生活号绑定的运营商
            if (tokenData.clientType == 1 && tokenData.appId > 0) {
                agentId.addAll(agentService.findByWeixinmp(tokenData.appId));
            } else if (tokenData.clientType  == 2 && tokenData.appId > 0) {
                agentId.addAll(agentService.findByAlipayfw(tokenData.appId));
            } else if (tokenData.clientType == 3 && tokenData.appId > 0) {
                agentId.addAll(agentService.findByPhoneapp(tokenData.appId));
            }else{
                agentId.addAll(agentService.findByPartner(customer.getPartnerId()));
            }
        } else {
            agentId = new ArrayList();
            agentId.add(customer.getAgentId());
        }

        //判断是否是白名单用户，白名单没有距离限制
        int isWhite = customerWhitelistService.findByMobile(null, null, customer.getMobile());
        if (isWhite > 0) {
            //判断是否平台白名单，平台白名单没有设备限制
            if( customerWhitelistService.findByMobile(Constant.SYSTEM_PARTNER_ID, null, customer.getMobile()) > 0 ){
                agentId = null;
            }
        }else {
            //普通用户默认查找20公里内的柜子
            geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);
        }

        //如果是独享用户，只能看到独享柜，其他用户，无法看到独享柜
        String unSharedCabinetId = null;
        Integer viewType = null;
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
        if(customerExchangeInfo != null){
            viewType = Cabinet.ViewType.SHARED.getValue();
            if( customerExchangeInfo.getBalanceCabinetId() != null){
                Cabinet customerCabinet =  cabinetService.find( customerExchangeInfo.getBalanceCabinetId());
                if(customerCabinet != null && customerCabinet.getViewType() == Cabinet.ViewType.UNSHARED.getValue()){
                    unSharedCabinetId = customerCabinet.getId();
                    viewType = Cabinet.ViewType.UNSHARED.getValue();
                }
            }
        }

        List<Cabinet> list = cabinetService.findNearest(
                agentId,
                geoHash,
                param.lng,
                param.lat,
                param.keyword,
                province == null ? null : province.getId(),
                city == null ? null : city.getId(),
                viewType,
                unSharedCabinetId,
                param.offset,
                param.limit);


        List<Map> result = new ArrayList<Map>();
        for (Cabinet cabinet : list) {
            Map line = new HashMap();
            line.put("id", cabinet.getId());
            line.put("agentId", cabinet.getAgentId());
            line.put("cabinetName", cabinet.getCabinetName());
            line.put("address", cabinet.getAddress());
            line.put("lng", cabinet.getLng());
            line.put("lat", cabinet.getLat());
            line.put("tel", cabinet.getTel() != null ? cabinet.getTel() : "");
            line.put("workTime", cabinet.getWorkTime()!= null ? cabinet.getWorkTime() : "");
            line.put("isOnline", cabinet.getIsOnline() != null && cabinet.getIsOnline() > 0 ? 1 : 0);
            line.put("distance", cabinet.getDistance());
            line.put("fullCount", cabinet.getFullCount());
            line.put("chargeCount", cabinet.getChargeCount());
            line.put("lockCount", cabinet.getLockCount());
            line.put("emptyCount", cabinet.getEmptyCount());
            line.put("signal", cabinet.getCurrentSignal());
            line.put("networkType", cabinet.getNetworkType());
            line.put("upLineStatus", cabinet.getUpLineStatus());
            line.put("imagePath1", staticImagePath(cabinet.getImagePath1()));
            line.put("imagePath2", staticImagePath(cabinet.getImagePath2()));
            line.put("reserveCount", cabinet.getReserveCount());
            result.add(line);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoParam {
        public String id;
        public double lng;
        public double lat;
    }

    @ResponseBody
    @RequestMapping(value = "/info")
    public RestResult info(@Valid @RequestBody InfoParam param) {
        Cabinet cabinet = cabinetService.find(param.id);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }
        double distance = GeoHashUtils.getDistanceInMeters(param.lng, param.lat, cabinet.getLng(), cabinet.getLat());

        List<Map> batteries = batteryService.findBoxBattery(param.id);
        List<Map> fullList = new ArrayList<Map>();
//        Map<String, String> dictItemMap = dictItemService.findMapByCategory(DictCategory.CategoryType.BATTERY_TYPE.getValue());

        for (Map map : batteries) {
            Map full = new HashMap();
  //          String type = dictItemMap.get(map.get("type").toString());
    //        full.put("type", type != null ? type : map.get("type").toString());
            full.put("count", Integer.parseInt(map.get("count").toString()));
            fullList.add(full);
        }

        Map line = new HashMap();
        line.put("id", cabinet.getId());
        line.put("cabinetName", cabinet.getCabinetName());
        line.put("address", cabinet.getAddress());
        line.put("emptyCount", cabinetBoxService.findEmptyCount(param.id));
        line.put("fullCount", cabinetBoxService.findFullCount(param.id));
        line.put("distance", distance);
        line.put("lng", cabinet.getLng());
        line.put("lat", cabinet.getLat());
        line.put("openTime", cabinet.getWorkTime()!= null ? cabinet.getWorkTime() : "");
        line.put("tel", cabinet.getTel() != null ? cabinet.getTel() : "");
        line.put("isOnline", cabinet.getIsOnline());
        line.put("fullList", fullList);
        line.put("heartTime", DateFormatUtils.format(cabinet.getHeartTime(), Constant.DATE_TIME_FORMAT));

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoxInfoParam {
        public String id;
        public double lng;
        public double lat;
    }

    /**
     * 57-查询站点格口明细
     * @param param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/box_info")
    public RestResult boxInfo(@Valid @RequestBody BoxInfoParam param) {
        TokenCache.Data tokenData = getTokenData();
        Long customerId = tokenData.customerId;
        Customer customer = customerService.find(tokenData.customerId);
        if(customer == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }
        Cabinet cabinet = cabinetService.find(param.id);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }
        double distance = GeoHashUtils.getDistanceInMeters(param.lng, param.lat, cabinet.getLng(), cabinet.getLat());

        Map line = new HashMap();

        line.put("id", cabinet.getId());
        line.put("cabinetName", cabinet.getCabinetName());
        line.put("agentId", cabinet.getAgentId());
        line.put("address", cabinet.getAddress());
        line.put("distance", distance);
        line.put("lng", cabinet.getLng());
        line.put("lat", cabinet.getLat());
        line.put("tel", cabinet.getTel() != null ? cabinet.getTel() : "");
        String imagePath1 = staticImagePath(cabinet.getImagePath1());
        String imagePath2 = staticImagePath(cabinet.getImagePath2());
        if (StringUtils.isEmpty(imagePath1) && StringUtils.isEmpty(imagePath2)) {
            String[] imagePathList = {};
            line.put("imagePathList", imagePathList);
        } else {
            String[] imagePathList = {imagePath1, imagePath2};
            line.put("imagePathList", imagePathList);
        }

        line.put("chargeFullVolume", cabinet.getChargeFullVolume());
        line.put("minExchangeVolume", cabinet.getMinExchangeVolume());

        //换电订单
        line.put("orderCount", 0);
        //押金数-骑手
        line.put("foregiftCount", 0);
        //换电人数
        line.put("activeCount",0);
        //电费
        line.put("referencePrice",0);

        //白名单显示
        int isWhite1 = customerWhitelistService.findByMobile(null, cabinet.getAgentId(), customer.getMobile());
        int isWhite2 = customerWhitelistService.findByMobile(Constant.SYSTEM_PARTNER_ID, null, customer.getMobile());
        if (isWhite1 > 0 || isWhite2 > 0) {
            String  statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
            CabinetDayStats cabinetDayStats = cabinetDayStatsService.findForCabinet(cabinet.getId(), statsDate);
            if(cabinetDayStats != null){
                //换电订单次数
                line.put("orderCount", cabinetDayStats.getOrderCount());
                //押金数-骑手注册数
                line.put("foregiftCount", cabinetDayStats.getForegiftCount());
                //换电人数
                line.put("activeCount", cabinetDayStats.getActiveCustomerCount());
            }

            //平均电费
            CabinetDayDegreeStats cabinetDayDegreeStats = cabinetDayDegreeStatsService.findLast(cabinet.getId());
            if(cabinetDayDegreeStats != null){
                if(cabinetDayStats != null && cabinetDayStats.getActiveCustomerCount() != 0){
                    line.put("referencePrice",  (cabinetDayDegreeStats.getNum() * cabinet.getPrice() * 100)/cabinetDayStats.getActiveCustomerCount());
                } else {
                    line.put("referencePrice", 0);
                }
            }

            line.put("isWhite", ConstEnum.Flag.TRUE.getValue());
        } else {
            line.put("isWhite", ConstEnum.Flag.FALSE.getValue());
        }

        if (cabinet != null) {
            line.put("isOnline", cabinet.getIsOnline());
            line.put("currentSignal", cabinet.getCurrentSignal());
            line.put("heartTime", cabinet.getHeartTime() == null ? "" : DateFormatUtils.format(cabinet.getHeartTime(), Constant.DATE_TIME_FORMAT));
        } else {
            line.put("isOnline", ConstEnum.Flag.FALSE.getValue());
            line.put("currentSignal", -1);
            line.put("heartTime", "");
        }

        List<Map> boxList = new ArrayList<Map>();
        int canChange = 0;
        int emptyCount = 0;
        String predictBoxNum = null;
        Integer batteryVolume = null;
        List<CabinetBox> cabinetBoxes = cabinetBoxService.findByCabinet(cabinet.getId());
        final int EMPTY_BOX = 1, NOT_OUT = 2, ORANGE = 3, FULL_POWER = 4, PROHIBIT = 5, MY_BATTERY = 6, BESPEAK = 7, ABNORMAL = 8;       //1 空箱 2 不可取出(未满和客户的电池) 3 橙色 4 满电 5禁用 6我的电池 8异常标识电池
        for (CabinetBox box : cabinetBoxes) {
            Map boxMap = new HashMap();
            boxMap.put("boxNum", box.getBoxNum());
            boxMap.put("batteryId", box.getBatteryId());
            boxMap.put("isNormal", null);
            if (StringUtils.isNotEmpty(box.getBatteryId())) {
                Battery battery = batteryService.find(box.getBatteryId());
                if (battery != null) {
                    boxMap.put("isNormal", battery.getIsNormal());
                }
            }
            boxMap.put("volume", box.getVolume() == null ? 0 : box.getVolume());
            boxMap.put("chargeStatus", box.getChargeStatus());
            int status = EMPTY_BOX; //1 空箱  空闲
            int expectFullTime = 0;
            if (box.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
                status = PROHIBIT;// 5 禁用
            } else if (box.getBatteryId() != null) {
                if ((box.getBatteryStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()
                        || box.getBatteryStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) && customerId.equals(box.getCustomerId())) {
                    status = MY_BATTERY; //6 我的电池
                } else if (box.getBatteryIsNormal() == ConstEnum.Flag.FALSE.getValue()) {
                    status = ABNORMAL;//8 异常标识电池
                } else if (box.getBoxStatus() == CabinetBox.BoxStatus.BESPEAK.getValue()) {
                    status = BESPEAK; // 预约
                } else if (box.getBoxStatus() == CabinetBox.BoxStatus.CUSTOMER_USE.getValue() &&
                        (box.getBatteryStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()
                                || box.getBatteryStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue())) {
                    status = NOT_OUT; // 2 不可取出(未满和客户的电池)
                } else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
                        && box.getVolume() < box.getBatteryFullVolume()) {
                    status = ORANGE; // 3 未充满 橙色
                }else if (box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
                            && box.getVolume() >= box.getBatteryFullVolume()
                            && box.getBatteryIsNormal() == ConstEnum.Flag.TRUE.getValue()) {
                        status = FULL_POWER; //4 满电(可换)
                        if (batteryVolume == null) {
                            batteryVolume = box.getVolume();
                            predictBoxNum = box.getBoxNum();
                        }
                        if (batteryVolume < box.getVolume()) {
                            batteryVolume = box.getVolume();
                            predictBoxNum = box.getBoxNum();
                        }
                        canChange++;
                }
                if (box.getVolume() != null && box.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
//                    Battery battery = batteryService.find(box.getBatteryId());
//                    if(battery.getCurrentCapacity() != null && battery.getElectricity() != null && battery.getElectricity() > 0 ){
//                        int totalCapacity = battery.getCurrentCapacity() * 100 / box.getVolume() ;
//                        if(battery.getTotalCapacity() != null){
//                            totalCapacity = battery.getTotalCapacity();
//                        }
//                        expectFullTime = (totalCapacity - battery.getCurrentCapacity()) / battery.getElectricity() * 60;
//
//                    }else{
//                        expectFullTime = (int) ((100 - box.getVolume()) * 1.2);
//                    }

                    expectFullTime = (int) Math.ceil(((100 - box.getVolume()) * 0.7));
                }
            } else if(box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()
                    || box.getBoxStatus() == CabinetBox.BoxStatus.FULL_LOCK.getValue()
                    || box.getBoxStatus() == CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
                status = NOT_OUT; // 2 不可取出(占箱的格口)
            }
            if (box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY.getValue()){
                emptyCount++;
            }
            boxMap.put("status", status);
            boxMap.put("expectFullTime", expectFullTime);
            boxList.add(boxMap);
        }
        line.put("emptyCount", emptyCount);
        line.put("predictBoxNum", predictBoxNum);
        line.put("batteryVolume", batteryVolume);
        line.put("canChange", canChange);
        line.put("boxList", boxList);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }

    /**
     * 79-查询预约柜子详情
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BespeakBoxInfoParam {
        public String id;
        public double lng;
        public double lat;
    }

    @ResponseBody
    @RequestMapping(value = "/bespeak_box_info")
    public RestResult bespeakBoxInfo(@Valid @RequestBody BespeakBoxInfoParam param) {
        TokenCache.Data tokenData = getTokenData();
        Long customerId = tokenData.customerId;
        Cabinet cabinet = cabinetService.find(param.id);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }
        double distance = GeoHashUtils.getDistanceInMeters(param.lng, param.lat, cabinet.getLng(), cabinet.getLat());

        Map line = new HashMap();

        line.put("id", cabinet.getId());
        line.put("cabinetName", cabinet.getCabinetName());
        line.put("agentId", cabinet.getAgentId());
        line.put("address", cabinet.getAddress());
        line.put("distance", distance);
        line.put("lng", cabinet.getLng());
        line.put("lat", cabinet.getLat());
        line.put("tel", cabinet.getTel() != null ? cabinet.getTel() : "");
        String[] imageList = new String[]{staticImagePath(cabinet.getImagePath1()), staticImagePath(cabinet.getImagePath2())};
        line.put("imageList", imageList);

        line.put("isOnline", cabinet.getIsOnline());
        line.put("currentSignal", cabinet.getCurrentSignal());
        line.put("heartTime", cabinet.getHeartTime() == null ? "" : DateFormatUtils.format(cabinet.getHeartTime(), Constant.DATE_TIME_FORMAT));

        String  bespeakMaxCacel = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BESPEAK_MAX_CANCEL.getValue(), cabinet.getAgentId());
        if(StringUtils.isEmpty(bespeakMaxCacel)){
            bespeakMaxCacel = "2";
        }
        line.put("bespeakMaxCancel", Integer.parseInt(bespeakMaxCacel));

        String  bespeakTime = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BESPEAK_TIME.getValue(), cabinet.getAgentId());
        if(StringUtils.isEmpty(bespeakTime)){
            bespeakTime = "30";
        }
        line.put("bespeakTime", Integer.parseInt(bespeakTime));

        List<Map> boxList = new ArrayList<Map>();
        List<CabinetBox> cabinetBoxes = cabinetBoxService.findByCabinet(cabinet.getId());
        for (CabinetBox box : cabinetBoxes) {
            Map boxMap = new HashMap();
            boxMap.put("boxNum", box.getBoxNum());
            boxMap.put("chargeStatus", box.getChargeStatus());
            boxMap.put("batteryId", box.getBatteryId());
            boxMap.put("volume", box.getVolume() == null ? 0 : box.getVolume());

            if (box.getIsActive() == ConstEnum.Flag.TRUE.getValue() && box.getIsOnline()  == ConstEnum.Flag.TRUE.getValue()
                    && box.getBatteryId() != null && box.getBatteryStatus() == Battery.Status.IN_BOX.getValue()
                    && box.getBoxStatus() == CabinetBox.BoxStatus.FULL.getValue()
                    && box.getVolume() >= box.getBatteryFullVolume()) {
                boxMap.put("status", 1);
            }else{
                boxMap.put("status", 2);
            }

            if(box.getBatteryId() != null){
                boxMap.put("isFull", 1);
            }else{
                boxMap.put("isFull", 0);
            }
            boxList.add(boxMap);
        }
        line.put("boxList", boxList);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryOrderOpenEmptyBoxParam {
        @NotBlank
        public String cabinetId;
    }


    @ResponseBody
    @RequestMapping(value = "/battery_order_open_empty_box")
    public RestResult batteryOrderOpenEmptyBox(@Valid @RequestBody BatteryOrderOpenEmptyBoxParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        if (customerExchangeBatteryService.exists(customerId) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有已租电池");
        }

        Integer batteryType = null;
        CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
        if(customerExchangeInfo != null){
            batteryType = customerExchangeInfo.getBatteryType();
        }else{
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(cabinet.getAgentId(), customer.getId());
            if(exchangeWhiteList != null){
                batteryType = exchangeWhiteList.getBatteryType();
            }
        }

        if (batteryType == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有设置电池类型");
        }

        CabinetBox cabinetBox = null;
        String cacheKey = CacheKey.key(CacheKey.K_CUSTOMER_ID_CABINET_ID_V_BOX_NUM, customer.getId(), cabinet.getId());
        String emptyBoxNum = (String) memCachedClient.get(cacheKey);
        if (StringUtils.isNotEmpty(emptyBoxNum)) {
            CabinetBox box = cabinetBoxService.find(param.cabinetId, emptyBoxNum);
            if (box != null && (box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY.getValue() || box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()) && StringUtils.isEmpty(box.getBatteryId())) {
                cabinetBox = box;
            }
        }

        if (cabinetBox == null) {
            //查询合适的空箱号
            cabinetBox = cabinetBoxService.findOneEmptyBoxNum(param.cabinetId, batteryType);
            if (cabinetBox == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "没有可用空箱");
            }
        }

        int subType = cabinet.getSubtype();
        String cabinetId = cabinetBox.getCabinetId();
        String boxNum = cabinetBox.getBoxNum();

        if (cabinetBox.getBoxStatus() == CabinetBox.BoxStatus.EMPTY.getValue()) {
            int effect = cabinetBoxService.lockBox(cabinetId, boxNum, CabinetBox.BoxStatus.EMPTY.getValue(), CabinetBox.BoxStatus.EMPTY_LOCK.getValue());
            if (effect == 0) {
                log.error("lockBox fail, cabinetId:{}, boxNum:{}", cabinetId, boxNum);
                return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
            }
        }

        memCachedClient.set(cacheKey, boxNum, MemCachedConfig.CACHE_THREE_MINUTE);

        int openSuccess = 0;
        Map data = new HashMap();
        data.put("customerFullname", customer.getFullname());
        data.put("cabinetId", cabinetId);
        data.put("cabinetName", cabinet.getCabinetName());
        data.put("boxNum", boxNum);
        data.put("batteryId", "");

        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, cabinetId, boxNum, subType);
        if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
            if (log.isDebugEnabled()) {
                log.debug("open {}, {} empty box success", cabinetId, boxNum);
            }
            openSuccess = 1;

        } else {
            CabinetBox box = cabinetBoxService.find(cabinetId, boxNum);
            if (box.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                openSuccess = 1;
            } else {
                cabinetBoxService.unlockBox(cabinetId, boxNum, CabinetBox.BoxStatus.EMPTY_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
                openSuccess = 0;
            }
            log.error("open {}, {} empty box fail", cabinetId, boxNum);
        }

        if (openSuccess == 1) {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单打开空箱成功", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

            //保存开箱客户进缓存，如果放入非开箱客户电池，给出提示
            memCachedClient.set(CacheKey.key(CacheKey.K_CABINET_BOX_V_CUSTMOER_ID, cabinet.getId(), cabinetBox.getBoxNum()), customerId, MemCachedConfig.CACHE_THREE_MINUTE);

        } else {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单打开空箱失败", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

        }

        data.put("openSuccess", openSuccess);

        String cabinetIdBox = cabinet.getId() + ":" + cabinetBox.getBoxNum();
        memCachedClient.set(CacheKey.key(CacheKey.K_CUSTOMER_ID_V_SUBCABINET_ID_BOX, customerId), cabinetIdBox, MemCachedConfig.CACHE_FIVE_MINUTE);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryOrderReopenEmptyBoxParam {
        @NotBlank
        public String cabinetId;
        @NotBlank
        public String boxNum;
    }

    @ResponseBody
    @RequestMapping(value = "/battery_order_reopen_empty_box")
    public RestResult batteryOrderReopenEmptyBox(@Valid @RequestBody BatteryOrderReopenEmptyBoxParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        if (customerExchangeBatteryService.exists(customerId) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有已租电池");
        }

        //查询合适的空箱号
        CabinetBox cabinetBox = cabinetBoxService.find(param.cabinetId, param.boxNum);
        if (cabinetBox == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "没有可用空箱");
        }

        int subType = cabinet.getSubtype();
        String cabinetId = cabinetBox.getCabinetId();
        String boxNum = cabinetBox.getBoxNum();

        int openSuccess = 1;
        Map data = new HashMap();
        data.put("customerFullname", customer.getFullname());
        data.put("cabinetId", cabinetId);
        data.put("cabinetName", cabinet.getCabinetName());
        data.put("boxNum", boxNum);
        data.put("batteryId", "");

        if (cabinetBox.getBoxStatus() != CabinetBox.BoxStatus.EMPTY.getValue() && cabinetBox.getBoxStatus() != CabinetBox.BoxStatus.EMPTY_LOCK.getValue()) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "箱号不是空箱", data);
        }

        if (cabinetBox.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
        }

        if (cabinetBox.getBoxStatus() == CabinetBox.BoxStatus.EMPTY.getValue()) {
            int effect = cabinetBoxService.lockBox(cabinetId, boxNum, CabinetBox.BoxStatus.EMPTY.getValue(), CabinetBox.BoxStatus.EMPTY_LOCK.getValue());
            if (effect == 0) {
                log.error("lockBox fail, cabinetId:{}, boxNum:{}", cabinetId, boxNum);
                return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
            }
        }

        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, cabinetId, boxNum, subType);
        if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
            if (log.isDebugEnabled()) {
                log.debug("open {}, {} empty box success", cabinetId, boxNum);
            }
            openSuccess = 1;

        } else {
            CabinetBox box = cabinetBoxService.find(cabinetId, boxNum);
            if (box.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                openSuccess = 1;
            } else {
                cabinetBoxService.unlockBox(cabinetId, boxNum, CabinetBox.BoxStatus.EMPTY_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
                openSuccess = 0;
            }
            log.error("open {}, {} empty box fail", cabinetId, boxNum);
        }

        if (openSuccess == 1) {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单重新打空箱成功", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

        } else {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单重新打开空箱失败", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

        }

        data.put("openSuccess", openSuccess);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @ResponseBody
    @RequestMapping(value = "/battery_order_reopen_empty_box_other")
    public RestResult batteryOrderReopenEmptyBoxOther(@Valid @RequestBody BatteryOrderReopenEmptyBoxParam param) throws InterruptedException {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;

        String key = (String) config.memCachedClient.get(CacheKey.key(CacheKey.K_CUSTOMER_ID_V_SUBCABINET_ID_BOX, customerId));
        if (StringUtils.isEmpty(key)) {
            return RestResult.result(RespCode.CODE_2.getValue(), "开箱失败,不是同一个操作人");
        }

        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }

        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }

        if (customerExchangeBatteryService.exists(customerId) == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有已租电池");
        }

        //查询合适的箱号
        CabinetBox cabinetBox = cabinetBoxService.find(param.cabinetId, param.boxNum);

        int subType = cabinet.getSubtype();
        String cabinetId = cabinetBox.getCabinetId();
        String boxNum = cabinetBox.getBoxNum();

        int openSuccess = 1;
        Map data = new HashMap();
        data.put("customerFullname", customer.getFullname());
        data.put("cabinetId", cabinetId);
        data.put("cabinetName", cabinet.getCabinetName());
        data.put("boxNum", boxNum);


        if (cabinetBox.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
        }

        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, cabinetId, boxNum, subType);
        if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
            if (log.isDebugEnabled()) {
                log.debug("open {}, {} empty box success", cabinetId, boxNum);
            }
            openSuccess = 1;

        } else {
            CabinetBox box = cabinetBoxService.find(cabinetId, boxNum);
            if (box.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                openSuccess = 1;
            } else {
                cabinetBoxService.unlockBox(cabinetId, boxNum, CabinetBox.BoxStatus.EMPTY_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
                openSuccess = 0;
            }
            log.error("open {}, {} empty box fail", cabinetId, boxNum);
        }

        if (openSuccess == 1) {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单重新打空箱(放入非本人电池)成功", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

        } else {
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    cabinetBox.getBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单重新打开空箱(放入非本人电池)失败", customer.getFullname(), customer.getMobile()),
                    customer.getFullname());

        }

        data.put("openSuccess", openSuccess);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryOrderOldBatteryOpenBoxParam {
        public String orderId;
    }

    @ResponseBody
    @RequestMapping(value = "/battery_order_old_battery_open_box")
    public RestResult batteryOrderOldBatteryOpenBox(@Valid @RequestBody BatteryOrderOldBatteryOpenBoxParam param) throws InterruptedException {
        long customerId = getTokenData().customerId;

        Customer customer = customerService.find(customerId);

        BatteryOrder batteryOrder = batteryOrderService.find(param.orderId);
        if (batteryOrder == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
        }

        if (batteryOrder.getCustomerId() != customerId) {
            return RestResult.result(RespCode.CODE_2.getValue(), "当前用户无权操作");
        }

        if (batteryOrder.getOrderStatus() != BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "订单状态错误");
        }

        Cabinet cabinet = cabinetService.find(batteryOrder.getPutCabinetId());

        //发送开箱指令
        RestResult result = ClientBizUtils.openStandardBox(config, batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), cabinet.getSubtype());

        int openSuccess = 0;
        Map data = new HashMap();
        data.put("customerFullname", customer.getFullname());
        data.put("cabinetId", batteryOrder.getPutCabinetId());
        data.put("cabinetName", cabinet.getCabinetName());
        data.put("boxNum", batteryOrder.getPutBoxNum());
        data.put("batteryId", batteryOrder.getBatteryId());

        if (result.getCode() == RespCode.CODE_0.getValue()) { //开箱成功后锁定箱子
            if (log.isDebugEnabled()) {
                log.debug("open {}, {} box success", batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());
            }
            openSuccess = 1;
            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    batteryOrder.getPutBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单%s打开未付款箱门成功", customer.getFullname(), customer.getMobile(), param.orderId),
                    customer.getFullname());


        } else {
            openSuccess = 0;
            log.error("open {}, {} box fail", batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum());

            insertCabinetOperateLog(cabinet.getAgentId(),
                    cabinet.getId(),
                    cabinet.getCabinetName(),
                    batteryOrder.getPutBoxNum(),
                    CabinetOperateLog.OperateType.OPEN_DOOR,
                    CabinetOperateLog.OperatorType.CUSTOMER,
                    String.format("客户%s %s, 换电订单%s打开未付款箱门失败", customer.getFullname(), customer.getMobile(), param.orderId),
                    customer.getFullname());

        }


        data.put("openSuccess", openSuccess);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryOrderOpenFullBoxParam {
        public String orderId;
        public String cabinetId;
    }

    @ResponseBody
    @RequestMapping(value = "/battery_order_open_full_box")
    public RestResult batteryOrderOpenFullBox(@Valid @RequestBody BatteryOrderOpenFullBoxParam param) throws InterruptedException, ExecutionException {
        TokenCache.Data tokenData = getTokenData();
        String cacheKey = CacheKey.key(CacheKey.K_CABINET_ID_CUSTOMER_ID_V_ZERO, param.cabinetId, tokenData.customerId);
        CabinetMemcachedLog cabinetMemcachedLog = (CabinetMemcachedLog)config.memCachedClient.get(cacheKey);
        if(cabinetMemcachedLog != null){
            if (log.isDebugEnabled()) {
                cabinetMemcachedLog.setToService("app-server");
                cabinetMemcachedLog.setToCabinetId(param.cabinetId);
                cabinetMemcachedLog.setCustomerId(tokenData.customerId);
                log.debug(cabinetMemcachedLog.toString());
            }
            return RestResult.result(RespCode.CODE_2.getValue(), "操作过于频繁,请稍后重试");
        }else{
            cabinetMemcachedLog = new CabinetMemcachedLog();
            cabinetMemcachedLog.setFromService("app-server");
            cabinetMemcachedLog.setFromCabinetId(param.cabinetId);
            cabinetMemcachedLog.setCustomerId(tokenData.customerId);
            memCachedClient.set(cacheKey, cabinetMemcachedLog, MemCachedConfig.CACHE_TEN_SECOND);
        }


        if (StringUtils.isEmpty(param.orderId)) {
            Cabinet cabinet = cabinetService.find(param.cabinetId);
            if (cabinet == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
            }
            Customer customer = customerService.find(tokenData.customerId);
            if (customer == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
            }

            Integer batteryType = null;
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
            if(customerExchangeInfo != null){
                batteryType = customerExchangeInfo.getBatteryType();
            }else{
                ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(cabinet.getAgentId(), customer.getId());
                if(exchangeWhiteList != null){
                    batteryType = exchangeWhiteList.getBatteryType();
                }
            }
            if(batteryType == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户没有设置电池类型");
            }

            List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customer.getId());

            //判断用户是否存在电池，如果用户在柜子中存在电池并且是客户未取出状态，取出自己的电池
            for(CustomerExchangeBattery customerExchangeBattery : batteryList){
                Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
                //新电未取出的情况
                if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) {
                    BatteryOrder batteryOrder = batteryOrderService.find(battery.getOrderId());
                    if (batteryOrder == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "换电订单不存在");
                    }
                    CabinetBox cabinetBox = cabinetBoxService.find(batteryOrder.getTakeCabinetId(), batteryOrder.getTakeBoxNum());
                    if (cabinetBox == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "箱号不存在");
                    }
                    if (StringUtils.isEmpty(cabinetBox.getBatteryId())) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "箱中没有电池");
                    }
                    if (!cabinetBox.getBatteryId().equals(battery.getId())) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "箱中电池编号错误");
                    }
                    Map map = new HashMap();
                    map.put("customerFullname", customer.getFullname());
                    map.put("cabinetId", param.cabinetId);
                    map.put("cabinetName", cabinet.getCabinetName());
                    map.put("boxNum", cabinetBox.getBoxNum());
                    map.put("batteryTypeName", batteryOrder.getBatteryTypeName());
                    map.put("batteryId", battery.getId());
                    map.put("volume", battery.getVolume());
                    map.put("orderId", batteryOrder.getId());
                    int openSuccess = 0;

                    //发送开箱指令
                    ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
                    //如果协议发送成功
                    if (result.getCode() == RespCode.CODE_0.getValue()) {
                        openSuccess = 1;
                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                cabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.CUSTOMER,
                                String.format("客户%s %s, 换电订单%s打开满箱成功", customer.getFullname(), customer.getMobile(), param.orderId),
                                customer.getFullname());

                    } else {
                        openSuccess = 0;
                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                cabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.CUSTOMER,
                                String.format("客户%s %s, 换电订单%s打开满箱失败", customer.getFullname(), customer.getMobile(), param.orderId),
                                customer.getFullname());

                    }

                    map.put("openSuccess", openSuccess);
                    return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
                }
            }

            //取新电
            int maxCount = 1;//默认1块电池
            String maxCountStr = agentSystemConfigService.findConfigValue(ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue(), cabinet.getAgentId());
            if(!StringUtils.isEmpty(maxCountStr)){
                maxCount = Integer.parseInt(maxCountStr);
            }
            if(batteryList.size() >= maxCount) {
                return RestResult.result(RespCode.CODE_2.getValue(), "用户已拥有最大数目的电池");
            }

            //查询是否有预约订单
            String bespeakBoxNum = null;
            BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(customer.getId());
            if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(param.cabinetId)){
                bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
            }

            CabinetBox cabinetBox = cabinetBoxService.findOneFull(param.cabinetId, batteryType, bespeakBoxNum);
            if (cabinetBox == null) {
                int fullCount = cabinetBoxService.findFullCount(param.cabinetId);
                String errorMessage = "没有符合类型的已充满电池";
                if (fullCount != 0) {
                    errorMessage = "扫码者与当前柜子不匹配";
                }
                return RestResult.result(RespCode.CODE_2.getValue(), errorMessage);
            }

            Battery battery = batteryService.find(cabinetBox.getBatteryId());
            if (battery == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在:" + cabinetBox.getBatteryId());
            }

            if(bespeakBoxNum == null || !bespeakBoxNum.equals(cabinetBox.getBoxNum())){
                int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
                if (effect == 0) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
                }
            }

            Map map = new HashMap();
            map.put("customerFullname", customer.getFullname());
            map.put("cabinetId", param.cabinetId);
            map.put("cabinetName", cabinet.getCabinetName());
            map.put("cabinetId", cabinetBox.getCabinetId());
            map.put("cabinetName", cabinet.getCabinetName());
            map.put("boxNum", cabinetBox.getBoxNum());
            map.put("batteryTypeName", systemBatteryTypeService.find(battery.getType()).getTypeName());
            map.put("batteryId", battery.getId());
            map.put("volume", battery.getVolume());
            int openSuccess = 0;

            //发送开箱指令
            ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
            //如果协议发送成功
            if (result.getCode() == RespCode.CODE_0.getValue()) {
                openSuccess = 1;
                BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);
                map.put("orderId", order.getId());

            } else {
                openSuccess = 0;
                if (result.getSerial() == -1) {
                    cabinetBoxService.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
                } else {
                    CabinetBox box = cabinetBoxService.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                    if (box.getIsOpen() != ConstEnum.Flag.TRUE.getValue()) {
                        insertCabinetOperateLog(cabinet.getAgentId(),
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                cabinetBox.getBoxNum(),
                                CabinetOperateLog.OperateType.OPEN_DOOR,
                                CabinetOperateLog.OperatorType.CUSTOMER,
                                String.format("客户%s %s, 换电订单打开满箱失败", customer.getFullname(), customer.getMobile()),
                                customer.getFullname());
                    }else{
                        openSuccess = 1;
                    }
                    BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);
                    map.put("orderId", order.getId());
                }
            }
            map.put("openSuccess", openSuccess);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

        } else { //重新开箱
            BatteryOrder batteryOrder = batteryOrderService.find(param.orderId);
            if (batteryOrder == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "订单不存在");
            }
            if (batteryOrder.getOrderStatus() == BatteryOrder.OrderStatus.TAKE_OUT.getValue()) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池已取出");
            }
            Cabinet cabinet = cabinetService.find(param.cabinetId);
            if (cabinet == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
            }
            Customer customer = customerService.find(tokenData.customerId);
            if (customer == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
            }
            CabinetBox cabinetBox = cabinetBoxService.find(batteryOrder.getTakeCabinetId(), batteryOrder.getTakeBoxNum());
            if (cabinetBox == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "没有符合类型的已充满电池");
            }
            Battery battery = batteryService.find(cabinetBox.getBatteryId());
            if (battery == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在:" + cabinetBox.getBatteryId());
            }

            Map map = new HashMap();
            map.put("orderId", batteryOrder.getId());
            map.put("customerFullname", customer.getFullname());
            map.put("cabinetId", param.cabinetId);
            map.put("cabinetName", cabinet.getCabinetName());
            map.put("cabinetId", cabinetBox.getCabinetId());
            map.put("cabinetName", cabinet.getCabinetName());
            map.put("boxNum", cabinetBox.getBoxNum());
            map.put("batteryTypeName", systemBatteryTypeService.find(battery.getType()).getTypeName());
            map.put("batteryId", battery.getId());
            map.put("volume", battery.getVolume());
            int openSuccess = 1;
            if (cabinetBox.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                // 箱门已打开
            } else {
                if (StringUtils.isEmpty(cabinetBox.getBatteryId())) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "电池已取出");
                }
                //发送开箱指令
                ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
                //如果协议发送成功
                if (result.getCode() == RespCode.CODE_0.getValue()) {
                    openSuccess = 1;
                    insertCabinetOperateLog(cabinet.getAgentId(),
                            cabinet.getId(),
                            cabinet.getCabinetName(),
                            cabinetBox.getBoxNum(),
                            CabinetOperateLog.OperateType.OPEN_DOOR,
                            CabinetOperateLog.OperatorType.CUSTOMER,
                            String.format("客户%s %s, 换电订单%s重新打开满箱成功", customer.getFullname(), customer.getMobile(), param.orderId),
                            customer.getFullname());
                } else {
                    openSuccess = 0;

                    insertCabinetOperateLog(cabinet.getAgentId(),
                            cabinet.getId(),
                            cabinet.getCabinetName(),
                            cabinetBox.getBoxNum(),
                            CabinetOperateLog.OperateType.OPEN_DOOR,
                            CabinetOperateLog.OperatorType.CUSTOMER,
                            String.format("客户%s %s, 换电订单%s重新打开满箱失败", customer.getFullname(), customer.getMobile(), param.orderId),
                            customer.getFullname());

                }
            }

            map.put("openSuccess", openSuccess);
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchCabinetParam {
        public int areaId;
        public double lng;
        public double lat;

    }

    @ResponseBody
    @RequestMapping(value = "back_cabinet_list")
    public RestResult getBackCabinetList(@RequestBody SearchCabinetParam param) {
        TokenCache.Data tokenData = getTokenData();
        Area area = areaCache.get(param.areaId);
        if(area == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        Area province = null, city = null;
        if(area.getAreaLevel() == 3) {
            city = areaCache.get(area.getParentId());
        } else if(area.getAreaLevel() == 2) {
            city = area;
        }

        province = areaCache.get(city.getParentId());
        if(province == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        Customer customer = customerService.find(tokenData.customerId);
        if(customer == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }

        List<Cabinet> list = null;
        List<Integer> agentId = null;
        String geoHash = null;
        Customer entiey = customerService.find(getTokenData().customerId);
        //TODO 这里暂时注释起来
//        if(entiey.getAppId() != null && entiey.getAppId() != Constant.SYSTEM_PARTNER_ID) {
//            agentId = new ArrayList();
//            agentId.add(entiey.getAppId());
//            agentService.findChildAgent(entiey.getAppId() , agentId);
//        }
        if (entiey.getIsWhiteList() != ConstEnum.Flag.TRUE.getValue()) {
            geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);
        }
        list = cabinetService.findNearest(agentId, geoHash, param.lng, param.lat, null,   province.getId(),
                city.getId(), null, null, 0, 1000);

        List<Map> result = new ArrayList<Map>();
        for (Cabinet cabinet : list) {

            if (cabinet.getEmptyCount() > 1) {
                Map line = new HashMap();
                line.put("id", cabinet.getId());
                line.put("cabinetName", cabinet.getCabinetName());
                line.put("address", cabinet.getAddress());
                line.put("emptyCount", cabinet.getEmptyCount());
                line.put("lng", cabinet.getLng());
                line.put("lat", cabinet.getLat());
                line.put("distance", cabinet.getDistance());
                result.add(line);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", result);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoxStatusParam {
        public String cabinetId;
        public String boxNum;
    }

    @ResponseBody
    @RequestMapping(value = "/box_status.htm")
    public RestResult getBoxStatus(@RequestBody BoxStatusParam param) {

        Map map = new HashMap();
        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜编号不存在");
        }
        CabinetBox box = cabinetBoxService.find(param.cabinetId, param.boxNum);
        if (box != null) {
            map.put("isOpen", box.getIsOpen());
            map.put("batteryId", box.getBatteryId());
        } else {
            return RestResult.result(RespCode.CODE_2.getValue(), "箱号不存在");
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", map);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LoopExchangeBatteryParam {
        public String cabinetId;
        public String oldBoxNum;
        public String newBoxNum;
        public int type;
    }

    @ResponseBody
    @RequestMapping(value = "/loop_exchange_battery.htm")
    public RestResult loopExchangeBattery(@RequestBody LoopExchangeBatteryParam param) throws InterruptedException {
        long customerId = getTokenData().customerId;
        RestResult result = cabinetService.loopExchangeBattery(customerId, param.cabinetId, param.oldBoxNum, param.newBoxNum, param.type, true);
        if (result.getCode() == 0) {
            Map root = (Map) result.getData();
            Map data = (Map) root.get("data");

            if ("opening_new_battery".equals(root.get("step"))) {
                if ("yes".equals(data.get("need_open_battery"))) {
                    data.remove("need_open_battery");

                    //查询分柜子情况
                    Cabinet cabinet = cabinetService.find(param.cabinetId);
                    if (cabinet == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
                    }

                    Customer customer = customerService.find(customerId);

                    Integer batteryType = null;
                    CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customerId);
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

                    //查询是否有预约订单
                    String bespeakBoxNum = null;
                    BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(customerId);
                    if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(param.cabinetId)){
                        bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
                    }

                    CabinetBox cabinetBox = cabinetBoxService.findOneFull(param.cabinetId, batteryType, bespeakBoxNum);
                    if (cabinetBox == null) {
                        int fullCount = cabinetBoxService.findFullCount(param.cabinetId);
                        String errorMessage = "没有符合类型的已充满电池";
                        if (fullCount != 0) {
                            errorMessage = "扫码者与当前柜子不匹配";
                        }
                        return RestResult.result(RespCode.CODE_2.getValue(), errorMessage);
                    }

                    Battery battery = batteryService.find(cabinetBox.getBatteryId());
                    if (battery == null) {
                        return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在:" + cabinetBox.getBatteryId());
                    }


                    if(bespeakBoxNum == null || !bespeakBoxNum.equals(cabinetBox.getBoxNum())){
                        int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
                        if (effect == 0) {
                            return RestResult.result(RespCode.CODE_2.getValue(), "锁定箱号失败");
                        }
                    }

                    //发送开箱指令
                    ClientBizUtils.SerialResult serialResult = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
                    //如果协议发送成功
                    if (serialResult.getCode() == RespCode.CODE_0.getValue()) {
                        batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

                        NewBoxNum newBoxNum = new NewBoxNum("app-server", data.get("cabinetId").toString(), null, data.get("boxNum").toString(), cabinetBox.getBoxNum(), new Date());
                        String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, data.get("cabinetId").toString(), data.get("boxNum").toString());
                        memCachedClient.set(cachekey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);
//                        String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, data.get("cabinetId").toString(), data.get("boxNum").toString());
//                        memCachedClient.set(cachekey, cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);

                        if (log.isDebugEnabled()) {
                            log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", data.get("cabinetId").toString(), data.get("boxNum").toString(), cabinetBox.getBoxNum());
                        }

                    } else {
                        if (serialResult.getSerial() == -1) {
                            cabinetBoxService.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
                        } else {
                            CabinetBox box = cabinetBoxService.find(cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                            if (box.getIsOpen() != ConstEnum.Flag.TRUE.getValue()) {
                                insertCabinetOperateLog(cabinet.getAgentId(),
                                        cabinet.getId(),
                                        cabinet.getCabinetName(),
                                        cabinetBox.getBoxNum(),
                                        CabinetOperateLog.OperateType.OPEN_DOOR,
                                        CabinetOperateLog.OperatorType.CUSTOMER,
                                        String.format("客户%s %s, 换电订单打开满箱失败", customer.getFullname(), customer.getMobile()),
                                        customer.getFullname());
                            }
                            BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

                            NewBoxNum newBoxNum = new NewBoxNum("app-server", data.get("cabinetId").toString(), null, data.get("boxNum").toString(), cabinetBox.getBoxNum(), new Date());
                            String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, data.get("cabinetId").toString(), data.get("boxNum").toString());
                            memCachedClient.set(cachekey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);

//                          String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, data.get("cabinetId").toString(), data.get("boxNum").toString());
//                          memCachedClient.set(cachekey, cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);
                            if (log.isDebugEnabled()) {
                                log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", data.get("cabinetId").toString(), data.get("boxNum").toString(), cabinetBox.getBoxNum());
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    //取出旧电
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryOrderTakeOldBatteryParam {
        public String cabinetId;
    }

    @ResponseBody
    @RequestMapping(value = "/battery_order_take_old_battery.htm")
    public RestResult batteryOrderTakeOldBattery(@RequestBody BatteryOrderTakeOldBatteryParam param) throws InterruptedException {
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户不存在");
        }
        if(cabinetService.find(param.cabinetId) == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
        }
        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryService.findListByCustomer(customer.getId());
        if (batteryList.size() == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "客户没有电池");
        }

        for(CustomerExchangeBattery customerExchangeBattery : batteryList){
            Battery battery = batteryService.find(customerExchangeBattery.getBatteryId());
            if (battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {
                Cabinet cabinet = cabinetService.find(battery.getCabinetId());
                if (cabinet == null) {
                    return RestResult.result(RespCode.CODE_2.getValue(), "换电柜[" + battery.getCabinetId() + "]不存在");
                }

                int openSuccess = 0;
                //发送开箱指令
                ClientBizUtils.SerialResult serialResult = ClientBizUtils.openStandardBox(config, battery.getCabinetId(), battery.getBoxNum(), cabinet.getSubtype());
                //如果协议发送成功
                if (serialResult.getCode() == RespCode.CODE_0.getValue()) {
                    openSuccess = 1;
                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());

                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(battery.getBoxNum());
                    operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.CUSTOMER.getValue());
                    operateLog.setContent(String.format("换电订单%s, 客户取出旧电开箱成功", battery.getOrderId()));
                    operateLog.setOperator(customer.getFullname());
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogService.insert(operateLog);
                } else {
                    openSuccess = 0;
                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());

                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(battery.getBoxNum());
                    operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.CUSTOMER.getValue());
                    operateLog.setContent(String.format("换电订单%s, 客户取出旧电开箱失败", battery.getOrderId()));
                    operateLog.setOperator(customer.getFullname());
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogService.insert(operateLog);
                }

                Map data = new HashMap();
                data.put("customerFullname", customer.getFullname());
                data.put("boxNum", battery.getBoxNum());
                data.put("batteryId", "");
                data.put("openSuccess", openSuccess);

                return RestResult.dataResult(RespCode.CODE_0.getValue(), "", data);
            }
        }

        return RestResult.result(RespCode.CODE_2.getValue(), "用户不存在待付款电池");
    }

    @ResponseBody
    @RequestMapping(value = "/province_city_list.htm")
    public RestResult provinceCityList() {
        return cabinetService.findAddressList();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InputDynamicCodeParam {
        public String cabinetId;
        public String dynamicCode;
    }

    @ResponseBody
    @RequestMapping(value = "/input_dynamic_code.htm")
    public RestResult inputDynamicCode(@RequestBody InputDynamicCodeParam param) {
        TokenCache.Data tokenData = getTokenData();
        long customerId = tokenData.customerId;
        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if(cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
        }

        if (StringUtils.isNotEmpty(cabinet.getDynamicCode())) {
            if (!cabinet.getDynamicCode().equals(param.dynamicCode)) {
                return RestResult.result(RespCode.CODE_2.getValue(), "动态码错误");

            } else {
                CabinetDynamicCodeCustomer record = cabinetDynamicCodeCustomerService.find(param.cabinetId, customerId);
                if (record == null) {
                    record = new CabinetDynamicCodeCustomer();
                    record.setCabinetId(param.cabinetId);
                    record.setCustomerId(customerId);
                    cabinetDynamicCodeCustomerService.insert(record);
                }
            }

        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), "", null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NearestInfoParam {
        public Integer areaId;
        public double lng;
        public double lat;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping("/nearest_info.htm")
    public RestResult nearestInfo(@RequestBody NearestInfoParam param) {
        TokenCache.Data tokenData = getTokenData();
        Area area = areaCache.get(param.areaId);
        if(area == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        Area province = null, city = null;
        if(area.getAreaLevel() == 3) {
            city = areaCache.get(area.getParentId());
        } else if(area.getAreaLevel() == 2) {
            city = area;
        }

        province = areaCache.get(city.getParentId());
        if(province == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        List<Integer> agentId = null;
        String geoHash = null;
        if (getTokenData() != null) {
            Customer customer = customerService.find(getTokenData().customerId);
            if (customer.getIsWhiteList() != ConstEnum.Flag.TRUE.getValue()) {
                geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);
            }

            //TODO 这里暂时注释起来
//            if (customer.getAppId() != null && customer.getAppId() != Constant.SYSTEM_PARTNER_ID) {
//                agentId = new ArrayList();
//                agentId.add(customer.getAppId());
//                agentService.findChildAgent(customer.getAppId(), agentId);
//            }
        }else {
            geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat).substring(0, 3);
        }
        //根据城市查找最近的地址，如果没有，查询城市外最近的地址
        List<Cabinet> list = cabinetService.findNearest(
                agentId,
                geoHash,
                param.lng,
                param.lat,
                null,
                province.getId(),
                city.getId(),
                null,
                null,
                0,
                1);

        if(list.size() == 0){
            list = cabinetService.findNearest(
                    agentId,
                    geoHash,
                    param.lng,
                    param.lat,
                    null,
                    null,
                    null,
                    null,
                    null,
                    0,
                    1);
        }

        Map line = new HashMap();

        if(list.size() > 0){
            Cabinet cabinet = list.get(0);
            line.put("agentId", cabinet.getAgentId());
            line.put("id", cabinet.getId());
            line.put("cabinetName", cabinet.getCabinetName());
            line.put("address", cabinet.getAddress());
            line.put("lng", cabinet.getLng());
            line.put("lat", cabinet.getLat());
            line.put("tel", cabinet.getTel());
            line.put("isOnline", cabinet.getIsOnline() != null && cabinet.getIsOnline() > 0 ? 1 : 0);
            line.put("distance", cabinet.getDistance());
            line.put("type",cabinet.getType());
            line.put("fullCount", cabinet.getFullCount());
            line.put("chargeCount", cabinet.getChargeCount());
            line.put("lockCount", cabinet.getLockCount());
            line.put("emptyCount", cabinet.getEmptyCount());
            line.put("signal", cabinet.getCurrentSignal());
            line.put("networkType", cabinet.getNetworkType());

            List batteryTypeList = new ArrayList();
            line.put("batteryTypeList", batteryTypeList);
            List<ExchangeBatteryForegift> exchangeBatteryForegiftList = exchangeBatteryForegiftService.findByCabinet(cabinet.getId());
            for (ExchangeBatteryForegift exchangeBatteryForegift : exchangeBatteryForegiftList) {
                Map batteryTypeMap = new HashMap();
                batteryTypeMap.put("batteryType",exchangeBatteryForegift.getBatteryType());
                batteryTypeMap.put("batteryTypeName", exchangeBatteryForegift.getTypeName());
                batteryTypeMap.put("money",exchangeBatteryForegift.getMoney());

                List priceList = new ArrayList();
                List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceService.findList(cabinet.getAgentId(), exchangeBatteryForegift.getBatteryType(), exchangeBatteryForegift.getId());
                for (PacketPeriodPrice packetPeriodPrice : packetPeriodPriceList) {
                    Map map = new HashMap();
                    map.put("dayCount",  packetPeriodPrice.getDayCount());
                    map.put("price", packetPeriodPrice.getPrice());
                    priceList.add(map);
                }
                batteryTypeMap.put("packetPeriodList",priceList);
                batteryTypeList.add(batteryTypeMap);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CabinetInfoParam {
        public String cabinetId;
        public String batteryId;
    }

    /*
    * 82-支付详情
    * */

    @ResponseBody
    @RequestMapping(value = "/cabinet_info")
    public RestResult nearest(@Valid @RequestBody CabinetInfoParam param) {
        Customer customer = customerService.find(getTokenData().customerId);
        if (customer == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }
        Map line = new HashMap();
        if (StringUtils.isNotEmpty(param.cabinetId)) {
            Cabinet cabinet = cabinetService.find(param.cabinetId);
            if (cabinet == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "柜子不存在", null);
            }
            int canChange = cabinetService.findFullCount(param.cabinetId);

            //查询是否有预约订单
            String bespeakBoxNum = null;
            BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(customer.getId());
            if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(cabinet.getId())){
                bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
            }
            //查询用户白名单
            ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(customer.getAgentId(), customer.getId());
            //查询用户押金
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
            if(exchangeWhiteList != null && customerExchangeInfo != null){
                return RestResult.result(RespCode.CODE_2.getValue(), "您为白名单用户，请先退押金后再进行换电操作");
            }
            Integer batteryType = null;
            if(customerExchangeInfo != null){
                batteryType = customerExchangeInfo.getBatteryType();
            }else if(exchangeWhiteList != null){
                batteryType = exchangeWhiteList.getBatteryType();
            }
            if (batteryType != null) {
                CabinetBox cabinetBox = cabinetBoxService.findOneFull(cabinet.getId(), batteryType, bespeakBoxNum);
                if (cabinetBox != null) {
                    line.put("predictBoxNum", cabinetBox.getBoxNum());
                    line.put("volume", cabinetBox.getVolume());
                } else {
                    line.put("predictBoxNum", null);
                    line.put("volume", 0);
                }
            }else {
                CabinetBox cabinetBox = cabinetBoxService.findOneFullByCabinet(cabinet.getId());
                if (cabinetBox != null) {
                    line.put("predictBoxNum", cabinetBox.getBoxNum());
                    line.put("volume", cabinetBox.getVolume());
                } else {
                    line.put("predictBoxNum", null);
                    line.put("volume", 0);
                }
            }

            line.put("imagePath", staticImagePath(cabinet.getImagePath1()));
            line.put("canChange", canChange);
            line.put("id", cabinet.getId());
            line.put("cabinetName", cabinet.getCabinetName());
            line.put("address", cabinet.getAddress());
            line.put("minPrice", cabinet.getMinPrice() == null ? 0 : cabinet.getMinPrice());
            line.put("maxPrice", cabinet.getMaxPrice() == null ? 0 : cabinet.getMaxPrice());
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }
}
