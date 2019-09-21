package cn.com.yusong.yhdg.serviceserver.service.hdg;


import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.utils.OkHttpClientUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerExchangeBatteryMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 智租推送
 */
@Service
public class ZhiZuPushMessageService {
    static Logger log = LogManager.getLogger(ZhiZuPushMessageService.class);
    Integer agentId = 23;
//    String TAKE_URL = "https://exchange-api.zhizukj.com/zhizu/exchange/api/exchange_battery_order/take_battery.json";
//    String PUT_URL = "https://exchange-api.zhizukj.com/zhizu/exchange/api/exchange_battery_order/put_battery.json";
//    String BACK_URL = "https://exchange-api.zhizukj.com/zhizu/exchange/api/back_battery_order/back_battery.json";
//    String PUT_ERROR_URL = "https://exchange-api.zhizukj.com/zhizu/exchange/api/exchange_battery_order/error_battery.json";
//    String BATTERY_URL = "http://devicej.zhizukj.com:8082/battery/report.json";
//    String CABINET_DEGREE_URL = "https://exchange-api.zhizukj.com/zhizu/exchange/api/cabinet_degree/report.json";

    String TAKE_URL = "https://exchange-api-sit.zhizukj.com/zhizu/exchange/api/exchange_battery_order/take_battery.json";
    String PUT_URL = "https://exchange-api-sit.zhizukj.com/zhizu/exchange/api/exchange_battery_order/put_battery.json";
    String BACK_URL = "https://exchange-api-sit.zhizukj.com/zhizu/exchange/api/back_battery_order/back_battery.json";
    String PUT_ERROR_URL = "https://exchange-api-sit.zhizukj.com/zhizu/exchange/api/exchange_battery_order/error_battery.json";
    String BATTERY_URL = "http://devicej-sit.zhizukj.com:8082/battery/report.json";
    String CABINET_DEGREE_URL = "https://exchange-api-sit.zhizukj.com/zhizu/exchange/api/cabinet_degree/report.json";

    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    CabinetDayDegreeStatsMapper cabinetDayDegreeStatsMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    MemCachedClient memCachedClient;



    public void scanMessage() throws Exception {

        int offset = 0, limit = 20; //这里offset 不用做累加 因为处理完就无法查找到了
        while (true) {
            List<PushOrderMessage> pushOrderMessageList = pushOrderMessageMapper.findList(PushOrderMessage.SendStatus.NOT.getValue(), offset, limit);
            if (pushOrderMessageList.isEmpty()) {
                break;
            }
//            log.debug("push message size:{}", pushMessageList.size());
            for (PushOrderMessage pushOrderMessage : pushOrderMessageList) {
                sendMsg(pushOrderMessage);

            }
            //offset += limit;
        }
    }

