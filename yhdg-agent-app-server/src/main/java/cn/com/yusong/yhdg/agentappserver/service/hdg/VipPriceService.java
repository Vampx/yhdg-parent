package cn.com.yusong.yhdg.agentappserver.service.hdg;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.PriceGroupController;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.hdg.VipPriceController;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class VipPriceService extends AbstractService {
    @Autowired
    VipExchangeBatteryForegiftMapper vipExchangeBatteryForegiftMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    ShopMapper shopMapper;
    @Autowired
    AgentCompanyMapper agentCompanyMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    VipPriceMapper vipPriceMapper;
    @Autowired
    VipPacketPeriodPriceMapper vipPacketPeriodPriceMapper;
    @Autowired
    VipPacketPeriodPriceRenewMapper vipPacketPeriodPriceRenewMapper;
    @Autowired
    VipPriceCabinetMapper vipPriceCabinetMapper;
    @Autowired
    VipPriceStationMapper vipPriceStationMapper;
    @Autowired
    StationMapper stationMapper;
    @Autowired
    VipPriceAgentCompanyMapper vipPriceAgentCompanyMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    VipPriceCustomerMapper vipPriceCustomerMapper;
    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;

    public VipPrice find(Long id) {
        return vipPriceMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult list(Integer agentId, String keyword, int offset, int limit) {

        List<VipPrice> list = vipPriceMapper.findListByAgentId(agentId, keyword, offset, limit);
        List<Map> result = new ArrayList<Map>();
        for (VipPrice vipPrice : list) {
            NotNullMap data = new NotNullMap();

            data.putLong("id", vipPrice.getId());
            data.putString("priceName", vipPrice.getPriceName());
            data.putInteger("isActive", vipPrice.getIsActive());
            data.putInteger("cabinetCount", vipPrice.getCabinetCount());
            data.putInteger("customerCount", vipPrice.getCustomerCount());
            data.putInteger("stationCount", vipPrice.getStationCount());

            result.add(data);
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, result);

    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult create(VipPrice vipPrice,
                             Integer agentId,
                             VipPriceController.CreateParam.VipPacketPeriodPrice[] packetPeriodList,
                             String[] customerMobileList, String[] cabinetIdList,
                             String[] stationIdList) {


        vipPriceMapper.insert(vipPrice);

        //biza
        if (packetPeriodList != null) {

            for (int i = 0; i < packetPeriodList.length; i++) {

                VipPriceController.CreateParam.VipPacketPeriodPrice bizaForegift = packetPeriodList[i];
                VipExchangeBatteryForegift vipForegift = new VipExchangeBatteryForegift();

                vipForegift.setAgentId(agentId);
                vipForegift.setPriceId(vipPrice.getId());
                vipForegift.setReduceMoney(bizaForegift.reduceMoney);
                vipForegift.setForegiftId(bizaForegift.foregiftId);
                vipForegift.setCreateTime(new Date());
                vipExchangeBatteryForegiftMapper.insert(vipForegift);

                for (int j = 0; j < packetPeriodList[i].priceList.length; j++) {
                    VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice bizaPrice = packetPeriodList[i].priceList[j];
                    VipPacketPeriodPrice vipPacketPeriodPrice = new VipPacketPeriodPrice();

                    vipPacketPeriodPrice.setVipForegiftId(vipForegift.getId());
                    vipPacketPeriodPrice.setPriceId(vipForegift.getPriceId());
                    vipPacketPeriodPrice.setForegiftId(bizaForegift.foregiftId);
                    vipPacketPeriodPrice.setDayCount(bizaPrice.dayCount);
                    vipPacketPeriodPrice.setPrice(bizaPrice.price);
                    vipPacketPeriodPrice.setMemo(bizaPrice.memo);
                    vipPacketPeriodPrice.setAgentId(agentId);
                    vipPacketPeriodPrice.setIsTicket(bizaPrice.isTicket);
                    vipPacketPeriodPrice.setAgentName(agentMapper.find(agentId).getAgentName());
                    vipPacketPeriodPrice.setBatteryType(vipPrice.getBatteryType());
                    vipPacketPeriodPrice.setLimitCount(bizaPrice.limitCount);
                    vipPacketPeriodPrice.setDayLimitCount(bizaPrice.dayLimitCount);
                    vipPacketPeriodPrice.setCreateTime(new Date());
                    vipPacketPeriodPriceMapper.insert(vipPacketPeriodPrice);

                    for (int k = 0; k < packetPeriodList[i].priceList[j].priceListRenew.length; k++) {
                        VipPriceController.CreateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew bizaPriceRenew = packetPeriodList[i].priceList[j].priceListRenew[k];
                        VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew = new VipPacketPeriodPriceRenew();
                        //续费
                        vipPacketPeriodPriceRenew.setPacketPriceId(vipPacketPeriodPrice.getId());
                        vipPacketPeriodPriceRenew.setForegiftId(bizaForegift.foregiftId);
                        vipPacketPeriodPriceRenew.setDayCount(bizaPriceRenew.dayCount);
                        vipPacketPeriodPriceRenew.setPrice(bizaPriceRenew.price);
                        vipPacketPeriodPriceRenew.setMemo(bizaPriceRenew.memo);
                        vipPacketPeriodPriceRenew.setAgentId(agentId);
                        vipPacketPeriodPriceRenew.setIsTicket(bizaPriceRenew.isTicket);
                        vipPacketPeriodPriceRenew.setAgentName(agentMapper.find(agentId).getAgentName());
                        vipPacketPeriodPriceRenew.setBatteryType(vipPrice.getBatteryType());
                        vipPacketPeriodPriceRenew.setLimitCount(bizaPriceRenew.limitCount);
                        vipPacketPeriodPriceRenew.setDayLimitCount(bizaPriceRenew.dayLimitCount);
                        vipPacketPeriodPriceRenew.setCreateTime(new Date());
                        vipPacketPeriodPriceRenewMapper.insert(vipPacketPeriodPriceRenew);

                    }
                }

            }
        }

        if (customerMobileList.length > 0) {
            for (String mobile : customerMobileList) {
                VipPriceCustomer vipPriceCustomer = vipPriceCustomerMapper.findByAgentIdAndMobile(agentId, mobile);
                if (vipPriceCustomer != null) {
                    return  RestResult.result(RespCode.CODE_2.getValue(),"该骑手手机号已存在");
                }
                VipPriceCustomer cct = new VipPriceCustomer();
                cct.setMobile(mobile);
                cct.setPriceId(vipPrice.getId());
                cct.setCreateTime(new Date());
                vipPriceCustomerMapper.insert(cct);
            }
            List<VipPriceCustomer> customerList = vipPriceCustomerMapper.findListByPriceId(vipPrice.getId());
            vipPriceMapper.updateCustomerCount(vipPrice.getId(),customerList.size());
        }

        if (cabinetIdList.length > 0) {
            for (String cabinetId : cabinetIdList) {
                VipPriceCabinet vipPriceCabinet = vipPriceCabinetMapper.findByPriceId(vipPrice.getId(), cabinetId);
                if (vipPriceCabinet != null) {
                    return  RestResult.result(RespCode.CODE_2.getValue(),"包含已存在的柜子");
                }
                VipPriceCabinet cct = new VipPriceCabinet();
                cct.setPriceId(vipPrice.getId());
                cct.setCabinetId(cabinetId);
                vipPriceCabinetMapper.insert(cct);
            }
            List<VipPriceCabinet> cabinetList = vipPriceCabinetMapper.findListByPriceId(vipPrice.getId());
            vipPriceMapper.updateCabinetCount(vipPrice.getId(),cabinetList.size());
        }

        if (stationIdList.length > 0) {
            for (String stationId : stationIdList) {
                VipPriceStation vipPriceStation = vipPriceStationMapper.findByPriceId(vipPrice.getId(), stationId);
                if (vipPriceStation != null) {
                    return  RestResult.result(RespCode.CODE_2.getValue(),"包含已存在的站点");
                }
                VipPriceStation cct = new VipPriceStation();
                cct.setPriceId(vipPrice.getId());
                cct.setStationId(stationId);
                vipPriceStationMapper.insert(cct);
            }
            List<VipPriceStation> stationList = vipPriceStationMapper.findListByPriceId(vipPrice.getId());
            vipPriceMapper.updateStationCount(vipPrice.getId(), stationList.size());
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);

    }


    @Transactional(rollbackFor=Throwable.class)
    public RestResult update(VipPrice vipPrice,
                             Integer agentId,
                             VipPriceController.UpdateParam.VipPacketPeriodPrice[] packetPeriodList,
                             String[] customerMobileList, String[] cabinetIdList, String[] stationIdList) {

        vipPriceMapper.update(vipPrice);
        //biza
        if (packetPeriodList != null) {

            vipExchangeBatteryForegiftMapper.deleteByPriceId(vipPrice.getId());
            List<VipPacketPeriodPrice> vipPeriodPriceList = vipPacketPeriodPriceMapper.findListByPriceId(vipPrice.getId());
            for (VipPacketPeriodPrice vipPeriodPrice : vipPeriodPriceList) {
                vipPacketPeriodPriceRenewMapper.deleteByPriceId(vipPeriodPrice.getId());
            }
            vipPacketPeriodPriceMapper.deleteByPriceId(vipPrice.getId());

            for (int i = 0; i < packetPeriodList.length; i++) {
                VipPriceController.UpdateParam.VipPacketPeriodPrice bizaForegift = packetPeriodList[i];
                VipExchangeBatteryForegift vipForegift = new VipExchangeBatteryForegift();

                vipForegift.setAgentId(agentId);
                vipForegift.setPriceId(vipPrice.getId());
                vipForegift.setReduceMoney(bizaForegift.reduceMoney);
                vipForegift.setForegiftId(bizaForegift.foregiftId);
                vipForegift.setCreateTime(new Date());
                vipExchangeBatteryForegiftMapper.insert(vipForegift);

                for (int j = 0; j < packetPeriodList[i].priceList.length; j++) {
                    VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice bizaPrice = packetPeriodList[i].priceList[j];
                    VipPacketPeriodPrice vipPacketPeriodPrice = new VipPacketPeriodPrice();

                    vipPacketPeriodPrice.setVipForegiftId(vipForegift.getId());
                    vipPacketPeriodPrice.setPriceId(vipForegift.getPriceId());
                    vipPacketPeriodPrice.setForegiftId(bizaForegift.foregiftId);
                    vipPacketPeriodPrice.setDayCount(bizaPrice.dayCount);
                    vipPacketPeriodPrice.setPrice(bizaPrice.price);
                    vipPacketPeriodPrice.setMemo(bizaPrice.memo);
                    vipPacketPeriodPrice.setAgentId(agentId);
                    vipPacketPeriodPrice.setIsTicket(bizaPrice.isTicket);
                    vipPacketPeriodPrice.setAgentName(agentMapper.find(agentId).getAgentName());
                    vipPacketPeriodPrice.setBatteryType(vipPrice.getBatteryType());
                    vipPacketPeriodPrice.setLimitCount(bizaPrice.limitCount);
                    vipPacketPeriodPrice.setDayLimitCount(bizaPrice.dayLimitCount);
                    vipPacketPeriodPrice.setCreateTime(new Date());
                    vipPacketPeriodPriceMapper.insert(vipPacketPeriodPrice);

                    for (int k = 0; k < packetPeriodList[i].priceList[j].priceListRenew.length; k++) {
                        VipPriceController.UpdateParam.VipPacketPeriodPrice.GroupBizaPrice.GroupBizaPriceRenew bizaPriceRenew = packetPeriodList[i].priceList[j].priceListRenew[k];
                        VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew = new VipPacketPeriodPriceRenew();
                        //续费
                        vipPacketPeriodPriceRenew.setPacketPriceId(vipPacketPeriodPrice.getId());
                        vipPacketPeriodPriceRenew.setForegiftId(bizaForegift.foregiftId);
                        vipPacketPeriodPriceRenew.setDayCount(bizaPriceRenew.dayCount);
                        vipPacketPeriodPriceRenew.setPrice(bizaPriceRenew.price);
                        vipPacketPeriodPriceRenew.setMemo(bizaPriceRenew.memo);
                        vipPacketPeriodPriceRenew.setAgentId(agentId);
                        vipPacketPeriodPriceRenew.setIsTicket(bizaPriceRenew.isTicket);
                        vipPacketPeriodPriceRenew.setAgentName(agentMapper.find(agentId).getAgentName());
                        vipPacketPeriodPriceRenew.setBatteryType(vipPrice.getBatteryType());
                        vipPacketPeriodPriceRenew.setLimitCount(bizaPriceRenew.limitCount);
                        vipPacketPeriodPriceRenew.setDayLimitCount(bizaPriceRenew.dayLimitCount);
                        vipPacketPeriodPriceRenew.setCreateTime(new Date());
                        vipPacketPeriodPriceRenewMapper.insert(vipPacketPeriodPriceRenew);

                    }
                }

            }
        }

        if (customerMobileList.length > 0) {

            Set<String> mobileSet = new HashSet<String>();
            for (String mobile : customerMobileList) {
                mobileSet.add(mobile);
            }

            vipPriceCustomerMapper.deleteByPriceId(vipPrice.getId());

            for (String mobile : mobileSet) {

                VipPriceCustomer cct = new VipPriceCustomer();
                cct.setMobile(mobile);
                cct.setPriceId(vipPrice.getId());
                cct.setCreateTime(new Date());
                vipPriceCustomerMapper.insert(cct);
            }
        }

        List<VipPriceCustomer> customerList = vipPriceCustomerMapper.findListByPriceId(vipPrice.getId());
        vipPriceMapper.updateCustomerCount(vipPrice.getId(),customerList.size());

        if (cabinetIdList.length > 0) {

            Set<String> cabinetSet = new HashSet<String>();
            for (String cabinetId : cabinetIdList) {
                cabinetSet.add(cabinetId);
            }

            vipPriceCabinetMapper.deleteByPriceId(vipPrice.getId());

            for (String cabinetId : cabinetSet) {

                VipPriceCabinet cct = new VipPriceCabinet();
                cct.setPriceId(vipPrice.getId());
                cct.setCabinetId(cabinetId);
                vipPriceCabinetMapper.insert(cct);
            }
        }

        List<VipPriceCabinet> cabinetList = vipPriceCabinetMapper.findListByPriceId(vipPrice.getId());
        vipPriceMapper.updateCabinetCount(vipPrice.getId(),cabinetList.size());

        if (stationIdList.length > 0) {

            Set<String> stationSet = new HashSet<String>();
            for (String stationId : stationIdList) {
                stationSet.add(stationId);
            }

            vipPriceStationMapper.deleteByPriceId(vipPrice.getId());

            for (String stationId : stationSet) {

                VipPriceStation cct = new VipPriceStation();
                cct.setPriceId(vipPrice.getId());
                cct.setStationId(stationId);
                vipPriceStationMapper.insert(cct);
            }
        }

        List<VipPriceStation> stationList = vipPriceStationMapper.findListByPriceId(vipPrice.getId());
        vipPriceMapper.updateStationCount(vipPrice.getId(), stationList.size());

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor = Throwable.class)
    public RestResult detail(Long id, Integer agentId) {
        NotNullMap data = new NotNullMap();

        VipPrice vipPrice = vipPriceMapper.find(id);
        if (vipPrice == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在!");
        }
        AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(vipPrice.getBatteryType(), agentId);
        data.putLong("id", vipPrice.getId());
        data.putInteger("batteryType", vipPrice.getBatteryType());
        data.putString("batteryTypeName", agentBatteryType.getTypeName());
        data.putString("priceName", vipPrice.getPriceName());
        data.putDateTime("beginTime", vipPrice.getBeginTime());
        data.putDateTime("endTime", vipPrice.getEndTime());
        data.putInteger("isActive", vipPrice.getIsActive());

        //biza
        List<VipExchangeBatteryForegift> vipExchangeBatteryForegiftList = vipExchangeBatteryForegiftMapper.findListByPriceId(vipPrice.getId());
        List<NotNullMap> packetPeriodList = new ArrayList<NotNullMap>();

        for (VipExchangeBatteryForegift bizaForegift : vipExchangeBatteryForegiftList) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.put("reduceMoney", bizaForegift.getReduceMoney());
            notNullMap.put("foregiftId", bizaForegift.getForegiftId());
            ExchangeBatteryForegift foregift = exchangeBatteryForegiftMapper.find(bizaForegift.getForegiftId());
            if (foregift != null) {
                notNullMap.put("foregift", foregift.getMoney());
            } else {
                notNullMap.put("foregift", 0);
            }

            List<VipPacketPeriodPrice> vipPeriodPriceList = vipPacketPeriodPriceMapper.findListByForegiftId(
                    bizaForegift.getForegiftId(), vipPrice.getBatteryType(), agentId, bizaForegift.getPriceId());
            List<NotNullMap> bizaPriceList = new ArrayList<NotNullMap>();
            for (VipPacketPeriodPrice bizaPacketPeriodPrice : vipPeriodPriceList) {
                NotNullMap notNullMap2 = new NotNullMap();

                notNullMap2.put("dayCount", bizaPacketPeriodPrice.getDayCount());
                notNullMap2.put("price", bizaPacketPeriodPrice.getPrice());
                notNullMap2.put("memo", bizaPacketPeriodPrice.getMemo());
                notNullMap2.put("isTicket", bizaPacketPeriodPrice.getIsTicket());
                notNullMap2.put("limitCount", bizaPacketPeriodPrice.getLimitCount());
                notNullMap2.put("dayLimitCount", bizaPacketPeriodPrice.getDayLimitCount());

                List<VipPacketPeriodPriceRenew> vipPacketPeriodPriceRenewList = vipPacketPeriodPriceRenewMapper.findList(bizaPacketPeriodPrice.getId());

                List<NotNullMap> vipPriceListRenew = new ArrayList<NotNullMap>();
                for (VipPacketPeriodPriceRenew vipPacketPeriodPriceRenew : vipPacketPeriodPriceRenewList) {
                    NotNullMap notNullMap3 = new NotNullMap();

                    notNullMap3.put("renewPriceId", vipPacketPeriodPriceRenew.getId());
                    notNullMap3.put("dayCount", vipPacketPeriodPriceRenew.getDayCount());
                    notNullMap3.put("price", vipPacketPeriodPriceRenew.getPrice());
                    notNullMap3.put("memo", vipPacketPeriodPriceRenew.getMemo());
                    notNullMap3.put("isTicket", vipPacketPeriodPriceRenew.getIsTicket());
                    notNullMap3.put("limitCount", vipPacketPeriodPriceRenew.getLimitCount());
                    notNullMap3.put("dayLimitCount", vipPacketPeriodPriceRenew.getDayLimitCount());

                    vipPriceListRenew.add(notNullMap3);
                }

                notNullMap2.put("priceListRenew", vipPriceListRenew);

                bizaPriceList.add(notNullMap2);
            }
            notNullMap.put("priceList", bizaPriceList);

            packetPeriodList.add(notNullMap);
        }
        data.put("packetPeriodList", packetPeriodList);


        List<VipPriceCustomer> vipPriceCustomerList = vipPriceCustomerMapper.findListByPriceId(vipPrice.getId());

        List<NotNullMap> vipPriceCustomers = new ArrayList<NotNullMap>();
        for (VipPriceCustomer vipPriceCustomer : vipPriceCustomerList) {
            NotNullMap notNullMap2 = new NotNullMap();
            notNullMap2.put("id", vipPriceCustomer.getId());
            notNullMap2.put("mobile", vipPriceCustomer.getMobile());
            vipPriceCustomers.add(notNullMap2);
        }
        data.put("customerMobileList", vipPriceCustomers);

        List<VipPriceCabinet> vipPriceCabinetList = vipPriceCabinetMapper.findListByPriceId(vipPrice.getId());

        List<NotNullMap> vipPriceCabinets = new ArrayList<NotNullMap>();
        for (VipPriceCabinet vipPriceCabinet : vipPriceCabinetList) {
            NotNullMap notNullMap3 = new NotNullMap();
            notNullMap3.put("id", vipPriceCabinet.getId());
            notNullMap3.put("cabinetId", vipPriceCabinet.getCabinetId());
            notNullMap3.put("cabinetName", cabinetMapper.find(vipPriceCabinet.getCabinetId()).getCabinetName());
            vipPriceCabinets.add(notNullMap3);
        }
        data.put("cabinetIdList", vipPriceCabinets);

        List<VipPriceStation> vipPriceStationList = vipPriceStationMapper.findListByPriceId(vipPrice.getId());

        List<NotNullMap> vipPriceStations = new ArrayList<NotNullMap>();
        for (VipPriceStation vipPriceStation : vipPriceStationList) {
            NotNullMap notNullMap4 = new NotNullMap();
            notNullMap4.put("id", vipPriceStation.getId());
            notNullMap4.put("stationId", vipPriceStation.getStationId());
            notNullMap4.put("stationName", stationMapper.find(vipPriceStation.getStationId()).getStationName());
            vipPriceStations.add(notNullMap4);
        }
        data.put("stationIdList", vipPriceStations);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult delete(Long id) {
        List<VipPriceCustomer> customerList = vipPriceCustomerMapper.findListByPriceId(id);
        if(customerList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"套餐包含骑手，不能删除。",null);
        }
        List<VipPriceCabinet> cabinetList = vipPriceCabinetMapper.findListByPriceId(id);
        if(cabinetList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"套餐包含柜子，不能删除。",null);
        }
        List<VipPriceStation> stationList = vipPriceStationMapper.findListByPriceId(id);
        if(stationList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"套餐包含站点，不能删除。",null);
        }
        vipExchangeBatteryForegiftMapper.deleteByPriceId(id);
        List<VipPacketPeriodPrice> vipPeriodPriceList = vipPacketPeriodPriceMapper.findListByPriceId(id);
        for (VipPacketPeriodPrice vipPeriodPrice : vipPeriodPriceList) {
            vipPacketPeriodPriceRenewMapper.deleteByPriceId(vipPeriodPrice.getId());
        }
        vipPacketPeriodPriceMapper.deleteByPriceId(id);
        vipPriceMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult vipCabinetDelete(Long id) {
        vipPriceCabinetMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult vipStationDelete(Long id) {
        vipPriceStationMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult vipCustomerDelete(Long id) {
        vipPriceCustomerMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult vipAgentCompanyDelete(Long id) {
        vipPriceAgentCompanyMapper.delete(id);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

}
