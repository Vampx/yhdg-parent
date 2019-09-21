package cn.com.yusong.yhdg.webserver.service.zd;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.zd.RentPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentInstallmentDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.zd.RentPeriodPriceMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RentPeriodPriceService extends AbstractService {
    @Autowired
    private RentPeriodPriceMapper rentPeriodPriceMapper;
    @Autowired
    RentInstallmentSettingMapper rentInstallmentSettingMapper;
    @Autowired
    RentInstallmentDetailMapper rentInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;

    public List<RentPeriodPrice> findListByForegift(Long foregiftId, Integer batteryType, Integer agentId) {
        return rentPeriodPriceMapper.findListByForegift(foregiftId, batteryType, agentId);
    }

    public List<RentPeriodPrice> findListByForegiftId(Long foregiftId, Integer batteryType, Integer agentId) {
        return rentPeriodPriceMapper.findListByForegiftId(foregiftId, batteryType, agentId);
    }

    public RentPeriodPrice find(Long id) {
        return rentPeriodPriceMapper.find(id);
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(RentPeriodPrice entity) {
        entity.setCreateTime(new Date());
        entity.setAgentName(findAgentInfo(entity.getAgentId()).getAgentName());
        rentPeriodPriceMapper.insert(entity);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(RentPeriodPrice entity) {
        RentPeriodPrice dbRentPeriodPrice = rentPeriodPriceMapper.find(entity.getId());
        if (dbRentPeriodPrice.getPrice().intValue() != entity.getPrice().intValue()) {
            //删除租金对应的分期设置
            List<RentInstallmentSetting> rentInstallmentSettingList = rentInstallmentSettingMapper.findByPacketId(entity.getId());
            rentInstallmentSettingMapper.deleteByPacketId(entity.getId());
            //删除押金对应的分期详情
            //清空分期记录的settingId
            for (RentInstallmentSetting rentInstallmentSetting : rentInstallmentSettingList) {
                rentInstallmentDetailMapper.deleteBySettingId(rentInstallmentSetting.getId());
                customerInstallmentRecordMapper.clearRentSettingId(rentInstallmentSetting.getId());
            }
        }
        entity.setAgentName(findAgentInfo(entity.getAgentId()).getAgentName());
        rentPeriodPriceMapper.update(entity);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        //删除租金对应的分期设置
        List<RentInstallmentSetting> rentInstallmentSettingList = rentInstallmentSettingMapper.findByPacketId(id);
        rentInstallmentSettingMapper.deleteByPacketId(id);
        //删除押金对应的分期详情
        //清空分期记录的settingId
        for (RentInstallmentSetting rentInstallmentSetting : rentInstallmentSettingList) {
            rentInstallmentDetailMapper.deleteBySettingId(rentInstallmentSetting.getId());
            customerInstallmentRecordMapper.clearRentSettingId(rentInstallmentSetting.getId());
        }
        int total = rentPeriodPriceMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }

}
