package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.RentInstallmentSettingMapper;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RentInstallmentSettingService {
    @Autowired
    RentInstallmentSettingMapper rentInstallmentSettingMapper;

    public RentInstallmentSetting findByMobile(String mobile) {
        return rentInstallmentSettingMapper.findByMobile(mobile);
    }
}
