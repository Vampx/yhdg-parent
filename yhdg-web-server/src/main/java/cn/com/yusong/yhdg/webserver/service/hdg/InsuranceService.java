package cn.com.yusong.yhdg.webserver.service.hdg;

import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import cn.com.yusong.yhdg.common.domain.hdg.Insurance;
import cn.com.yusong.yhdg.common.domain.hdg.PacketPeriodPrice;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.entity.pagination.Page;
import cn.com.yusong.yhdg.webserver.persistence.basic.CustomerInstallmentRecordMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentDetailMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.webserver.persistence.hdg.InsuranceMapper;
import cn.com.yusong.yhdg.webserver.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class InsuranceService extends AbstractService {

    @Autowired
    InsuranceMapper insuranceMapper;
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;
    @Autowired
    ExchangeInstallmentDetailMapper exchangeInstallmentDetailMapper;
    @Autowired
    CustomerInstallmentRecordMapper customerInstallmentRecordMapper;

    public Insurance find(Long id) {
        return insuranceMapper.find(id);
    }

    public List<Insurance> findListByBatteryType(Integer batteryType, Integer agentId) {
        return insuranceMapper.findListByBatteryType(batteryType,agentId);
    }

    public Page findPage(Insurance search) {
        Page page = search.buildPage();
        page.setTotalItems(insuranceMapper.findPageCount(search));
        search.setBeginIndex(page.getOffset());
        List<Insurance> insuranceList = insuranceMapper.findPageResult(search);
        for (Insurance insurance : insuranceList) {
            insurance.setAgentName(findAgentInfo(insurance.getAgentId()).getAgentName());
            insurance.setBatteryTypeName(findBatteryType(insurance.getBatteryType()).getTypeName());
        }
        page.setResult(insuranceList);
        return page;
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult create(Insurance insurance) {
        insurance.setCreateTime(new Date());
        insuranceMapper.insert(insurance);
        return ExtResult.successResult();
    }

    @Transactional(rollbackFor = Throwable.class)
    public ExtResult update(Insurance insurance) {
        Insurance dbInsurance = insuranceMapper.find(insurance.getId());
        if (dbInsurance.getPrice().intValue() != insurance.getPrice().intValue()) {
            //删除保险对应的分期设置
            List<ExchangeInstallmentSetting> exchangeInstallmentSettingList = exchangeInstallmentSettingMapper.findByInsuranceId(insurance.getId());
            exchangeInstallmentSettingMapper.deleteByInsuranceId(insurance.getId());
            //删除押金对应的分期详情
            //清空分期记录的settingId
            for (ExchangeInstallmentSetting exchangeInstallmentSetting : exchangeInstallmentSettingList) {
                exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
                customerInstallmentRecordMapper.clearExchangeSettingId(exchangeInstallmentSetting.getId());
            }
        }
        insuranceMapper.update(insurance);
        return ExtResult.successResult();
    }

    public ExtResult delete(Long id) {
        //删除保险对应的分期设置
        List<ExchangeInstallmentSetting> exchangeInstallmentSettingList = exchangeInstallmentSettingMapper.findByInsuranceId(id);
        exchangeInstallmentSettingMapper.deleteByInsuranceId(id);
        //删除押金对应的分期详情
        //清空分期记录的settingId
        for (ExchangeInstallmentSetting exchangeInstallmentSetting : exchangeInstallmentSettingList) {
            exchangeInstallmentDetailMapper.deleteBySettingId(exchangeInstallmentSetting.getId());
            customerInstallmentRecordMapper.clearExchangeSettingId(exchangeInstallmentSetting.getId());
        }
        int total = insuranceMapper.delete(id);
        if (total == 0) {
            return ExtResult.failResult("操作失败！");
        }
        return ExtResult.successResult();
    }
}