    public void sendMsg(PushOrderMessage pushOrderMessage) throws Exception {
        Map params = new HashMap();
        Battery battery = null;

        if(pushOrderMessage.getSourceType() == PushOrderMessage.SourceType.TAKE.getValue()){
            BatteryOrder batteryOrder = batteryOrderMapper.find(pushOrderMessage.getSourceId());
            if(batteryOrder != null){
                params.put("id", batteryOrder.getId());
                battery = batteryMapper.find(batteryOrder.getBatteryId());
                if(battery != null){
                    params.put("batteryId", battery.getCode());
                }
                params.put("takeCabinetId", batteryOrder.getTakeCabinetId());
                params.put("takeCabinetName", batteryOrder.getTakeCabinetName());
                params.put("takeSubcabinetId", batteryOrder.getTakeCabinetId());
                params.put("takeSubcabinetName", batteryOrder.getTakeCabinetName());
                params.put("takeBoxNum", batteryOrder.getTakeBoxNum());
                params.put("takeTime", DateFormatUtils.format(batteryOrder.getTakeTime(), Constant.DATE_TIME_FORMAT));
                params.put("initVolume", batteryOrder.getInitVolume());
                params.put("customerMobile", batteryOrder.getCustomerMobile());
            }
        }else if(pushOrderMessage.getSourceType() == PushOrderMessage.SourceType.PUT.getValue()){
            BatteryOrder batteryOrder = batteryOrderMapper.find(pushOrderMessage.getSourceId());
            if(batteryOrder != null){
                params.put("id", batteryOrder.getId());
                battery = batteryMapper.find(batteryOrder.getBatteryId());
                if(battery != null){
                    params.put("batteryId", battery.getCode());
                }
                params.put("putCabinetId", batteryOrder.getPutCabinetId());
                params.put("putCabinetName", batteryOrder.getPutCabinetName());
                params.put("putSubcabinetId", batteryOrder.getPutCabinetId());
                params.put("putSubcabinetName", batteryOrder.getPutCabinetName());
                params.put("putBoxNum", batteryOrder.getPutBoxNum());
                if (batteryOrder.getPutTime() != null) {
                    params.put("putTime", DateFormatUtils.format(batteryOrder.getPutTime(), Constant.DATE_TIME_FORMAT));
                } else {
                    params.put("putTime", "");
                }
                params.put("volume", batteryOrder.getInitVolume());
                params.put("customerMobile", batteryOrder.getCustomerMobile());
            }
        }else if(pushOrderMessage.getSourceType() == PushOrderMessage.SourceType.BACK.getValue()){
            BackBatteryOrder batteryOrder = backBatteryOrderMapper.find(pushOrderMessage.getSourceId());
            if(batteryOrder != null){
                params.put("id", batteryOrder.getId());
                params.put("putTime", batteryOrder.getBackTime());
                params.put("customerMobile", batteryOrder.getCustomerMobile());
            }
        }else if(pushOrderMessage.getSourceType() == PushOrderMessage.SourceType.PUT_ERROR.getValue()){
            String [] sourceIds = pushOrderMessage.getSourceId().split(":");
            String batteryId = sourceIds[0];
            Long customerId = Long.parseLong(sourceIds[1]);
            Customer customer = customerMapper.find(customerId);
            CustomerExchangeBattery customerExchangeBattery = customerExchangeBatteryMapper.findOneByCustomer(customerId);
            if(customerExchangeBattery != null ){
                params.put("batteryId",customerExchangeBattery.getBatteryCode());
            }
            params.put("customerMobile", customer.getMobile());

//            BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
//            if(batteryOrder != null){
//                battery = batteryMapper.find(batteryOrder.getBatteryId());
//                if(battery != null){
//                    params.put("batteryId", battery.getCode());
//                }
//
//            }
        }

        boolean success = sendDate(pushOrderMessage.getSourceType(),params);
        if (success) {
            //发送成功更新状态
            pushOrderMessageMapper.complete(pushOrderMessage.getId(), new Date(), PushOrderMessage.SendStatus.OK.getValue(), pushOrderMessage.getResendNum());
        } else {
            //发送失败
            log.error("push message error");
            if (pushOrderMessage.getResendNum() == null) {
                pushOrderMessage.setResendNum(1);
                //更新状态，第一次发送失败，重发次数为1
                pushOrderMessageMapper.complete(pushOrderMessage.getId(), new Date(), PushMessage.MessageStatus.NOT.getValue(), pushOrderMessage.getResendNum());
            } else if (pushOrderMessage.getResendNum() != null && pushOrderMessage.getResendNum() > 2) {
                //更新状态，失败此时大于2次，设置为发送失败
                pushOrderMessageMapper.complete(pushOrderMessage.getId(), new Date(), PushMessage.MessageStatus.FAIL.getValue(), pushOrderMessage.getResendNum());
            } else {
                //更新状态，失败此时少于3次，重发次数加1
                pushOrderMessage.setResendNum(pushOrderMessage.getResendNum() + 1);
                pushOrderMessageMapper.complete(pushOrderMessage.getId(), new Date(), PushMessage.MessageStatus.NOT.getValue(), pushOrderMessage.getResendNum());
            }
        }

    }

