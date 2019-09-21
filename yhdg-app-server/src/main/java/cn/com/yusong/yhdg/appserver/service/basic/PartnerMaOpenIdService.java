package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.PartnerMaOpenIdMapper;
import cn.com.yusong.yhdg.common.domain.basic.PartnerMaOpenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerMaOpenIdService {
    @Autowired
    PartnerMaOpenIdMapper partnerMaOpenIdMapper;

    public PartnerMaOpenId findByOpenId(int partnerId, String openId) {
        return partnerMaOpenIdMapper.findByOpenId(partnerId, openId);
    }

    public int insert(PartnerMaOpenId openId) {
        return partnerMaOpenIdMapper.insert(openId);
    }

    public int update(int partnerId, String openId, String nickname, String photoPath) {
        return partnerMaOpenIdMapper.update(partnerId, openId, nickname, photoPath);
    }

    public int updateCustomerId(int partnerId, String openId, Long customerId){
        return partnerMaOpenIdMapper.updateCustomerId(partnerId, openId, customerId);
    }


    public int updateSessionKey(long id, String sessionKey) {
        return partnerMaOpenIdMapper.updateSessionKey(id, sessionKey);
    }
}
