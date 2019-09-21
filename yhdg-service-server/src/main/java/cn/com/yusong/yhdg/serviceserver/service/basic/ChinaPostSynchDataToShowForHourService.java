package cn.com.yusong.yhdg.serviceserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.SyncCursor;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.common.utils.OkHttpClientUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import cn.com.yusong.yhdg.serviceserver.config.AppConfig;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.CustomerMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SyncCursorMapper;
import cn.com.yusong.yhdg.serviceserver.persistence.hdg.*;
import cn.com.yusong.yhdg.serviceserver.service.AbstractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChinaPostSynchDataToShowForHourService extends AbstractService {
    static final Logger log = LogManager.getLogger(ChinaPostSynchDataToShowForHourService.class);
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
    BatteryOrderHistoryMapper batteryOrderHistoryMapper;
    @Autowired
    AreaCache areaCache;

    public void transfer(){
        log.debug("----- 同步数据到演示环境小时统计 begin -----");

        boolean daySync = true;
        Calendar calendar = new GregorianCalendar();
        Date now = calendar.getTime();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min =  calendar.get(Calendar.MINUTE);

        //按日同步数据 每天2点
//        if(hour != 2) {
//            daySync = false;
//        }

        if(daySync){
            synchSubcabine(); //换电柜 type=1
            log.debug("----- synchSubcabine success -----");
            synchSubcabineBox(); //换电柜 格子 type=2
            log.debug("----- synchSubcabineBox success -----");
            synchCustomer(); //用户 type=4
            log.debug("----- synchCustomer success -----");
           // synchFaultLog(); //故障日志 type=7
            log.debug("----- synchFaultLog success -----");
            synchDataStats(); //统计数据 type=10
            log.debug("----- synchDataStats——hour success -----");
        }

        log.debug("----- 同步数据到演示环境小时统计 end -----");
    }

    /**
     * 换电柜
     */
    private void synchSubcabine(){
        int offset = 0, limit = 1000;
        while (true) {
            List<Cabinet> cabinetList =  (List<Cabinet>) setAreaProperties(areaCache,cabinetMapper.findAllByAgent(9, offset, limit));
            if (cabinetList.isEmpty()) {
                break;
            }
            //组装查询数据
            List<Map> dataList = new ArrayList();
            for(Cabinet entity : cabinetList){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("id", SYS_FLAG + entity.getId());
                data.put("cabinetName", entity.getCabinetName());
                data.put("temp1", entity.getTemp1() == null ? 0 : entity.getTemp1() / 100);
                data.put("temp2", entity.getTemp2() == null ? 0 : entity.getTemp2() / 100);
                data.put("isOnline", entity.getIsOnline());
                data.put("provinceId", entity.getProvinceId());
                data.put("cityId", entity.getCityId());
                data.put("districtId", entity.getDistrictId());
                data.put("provinceName", entity.getProvinceName());
                data.put("cityName", entity.getCityName());
                data.put("districtName", entity.getDistrictName());
                data.put("street", entity.getStreet());
                data.put("lng", entity.getLng());
                data.put("lat", entity.getLat());
                data.put("chargeFullVolume", entity.getChargeFullVolume());

                dataList.add(data);
            }
            //发送请求
            sendDate(dataList,1);

            offset += limit;
        }
    }

    /**
     * 格口
     */
    private void synchSubcabineBox(){
        int offset = 0, limit = 1000;
        while (true) {
            List<CabinetBox> cabinetBoxList = cabinetBoxMapper.findAllByAgent(9, offset, limit);
            if (cabinetBoxList.isEmpty()) {
                break;
            }
            //组装查询数据
            List<Map> dataList = new ArrayList();
            for(CabinetBox entity : cabinetBoxList){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("subcabinetId", SYS_FLAG + entity.getCabinetId());
                data.put("boxNum", entity.getBoxNum());
                data.put("boxStatus", entity.getBoxStatus());
                data.put("batteryId", entity.getBatteryId());
                dataList.add(data);
            }
            //发送请求
            sendDate(dataList,2);

            offset += limit;
        }
    }

    /**
     * 用户
     */
    private void synchCustomer(){
        int offset = 0, limit = 1000;
        while (true) {
            List<Customer> customerList = customerMapper.findAllByAgent(9, offset, limit);
            if (customerList.isEmpty()) {
                break;
            }
            //组装查询数据
            List<Map> dataList = new ArrayList();
            for(Customer entity : customerList){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("mobile", entity.getMobile());
                data.put("fullname", entity.getFullname());
                data.put("idCard", entity.getIdCard());
                data.put("createTime", entity.getCreateTime());
                data.put("batteryId", "");
                data.put("foregift", "");
                dataList.add(data);
            }
            //发送请求
            sendDate(dataList,4);

            offset += limit;
        }
    }

    /**
     * 故障日志
     */
    private void synchFaultLog(){
        int limit = 1000;
        while (true) {
            String cursorId = null;
            //查询游标
            SyncCursor syncCursor = syncCursorMapper.findByType(SyncCursor.Type.FAULT_LOG.getValue());
            if(syncCursor != null){
                cursorId = syncCursor.getId();
            }else{
                //初始化游标
                Integer id = faultLogMapper.findMaxId();
                if(id != null){
                    cursorId = id.toString();
                }
                syncCursorMapper.create(SyncCursor.Type.FAULT_LOG.getValue(),cursorId);
            }

            List<FaultLog> faultLogList = faultLogMapper.findAllByCursorAndAgent(9, Integer.parseInt(cursorId), limit);
            if (faultLogList.isEmpty()) {
                break;
            }

            //组装查询数据
            List<Map> dataList = new ArrayList();
            for(FaultLog entity : faultLogList){
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("orderId", SYS_FLAG + entity.getAgentId());
                data.put("batteryId", SYS_FLAG +  entity.getBatteryId());
                data.put("boxNum", entity.getBoxNum());
                data.put("faultType", entity.getFaultType());
                data.put("faultContent", entity.getFaultContent());
                data.put("status", entity.getStatus());
                data.put("handleType", entity.getHandleType());
                data.put("handleMemo", entity.getHandleMemo());
                data.put("handleTime", entity.getHandleTime());
                data.put("handlerName", entity.getHandlerName());
                data.put("createTime", entity.getCreateTime());
                dataList.add(data);

                cursorId = entity.getId().toString();
            }
            //发送请求
            boolean returnStatus = sendDate(dataList,7);
            if(returnStatus){
                //更新游标
                syncCursorMapper.updateByType(SyncCursor.Type.FAULT_LOG.getValue(),cursorId);
            }
        }
    }

    /**
     * 统计数据  总换电数  总异常换电数 总换电地区排行
     */
    private void synchDataStats(){
        Date todayBeginTime = org.apache.commons.lang.time.DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
        //查询总换电数
        //log.debug("查询总换电数开始");
        int totalOrderCount = batteryOrderMapper.findTotalCountByAgent(9,null, todayBeginTime);
        //log.debug("查询到yhdg表总换电数：{} ",totalOrderCount);
        List<String> list = batteryOrderHistoryMapper.findTable("hdg_battery_order_20%");
        for (String table : list) {
            //log.debug("查询到table：{} ",table);
            totalOrderCount += batteryOrderHistoryMapper.findTotalCountByAgent(9, table, null, todayBeginTime);
            //log.debug("查询到历史表总换电数：{} ",totalOrderCount);
        }
        //log.debug("查询总换电数结束");

        //总异常换电数
        int faultLogCount = faultLogMapper.findTotalCountByAgent(9,null, todayBeginTime);
        //log.debug("总异常换电数：{} ",faultLogCount);

        //总换电地区排行
        //log.debug("查询总换电地区排行开始");
        List <BatteryOrder>  orderList = new ArrayList();
        orderList.addAll(batteryOrderMapper.findCountByCityAndAgent(9,null, todayBeginTime));
        //log.debug("总yhdg表排行size：{} ",orderList.size());
        for (String table : list) {
            //log.debug("查询到table：{} ",table);
            orderList.addAll(batteryOrderHistoryMapper.findCountByCityAndAgent(9, table));
            //log.debug("总历史表排行size：{} ",orderList.size());
        }
        //log.debug("查询总换电地区排序开始");
        List <Map>  areaList = new ArrayList();
//        Map<Integer,Integer>areaMap = new HashMap();
//        for (BatteryOrder batteryOrder : orderList) {
//            if (areaMap.containsKey(batteryOrder.getCityId())) {
//                areaMap.put(batteryOrder.getCityId(), areaMap.get(batteryOrder.getCityId()) + batteryOrder.getOrderCount());
//            } else {
//                areaMap.put(batteryOrder.getCityId(),  batteryOrder.getOrderCount());
//            }
//        }
//        for (Map.Entry<Integer, Integer> entry : areaMap.entrySet()) {
//            Area area =  areaCache.get(entry.getKey());
//            if(area == null){
//                continue;
//            }
//            Map map = new HashMap();
//            map.put("cityId",entry.getKey());
//            map.put("cityName",area.getAreaName());
//            map.put("count",entry.getValue());
//            areaList.add(map);
//        }

        //log.debug("查询总换电地区排序结束");
        Map data = new HashMap();
        data.put("sys",SYS_FLAG);
        data.put("totalOrderCount",totalOrderCount);
        data.put("faultLogCount", faultLogCount);
        data.put("areaList", areaList);

        //发送请求
        sendDate(data,10);
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
