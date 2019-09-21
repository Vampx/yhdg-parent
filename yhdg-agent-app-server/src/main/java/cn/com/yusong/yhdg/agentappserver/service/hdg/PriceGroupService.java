package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.PriceGroupController;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class PriceGroupService extends AbstractService {
    @Autowired
    ExchangePriceTimeMapper exchangePriceTimeMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    DictItemMapper dictItemMapper;
    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    PacketPeriodPriceRenewMapper packetPeriodPriceRenewMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    InsuranceMapper insuranceMapper;
    @Autowired
    CabinetBatteryTypeMapper cabinetBatteryTypeMapper;
    @Autowired
    StationBatteryTypeMapper stationBatteryTypeMapper;
    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;
    @Autowired
    CustomerForegiftOrderMapper customerForegiftOrderMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    ExchangeInstallmentDetailMapper exchangeInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    StationMapper stationMapper;

    @Transactional(rollbackFor = Throwable.class)
    public RestResult list(Integer agentId) {

        List<AgentBatteryType> list = agentBatteryTypeMapper.findListByAgentId(agentId);
        List<Map> result = new ArrayList<Map>();
        for (AgentBatteryType agentBatteryType : list) {
            NotNullMap data = new NotNullMap();

            SystemBatteryType systemBatteryType = systemBatteryTypeMapper.find(agentBatteryType.getBatteryType());
            data.putInteger("batteryType", agentBatteryType.getBatteryType());
            data.putString("batteryTypeName", agentBatteryType.getTypeName());
            data.putInteger("ratedVoltage", systemBatteryType.getRatedVoltage());
            data.putInteger("ratedCapacity", systemBatteryType.getRatedCapacity());

            ExchangePriceTime exchangePriceTime = exchangePriceTimeMapper.find(agentId, agentBatteryType.getBatteryType());
            if (exchangePriceTime != null && exchangePriceTime.getActiveSingleExchange() != null
                    && exchangePriceTime.getActiveSingleExchange() == ExchangePriceTime.ActiveType.TIMES.getValue()) {
                data.putInteger("isActive", exchangePriceTime.getActiveSingleExchange());
                data.putInteger("timesPrice", exchangePriceTime.getTimesPrice());
            } else {
                data.putInteger("isActive", ConstEnum.Flag.FALSE.getValue());
                data.putInteger("timesPrice",ConstEnum.Flag.FALSE.getValue());
            }

            List<CabinetBatteryType> cabinetBatteryTypeList = cabinetBatteryTypeMapper.findListByBatteryType(agentBatteryType.getBatteryType(), agentId);

            List<NotNullMap> priceCabinets = new ArrayList<NotNullMap>();
            for (CabinetBatteryType cabinetBatteryType : cabinetBatteryTypeList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.put("batteryType", cabinetBatteryType.getBatteryType());
                notNullMap.put("cabinetId", cabinetBatteryType.getCabinetId());
                notNullMap.put("cabinetName", cabinetMapper.find(cabinetBatteryType.getCabinetId()).getCabinetName());
                priceCabinets.add(notNullMap);
            }
            data.put("cabinetIdList", priceCabinets);

            List<StationBatteryType> stationBatteryTypeList = stationBatteryTypeMapper.findListByBatteryType(agentBatteryType.getBatteryType(), agentId);

            List<NotNullMap> priceStations = new ArrayList<NotNullMap>();
            for (StationBatteryType stationBatteryType : stationBatteryTypeList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.put("batteryType", stationBatteryType.getBatteryType());
                notNullMap.put("stationId", stationBatteryType.getStationId());
                notNullMap.put("stationName", stationMapper.find(stationBatteryType.getStationId()).getStationName());
                priceStations.add(notNullMap);
            }
            data.put("stationIdList", priceStations);


            List<NotNullMap> packetPeriodList = new ArrayList<NotNullMap>();
            //biza
            List<ExchangeBatteryForegift> bizaForegiftList = exchangeBatteryForegiftMapper.findList(agentBatteryType.getBatteryType(), agentId);


            for (ExchangeBatteryForegift bizaForegift : bizaForegiftList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.putLong("foregiftId", bizaForegift.getId());
                notNullMap.putInteger("foregift", bizaForegift.getMoney());
                notNullMap.putString("memo", bizaForegift.getMemo());

                List<PacketPeriodPrice> bizaPacketPeriodPriceList = packetPeriodPriceMapper.findListByBatteryType(bizaForegift.getId(), bizaForegift.getBatteryType(), agentId);

                List<NotNullMap> bizaPriceList = new ArrayList<NotNullMap>();
                for (PacketPeriodPrice bizaPacketPeriodPrice : bizaPacketPeriodPriceList) {
                    NotNullMap notNullMap2 = new NotNullMap();

                    notNullMap2.put("priceId", bizaPacketPeriodPrice.getId());
                    notNullMap2.put("dayCount", bizaPacketPeriodPrice.getDayCount());
                    notNullMap2.put("price", bizaPacketPeriodPrice.getPrice());
                    notNullMap2.put("memo", bizaPacketPeriodPrice.getMemo());
                    notNullMap2.put("isTicket", bizaPacketPeriodPrice.getIsTicket());
                    notNullMap2.put("limitCount", bizaPacketPeriodPrice.getLimitCount());
                    notNullMap2.put("dayLimitCount", bizaPacketPeriodPrice.getDayLimitCount());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(bizaPacketPeriodPrice.getCreateTime());
                    calendar.add(Calendar.DAY_OF_MONTH, bizaPacketPeriodPrice.getDayCount());
                    String date = DateFormatUtils.format(calendar.getTime(), Constant.DATE_FORMAT);
                    notNullMap2.put("endDate", date);

                    List<PacketPeriodPriceRenew> packetPeriodPriceRenewList = packetPeriodPriceRenewMapper.findList(bizaPacketPeriodPrice.getId());

                    List<NotNullMap> priceListRenew = new ArrayList<NotNullMap>();
                    for (PacketPeriodPriceRenew packetPeriodPriceRenew : packetPeriodPriceRenewList) {
                        NotNullMap notNullMap3 = new NotNullMap();

                        notNullMap3.put("renewPriceId", packetPeriodPriceRenew.getId());
                        notNullMap3.put("dayCount", packetPeriodPriceRenew.getDayCount());
                        notNullMap3.put("price", packetPeriodPriceRenew.getPrice());
                        notNullMap3.put("memo", packetPeriodPriceRenew.getMemo());
                        notNullMap3.put("isTicket", packetPeriodPriceRenew.getIsTicket());
                        notNullMap3.put("limitCount", packetPeriodPriceRenew.getLimitCount());
                        notNullMap3.put("dayLimitCount", packetPeriodPriceRenew.getDayLimitCount());

                        priceListRenew.add(notNullMap3);
                    }

                    notNullMap2.put("priceListRenew", priceListRenew);

                    bizaPriceList.add(notNullMap2);
                }
                notNullMap.put("priceList", bizaPriceList);

                packetPeriodList.add(notNullMap);
            }

            data.put("packetPeriodList", packetPeriodList);
            result.add(data);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);

    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult detail(Integer batteryType, Integer agentId) {
        AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(batteryType, agentId);

        NotNullMap data = new NotNullMap();
        if (agentBatteryType == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在!");
        }

        SystemBatteryType systemBatteryType = systemBatteryTypeMapper.find(batteryType);
        data.putInteger("id", systemBatteryType.getId());
        ExchangePriceTime exchangePriceTime = exchangePriceTimeMapper.find(agentId, batteryType);
        if (exchangePriceTime != null && exchangePriceTime.getActiveSingleExchange() == ExchangePriceTime.ActiveType.TIMES.getValue()) {
            data.putInteger("isActive", exchangePriceTime.getActiveSingleExchange());
            data.putInteger("timesPrice", exchangePriceTime.getTimesPrice());
        } else {
            data.putInteger("isActive", ConstEnum.Flag.FALSE.getValue());
            data.putInteger("timesPrice",ConstEnum.Flag.FALSE.getValue());
        }

        data.putInteger("batteryType", agentBatteryType.getBatteryType());
        data.putString("batteryTypeName", agentBatteryType.getTypeName());
        data.putInteger("ratedVoltage", agentBatteryType.getRatedVoltage());
        data.putInteger("ratedCapacity", agentBatteryType.getRatedCapacity());

        List<CabinetBatteryType> cabinetBatteryTypeList = cabinetBatteryTypeMapper.findListByBatteryType(agentBatteryType.getBatteryType(), agentId);

        List<NotNullMap> priceCabinets = new ArrayList<NotNullMap>();
        for (CabinetBatteryType cabinetBatteryType : cabinetBatteryTypeList) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.put("batteryType", cabinetBatteryType.getBatteryType());
            notNullMap.put("cabinetId", cabinetBatteryType.getCabinetId());
            notNullMap.put("cabinetName", cabinetMapper.find(cabinetBatteryType.getCabinetId()).getCabinetName());
            priceCabinets.add(notNullMap);
        }
        data.put("cabinetIdList", priceCabinets);

        List<StationBatteryType> stationBatteryTypeList = stationBatteryTypeMapper.findListByBatteryType(agentBatteryType.getBatteryType(), agentId);

        List<NotNullMap> priceStations = new ArrayList<NotNullMap>();
        for (StationBatteryType stationBatteryType : stationBatteryTypeList) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.put("batteryType", stationBatteryType.getBatteryType());
            notNullMap.put("stationId", stationBatteryType.getStationId());
            notNullMap.put("stationName", stationMapper.find(stationBatteryType.getStationId()).getStationName());
            priceStations.add(notNullMap);
        }
        data.put("stationIdList", priceStations);

        //biza
        List<ExchangeBatteryForegift> bizaForegiftList = exchangeBatteryForegiftMapper.findList(batteryType, agentId);
        List<NotNullMap> packetPeriodList = new ArrayList<NotNullMap>();

        for (ExchangeBatteryForegift bizaForegift : bizaForegiftList) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.putLong("foregiftId", bizaForegift.getId());
            notNullMap.putInteger("foregift", bizaForegift.getMoney());
            notNullMap.putString("memo", bizaForegift.getMemo());

            List<PacketPeriodPrice> bizaPacketPeriodPriceList = packetPeriodPriceMapper.findListByBatteryType(bizaForegift.getId(), bizaForegift.getBatteryType(), agentId);
            List<NotNullMap> bizaPriceList = new ArrayList<NotNullMap>();
            for (PacketPeriodPrice bizaPacketPeriodPrice : bizaPacketPeriodPriceList) {
                NotNullMap notNullMap2 = new NotNullMap();

                notNullMap2.put("priceId", bizaPacketPeriodPrice.getId());
                notNullMap2.put("dayCount", bizaPacketPeriodPrice.getDayCount());
                notNullMap2.put("price", bizaPacketPeriodPrice.getPrice());
                notNullMap2.put("memo", bizaPacketPeriodPrice.getMemo());
                notNullMap2.put("isTicket", bizaPacketPeriodPrice.getIsTicket());
                notNullMap2.put("limitCount", bizaPacketPeriodPrice.getLimitCount());
                notNullMap2.put("dayLimitCount", bizaPacketPeriodPrice.getDayLimitCount());

                List<PacketPeriodPriceRenew> packetPeriodPriceRenewList = packetPeriodPriceRenewMapper.findList(bizaPacketPeriodPrice.getId());

                List<NotNullMap> priceListRenew = new ArrayList<NotNullMap>();
                for (PacketPeriodPriceRenew packetPeriodPriceRenew : packetPeriodPriceRenewList) {
                    NotNullMap notNullMap3 = new NotNullMap();

                    notNullMap3.put("renewPriceId", packetPeriodPriceRenew.getId());
                    notNullMap3.put("dayCount", packetPeriodPriceRenew.getDayCount());
                    notNullMap3.put("price", packetPeriodPriceRenew.getPrice());
                    notNullMap3.put("memo", packetPeriodPriceRenew.getMemo());
                    notNullMap3.put("isTicket", packetPeriodPriceRenew.getIsTicket());
                    notNullMap3.put("limitCount", packetPeriodPriceRenew.getLimitCount());
                    notNullMap3.put("dayLimitCount", packetPeriodPriceRenew.getDayLimitCount());

                    priceListRenew.add(notNullMap3);
                }

                notNullMap2.put("priceListRenew", priceListRenew);

                bizaPriceList.add(notNullMap2);
            }
            notNullMap.put("priceList", bizaPriceList);

            packetPeriodList.add(notNullMap);
        }

        data.put("packetPeriodList", packetPeriodList);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult create(AgentBatteryType agentBatteryType,
                             Integer agentId,Integer isActive,Integer timesPrice,
                             PriceGroupController.CreateParam.GroupBiza[] packetPeriodList,
                             String[] cabinetIdList, String[] stationIdList) {

        agentBatteryTypeMapper.insert(agentBatteryType);

        if (isActive == ExchangePriceTime.ActiveType.TIMES.getValue()) {
            ExchangePriceTime exchangePriceTime = new ExchangePriceTime();
            exchangePriceTime.setAgentId(agentId);
            exchangePriceTime.setBatteryType(agentBatteryType.getBatteryType());
            exchangePriceTime.setActiveSingleExchange(ExchangePriceTime.ActiveType.TIMES.getValue());
            exchangePriceTime.setTimesPrice(timesPrice);
            exchangePriceTimeMapper.insert(exchangePriceTime);
        }

        //biza
        if (packetPeriodList != null) {

            for (int i = 0; i < packetPeriodList.length; i++) {

                PriceGroupController.CreateParam.GroupBiza detail = packetPeriodList[i];
                ExchangeBatteryForegift bizaForegift = new ExchangeBatteryForegift();
                bizaForegift.setBatteryType(agentBatteryType.getBatteryType());
                bizaForegift.setMoney(detail.foregift);
                bizaForegift.setMemo(detail.memo);
                bizaForegift.setAgentId(agentId);
                exchangeBatteryForegiftMapper.insert(bizaForegift);

                for (int j = 0; j < packetPeriodList[i].priceList.length; j++) {
                    PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice bizaPrice = packetPeriodList[i].priceList[j];
                    PacketPeriodPrice bizaPacketPeriodPrice = new PacketPeriodPrice();
                    //首次
                    bizaPacketPeriodPrice.setForegiftId(bizaForegift.getId());
                    bizaPacketPeriodPrice.setDayCount(bizaPrice.dayCount);
                    bizaPacketPeriodPrice.setPrice(bizaPrice.price);
                    bizaPacketPeriodPrice.setMemo(bizaPrice.memo);
                    bizaPacketPeriodPrice.setAgentId(agentId);
                    bizaPacketPeriodPrice.setIsTicket(bizaPrice.isTicket);
                    bizaPacketPeriodPrice.setAgentName(agentMapper.find(agentId).getAgentName());
                    bizaPacketPeriodPrice.setBatteryType(bizaForegift.getBatteryType());
                    bizaPacketPeriodPrice.setLimitCount(bizaPrice.limitCount);
                    bizaPacketPeriodPrice.setDayLimitCount(bizaPrice.dayLimitCount);
                    bizaPacketPeriodPrice.setCreateTime(new Date());
                    packetPeriodPriceMapper.insert(bizaPacketPeriodPrice);


                    for (int k = 0; k < packetPeriodList[i].priceList[j].priceListRenew.length; k++) {
                        PriceGroupController.CreateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew bizaPriceRenew = packetPeriodList[i].priceList[j].priceListRenew[k];
                        PacketPeriodPriceRenew bizaPacketPeriodPriceRenew = new PacketPeriodPriceRenew();
                        //续费
                        bizaPacketPeriodPriceRenew.setPriceId(bizaPacketPeriodPrice.getId());
                        bizaPacketPeriodPriceRenew.setForegiftId(bizaForegift.getId());
                        bizaPacketPeriodPriceRenew.setDayCount(bizaPriceRenew.dayCount);
                        bizaPacketPeriodPriceRenew.setPrice(bizaPriceRenew.price);
                        bizaPacketPeriodPriceRenew.setMemo(bizaPriceRenew.memo);
                        bizaPacketPeriodPriceRenew.setAgentId(agentId);
                        bizaPacketPeriodPriceRenew.setIsTicket(bizaPriceRenew.isTicket);
                        bizaPacketPeriodPriceRenew.setAgentName(agentMapper.find(agentId).getAgentName());
                        bizaPacketPeriodPriceRenew.setBatteryType(bizaForegift.getBatteryType());
                        bizaPacketPeriodPriceRenew.setLimitCount(bizaPriceRenew.limitCount);
                        bizaPacketPeriodPriceRenew.setDayLimitCount(bizaPriceRenew.dayLimitCount);
                        bizaPacketPeriodPriceRenew.setCreateTime(new Date());
                        packetPeriodPriceRenewMapper.insert(bizaPacketPeriodPriceRenew);

                    }

                }

            }
        }

        if (cabinetIdList.length > 0) {
            for (String cabinetId : cabinetIdList) {
                CabinetBatteryType cct = new CabinetBatteryType();
                cct.setBatteryType(agentBatteryType.getBatteryType());
                cct.setCabinetId(cabinetId);
                cabinetBatteryTypeMapper.insert(cct);
            }
        }

        if (stationIdList.length > 0) {
            for (String stationId : stationIdList) {
                StationBatteryType cct = new StationBatteryType();
                cct.setBatteryType(agentBatteryType.getBatteryType());
                cct.setStationId(stationId);
                stationBatteryTypeMapper.insert(cct);
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);

    }


    @Transactional(rollbackFor=Throwable.class)
    public RestResult update(AgentBatteryType agentBatteryType,
                             Integer agentId,Integer isActive,Integer timesPrice,
                             PriceGroupController.UpdateParam.GroupBiza[] packetPeriodList,
                             String[] cabinetIdList, String[] stationIdList) {

        if (agentBatteryTypeMapper.find(agentBatteryType.getBatteryType(), agentId) != null) {
            agentBatteryTypeMapper.delete(agentBatteryType.getBatteryType(), agentId);
        }

        agentBatteryTypeMapper.insert(agentBatteryType);

        if (isActive == ConstEnum.Flag.TRUE.getValue()) {
            exchangePriceTimeMapper.delete(agentId, agentBatteryType.getBatteryType());

            ExchangePriceTime exchangePriceTime = new ExchangePriceTime();
            exchangePriceTime.setAgentId(agentId);
            exchangePriceTime.setBatteryType(agentBatteryType.getBatteryType());
            exchangePriceTime.setActiveSingleExchange(ExchangePriceTime.ActiveType.TIMES.getValue());
            exchangePriceTime.setTimesPrice(timesPrice);
            exchangePriceTimeMapper.insert(exchangePriceTime);
        } else {
            exchangePriceTimeMapper.delete(agentId, agentBatteryType.getBatteryType());
        }

        //biza
        if (packetPeriodList != null) {

            for (int i = 0; i < packetPeriodList.length; i++) {
                PriceGroupController.UpdateParam.GroupBiza detail = packetPeriodList[i];
                ExchangeBatteryForegift bizaForegift = new ExchangeBatteryForegift();
                if (detail.foregiftId == null) {
                    bizaForegift.setBatteryType(agentBatteryType.getBatteryType());
                    bizaForegift.setMoney(detail.foregift);
                    bizaForegift.setAgentId(agentId);
                    bizaForegift.setMemo(detail.memo);
                    exchangeBatteryForegiftMapper.insert(bizaForegift);
                } else {
                    bizaForegift.setBatteryType(agentBatteryType.getBatteryType());
                    bizaForegift.setId(detail.foregiftId);
                    bizaForegift.setMoney(detail.foregift);
                    bizaForegift.setAgentId(agentId);
                    bizaForegift.setMemo(detail.memo);
                    exchangeBatteryForegiftMapper.update(bizaForegift);
                }

                for (int j = 0; j < packetPeriodList[i].priceList.length; j++) {
                    PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice bizaPrice = packetPeriodList[i].priceList[j];
                    PacketPeriodPrice bizaPacketPeriodPrice = new PacketPeriodPrice();

                    if (bizaPrice.priceId == null) {
                        bizaPacketPeriodPrice.setForegiftId(bizaForegift.getId());
                        bizaPacketPeriodPrice.setDayCount(bizaPrice.dayCount);
                        bizaPacketPeriodPrice.setPrice(bizaPrice.price);
                        bizaPacketPeriodPrice.setMemo(bizaPrice.memo);
                        bizaPacketPeriodPrice.setAgentId(agentId);
                        bizaPacketPeriodPrice.setIsTicket(bizaPrice.isTicket);
                        bizaPacketPeriodPrice.setAgentName(agentMapper.find(agentId).getAgentName());
                        bizaPacketPeriodPrice.setBatteryType(bizaForegift.getBatteryType());
                        bizaPacketPeriodPrice.setLimitCount(bizaPrice.limitCount);
                        bizaPacketPeriodPrice.setDayLimitCount(bizaPrice.dayLimitCount);
                        bizaPacketPeriodPrice.setCreateTime(new Date());
                        packetPeriodPriceMapper.insert(bizaPacketPeriodPrice);
                    } else {
                        bizaPacketPeriodPrice.setId(bizaPrice.priceId);
                        bizaPacketPeriodPrice.setDayCount(bizaPrice.dayCount);
                        bizaPacketPeriodPrice.setPrice(bizaPrice.price);
                        bizaPacketPeriodPrice.setMemo(bizaPrice.memo);
                        bizaPacketPeriodPrice.setIsTicket(bizaPrice.isTicket);
                        bizaPacketPeriodPrice.setLimitCount(bizaPrice.limitCount);
                        bizaPacketPeriodPrice.setDayLimitCount(bizaPrice.dayLimitCount);
                        packetPeriodPriceMapper.update(bizaPacketPeriodPrice);
                    }


                    for (int k = 0; k < packetPeriodList[i].priceList[j].priceListRenew.length; k++) {
                        PriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice.GroupBizaPriceRenew bizaPriceRenew = packetPeriodList[i].priceList[j].priceListRenew[k];
                        PacketPeriodPriceRenew bizaPacketPeriodPriceRenew = new PacketPeriodPriceRenew();

                        if (bizaPriceRenew.renewPriceId == null) {
                            bizaPacketPeriodPriceRenew.setPriceId(bizaPacketPeriodPrice.getId());
                            bizaPacketPeriodPriceRenew.setForegiftId(bizaForegift.getId());
                            bizaPacketPeriodPriceRenew.setDayCount(bizaPriceRenew.dayCount);
                            bizaPacketPeriodPriceRenew.setPrice(bizaPriceRenew.price);
                            bizaPacketPeriodPriceRenew.setMemo(bizaPriceRenew.memo);
                            bizaPacketPeriodPriceRenew.setAgentId(agentId);
                            bizaPacketPeriodPriceRenew.setIsTicket(bizaPriceRenew.isTicket);
                            bizaPacketPeriodPriceRenew.setAgentName(agentMapper.find(agentId).getAgentName());
                            bizaPacketPeriodPriceRenew.setBatteryType(bizaForegift.getBatteryType());
                            bizaPacketPeriodPriceRenew.setLimitCount(bizaPriceRenew.limitCount);
                            bizaPacketPeriodPriceRenew.setDayLimitCount(bizaPriceRenew.dayLimitCount);
                            bizaPacketPeriodPriceRenew.setCreateTime(new Date());
                            packetPeriodPriceRenewMapper.insert(bizaPacketPeriodPriceRenew);
                        } else {
                            bizaPacketPeriodPriceRenew.setId(bizaPriceRenew.renewPriceId);
                            bizaPacketPeriodPriceRenew.setDayCount(bizaPriceRenew.dayCount);
                            bizaPacketPeriodPriceRenew.setPrice(bizaPriceRenew.price);
                            bizaPacketPeriodPriceRenew.setMemo(bizaPriceRenew.memo);
                            bizaPacketPeriodPriceRenew.setIsTicket(bizaPriceRenew.isTicket);
                            bizaPacketPeriodPriceRenew.setLimitCount(bizaPriceRenew.limitCount);
                            bizaPacketPeriodPriceRenew.setDayLimitCount(bizaPriceRenew.dayLimitCount);
                            packetPeriodPriceRenewMapper.update(bizaPacketPeriodPriceRenew);
                        }
                    }

                }
            }
        }

        if (cabinetIdList.length > 0) {

            Set<String> cabinetSet = new HashSet<String>();
            for (String cabinetId : cabinetIdList) {
                cabinetSet.add(cabinetId);
            }

            if (cabinetIdList.length > 0) {
                for (String cabinetId : cabinetSet) {
                    CabinetBatteryType cct = new CabinetBatteryType();
                    cct.setBatteryType(agentBatteryType.getBatteryType());
                    cct.setCabinetId(cabinetId);
                    cabinetBatteryTypeMapper.insert(cct);
                }
            }
        }

        if (stationIdList.length > 0) {

            Set<String> stationSet = new HashSet<String>();
            for (String stationId : stationIdList) {
                stationSet.add(stationId);
            }

            if (stationIdList.length > 0) {
                for (String stationId : stationSet) {
                    StationBatteryType cct = new StationBatteryType();
                    cct.setBatteryType(agentBatteryType.getBatteryType());
                    cct.setStationId(stationId);
                    stationBatteryTypeMapper.insert(cct);
                }
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult batteryForegiftDelete(Long foregiftId) {
        ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.find(foregiftId);
        //无法删除的押金状态
        List<Integer> foregiftList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(),
                CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        List<CustomerForegiftOrder> customerForegiftOrderList = customerForegiftOrderMapper.findByForegiftIdAndStatus(exchangeBatteryForegift.getId(), foregiftList);
        if (customerForegiftOrderList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"存在使用中的押金，无法删除",null);
        }
        //删除押金对应的分期设置
        List<ExchangeInstallmentSetting> exchangeInstallmentSettingList = exchangeInstallmentSettingMapper.findByForegiftId(exchangeBatteryForegift.getId());
        exchangeInstallmentSettingMapper.deleteByForegiftId(exchangeBatteryForegift.getId());
        //删除押金对应的分期详情
        //清空分期记录的settingId
        for (ExchangeInstallmentSetting exchangeInstallmentSetting : exchangeInstallmentSettingList) {
            exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
            customerInstallmentRecordMapper.clearExchangeSettingId(exchangeInstallmentSetting.getId());
        }
        exchangeBatteryForegiftMapper.delete(foregiftId);
        packetPeriodPriceMapper.deleteByForegiftId(foregiftId);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult batteryPeriodPriceDelete(Integer priceId) {
        packetPeriodPriceMapper.delete(priceId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult batteryTypeDelete(Integer batteryType, Integer agentId) {
        //存在押金
        List<ExchangeBatteryForegift> foregiftMapperList = exchangeBatteryForegiftMapper.findList(batteryType, agentId);
        if (foregiftMapperList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"存在押金，无法删除",null);
        }
        List<CabinetBatteryType> cabinetBatteryTypeList = cabinetBatteryTypeMapper.findListByBatteryType(batteryType, agentId);
        if (cabinetBatteryTypeList.size() != 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"电池类型已绑定换电柜，无法删除",null);
        }
        ExchangePriceTime exchangePriceTime = exchangePriceTimeMapper.find(agentId, batteryType);
        if (exchangePriceTime != null) {
            exchangePriceTimeMapper.delete(agentId, batteryType);
        }
        exchangeBatteryForegiftMapper.deleteByBatteryType(batteryType, agentId);
        packetPeriodPriceMapper.deleteByBatteryType(batteryType, agentId);
        insuranceMapper.delete(batteryType, agentId);
        agentBatteryTypeMapper.delete(batteryType, agentId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }


    @Transactional(rollbackFor=Throwable.class)
    public RestResult delete(Integer batteryType, Integer agentId) {
        ExchangeBatteryForegift exchangeBatteryForegift = exchangeBatteryForegiftMapper.findByBatteryType(batteryType, agentId);
        if (exchangeBatteryForegift != null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"存在押金，无法删除",null);
        }
        List<CabinetBatteryType> cabinetBatteryTypeList = cabinetBatteryTypeMapper.findListByBatteryType(batteryType, agentId);
        if (cabinetBatteryTypeList.size() != 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"电池类型已绑定换电柜，无法删除",null);
        }
        List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceMapper.findListByBatteryType(exchangeBatteryForegift.getId(), batteryType, agentId);
        if (packetPeriodPriceList.size() != 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"存在租金，无法删除",null);
        }
        ExchangePriceTime exchangePriceTime = exchangePriceTimeMapper.find(agentId, batteryType);
        if (exchangePriceTime != null) {
            exchangePriceTimeMapper.delete(agentId, batteryType);
        }
        exchangeBatteryForegiftMapper.deleteByBatteryType(batteryType, agentId);
      /*  List<PacketPeriodPrice> packetPeriodPriceList = packetPeriodPriceMapper.findListByBatteryType(exchangeBatteryForegift.getId(), batteryType, agentId);
        if (packetPeriodPriceList.size() > 0 ) {
            for (PacketPeriodPrice packetPeriodPrice : packetPeriodPriceList) {
                packetPeriodPriceRenewMapper.deleteByPriceId(packetPeriodPrice.getId());
            }
        }*/
        packetPeriodPriceMapper.deleteByBatteryType(batteryType, agentId);
        agentBatteryTypeMapper.delete(batteryType, agentId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult cabinetDelete(String cabinetId, Integer batteryType) {
        cabinetBatteryTypeMapper.delete(cabinetId, batteryType);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult stationDelete(String stationId, Integer batteryType) {
        stationBatteryTypeMapper.delete(stationId, batteryType);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

}
