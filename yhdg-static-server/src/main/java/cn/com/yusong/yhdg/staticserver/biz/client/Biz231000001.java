package cn.com.yusong.yhdg.staticserver.biz.client;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.TsRespCode;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.domain.basic.CustomerExchangeInfo;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NewBoxNum;
import cn.com.yusong.yhdg.common.protocol.msg22.Msg222000004;
import cn.com.yusong.yhdg.common.protocol.msg23.Msg231000001;
import cn.com.yusong.yhdg.common.protocol.msg23.Msg232000001;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.staticserver.config.AppConfig;
import cn.com.yusong.yhdg.staticserver.constant.RespCode;
import cn.com.yusong.yhdg.staticserver.service.basic.*;
import cn.com.yusong.yhdg.staticserver.service.hdg.*;
import io.netty.channel.ChannelHandlerContext;
import net.spy.memcached.internal.OperationFuture;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 给客户分配一个新满箱通知
 */
@Component
public class Biz231000001 extends AbstractBiz {

    private static final Logger log = LogManager.getLogger(Biz231000001.class);

    @Autowired
    AppConfig config;
    @Autowired
    CustomerService customerService;
    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    BatteryService batteryService;
    @Autowired
    BatteryOrderService batteryOrderService;
    @Autowired
    CustomerExchangeInfoService customerExchangeInfoService;
    @Autowired
    CustomerExchangeBatteryService customerExchangeBatteryService;
    @Autowired
    ExchangeWhiteListService exchangeWhiteListService;
    @Autowired
    AgentService agentService;
    @Autowired
    BespeakOrderService bespeakOrderService;
    @Autowired
    AgentSystemConfigService agentSystemConfigService;
    @Autowired
    MemCachedClient memCachedClient;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg231000001 request = (Msg231000001) message;
        Msg232000001 response = new Msg232000001();
        response.setSerial(request.getSerial());

        Cabinet cabinet = cabinetService.find(request.cabinetId);
        if (cabinet == null) {
            response.rtnCode = (short) RespCode.CODE_2.getValue();
            response.rtnMsg = "换电柜编号不存在";
            writeAndFlush(context, response);
            return;
        }
        Customer customer = customerService.find(request.customerId);
        if (customer == null) {
            response.rtnCode = (short) RespCode.CODE_2.getValue();
            response.rtnMsg = "客户不存在";
            writeAndFlush(context, response);
            return;
        }

        Integer batteryType = null;

        //独立用户特殊处理
        Agent agent = agentService.find(cabinet.getAgentId());
        if(agent != null && agent.getIsIndependent() != null &&  agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()){
            if(StringUtils.isNotEmpty(customer.getIcCard()) ){
                batteryType = Integer.parseInt(customer.getIcCard());
            }
        }else{
            CustomerExchangeInfo customerExchangeInfo = customerExchangeInfoService.find(customer.getId());
            if(customerExchangeInfo != null){
                batteryType = customerExchangeInfo.getBatteryType();
            }else{
                ExchangeWhiteList exchangeWhiteList = exchangeWhiteListService.findByCustomer(cabinet.getAgentId(), customer.getId());
                batteryType = exchangeWhiteList.getBatteryType();
            }
        }

        if(batteryType == null) {
            response.rtnCode = (short) RespCode.CODE_2.getValue();
            response.rtnMsg = "客户没有设置电池类型";
            writeAndFlush(context, response);
            return;
        }

        //如果客户绑定电池，不再开箱门
        //运营商最大电池数
        int maxCount = 1;//默认1块电池
        String maxCountStr = agentSystemConfigService.findConfigValue(cabinet.getAgentId(), ConstEnum.AgentSystemConfigKey.BATTERY_MAX_COUNT.getValue());
        if (!StringUtils.isEmpty(maxCountStr)) {
            maxCount = Integer.parseInt(maxCountStr);
        }
        int batteryCount = customerExchangeBatteryService.exists(customer.getId());
        if(batteryCount >= maxCount){
            response.rtnCode = (short) RespCode.CODE_2.getValue();
            response.rtnMsg = "客户已绑定最大电池数";
            writeAndFlush(context, response);
            return;
        }


