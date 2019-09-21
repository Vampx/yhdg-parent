package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentSettingMapper;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExchangeInstallmentSettingService {
    @Autowired
    ExchangeInstallmentSettingMapper exchangeInstallmentSettingMapper;

    public ExchangeInstallmentSetting findByMobile(String mobile) {
        return exchangeInstallmentSettingMapper.findByMobile(mobile);
    }

    public ExchangeInstallmentSetting find(long id) {
        return exchangeInstallmentSettingMapper.find(id);
    }
}
