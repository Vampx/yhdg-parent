package cn.com.yusong.yhdg.agentserver.service.zd;

import cn.com.yusong.yhdg.agentserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentInstallmentDetailMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentInstallmentSettingMapper;
import cn.com.yusong.yhdg.agentserver.persistence.zd.RentInsuranceMapper;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.zd.RentInsurance;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class RentInsuranceService extends AbstractService {
    @Autowired
    private RentInsuranceMapper rentInsuranceMapper;
    @Autowired
    RentInstallmentSettingMapper rentInstallmentSettingMapper;
    @Autowired
    RentInstallmentDetailMapper rentInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;

    public List<RentInsurance> findListByBatteryType(Integer batteryType, Integer agentId) {
        return rentInsuranceMapper.findListByBatteryType(batteryType, agentId);
    }

    public RentInsurance find(Long id) {
        return rentInsuranceMapper.find(id);
    }

    public Page findPage(RentInsurance search) {
        Page page = search.buildPage();
        page.setTotalItems(rentInsuranceMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<RentInsurance> insuranceList = rentInsuranceMapper.findPageResult(search);
        for (RentInsurance insurance : insuranceList) {
            insurance.setAgentName(findAgentInfo(insurance.getAgentId()).getAgentName());
            insurance.setBatteryTypeName(findBatteryType(insurance.getBatteryType()).getTypeName());
        }
        page.setResult(insuranceList);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(RentInsurance insurance) {
        insurance.setCreateTime(new Date());
        rentInsuranceMapper.insert(insurance);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(RentInsurance insurance) {
        RentInsurance dbInsurance = rentInsuranceMapper.find(insurance.getId());
        if (dbInsurance.getPrice().intValue() != insurance.getPrice().intValue()) {
            //删除保险对应的分期设置
            List<RentInstallmentSetting> rentInstallmentSettingList = rentInstallmentSettingMapper.findByInsuranceId(insurance.getId());
            rentInstallmentSettingMapper.deleteByInsuranceId(insurance.getId());
            //删除押金对应的分期详情
            //清空分期记录的settingId
            for (RentInstallmentSetting rentInstallmentSetting : rentInstallmentSettingList) {
                rentInstallmentDetailMapper.deleteBySettingId(rentInstallmentSetting.getId());
                customerInstallmentRecordMapper.clearRentSettingId(rentInstallmentSetting.getId());
            }
        }
        rentInsuranceMapper.update(insurance);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        //删除保险对应的分期设置
        List<RentInstallmentSetting> rentInstallmentSettingList = rentInstallmentSettingMapper.findByInsuranceId(id);
        rentInstallmentSettingMapper.deleteByInsuranceId(id);
        //删除押金对应的分期详情
        //清空分期记录的settingId
        for (RentInstallmentSetting rentInstallmentSetting : rentInstallmentSettingList) {
            rentInstallmentDetailMapper.deleteBySettingId(rentInstallmentSetting.getId());
            customerInstallmentRecordMapper.clearRentSettingId(rentInstallmentSetting.getId());
        }
        int total = rentInsuranceMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
