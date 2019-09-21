package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.*;
import cn.com.yusong.yhdg.common.entity.json.DataResult;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.AgentBatteryTypeMapper;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.*;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
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
    PacketPeriodPriceRenewMapper packetPeriodPriceRenewMapper;
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

    public List<PacketPeriodPrice> findListByForegiftId(Long foregiftId, Integer batteryType, Integer agentId) {
        return packetPeriodPriceMapper.findListByForegiftId(foregiftId, batteryType, agentId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(PacketPeriodPrice packetPeriodPrice) {
        if (packetPeriodPrice.getId() == null || packetPeriodPrice.getId() == 0) {
            packetPeriodPrice.setCreateTime(new Date());
            packetPeriodPrice.setAgentName(findAgentInfo(packetPeriodPrice.getAgentId()).getAgentName());
            packetPeriodPriceMapper.insert(packetPeriodPrice);
        } else {
            packetPeriodPriceMapper.update(packetPeriodPrice);
        }

        return DataResult.successResult(packetPeriodPrice);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(PacketPeriodPrice packetPeriodPrice) {
        packetPeriodPriceMapper.update(packetPeriodPrice);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        packetPeriodPriceRenewMapper.deleteByPriceId(id);
        packetPeriodPriceRenewMapper.delete(id);
        int total = packetPeriodPriceMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
