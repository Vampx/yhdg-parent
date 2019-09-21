package cn.com.yusong.yhdg.appserver.service.hdg;

import cn.com.yusong.yhdg.appserver.persistence.hdg.ExchangeInstallmentDetailMapper;
import cn.com.yusong.yhdg.common.domain.hdg.ExchangeInstallmentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExchangeInstallmentDetailService {
    @Autowired
    ExchangeInstallmentDetailMapper exchangeInstallmentDetailMapper;

    public List<ExchangeInstallmentDetail> findListBySettingId(Long settingId) {
        return exchangeInstallmentDetailMapper.findListBySettingId(settingId);
    }
}
