package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.appserver.persistence.basic.PartnerMpOpenIdMapper;
import cn.com.yusong.yhdg.common.domain.basic.PartnerMpOpenId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerMpOpenIdService {
    @Autowired
    PartnerMpOpenIdMapper partnerMpOpenIdMapper;

    public PartnerMpOpenId findByOpenId(int partnerId, String openId) {
        return partnerMpOpenIdMapper.findByOpenId(partnerId, openId);
    }

    public int insert(PartnerMpOpenId openId) {
        return partnerMpOpenIdMapper.insert(openId);
    }

    public int update(int partnerId, String openId, String nickname, String photoPath) {
        return partnerMpOpenIdMapper.update(partnerId, openId, nickname, photoPath);
    }

    public int updateCustomerId(int partnerId, String openId, Long customerId){
        return partnerMpOpenIdMapper.updateCustomerId(partnerId, openId, customerId);
    }
}
