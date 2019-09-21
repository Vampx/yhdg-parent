package cn.com.yusong.yhdg.appserver.web.controller.api.v1.customer.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.CustomerService;
import cn.com.yusong.yhdg.appserver.service.hdg.ReliefStationService;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.domain.basic.Area;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.hdg.ReliefStation;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
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

/**
 * Created by ruanjian5 on 2017/11/7.
 */
@Controller("api_v1_customer_hdg_relief_station")
@RequestMapping(value = "/api/v1/customer/hdg/relief_station")
public class ReliefStationController extends ApiController {
    static final Logger log = LogManager.getLogger(ReliefStationController.class);

    @Autowired
    ReliefStationService reliefStationService;
    @Autowired
    CustomerService customerService;


    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ListParam {
        public Integer areaId;
        public double lng;
        public double lat;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "list.htm")
    public RestResult list(@Valid @RequestBody ListParam param) {
        Customer customer = customerService.find(getTokenData().customerId);
        if (customer == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "客户不存在", null);
        }

        Area area = areaCache.get(param.areaId);
        if (area == null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(), "无效的区域Id", null);
        }

        Area city = null;
        if (area.getAreaLevel() == 3) {
            city = areaCache.get(area.getParentId());
        } else if (area.getAreaLevel() == 2) {
            city = area;
        }

        List<Map> list = new ArrayList<Map>();
        List<ReliefStation> listRelifStation = reliefStationService.findList(customer.getPartnerId(), city.getId(), param.lng, param.lat, param.offset, param.limit);
        if (listRelifStation != null) {
            for (ReliefStation reliefStation : listRelifStation) {
                Map line = new HashMap();
                line.put("id", reliefStation.getId());
                line.put("stationName", StringUtils.trimToEmpty(reliefStation.getStationName()));
                line.put("tel", reliefStation.getTel());
                line.put("street", reliefStation.getStreet());
                line.put("imagePath", staticImagePath(reliefStation.getImagePath()));
                line.put("introduce", reliefStation.getIntroduce());
                line.put("lng", reliefStation.getLng());
                line.put("lat", reliefStation.getLat());
                line.put("star", reliefStation.getStar());
                line.put("minPrice", reliefStation.getMinPrice());
                line.put("maxPrice", reliefStation.getMaxPrice());
                double distance = GeoHashUtils.getDistanceInMeters(param.lng, param.lat, reliefStation.getLng(), reliefStation.getLat());

                line.put("distance", String.format("%.2f", distance));

                list.add(line);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, list);

    }


}
