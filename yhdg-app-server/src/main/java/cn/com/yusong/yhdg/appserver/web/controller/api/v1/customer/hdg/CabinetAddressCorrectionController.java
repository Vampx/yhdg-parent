package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetAddressCorrectionService;
import cn.com.yusong.yhdg.appserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Created by ruanjian5 on 2017/12/15.
 */
@Controller("api_v1_customer_hdg_cabinet_address_correction")
@RequestMapping(value = "/api/v1/customer/hdg/cabinet_address_correction")
public class CabinetAddressCorrectionController extends ApiController {
    final static Logger log = LogManager.getLogger(CabinetAddressCorrectionController.class);

    @Autowired
    CabinetAddressCorrectionService cabinetAddressCorrectionService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CreateParam {
        public String cabinetId;
        public String cabinetName;
        public Integer areaId;
        public String street;
        public Double lng;
        public Double lat;
        public String memo;

    }
    @ResponseBody
    @RequestMapping(value = "/create.htm")
    public RestResult create(@RequestBody CreateParam param) {

        return  cabinetAddressCorrectionService.changePositionCustomer(
                                                        param.cabinetId,
                                                        param.cabinetName,
                                                        param.areaId,
                                                        param.street,
                                                        param.lng,
                                                        param.lat,
                                                        param.memo,
                                                        getTokenData().customerId);
    }
}
