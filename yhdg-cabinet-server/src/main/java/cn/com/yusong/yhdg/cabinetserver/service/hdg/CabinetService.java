package cn.com.yusong.yhdg.cabinetserver.service.hdg;

import cn.com.yusong.yhdg.cabinetserver.biz.server.Task;
import cn.com.yusong.yhdg.cabinetserver.comm.session.CabinetSession;
import cn.com.yusong.yhdg.cabinetserver.comm.session.SessionManager;
import cn.com.yusong.yhdg.cabinetserver.config.AppConfig;
import cn.com.yusong.yhdg.cabinetserver.persistence.basic.*;
import cn.com.yusong.yhdg.cabinetserver.persistence.hdg.*;
import cn.com.yusong.yhdg.cabinetserver.persistence.zc.ShopStoreVehicleBatteryMapper;
import cn.com.yusong.yhdg.cabinetserver.service.AbstractService;
import cn.com.yusong.yhdg.cabinetserver.service.basic.OrderIdService;
import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.*;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.Kv;
import cn.com.yusong.yhdg.common.entity.NewBoxNum;
import cn.com.yusong.yhdg.common.protocol.msg08.HeartParam;
import cn.com.yusong.yhdg.common.protocol.msg23.Msg231000001;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.utils.OkHttpClientUtils;
import cn.com.yusong.yhdg.common.utils.YhdgUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CabinetService extends AbstractService {

    private static final Logger log = LogManager.getLogger(CabinetService.class);

    @Autowired
    MemCachedClient memcachedClient;
    @Autowired
    CabinetDayDegreeStatsMapper cabinetDayDegreeStatsMapper;
    @Autowired
    AppConfig config;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CabinetCodeMapper cabinetCodeMapper;
    @Autowired
    SystemConfigMapper systemConfigMapper;
    @Autowired
    CabinetBoxMapper cabinetBoxMapper;
    @Autowired
    BatteryMapper batteryMapper;
    @Autowired
    KeepOrderMapper keepOrderMapper;
    @Autowired
    KeepPutOrderMapper keepPutOrderMapper;
    @Autowired
    KeepTakeOrderMapper keepTakeOrderMapper;
    @Autowired
    BatteryOrderMapper batteryOrderMapper;
    @Autowired
    BatteryUtilizeMapper batteryUtilizeMapper;
    @Autowired
    BatteryOperateLogMapper batteryOperateLogMapper;
    @Autowired
    BackBatteryOrderMapper backBatteryOrderMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CabinetOnlineStatsMapper cabinetOnlineStatsMapper;
    @Autowired
    PacketPeriodOrderMapper packetPeriodOrderMapper;
    @Autowired
    ExchangePriceTimeMapper exchangePriceTimeMapper;
    @Autowired
    CabinetOperateLogMapper cabinetOperateLogMapper;
    @Autowired
    PushMetaDataMapper pushMetaDataMapper;
    @Autowired
    BatteryChargeRecordMapper batteryChargeRecordMapper;
    @Autowired
    FaultLogMapper faultLogMapper;
    @Autowired
    BatterySequenceMapper batterySequenceMapper;
    @Autowired
    OrderIdService orderIdService;
    @Autowired
    BatteryReportLogMapper batteryReportLogMapper;
    @Autowired
    BatteryReportDateMapper batteryReportDateMapper;
    @Autowired
    BatteryChargeRecordDetailMapper batteryChargeRecordDetailMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    PushOrderMessageMapper pushOrderMessageMapper;
    @Autowired
    CustomerExchangeBatteryMapper customerExchangeBatteryMapper;
    @Autowired
    ExchangeWhiteListMapper exchangeWhiteListMapper;
    @Autowired
    CustomerExchangeInfoMapper customerExchangeInfoMapper;
    @Autowired
    BatteryReportMapper batteryReportMapper;
    @Autowired
    BatteryParameterMapper batteryParameterMapper;
    @Autowired
    BatteryParameterLogMapper batteryParameterLogMapper;
    @Autowired
    LaxinCustomerMapper laxinCustomerMapper;
    @Autowired
    LaxinRecordMapper laxinRecordMapper;
    @Autowired
    BespeakOrderMapper bespeakOrderMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    CabinetBatteryStatsMapper cabinetBatteryStatsMapper;
    @Autowired
    CabinetReportMapper cabinetReportMapper;
    @Autowired
    CabinetReportDateMapper cabinetReportDateMapper;
    @Autowired
    CabinetReportBatteryMapper cabinetReportBatteryMapper;
    @Autowired
    CabinetReportBatteryDateMapper cabinetReportBatteryDateMapper;
    @Autowired
    ShopStoreVehicleBatteryMapper shopStoreVehicleBatteryMapper;
    @Autowired
    SessionManager sessionManager;

    public String findMaxId() {
        String date = DateFormatUtils.format(new Date(), "yyyyMMdd");
        String id = cabinetMapper.findMaxId(date);
        if (StringUtils.isEmpty(id)) {
            id = String.format("%s%0" + Constant.CABINET_ID_SEQUENCE_LENGTH + "d", date, 1);
        } else {
            id = String.valueOf(Long.parseLong(id) + 1);
        }
        return id;
    }

    private String nextBatteryId() {
        BatterySequence entity = new BatterySequence();
        batterySequenceMapper.insert(entity);
        String result = "Z" + StringUtils.leftPad(Integer.toString(entity.getId(), 35), 7, '0').toUpperCase();
        return result;
    }

    private FaultLog insertFaultLog(Integer faultLevel, String orderId, Integer provinceId, Integer cityId, Integer districtId, Long dispatcherId, Integer agentId, String agentName, String batteryId, String cabinetId, String cabinetName,
                                    String cabinetAddress, String boxNum, Integer faultType, String faultContent) {
        FaultLog data = new FaultLog();
        data.setFaultLevel(faultLevel);
        data.setOrderId(orderId);
        data.setProvinceId(provinceId);
        data.setCityId(cityId);
        data.setDistrictId(districtId);
        data.setDispatcherId(dispatcherId);
        data.setAgentId(agentId);
        data.setAgentName(agentName);
        data.setBatteryId(batteryId);
        data.setCabinetId(cabinetId);
        data.setCabinetName(cabinetName);
        data.setCabinetAddress(cabinetAddress);
        data.setBoxNum(boxNum);
        data.setFaultType(faultType);
        data.setFaultContent(faultContent);
        data.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        data.setCreateTime(new Date());
        faultLogMapper.insert(data);
        return data;
    }

    public Cabinet find(String id) {
        return cabinetMapper.find(id);
    }

    public Cabinet insertOrUpdate(String code, String version, Integer temp1, Integer temp2, Integer networkType, Integer currentSignal, List<HeartParam.Box> boxList, Integer isFpOpen, Integer degree, Integer fanSpeed, Integer waterLevel, Integer smokeState, Integer peripheral, Integer acVoltage) {
        //获取机器码(code)对应的12位编号 如果不存在就新建一个
        CabinetCode cabinetCode = cabinetCodeMapper.find(code);
        if (cabinetCode == null) {
            cabinetCode = new CabinetCode();
            cabinetCode.setId(code);
            cabinetCode.setCode(findMaxId());
            cabinetCodeMapper.insert(cabinetCode);
        }

        String id = cabinetCode.getCode();
        //查询12位编号对应的设备 如果不存在就新建一个code=mac
        Cabinet cabinet = cabinetMapper.find(id);
        if (cabinet == null) {
            cabinet = new Cabinet();
            cabinet.setId(id);
            cabinet.setCabinetName(id);
            cabinet.setMac(code);
            cabinet.setVersion(version);
            cabinet.setSubtype(Cabinet.Subtype.EXCHANGE.getValue());//换电
            cabinet.setAgentId(Integer.parseInt(findConfigValue(ConstEnum.SystemConfigKey.TEST_AGENT.getValue())));
            cabinet.setHeartTime(new Date());
            cabinet.setIsOnline(ConstEnum.Flag.TRUE.getValue());////在线
            cabinet.setActiveStatus(ConstEnum.Flag.TRUE.getValue());
            cabinet.setCreateTime(new Date());
            cabinet.setFaultType(Cabinet.FaultType.NORMAL.getValue());//正常
            cabinet.setMaxChargeCount(Constant.MAX_CHARGE_COUNT);
            cabinet.setActiveFanTemp(Constant.ACTIVE_FAN_TEMP);
            cabinet.setCurrentSignal(currentSignal);
            cabinet.setMaxChargePower(Constant.MAX_CHARGE_POWER);
            cabinet.setBoxMaxPower(Constant.BOX_MAX_POWER);
            cabinet.setBoxMinPower(Constant.BOX_MIN_POWER);
            cabinet.setBoxTrickleTime(90);
            cabinet.setEnableWifi(Constant.ENABLE_WIFI);
            cabinet.setEnableBluetooth(Constant.ENABLE_BLUETOOTH);
            cabinet.setEnableVoice(Constant.ENABLE_VOICE);
            cabinet.setIsFpOpen(isFpOpen);/*灭火器是否打开 1开启 0 关闭*/
            cabinet.setFanSpeed(fanSpeed);
            cabinet.setWaterLevel(waterLevel);
            cabinet.setSmokeState(smokeState);

            //新协议外设判断
            if(peripheral != null){
                int[] peripheralStats = Cabinet.parsePeripheral(peripheral);
                cabinet.setIsFpOpen(peripheralStats[0]);/*灭火器是否打开 1开启 0 关闭*/
                cabinet.setWaterLevel(peripheralStats[1]);/*水位状态 0正常 1超水位 */
                cabinet.setSmokeState(peripheralStats[2]);/*柜子烟雾传感器状态 0正常 1报警*/
                cabinet.setAcVoltageState(peripheralStats[3]);/*交流电输入状态 0:正常 1:断电*/
            }
            cabinet.setAcVoltage(acVoltage);

            cabinet.setAllFullCount(ConstEnum.Flag.FALSE.getValue());
            if (networkType != null) {
                cabinet.setNetworkType(networkType);
            } else {
                cabinet.setNetworkType(Cabinet.NetworkType.NETWORK_0.getValue());
            }
            cabinet.setChargeFullVolume(Constant.FULL_VOLUME);//满电电量
            cabinet.setRecoilVolume(Constant.FULL_VOLUME);//满电电量
            cabinet.setPermitExchangeVolume(Constant.PERMIT_EXCHANGE_VOLUME);//可换电电量
            cabinet.setUpLineStatus(Cabinet.UpLineStatus.NOT_ONLINE.getValue());//未上线
            cabinet.setForegiftMoney(ConstEnum.Flag.FALSE.getValue());
            cabinet.setRentMoney(ConstEnum.Flag.FALSE.getValue());
            cabinet.setRentPeriodType(0);
            cabinet.setPrice(0.0);//电价
            cabinet.setViewType(Cabinet.ViewType.SHARED.getValue());

            cabinetMapper.insert(cabinet);

            for (int i = 0; i < boxList.size(); i++) {
                HeartParam.Box e = boxList.get(i);

                String boxNum = String.format("%02d", e.boxNum);
                CabinetBox cabinetBox = new CabinetBox();
                cabinetBox.setAgentId(cabinet.getAgentId());
                cabinetBox.setCabinetId(cabinet.getId());
                cabinetBox.setBoxNum(boxNum);
                cabinetBox.setType(CabinetBox.TYPE_NOT_SUPPORT_CHARGE); //不支持充电
                cabinetBox.setSubtype(cabinet.getSubtype());
                cabinetBox.setIsActive(ConstEnum.Flag.TRUE.getValue());
                cabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
                cabinetBox.setIsOpen(0); //初始设置箱门未打开
                cabinetBox.setIsOnline(ConstEnum.Flag.TRUE.getValue());
                cabinetBox.setOpenTime(null);
                cabinetBox.setChargeFullVolume(Constant.FULL_VOLUME);
                cabinetBoxMapper.insert(cabinetBox);
            }

        } else {
            //处理分柜离线异常
            if (cabinet.getOfflineFaultLogId() != null) {
                faultLogMapper.handle(cabinet.getOfflineFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, new Date(), null, FaultLog.Status.PROCESSED.getValue());
            }
            //处理分柜温度异常
            if (cabinet.getTempFaultLogId() != null) {
                boolean fault = false;
                if(temp1 != null && (temp1 <= config.getCabinetLowAlarmTemp() ||  temp1 >= config.getCabinetHotAlarmTemp())){
                    fault = true;
                }
                if(temp2 != null && (temp2 <= config.getCabinetLowAlarmTemp() ||  temp2 >= config.getCabinetHotAlarmTemp())){
                    fault = true;
                }
                if(!fault){
                    faultLogMapper.handle(cabinet.getTempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, new Date(), null, FaultLog.Status.PROCESSED.getValue());
                    cabinet.setTempFaultLogId(null);
                }
            } else {
                if ((temp1 != null && temp1 >= config.getCabinetHotAlarmTemp()) || (temp2 != null && temp2 >= config.getCabinetHotAlarmTemp())) {
                    if (cabinet != null) {
                        AgentInfo agentInfo = findAgentInfo(cabinet.getAgentId());
                        FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(),
                                null,
                                cabinet != null ? cabinet.getProvinceId() : null,
                                cabinet != null ? cabinet.getCityId() : null,
                                cabinet != null ? cabinet.getDistrictId() : null,
                                cabinet != null ? cabinet.getDispatcherId() : null,
                                cabinet.getAgentId(),
                                agentInfo.getAgentName(),
                                null,
                                cabinet != null ? cabinet.getId() : null,
                                cabinet != null ? cabinet.getCabinetName() : null,
                                cabinet != null ? cabinet.getAddress() : null,
                                null,
                                FaultLog.FaultType.CODE_22.getValue(),
                                String.format("当前温度: %s", Math.max(temp1 == null ? 0 : temp1, temp2 == null ? 0 : temp2)));
                        cabinet.setTempFaultLogId(faultLog.getId());

                        PushMetaData metaData = new PushMetaData();
                        metaData.setSourceType(PushMessage.SourceType.CABINET_HIGH_TEMP.getValue());
                        metaData.setSourceId(cabinet.getId());
                        metaData.setCreateTime(new Date());
                        pushMetaDataMapper.insert(metaData);
                    }
                }else if((temp1 != null && temp1 <= config.getCabinetLowAlarmTemp()) || (temp2 != null && temp2 <=  config.getCabinetLowAlarmTemp())){
                    if (cabinet != null) {
                        AgentInfo agentInfo = findAgentInfo(cabinet.getAgentId());
                        FaultLog faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(),
                                null,
                                cabinet != null ? cabinet.getProvinceId() : null,
                                cabinet != null ? cabinet.getCityId() : null,
                                cabinet != null ? cabinet.getDistrictId() : null,
                                cabinet != null ? cabinet.getDispatcherId() : null,
                                cabinet.getAgentId(),
                                agentInfo.getAgentName(),
                                null,
                                cabinet != null ? cabinet.getId() : null,
                                cabinet != null ? cabinet.getCabinetName() : null,
                                cabinet != null ? cabinet.getAddress() : null,
                                null,
                                FaultLog.FaultType.CODE_23.getValue(),
                                String.format("当前温度: %s", Math.min(temp1 == null ? 0 : temp1, temp2 == null ? 0 : temp2)));
                        cabinet.setTempFaultLogId(faultLog.getId());

                        PushMetaData metaData = new PushMetaData();
                        metaData.setSourceType(PushMessage.SourceType.CABINET_LOW_TEMP.getValue());
                        metaData.setSourceId(cabinet.getId());
                        metaData.setCreateTime(new Date());
                        pushMetaDataMapper.insert(metaData);
                    }
                }
            }

            //新协议外设判断
            Integer acVoltageState = null;
            if(peripheral != null){
                int[] peripheralStats = Cabinet.parsePeripheral(peripheral);
                isFpOpen = peripheralStats[0];
                waterLevel = peripheralStats[1];
                smokeState = peripheralStats[2];
                acVoltageState = peripheralStats[3];
            }

            cabinetMapper.updateHeart(cabinet.getId(), version, code, ConstEnum.Flag.TRUE.getValue(), new Date(), temp1, temp2, networkType, currentSignal, cabinet.getTempFaultLogId(), isFpOpen, fanSpeed, waterLevel, smokeState, acVoltageState, acVoltage);

        }
        //处理用电度数
        if (degree != null && degree > 0) {
            Date now = new Date();
            String today = DateFormatUtils.format(now, Constant.DATE_FORMAT);
            CabinetDayDegreeStats dayElectricStats = cabinetDayDegreeStatsMapper.find(cabinet.getId(), today);
            if (dayElectricStats == null) {
                CabinetDayDegreeStats stats = new CabinetDayDegreeStats();
                stats.setCabinetId(cabinet.getId());
                stats.setCabinetName(cabinet.getCabinetName());
                stats.setAgentId(cabinet.getAgentId());
                stats.setAgentName(cabinet.getAgentName());
                stats.setStatsDate(today);
                stats.setBeginTime(now);
                stats.setEndTime(now);
                stats.setBeginNum(degree);
                stats.setEndNum(degree);
                stats.setNum(0);

                CabinetDayDegreeStats before = cabinetDayDegreeStatsMapper.findBefore(cabinet.getId(), today);
                if (before != null && degree > before.getEndNum()) {
                    stats.setBeginNum(before.getEndNum());
                    stats.setEndNum(before.getEndNum());

                    if (degree > before.getEndNum()) {
                        stats.setEndNum(degree);
                    }
                    stats.setNum(stats.getEndNum() - stats.getBeginNum());
                }
                cabinetDayDegreeStatsMapper.insert(stats);
            } else {
                //防止当前上报度数大于上次上报度数
                if (dayElectricStats.getBeginNum() > degree) {
                    if (log.isDebugEnabled()) {
                        log.warn("设备{}开始度数{}, 上报度数{}, 上报度数小于开始度数, 重置电表开始度数为上报度数", cabinet.getId(), dayElectricStats.getBeginNum(), degree);
                    }

                    cabinetDayDegreeStatsMapper.updateBegin(cabinet.getId(), today, now, degree, degree, 0);
                } else if ((dayElectricStats.getBeginNum() + 1000 * 100) > degree) {//如果上报度数比begin_num大0-1000度,正常更新end_num
                    cabinetDayDegreeStatsMapper.updateEnd(cabinet.getId(), today, now, degree, degree - dayElectricStats.getBeginNum());
                }
            }
        }
        return cabinet;
    }

    public void heart(HeartParam heartParam, String cabinetId, List<Integer> openBoxList, List<Battery> batteryList) {
        Cabinet cabinet = cabinetMapper.find(cabinetId);
        Date now = new Date();
        //service控制在线结束时间3分钟未上报设置离线
        CabinetOnlineStats stats = cabinetOnlineStatsMapper.findMaxRecord(cabinet.getId());
        if (stats == null || stats.getEndTime() != null) {
            stats = new CabinetOnlineStats();
            stats.setCabinetId(cabinet.getId());
            stats.setBeginTime(new Date());
            cabinetOnlineStatsMapper.insert(stats);
        }

        //心跳是否是重连第一次上报
        boolean reconnect = false;
        CabinetSession session = sessionManager.getCabinetSession(cabinet.getId());
        if(session != null){
            reconnect = session.reconnect;

            if(session.reconnect == true){
                log.debug("换电柜重连, 柜子{}", cabinet.getId());
            }
        }

        int power = 0, operationFlag = 0, batteryNum = 0, chargeBatteryNum = 0;
        String batteryMessage = "",boxMessage = "";
        //注意这里调用.size()不能优化
        for (int i = 0; i < heartParam.boxList.size(); i++) {
            HeartParam.Box e = heartParam.boxList.get(i);

            String boxNum = String.format("%02d", e.boxNum);
            CabinetBox cabinetBox = cabinetBoxMapper.find(cabinet.getId(), boxNum);

            Battery battery = null;
            //兼容老版本
            if (StringUtils.isNotEmpty(e.batteryId)) {
                battery = batteryMapper.find(e.batteryId);
                if (battery == null) { //电池未注册
                    log.error("电池{}未注册", e.batteryId);
                }
            } else if (StringUtils.isNotEmpty(e.batteryCode)) {
                battery = batteryMapper.findByCode(e.batteryCode);
                if (battery == null) {
                    battery = createBattery(e.batteryCode, cabinet.getAgentId(), e.voltage, e.electricity, e.volume, e.protectState, e.fet, Battery.ChargeStatus.NOT_CHARGE.getValue());
                } else {
                    if(StringUtils.isNotEmpty(e.batteryVersion)){
                        battery.setVersion(e.batteryVersion);
                    }
                    battery.setVoltage(e.voltage);
                    battery.setElectricity(e.electricity);
                    battery.setCurrentCapacity(e.restCapacity);
                    battery.setVolume(e.volume.intValue());
                    battery.setCircle(e.circle);
                    battery.setTotalCapacity(e.ratedCapacity);
                    if(e.temp != null){
                        battery.setTemp(e.temp);
                    }else{
                        battery.setTemp(String.format("%s,%s",e.batteryTemp1,e.batteryTemp2));
                    }
                    battery.setStrand(e.serials);
                    battery.setSingleVoltage(e.singleVoltage);
                    battery.setProtectState(e.protectState);
                    battery.setFet(e.fet.intValue());
                    battery.setLinkStatus(e.linkStatus);
                    battery.setIsOnline(ConstEnum.Flag.TRUE.getValue());
                    battery.setReportTime(now);
                    battery.setLat(cabinet.getLat());
                    battery.setLng(cabinet.getLng());
                    battery.setGpsUpdateTime(new Date());
                }
                e.batteryId = battery.getId();
            }

            if (cabinetBox == null) {
                cabinetBox = new CabinetBox();
                cabinetBox.setAgentId(cabinet.getAgentId());
                cabinetBox.setCabinetId(cabinet.getId());
                cabinetBox.setBoxNum(boxNum);
                cabinetBox.setType(CabinetBox.TYPE_NOT_SUPPORT_CHARGE); //不支持充电
                cabinetBox.setSubtype(cabinet.getSubtype());
                cabinetBox.setIsActive(ConstEnum.Flag.TRUE.getValue());
                cabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
                cabinetBox.setIsOpen(0); //初始设置箱门未打开
                cabinetBox.setIsOnline(ConstEnum.Flag.TRUE.getValue());
                cabinetBox.setOpenTime(null);
                cabinetBox.setChargeFullVolume(Constant.FULL_VOLUME);
                cabinetBoxMapper.insert(cabinetBox);
            } else {
                if (cabinetBox.getIsOnline() == ConstEnum.Flag.FALSE.getValue()) {
                    cabinetBoxMapper.updateOnline(cabinet.getId(), ConstEnum.Flag.TRUE.getValue());
                }
            }
            //格口是否启用保存到心跳参数：格口是否禁用
            if (cabinetBox.getIsActive() != null) {
                if (cabinetBox.getIsActive() == ConstEnum.Flag.TRUE.getValue()) {
                    e.boxForbidden = ConstEnum.Flag.FALSE.getValue();
                } else if (cabinetBox.getIsActive() == ConstEnum.Flag.FALSE.getValue()) {
                    e.boxForbidden = ConstEnum.Flag.TRUE.getValue();
                }
            }

            //记录电池数
            if( battery != null){
                batteryMessage += String.format("%s(%s)/%s;",boxNum,e.isClosed == 0?"开":"关",battery.getId());
                batteryNum ++;
            }

            if (battery != null && StringUtils.isNotEmpty(cabinetBox.getBatteryId()) && !battery.getId().equals(cabinetBox.getBatteryId())) {
                log.debug("上报电池与箱中电池不一致, 柜子{} 箱号{} 上报{}, 箱中{}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), cabinetBox.getBatteryId());
                //先取出原来的电池
                //再放入新的电池
                //箱门关闭 心跳bit0:门状态0开/1关
                if (e.isClosed == 1) {
                    //防止格口取出电池  空箱状态下未上报心跳   模拟一次业务
                    HeartParam.Box box = new HeartParam.Box();
                    box.boxNum = e.boxNum;
                    box.isClosed = 1;
                    box.boxState = 0;
                    box.batteryId = e.batteryId;
                    box.voltage = e.voltage;
                    box.electricity = e.electricity;
                    box.volume = e.volume;
                    box.protectState = e.protectState;
                    box.fet = e.fet;
                    box.chargeStatus = e.chargeStatus;
                    box.power = e.power;
                    box.batteryTemp1 = e.batteryTemp1;
                    box.batteryTemp2 = e.batteryTemp2;
                    box.restCapacity = e.restCapacity;

                    heartParam.boxList.add(box);

                    e.batteryId = null;
                    battery = null;
                }
            }

            List<Kv> cabinetBoxKvList = new ArrayList<Kv>();
            if(e.power != null ){
                //排查功率为空或为0的情况
                if(cabinetBox.getPower() == null || (cabinetBox.getPower() == 0 && e.power != 0)  || (cabinetBox.getPower() != 0 && e.power == 0)){
                    cabinetBoxKvList.add(new Kv("power", e.power));
                }else{
                    //功率相差50以上才做更新
                    if( Math.abs(cabinetBox.getPower() - e.power) > 50){
                        cabinetBoxKvList.add(new Kv("power", e.power));
                    }
                }
            }
//            if(e.boxVersion != null && (cabinetBox.getBoxVersion()  == null || cabinetBox.getBoxVersion() != e.boxVersion.intValue())){
//                cabinetBoxKvList.add(new Kv("box_version", e.boxVersion));
//            }
//            if(e.boxTemp != null && (cabinetBox.getBoxTemp() == null || Math.abs(cabinetBox.getBoxTemp() - e.boxTemp.intValue()) > 5)){
//                cabinetBoxKvList.add(new Kv("box_temp", e.boxTemp));
//            }
//            if(e.fanSpeed != null && (cabinetBox.getFanSpeed() == null || cabinetBox.getFanSpeed() != e.fanSpeed.intValue())){
//                cabinetBoxKvList.add(new Kv("fan_speed", e.fanSpeed));
//            }
            if (cabinetBox.getBoxState() == null || cabinetBox.getBoxState() != e.boxState) {
                cabinetBoxKvList.add(new Kv("box_state", e.boxState));
            }

            power += e.power == null ? 0 : e.power;
            if (!cabinetBox.getSubtype().equals(cabinet.getSubtype())) {
                cabinetBoxKvList.add(new Kv("subtype", cabinet.getSubtype()));
            }
            if (cabinetBox.getIsOnline() == ConstEnum.Flag.FALSE.getValue()) {
                cabinetBoxKvList.add(new Kv("is_online", ConstEnum.Flag.TRUE.getValue()));
            }

            int oldIsOpen = cabinetBox.getIsOpen();

            //处理箱门的打开与关闭
            if (e.isClosed == 0) { //箱门打开 心跳bit0:门状态0开/1关
                if (cabinetBox.getIsOpen() == ConstEnum.Flag.FALSE.getValue()) {
                    //箱体状态 如果是关闭状态则更新成打开状态
                    cabinetBox.setIsOpen(ConstEnum.Flag.TRUE.getValue());
                    cabinetBox.setOpenTime(now);

                    cabinetBoxKvList.add(new Kv("is_open", ConstEnum.Flag.TRUE.getValue()));
                    cabinetBoxKvList.add(new Kv("open_time", now));

                    if (log.isDebugEnabled()) {
                        log.debug("柜子{} 箱号{} 开门", cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                    }

                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());
                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(String.format("%02d", e.boxNum));
                    operateLog.setOperateType(CabinetOperateLog.OperateType.OPEN_DOOR.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
                    operateLog.setContent("开门");
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogMapper.insert(operateLog);
                    //为了防止
                    if (operationFlag == 0) {
                        cabinetMapper.updateOperationFlag(cabinetBox.getCabinetId(), ConstEnum.Flag.TRUE.getValue());
                        operationFlag = 1;
                    }

                    //删除电池通讯缓存
                    String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_BATTERY, cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                    String batteryId = (String) memcachedClient.get(cacheKey);
                    if(StringUtils.isNotEmpty(batteryId) ){
                        memcachedClient.delete(cacheKey);
                    }

                } else if (cabinetBox.getOpenTime() == null) {
                    //箱体状态 如果是打开状态 并且打开时间为空 则更新打开时间
                    cabinetBox.setOpenTime(now);

                    cabinetBoxKvList.add(new Kv("open_time", now));
                }

                //如果箱门一直打开着，并且之前有骑手取过电池，而且骑手取电后没有过关箱门操作，那么判断骑手箱门未关。给出推送
                if (oldIsOpen == ConstEnum.Flag.TRUE.getValue()) {
                    String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_BATTERY_ORDER, cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                    BatteryOrder batteryOrder = (BatteryOrder) memcachedClient.get(cacheKey);
                    if(batteryOrder != null) {
                        //如果电池被用户取出大于5分钟，箱门还没关，给出通知，删除缓存
                        if(DateUtils.addMinutes(batteryOrder.getTakeTime(), 5 ).compareTo(new Date()) < 0 ){
                            AgentInfo agentInfo = findAgentInfo(cabinet.getAgentId());
                            insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(),
                                batteryOrder.getId(),
                                cabinet.getProvinceId(),
                                cabinet.getCityId(),
                                cabinet.getDistrictId(),
                                cabinet.getDispatcherId(),
                                cabinet.getAgentId(),
                                agentInfo.getAgentName(),
                                null,
                                cabinet.getId(),
                                cabinet.getCabinetName(),
                                cabinet.getAddress(),
                                boxNum,
                                FaultLog.FaultType.CODE_24.getValue(),
                                String.format("骑手%s取电池后未关门",batteryOrder.getCustomerFullname()));

                            PushMetaData pushMetaData = new PushMetaData();
                            pushMetaData.setSourceId(batteryOrder.getId());
                            pushMetaData.setSourceType(PushMessage.SourceType.NO_CLOSE_BOX.getValue());
                            pushMetaData.setCreateTime(new Date());
                            pushMetaDataMapper.insert(pushMetaData);

                            //推送运营商
                            pushMetaData = new PushMetaData();
                            pushMetaData.setSourceId(batteryOrder.getId());
                            pushMetaData.setSourceType(PushMessage.SourceType.NO_CLOSE_BOX_NOTICE_AGENT.getValue());
                            pushMetaData.setCreateTime(new Date());
                            pushMetaDataMapper.insert(pushMetaData);

                            memcachedClient.delete(cacheKey);
                            log.debug("already push delete cache K_CABINET_BOX_V_BATTERY_ORDER, cabinetId={}, boxNum={}, orderId={}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), batteryOrder.getId());
                        }

                    }
                }

            } else { //箱门关闭 心跳bit0:门状态0开/1关
                if (cabinetBox.getIsOpen() == ConstEnum.Flag.TRUE.getValue()) {
                    //箱体状态 如果是打开状态则更新成关闭状态
                    cabinetBox.setIsOpen(ConstEnum.Flag.FALSE.getValue());
                    //设置打开时间为空
                    cabinetBox.setOpenTime(null);

                    cabinetBoxKvList.add(new Kv("is_open", ConstEnum.Flag.FALSE.getValue()));
                    cabinetBoxKvList.add(new Kv("open_time", null));

                    if (log.isDebugEnabled()) {
                        log.debug("柜子{} 箱号{} 关闭", cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                    }

                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());
                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(String.format("%02d", e.boxNum));
                    operateLog.setOperateType(CabinetOperateLog.OperateType.CLOSE_DOOR.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
                    operateLog.setContent("关门");
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogMapper.insert(operateLog);
                    //更新主柜有无操作(一个主柜屏幕操作多个分柜) 轮询是否需要更新列表协议(防止浪费流量)
                    if (operationFlag == 0) {
                        cabinetMapper.updateOperationFlag(cabinetBox.getCabinetId(), ConstEnum.Flag.TRUE.getValue());
                        operationFlag = 1;
                    }

                    //箱门关闭后，删除客户取新电进缓存。缓存的作用是为了记录客户取新电后没关箱门
                    String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_BATTERY_ORDER, cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                    BatteryOrder batteryOrder = (BatteryOrder) memcachedClient.get(cacheKey);
                    if(batteryOrder != null) {
                        memcachedClient.delete(cacheKey);
                        log.debug("delete cache K_CABINET_BOX_V_BATTERY_ORDER, cabinetId={}, boxNum={}, orderId={}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), batteryOrder.getId());
                    }
                }
            }

            if (e.isClosed == 0 && battery != null && StringUtils.isEmpty(cabinetBox.getBatteryId())) { //说明电池之前不在柜子中 现在电池在格子中 但门没关
                if (log.isDebugEnabled()) {
                    log.debug("柜子{}箱号{} 检测到电池(未关门) {} 状态 {}, {}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                }
            }

            //如果箱门关闭，并且箱门中有电池。加入缓存
            if (e.isClosed == 1 && battery != null){
                //解锁箱门
                if(cabinetBox.getIsActive() == ConstEnum.Flag.FALSE.getValue()
                        && StringUtils.isNotEmpty(cabinetBox.getForbiddenCause()) && cabinetBox.getForbiddenCause().contains("系统检测:电池通讯异常") ){
                    cabinetBoxKvList.add(new Kv("is_active", ConstEnum.Flag.TRUE.getValue()));
                    cabinetBoxKvList.add(new Kv("forbidden_cause", null));

                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());
                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(String.format("%02d", e.boxNum));
                    operateLog.setOperateType(CabinetOperateLog.OperateType.ACTIVE.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.PLATFORM.getValue());
                    operateLog.setContent("箱门激活成功（心跳）");
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogMapper.insert(operateLog);
                }
                //加入柜子电池缓存
                String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_BATTERY, cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                memcachedClient.set(cacheKey, battery.getId(), MemCachedConfig.CACHE_FIVE_MINUTE);
            }

            //柜子重连，如果箱门关闭，并且箱门中有电池，并且电池为绑定客户。防止误开箱门，自动结束该订单
            if (e.isClosed == 1 && battery != null && reconnect) {
                if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue() || battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue() || battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {

                    cabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
                    Kv boxStatusKv = new Kv("box_status", cabinetBox.getBoxStatus());
                    cabinetBoxKvList.add(boxStatusKv);

                    reconnectPut(cabinet, battery, boxNum);
                }
            }

            //说明电池一直在箱中 没有取出放入操作(防止误开门，柜子重连的首次心跳不走该操作)
            if (e.isClosed == 1 && battery != null && StringUtils.isNotEmpty(cabinetBox.getBatteryId()) && battery.getId().equals(cabinetBox.getBatteryId()) && !reconnect) {
                if (battery.getStatus() == Battery.Status.NOT_USE.getValue()) {
                    if (log.isDebugEnabled()) {
                        log.debug("无关联订单放入(电池已在柜子里 但是电池状态是未使用) 订单id{} 柜子{} 箱号{} 电池 {} 状态 {}, {}", battery.getOrderId(), cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                    }

                    cabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
                    cabinetBoxKvList.add(new Kv("box_status", cabinetBox.getBoxStatus()));
                    if(battery.getInBoxTime() == null || battery.getCabinetId() == null || battery.getStatus() != Battery.Status.IN_BOX.getValue()){
                        batteryMapper.updateInBox(battery.getId(), now, cabinet.getId(), cabinet.getCabinetName(), boxNum, Battery.Status.IN_BOX.getValue(), Battery.ChargeStatus.NOT_CHARGE.getValue(), null, null); //设置电池的放入时间
                    }

                } else if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) { // 箱门关闭状态下 客户使用未取出的电池 发送开箱指令
                    //收集要开箱的箱号
                    openBoxList.add(Integer.valueOf(e.boxNum));
                } else if (battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {
                    boolean open = false;

                    //非本人电池校验
                    String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_CUSTMOER_ID, cabinet.getId(), boxNum);
                    Long putCustomerId = (Long) memcachedClient.get(cacheKey);
                    if (putCustomerId != null && battery.getCustomerId() != null &&  battery.getCustomerId().intValue() != putCustomerId) {
                        //收集要开箱的箱号
                        open = true;
                        openBoxList.add(Integer.parseInt(boxNum, 10));
                        if (log.isDebugEnabled()) {
                            log.debug("重开箱门 电池推入检测, 非本人电池, batteryId: {}, cabinetId: {}, boxNum: {}", battery.getId(), cabinet.getId(), boxNum);
                        }
                    }

                    // 电池当前电量>柜子可换进电量 放电量不满足最低换电电量要重新开箱 发送开箱指令
//                    if (battery.getVolume() > cabinet.getPermitExchangeVolume()) {
//                        //收集要开箱的箱号
//                        open = true;
//                        openBoxList.add(Integer.parseInt(boxNum, 10));
//                        if (log.isDebugEnabled()) {
//                            log.debug("重开箱门 电池在箱中检测, 电池当前电量大于柜子可换进电量, batteryId: {}, cabinetId: {}, boxNum: {}", battery.getId(), cabinet.getId(), boxNum);
//                        }
//                    }

                    //没有满电电池在格口中(正常流程不扫码 有打开的格口客户直接放进去)要重新开箱  重点：必须是箱门打开的情况下关闭箱门才会弹开
                    if (!open && oldIsOpen == ConstEnum.Flag.TRUE.getValue()) {
                        //查询是否有预约订单
                        String bespeakBoxNum = null;
                        BespeakOrder bespeakOrder = bespeakOrderMapper.findSuccessByCustomer(battery.getCustomerId(), BespeakOrder.Status.SUCCESS.getValue());
                        if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(cabinet.getId())){
                            bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
                        }

                        String checkBatteryId = null;
                        if (putCustomerId != null  ) {
                            checkBatteryId = battery.getId();

                        }
                        CabinetBox fullBox = cabinetBoxMapper.findOneFull(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), cabinet.getId(), checkBatteryId, battery.getType(), bespeakBoxNum);
                        if (fullBox == null) {
                            //收集要开箱的箱号
                            open = true;
                            openBoxList.add(Integer.parseInt(boxNum, 10));
                            if (log.isDebugEnabled()) {
                                log.debug("重开箱门 电池在箱中检测, 没有满电, batteryId: {}, cabinetId: {}, boxNum: {}", battery.getId(), cabinet.getId(), boxNum);
                            }
                        }
                    }

                    //没有空箱格口了,要重新开箱。 重点：必须是箱门打开的情况下关闭箱门才会弹开
                    if (!open && oldIsOpen == ConstEnum.Flag.TRUE.getValue()) {
                        CabinetBox emptyBox = cabinetBoxMapper.findOneEmptyBoxNum(cabinetId, CabinetBox.BoxStatus.EMPTY.getValue(), ConstEnum.Flag.TRUE.getValue(), ConstEnum.Flag.TRUE.getValue());
                        if (emptyBox == null) {
                            //收集要开箱的箱号
                            openBoxList.add(Integer.parseInt(boxNum, 10));
                            if (log.isDebugEnabled()) {
                                log.debug("重开箱门 电池在箱中检测, 没有空箱, batteryId: {}, cabinetId: {}, boxNum: {}", battery.getId(), cabinet.getId(), boxNum);
                            }
                        }
                    }
                }
            }

            //之前格口为空 现在格口中有电池(心跳上报有电池) 说明电池被放入格口(防止误开门，柜子重连的首次心跳不走该操作)
            if (e.isClosed == 1 && battery != null && StringUtils.isEmpty(cabinetBox.getBatteryId()) && !reconnect) { //说明电池之前不在柜子中 现在心跳上报电池在格子中
                //更新主柜有无操作(一个主柜屏幕操作多个分柜) 轮询是否需要更新列表协议(防止浪费流量)
                if (operationFlag == 0) {
                    cabinetMapper.updateOperationFlag(cabinetBox.getCabinetId(), ConstEnum.Flag.TRUE.getValue());
                    operationFlag = 1;
                }
                try {
                    String cacheKey = CacheKey.key(CacheKey.K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                    NewBoxNum newBoxNum = (NewBoxNum) memcachedClient.get(cacheKey);
                    if (newBoxNum != null) {
                        memcachedClient.delete(cacheKey);
                        if (log.isDebugEnabled()) {
                            log.debug("delete cache K_OLD_BOXNUM_V_NEW_BOXNUM, cabinetId={}, boxNum={}, NewBoxNum={}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), newBoxNum);
                        }
                    }
                } catch (Exception ex) {
                    log.error(String.format("K_OLD_BOXNUM_V_NEW_BOXNUM error, cabinetId={}, boxNum={}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum()), e);
                }

                if (log.isDebugEnabled()) {
                    log.debug("柜子{} 箱号{} 检测到电池(关门) {} 状态 {}, {}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                }

                cabinetBox.setBatteryId(battery.getId());

                cabinetBoxKvList.add(new Kv("battery_id", battery.getId()));

                if (cabinetBox.getLockTime() != null) {
                    cabinetBoxKvList.add(new Kv("lock_time", null));
                }
                if (cabinetBox.getOpenType() != null) {
                    cabinetBoxKvList.add(new Kv("open_type", null));
                }
                if (cabinetBox.getOpenerId() != null) {
                    cabinetBoxKvList.add(new Kv("opener_id", null));
                }


                battery.setInBoxTime(now);
                battery.setCabinetId(cabinet.getId());
                battery.setCabinetName(cabinet.getCabinetName());
                battery.setBoxNum(boxNum);

                int status = battery.getStatus();
                String backOrderId = null;
                String batteryOrderId = null;

                //客户退租
                if (cabinetBox.getBoxStatus() == CabinetBox.BoxStatus.BACK_LOCK.getValue()) {
                    //查询分柜箱号对应退租订单
                    BackBatteryOrder backBatteryOrder = backBatteryOrderMapper.findByBoxNum(cabinet.getId(), boxNum, BackBatteryOrder.OrderStatus.SUCCESS.getValue());
                    if (backBatteryOrder != null) {
                        backOrderId = backBatteryOrder.getId();
                    } else {
                        log.error("电池{}对应箱门无退租订单 放入退租格口{} {}", battery.getId(), cabinet.getId(), boxNum);
                    }
                } else if (battery.getStatus() == Battery.Status.NOT_USE.getValue()) {
                    status = Battery.Status.IN_BOX.getValue();
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.BATTERY_PUT.getValue(), battery.getVolume(), new Date()));

                } else if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue() || battery.getStatus() == Battery.Status.CUSTOMER_OUT.getValue() || battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) {
                    status = Battery.Status.IN_BOX_NOT_PAY.getValue();
                    batteryOrderId = battery.getOrderId();

                } else if (battery.getStatus() == Battery.Status.KEEPER_OUT.getValue()) {
                    status = Battery.Status.IN_BOX.getValue();
                }

                cabinetBox.setBoxStatus(CabinetBox.BoxStatus.FULL.getValue());
                Kv boxStatusKv = new Kv("box_status", cabinetBox.getBoxStatus());
                cabinetBoxKvList.add(boxStatusKv);
                batteryMapper.updateInBox(battery.getId(), now, cabinet.getId(), cabinet.getCabinetName(), boxNum, status, Battery.ChargeStatus.NOT_CHARGE.getValue(), null, e.volume == null ? null : e.volume.intValue()); //设置电池的放入时间

                if (batteryOrderId != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("换电放入 订单{} 柜子{} 箱号{} 电池 {} 状态 {}, {}", batteryOrderId, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                    }
                    boxMessage += String.format("%s客户换电放入;",boxNum);
                    if (!customerPut(batteryOrderId, cabinet, battery, boxNum, openBoxList)) { //没有付款成功
                        cabinetBox.setBoxStatus(CabinetBox.BoxStatus.CUSTOMER_USE.getValue());
                        boxStatusKv.setV(cabinetBox.getBoxStatus());
                    }

                } else if (backOrderId != null) {
                    if (log.isDebugEnabled()) {
                        log.debug("退租放入 订单{} 柜子{} 箱号{} 电池 {} 状态 {}, {}", backOrderId, cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                    }
                    boxMessage += String.format("%s客户退租放入;",boxNum);
                    backBatteryPut(backOrderId, cabinet, battery, boxNum, openBoxList);

                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("无关联订单放入 订单id{} 柜子{} 箱号{} 电池 {} 状态 {}, {}", battery.getOrderId(), cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                    }
                    boxMessage += String.format("%s维护放入;",boxNum);
                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());
                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(String.format("%02d", e.boxNum));
                    operateLog.setOperateType(CabinetOperateLog.OperateType.PUT_BATTERY.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
                    operateLog.setContent(String.format("检测到放入电池 %s, 直接放入无订单", battery.getId()));
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogMapper.insert(operateLog);
                }

                //如果电池出于异常状态，插入推送元数据
                if(battery.getIsNormal() != null && battery.getIsNormal() == ConstEnum.Flag.FALSE.getValue()){
                    PushMetaData pushMetaData = new PushMetaData();
                    pushMetaData.setSourceType(PushMessage.SourceType.FAULT_FLAG_BATTERY.getValue());
                    pushMetaData.setSourceId(battery.getId());
                    pushMetaData.setCreateTime(now);
                    if (log.isDebugEnabled()) {
                        log.debug("battery: {} IMEI:{}  ", battery.getId(), battery.getCode());
                    }
                    pushMetaDataMapper.insert(pushMetaData);
                }

            } else if (e.batteryId == null && StringUtils.isNotEmpty(cabinetBox.getBatteryId()) && !reconnect) {
                //格子中没有电池(心跳上报电池为空) 但是之前有电池 说明电池被人取走了(防止误开门，柜子重连的首次心跳不走该操作)
                cabinetBox.setBoxStatus(CabinetBox.BoxStatus.EMPTY.getValue());
                cabinetBoxKvList.add(new Kv("box_status", cabinetBox.getBoxStatus()));
                cabinetBoxKvList.add(new Kv("battery_id", null));

                if (cabinetBox.getLockTime() != null) {
                    cabinetBoxKvList.add(new Kv("lock_time", null));
                }
                if (cabinetBox.getOpenType() != null) {
                    cabinetBoxKvList.add(new Kv("open_type", null));
                }
                if (cabinetBox.getOpenerId() != null) {
                    cabinetBoxKvList.add(new Kv("opener_id", null));
                }

                battery = batteryMapper.find(cabinetBox.getBatteryId());


                //如果电池表保存对应的柜子和心跳对比不是同一个柜子，有可能存在这次心跳延迟上报了。电池已经被取出放入其他柜子中了，该次电池状态不再更新
                if(battery.getCabinetId() != null && !battery.getCabinetId().equals(cabinetBox.getCabinetId())){
                    if (log.isDebugEnabled()) {
                        log.debug("电池被取出延迟上报 柜子{} 箱号{} 电池当前柜子{} 电池当前箱号{} 电池 {} 状态 {}, {}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getCabinetId(), battery.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                    }
                    boxMessage += String.format("%s客户新电取出(延迟上报);",boxNum);
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_TAKE_NEW.getValue(), battery.getCustomerId(), battery.getCustomerMobile(), battery.getCustomerFullname(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));
                }else if (battery.getStatus() == Battery.Status.IN_BOX_NOT_PAY.getValue()) { //可能是重开箱门 客户拿走了电池
                    if (log.isDebugEnabled()) {
                        log.debug("客户拿走未付款电池 柜子{} 箱号{} 电池 {} 状态 {}, {}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                    }
                    boxMessage += String.format("%s客户旧电取出;",boxNum);
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_TAKE_OLD.getValue(), battery.getCustomerId(), battery.getCustomerMobile(), battery.getCustomerFullname(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));
                    batteryMapper.updateInBox(battery.getId(), null, null, null, null, Battery.Status.CUSTOMER_OUT.getValue(), Battery.ChargeStatus.NOT_CHARGE.getValue(), now, null); //设置电池的放入时间

                    BatteryOrder order = batteryOrderMapper.find(battery.getOrderId());
                    if (order != null && order.getPayTimeoutFaultLogId() != null) {
                        faultLogMapper.handle(order.getPayTimeoutFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                    }
                    batteryOrderMapper.takeOldBattery(battery.getOrderId(), BatteryOrder.OrderStatus.TAKE_OUT.getValue());
//                    BatteryUtilize batteryUtilize = batteryUtilizeMapper.find(battery.getId());
//                    if (batteryUtilize != null) {
//                        batteryUtilizeMapper.delete(batteryUtilize.getId());
//                    }
                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());
                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(boxNum);
                    operateLog.setOperateType(CabinetOperateLog.OperateType.TAKE_BATTERY.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
                    operateLog.setContent(String.format("换电订单%s, 客户取出未付款电池%s", battery.getOrderId(), battery.getId()));
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogMapper.insert(operateLog);

                    //加入客户取新电进缓存
                    if(order != null){
                        String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_BATTERY_ORDER, cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                        memcachedClient.set(cacheKey, order, MemCachedConfig.CACHE_ONE_HOUR);
                        log.debug("add cache K_CABINET_BOX_V_BATTERY_ORDER, cabinetId={}, boxNum={}, orderId={}", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), order.getId());
                    }

                } else if (battery.getStatus() == Battery.Status.IN_BOX_CUSTOMER_USE.getValue()) {
                    if (log.isDebugEnabled()) {
                        log.debug("客户拿走新电 订单id{} 柜子{} 箱号{} 电池 {} 状态 {}, {}", battery.getOrderId(), cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                    }
                    boxMessage += String.format("%s客户新电取出;",boxNum);
                    batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_TAKE_NEW.getValue(), battery.getCustomerId(), battery.getCustomerMobile(), battery.getCustomerFullname(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));

                    BatteryOrder order = batteryOrderMapper.find(battery.getOrderId());
                    if (order != null && order.getNotTakeTimeoutFaultLogId() != null) {
                        faultLogMapper.handle(order.getNotTakeTimeoutFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                    }
                    //客户取新电 更新状态 使用中
                    batteryOrderMapper.takeNewBattery(battery.getOrderId(), now, BatteryOrder.OrderStatus.TAKE_OUT.getValue());
                    //客户取出 清空电池上主柜 分柜信息 设置电池的空出时间
                    batteryMapper.updateInBox(battery.getId(), null, null, null, null, Battery.Status.CUSTOMER_OUT.getValue(), Battery.ChargeStatus.NOT_CHARGE.getValue(), now, null);

                    CabinetOperateLog operateLog = new CabinetOperateLog();
                    operateLog.setAgentId(cabinet.getAgentId());
                    operateLog.setCabinetId(cabinet.getId());
                    operateLog.setCabinetName(cabinet.getCabinetName());
                    operateLog.setBoxNum(boxNum);
                    operateLog.setOperateType(CabinetOperateLog.OperateType.TAKE_BATTERY.getValue());
                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
                    operateLog.setContent(String.format("换电订单%s, 客户取出新电池%s", battery.getOrderId(), battery.getId()));
                    operateLog.setCreateTime(new Date());
                    cabinetOperateLogMapper.insert(operateLog);

                    //加入客户取新电进缓存
                    if(order != null && e.isClosed == 0){
                        String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_BATTERY_ORDER, cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                        memcachedClient.set(cacheKey, order, MemCachedConfig.CACHE_ONE_HOUR);
                    }

                }  else { //处理维护员直接开箱取出 不经过订单
                    //如果存在预约订单，结束掉
                    BespeakOrder bespeakOrder = bespeakOrderMapper.findByBespeak(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), BespeakOrder.Status.SUCCESS.getValue());
                    if(bespeakOrder != null){
                        bespeakOrderMapper.updateStatus(bespeakOrder.getId(), BespeakOrder.Status.SUCCESS.getValue(), BespeakOrder.Status.MANUAL_COMPLETE.getValue(), new Date());
                        //cabinetBoxMapper.unlockBox(cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), CabinetBox.BoxStatus.BACK_LOCK.getValue(), CabinetBox.BoxStatus.EMPTY.getValue());
                    }
                    boxMessage += String.format("%s维护取出;",boxNum);
                    if (StringUtils.isNotEmpty(battery.getOrderId())) {
                        log.error("battery{} 不经过订单直接取出, 状态异常, orderId{}", battery.getId(), battery.getOrderId());

                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("不经过订单直接取出 订单id{} 柜子{} 箱号{} 电池 {} 状态 {}, {}", battery.getOrderId(), cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), battery.getId(), battery.getStatus(), Battery.Status.getName(battery.getStatus()));
                        }
                        batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.BATTERY_TAKE.getValue(), battery.getVolume(), new Date()));

                        batteryMapper.updateFreeOut(cabinetBox.getBatteryId(), Battery.Status.NOT_USE.getValue(), new Date());

                        //电池通讯异常推送,如果箱门是关着的，并且电池没了，判断为通讯异常
                        String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_BATTERY, cabinetBox.getCabinetId(), cabinetBox.getBoxNum());
                        String batteryId = (String) memcachedClient.get(cacheKey);
                        if(StringUtils.isNotEmpty(batteryId) ){
                            if(e.isClosed == 1 && battery.getId().equals(batteryId)){
                                //锁定箱门
                                if(cabinetBox.getIsActive() == ConstEnum.Flag.TRUE.getValue()){
                                    cabinetBoxKvList.add(new Kv("is_active", ConstEnum.Flag.FALSE.getValue()));
                                    cabinetBoxKvList.add(new Kv("forbidden_cause", "系统检测:电池通讯异常"));

                                    CabinetOperateLog operateLog = new CabinetOperateLog();
                                    operateLog.setAgentId(cabinet.getAgentId());
                                    operateLog.setCabinetId(cabinet.getId());
                                    operateLog.setCabinetName(cabinet.getCabinetName());
                                    operateLog.setBoxNum(String.format("%02d", e.boxNum));
                                    operateLog.setOperateType(CabinetOperateLog.OperateType.NO_ACTIVE.getValue());
                                    operateLog.setOperatorType(CabinetOperateLog.OperatorType.PLATFORM.getValue());
                                    operateLog.setContent("箱门禁用成功（心跳）");
                                    operateLog.setCreateTime(new Date());
                                    cabinetOperateLogMapper.insert(operateLog);
                                }

                                //推送异常
                                AgentInfo agentInfo = findAgentInfo(cabinet.getAgentId());
                                insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(),
                                        battery.getId(),
                                        cabinet.getProvinceId(),
                                        cabinet.getCityId(),
                                        cabinet.getDistrictId(),
                                        cabinet.getDispatcherId(),
                                        cabinet.getAgentId(),
                                        agentInfo.getAgentName(),
                                        battery.getId(),
                                        cabinet.getId(),
                                        cabinet.getCabinetName(),
                                        cabinet.getAddress(),
                                        boxNum,
                                        FaultLog.FaultType.CODE_25.getValue(),
                                        String.format("换电柜%s,%s中电池%s通讯异常，请关注",cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), batteryId));

                                //推送运营商
                                PushMetaData pushMetaData = new PushMetaData();
                                pushMetaData.setSourceId(String.format("%s:%s:%s", cabinetBox.getCabinetId(), cabinetBox.getBoxNum(), batteryId));
                                pushMetaData.setSourceType(PushMessage.SourceType.CABINET_BATTERY_REPORT_FALUT.getValue());
                                pushMetaData.setCreateTime(new Date());
                                pushMetaDataMapper.insert(pushMetaData);
                            }
                            memcachedClient.delete(cacheKey);
                        }


                        //异常电池处理
                        if(battery.getUpLineStatus() == ConstEnum.Flag.TRUE.getValue() && battery.getIsNormal() == ConstEnum.Flag.FALSE.getValue()
                                && battery.getAbnormalCause() != null && battery.getAbnormalCause().startsWith("心跳检测")){
                            batteryMapper.changeIsNormal(battery.getId(), ConstEnum.Flag.TRUE.getValue(),null,null, null);
                        }

                        CabinetOperateLog operateLog = new CabinetOperateLog();
                        operateLog.setAgentId(cabinet.getAgentId());
                        operateLog.setCabinetId(cabinet.getId());
                        operateLog.setCabinetName(cabinet.getCabinetName());
                        operateLog.setBoxNum(boxNum);
                        operateLog.setOperateType(CabinetOperateLog.OperateType.TAKE_BATTERY.getValue());
                        operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
                        operateLog.setContent(String.format("未经过订单直接取出电池%s", battery.getId()));
                        operateLog.setCreateTime(new Date());
                        cabinetOperateLogMapper.insert(operateLog);
                    }

                }

            }



            if (!cabinetBoxKvList.isEmpty()) {
                Kv.uniqueCheck(cabinetBoxKvList);
                cabinetBoxMapper.update(cabinetId, boxNum, cabinetBoxKvList);
            }
            if (StringUtils.isNotEmpty(e.batteryCode)) {

                if(e.chargeStatus != null){
                    int[] chargeStatus = parseStatus(e.chargeStatus, 8);
                    if (chargeStatus[4] == 1) {
                        battery.setChargeStatus(Battery.ChargeStatus.CHARGE_FULL.getValue());
                    } else if (chargeStatus[5] == 1) {
                        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
                        chargeBatteryNum++;
                    } else if (chargeStatus[5] == 0) {
                        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
                    }
                }else if(e.chargeStage != null){
                    if (e.chargeStage  == 5) {
                        battery.setChargeStatus(Battery.ChargeStatus.CHARGE_FULL.getValue());
                    } else if (e.chargeStage  == 1 || e.chargeStage  == 2 || e.chargeStage  == 3 || e.chargeStage  == 4) {
                        battery.setChargeStatus(Battery.ChargeStatus.CHARGING.getValue());
                        chargeBatteryNum++;
                    } else if (e.chargeStage == 0) {
                        battery.setChargeStatus(Battery.ChargeStatus.NOT_CHARGE.getValue());
                    }
                }

                //运营商属于独立用户，电池自动上线
                if((battery.getUpLineStatus() == null || battery.getUpLineStatus() == ConstEnum.Flag.FALSE.getValue())){
                   Agent agent = agentMapper.find(cabinet.getAgentId());
                   if (agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()) {
                        batteryMapper.updateUpLine(battery.getId(), cabinet.getAgentId(), ConstEnum.Flag.TRUE.getValue(), new Date());
                   }
                   if (agent != null && agent.getId() == 33) {
                       batteryMapper.updateUpLine(battery.getId(), cabinet.getAgentId(), ConstEnum.Flag.TRUE.getValue(), new Date());
                   }
                }

                //加入待处理电池列表（异步处理电池数据）
                batteryList.add(battery);
            }

        }
        cabinetMapper.updatePower(cabinetId, power, batteryNum, chargeBatteryNum);
        //柜子电池统计
        handleCabinetBatteryStats(cabinet, batteryNum,  batteryMessage,  boxMessage);
    }

    private int[] parseStatus(int value, int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = value >> i & 0x01;
        }
        return array;
    }



    public void cleanFaultLogId(String cabinetId) {
        cabinetMapper.cleanFaultLogId(cabinetId);
    }


    /**
     * 柜子重连时结束入柜未付款订单
     */
    private void reconnectPut(Cabinet cabinet, Battery battery, String boxNum) {
        BatteryOrder batteryOrder = batteryOrderMapper.find(battery.getOrderId());
        batteryOrderMapper.updateErrorMessage(battery.getOrderId(), new Date(), String.format("柜子离线重连结束订单"));
        customerExchangeBatteryMapper.clearBattery(batteryOrder.getCustomerId(), batteryOrder.getBatteryId());
        batteryMapper.clearCustomer(battery.getId(), Battery.Status.IN_BOX.getValue());
        if (batteryOrder.getPayTimeoutFaultLogId() != null) {
            faultLogMapper.handle(batteryOrder.getPayTimeoutFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, new Date(), null, FaultLog.Status.PROCESSED.getValue());
        }

        batteryOrderMapper.backOk(battery.getOrderId(), BatteryOrder.OrderStatus.MANUAL_COMPLETE.getValue(), ConstEnum.PayType.BALANCE.getValue(), new Date(),
                0, 0, battery.getVolume(),
                cabinet.getId(), cabinet.getCabinetName(),
                boxNum, new Date(), battery.getCurrentCapacity());

        Agent agent = agentMapper.find(batteryOrder.getAgentId());
        if (agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()) {
            PushOrderMessage pushOrderMessage = new PushOrderMessage();
            pushOrderMessage.setAgentId(agent.getId());
            pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
            pushOrderMessage.setSourceId(battery.getOrderId());
            pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
            pushOrderMessage.setCreateTime(new Date());
            pushOrderMessageMapper.insert(pushOrderMessage);
        }
    }

    /**
     * 退租电池的放入处理
     * 1 修改退租订单是完成状态
     * 2 修改换电订单是已支付状态
     *
     * @param orderId 退租预约订单
     * @param cabinet 主柜
     * @param battery 柜中电池
     * @param boxNum  箱号
     */
    private void backBatteryPut(String orderId, Cabinet cabinet, Battery battery, String boxNum, List<Integer> openBoxList) {
        Date now = new Date();
        //判断心跳电池与退租订单对应电池是否一致
        BackBatteryOrder backBatteryOrder = backBatteryOrderMapper.find(orderId);
        Customer customer = customerMapper.find(backBatteryOrder.getCustomerId());
        List<CustomerExchangeBattery> batteryList = customerExchangeBatteryMapper.findListByCustomer(backBatteryOrder.getCustomerId());
        boolean sameBattery = false;
        Agent agent = agentMapper.find(cabinet.getAgentId());
        for (CustomerExchangeBattery customerExchangeBattery : batteryList) {
            if (customerExchangeBattery.getBatteryId().equals(battery.getId())) {
                /*电池相同*/
                sameBattery = true;
                //清除客户上的电池引用
                customerExchangeBatteryMapper.clearBattery(customer.getId(), customerExchangeBattery.getBatteryId());
                //预约成功-->已归还
                backBatteryOrderMapper.backBattery(orderId, battery.getId(), BackBatteryOrder.OrderStatus.EXCHANGE.getValue(), now);

                //强制结束换电订单 这里实际上有个漏洞 退租时候 不用支付换电费用
                batteryOrderMapper.backOk(battery.getOrderId(), BatteryOrder.OrderStatus.PAY.getValue(), ConstEnum.PayType.BALANCE.getValue(), now,
                        0, 0, battery.getVolume(),
                        cabinet.getId(), cabinet.getCabinetName(),
                        boxNum, now,battery.getCurrentCapacity());
                //电池在柜子中
                batteryMapper.clearCustomer(battery.getId(), Battery.Status.IN_BOX.getValue());

                CabinetOperateLog operateLog = new CabinetOperateLog();
                operateLog.setAgentId(cabinet.getAgentId());
                operateLog.setCabinetId(cabinet.getId());
                operateLog.setCabinetName(cabinet.getCabinetName());
                operateLog.setBoxNum(boxNum);
                operateLog.setOperateType(CabinetOperateLog.OperateType.PUT_BATTERY.getValue());
                operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
                operateLog.setContent(String.format("退租订单%s 放入电池%s", orderId, battery.getId()));
                operateLog.setOperator(customer.getFullname());
                operateLog.setCreateTime(new Date());
                cabinetOperateLogMapper.insert(operateLog);
                batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_BACK_OLD.getValue(), battery.getCustomerId(), battery.getCustomerMobile(), battery.getCustomerFullname(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));


                if (agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()) {
                    PushOrderMessage pushOrderMessage = new PushOrderMessage();
                    pushOrderMessage.setAgentId(agent.getId());
                    pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
                    pushOrderMessage.setSourceId(battery.getOrderId());
                    pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                    pushOrderMessage.setCreateTime(new Date());
                    pushOrderMessageMapper.insert(pushOrderMessage);

                    pushOrderMessage = new PushOrderMessage();
                    pushOrderMessage.setAgentId(agent.getId());
                    pushOrderMessage.setSourceType(PushOrderMessage.SourceType.BACK.getValue());
                    pushOrderMessage.setSourceId(backBatteryOrder.getId());
                    pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                    pushOrderMessage.setCreateTime(new Date());
                    pushOrderMessageMapper.insert(pushOrderMessage);
                }
                break;
            }
        }

        if (!sameBattery) {
            if (log.isDebugEnabled()) {
                log.debug("退租订单放入不是自己的电池开箱, batteryId: {}, cabinetId: {}, boxNum: {}", battery.getId(), cabinet.getId(), boxNum);
            }
            openBoxList.add(Integer.parseInt(boxNum, 10));

            /*电池不同*/
            //退租订单结束处理
            //customerMapper.clearOnlyBackBatteryOrder(backBatteryOrder.getCustomerId());
            //状态: 电池不一致
            backBatteryOrderMapper.backBattery(orderId, backBatteryOrder.getBatteryId(), BackBatteryOrder.OrderStatus.DIFFER.getValue(), now);

            //查询箱中电池是否关联客户
            if (battery.getCustomerId() == null) {
                log.error("电池{}无关联客户 放入退租格口{} {}", battery.getId(), cabinet.getId(), boxNum);
                battery.setStatus(Battery.Status.IN_BOX.getValue());//关联客户柜子中已付款
                batteryMapper.updateInBox(battery.getId(), now, cabinet.getId(), cabinet.getCabinetName(), boxNum, battery.getStatus(), Battery.ChargeStatus.NOT_CHARGE.getValue(), null, null); //设置电池的放入时间
            } else {
                battery.setStatus(Battery.Status.IN_BOX_NOT_PAY.getValue());//未关联客户柜子中未付款
                batteryMapper.updateInBox(battery.getId(), now, cabinet.getId(), cabinet.getCabinetName(), boxNum, battery.getStatus(), Battery.ChargeStatus.NOT_CHARGE.getValue(), null, null); //设置电池的放入时间
                //退租电池异常
                batteryOrderMapper.updateErrorMessage(battery.getOrderId(), new Date(), String.format("退租%s放入换电电池%s异常", orderId, battery.getId()));
                //入柜未付款
                String shopId = null,shopName = null;
                Shop shop = shopMapper.find(cabinet.getShopId());
                if(shop != null){
                    shopId = shop.getId();
                    shopName = shop.getShopName();
                }
                batteryOrderMapper.putBattery(battery.getOrderId(), battery.getVolume(), BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue(),
                        shopId, shopName, cabinet.getId(), cabinet.getCabinetName(), boxNum, new Date(),battery.getCurrentCapacity());
                //电池利用率
                // batteryUtilizeMapper.insert(new BatteryUtilize(battery.getId(), cabinet.getId(), cabinet.getCabinetName(), now, null, null));
                //推送消息给柜中电池绑定客户
                //推送电池异常
                if (agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()) {
                    PushOrderMessage pushOrderMessage = new PushOrderMessage();
                    pushOrderMessage.setAgentId(agent.getId());
                    pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT_ERROR.getValue());
                    pushOrderMessage.setSourceId(String.format("%s:%d",battery.getCode(), customer.getId()));
                    pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                    pushOrderMessage.setCreateTime(new Date());
                    pushOrderMessageMapper.insert(pushOrderMessage);
                }else{
                    PushMetaData pushMetaData = new PushMetaData();
                    pushMetaData.setSourceId(battery.getOrderId());
                    pushMetaData.setSourceType(PushMessage.SourceType.BATTERY_IN_BOX_NOTICE.getValue());
                    pushMetaData.setCreateTime(new Date());
                    pushMetaDataMapper.insert(pushMetaData);
                }
            }

            CabinetOperateLog operateLog = new CabinetOperateLog();
            operateLog.setAgentId(cabinet.getAgentId());
            operateLog.setCabinetId(cabinet.getId());
            operateLog.setCabinetName(cabinet.getCabinetName());
            operateLog.setBoxNum(boxNum);
            operateLog.setOperateType(CabinetOperateLog.OperateType.PUT_BATTERY.getValue());
            operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
            operateLog.setContent(String.format("退租订单%s 应退电池%s 放入电池%s", orderId, backBatteryOrder.getBatteryId(), battery.getId()));
            operateLog.setOperator(customer.getFullname());
            operateLog.setCreateTime(new Date());
            cabinetOperateLogMapper.insert(operateLog);
            batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_BACK_OLD.getValue(), backBatteryOrder.getCustomerId(), backBatteryOrder.getCustomerMobile(), backBatteryOrder.getCustomerFullname(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));
        }
    }

    private boolean customerPut(String orderId, Cabinet cabinet, Battery battery, String boxNum, List<Integer> openBoxList) {
        long customerId = battery.getCustomerId();
        Customer customer = customerMapper.find(customerId);
        if (customer == null) {
            batteryOrderMapper.updateErrorMessage(orderId, new Date(), "电池未对应客户");

            //推送电池异常
            PushMetaData pushMetaData = new PushMetaData();
            pushMetaData.setSourceId(orderId);
            pushMetaData.setSourceType(PushMessage.SourceType.BATTERY_IN_BOX_NOTICE.getValue());
            pushMetaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(pushMetaData);
            return false;
        }
        //订单状态 入柜未付款 绑定主柜分柜信息
        String shopId = null,shopName = null;
        Shop shop = shopMapper.find(cabinet.getShopId());
        if(shop != null){
            shopId = shop.getId();
            shopName = shop.getShopName();
        }
        batteryOrderMapper.putBattery(orderId, battery.getVolume(), BatteryOrder.OrderStatus.IN_BOX_WAIT_PAY.getValue(),
                shopId, shopName, cabinet.getId(), cabinet.getCabinetName(), boxNum, new Date(), battery.getCurrentCapacity());

        CabinetOperateLog operateLog = new CabinetOperateLog();
        operateLog.setAgentId(cabinet.getAgentId());
        operateLog.setCabinetId(cabinet.getId());
        operateLog.setCabinetName(cabinet.getCabinetName());
        operateLog.setBoxNum(boxNum);
        operateLog.setOperateType(CabinetOperateLog.OperateType.PUT_BATTERY.getValue());
        operateLog.setOperatorType(CabinetOperateLog.OperatorType.HEARTBEAT.getValue());
        operateLog.setContent(String.format("换电订单%s 放入电池%s", orderId, battery.getId()));
        operateLog.setOperator(customer.getFullname());
        operateLog.setCreateTime(new Date());
        cabinetOperateLogMapper.insert(operateLog);

        //客户放旧电
        batteryOperateLogMapper.insert(new BatteryOperateLog(battery.getId(), BatteryOperateLog.OperateType.CUSTOMER_PUT_OLD.getValue(), battery.getCustomerId(), battery.getCustomerMobile(), battery.getCustomerFullname(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), battery.getVolume(), new Date()));

        Agent agent = agentMapper.find(cabinet.getAgentId());

        //非本人电池校验
        String cacheKey = CacheKey.key(CacheKey.K_CABINET_BOX_V_CUSTMOER_ID, cabinet.getId(), boxNum);
        Long putCustomerId = (Long) memcachedClient.get(cacheKey);
        if (putCustomerId != null && customerId != putCustomerId) {
            //收集要开箱的箱号
            openBoxList.add(Integer.parseInt(boxNum, 10));
            if (log.isDebugEnabled()) {
                log.debug("重开箱门 电池推入检测, 非本人电池, batteryId: {}, cabinetId: {}, boxNum: {}", battery.getId(), cabinet.getId(), boxNum);
            }
            batteryOrderMapper.updateErrorMessage(orderId, new Date(), String.format("客户[%s]在柜子[%s][%s]放入非本人电池",customer.getFullname(), cabinet.getId(), boxNum));

            //推送电池异常
            if (agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()) {
                PushOrderMessage pushOrderMessage = new PushOrderMessage();
                pushOrderMessage.setAgentId(agent.getId());
                pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT_ERROR.getValue());
                pushOrderMessage.setSourceId(String.format("%s:%d",battery.getCode(), putCustomerId));
                pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                pushOrderMessage.setCreateTime(new Date());
                pushOrderMessageMapper.insert(pushOrderMessage);
            }else{
                PushMetaData pushMetaData = new PushMetaData();
                pushMetaData.setSourceId(orderId);
                pushMetaData.setSourceType(PushMessage.SourceType.BATTERY_IN_BOX_NOTICE.getValue());
                pushMetaData.setCreateTime(new Date());
                pushMetaDataMapper.insert(pushMetaData);
            }

            return false;
        }else{
            memcachedClient.delete(cacheKey);
        }

        // 电池当前电量>柜子可换进电量 放电量不满足最低换电电量要重新开箱 发送开箱指令
        if (battery.getVolume() > cabinet.getPermitExchangeVolume()) {
            //收集要开箱的箱号  暂不弹开箱门
            //openBoxList.add(Integer.parseInt(boxNum, 10));
            if (log.isDebugEnabled()) {
                log.debug("重开箱门 电池推入检测, 电池当前电量大于柜子可换进电量, batteryId: {}, cabinetId: {}, boxNum: {}", battery.getId(), cabinet.getId(), boxNum);
            }
            batteryOrderMapper.updateErrorMessage(orderId, new Date(), String.format("用户[%s]电池当前电量大于柜子可换进电量", battery.getCustomerFullname()));

            PushMetaData pushMetaData = new PushMetaData();
            pushMetaData.setSourceId(orderId);
            pushMetaData.setSourceType(PushMessage.SourceType.BATTERY_IN_BOX_NOTICE.getValue());
            pushMetaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(pushMetaData);
            return false;
        }
        //没有满电电池在格口中(正常流程不扫码 有打开的格口客户直接放进去)要重新开箱
        Integer batteryType = null;
        PacketPeriodOrder packetPeriodOrder = null;

        if (cabinet.getAgentId() == null) {
            batteryOrderMapper.updateErrorMessage(orderId, new Date(), String.format("柜子[%s]没有设置运营商", cabinet.getId()));
            return false;
        }
        BatteryOrder batteryOrder = batteryOrderMapper.find(orderId);
        if (batteryOrder == null) {
            batteryOrderMapper.updateErrorMessage(orderId, new Date(), String.format("换电订单[%s]不存在", orderId));
            return false;
        } else {
            batteryType = batteryOrder.getBatteryType();
        }

        //查询是否有预约订单
        String bespeakBoxNum = null;
        BespeakOrder bespeakOrder = bespeakOrderMapper.findSuccessByCustomer(customer.getId(), BespeakOrder.Status.SUCCESS.getValue());
        if(bespeakOrder != null && bespeakOrder.getBespeakCabinetId().equals(cabinet.getId())){
            bespeakBoxNum = bespeakOrder.getBespeakBoxNum();
        }

        CabinetBox subcabinetBox = cabinetBoxMapper.findOneFull(CabinetBox.BoxStatus.FULL.getValue(), Battery.Status.IN_BOX.getValue(), cabinet.getId(), battery.getId(), batteryType, bespeakBoxNum);
        if (subcabinetBox == null) {
            //收集要开箱的箱号
            openBoxList.add(Integer.parseInt(boxNum, 10));
            if (log.isDebugEnabled()) {
                log.debug("重开箱门 电池推入检测, 没有满电, batteryId: {}, cabinetId: {}, boxNum: {}", battery.getId(), cabinet.getId(), boxNum);
            }
            batteryOrderMapper.updateErrorMessage(orderId, new Date(), String.format("柜子[%s]没有满电", cabinet.getId()));

            //推送电池异常
            PushMetaData pushMetaData = new PushMetaData();
            pushMetaData.setSourceId(orderId);
            pushMetaData.setSourceType(PushMessage.SourceType.BATTERY_IN_BOX_NOTICE.getValue());
            pushMetaData.setCreateTime(new Date());
            pushMetaDataMapper.insert(pushMetaData);
            return false;
        }

        packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customerId, PacketPeriodOrder.Status.USED.getValue(), batteryOrder.getAgentId(), batteryOrder.getBatteryType());
        if (packetPeriodOrder == null) {
            packetPeriodOrder = packetPeriodOrderMapper.findOneEnabled(customerId, PacketPeriodOrder.Status.NOT_USE.getValue(), batteryOrder.getAgentId(), batteryOrder.getBatteryType());
        }
        boolean payOk = false;
        int payType = ConstEnum.PayType.PACKET.getValue();


        ExchangeWhiteList exchangeWhiteList = exchangeWhiteListMapper.findByCustomer(cabinet.getAgentId(), customerId);
        if (exchangeWhiteList != null) {
            payOk = true;
            payType = ConstEnum.PayType.BALANCE.getValue();
        }else if (agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()) {
           //非扫码放入电池用户，需单独判断是否允许换电
            if(putCustomerId == null){
                customerPutForIndependent( customerId,  batteryOrder);
                return true;
            }
            //独立客户，不需要支付
            payOk = true;
            payType = ConstEnum.PayType.PLATFORM.getValue();
        } else {
            if (packetPeriodOrder == null) {
                ExchangePriceTime exchangePriceTime = exchangePriceTimeMapper.findByBatteryType(cabinet.getAgentId(), batteryType);
                if (exchangePriceTime == null || exchangePriceTime.getActiveSingleExchange() == null || exchangePriceTime.getActiveSingleExchange() == ConstEnum.Flag.FALSE.getValue()) {
                    batteryOrderMapper.updateErrorMessage(orderId, new Date(), String.format("客户没有可用包时段套餐", cabinet.getId()));

                    //推送电池异常
                    PushMetaData pushMetaData = new PushMetaData();
                    pushMetaData.setSourceId(orderId);
                    pushMetaData.setSourceType(PushMessage.SourceType.BATTERY_IN_BOX_NOTICE.getValue());
                    pushMetaData.setCreateTime(new Date());
                    pushMetaDataMapper.insert(pushMetaData);
                    return false;
                } else {
                    int money = 0;
                    if (battery.getType().equals(exchangePriceTime.getBatteryType())) {
                        if (exchangePriceTime != null && exchangePriceTime.getVolumePrice() != null) {
                            money = Math.abs(battery.getVolume() - batteryOrder.getInitVolume()) * exchangePriceTime.getVolumePrice();
                        } else {
                            money = exchangePriceTime.getTimesPrice();
                        }
                    }

                    if (money == 0) {
                        payOk = true;
                        payType = ConstEnum.PayType.BALANCE.getValue();
                    } else {
                        payOk = false;
                        batteryOrderMapper.updatePrice(battery.getOrderId(), money, money);
                    }
                }

            } else { //有包月套餐的
                if (packetPeriodOrder.getStatus() == PacketPeriodOrder.Status.NOT_USE.getValue()) {

                    Date beginTime = new Date();
                    Date endTime = DateUtils.addSeconds(DateUtils.truncate(DateUtils.addDays(beginTime, packetPeriodOrder.getDayCount()), Calendar.DAY_OF_MONTH), -1);
                    int eft = packetPeriodOrderMapper.updateStatus(packetPeriodOrder.getId(), PacketPeriodOrder.Status.NOT_USE.getValue(), PacketPeriodOrder.Status.USED.getValue(), beginTime, endTime);
                    if (eft > 0) {
                        handleLaxinCustomerByMonth(agent, customer, packetPeriodOrder.getMoney());
                    }
                }

                payOk = true;
            }
        }


        if (payOk) { //给static-server发指令 开满箱
            if (log.isDebugEnabled()) {
                log.debug("BatteryOrder {} 换电完成", battery.getOrderId());
            }

            CabinetMemcachedLog cabinetMemcachedLog = new CabinetMemcachedLog();
            cabinetMemcachedLog.setFromService("cabinet-server");
            cabinetMemcachedLog.setOldBatteryOrder(batteryOrder.getId());
            memcachedClient.set(CacheKey.key(CacheKey.K_CABINET_ID_CUSTOMER_ID_V_ZERO, batteryOrder.getPutCabinetId(), batteryOrder.getCustomerId()), cabinetMemcachedLog, MemCachedConfig.CACHE_TEN_SECOND);
            if (log.isDebugEnabled()) {
                log.debug(cabinetMemcachedLog.toString());
            }

            customerExchangeInfoMapper.updateErrorMessage(customerId, null, null);

            customerExchangeBatteryMapper.clearBattery(batteryOrder.getCustomerId(), batteryOrder.getBatteryId());

            if (packetPeriodOrder != null) {
                packetPeriodOrderMapper.updateOrderCount(packetPeriodOrder.getId());
            }

            batteryMapper.clearCustomer(battery.getId(), Battery.Status.IN_BOX.getValue());

            if (batteryOrder.getPayTimeoutFaultLogId() != null) {
                faultLogMapper.handle(batteryOrder.getPayTimeoutFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, new Date(), null, FaultLog.Status.PROCESSED.getValue());
            }

            batteryOrderMapper.payOk(battery.getOrderId(), BatteryOrder.OrderStatus.PAY.getValue(),
                    payType,
                    new Date(), packetPeriodOrder == null ? null : packetPeriodOrder.getId(),
                    0, 0);

            if (agent != null && agent.getIsIndependent() != null && agent.getIsIndependent() == ConstEnum.Flag.TRUE.getValue()) {
                PushOrderMessage pushOrderMessage = new PushOrderMessage();
                pushOrderMessage.setAgentId(agent.getId());
                pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
                pushOrderMessage.setSourceId(battery.getOrderId());
                pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                pushOrderMessage.setCreateTime(new Date());
                pushOrderMessageMapper.insert(pushOrderMessage);
            }

            ChannelHandlerContext context = config.sessionManager.getStaticServerClient();
            if (context != null) {
                Msg231000001 msg = new Msg231000001();
                msg.cabinetId = cabinet.getId();
                msg.boxNum = boxNum;
                msg.customerId = customerId;
                msg.setSerial(config.sequence.incrementAndGet());
                context.writeAndFlush(msg);
                if (log.isDebugEnabled()) {
                    log.debug("cabinet {} send message {} to static-server", cabinet.getId(), msg);
                }

            } else {
                if (log.isDebugEnabled()) {
                    log.debug("static-server not connect cabinet {}", cabinet.getId());
                }
                customerExchangeInfoMapper.updateErrorMessage(customerId, new Date(), String.format("柜子[%s]没有连接静态服务器", cabinet.getId()));
            }
        }

        return payOk;
    }

    public Battery createBattery(String code, int agentId, int voltage, int electricity, int volume, int protectState, int fet, int chargeStatus) {
        String id = nextBatteryId();
        Battery battery = new Battery();
        battery.setId(id);
        battery.setCode(code);
        battery.setShellCode("");
        battery.setType(2);
        //默认60V
        //智租电池特殊处理
        if(agentId == 23){
            if(code.startsWith("B1")){
                battery.setType(1);
            }else if(code.startsWith("B2")){
                battery.setType(2);
            }
        }
        battery.setVoltage(voltage);
        battery.setElectricity(electricity);
        battery.setVolume(volume);
        battery.setProtectState(protectState);
        battery.setFet(fet);
        battery.setChargeStatus(chargeStatus);
        battery.setCategory(Battery.Category.EXCHANGE.getValue());
        battery.setAgentId(agentId);
        battery.setQrcode("");
        battery.setOrderDistance(0);
        battery.setTotalDistance(0L);
        battery.setIsActive(ConstEnum.Flag.TRUE.getValue());
        battery.setExchangeAmount(0);
        battery.setIsReportVoltage(ConstEnum.Flag.FALSE.getValue());
        battery.setUseCount(0);
        battery.setIsOnline(ConstEnum.Flag.FALSE.getValue());
        battery.setIsNormal(ConstEnum.Flag.TRUE.getValue());
        battery.setStatus(Battery.Status.NOT_USE.getValue());
        battery.setCreateTime(new Date());
        battery.setStayHeartbeat(Constant.STAY_HEARTBEAT);
        battery.setMoveHeartbeat(Constant.MOVE_HEARTBEAT);
        battery.setElectrifyHeartbeat(Constant.ELECTRIFY_HEARTBEAT);
        battery.setChargeCompleteVolume(95);
        battery.setGpsSwitch(ConstEnum.Flag.FALSE.getValue());
        battery.setGprsShutdown(ConstEnum.Flag.FALSE.getValue());
        battery.setShutdownVoltage(42000);
        battery.setAcceleretedSpeed(1);
        battery.setRepairStatus(Battery.RepairStatus.NOT.getValue());
        battery.setUpLineStatus(ConstEnum.Flag.FALSE.getValue());
        battery.setLockSwitch(Battery.LockSwitch.DISCHG_CLOSE.getValue());
        batteryMapper.insert(battery);
        return battery;
    }

    private String getTableSuffix(Date time) {
        String suffix = DateFormatUtils.format(time, Constant.DATE_FORMAT_NO_LINE);
        return suffix;
    }

    public FaultLog insertFaultLog(Integer faultLevel, Integer agentId, String agentName, String batteryId, String cabinetId, String cabinetName, String boxNum, Integer faultType, String faultContent) {
        FaultLog data = new FaultLog();
        data.setFaultLevel(faultLevel);
        data.setAgentId(agentId);
        data.setAgentName(agentName);
        data.setBatteryId(batteryId);
        data.setCabinetId(cabinetId);
        data.setCabinetName(cabinetName);
        data.setBoxNum(boxNum);
        data.setFaultType(faultType);
        data.setFaultContent(faultContent);
        data.setStatus(FaultLog.Status.WAIT_PROCESS.getValue());
        data.setCreateTime(new Date());
        faultLogMapper.insert(data);
        return data;
    }

    public void insertPushMetaData(Integer sourceType, String sourceId) {
        PushMetaData data = new PushMetaData();
        data.setSourceType(sourceType);
        data.setSourceId(sourceId);
        data.setCreateTime(new Date());
        pushMetaDataMapper.insert(data);
    }
    /**
     * 柜子日志插入
     */
    public void insertCabinetReport(CabinetReport cabinetReport) {
        try{
            String tableName = String.format("%s%s", CabinetReport.CABINET_REPORT_TABLE_NAME, cabinetReport.getSuffix());
            boolean result = findCabinetReportTable(tableName);
            if (!result) {
                cabinetReportMapper.createTable(tableName);
            }
            cabinetReport.setCreateTime(new Date());
            cabinetReportMapper.insert(cabinetReport);
            String reportDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
            if (cabinetReportDateMapper.update(reportDate, cabinetReport.getCabinetId()) == 0) {
                cabinetReportDateMapper.create(reportDate, cabinetReport.getCabinetId());
            }
        }catch (Exception e){
            log.error(String.format("换电柜日志插入 保存 error=%s", e.getMessage()));
        }
    }

    /**
     * 柜子中电池日志插入
     */
    public void insertCabinetReportBattery(CabinetReportBattery cabinetReportBattery) {
        try{
            String tableName = String.format("%s%s", CabinetReportBattery.CABINET_REPORT_BATTERY_TABLE_NAME, cabinetReportBattery.getSuffix());
            boolean result = findCabinetReportBatteryTable(tableName);
            if (!result) {
                cabinetReportBatteryMapper.createTable(tableName);
            }
            cabinetReportBattery.setCreateTime(new Date());
            cabinetReportBatteryMapper.insert(cabinetReportBattery);
            String reportDate = DateFormatUtils.format(new Date(), Constant.DATE_FORMAT);
            if (cabinetReportBatteryDateMapper.update(reportDate, cabinetReportBattery.getBatteryId()) == 0) {
                cabinetReportBatteryDateMapper.create(reportDate, cabinetReportBattery.getBatteryId());
            }
        }catch (Exception e){
            log.error(String.format("换电柜日志插入 保存 error=%s", e.getMessage()));
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void report(Cabinet cabinet, Battery battery) {
        Date now = new Date();

        //初始化电池上报信息
        BatteryReport batteryReport = new BatteryReport();
        batteryReport.setHeartType(BatteryReport.HeartType.CABINET_HEART.getValue());
        batteryReport.setBatteryId(battery.getId());
        batteryReport.setVersion(battery.getVersion());
        batteryReport.setLinkStatus(battery.getLinkStatus());
        batteryReport.setVoltage(battery.getVoltage());
        batteryReport.setElectricity(battery.getElectricity());
        batteryReport.setVolume(battery.getVolume());
        batteryReport.setCurrentCapacity(battery.getCurrentCapacity() );
        batteryReport.setTotalCapacity(battery.getTotalCapacity());
        batteryReport.setFault(battery.getProtectState());/*保护状态*/
        batteryReport.setSingleVoltage(battery.getSingleVoltage());
        batteryReport.setMos(battery.getFet());  /*bit0表示充电，bit1表示放电，0表示MOS关闭，1表示打开*/
        batteryReport.setSerials(battery.getStrand()); /*电池串数*/
        batteryReport.setTemp(battery.getTemp());
        batteryReport.setLng(battery.getLng());
        batteryReport.setLat(battery.getLat());
        batteryReport.setDistance(null);
        batteryReport.setCurrentSignal(battery.getCurrentSignal());
        batteryReport.setCircle(battery.getCircle());
        batteryReport.setIsMotion(Battery.PositionState.NOT_MOVE.getValue());/*0表示位置不移动 1表示位置移动中 2表示通电中*/
        batteryReport.setSuffix(getTableSuffix(now));
        batteryReport.setCode(battery.getCode());
        batteryReport.setCabinetId(battery.getCabinetId());
        batteryReport.setBoxNum(battery.getBoxNum());
        //电池上报
        insertBatteryReport(batteryReport, now);

        //故障保存
        BatteryReportLog.ProtectState[] protectStates = BatteryReportLog.ProtectState.values();
        char[] str = new StringBuilder(String.format("%0" + protectStates.length + "d", Long.parseLong(Integer.toBinaryString(battery.getProtectState() == null ? 0 : battery.getProtectState() )))).reverse().toString().toCharArray();
        for (int i = 0; i < str.length; i++) {
            if ('1' == str[i]) {
                FaultLog faultLog = null;
                switch (protectStates[i].getValue()) {
                    case 1:
                        if (battery.getMonomerOvervoltageFaultLogId() == null) {
                            //单体过压发生保护 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_8.getValue(), battery.getId());

                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_1.getValue(), FaultLog.FaultType.CODE_1.getName());
                            battery.setMonomerOvervoltageFaultLogId(faultLog.getId());
                        }
                        break;
                    case 2:
                        if (battery.getMonomerLowvoltageFaultLogId() == null) {

                            //单体欠压发生保护 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_9.getValue(), battery.getId());

                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(), FaultLog.FaultType.CODE_2.getValue(), FaultLog.FaultType.CODE_2.getName());
                            battery.setMonomerLowvoltageFaultLogId(faultLog.getId());
                        }
                        break;
                    case 3:
                        if (battery.getWholeOvervoltageFaultLogId() == null) {

                            //整组过压发生保护 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_10.getValue(), battery.getId());

                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_3.getValue(), FaultLog.FaultType.CODE_3.getName());
                            battery.setWholeOvervoltageFaultLogId(faultLog.getId());
                        }
                        break;
                    case 4:
                        if (battery.getWholeLowvoltageFaultLogId() == null) {

                            //整组欠压发生保护 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_11.getValue(), battery.getId());

                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_4.getValue(), FaultLog.FaultType.CODE_4.getName());
                            battery.setWholeLowvoltageFaultLogId(faultLog.getId());
                        }
                        break;
                    case 5:
                        if (battery.getChargeOvertempFaultLogId() == null) {

                            //充电过温发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_5.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_5.getValue(), FaultLog.FaultType.CODE_5.getName());
                            battery.setChargeOvertempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 6:
                        if (battery.getChargeLowtempFaultLogId() == null) {

                            //充电低温发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_6.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_6.getValue(), FaultLog.FaultType.CODE_6.getName());
                            battery.setChargeLowtempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 7:
                        if (battery.getDischargeOvertempFaultLogId() == null) {

                            //放电过温发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_7.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_7.getValue(), FaultLog.FaultType.CODE_7.getName());
                            battery.setDischargeOvertempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 8:
                        if (battery.getDischargeLowtempFaultLogId() == null) {

                            //放电低温发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_8.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog(FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_8.getValue(), FaultLog.FaultType.CODE_8.getName());
                            battery.setDischargeLowtempFaultLogId(faultLog.getId());
                        }
                        break;
                    case 9:
                        if (battery.getChargeOvercurrentFaultLogId() == null) {

                            //充电过流发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_9.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));
                            faultLog = insertFaultLog( FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_9.getValue(), FaultLog.FaultType.CODE_9.getName());
                            battery.setChargeOvercurrentFaultLogId(faultLog.getId());
                        }
                        break;
                    case 10:
                        if (battery.getDischargeOvercurrentFaultLogId() == null) {
//                            //放电过流发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_10.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));

                            faultLog = insertFaultLog( FaultLog.FaultLevel.IMPORTANCE.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_10.getValue(), FaultLog.FaultType.CODE_10.getName());
                            battery.setDischargeOvercurrentFaultLogId(faultLog.getId());
                        }
                        break;
                    case 11:
                        if (battery.getShortCircuitFaultLogId() == null) {
                            //短路发生保护 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_11.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));
                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_11.getValue(), FaultLog.FaultType.CODE_11.getName());
                            battery.setShortCircuitFaultLogId(faultLog.getId());
                        }
                        break;
                    case 12:
                        if (battery.getTestingIcFaultLogId() == null) {
                            //前端检测IC错误 插入运营商告警
                            insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_12.getValue(), String.format("%d:%s",  battery.getAgentId(), battery.getId()));
                            faultLog = insertFaultLog(FaultLog.FaultLevel.MEDIUM.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_12.getValue(), FaultLog.FaultType.CODE_12.getName());
                            battery.setTestingIcFaultLogId(faultLog.getId());
                        }
                        break;
                    case 13:
                        if (battery.getSoftwareLockingFaultLogId() == null) {
                            //保护板充电MOS锁定 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_13.getValue(), battery.getId());
                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_13.getValue(), FaultLog.FaultType.CODE_13.getName());
                            battery.setSoftwareLockingFaultLogId(faultLog.getId());
                        }
                        break;
                    case 14:
                        if (battery.getDischargeLockingFaultLogId() == null) {
                            //保护板放电MOS锁定 插入运营商告警
                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_14.getValue(), battery.getId());
                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), battery.getCabinetId(), battery.getCabinetName(), battery.getBoxNum(),  FaultLog.FaultType.CODE_14.getValue(), FaultLog.FaultType.CODE_14.getName());
                            battery.setDischargeLockingFaultLogId(faultLog.getId());
                        }
                        break;
                    case 15:
