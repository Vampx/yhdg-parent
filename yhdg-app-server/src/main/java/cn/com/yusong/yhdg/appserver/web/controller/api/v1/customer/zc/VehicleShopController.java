package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.hdg.ShopService;
import cn.com.yusong.yhdg.appserver.service.zc.ShopPriceSettingService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg.ShopController;
import cn.com.yusong.yhdg.common.domain.hdg.Shop;
import cn.com.yusong.yhdg.common.domain.zc.ShopPriceSetting;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("api_v1_customer_zc_shop")
@RequestMapping(value = "/api/v1/customer/zc/shop")
public class VehicleShopController extends ApiController {

    static final Logger log = LogManager.getLogger(VehicleShopController.class);

    @Autowired
    ShopService shopService;
    @Autowired
    ShopPriceSettingService shopPriceSettingServiceg;


    @ResponseBody
    @RequestMapping(value = "/province_city_list.htm")
    public RestResult provinceCityList() {
        return shopService.findAddVehicleShopList();
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public String id;
        public String lng;
        public String lat;
    }

    /*
    * 查询门店详情
    * */
    @ResponseBody
    @RequestMapping("/detail.htm")
    public RestResult detail(@Valid @RequestBody ListParam param) {

        String id = null;
        String shopName = null;
        String address = null;
        Integer vehicleCount = 0;
        Double lng = null;
        Double lat = null;
        String tel = null;
        String workTime = null;
        Integer isExchange = null;
        Integer isRent = null;
        Integer isVehicle = null;
        String linkname = null;
        String imagePath1 = null;
        String imagePath2 = null;

        Map map = new HashMap();
        Shop shop = shopService.find(param.id);
        if(shop==null){
            return RestResult.result(RespCode.CODE_2.getValue(), "无此门店");
        }
        List<ShopPriceSetting> byPriceListShop = shopPriceSettingServiceg.findByPriceListShop(shop.getId(),null);
        for (ShopPriceSetting byPriceListShops: byPriceListShop) {
            vehicleCount+= byPriceListShops.getVehicleCount() == null ? 0 : byPriceListShops.getVehicleCount();
        }
        id = shop.getId();
        shopName = shop.getShopName();
        address = shop.getAddress();
        lng = shop.getLng();
        lat = shop.getLat();
        tel = shop.getTel();
        workTime = shop.getWorkTime();
        isExchange = shop.getIsExchange();
        isRent = shop.getIsRent();
        isVehicle = shop.getIsVehicle();
        linkname = shop.getLinkname();
        imagePath1 = shop.getImagePath1();
        imagePath2 = shop.getImagePath2();

        map.put("id",id);
        map.put("shopName",shopName);
        map.put("address",address);
        map.put("lng",lng);
        map.put("lat",lat);
        map.put("tel",tel);
        map.put("workTime",workTime);
        map.put("isExchange",isExchange);
        map.put("isRent",isRent);
        map.put("isVehicle",isVehicle);
        map.put("linkname",linkname);
        map.put("imagePath1",staticImagePath(imagePath1));
        map.put("imagePath2",staticImagePath(imagePath2));
        map.put("vehicleCount",vehicleCount);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, map);

    }

}
