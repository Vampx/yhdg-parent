package cn.com.yusong.yhdg.cabinetserver.biz.server;

import cn.com.yusong.yhdg.cabinetserver.comm.session.CabinetSession;
import cn.com.yusong.yhdg.cabinetserver.constant.RespCode;
import cn.com.yusong.yhdg.cabinetserver.service.basic.ChargerUpgradePackDetailService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.ChargerUpgradePackService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.TerminalUpgradePackDetailService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.TerminalUpgradePackService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetBoxService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetChargerService;
import cn.com.yusong.yhdg.cabinetserver.service.hdg.CabinetService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.ChargerUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePack;
import cn.com.yusong.yhdg.common.domain.basic.TerminalUpgradePackDetail;
import cn.com.yusong.yhdg.common.domain.hdg.Battery;
import cn.com.yusong.yhdg.common.domain.hdg.Cabinet;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetBox;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCharger;
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
public class Biz081000006 extends AbstractBiz {

    @Autowired
    CabinetBoxService cabinetBoxService;
    @Autowired
    CabinetService cabinetService;
    @Autowired
    CabinetChargerService cabinetChargerService;
    @Autowired
    TerminalUpgradePackService terminalUpgradePackService;
    @Autowired
    TerminalUpgradePackDetailService terminalUpgradePackDetailService;
    @Autowired
    ChargerUpgradePackService chargerUpgradePackService;
    @Autowired
    ChargerUpgradePackDetailService chargerUpgradePackDetailService;

    @Override
    public void doBiz(ChannelHandlerContext context, Map<String, Object> attributes, Object message) throws Exception {
        Msg081000006 request = (Msg081000006) message;
        Msg082000006 response = new Msg082000006();
        response.setSerial(request.getSerial());

        if (request.checkCRC == null || request.checkCRC == false) {
            response.rtnCode = RespCode.CODE_4.getValue();
            writeAndFlush(context, response);
            return;
        }

        if (StringUtils.isEmpty(request.code)) {
            response.rtnCode = RespCode.CODE_2.getValue();
            writeAndFlush(context, response);
            return;
        }
        HeartParam heartParam = request.toParam();
        Cabinet cabinet = cabinetService.insertOrUpdate(request.code, request.version, heartParam.temp1, heartParam.temp2, heartParam.network.intValue(), heartParam.signal.intValue(), heartParam.boxList, null, heartParam.degree, heartParam.fanSpeed.intValue(),null, null, heartParam.peripheral.intValue(), heartParam.acVoltage);
        String cabinetId = cabinet.getId();

        //充电器不存在时，创建充电器
        List<CabinetCharger> cabinetChargerList =  cabinetChargerService.findOrInsert(cabinetId, heartParam.boxList);

        String serverAddress = config.getServerIp() + ":" + config.getServerPort();
        sessionManager.addCabinetSession(context, attributes, cabinetId, CabinetSession.NEW_CABINET_HERT);
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
        byte recoilVolume = Constant.FULL_VOLUME;
        if(cabinet.getChargeFullVolume() != null) {
            fullVolume = cabinet.getChargeFullVolume().byteValue();
        }
        if(cabinet.getRecoilVolume() != null) {
            recoilVolume = cabinet.getRecoilVolume().byteValue();
        }

        response.sn = cabinetId;
        response.operatorId = cabinet.getAgentId().shortValue();
        response.minExchangeSOC = cabinet.getMinExchangeVolume().byteValue();
        response.cabinetType = cabinet.getSubtype().byteValue();
        response.activeFanTemp =cabinet.getActiveFanTemp() * 10 + 2731;
        response.fullVolume = fullVolume;
        response.recoil = recoilVolume;
        response.power = cabinet.getMaxChargePower();
        response.boxMaxPower = cabinet.getBoxMaxPower();
        response.boxMinPower = cabinet.getBoxMinPower();
        response.trickleTime = cabinet.getBoxTrickleTime();
        response.chargeBoxNum = cabinet.getMaxChargeCount();
        response.enableWifi = cabinet.getEnableWifi().byteValue();//wifi
        response.enableBluetooth = cabinet.getEnableBluetooth().byteValue();//蓝牙
        response.enableVoice = cabinet.getEnableVoice().byteValue();//是否语言
        response.reserved = 0;

        List<String> chargerVersionList = new ArrayList<String>();

        //返回格口信息
        for(CabinetCharger cabinetCharger : cabinetChargerList){
            Msg082000006.Box box = new Msg082000006.Box();

            Integer boxNum = Integer.parseInt(cabinetCharger.getBoxNum());
            box.boxNum = boxNum.byteValue();
            box.enableCharge = cabinetCharger.getEnableCharge().byteValue();
            box.enableLink = cabinetCharger.getEnableLink().byteValue();
            box.autoSwtichMode = cabinetCharger.getAutoSwtichMode().byteValue();
            box.maxChargeVoltageOfStage1 = (short) (cabinetCharger.getMaxChargeVoltageOfStage1().byteValue() / 10);
            box.chargeCurrentOfStage1 = cabinetCharger.getChargeCurrentOfStage1().byteValue();
            box.maxChargeVoltageOfStage2 = (short) (cabinetCharger.getMaxChargeVoltageOfStage2().byteValue() / 10);
            box.chargeCurrentOfStage2 = cabinetCharger.getChargeCurrentOfStage2().byteValue();
            box.slopeVoltage = (short) (cabinetCharger.getSlopeVoltage().byteValue() / 10);
            box.chargeCurrentOfStage3 = cabinetCharger.getChargeCurrentOfStage3().byteValue();
            box.fullVoltage = (short) (cabinetCharger.getFullVoltage().byteValue() / 10);
            box.slopeCurrent = cabinetCharger.getSlopeCurrent().byteValue();
            box.minCurrentOfStage4 = cabinetCharger.getMinCurrentOfStage4().byteValue();
            box.lowTemperatureMode = cabinetCharger.getLowTemperatureMode().byteValue();
            box.other = cabinetCharger.getOther().byteValue();
            //格口是否禁用
            for (HeartParam.Box box1 : heartParam.boxList) {
                if (box1.boxNum == box.boxNum) {
                    box.boxForbidden = box1.boxForbidden.byteValue();
                }
            }

            if (!chargerVersionList.contains(cabinetCharger.getChargerVersion())) {
                chargerVersionList.add(cabinetCharger.getChargerVersion());
            }
            response.boxList.add(box);
        }

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

        //默认无新版本
        response.hasChargerNewVersion = (byte) 0;
        for (String chargerVersion : chargerVersionList) {
            List<ChargerUpgradePack> chargerUpgradePackList = chargerUpgradePackService.findByOldVersion(ChargerUpgradePack.PackType.BMS.getValue(), chargerVersion);
            if (!chargerUpgradePackList.isEmpty()) {
                for(ChargerUpgradePack upgradePack : chargerUpgradePackList){
                    ChargerUpgradePackDetail detail = chargerUpgradePackDetailService.find(upgradePack.getId(), cabinet.getId());
                    if (detail != null) {
                        //服务器有新版本
                        response.hasChargerNewVersion = (byte) 1;
                        break;
                    }
                }
            }
        }

        writeAndFlush(context, response);

        //异步保存柜子心跳日志
        saveHeartLog(cabinet, heartParam, batteryList);

        openBox(context, openBoxList);
    }
}
