package cn.com.yusong.yhdg.serviceserver.service.basic;


import cn.com.yusong.yhdg.common.domain.basic.SmsConfigInfo;
import cn.com.yusong.yhdg.serviceserver.persistence.basic.SmsConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsConfigService {
    @Autowired
    SmsConfigMapper smsConfigMapper;

    public List<SmsConfigInfo> findInfoByPartner(int partnerId) {
        return smsConfigMapper.findInfoByPartner(partnerId);
    }
}
