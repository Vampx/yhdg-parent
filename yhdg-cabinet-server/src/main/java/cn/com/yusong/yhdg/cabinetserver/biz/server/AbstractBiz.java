package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.cabinetserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.cabinetserver.config.AppConfig;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.BatteryService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetChargerService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetReport;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetReportBattery;
import cn.com.yusong.yhdg.common.protocol.msg07.Msg071000001;
import cn.com.yusong.yhdg.common.protocol.msg08.HeartParam;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.netty.Biz;
import cn.com.yusong.yhdg.common.tool.netty.Message;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

public abstract class AbstractBiz extends Biz {

    static final Logger log = LogManager.getLogger(AbstractBiz.class);

    @Autowired
    SessionManager sessionManager;
    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    AppConfig config;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    CabinetChargerService cabinetChargerService;
    @Autowired
    BatteryService batteryService;


    protected void openBox(ChannelHandlerContext context, List<Integer> openBoxList) {
        Set<Integer> set = new HashSet<Integer>(openBoxList);
        for(Integer e : set) {
            Msg071000001 msg = new Msg071000001();
            msg.boxType = 0;
            msg.lockNum = e.byteValue();
            msg.setSerial(config.sequence.incrementAndGet());
            writeAndFlush(context, msg);
        }
    }

    public static Map<Integer, Resp> respPool = new ConcurrentHashMap<Integer, Resp>();
    public static final Runnable SCAN_TASK = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            List<Integer> expireList = new LinkedList<Integer>();
            for(Map.Entry<Integer, Resp> kv : respPool.entrySet()) {
                if(now - kv.getValue().timestamp >= 1000 * 15) {
                    expireList.add(kv.getKey());
                }
            }
            for(Integer k : expireList) {
                respPool.remove(k);
            }

            if(log.isDebugEnabled()) {
                log.debug("respPool.size = {}", respPool.size());
            }


            try {
                Thread.sleep(1000 * 5);
            } catch (Exception e) {
            }
        }
    };

    static {
        Thread thread = new Thread(SCAN_TASK);
        thread.start();
    }

    public static class Resp {
        public ChannelHandlerContext context;
        public Map<String, Object> attributes;
        public Message msg;
        public long timestamp = System.currentTimeMillis();

        public Resp(ChannelHandlerContext context, Map<String, Object> attributes, Message msg) {
            this.context = context;
            this.attributes = attributes;
            this.msg = msg;
        }
    }

    public void saveHeartLog(Cabinet cabinet, HeartParam heartParam, List<Battery> batteryList){
        config.heartLogExecutorService.submit(new LogThread(cabinet, heartParam, batteryList));
    }

    public class LogThread implements Runnable{
        Cabinet cabinet;
        HeartParam heartParam;
        List<Battery> batteryList;
        public LogThread(Cabinet cabinet, HeartParam heartParam, List<Battery> batteryList){
            this.cabinet = cabinet;
            this.heartParam = heartParam;
            this.batteryList = batteryList;
        }

        @Override
        public void run() {
//            CabinetReport cabinetReport = new CabinetReport();
//            cabinetReport.setCabinetId(cabinet.getId());
//            cabinetReport.setRequestBodyHex(request.requestBodyHex);
//            cabinetReport.setRequestBodyObj(request.toString());
//            cabinetReport.setResponseBodyObj(response.toString());
//            cabinetReport.setSuffix(DateFormatUtils.format(new Date(), "yyyyMMdd"));
//            cabinetReport.setCreateTime(new Date());
//            cabinetService.insertCabinetReport(cabinetReport);
            //更新充电器
            cabinetChargerService.update(cabinet.getId(), heartParam.boxList);

            for(Battery battery : batteryList){
                //电池上报处理
                cabinetService.report(cabinet, battery);
            }
        }
    }

}
