package cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg;

import cn.com.yusong.yhdg.agentappserver.config.TokenCache;
import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.service.basic.UserService;
import cn.com.yusong.yhdg.agentappserver.service.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.zd.RentOrderService;
import cn.com.yusong.yhdg.agentappserver.utils.InstallUtils;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.ApiController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.User;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.domain.zd.RentOrder;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;

@Controller("agent_api_v1_agent_hdg_battery")
@RequestMapping(value = "/agent_api/v1/agent/hdg/battery")
public class BatteryController extends ApiController {

    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryParameterService batteryParameterService;
    @Autowired
    FaultLogService faultLogService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    ShopService shopService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    RentOrderService rentOrderService;
    @Autowired
    UserService userService;
    @Autowired
    CabinetBoxService cabinetBoxService;

    /*
     * 33-查询电池列表
     * */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryListParam {
        public String batteryId;
        public int category;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/battery_list.htm")
    public RestResult batteryList(@Valid @RequestBody BatteryListParam param ) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<Battery> list = batteryService.findAgentBatteryList(agentId, param.category, param.batteryId, null,null, null, null,null,null,null,param.offset,param.limit);

        List<Map> result = new ArrayList<Map>();
        if (list != null){
            for (Battery battery : list){
                Map line = new HashMap();
                line.put("id", battery.getId());
                String batteryTypeName = batteryService.getBatteryTypeName(battery.getType());
                line.put("typeName", batteryTypeName);
                line.put("code", battery.getCode());
                line.put("status", battery.getStatus());
                line.put("volume", battery.getVolume());
                line.put("customerFullname", battery.getCustomerFullname());
                line.put("customerMobile", battery.getCustomerMobile());
                line.put("isOnline", battery.getIsOnline());
                line.put("isNormal", battery.getIsNormal());
                line.put("chargeStatus", battery.getChargeStatus());
                line.put("upLineStatus", battery.getUpLineStatus());
                line.put("shellCode", battery.getShellCode() == null ? null : battery.getShellCode());
                result.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    /*
    * 71 电池控制
    * */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpdateLockSwitchParam {
        public String batteryId;
        public int lockSwitch;
    }

    @ResponseBody
    @RequestMapping(value = "/update_lock_switch.htm")
    public RestResult updateLockSwitch(@Valid @RequestBody UpdateLockSwitchParam param) {
        batteryService.updateLockSwitch(param.batteryId, param.lockSwitch);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryFaultLogListParam {
        public String batteryId;
        public int offset;
        public int limit;
    }

    /*
     * 66-查询电池告警信息
     **/
    @ResponseBody
    @RequestMapping(value = "/fault_log_list.htm")
    public RestResult batteryFaultLogList(@Valid @RequestBody BatteryFaultLogListParam param ) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<FaultLog> list = faultLogService.findByBatteryId(agentId, param.batteryId, param.offset, param.limit);
        List<Map> result = new ArrayList<Map>();
        if (list != null) {
            for (FaultLog faultLog : list){
                Map line = new HashMap();
                line.put("faultType", faultLog.getFaultTypeName());
                line.put("boxNum", faultLog.getBoxNum());
                line.put("faultContent", faultLog.getFaultContent());
                line.put("createTime", faultLog.getCreateTime());
                result.add(line);
            }
        }
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }


    /*
    * 49-查询电池详情
    * */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DetailParam {
        public String batteryId;
    }

    @ResponseBody
    @RequestMapping(value = "/detail.htm")
    public RestResult detail(@Valid @RequestBody DetailParam param ) {
        Battery battery = batteryService.find(param.batteryId);
        if (battery == null){
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"电池不存在",null);
        }
        BatteryParameter batteryParameter = batteryParameterService.find(param.batteryId);
        Integer circle = null;
        Integer currentSignal = null;
        Integer totalCapacity = null;
        Integer currentCapacity = null;
        Integer cellCount = null;
        if (battery != null) {
            circle = battery.getCircle() == null ? 0 : battery.getCircle();
            totalCapacity = battery.getTotalCapacity() == null ? 0 : battery.getTotalCapacity();
            currentCapacity = battery.getCurrentCapacity() == null ? 0 : battery.getCurrentCapacity();
        }
        if (batteryParameter != null) {
            if (circle == 0) {
                circle = batteryParameter.getCircle() == null ? null : batteryParameter.getCircle();
            }
            if (totalCapacity == 0) {
                totalCapacity = batteryParameter.getNominalCapacity() == null ? null : batteryParameter.getNominalCapacity()*10;
            }
            if (currentCapacity == 0) {
                currentCapacity = batteryParameter.getCurrentCapacity() == null ? null : batteryParameter.getCurrentCapacity()*10;
            }
            currentSignal = batteryParameter.getCurrentSignal();
            cellCount = batteryParameter.getSerials();
        }

        Map result = new HashMap();
        result.put("id",battery.getId());
        result.put("volume",battery.getVolume() == null ? 0 : battery.getVolume());
        result.put("chargeStatus",battery.getChargeStatus());
        int expectFullTime = 0;
        if (battery .getVolume() != null && battery.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
            expectFullTime = (int) Math.ceil(((100 - battery .getVolume()) * 0.7));
        }
        result.put("expectFullTime",expectFullTime);
        result.put("voltage", battery.getVoltage() == null ? null : battery.getVoltage()/100);//电压
        result.put("electricity", battery.getElectricity() == null ? null : battery.getElectricity());//电流
        result.put("fet", battery.getFet() == null ? null : battery.getFet());/*mos指示状态 0表示mos关闭，1表示打开*/
        result.put("circle", circle);//循环次数
        result.put("signalType", battery.getSignalType());//信号类型
        result.put("lockSwitch", battery.getLockSwitch());
        result.put("lng", battery.getLng());/*经度*/
        result.put("lat", battery.getLat());/*纬度*/
        result.put("currentSignal", currentSignal);//信号
        result.put("totalCapacity", totalCapacity);//额定容量
        result.put("currentCapacity", currentCapacity);//剩余容量
        result.put("cellCount", cellCount);//电池串数
        String[] temps = null;
        if (batteryParameter != null && batteryParameter.getTemp() != null) {
            temps = battery.getTemp().split(",");
        }
        result.put("temps", temps);//温度数组
        result.put("soc", battery.getVolume());
        List<Map> mapList = new ArrayList<Map>();
        if (batteryParameter != null) {
            if (StringUtils.isNotEmpty(batteryParameter.getSingleVoltage())) {
                String[] singleVoltages = batteryParameter.getSingleVoltage().split(",");
                List<Integer> voltageList = new ArrayList<Integer>();
                for (int i = 0; i < singleVoltages.length; i++) {
                    voltageList.add(Integer.parseInt(singleVoltages[i]));
                }
                int minVoltage = Collections.min(voltageList);
                int maxVoltage = Collections.max(voltageList);
                int averageVoltage = 0;
                int sum = 0;
                for (Integer voltage : voltageList) {
                    sum += voltage;
                }
                averageVoltage = sum / voltageList.size();
                int voltageRange = maxVoltage - minVoltage;
                result.put("minimumVoltage", minVoltage);//最低电压
                result.put("maximumVoltage", maxVoltage);//最高电压
                result.put("averageVoltage", averageVoltage);//平均电压
                result.put("voltageRange", voltageRange);//压差
                int i = 1;
                for (Integer voltage : voltageList) {
                    HashMap<String, Integer> map = new HashMap<String, Integer>();
                    map.put("no", i);//序号
                    map.put("tension", voltage);//电压
                    map.put("maxVoltageRange", maxVoltage - voltage);//最高压差
                    map.put("minVoltageRange", voltage - minVoltage);//最低压差
                    mapList.add(map);
                    i++;
                }
            }
        }else {
            result.put("minimumVoltage", null);//最低电压
            result.put("maximumVoltage", null);//最高电压
            result.put("averageVoltage", null);//平均电压
            result.put("voltageRange", null);//压差
        }
        result.put("mapList", mapList);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

    /*
    * 72-查询电池轨迹
    * */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TrackListParam {
        public String orderId;
        public String trackTime;
        public int offset;
        public int limit;
    }

    @ResponseBody
    @RequestMapping(value = "/track_list.htm")
    public RestResult trackList(@RequestBody TrackListParam param) {
        return batteryOrderService.findBatteryReportLogByOrderId(param.orderId, param.trackTime, param.offset, param.limit);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BatteryRentRecordParam {
        public String shopId;
        public String batteryId;
        public int offset;
        public int limit;
    }

    /**
     * 135-查询门店电池租用记录信息
     */
    @ResponseBody
    @RequestMapping(value = "/battery_rent_record.htm")
    public RestResult batteryRentRecord(@Valid @RequestBody BatteryRentRecordParam param) throws ParseException {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }
        if (param.shopId != null) {
            String shopId = param.shopId;
            Shop shop = shopService.find(shopId);
            if (shop == null) {
                return RestResult.result(RespCode.CODE_2.getValue(), "门店不存在");
            }
        }
        Battery battery = batteryService.find(param.batteryId);
        if(battery == null){
            return RestResult.result(RespCode.CODE_2.getValue(), "电池不存在");
        }

        List<Map> result = new ArrayList<Map>();
        //根据电池类型显示 换租电记录
        if (battery.getCategory() == Battery.Category.EXCHANGE.getValue()) {
            List<BatteryOrder> list1 = batteryOrderService.findByBatteryList(agentId, param.batteryId, null, param.offset, param.limit);
            for (BatteryOrder batteryOrder : list1) {
                NotNullMap notNullMap = new NotNullMap();

                notNullMap.putString("id", batteryOrder.getId());
                notNullMap.putString("customerFullname", batteryOrder.getCustomerFullname());
                notNullMap.putMobileMask("customerMobile", batteryOrder.getCustomerMobile());
                notNullMap.putString("statusName", batteryOrder.getOrderStatusName());
                notNullMap.putString("takeCabinetId", batteryOrder.getTakeCabinetId());
                notNullMap.putString("takeCabinetName", batteryOrder.getTakeCabinetName());
                notNullMap.putString("takeBoxNum", batteryOrder.getTakeBoxNum());
                notNullMap.putDateTime("takeTime", batteryOrder.getTakeTime());
                notNullMap.putString("putCabinetId", batteryOrder.getPutCabinetId());
                notNullMap.putString("putCabinetName", batteryOrder.getPutCabinetName());
                notNullMap.putString("putBoxNum", batteryOrder.getPutBoxNum());
                notNullMap.putDateTime("putTime", batteryOrder.getPutTime());
                notNullMap.putInteger("beginVolume", batteryOrder.getInitVolume());
                notNullMap.putInteger("endVolume", batteryOrder.getCurrentVolume());

                result.add(notNullMap);
            }
        } else {
            List<RentOrder> list;
            if (param.shopId != null) {
                list = rentOrderService.findListByBatteryId(param.shopId, param.batteryId, param.offset, param.limit);
            }else {
                list = rentOrderService.findListByAgentBatteryId(agentId, param.batteryId, param.offset, param.limit);
            }
            for (RentOrder rentOrder : list) {
                NotNullMap line = new NotNullMap();

                line.putString("id", rentOrder.getId());
                line.putString("customerFullname", rentOrder.getCustomerFullname());
                line.putMobileMask("customerMobile", rentOrder.getCustomerMobile());
                line.putInteger("volume", rentOrder.getCurrentVolume());
                line.putString("shopId", rentOrder.getShopId());
                line.putString("shopName", rentOrder.getShopName());
                line.putDateTime("backTime", rentOrder.getBackTime());
                line.putDateTime("createTime", rentOrder.getCreateTime());

                result.add(line);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);
    }

  /*
    * 149 正常电池标识为异常
    * */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChangeToAbnormalParam {
        public String batteryId;
        public String abnormalCause;
    }

    @ResponseBody
    @RequestMapping(value = "/change_to_abnormal.htm")
    public RestResult changeToAbnormal(@Valid @RequestBody ChangeToAbnormalParam param) {
        TokenCache.Data tokenData = getTokenData();
        User user = userService.find(tokenData.userId);
        String operator = user.getFullname();
        batteryService.changeToAbnormal(param.batteryId, param.abnormalCause, operator, new Date());
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

      /*
    * 156 异常电池解除异常
    * */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ChangeToNormalParam {
        public String batteryId;
    }

    @ResponseBody
    @RequestMapping(value = "/change_to_normal.htm")
    public RestResult changeIsNormal(@Valid @RequestBody ChangeToNormalParam param) {
        batteryService.changeToNormal(param.batteryId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

     /*
    * 157 查看异常标识原因
    * */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ViewAbnormalCauseParam {
        public String batteryId;
    }

    @ResponseBody
    @RequestMapping(value = "/view_abnormal_cause.htm")
    public RestResult viewAbnormalCause(@Valid @RequestBody ViewAbnormalCauseParam param) {
        NotNullMap notNullMap = new NotNullMap();
        notNullMap.put("abnormalCause", batteryService.viewAbnormalCause(param.batteryId));
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, notNullMap);
    }


    /**
     * 175-查询运营商电池列表
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgentBatteryListParam {
        public String keyword;
        public Integer outType;
        public Integer inType;
        public Integer lowCircle;
        public Integer highCircle;
        public Integer lowDay;
        public Integer highDay;
        public Integer category;
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "/agent_battery_list.htm")
    public RestResult batteryList(@Valid @RequestBody AgentBatteryListParam param) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        List<Battery> list = new ArrayList<Battery>();
        //查询运营商电池
        List<Integer> notNormal = Arrays.asList(ConstEnum.Flag.FALSE.getValue());
        List<Integer> normal = Arrays.asList(ConstEnum.Flag.TRUE.getValue());
        List<Integer> allNormal = Arrays.asList(ConstEnum.Flag.TRUE.getValue(), ConstEnum.Flag.FALSE.getValue());

        List<Integer> onlineStatus = Arrays.asList(Battery.UpLineStatus.ONLINE.getValue());
        List<Integer> allLineStatus = Arrays.asList(Battery.UpLineStatus.ONLINE.getValue(),Battery.UpLineStatus.NOT_ONLINE.getValue());

        List<Integer> customerUserStatus = Arrays.asList(Battery.Status.CUSTOMER_OUT.getValue());

        List<Integer> inCabinetStatus = Arrays.asList(
                Battery.Status.IN_BOX_NOT_PAY.getValue(),
                Battery.Status.IN_BOX.getValue(),
                Battery.Status.IN_BOX_CUSTOMER_USE.getValue());

        List<Integer> allStatus = Arrays.asList(
                Battery.Status.NOT_USE.getValue(),
                Battery.Status.IN_BOX_NOT_PAY.getValue(),
                Battery.Status.IN_BOX.getValue(),
                Battery.Status.IN_BOX_CUSTOMER_USE.getValue(),
                Battery.Status.CUSTOMER_OUT.getValue());

        if (param.outType == 2 && param.inType == 1) {
            //客户使用中
            list = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, normal, customerUserStatus, onlineStatus,null,null,null,null, param.offset, param.limit);
        } else if (param.outType == 2 && param.inType == 2) {
            //柜子中
            list = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, normal, inCabinetStatus, onlineStatus,null,null,null,null,param.offset, param.limit);
        } else if (param.outType == 2 && param.inType == 3) {
            //门店中
            list = batteryService.agentShopList(agentId, param.category, param.keyword, param.offset, param.limit);
        } else if (param.outType == 2 && param.inType == 4) {
            //其他 不在库存中 不是客户使用柜子中
            list = batteryService.agentRestsList(agentId, param.category, param.keyword, param.offset, param.limit);
        } else if (param.outType == 3 && param.inType == 1) {
            //异常
            list = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, notNormal, allStatus, allLineStatus, null, null, null, null,param.offset,param.limit);
        } else if (param.outType == 1 && param.inType == 1) {
            //全部
            list = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, allNormal, allStatus,allLineStatus, param.lowDay, param.highDay, param.lowCircle, param.highCircle, param.offset,param.limit);
        }
        //客户使用中
        int agentCustomerUseCount = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, normal, customerUserStatus, onlineStatus,null,null,null,null, null, null).size();
        //柜子中
        int agentCabinetCount = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, normal, inCabinetStatus, onlineStatus,null,null,null,null,null, null).size();
        //门店中
        int agentShopCount = batteryService.agentShopList(agentId, param.category, param.keyword, null, null).size();
        //其他
        int otherCount = batteryService.agentRestsList(agentId, param.category, param.keyword, null, null).size();
        //异常
        int notNormalCount = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, notNormal, allStatus, allLineStatus, null, null, null, null,null,null).size();
        //全部
        int allNormalCount = batteryService.findAgentBatteryList(agentId, param.category, param.keyword, allNormal, allStatus,allLineStatus, param.lowDay, param.highDay, param.lowCircle, param.highCircle, null,null).size();

        Map data = new HashMap();
        List<Map> result = new ArrayList<Map>();
        for (Battery battery : list) {
            Map line = new HashMap();
            line.put("id", battery.getId());
            String batteryTypeName = batteryService.getBatteryTypeName(battery.getType());
            line.put("typeName", batteryTypeName);
            line.put("code", battery.getCode());
            line.put("status", battery.getStatus());
            line.put("volume", battery.getVolume());
            line.put("customerFullname", battery.getCustomerFullname());
            line.put("customerMobile", battery.getCustomerMobile() == null ? null : battery.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            line.put("isOnline", battery.getIsOnline());
            line.put("isNormal", battery.getIsNormal());
            line.put("chargeStatus", battery.getChargeStatus());
            line.put("upLineStatus", battery.getUpLineStatus());
            line.put("shellCode", battery.getShellCode());
            line.put("signalType", battery.getSignalType());
            line.put("currentSignal", battery.getCurrentSignal());
            line.put("category", battery.getCategory());
            line.put("cabinetId", battery.getCabinetId());
            if (battery.getStatus() == 1){
                line.put("explain", "");
            }else if (battery.getStatus() == 3) {
                CabinetBox cabinetBox = cabinetBoxService.findByBatteryId(battery.getId());
                if (cabinetBox != null){
                    Cabinet cabinet = cabinetService.find(cabinetBox.getCabinetId());
                    line.put("explain", cabinet.getCabinetName()+cabinetBox.getBoxNum()+"格口");
                }
            }else if (battery.getStatus() == 6) {
                line.put("explain", battery.getCustomerFullname()+battery.getCustomerMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }else if (battery.getStatus() == 7) {
                Shop shop = shopService.find(battery.getShopId());
                line.put("explain", shop.getShopName());
            }
            if (battery.getCabinetId() != null) {
                line.put("cabinetName", cabinetService.find(battery.getCabinetId()).getCabinetName());
            } else {
                line.put("cabinetName", "");
            }
            if (battery.getShopId() != null) {
                line.put("shopName", shopService.find(battery.getShopId()).getShopName());
            } else {
                line.put("shopName", "");
            }
            line.put("boxNum", battery.getBoxNum()!= null? battery.getBoxNum() : "");
            if (battery.getUpLineTime() != null) {
                line.put("upLineTime", DateFormatUtils.format(battery.getUpLineTime(), Constant.DATE_TIME_FORMAT));
            } else {
                line.put("upLineTime", "");
            }
            if (battery.getUpLineTime() != null) {
                line.put("useDay", InstallUtils.getUseDay(battery.getUpLineTime()));
            } else {
                line.put("useDay", 0);
            }
            line.put("offLineMemo", battery.getAbnormalCause());
            result.add(line);
        }
        data.put("allCount", allNormalCount);

        Map onLineMap = new HashMap();
        onLineMap.put("onLineCount", agentCustomerUseCount + agentCabinetCount + agentShopCount + otherCount);
        onLineMap.put("inCustomerCount", agentCustomerUseCount);
        onLineMap.put("inCabinetCount", agentCabinetCount);
        onLineMap.put("inShopCount", agentShopCount);
        onLineMap.put("otherCount", otherCount);

        data.put("onLine", onLineMap);
        data.put("offLineCount", notNormalCount);
        data.put("batteryList", result);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    /*
     * 177-查询异常电池列表
     * */

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FaultBatteryListParam {
        public int faultType;
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "/fault_battery_list.htm")
    public RestResult faultBatteryList(@Valid @RequestBody FaultBatteryListParam param ) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        return batteryService.faultBatteryList(agentId, param.faultType, param.offset, param.limit);

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UnbindBatteryOverTimeListParam {
        public Integer offset;
        public Integer limit;
    }

    @ResponseBody
    @RequestMapping(value = "/unbind_battery_over_time_list.htm")
    public RestResult unbindBatteryOverTimeList(@Valid @RequestBody UnbindBatteryOverTimeListParam param ) {
        TokenCache.Data tokenData = getTokenData();
        int agentId = tokenData.agentId;
        if (agentId == 0) {
            return RestResult.result(RespCode.CODE_2.getValue(), "用户信息错误");
        }

        return batteryService.unbindBatteryOverTimeList(agentId, param.offset, param.limit);

    }


}
