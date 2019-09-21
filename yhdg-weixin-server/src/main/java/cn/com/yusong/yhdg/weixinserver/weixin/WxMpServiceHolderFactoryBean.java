package cn.com.yusong.yhdg.weixinserver.weixin;

import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpMemcachedConfigStorage;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.weixinserver.service.basic.PartnerService;
import cn.com.yusong.yhdg.weixinserver.service.basic.WeixinmpService;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WxMpServiceHolderFactoryBean implements FactoryBean<WxMpServiceHolder> {

    static Logger log = LogManager.getLogger(WxMpServiceHolderFactoryBean.class);

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    PartnerService partnerService;
    @Autowired
    WeixinmpService weixinmpService;

    @Override
    public WxMpServiceHolder getObject() throws Exception {
        WxMpServiceHolder wxMpServiceHolder = new WxMpServiceHolder(new WxMpServiceHolder.Store() {
            @Override
            public WxMpService obtainPartner(int id) {
                Partner partner = partnerService.find(id);
                if (partner == null) {
                    return null;
                }
                return createFromPartner(partner);
            }

            @Override
            public WxMpService obtainWeixinmp(int id) {
                Weixinmp weixinmp = weixinmpService.find(id);
                if (weixinmp == null) {
                    return null;
                }
                return createFromWeixinmp(weixinmp);
            }
        });

        List<Partner> partnerList = partnerService.findAll();
        for (Partner partner : partnerList) {
            wxMpServiceHolder.putPartner(partner.getId(), createFromPartner(partner));
        }

        List<Weixinmp> weixinmpList = weixinmpService.findAll();
        for(Weixinmp weixinmp : weixinmpList) {
            if(StringUtils.isNotEmpty(weixinmp.getAppId())) {
                wxMpServiceHolder.putWeixinmp(weixinmp.getId(), createFromWeixinmp(weixinmp));
            }
        }
        return wxMpServiceHolder;
    }

    @Override
    public Class<?> getObjectType() {
        return WxMpServiceHolder.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private WxMpService createFromPartner(Partner partner) {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(partner.getMpAppId()); // 设置微信公众号的appid
        config.setSecret(partner.getMpAppSecret()); // 设置微信公众号的app corpSecret
        config.setToken(null); // 设置微信公众号的token
        config.setAesKey(null); // 设置微信公众号的EncodingAESKey

        WxMpService wxService = new WxMpServiceImpl();// 实际项目中请注意要保持单例，不要在每次请求时构造实例，具体可以参考demo项目
        wxService.setWxMpConfigStorage(config);

        return wxService;
    }

    private WxMpService createFromWeixinmp(Weixinmp weixinmp) {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(weixinmp.getAppId()); // 设置微信公众号的appid
        config.setSecret(weixinmp.getAppSecret()); // 设置微信公众号的app corpSecret
        config.setToken(null); // 设置微信公众号的token
        config.setAesKey(null); // 设置微信公众号的EncodingAESKey

        WxMpService wxService = new WxMpServiceImpl();// 实际项目中请注意要保持单例，不要在每次请求时构造实例，具体可以参考demo项目
        wxService.setWxMpConfigStorage(config);

        return wxService;
    }

}
