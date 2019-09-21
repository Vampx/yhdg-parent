package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerMpOpenId;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.PartnerMpOpenIdMapper;
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

    public int update(int partnerId, String openId,  String nickname, String photoPath) {
        return partnerMpOpenIdMapper.update(partnerId, openId, nickname, photoPath);
    }
}
