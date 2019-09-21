package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.SyncCursor;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.BatteryOrder;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.utils.OkHttpClientUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SyncCursorMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

@Service
public class ChinaPostSynchDataToShowForMinuteService extends AbstractService {
    static final Logger log = LogManager.getLogger(ChinaPostSynchDataToShowForMinuteService.class);
    String SHOW_URL= "http://122.224.164.50:5520/synch_data/accept.htm";
    String SYS_FLAG = "ZGYZ";


    @Autowired
    AppConfig appConfig;
    @Autowired
    SyncCursorMapper syncCursorMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    AreaCache areaCache;

    public void transfer(){
        log.debug("----- 同步数据到演示环境分钟 begin -----");
        synchBattery(); //电池 type=3
        log.debug("----- synchBattery success -----");

        synchBatteryOrder(); //换电订单 type=5
        log.debug("----- synchBatteryOrder success -----");

        try {
            synchBatteryOrderForPut(); //换电订单结束时间数据 type=6
        } catch (ParseException e) {
            log.error("synchBatteryOrderForPut 日期转换失败：", e);
            e.printStackTrace();
        }
        log.debug("----- synchBatteryOrderForPut success -----");

//        synchPacketPeriodOrder(); //包月订单 type=8
//        log.debug("----- synchPacketPeriodOrder success -----");

        synchDataStats(); //统计数据 type=9
        log.debug("----- synchDataStats success -----");

        log.debug("----- 同步数据到演示环境分钟 end -----");
    }

    /**
     * 电池
     */
    private void synchBattery(){
        int offset = 0, limit = 1000;
        while (true) {
            List<Battery> batteryList = batteryMapper.findByAgent(9, offset, limit);
            if (batteryList.isEmpty()) {
                break;
            }
            //组装查询数据
            List<Map> dataList = new ArrayList();
            for(Battery entity : batteryList){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", SYS_FLAG + entity.getId());
                data.put("code", entity.getCode());
                data.put("voltage", entity.getVoltage());
                data.put("electricity", entity.getElectricity());
                data.put("volume", entity.getVolume());
                data.put("status", entity.getStatus());
                data.put("chargeStatus", entity.getChargeStatus());
                data.put("type", entity.getType());
                data.put("reportTime", entity.getReportTime());
                data.put("createTime", entity.getCreateTime());
                if(entity.getCabinetId() != null){
                    data.put("subcabinetId", SYS_FLAG + entity.getCabinetId());
                }else{
                    data.put("subcabinetId", entity.getCabinetId());
                }
                data.put("boxNum", entity.getBoxNum());
                data.put("customerMobile", entity.getCustomerMobile());
                data.put("customerFullname", entity.getCustomerFullname());
                data.put("lng", entity.getLng());
                data.put("lat", entity.getLat());
                dataList.add(data);
            }
            //发送请求
            sendDate(dataList,3);

            offset += limit;
        }
    }

    /**
     * 换电订单
     */
    private void synchBatteryOrder(){
        int limit = 1000;
        while (true) {
            String cursorId = null;
            //查询游标
            SyncCursor syncCursor = syncCursorMapper.findByType(SyncCursor.Type.BATTERY_ORDER.getValue());
            if(syncCursor != null){
                cursorId = syncCursor.getId();
            }else{
                //初始化游标
                cursorId = batteryOrderMapper.findMaxId();
                syncCursorMapper.create(SyncCursor.Type.BATTERY_ORDER.getValue(),cursorId);
            }

            List<BatteryOrder> batteryOrderList = batteryOrderMapper.findByCursorAndAgent( cursorId,9, limit);
            if (batteryOrderList.isEmpty()) {
                break;
            }

            //组装查询数据
            List<Map> dataList = new ArrayList();
            for(BatteryOrder entity : batteryOrderList){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", SYS_FLAG +  entity.getId());
                data.put("batteryId", SYS_FLAG +  entity.getBatteryId());
                data.put("customerMobile", entity.getCustomerMobile());
                data.put("customerFullname", entity.getCustomerFullname());
                data.put("provinceId", entity.getProvinceId());
                data.put("cityId", entity.getCityId());
                data.put("districtId", entity.getDistrictId());
                if(entity.getPutCabinetId() != null) {
                    data.put("putSubcabinetId", SYS_FLAG + entity.getPutCabinetId());
                }else {
                    data.put("putSubcabinetId", null);
                }
                data.put("putBoxNum", entity.getPutBoxNum());
                data.put("putTime", entity.getPutTime());
                data.put("takeSubcabinetId", SYS_FLAG +  entity.getTakeCabinetId());
                data.put("takeBoxNum", entity.getTakeBoxNum());
                data.put("takeTime", entity.getTakeTime());
                data.put("payType", entity.getPayType());
                data.put("money", entity.getMoney());
                data.put("orderStatus", entity.getOrderStatus());
                data.put("createTime", entity.getCreateTime());
                dataList.add(data);

                cursorId = entity.getId();
            }
            //发送请求
            boolean returnStatus = sendDate(dataList,5);
            if(returnStatus){
                //更新游标
                syncCursorMapper.updateByType(SyncCursor.Type.BATTERY_ORDER.getValue(),cursorId);
            }
        }
    }

