package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.cabinetserver.comm.session.CabinetSession;
import cn.com.yusong.yhdg.cabinetserver.constant.RespCode;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetBoxService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.protocol.msg08.HeartParam;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg081000001;
import cn.com.yusong.yhdg.common.protocol.msg08.Msg082000001;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * 心跳
 */
@Component
public class Biz081000001 extends AbstractBiz {

    @Autowired
    CabinetService cabinetService;
    @Autowired
    CabinetBoxService cabinetBoxService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg081000001 request = (Msg081000001) message;
        Msg082000001 response = new Msg082000001();
        response.setSerial(request.getSerial());

        if (StringUtils.isEmpty(request.code)) {
            response.rtnCode = RespCode.CODE_2.getValue();
            writeAndFlush(context, response);
            return;
        }
        HeartParam heartParam = request.toParam();
        Cabinet cabinet = cabinetService.insertOrUpdate(request.code, request.version, heartParam.temp1, heartParam.temp2,null,0,heartParam.boxList, ConstEnum.Flag.FALSE.getValue(),null,null,null,null, null, null);
        String cabinetId = cabinet.getId();

        String serverAddress = config.getServerIp() + ":" + config.getServerPort();
        sessionManager.addCabinetSession(context, attributes, cabinetId, CabinetSession.NORMAL_CABINET_HERT);
        memCachedClient.set(CacheKey.key(CacheKey.K_SUBCABINET_ID_V_LOGIN_SERVER, cabinetId), serverAddress, MemCachedConfig.CACHE_FIVE_MINUTE);

        List<Integer> openBoxList = new ArrayList<Integer>();
        List<Battery> batteryList = new ArrayList<Battery>();


        if (StringUtils.isNotEmpty(cabinet.getId()) && cabinet.getUpLineStatus() == Cabinet.UpLineStatus.ONLINE.getValue() ) {

            cabinetService.heart(heartParam, cabinetId, openBoxList, batteryList);

        } else {
            if (log.isErrorEnabled()) {
                log.error("换电柜{}未上线", cabinet.getId());
            }

        }

        List<CabinetBox> boxList = cabinetBoxService.findBoxBatteryList(cabinetId);
        response.cabinetType = (byte) (cabinet.getSubtype() == Cabinet.Subtype.EXCHANGE_CHARGE.getValue() ? 0 : 1);
        response.activeFanTemp = cabinet.getActiveFanTemp().byteValue();
        for (CabinetBox e : boxList) {
            Msg082000001.Box box = new Msg082000001.Box();
            box.boxNum = Byte.parseByte(e.getBoxNum(), 10);
            if (StringUtils.isEmpty(e.getBatteryId())) {
                box.batteryStatus = Msg082000001.BATTERY_STATUS_EMPTY;
            } else {
                if (e.getBatteryStatus() != Battery.Status.IN_BOX.getValue()) {
                    box.batteryStatus = Msg082000001.BATTERY_STATUS_NOT_CHARGE;
                } else {
                    if (e.getVolume() != null && e.getFullVolume() != null && e.getVolume() >= e.getFullVolume()) {
                        box.batteryStatus = Msg082000001.BATTERY_STATUS_FULL;
                    } else if (e.getChargeStatus() != null && e.getChargeStatus() == Battery.ChargeStatus.CHARGING.getValue()) {
                        box.batteryStatus = Msg082000001.BATTERY_STATUS_CHARGING;
                    } else {
                        box.batteryStatus = Msg082000001.BATTERY_STATUS_NOT_CHARGE;
                    }
                }
            }

            response.boxList.add(box);
        }

        //异步保存柜子心跳日志
        saveHeartLog(cabinet, heartParam, batteryList);

        writeAndFlush(context, response);

        openBox(context, openBoxList);
    }
}
