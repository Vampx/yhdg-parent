package cn.com.yusong.yhdg.agentappserver.service.zd;

import cn.com.yusong.yhdg.agentappserver.constant.RespCode;
import cn.com.yusong.yhdg.agentappserver.entity.result.RestResult;
import cn.com.yusong.yhdg.agentappserver.persistence.basic.*;
import cn.com.yusong.yhdg.agentappserver.persistence.zd.*;
import cn.com.yusong.yhdg.agentappserver.service.AbstractService;
import cn.com.yusong.yhdg.agentappserver.web.controller.api.v1.agent.zd.RentPriceGroupController;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.SystemBatteryType;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.NotNullMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class RentPriceGroupService extends AbstractService {
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    RentBatteryTypeMapper rentBatteryTypeMapper;
    @Autowired
    RentBatteryForegiftMapper rentBatteryForegiftMapper;
    @Autowired
    RentPeriodPriceMapper rentPeriodPriceMapper;
    @Autowired
    AgentMapper agentMapper;
    @Autowired
    RentInsuranceMapper rentInsuranceMapper;
    @Autowired
    SystemBatteryTypeMapper systemBatteryTypeMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    RentInstallmentSettingMapper rentInstallmentSettingMapper;
    @Autowired
    RentInstallmentDetailMapper rentInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;

    @Transactional(rollbackFor = Throwable.class)
    public RestResult list(Integer agentId) {

        List<RentBatteryType> list = rentBatteryTypeMapper.findListByAgentId(agentId);
        List<Map> result = new ArrayList<Map>();
        for (RentBatteryType rentBatteryType : list) {
            NotNullMap data = new NotNullMap();

            SystemBatteryType systemBatteryType = systemBatteryTypeMapper.find(rentBatteryType.getBatteryType());
            data.putInteger("batteryType", rentBatteryType.getBatteryType());
            data.putString("batteryTypeName", rentBatteryType.getTypeName());
            data.putInteger("ratedVoltage", systemBatteryType.getRatedVoltage());
            data.putInteger("ratedCapacity", systemBatteryType.getRatedCapacity());

            List<RentInsurance> bizaInsuranceList = rentInsuranceMapper.findListByBatteryType(rentBatteryType.getBatteryType(), agentId);

            List<NotNullMap> insuranceList = new ArrayList<NotNullMap>();
            for (RentInsurance insurance : bizaInsuranceList) {
                NotNullMap notNullMap3 = new NotNullMap();

                notNullMap3.putString("agentName", agentMapper.find(agentId).getAgentName());
                notNullMap3.putString("insuranceName", insurance.getInsuranceName());
                notNullMap3.putInteger("price", insurance.getPrice());
                notNullMap3.putInteger("money", insurance.getPaid());
                notNullMap3.putInteger("monthCount", insurance.getMonthCount());
                notNullMap3.putInteger("isActive", insurance.getIsActive());
                notNullMap3.putString("memo", insurance.getMemo());

                insuranceList.add(notNullMap3);
            }
            data.put("insuranceList", insuranceList);

            List<NotNullMap> packetPeriodList = new ArrayList<NotNullMap>();
            //biza
            List<RentBatteryForegift> bizaForegiftList = rentBatteryForegiftMapper.findList(rentBatteryType.getBatteryType(), agentId);

            for (RentBatteryForegift bizaForegift : bizaForegiftList) {
                NotNullMap notNullMap = new NotNullMap();
                notNullMap.putLong("foregiftId", bizaForegift.getId());
                notNullMap.putInteger("foregift", bizaForegift.getMoney());
                notNullMap.putString("memo", bizaForegift.getMemo());

                List<RentPeriodPrice> bizaPacketPeriodPriceList = rentPeriodPriceMapper.findListByBatteryType(bizaForegift.getId(), bizaForegift.getBatteryType(), agentId);

                List<NotNullMap> bizaPriceList = new ArrayList<NotNullMap>();
                for (RentPeriodPrice bizaPacketPeriodPrice : bizaPacketPeriodPriceList) {
                    NotNullMap notNullMap2 = new NotNullMap();

                    notNullMap2.put("priceId", bizaPacketPeriodPrice.getId());
                    notNullMap2.put("dayCount", bizaPacketPeriodPrice.getDayCount());
                    notNullMap2.put("price", bizaPacketPeriodPrice.getPrice());
                    notNullMap2.put("memo", bizaPacketPeriodPrice.getMemo());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(bizaPacketPeriodPrice.getCreateTime());
                    calendar.add(Calendar.DAY_OF_MONTH, bizaPacketPeriodPrice.getDayCount());
                    String date = DateFormatUtils.format(calendar.getTime(), Constant.DATE_FORMAT);
                    notNullMap2.put("endDate", date);

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

    @Transactional(rollbackFor=Throwable.class)
    public RestResult create(RentBatteryType rentBatteryType,
                             Integer agentId,
                             RentPriceGroupController.CreateParam.Insurance[] insuranceList,
                             RentPriceGroupController.CreateParam.GroupBiza[] packetPeriodList) {

        rentBatteryTypeMapper.insert(rentBatteryType);

        if (insuranceList != null) {

            for (int j = 0; j < insuranceList.length; j++) {
                RentPriceGroupController.CreateParam.Insurance insurance = insuranceList[j];
                RentInsurance insurance1 = new RentInsurance();

                insurance1.setAgentId(agentId);
                insurance1.setBatteryType(rentBatteryType.getBatteryType());
                insurance1.setPrice(insurance.price);
                insurance1.setPaid(insurance.money);
                insurance1.setMonthCount(insurance.monthCount);
                insurance1.setMemo(insurance.memo);
                insurance1.setInsuranceName(insurance.insuranceName);
                insurance1.setIsActive(insurance.isActive);
                insurance1.setCreateTime(new Date());
                rentInsuranceMapper.insert(insurance1);

            }
        }

        //biza
        if (packetPeriodList != null) {

            for (int i = 0; i < packetPeriodList.length; i++) {
                RentPriceGroupController.CreateParam.GroupBiza detail = packetPeriodList[i];
                RentBatteryForegift bizaForegift = new RentBatteryForegift();
                bizaForegift.setBatteryType(rentBatteryType.getBatteryType());
                bizaForegift.setMoney(detail.foregift);
                bizaForegift.setMemo(detail.memo);
                bizaForegift.setAgentId(agentId);
                rentBatteryForegiftMapper.insert(bizaForegift);

                for (int j = 0; j < packetPeriodList[i].priceList.length; j++) {
                    RentPriceGroupController.CreateParam.GroupBiza.GroupBizaPrice bizaPrice = packetPeriodList[i].priceList[j];
                    RentPeriodPrice bizaPacketPeriodPrice = new RentPeriodPrice();

                    bizaPacketPeriodPrice.setForegiftId(bizaForegift.getId());
                    bizaPacketPeriodPrice.setDayCount(bizaPrice.dayCount);
                    bizaPacketPeriodPrice.setPrice(bizaPrice.price);
                    bizaPacketPeriodPrice.setMemo(bizaPrice.memo);
                    bizaPacketPeriodPrice.setAgentId(agentId);
                    bizaPacketPeriodPrice.setAgentName(agentMapper.find(agentId).getAgentName());
                    bizaPacketPeriodPrice.setBatteryType(bizaForegift.getBatteryType());
                    bizaPacketPeriodPrice.setCreateTime(new Date());
                    rentPeriodPriceMapper.insert(bizaPacketPeriodPrice);

                }

            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);

    }


    @Transactional(rollbackFor=Throwable.class)
    public RestResult update(RentBatteryType rentBatteryType,
                             Integer agentId,
                             RentPriceGroupController.UpdateParam.Insurance[] insuranceList,
                             RentPriceGroupController.UpdateParam.GroupBiza[] packetPeriodList) {

        if (rentBatteryTypeMapper.find(rentBatteryType.getBatteryType(), agentId) != null) {
            rentBatteryTypeMapper.delete(rentBatteryType.getBatteryType(), agentId);
        }

        rentBatteryTypeMapper.insert(rentBatteryType);

        if (insuranceList != null) {
            rentInsuranceMapper.delete(rentBatteryType.getBatteryType(), agentId);

            for (int j = 0; j < insuranceList.length; j++) {
                RentPriceGroupController.UpdateParam.Insurance insurance = insuranceList[j];
                RentInsurance insurance1 = new RentInsurance();

                insurance1.setAgentId(agentId);
                insurance1.setBatteryType(rentBatteryType.getBatteryType());
                insurance1.setPrice(insurance.price);
                insurance1.setPaid(insurance.money);
                insurance1.setMonthCount(insurance.monthCount);
                insurance1.setMemo(insurance.memo);
                insurance1.setInsuranceName(insurance.insuranceName);
                insurance1.setIsActive(insurance.isActive);
                insurance1.setCreateTime(new Date());
                rentInsuranceMapper.insert(insurance1);
            }
        }

        //biza
        if (packetPeriodList != null) {

            for (int i = 0; i < packetPeriodList.length; i++) {
                RentPriceGroupController.UpdateParam.GroupBiza detail = packetPeriodList[i];
                RentBatteryForegift bizaForegift = new RentBatteryForegift();
                if (detail.foregiftId == null) {
                    bizaForegift.setBatteryType(rentBatteryType.getBatteryType());
                    bizaForegift.setMoney(detail.foregift);
                    bizaForegift.setAgentId(agentId);
                    bizaForegift.setMemo(detail.memo);
                    rentBatteryForegiftMapper.insert(bizaForegift);
                } else {
                    bizaForegift.setBatteryType(rentBatteryType.getBatteryType());
                    bizaForegift.setId(detail.foregiftId);
                    bizaForegift.setMoney(detail.foregift);
                    bizaForegift.setAgentId(agentId);
                    bizaForegift.setMemo(detail.memo);
                    rentBatteryForegiftMapper.update(bizaForegift);
                }

                for (int j = 0; j < packetPeriodList[i].priceList.length; j++) {
                    RentPriceGroupController.UpdateParam.GroupBiza.GroupBizaPrice bizaPrice = packetPeriodList[i].priceList[j];
                    RentPeriodPrice bizaPacketPeriodPrice = new RentPeriodPrice();

                    if (bizaPrice.priceId == null) {
                        bizaPacketPeriodPrice.setForegiftId(bizaForegift.getId());
                        bizaPacketPeriodPrice.setDayCount(bizaPrice.dayCount);
                        bizaPacketPeriodPrice.setPrice(bizaPrice.price);
                        bizaPacketPeriodPrice.setMemo(bizaPrice.memo);
                        bizaPacketPeriodPrice.setAgentId(agentId);
                        bizaPacketPeriodPrice.setAgentName(agentMapper.find(agentId).getAgentName());
                        bizaPacketPeriodPrice.setBatteryType(bizaForegift.getBatteryType());
                        bizaPacketPeriodPrice.setCreateTime(new Date());
                        rentPeriodPriceMapper.insert(bizaPacketPeriodPrice);
                    } else {
                        bizaPacketPeriodPrice.setId(bizaPrice.priceId);
                        bizaPacketPeriodPrice.setDayCount(bizaPrice.dayCount);
                        bizaPacketPeriodPrice.setPrice(bizaPrice.price);
                        bizaPacketPeriodPrice.setMemo(bizaPrice.memo);
                        rentPeriodPriceMapper.update(bizaPacketPeriodPrice);
                    }
                }
            }
        }

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }



    @Transactional(rollbackFor = Throwable.class)
    public RestResult detail(Integer batteryType, Integer agentId) {
        RentBatteryType rentBatteryType = rentBatteryTypeMapper.find(batteryType, agentId);

        NotNullMap data = new NotNullMap();
        if (rentBatteryType == null) {
            return RestResult.result(RespCode.CODE_2.getValue(), "记录不存在!");
        }

        SystemBatteryType systemBatteryType = systemBatteryTypeMapper.find(batteryType);
        data.putInteger("id", systemBatteryType.getId());

        data.putInteger("batteryType", rentBatteryType.getBatteryType());
        data.putString("batteryTypeName", rentBatteryType.getTypeName());
        data.putInteger("ratedVoltage", rentBatteryType.getRatedVoltage());
        data.putInteger("ratedCapacity", rentBatteryType.getRatedCapacity());

        List<RentInsurance> insuranceList = rentInsuranceMapper.findListByBatteryType(batteryType, agentId);
        List<NotNullMap> insurances = new ArrayList<NotNullMap>();
        for (RentInsurance insurance : insuranceList) {
            NotNullMap notNullMap3 = new NotNullMap();
            notNullMap3.putString("agentName", agentMapper.find(agentId).getAgentName());
            notNullMap3.putString("insuranceName", insurance.getInsuranceName());
            notNullMap3.putInteger("price", insurance.getPrice());
            notNullMap3.putInteger("money", insurance.getPaid());
            notNullMap3.putInteger("monthCount", insurance.getMonthCount());
            notNullMap3.putInteger("isActive", insurance.getIsActive());
            notNullMap3.putString("memo", insurance.getMemo());
            insurances.add(notNullMap3);
        }
        data.put("insuranceList", insurances);

        //biza
        List<RentBatteryForegift> bizaForegiftList = rentBatteryForegiftMapper.findList(batteryType, agentId);
        List<NotNullMap> packetPeriodList = new ArrayList<NotNullMap>();

        for (RentBatteryForegift bizaForegift : bizaForegiftList) {
            NotNullMap notNullMap = new NotNullMap();
            notNullMap.putLong("foregiftId", bizaForegift.getId());
            notNullMap.putInteger("foregift", bizaForegift.getMoney());
            notNullMap.putString("memo", bizaForegift.getMemo());

            List<RentPeriodPrice> bizaPacketPeriodPriceList = rentPeriodPriceMapper.findListByBatteryType(bizaForegift.getId(), bizaForegift.getBatteryType(), agentId);
            List<NotNullMap> bizaPriceList = new ArrayList<NotNullMap>();
            for (RentPeriodPrice bizaPacketPeriodPrice : bizaPacketPeriodPriceList) {
                NotNullMap notNullMap2 = new NotNullMap();

                notNullMap2.put("priceId", bizaPacketPeriodPrice.getId());
                notNullMap2.put("dayCount", bizaPacketPeriodPrice.getDayCount());
                notNullMap2.put("price", bizaPacketPeriodPrice.getPrice());
                notNullMap2.put("memo", bizaPacketPeriodPrice.getMemo());

                bizaPriceList.add(notNullMap2);
            }
            notNullMap.put("priceList", bizaPriceList);

            packetPeriodList.add(notNullMap);
        }
        data.put("packetPeriodList", packetPeriodList);

        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, data);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult batteryForegiftDelete(Long foregiftId) {
        RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(foregiftId);
        //无法删除的押金状态
        List<Integer> foregiftList = Arrays.asList(RentForegiftOrder.Status.PAY_OK.getValue(),
                RentForegiftOrder.Status.APPLY_REFUND.getValue());
        List<RentForegiftOrder> customerForegiftOrderList = rentForegiftOrderMapper.findByForegiftIdAndStatus(rentBatteryForegift.getId(), foregiftList);
        if (customerForegiftOrderList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"存在使用中的押金，无法删除",null);
        }

        //删除押金对应的分期设置
        List<RentInstallmentSetting> rentInstallmentSettingList = rentInstallmentSettingMapper.findByForegiftId(rentBatteryForegift.getId());
        rentInstallmentSettingMapper.deleteByForegiftId(rentBatteryForegift.getId());
        //删除押金对应的分期详情
        //清空分期记录的settingId
        for (RentInstallmentSetting rentInstallmentSetting : rentInstallmentSettingList) {
            rentInstallmentDetailMapper.deleteBySettingId(rentInstallmentSetting.getId());
            customerInstallmentRecordMapper.clearRentSettingId(rentInstallmentSetting.getId());
        }

        rentBatteryForegiftMapper.deleteById(foregiftId);
        rentPeriodPriceMapper.deleteByForegiftId(foregiftId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

    @Transactional(rollbackFor=Throwable.class)
    public RestResult batteryPeriodPriceDelete(Integer priceId) {
        rentPeriodPriceMapper.delete(priceId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }


    @Transactional(rollbackFor=Throwable.class)
    public RestResult batteryTypeDelete(Integer batteryType, Integer agentId) {
        //存在押金
        List<RentBatteryForegift> foregiftMapperList = rentBatteryForegiftMapper.findList(batteryType, agentId);
        if (foregiftMapperList.size() > 0) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"存在押金，无法删除",null);
        }
        rentBatteryForegiftMapper.delete(batteryType, agentId);
        rentPeriodPriceMapper.deleteByBatteryType(batteryType, agentId);
        rentInsuranceMapper.delete(batteryType, agentId);
        rentBatteryTypeMapper.delete(batteryType, agentId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }


    @Transactional(rollbackFor=Throwable.class)
    public RestResult delete(Integer batteryType, Integer agentId) {
        RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.findByBatteryType(batteryType, agentId);
        if (rentBatteryForegift != null) {
            return RestResult.dataResult(RespCode.CODE_2.getValue(),"存在押金，无法删除",null);
        }
        rentBatteryForegiftMapper.delete(batteryType, agentId);
        rentPeriodPriceMapper.deleteByBatteryType(batteryType, agentId);
        rentInsuranceMapper.delete(batteryType, agentId);
        rentBatteryTypeMapper.delete(batteryType, agentId);
        return RestResult.dataResult(RespCode.CODE_0.getValue(), null, null);
    }

}
