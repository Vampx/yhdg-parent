package cn.com.yusong.yhdg.appserver.web.controller.api.v1.independent_customer.hdg;

import cn.com.yusong.yhdg.appserver.constant.RespCode;
import cn.com.yusong.yhdg.appserver.entity.result.RestResult;
import cn.com.yusong.yhdg.appserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.appserver.service.hdg.*;
import cn.com.yusong.yhdg.appserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.annotation.NotLogin;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.utils.GeoHashUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.*;

@Controller("api_v1_independent_customer_hdg_cabinet")
@RequestMapping(value = "/api/v1/independent_customer/hdg/cabinet")
public class CabinetController extends ApiController {

    static final Logger log = LogManager.getLogger(CabinetController.class);

    @Autowired
    CabinetService cabinetService;
    @Autowired
    CabinetBoxService CabinetBoxService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    SystemConfigService systemConfigService;
    @Autowired
    CabinetInstallRecordService cabinetInstallRecordService;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NearestParam {
        public double lng;
        public double lat;
        public int agentId;
        public int offset;
        public int limit;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping("nearest.htm")
    public RestResult searchByKeyword(@RequestBody NearestParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        if (param == null || param.lng == 0 || param.lat == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "经纬度错误");
        }
        List<Integer> agentIdList = new ArrayList<Integer>();
        agentIdList.add(param.agentId);
        List<Cabinet> list = cabinetService.findNearest(agentIdList,
                null,
                param.lng,
                param.lat,
                null,
                null,
                null,
                null,
                null,
                param.offset,
                param.limit
        );
        List<Map> data = new ArrayList<Map>();

        for(Cabinet e : list) {
            Map line = new HashMap();

            line.put("id", e.getId());
            line.put("cabinetName", e.getCabinetName());
            line.put("address", e.getAddress());
            line.put("emptyCount", e.getEmptyCount());
            line.put("fullCount", e.getFullCount());
            line.put("lng", e.getLng());
            line.put("lat", e.getLat());
            line.put("tel", e.getTel() != null ? e.getTel() : "");
            line.put("distance", e.getDistance());
            line.put("isOnline", e.getIsOnline() != null && e.getIsOnline() > 0 ? 1 : 0);
            data.add(line);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BoxInfoParam {
        public String id;
        public double lng;
        public double lat;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/subcabinet_box_info")
    public RestResult subcabinetBoxInfo(@Valid @RequestBody BoxInfoParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }
        if (param == null || param.lng == 0 || param.lat == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "经纬度错误");
        }

        Cabinet cabinet = cabinetService.find(param.id);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
        }

        if (cabinet.getUpLineStatus() == Cabinet.UpLineStatus.NOT_ONLINE.getValue()) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜未上线");
        }

        double distance = GeoHashUtils.getDistanceInMeters(param.lng, param.lat, cabinet.getLng(), cabinet.getLat());

        Map line = new HashMap();

        line.put("id", cabinet.getId());
        line.put("cabinetName", cabinet.getCabinetName());
        line.put("address", cabinet.getAddress());
        line.put("emptyCount", CabinetBoxService.findEmptyCount(param.id));
        line.put("fullCount", CabinetBoxService.findFullCount(param.id));
        line.put("lng", cabinet.getLng());
        line.put("lat", cabinet.getLat());
        line.put("tel", cabinet.getTel() != null ? cabinet.getTel() : "");
        line.put("distance", distance);
        line.put("openTime", cabinet.getWorkTime() != null ? cabinet.getWorkTime() : "");
        line.put("addressDetails", cabinet.getAddressName());
        line.put("batteryCount", CabinetBoxService.findBatteryCount(param.id));

        String[] imageList = new String[]{staticImagePath(cabinet.getImagePath1()) != null ? staticImagePath(cabinet.getImagePath1()) : ""
                , staticImagePath(cabinet.getImagePath2()) != null ? staticImagePath(cabinet.getImagePath2()) : ""};
        line.put("imageList", imageList);

        List<Map> subcabinetList = new ArrayList<Map>();

        line.put("subcabinetList", subcabinetList);

