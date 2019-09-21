package cn.com.yusong.yhdg.appserver.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.com.yusong.yhdg.appserver.persistence.basic.WeixinmaMapper;
import cn.com.yusong.yhdg.common.domain.basic.Weixinma;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.weixin.WxMaMemcachedConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WxMaServiceHolder {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    WeixinmaMapper weixinmaMapper;

    Map<Integer, WxMaService> maServiceMap = new ConcurrentHashMap<Integer, WxMaService>();

    @PostConstruct
    public void init() {
        List<Weixinma> weixinmaList = weixinmaMapper.findAll();
        for (Weixinma weixinma : weixinmaList) {
            maServiceMap.put(weixinma.getId(), buildWxMaService(weixinma));
        }
    }

    public WxMaService get(int id) {
        WxMaService wxMaService = maServiceMap.get(id);
        if (wxMaService == null) {
            synchronized (WxMaServiceHolder.class) {
                wxMaService = maServiceMap.get(id);
                if (wxMaService != null) {
                    return wxMaService;
                }

                Weixinma weixinma = weixinmaMapper.find(id);
                if (weixinma == null) {
                    return null;
                }

                maServiceMap.put(weixinma.getId(), wxMaService = buildWxMaService(weixinma));
                return wxMaService;
            }
        }

        return wxMaService;
    }


    private WxMaService buildWxMaService(Weixinma weixinma) {
        WxMaMemcachedConfig config = new WxMaMemcachedConfig(memCachedClient, weixinma.getId());
        config.setAppid(weixinma.getAppId());
        config.setSecret(weixinma.getAppSecret());
        config.setToken(null);
        config.setAesKey(null);
        config.setMsgDataFormat("JSON");

        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        return service;
    }
}