//                        if (battery.getChargeMosFaultLogId() == null) {
//                            //充电MOS异常 插入运营商告警
//                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_15.getValue(), battery.getId());
//                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_15.getValue(), FaultLog.FaultType.CODE_15.getName());
//                            battery.setChargeMosFaultLogId(faultLog.getId());
//                        }
                       break;
                    case 16:
//                        if (battery.getDischargeMosFaultLogId() == null) {
//                            //放电MOS异常 插入运营商告警
//                            //insertPushMetaData(PushMessage.SourceType.FAULT_TYPE_CODE_16.getValue(), battery.getId());
//                            faultLog = insertFaultLog(FaultLog.FaultLevel.GENERAL.getValue(), battery.getAgentId(), findAgentInfo(battery.getAgentId()).getAgentName(), battery.getId(), FaultLog.FaultType.CODE_16.getValue(), FaultLog.FaultType.CODE_16.getName());
//                            battery.setDischargeMosFaultLogId(faultLog.getId());
//                        }
                        break;


                    default:
                        break;
                }
            } else {
                switch (protectStates[i].getValue()) {
                    case 1:
                        if (battery.getMonomerOvervoltageFaultLogId() != null)
                            faultLogMapper.handle(battery.getMonomerOvervoltageFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                        battery.setMonomerOvervoltageFaultLogId(null);
                        break;
                    case 2:
                        if (battery.getMonomerLowvoltageFaultLogId() != null)
                            faultLogMapper.handle(battery.getMonomerLowvoltageFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                        battery.setMonomerLowvoltageFaultLogId(null);
                        break;
                    case 3:
                        if (battery.getWholeOvervoltageFaultLogId() != null)
                            faultLogMapper.handle(battery.getWholeOvervoltageFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                        battery.setWholeOvervoltageFaultLogId(null);
                        break;
                    case 4:
                        if (battery.getWholeLowvoltageFaultLogId() != null)
                            faultLogMapper.handle(battery.getWholeLowvoltageFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
                        battery.setWholeLowvoltageFaultLogId(null);
                        break;
                    case 5:
//                        if (battery.getChargeOvertempFaultLogId() != null)
//                            faultLogMapper.handle(battery.getChargeOvertempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
//                        battery.setChargeOvertempFaultLogId(null);
                        break;
                    case 6:
                        if (battery.getChargeLowtempFaultLogId() != null)
                            faultLogMapper.handle(battery.getChargeLowtempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setChargeLowtempFaultLogId(null);
                        break;
                    case 7:
//                        if (battery.getDischargeOvertempFaultLogId() != null)
//                            faultLogMapper.handle(battery.getDischargeOvertempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
//
//                        battery.setDischargeOvertempFaultLogId(null);
                        break;
                    case 8:
                        if (battery.getDischargeLowtempFaultLogId() != null)
                            faultLogMapper.handle(battery.getDischargeLowtempFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setDischargeLowtempFaultLogId(null);
                        break;
                    case 9:
//                        if (battery.getChargeOvercurrentFaultLogId() != null)
//                            faultLogMapper.handle(battery.getChargeOvercurrentFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
//
//                        battery.setChargeOvercurrentFaultLogId(null);
                        break;
                    case 10:
//                        if (battery.getDischargeOvercurrentFaultLogId() != null)
//                            faultLogMapper.handle(battery.getDischargeOvercurrentFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());
//
//                        battery.setDischargeOvercurrentFaultLogId(null);
                        break;
                    case 11:
                        if (battery.getShortCircuitFaultLogId() != null)
                            faultLogMapper.handle(battery.getShortCircuitFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setShortCircuitFaultLogId(null);
                        break;
                    case 12:
                        if (battery.getTestingIcFaultLogId() != null)
                            faultLogMapper.handle(battery.getTestingIcFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setTestingIcFaultLogId(null);
                        break;
                    case 13:
                        if (battery.getSoftwareLockingFaultLogId() != null)
                            faultLogMapper.handle(battery.getSoftwareLockingFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setSoftwareLockingFaultLogId(null);
                        break;
                    case 14:
                        if (battery.getDischargeLockingFaultLogId() != null)
                            faultLogMapper.handle(battery.getDischargeLockingFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setDischargeLockingFaultLogId(null);
                        break;
                    case 15:
                        if (battery.getChargeMosFaultLogId() != null)
                            faultLogMapper.handle(battery.getChargeMosFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setChargeMosFaultLogId(null);
                        break;
                    case 16:
                        if (battery.getDischargeMosFaultLogId() != null)
                            faultLogMapper.handle(battery.getDischargeMosFaultLogId(), FaultLog.HandleType.SYSTEM.getValue(), null, now, null, FaultLog.Status.PROCESSED.getValue());

                        battery.setDischargeMosFaultLogId(null);
                        break;
                    default:
                        break;
                }
            }
        }

        //异常电池处理
        if (battery.getUpLineStatus() == ConstEnum.Flag.TRUE.getValue() && battery.getIsNormal() == ConstEnum.Flag.TRUE.getValue()) {
            boolean push = false;
            String message = "";
            if (battery.getAgentId() != cabinet.getAgentId().intValue() && cabinet.getAgentId() != Constant.TEST_AGENT_ID) {
                battery.setIsNormal(ConstEnum.Flag.FALSE.getValue());
                battery.setAbnormalCause("心跳检测：运营商不一致");
                battery.setOperator("心跳");
                battery.setOperatorTime(now);
                push = true;
                message = "心跳检测：运营商不一致";

            } else if (shopStoreVehicleBatteryMapper.existBattery(battery.getId()) > 0) {
                battery.setIsNormal(ConstEnum.Flag.FALSE.getValue());
                battery.setAbnormalCause("心跳检测：租车库存电池无法入柜");
                battery.setOperator("心跳");
                battery.setOperatorTime(now);
                push = true;
                message = "心跳检测：租车库存电池无法入柜";
            }

            if (push) {
                PushMetaData pushMetaData = new PushMetaData();
                pushMetaData.setSourceType(PushMessage.SourceType.FAULT_FLAG_BATTERY.getValue());
                pushMetaData.setSourceId(battery.getId());
                pushMetaData.setCreateTime(now);
                if (log.isDebugEnabled()) {
                    log.debug("battery: {} IMEI:{}  ", battery.getId(), battery.getCode());
                }
                pushMetaDataMapper.insert(pushMetaData);

            }
        } else if (battery.getUpLineStatus() == ConstEnum.Flag.TRUE.getValue() && battery.getIsNormal() == ConstEnum.Flag.FALSE.getValue()) {
            if (battery.getAgentId() == cabinet.getAgentId().intValue() && battery.getAbnormalCause().indexOf("运营商不一致") > -1) {
                battery.setIsNormal(ConstEnum.Flag.TRUE.getValue());
                battery.setAbnormalCause(null);
                battery.setOperator(null);
                battery.setOperatorTime(null);
            }
        }

        //保存电池数据
        batteryMapper.update(battery);
    }

    /**
     * 电池日志插入
     *
     * @param batteryReport
     */
    private void insertBatteryReport(BatteryReport batteryReport, Date date) {
        try{
            String tableName = String.format("%s%s", BatteryReport.BATTERY_REPORT_TABLE_NAME, batteryReport.getSuffix());
            boolean result = findTable(tableName);
            if (!result) {
                batteryReportMapper.createTable(tableName);
            }
            batteryReport.setCreateTime(new Date());
            batteryReportMapper.insert(batteryReport);
            String reportDate = DateFormatUtils.format(date, Constant.DATE_FORMAT);
            if (batteryReportDateMapper.update(reportDate, batteryReport.getBatteryId()) == 0) {
                batteryReportDateMapper.create(reportDate, batteryReport.getBatteryId());
            }
        }catch (Exception e){
            log.error(String.format("电池日志插入 保存 error=%s", e.getMessage()));
        }
    }


    //这里不要加事务 由调用方法加事务
    public void handleLaxinCustomerByMonth(Agent agent, Customer customer, int packetPeriodMoney) {

        //客户买好押金后 处理拉新记录
        LaxinCustomer laxinCustomer = laxinCustomerMapper.findByTargetMobile(customer.getMobile());
        if (laxinCustomer != null
                && agent.getId().equals(laxinCustomer.getAgentId())
                && laxinCustomer.getForegiftTime() != null
                && laxinCustomer.getIncomeType() != null
                && laxinCustomer.getIncomeType() == Laxin.IncomeType.MONTH.getValue()
                && laxinCustomer.getPacketPeriodMonth() != null
                && laxinCustomer.getPacketPeriodMonth() > 0
                && laxinCustomer.getPacketPeriodMoney() != null
                && laxinCustomer.getPacketPeriodMoney() > 0
                && laxinCustomer.getPacketPeriodExpireTime() != null
                && laxinCustomer.getPacketPeriodExpireTime().getTime() >= System.currentTimeMillis()) {

            LaxinRecord laxinRecord = new LaxinRecord();
            laxinRecord.setId(newOrderId(OrderId.OrderIdType.LAXIN_RECORD));
            laxinRecord.setAgentId(agent.getId());
            laxinRecord.setAgentName(agent.getAgentName());
            laxinRecord.setAgentCode(agent.getAgentCode());
            laxinRecord.setLaxinId(laxinCustomer.getLaxinId());
            laxinRecord.setLaxinMobile(laxinCustomer.getLaxinMobile());
            laxinRecord.setLaxinMoney(laxinCustomer.getPacketPeriodMoney());
            laxinRecord.setTargetCustomerId(customer.getId());
            laxinRecord.setTargetMobile(customer.getMobile());
            laxinRecord.setTargetFullname(customer.getFullname());
            laxinRecord.setStatus(LaxinRecord.Status.WAIT.getValue());
            laxinRecord.setIncomeType(laxinCustomer.getIncomeType());
            laxinRecord.setForegiftMoney(0);
            laxinRecord.setPacketPeriodMoney(packetPeriodMoney);
            laxinRecord.setCreateTime(new Date());
            laxinRecordMapper.insert(laxinRecord);
        }
    }


    public void handleCabinetBatteryStats( Cabinet cabinet, int batteryNum, String boxBatteryMessage, String boxMessage) {
        CabinetBatteryStats cabinetBatteryStats = cabinetBatteryStatsMapper.findBefore(cabinet.getId());
        if(cabinetBatteryStats != null && cabinetBatteryStats.getBoxBatteryMessage().equals(boxBatteryMessage)){
            return;
        }
        cabinetBatteryStats = new CabinetBatteryStats();
        cabinetBatteryStats.setCabinetId(cabinet.getId());
        cabinetBatteryStats.setCabinetName(cabinet.getCabinetName());
        cabinetBatteryStats.setAgentId(cabinet.getAgentId());
        Agent agent = agentMapper.find(cabinet.getAgentId());
        cabinetBatteryStats.setAgentName(agent.getAgentName());
        cabinetBatteryStats.setBatteryNum(batteryNum);
        cabinetBatteryStats.setBoxBatteryMessage(boxBatteryMessage);
        cabinetBatteryStats.setBoxMessage(boxMessage);
        if(StringUtils.isNotEmpty(cabinetBatteryStats.getBoxMessage())
              && cabinetBatteryStats.getBoxMessage().indexOf("维护") > -1  ){
            cabinetBatteryStats.setStatus(CabinetBatteryStats.Status.FALSE.getValue());
        }else{
            cabinetBatteryStats.setStatus(CabinetBatteryStats.Status.TRUE.getValue());
        }
        cabinetBatteryStats.setCreateTime(new Date());
        cabinetBatteryStatsMapper.insert(cabinetBatteryStats);
    }

//    private void batteryReportAndParameter(Date date, Battery battery) {
//        //电池上报
//        BatteryReport batteryReport = new BatteryReport();
//        batteryReport.setHeartType(BatteryReport.HeartType.CABINET_HEART.getValue());
//        batteryReport.setType(battery.getType());
//        batteryReport.setBatteryId(battery.getId());
//        batteryReport.setVoltage(battery.getVoltage());
//        batteryReport.setElectricity(battery.getElectricity());
//        batteryReport.setVolume(battery.getVolume());
//        batteryReport.setCurrentCapacity(battery.getCurrentCapacity() );
//        batteryReport.setTotalCapacity(battery.getTotalCapacity());
//        batteryReport.setFault(battery.getProtectState());/*保护状态*/
//        batteryReport.setMos(battery.getFet());  /*bit0表示充电，bit1表示放电，0表示MOS关闭，1表示打开*/
//        batteryReport.setSerials(battery.getStrand()); /*电池串数*/
//        batteryReport.setTemp(battery.getTemp());
//        batteryReport.setLng(battery.getLng());
//        batteryReport.setLat(battery.getLat());
//        batteryReport.setDistance(null);
//        batteryReport.setCurrentSignal(battery.getCurrentSignal());
//        batteryReport.setIsMotion(Battery.PositionState.NOT_MOVE.getValue());/*0表示位置不移动 1表示位置移动中 2表示通电中*/
//        batteryReport.setSimCode(null);
//        batteryReport.setSingleVoltage(battery.getSingleVoltage());
//        batteryReport.setSuffix(getTableSuffix(date));
//        batteryReport.setCode(battery.getCode());
//        //insertBatteryReport(batteryReport, date);
//
//
//        //电池参数
//        BatteryParameter batteryParameter = batteryParameterMapper.find(battery.getId());
//        if (batteryParameter == null) {
//            batteryParameter = batteryReport;
//            batteryParameter.setId(batteryReport.getBatteryId());
//            batteryParameter.setCreateTime(new Date());
//            batteryParameterMapper.insert(batteryParameter);
//        } else {
//            //保存同步过来的参数信息
//            batteryParameter = batteryReport;
//            batteryParameter.setId(batteryReport.getBatteryId());
//            batteryParameterMapper.update(batteryParameter);
//        }
//    }


    /**
     * 智租放电池判断
     * @param customerId
     * @param batteryOrder
     */
    private void customerPutForIndependent(final Long customerId, final BatteryOrder batteryOrder) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String URL = "https://exchange-api.zhizukj.com/zhizu/exchange/api/exchange_battery_order/check_battery.json";

                Map<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");

                Map params = new HashMap();
                params.put("customerMobile", batteryOrder.getCustomerMobile());
                params.put("cabinetId", batteryOrder.getPutCabinetId());

                OkHttpClientUtils.HttpResp httpResp = null;
                try {
                    httpResp = OkHttpClientUtils.post(URL, YhdgUtils.encodeJson(params), header);
                    if (httpResp != null && httpResp.status == 200) {
                        Map map = (Map) YhdgUtils.decodeJson(httpResp.content, Map.class);
                        if ((Integer) map.get("code") == 0) {
                            //逻辑处理
                            customerExchangeInfoMapper.updateErrorMessage(customerId, null, null);
                            customerExchangeBatteryMapper.clearBattery(batteryOrder.getCustomerId(), batteryOrder.getBatteryId());
                            batteryMapper.clearCustomer(batteryOrder.getBatteryId(), Battery.Status.IN_BOX.getValue());
                            batteryOrderMapper.payOk(batteryOrder.getId(), BatteryOrder.OrderStatus.PAY.getValue(),
                                    ConstEnum.PayType.PLATFORM.getValue(),
                                    new Date(), null,
                                    0, 0);

                            PushOrderMessage pushOrderMessage = new PushOrderMessage();
                            pushOrderMessage.setAgentId(batteryOrder.getAgentId());
                            pushOrderMessage.setSourceType(PushOrderMessage.SourceType.PUT.getValue());
                            pushOrderMessage.setSourceId(batteryOrder.getId());
                            pushOrderMessage.setSendStatus(PushOrderMessage.SendStatus.NOT.getValue());
                            pushOrderMessage.setCreateTime(new Date());
                            pushOrderMessageMapper.insert(pushOrderMessage);

                            ChannelHandlerContext context = config.sessionManager.getStaticServerClient();
                            if (context != null) {
                                Msg231000001 msg = new Msg231000001();
                                msg.cabinetId = batteryOrder.getPutCabinetId();
                                msg.boxNum = batteryOrder.getPutBoxNum();
                                msg.customerId = customerId;
                                msg.setSerial(config.sequence.incrementAndGet());
                                context.writeAndFlush(msg);
                                if (log.isDebugEnabled()) {
                                    log.debug("cabinet {} send message {} to static-server", batteryOrder.getPutCabinetId(), msg);
                                }

                            } else {
                                if (log.isDebugEnabled()) {
                                    log.debug("static-server not connect cabinet {}", batteryOrder.getPutCabinetId());
                                }
                                customerExchangeInfoMapper.updateErrorMessage(customerId, new Date(), String.format("柜子[%s]没有连接静态服务器", batteryOrder.getPutCabinetId()));
                            }

                            //旧箱门改为电池已入箱
                            //cabinetBoxMapper.updateBoxStatus(batteryOrder.getPutCabinetId(), batteryOrder.getPutBoxNum(), CabinetBox.BoxStatus.FULL.getValue());
                        }

                    }
                } catch (Exception e) {
                    log.error("智租开箱异常：", e);
                }
            }
        };
        Task task = new Task(runnable, getClass().getSimpleName());
        config.taskExecutorService.submit(task);
    }
}