        //查询是否有预约订单
        String bespeakBoxNum = null;
        BespeakOrder bespeakOrder = bespeakOrderService.findSuccessByCustomer(customer.getId());
        if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(request.cabinetId)){
            bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
        }

        //不再查找原来柜子中的电池
        String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_BATTERY, request.cabinetId, request.boxNum);
        String batteryId = (String) memCachedClient.get(cacheKey);

        CabinetBox cabinetBox = cabinetBoxService.findOneFull(request.cabinetId, batteryId, batteryType, bespeakBoxNum);
        if (cabinetBox == null) {
            int fullCount = cabinetBoxService.findFullCount(request.cabinetId);
            String errorMessage = "没有符合类型的已充满电池";
            if (fullCount != 0) {
                errorMessage = "扫码者与当前柜子不匹配";
            }
            response.rtnCode = (short) RespCode.CODE_2.getValue();
            response.rtnMsg = errorMessage;
            writeAndFlush(context, response);
            return;
        }

        Battery battery = batteryService.find(cabinetBox.getBatteryId());
        if (battery == null) {
            response.rtnCode = (short) RespCode.CODE_2.getValue();
            response.rtnMsg = "电池不存在:" + cabinetBox.getBatteryId();
            writeAndFlush(context, response);
            return;
        }

        if(bespeakBoxNum == null || !bespeakBoxNum.equals(cabinetBox.getBoxNum())){
            int effect = cabinetBoxService.lockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL.getValue(), CabinetBox.BoxStatus.FULL_LOCK.getValue());
            if (effect == 0) {
                response.rtnCode = (short) RespCode.CODE_2.getValue();
                response.rtnMsg = "锁定箱号失败";
                writeAndFlush(context, response);
                return;
            }
        }

        if (log.isDebugEnabled()) {
            log.debug("openStandardBox: {}, {}", request.cabinetId, cabinetBox.getBoxNum());
        }
        //发送开箱指令
        ClientBizUtils.SerialResult result = ClientBizUtils.openStandardBox(config, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), cabinet.getSubtype());
        //如果协议发送成功
        if (result.getCode() == RespCode.CODE_0.getValue()) {
            Msg222000004 msg222000004 = (Msg222000004) result.getData();
            BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

            NewBoxNum newBoxNum = new NewBoxNum("static-server", request.cabinetId, null, request.boxNum, cabinetBox.getBoxNum(), new Date());
            String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, request.cabinetId, request.boxNum);
            config.memCachedClient.set(cachekey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);

//            String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, request.cabinetId, request.boxNum);
//            config.memCachedClient.set(cachekey, cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);

            if (log.isDebugEnabled()) {
                log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", request.cabinetId, request.boxNum, cabinetBox.getBoxNum());
            }

            cachekey = CacheKey.key(CacheKey.K_CABINET_ID_CUSTOMER_ID_V_ZERO, request.cabinetId, request.customerId);
            CabinetMemcachedLog cabinetMemcachedLog = (CabinetMemcachedLog)config.memCachedClient.get(cachekey);
            if(cabinetMemcachedLog != null){
                if (log.isDebugEnabled()) {
                    cabinetMemcachedLog.setToService("static-server");
                    cabinetMemcachedLog.setNewBatteryOrder(order.getId());
                    log.debug(cabinetMemcachedLog.toString());
                }
                config.memCachedClient.delete(cachekey);
            }

            if (msg222000004.boxStatus != 0) {
                log.error("{} 柜子 {} 箱号 开门状态错误 箱门状态：{}", request.cabinetId, cabinetBox.getBoxNum(), msg222000004.boxStatus);
            }

        } else {
            if (result.getSerial() == -1) {
                cabinetBoxService.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.FULL_LOCK.getValue(), CabinetBox.BoxStatus.FULL.getValue());
            } else {

                BatteryOrder order = batteryOrderService.createNewOrder(customer, battery, cabinet, cabinetBox, bespeakOrder);

                NewBoxNum newBoxNum = new NewBoxNum("static-server", request.cabinetId, null, request.boxNum, cabinetBox.getBoxNum(), new Date());
                String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, request.cabinetId, request.boxNum);
                config.memCachedClient.set(cachekey, newBoxNum, MemCachedConfig.CACHE_FIVE_MINUTE);

//                String cachekey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, request.cabinetId, request.boxNum);
//                config.memCachedClient.set(cachekey, cabinetBox.getBoxNum(), MemCachedConfig.CACHE_FIVE_MINUTE);
                if (log.isDebugEnabled()) {
                    log.debug("K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId: {}, oldBox: {}, newBox: {}", request.cabinetId, request.boxNum, cabinetBox.getBoxNum());
                }

            }
        }

        response.rtnCode = (short) RespCode.CODE_0.getValue();
        response.cabinetId = request.cabinetId;
        response.subcabinetId = cabinet.getId();
        response.boxNum = cabinetBox.getBoxNum();

        writeAndFlush(context, response);
    }
}
