package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.cabinetserver.comm.session.CabinetSession;
import cn.com.yusong.yhdg.cabinetserver.constant.RespCode;
import cn.com.yusong.yhdg.cabinetserver.service.basic.TerminalUpgradePackDetailService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.TerminalUpgradePackService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetBoxService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.protocol.msg08.*;
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
public class Biz081000008 extends AbstractBiz {

    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    TerminalUpgradePackService terminalUpgradePackService;
    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg081000008 request = (Msg081000008) message;
        Msg082000008 response = new Msg082000008();
        response.setSerial(request.getSerial());

        if (StringUtils.isEmpty(request.code)) {
            response.rtnCode = RespCode.CODE_2.getValue();
            writeAndFlush(context, response);
            return;
        }
        HeartParam heartParam = request.toParam();

        Cabinet cabinet = cabinetService.insertOrUpdate(request.code, request.version, heartParam.temp1, heartParam.temp2, heartParam.network.intValue(), heartParam.signal.intValue(), heartParam.boxList, heartParam.fireState.intValue(), heartParam.degree,null,null, null, null, null);
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

        byte fullVolume = Constant.FULL_VOLUME;
        if(StringUtils.isNotEmpty(cabinet.getId())) {
            if(cabinet != null && cabinet.getChargeFullVolume() != null) {
                fullVolume = cabinet.getChargeFullVolume().byteValue();
            }
        }

        response.cabinetType = cabinet.getSubtype().byteValue();
        response.activeFanTemp = cabinet.getActiveFanTemp().byteValue();
        response.fullVolume = fullVolume;
        response.power = cabinet.getMaxChargePower();
        response.boxMaxPower = cabinet.getBoxMaxPower();
        response.boxMinPower = cabinet.getBoxMinPower();
        response.trickleTime = cabinet.getBoxTrickleTime();
        response.reserved = 0;


        List<TerminalUpgradePack> upgradePackList = terminalUpgradePackService.findByOldVersion(TerminalUpgradePack.PackType.SUBCABINET_UPGRADE.getValue(), request.version);
        //默认无新版本
        response.hasNewVersion = (byte) 0;
        if(!upgradePackList.isEmpty()) {
            for(TerminalUpgradePack upgradePack : upgradePackList){
                TerminalUpgradePackDetail detail = terminalUpgradePackDetailService.find(upgradePack.getId(), cabinet.getId());
                if (detail != null) {
                    //服务器有新版本
                    response.hasNewVersion = (byte) 1;
                }
            }
        }
        //异步保存柜子心跳日志
        saveHeartLog(cabinet, heartParam, batteryList);

        writeAndFlush(context, response);

        openBox(context, openBoxList);
    }
}
