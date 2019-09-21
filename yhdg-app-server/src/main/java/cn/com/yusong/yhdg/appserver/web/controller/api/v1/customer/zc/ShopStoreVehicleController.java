package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;


import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.persistence.hdg.ShopMapper;
import cn.com.yusong.yhdg.appserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.appserver.service.zc.ShopPriceSettingService;
import cn.com.yusong.yhdg.appserver.service.zc.ShopStoreVehicleService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.ShopPriceSetting;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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

@Controller("api_v1_customer_zc_shop_store_vehicle")
@RequestMapping(value = "/api/v1/customer/zc/shop_store_vehicle")
public class ShopStoreVehicleController extends ApiController {

    static final Logger log = LogManager.getLogger(ShopStoreVehicleController.class);

    @Autowired
    ShopPriceSettingService shopPriceSettingService;
    @Autowired
    ShopService shopService;
    @Autowired
    ShopStoreVehicleService shopStoreVehicleService;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VehicleSettingIdParam {

        public Long settingId;
        public Integer areaId;
        public Double lng;
        public Double lat;
    }
    /*
    *
    * 03.根据套餐查询门店
    * */
    @ResponseBody
    @RequestMapping("list.htm")
    public RestResult list(@Valid @RequestBody VehicleSettingIdParam param){

        String id = null;
        String shopName = null;
        String address = null;
        Double lng = null;
        Double lat = null;
        String tel = null;
        Double distance = null;
        Integer vehicleCount = null;
        String imagePath1 = null;
        String imagePath2 = null;
        List list = new ArrayList();
        if (param == null || param.lng == 0 || param.lat == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "经纬度错误");
        }
        String geoHash = GeoHashUtils.getGeoHashString(param.lng, param.lat);
        Area province = null, city = null;
        if (param.areaId != 0) {
            Area area = areaCache.get(param.areaId);
            if (area == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
            if (area.getAreaLevel() == 3) {
                city = areaCache.get(area.getParentId());
            } else if (area.getAreaLevel() == 2) {
                city = area;
            } else if (area.getAreaLevel() == 1) {
                province = area;
            }
        }
        if (city != null) {
            province = areaCache.get(city.getParentId());
            if (province == null) {
                return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
            }
        }
        if (province == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        if(param.settingId == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "套餐ID不能为空", null);
        }
        List<ShopPriceSetting> byPriceSettingIdAll = shopPriceSettingService.findByPriceSettingIdAll(param.settingId);
        if(byPriceSettingIdAll == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "未找到套餐所属门店");
        }
        for (ShopPriceSetting byPriceSettingId: byPriceSettingIdAll) {
            Map map = new HashMap();
            if(byPriceSettingId.getShopId()!=null){
                Shop shop = shopService.findShopIdDistance(byPriceSettingId.getShopId(), geoHash, param.lng, param.lat);
                if(shop!=null){
                    id = shop.getId();
                    shopName = shop.getShopName();
                    address = shop.getAddress();
                    lng = shop.getLng();
                    lat = shop.getLat();
                    tel = shop.getTel();
                    distance =shop.getDistance();
                    imagePath1 =staticImagePath(shop.getImagePath1());
                    imagePath2 = staticImagePath(shop.getImagePath2());
                    vehicleCount = shopStoreVehicleService.findByVehicleCount(shop.getId(), byPriceSettingId.getPriceSettingId());
                }
            }
            map.put("id",id);
            map.put("shopName",shopName);
            map.put("address",address);
            map.put("lng",lng);
            map.put("lat",lat);
            map.put("tel",tel);
            map.put("distance",distance);
            map.put("vehicleCount",vehicleCount);
            map.put("imagePath1",imagePath1);
            map.put("imagePath2",imagePath2);
            list.add(map);
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);
    }

}