    public void pushBatteryInfo() throws Exception {
        int offset = 0, limit = 50; //这里offset 不用做累加 因为处理完就无法查找到了
        while (true) {
            List<BatteryParameter> batteryParameterList = batteryMapper.findListByAgent(agentId, offset, limit);
            if (batteryParameterList.isEmpty()) {
                break;
            }

            //每次推送50条数据
           log.debug("push pushBatteryInfo size:{}", batteryParameterList.size());
            List batteryList = new ArrayList();
            for (BatteryParameter batteryParameter : batteryParameterList) {
                Map map = new HashMap();
                map.put("id", batteryParameter.getCode());
                map.put("type", batteryParameter.getType());
                map.put("code", batteryParameter.getCode());
                map.put("simCode", batteryParameter.getSimCode());
                map.put("version", batteryParameter.getVersion());
                map.put("currentSignal", batteryParameter.getCurrentSignal());
                map.put("lng", batteryParameter.getLng());
                map.put("lat", batteryParameter.getLat());
                map.put("lbs", batteryParameter.getLbs());
                map.put("voltage", batteryParameter.getVoltage());
                map.put("electricity", batteryParameter.getElectricity());
                map.put("serials", batteryParameter.getSerials());
                map.put("singleVoltage", batteryParameter.getSingleVoltage());
                map.put("currentCapacity", batteryParameter.getCurrentCapacity());
                map.put("volume", batteryParameter.getVolume());
                map.put("circle", batteryParameter.getCircle());
                map.put("mos", batteryParameter.getMos());
                map.put("temp", batteryParameter.getTemp());
                map.put("fault", batteryParameter.getFault());

                map.put("reportTime", batteryParameter.getShortReportTime());

                batteryList.add(map);
            }
            Map pushMap = new HashMap();
            pushMap.put("offset", offset);
            pushMap.put("limit", limit);
            pushMap.put("batteryList", batteryList);
            sendDate(4, pushMap);

            offset += limit;
        }
    }

    public void pushCabinetDayDegree() throws Exception {
        String statsDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);

        int offset = 0, limit = 50; //这里offset 不用做累加 因为处理完就无法查找到了
        while (true) {
            List<CabinetDayDegreeStats> cabinetDayStatsList = cabinetDayDegreeStatsMapper.findListByAgent(agentId, statsDate, offset, limit);
            if (cabinetDayStatsList.isEmpty()) {
                break;
            }

            //每次推送50条数据
            log.debug("push cabinetDayStatsList size:{}", cabinetDayStatsList.size());
            List statsList = new ArrayList();
            for (CabinetDayDegreeStats cabinetDayStats : cabinetDayStatsList) {
                Map map = new HashMap();
                map.put("cabinetId", cabinetDayStats.getCabinetId());
                map.put("cabinetName", cabinetDayStats.getCabinetName());
                map.put("statsDate", cabinetDayStats.getStatsDate());
                map.put("beginTime", cabinetDayStats.getBeginTime());
                map.put("endTime", cabinetDayStats.getEndTime());
                map.put("beginNum", cabinetDayStats.getBeginNum());
                map.put("endNum", cabinetDayStats.getEndNum());
                map.put("num", cabinetDayStats.getNum());
                statsList.add(map);
            }
            Map pushMap = new HashMap();
            pushMap.put("offset", offset);
            pushMap.put("limit", limit);
            pushMap.put("cabinetList", statsList);
            sendDate(5, pushMap);

            offset += limit;
        }
    }




//    public void cleanMessage() {
//        List<Integer> sendStatus = new LinkedList<Integer>();
//        sendStatus.add(PushMessage.MessageStatus.NOT.getValue());
//
//        Date createTime = DateUtils.addDays(DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH), -30); //30天之前的数据
//        List<Integer> list = pushMessageMapper.findByStatus(sendStatus, createTime, 100);
//        for (Integer id : list) {
//            pushMessageMapper.delete(id);
//        }
//    }

    private boolean sendDate(Integer sourceType, Map params){
        boolean flag = false;
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        OkHttpClientUtils.HttpResp httpResp = null;
        try {
            String url = null;
            if(sourceType == PushOrderMessage.SourceType.TAKE.getValue()){
                url = TAKE_URL;
            }else if(sourceType == PushOrderMessage.SourceType.PUT.getValue()){
                url = PUT_URL;
            }else if(sourceType == PushOrderMessage.SourceType.BACK.getValue()){
                url = BACK_URL;
            }else if(sourceType == PushOrderMessage.SourceType.PUT_ERROR.getValue()){
                url = PUT_ERROR_URL;
            }else if(sourceType == 4){//电池推送
                url = BATTERY_URL;
            }else if(sourceType == 5){//换电柜电量推送
                url = CABINET_DEGREE_URL;
            }
            httpResp = OkHttpClientUtils.post(url, YhdgUtils.encodeJson(params),header);
            if(httpResp != null && httpResp.status == 200) {
                log.debug("invoke type={} success ",PushOrderMessage.SourceType.getName(sourceType));
                flag = true;
            }else{
                log.debug("invoke type={} fail {}",PushOrderMessage.SourceType.getName(sourceType),httpResp);
            }
        } catch (Exception e) {
            log.error("发送失败：", e);
        }
        return  flag;
    }
}
