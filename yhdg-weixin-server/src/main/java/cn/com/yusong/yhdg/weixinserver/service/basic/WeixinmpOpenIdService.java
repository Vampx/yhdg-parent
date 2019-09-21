package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpOpenId;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.WeixinmpOpenIdMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeixinmpOpenIdService {
    @Autowired
    WeixinmpOpenIdMapper weixinmpOpenIdMapper;

    public WeixinmpOpenId findByOpenId(int weixinmpId, String openId) {
        return weixinmpOpenIdMapper.findByOpenId(weixinmpId, openId);
    }

    public int insert(WeixinmpOpenId weixinmpOpenId) {
        return weixinmpOpenIdMapper.insert(weixinmpOpenId);
    }


}
