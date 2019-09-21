package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.agentserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.*;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.basic.AgentBatteryType;
import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PacketPeriodPriceService extends AbstractService {

    @Autowired
    PacketPeriodPriceMapper packetPeriodPriceMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    ExchangeInstallmentDetailMapper exchangeInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;
    @Autowired
    ExchangeBatteryForegiftMapper exchangeBatteryForegiftMapper;
    @Autowired
    CabinetMapper cabinetMapper;
    @Autowired
    AgentBatteryTypeMapper agentBatteryTypeMapper;
    @Autowired
    VipPriceShopMapper vipPriceShopMapper;

    public PacketPeriodPrice find(Long id) {
        return packetPeriodPriceMapper.find(id);
    }

    public List<PacketPeriodPrice> findListByBatteryType(Integer batteryType, Integer agentId) {
        return packetPeriodPriceMapper.findListByBatteryType(batteryType, agentId);
    }

    public List<PacketPeriodPrice> findListByForegiftId(Integer foregiftId, Integer batteryType, Integer agentId) {
        return packetPeriodPriceMapper.findListByForegiftId(foregiftId, batteryType, agentId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(PacketPeriodPrice packetPeriodPrice) {
        packetPeriodPrice.setCreateTime(new Date());
        packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
        packetPeriodPriceMapper.insert(packetPeriodPrice);
        ExchangeBatteryForegift foregift = exchangeBatteryForegiftMapper.find(packetPeriodPrice.getForegiftId());
        if (foregift != null) {
            AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(foregift.getBatteryType(), foregift.getAgentId());
            if (agentBatteryType != null) {
                List<Cabinet> cabinetList = cabinetMapper.findListByAgentAndBatteryType(agentBatteryType.getAgentId(), agentBatteryType.getBatteryType());
                for (Cabinet cabinet : cabinetList) {
                    updatePrice(cabinet.getAgentId(), cabinet.getId());
                    if (cabinet.getShopId() != null) {
                        VipPriceShop vipPriceShop = vipPriceShopMapper.findByShopId(cabinet.getShopId());
                        if (vipPriceShop != null) {
                            updateShopPrice(vipPriceShop.getId(), cabinet.getShopId());
                        }else {
                            updateShopPriceByCabint(cabinet.getShopId());
                        }
                    }
                }
            }
        }
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(PacketPeriodPrice packetPeriodPrice) {
        PacketPeriodPrice dbPacketPeriodPrice = packetPeriodPriceMapper.find(packetPeriodPrice.getId());
        if (dbPacketPeriodPrice.getPrice().intValue() != packetPeriodPrice.getPrice().intValue()) {
            //删除租金对应的分期设置
            List<ExchangeInstallmentSetting> exchangeInstallmentSettingList = exchangeInstallmentSettingMapper.findByPacketId(packetPeriodPrice.getId());
            exchangeInstallmentSettingMapper.deleteByPacketId(packetPeriodPrice.getId());
            //删除押金对应的分期详情
            //清空分期记录的settingId
            for (ExchangeInstallmentSetting exchangeInstallmentSetting : exchangeInstallmentSettingList) {
                exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
                customerInstallmentRecordMapper.clearExchangeSettingId(exchangeInstallmentSetting.getId());
            }
        }
        packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
        packetPeriodPriceMapper.update(packetPeriodPrice);
        ExchangeBatteryForegift foregift = exchangeBatteryForegiftMapper.find(packetPeriodPrice.getForegiftId());
        if (foregift != null) {
            AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(foregift.getBatteryType(), foregift.getAgentId());
            if (agentBatteryType != null) {
                List<Cabinet> cabinetList = cabinetMapper.findListByAgentAndBatteryType(agentBatteryType.getAgentId(), agentBatteryType.getBatteryType());
                for (Cabinet cabinet : cabinetList) {
                    updatePrice(cabinet.getAgentId(), cabinet.getId());
                    if (cabinet.getShopId() != null) {
                        VipPriceShop vipPriceShop = vipPriceShopMapper.findByShopId(cabinet.getShopId());
                        if (vipPriceShop != null) {
                            updateShopPrice(vipPriceShop.getId(), cabinet.getShopId());
                        }else {
                            updateShopPriceByCabint(cabinet.getShopId());
                        }
                    }
                }
            }
        }
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        PacketPeriodPrice packetPeriodPrice = packetPeriodPriceMapper.find(id);
        //删除租金对应的分期设置
        List<ExchangeInstallmentSetting> exchangeInstallmentSettingList = exchangeInstallmentSettingMapper.findByPacketId(id);
        exchangeInstallmentSettingMapper.deleteByPacketId(id);
        //删除押金对应的分期详情
        //清空分期记录的settingId
        for (ExchangeInstallmentSetting exchangeInstallmentSetting : exchangeInstallmentSettingList) {
            exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
            customerInstallmentRecordMapper.clearExchangeSettingId(exchangeInstallmentSetting.getId());
        }
        int total = packetPeriodPriceMapper.delete(id);
        ExchangeBatteryForegift foregift = exchangeBatteryForegiftMapper.find(packetPeriodPrice.getForegiftId());
        if (foregift != null) {
            AgentBatteryType agentBatteryType = agentBatteryTypeMapper.find(foregift.getBatteryType(), foregift.getAgentId());
            if (agentBatteryType != null) {
                List<Cabinet> cabinetList = cabinetMapper.findListByAgentAndBatteryType(agentBatteryType.getAgentId(), agentBatteryType.getBatteryType());
                for (Cabinet cabinet : cabinetList) {
                    updatePrice(cabinet.getAgentId(), cabinet.getId());
                    if (cabinet.getShopId() != null) {
                        VipPriceShop vipPriceShop = vipPriceShopMapper.findByShopId(cabinet.getShopId());
                        if (vipPriceShop != null) {
                            updateShopPrice(vipPriceShop.getId(), cabinet.getShopId());
                        }else {
                            updateShopPriceByCabint(cabinet.getShopId());
                        }
                    }
                }
            }
        }
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
