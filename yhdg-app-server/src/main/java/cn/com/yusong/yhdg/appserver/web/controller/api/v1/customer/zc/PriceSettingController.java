package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;


import cn.com.yusong.yhdg.appserver.config.TokenCache;
import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.*;
import cn.com.yusong.yhdg.appserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.appserver.service.zc.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.Phoneapp;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_customer_zc_price_setting")
@RequestMapping(value = "/api/v1/customer/zc/price_setting")
public class PriceSettingController extends ApiController {

    @Autowired
    PriceSettingService priceSettingService;
    @Autowired
    WeixinmpService weixinmpService;
    @Autowired
    AlipayfwService alipayfwService;
    @Autowired
    PhoneappService phoneappService;
    @Autowired
    AgentService agentService;
    @Autowired
    RentPriceService rentPriceService;
    @Autowired
    VehicleVipPriceService vehicleVipPriceService;
    @Autowired
    ShopPriceSettingService shopPriceSettingService;
    @Autowired
    ShopStoreVehicleService shopStoreVehicleService;
    @Autowired
    VehicleVipPriceCustomerService vehicleVipPriceCustomerService;
    @Autowired
    VehicleVipPriceShopService vehicleVipPriceShopService;
    @Autowired
    CustomerService customerService;
    @Autowired
    ShopService shopService;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String shopId;
    }
    /**
     * 02-首页查询租车套餐
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        TokenCache.Data tokenData = getTokenData();
        List list = new ArrayList();
        Integer id;
        String settingName;
        Integer modeId;
        String vehicleName;
        Integer minPrice;
        Integer maxPrice;
        String modelImagePath;
        Integer vehicleCount;

        if(StringUtils.isNotBlank(param.shopId)){
            List<PriceSetting> priceSettings = priceSettingService.findShopIdAll(param.shopId);
            if(priceSettings == null || priceSettings.size() == 0){
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "商户无租车配置", null);
            }
            for (PriceSetting priceSetting:  priceSettings) {
                Map map = new HashMap();
                id = priceSetting.getId().intValue();
                settingName = priceSetting.getSettingName();
                modeId = priceSetting.getModelId();
                vehicleName = priceSetting.getVehicleName();
                minPrice = priceSetting.getMinPrice();
                maxPrice = priceSetting.getMaxPrice();
                modelImagePath = priceSetting.getModelImagePath();
                vehicleCount = shopStoreVehicleService.findByVehicleCount(param.shopId, priceSetting.getId().intValue());
                map.put("id",id);
                map.put("settingName", settingName);
                map.put("modeId", modeId);
                map.put("vehicleName", vehicleName);
                map.put("minPrice", minPrice);
                map.put("maxPrice", maxPrice);
                map.put("modelImagePath", staticImagePath(modelImagePath));
                map.put("vehicleCount", vehicleCount);
                list.add(map);

            }
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
        } else {
            List<Integer> agentIdList = null;
            if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_MP) {
                Weixinmp weixinmp = weixinmpService.find(tokenData.appId);
                if (weixinmp == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "公众号配置不存在", null);
                }
                agentIdList = agentService.findByWeixinmp(weixinmp.getId());

            } else if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_FW) {
                Alipayfw alipayfw = alipayfwService.find(tokenData.appId);
                if (alipayfw == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "生活号配置不存在", null);
                }
                agentIdList = agentService.findByAlipayfw(alipayfw.getId());
            } else if (tokenData.clientType == TokenCache.Data.CLIENT_TYPE_APP) {
                Phoneapp phoneapp = phoneappService.find(tokenData.appId);
                if (phoneapp == null) {
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "手机端配置不存在", null);
                }
                agentIdList = agentService.findByPhoneapp(phoneapp.getId());
            }

            if(agentIdList == null){
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "运营商不存在", null);
            } else {
                List<PriceSetting> agentIDAll = priceSettingService.findAgentIdAll(agentIdList);
                if(agentIDAll == null || agentIDAll.size() == 0){
                    return RestResult.dataResult(RespCode.CODE_2.getValue(), "运营商无租车配置", null);
                }
                for (PriceSetting priceSetting: agentIDAll) {
                    List<ShopPriceSetting> byPriceSettingIdAll = shopPriceSettingService.findByPriceSettingIdAll(priceSetting.getId());
                    if(byPriceSettingIdAll.size()>0){
                        Map map = new HashMap();
                        id = priceSetting.getId().intValue();
                        settingName = priceSetting.getSettingName();
                        modeId = priceSetting.getModelId();
                        vehicleName = priceSetting.getVehicleName();
                        minPrice = priceSetting.getMinPrice();
                        maxPrice = priceSetting.getMaxPrice();
                        modelImagePath = staticImagePath(priceSetting.getModelImagePath());
                        vehicleCount = priceSetting.getVehicleCount();
                        map.put("id", id);
                        map.put("settingName", settingName);
                        map.put("modeId", modeId);
                        map.put("vehicleName", vehicleName);
                        map.put("minPrice", minPrice);
                        map.put("maxPrice", maxPrice);
                        map.put("modelImagePath", modelImagePath);
                        map.put("vehicleCount", vehicleCount);
                        list.add(map);
                    }
                }

                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailParam  {
        public Long settingId;
        public String shopId;
    }


    @ResponseBody
    @RequestMapping("/detail.htm")
    public RestResult detail(@Valid @RequestBody DetailParam param) {

        List<Map> list = new ArrayList<Map>();
        Integer agentId;
        Long priceId;
        String priceName;
        Integer foregiftPrice;
        Integer rentPrice;
        Integer dayCount;
        String memo;

        if (param == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
        }

        Shop shop = shopService.find(param.shopId);
        if (shop == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "门店不存在", null);
        }

        PriceSetting priceSetting = priceSettingService.find(param.settingId);
        if(priceSetting == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "未找到该套餐设置", null);
        }

        List<RentPrice> rentPriceList = rentPriceService.findListBySettingId(priceSetting.getId());
        if (rentPriceList.size() == 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "没有此类套餐", null);
        }

        int isVipCustomer = 0;
        long customerId = getTokenData().customerId;
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }
        //vip套餐客户
        VehicleVipPriceCustomer vehicleVipPriceCustomer = null;
        if (StringUtils.isNotEmpty(customer.getMobile())) {
            vehicleVipPriceCustomer = vehicleVipPriceCustomerService.findByMobile(customer.getMobile());
            if (vehicleVipPriceCustomer != null) {
                VehicleVipPrice vehicleVipPrice = vehicleVipPriceService.find(vehicleVipPriceCustomer.getPriceId());
                if (vehicleVipPrice != null && vehicleVipPrice.getPriceSettingId() == param.settingId.longValue()) {
                    isVipCustomer = 1;
                }
            }
        }

        for (RentPrice rentPriceBy : rentPriceList) {
            Map map = new HashMap();
            agentId = rentPriceBy.getAgentId();
            priceId = rentPriceBy.getId() ;
            priceName = rentPriceBy.getPriceName();
            foregiftPrice = rentPriceBy.getForegiftPrice();
            rentPrice = rentPriceBy.getRentPrice();
            dayCount = rentPriceBy.getDayCount();
            memo = rentPriceBy.getMemo();

            if (isVipCustomer == 1) {
                //vip套餐用户查询的数据
                VehicleVipPrice vehicleVipPrice = vehicleVipPriceService.findByRentPriceId(priceId);
                //必须是同一个大套餐（车型）才可以使用vip
                if (vehicleVipPrice != null) {
                    if (vehicleVipPrice.getPriceSettingId() == param.settingId.longValue()) {
                        map.put("vipPriceId", vehicleVipPrice.getId());
                        map.put("priceName", vehicleVipPrice.getPriceName());
                        map.put("foregiftPrice", vehicleVipPrice.getForegiftPrice());
                        map.put("rentPrice", vehicleVipPrice.getRentPrice());
                        map.put("dayCount", vehicleVipPrice.getDayCount());
                        map.put("memo", vehicleVipPrice.getMemo());
                        list.add(map);
                        break;
                    }
                }
            } else {
                //如果门店加入到了vip套餐，使用该vip套餐
                VehicleVipPriceShop vehicleVipPriceShop = vehicleVipPriceShopService.findByShopId(param.shopId);
                if (vehicleVipPriceShop != null) {
                    VehicleVipPrice vehicleVipPrice = vehicleVipPriceService.find(vehicleVipPriceShop.getPriceId());
                    //必须是同一个大套餐（车型）才可以使用vip
                    if (vehicleVipPrice != null) {
                        if (vehicleVipPrice.getPriceSettingId() == param.settingId.longValue()) {
                            map.put("vipPriceId", vehicleVipPrice.getId());
                            map.put("priceName", vehicleVipPrice.getPriceName());
                            map.put("foregiftPrice", vehicleVipPrice.getForegiftPrice());
                            map.put("rentPrice", vehicleVipPrice.getRentPrice());
                            map.put("dayCount", vehicleVipPrice.getDayCount());
                            map.put("memo", vehicleVipPrice.getMemo());
                            list.add(map);
                            break;
                        }
                    }
                }
            }

            map.put("agentId", agentId);
            map.put("priceId", priceId);

            map.put("vipPriceId", 0);
            map.put("priceName", priceName);
            map.put("foregiftPrice", foregiftPrice);
            map.put("rentPrice", rentPrice);
            map.put("dayCount", dayCount);
            map.put("memo", memo);

            list.add(map);
        }

        List<Map> vipList = new ArrayList<Map>();
        for (Map map : list) {
            if ((Integer) map.get("vipPriceId") != null) {
                continue;
            } else {
                vipList.add(map);
            }
        }
        if (vipList.size() >= 1) {
            //只取其中一个
            List<Map> vipResultList = new ArrayList<Map>();
            vipResultList.add(vipList.get(0));
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, vipResultList);
        } else {
            return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContinueParam  {
        public Integer vipPriceId;
        public Integer rentPriceId;
    }

    @ResponseBody
    @RequestMapping("/continue.htm")
    public RestResult payContinue(@Valid @RequestBody ContinueParam param) {
        Integer rentPriceId = param.rentPriceId;
        if (rentPriceId == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "参数错误");
        }
        NotNullMap data = new NotNullMap();

        if (param.vipPriceId != null && param.vipPriceId != 0) {
            VehicleVipPrice vehicleVipPrice = vehicleVipPriceService.find(param.vipPriceId);
            if (vehicleVipPrice != null) {
                data.put("agentId", vehicleVipPrice.getAgentId());
                data.put("priceId", vehicleVipPrice.getId());
                data.put("vipPriceId", vehicleVipPrice.getId());
                data.put("priceName", vehicleVipPrice.getPriceName());
                data.put("foregiftPrice", vehicleVipPrice.getForegiftPrice());
                data.put("rentPrice", vehicleVipPrice.getRentPrice());
                data.put("dayCount", vehicleVipPrice.getDayCount());
                data.put("memo", vehicleVipPrice.getMemo());
                return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
            }
        }

        RentPrice rentPrice = rentPriceService.find(rentPriceId);
        if (rentPrice == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "套餐不存在");
        }

        data.put("agentId", rentPrice.getAgentId());
        data.put("priceId", rentPrice.getId());
        data.put("vipPriceId", "");
        data.put("priceName", rentPrice.getPriceName());
        data.put("foregiftPrice", rentPrice.getForegiftPrice());
        data.put("rentPrice", rentPrice.getRentPrice());
        data.put("dayCount", rentPrice.getDayCount());
        data.put("memo", rentPrice.getMemo());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

}
