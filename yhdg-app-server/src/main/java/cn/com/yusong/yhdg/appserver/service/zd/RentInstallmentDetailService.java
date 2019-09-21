package cn.com.yusong.yhdg.appserver.service.zd;

import cn.com.yusong.yhdg.appserver.persistence.zd.RentInstallmentDetailMapper;
import cn.com.yusong.yhdg.common.domain.zd.RentInstallmentDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentInstallmentDetailService {
    @Autowired
    RentInstallmentDetailMapper rentInstallmentDetailMapper;

    public List<RentInstallmentDetail> findListBySettingId(Long settingId) {
        return rentInstallmentDetailMapper.findListBySettingId(settingId);
    }
}
