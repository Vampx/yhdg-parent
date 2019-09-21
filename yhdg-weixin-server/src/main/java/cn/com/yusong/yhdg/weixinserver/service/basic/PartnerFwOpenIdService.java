package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.PartnerFwOpenId;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.PartnerFwOpenIdMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartnerFwOpenIdService {
    @Autowired
    PartnerFwOpenIdMapper partnerFwOpenIdMapper;

    public PartnerFwOpenId findByOpenId(int partnerId, String openId) {
        return partnerFwOpenIdMapper.findByOpenId(partnerId, openId);
    }

    public int insert(PartnerFwOpenId openId) {
        return partnerFwOpenIdMapper.insert(openId);
    }

    public int update(int partnerId, String openId,  String nickname, String photoPath) {
        return partnerFwOpenIdMapper.update(partnerId, openId, nickname, photoPath);
    }
}
