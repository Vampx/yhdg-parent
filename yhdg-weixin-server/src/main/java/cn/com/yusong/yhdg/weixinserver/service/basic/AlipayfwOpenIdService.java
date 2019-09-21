package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwOpenId;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.AlipayfwOpenIdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayfwOpenIdService {
    @Autowired
    AlipayfwOpenIdMapper alipayfwOpenIdMapper;

    public AlipayfwOpenId findByOpenId(int alipayfwId, String openId) {
        return alipayfwOpenIdMapper.findByOpenId(alipayfwId, openId);
    }

    public int insert(AlipayfwOpenId alipayfwOpenId) {
        return alipayfwOpenIdMapper.insert(alipayfwOpenId);
    }


}