        List<String> subcabinetIdList = new ArrayList<String>();
        subcabinetIdList.add( cabinet.getId());
        for(String subcabinetId : subcabinetIdList){
            Map map = new HashMap();
            map.put("subcabinetId", subcabinetId);

            List<Map> boxList = new ArrayList<Map>();

            map.put("boxList", boxList);

            List<CabinetBox> subcabinetBoxList = CabinetBoxService.findBySubcabinetId(subcabinetId);
            // 满箱电池状态(0 无电池 1 正常 2 用户电池)
            final int NOT_BATTERY = 0, FULL_BOX = 1, CUSTOMER_BATTERY = 2;
            // 是否可换出(1 不可换出 2 可换出)
            final int  NOT_CHANGE = 1, CHANGE = 2;

            for (CabinetBox box : subcabinetBoxList) {
                Map boxMap = new HashMap();
                boxMap.put("subcabinetId", box.getCabinetId());
                boxMap.put("boxNum", box.getBoxNum());
                boxMap.put("volume", box.getVolume() == null ? 0 : box.getVolume());

                Battery battery = batteryService.find(box.getBatteryId());
                if (battery != null) {
                    boxMap.put("batteryId", battery.getCode());
                    String batteryTypeName = cabinetService.getBatteryTypeName(battery.getType());
                    boxMap.put("type", batteryTypeName);
                    boxMap.put("shellCode", battery.getShellCode());
                } else {
                    boxMap.put("batteryId","");
                    boxMap.put("type", "");
                    boxMap.put("shellCode", "");
                }
                Integer designMileage = Integer.parseInt(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.BATTERY_DESIGN_MILEAGE.getValue()));
                if (battery != null && battery.getVolume() != null) {
                    boxMap.put("expectDistance", designMileage * battery.getVolume() / 100);
                } else {
                    boxMap.put("expectDistance", "");
                }
                if (box.getChargeStatus() != null)  {
                    if (box.getChargeStatus() == Battery.ChargeStatus.NOT_CHARGE.getValue()) {
                        boxMap.put("chargeStatus",  1);
                    } else if (box.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                        boxMap.put("chargeStatus",  2);
                    } else if (box.getChargeStatus() == Battery.ChargeStatus.CHARGE_FULL.getValue()){
                        boxMap.put("chargeStatus",  3);
                    }
                } else {
                    boxMap.put("chargeStatus",  0);
                }
                int status = 0;
                int isFull = 0;
                int expectFullTime = 0;
                if (box.getBatteryId() != null) {
                    if (box.getBoxStatus() == CabinetBox.BoxStatus.FULL.getValue()) {
                        status = FULL_BOX; // 1 正常
                    } else if (box.getBoxStatus() == CabinetBox.BoxStatus.CUSTOMER_USE.getValue()) {
                        status = CUSTOMER_BATTERY; // 2 用户电池
                    }
                    if (box.getVolume() != null && box.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                        expectFullTime = (int) Math.ceil(((100 - box.getVolume()) * 0.7));
                    }
                } else if(box.getBoxStatus() == CabinetBox.BoxStatus.BESPEAK.getValue()
                        || box.getBoxStatus() == CabinetBox.BoxStatus.EMPTY_LOCK.getValue()
                        || box.getBoxStatus() == CabinetBox.BoxStatus.FULL_LOCK.getValue()
                        || box.getBoxStatus() == CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
                    status = NOT_BATTERY; // 0 无电池
                }

                if (box.getBatteryId() != null) {
                    if (box.getBoxStatus() == CabinetBox.BoxStatus.FULL.getValue()
                            && box.getVolume() < box.getBatteryFullVolume()) {
                        isFull = NOT_CHANGE; // 1 未充满(不可换)
                    } else if (box.getBoxStatus() == CabinetBox.BoxStatus.FULL.getValue()
                            && box.getVolume() >= box.getBatteryFullVolume()) {
                        isFull = CHANGE; //2 满电(可换)
                    }
                }

                boxMap.put("status", status);
                boxMap.put("isFull", isFull);
                boxMap.put("expectFullTime", expectFullTime);
                boxList.add(boxMap);
            }

            subcabinetList.add(map);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, line);
    }

    /**
     * 上线
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpLinetParam {
        public String cabinetId;
        public String cabinetName;
        public String customerMobile;
        public String address;
        public Integer areaId;
        public String street;
        public Double lng;
        public Double lat;
        public Integer price;
        public Integer permitExchangeVolume;
        public Integer chargeFullVolume;
        public String linkname;
        public String tel;
        public Long time;
        public String sign;
    }

    @NotLogin
    @ResponseBody
    @RequestMapping(value = "/up_line.htm")
    public RestResult upLine(@RequestBody UpLinetParam param) {
        if(!checkSign(param.time, param.sign)){
            return RestResult.result(RespCode.CODE_2.getValue(), "签名错误");
        }

        Cabinet cabinet = cabinetService.find(param.cabinetId);
        if (cabinet == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜不存在");
        }

        if(cabinet.getUpLineStatus() == Cabinet.UpLineStatus.ONLINE.getValue()){
            return RestResult.result(RespCode.CODE_2.getValue(), "换电柜已上线");

        }

        // 生产
        // cabinet.setAgentId(23);
        cabinet.setAgentId(23);

        if(param.price != null){
            cabinet.setPrice(param.price * 1d / 100);
        }

        cabinet.setCabinetName(param.cabinetName);
        cabinet.setPermitExchangeVolume(param.permitExchangeVolume);
        cabinet.setChargeFullVolume(param.chargeFullVolume);
        cabinet.setLinkname(param.linkname);
        cabinet.setTel(param.tel);

        return cabinetInstallRecordService.zhizuUPLine(cabinet, param.customerMobile, param.address, param.areaId, param.street, param.lng, param.lat);
    }



}
