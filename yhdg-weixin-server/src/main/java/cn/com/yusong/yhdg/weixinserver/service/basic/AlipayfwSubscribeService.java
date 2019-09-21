package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.AlipayfwSubscribe;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.AlipayfwSubscribeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AlipayfwSubscribeService {
    @Autowired
    AlipayfwSubscribeMapper alipayfwSubscribeMapper;

    public AlipayfwSubscribe findByOpenId(int alipayfwId, String openId) {
        return alipayfwSubscribeMapper.findByOpenId(alipayfwId, openId);
    }

    public void subscribe(int alipayfwId, String openId) {
        AlipayfwSubscribe subscribe = alipayfwSubscribeMapper.findByOpenId(alipayfwId, openId);
        if(subscribe == null) {
            subscribe = new AlipayfwSubscribe();
            subscribe.setAlipayfwId(alipayfwId);
            subscribe.setOpenId(openId);
            subscribe.setCreateTime(new Date());
            alipayfwSubscribeMapper.insert(subscribe);
        }
    }

    public void unsubscribe(int alipayfwId, String openId) {
        alipayfwSubscribeMapper.delete(alipayfwId, openId);
    }
}
