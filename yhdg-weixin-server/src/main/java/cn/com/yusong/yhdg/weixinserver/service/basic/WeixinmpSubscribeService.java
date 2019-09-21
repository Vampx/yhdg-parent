package cn.com.yusong.yhdg.weixinserver.service.basic;

import cn.com.yusong.yhdg.common.domain.basic.WeixinmpSubscribe;
import cn.com.yusong.yhdg.weixinserver.persistence.basic.WeixinmpSubscribeMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class WeixinmpSubscribeService {
    @Autowired
    WeixinmpSubscribeMapper weixinmpSubscribeMapper;

    public WeixinmpSubscribe findByOpenId(int weixinmpId, String openId) {
        return weixinmpSubscribeMapper.findByOpenId(weixinmpId, openId);
    }

    public void subscribe(int weixinmpId, String openId) {
        WeixinmpSubscribe subscribe = weixinmpSubscribeMapper.findByOpenId(weixinmpId, openId);
        if(subscribe == null) {
            subscribe = new WeixinmpSubscribe();
            subscribe.setWeixinmpId(weixinmpId);
            subscribe.setOpenId(openId);
            subscribe.setCreateTime(new Date());
            weixinmpSubscribeMapper.insert(subscribe);
        }
    }

    public void unsubscribe(int weixinmpId, String openId) {
        weixinmpSubscribeMapper.delete(weixinmpId, openId);
    }
}
