package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.zc;


import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.zc.ShopAddressCorrectionService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller("api_v1_customer_zc_shop_address_correction")
@RequestMapping(value = "/api/v1/customer/zc/shop_address_correction")
public class ShopAddressCorrectionController extends ApiController {

    @Autowired
    ShopAddressCorrectionService shopAddressCorrectionService;
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ShopAddressCorrectionParam {
        public String shopId;
        public String shopName;
        public Integer areaId;
        public String street;
        public Double lng;
        public Double lat;
        public String memo;
    }

    /*
    *  05.门店位置纠错
    * */
    @ResponseBody
    @RequestMapping("/create.htm")
    public RestResult create(@Valid @RequestBody ShopAddressCorrectionController.ShopAddressCorrectionParam param) {

        return  shopAddressCorrectionService.changePositionCustomer(
                param.shopId,
                param.shopName,
                param.areaId,
                param.street,
                param.lng,
                param.lat,
                param.memo,
                getTokenData().customerId
        );
    }
}