    /**
     * 换电订单结束
     */
    private void synchBatteryOrderForPut() throws ParseException {
        int limit = 1000;
        while (true) {
            String cursorId = null;
            //查询游标
            SyncCursor syncCursor = syncCursorMapper.findByType(SyncCursor.Type.BATTERY_ORDER_FOR_PUT.getValue());
            if(syncCursor != null){
                cursorId = syncCursor.getId();
            }else{
                //初始化游标
                cursorId = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
                syncCursorMapper.create(SyncCursor.Type.BATTERY_ORDER_FOR_PUT.getValue(),cursorId);
            }

            Date putTime = DateUtils.parseDate(cursorId, "yyyy-MM-dd HH:mm:ss");
            List<BatteryOrder> batteryOrderList = batteryOrderMapper.findAllForPutByAgent(9,putTime, limit);
            if (batteryOrderList.isEmpty()) {
                break;
            }

            //组装查询数据
            Date maxPutTime = null;
            List<Map> dataList = new ArrayList();
            for(BatteryOrder entity : batteryOrderList){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", SYS_FLAG +  entity.getId());
                data.put("batteryId", SYS_FLAG +  entity.getBatteryId());
                data.put("customerMobile", entity.getCustomerMobile());
                data.put("customerFullname", entity.getCustomerFullname());
                data.put("provinceId", entity.getProvinceId());
                data.put("cityId", entity.getCityId());
                data.put("districtId", entity.getDistrictId());
                data.put("putSubcabinetId", SYS_FLAG +  entity.getPutCabinetId());
                data.put("putBoxNum", entity.getPutBoxNum());
                data.put("putTime", entity.getPutTime());
                data.put("takeSubcabinetId", SYS_FLAG +  entity.getTakeCabinetId());
                data.put("takeBoxNum", entity.getTakeBoxNum());
                data.put("takeTime", entity.getTakeTime());
                data.put("payType", entity.getPayType());
                data.put("money", entity.getMoney());
                data.put("orderStatus", entity.getOrderStatus());
                data.put("createTime", entity.getCreateTime());
                dataList.add(data);

                maxPutTime = entity.getPutTime();
            }
            //发送请求
            boolean returnStatus = sendDate(dataList,6);
            if(returnStatus){
                //更新游标
                if(maxPutTime != null)
                    syncCursorMapper.updateByType(SyncCursor.Type.BATTERY_ORDER_FOR_PUT.getValue(),DateFormatUtils.format(maxPutTime, "yyyy-MM-dd HH:mm:ss"));
            }
        }
    }

    /**
     * 统计数据  当日新增用户数  当日异常换电数
     */
    private void synchDataStats(){
        Date todayBeginTime = org.apache.commons.lang.time.DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        //查询当日数据
        int customerCount = customerMapper.findCountByTodayAndAgent(9);
        int faultLogCount = faultLogMapper.findTotalCountAndAgent(9, todayBeginTime, null);

        Map data = new HashMap();
        data.put("sys",SYS_FLAG);
        data.put("customerCount",customerCount);
        data.put("faultLogCount", faultLogCount);

        //发送请求
        sendDate(data,9);
    }

    private boolean sendDate(Object data, int dataType){
        boolean flag = false;
        Map<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        OkHttpClientUtils.HttpResp httpResp = null;
        try {
            //组装参数
            Map params = new HashMap();
            params.put("type",dataType);
            params.put("sys",SYS_FLAG);
            params.put("data",data);

            httpResp = OkHttpClientUtils.post(String.format(SHOW_URL,dataType), YhdgUtils.encodeJson(params),header);
            if(httpResp != null && httpResp.status == 200) {
                log.debug("invoke type={} success ",dataType);
                flag = true;
            }else{
                log.debug("invoke type={} fail {}",dataType,httpResp);
            }
        } catch (Exception e) {
            log.error("发送失败：", e);
        }
        return  flag;
    }
}
