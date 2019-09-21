package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ExchangeInstallmentDetailMapper;
import cn.com.yusong.yhdg.agentserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.*;
import cn.com.yusong.yhdg.common.domain.basic.CustomerForegiftOrder;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeBatteryForegift;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.zd.*;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class RentBatteryForegiftService {
    @Autowired
    VipRentBatteryForegiftMapper vipRentBatteryForegiftMapper;
    @Autowired
    RentBatteryForegiftMapper rentBatteryForegiftMapper;
    @Autowired
    RentPeriodPriceMapper rentPeriodPriceMapper;
    @Autowired
    RentForegiftOrderMapper rentForegiftOrderMapper;
    @Autowired
    RentInstallmentSettingMapper rentInstallmentSettingMapper;
    @Autowired
    RentInstallmentDetailMapper rentInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;

    public List<RentBatteryForegift> findListByBatteryType(Integer batteryType, Integer agentId) {
        return rentBatteryForegiftMapper.findListByBatteryType(batteryType, agentId);
    }

    public List<RentBatteryForegift> findListByVipBatteryType(Integer batteryType, Integer agentId,Long priceId) {
        List<RentBatteryForegift> list = rentBatteryForegiftMapper.findListByBatteryType(batteryType,agentId);
        for (RentBatteryForegift foregift : list) {
            VipRentBatteryForegift vipForegift = vipRentBatteryForegiftMapper.findByAgentIdAndForegiftId(agentId, Long.valueOf(foregift.getId()), priceId);
            if (vipForegift != null && vipForegift.getReduceMoney() != null) {
                foregift.setReduceMoney(vipForegift.getReduceMoney());
                foregift.setVipPriceId(vipForegift.getId());
            } else {
                foregift.setReduceMoney(0);
            }
            foregift.setForegiftId(foregift.getId());
        }
        return list;
    }

    public RentBatteryForegift find(Long id) {
        return rentBatteryForegiftMapper.find(id);
    }

    public ExtResult create(RentBatteryForegift entity) {
        rentBatteryForegiftMapper.insert(entity);
        return ExtResult.successResult();
    }

    public ExtResult update(RentBatteryForegift entity) {
        RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(entity.getId());
        if (rentBatteryForegift.getMoney().intValue() != entity.getMoney()) {
            //删除押金对应的分期设置
            List<RentInstallmentSetting> rentInstallmentSettingList = rentInstallmentSettingMapper.findByForegiftId(rentBatteryForegift.getId());
            rentInstallmentSettingMapper.deleteByForegiftId(rentBatteryForegift.getId());
            //删除押金对应的分期详情
            //清空分期记录的settingId
            for (RentInstallmentSetting rentInstallmentSetting : rentInstallmentSettingList) {
                rentInstallmentDetailMapper.deleteBySettingId(rentInstallmentSetting.getId());
                customerInstallmentRecordMapper.clearRentSettingId(rentInstallmentSetting.getId());
            }
        }
        rentBatteryForegiftMapper.update(entity.getMoney(), entity.getBatteryType(), entity.getAgentId(), entity.getMemo(), entity.getId());
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult delete(Long id) {
        RentBatteryForegift rentBatteryForegift = rentBatteryForegiftMapper.find(id);
        //无法删除的押金状态
        List<Integer> foregiftList = Arrays.asList(CustomerForegiftOrder.Status.PAY_OK.getValue(), CustomerForegiftOrder.Status.APPLY_REFUND.getValue());
        List<RentForegiftOrder> rentForegiftOrderList = rentForegiftOrderMapper.findByForegiftIdAndStatus(rentBatteryForegift.getId().longValue(), foregiftList);
        if (rentForegiftOrderList.size() > 0) {
            return ExtResult.failResult("存在使用中的押金，无法删除");
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
        //删除租金对应的分期设置
        List<RentPeriodPrice> rentPeriodPriceList = rentPeriodPriceMapper.findListByForegiftId(id.longValue(), rentBatteryForegift.getBatteryType(), rentBatteryForegift.getAgentId());
        for (RentPeriodPrice rentPeriodPrice : rentPeriodPriceList) {
            List<RentInstallmentSetting> rentInstallmentSettingList1 = rentInstallmentSettingMapper.findByPacketId(rentPeriodPrice.getId());
            rentInstallmentSettingMapper.deleteByPacketId(rentPeriodPrice.getId());
            //删除押金对应的分期详情
            //清空分期记录的settingId
            for (RentInstallmentSetting rentInstallmentSetting : rentInstallmentSettingList1) {
                rentInstallmentDetailMapper.deleteBySettingId(rentInstallmentSetting.getId());
                customerInstallmentRecordMapper.clearRentSettingId(rentInstallmentSetting.getId());
            }
        }
        int total = rentBatteryForegiftMapper.delete(id);
        rentPeriodPriceMapper.deleteByForegift(id.intValue());
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }

        return ExtResult.successResult();
    }

}
