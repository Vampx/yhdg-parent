package cn.com.yusong.yhdg.agentappserver.weixin;

import cn.com.yusong.yhdg.agentappserver.service.basic.PartnerService;
import cn.com.yusong.yhdg.agentappserver.service.basic.SystemConfigService;
import cn.com.yusong.yhdg.agentappserver.service.basic.WeixinmpService;
import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.constant.Constant;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpMemcachedConfigStorage;
import cn.com.yusong.yhdg.common.tool.weixin.WxMpServiceHolder;
import cn.com.yusong.yhdg.common.tool.weixin.WxPayServiceHolder;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
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
public class WxPayServiceHolderFactoryBean implements FactoryBean<WxPayServiceHolder> {

    static Logger log = LogManager.getLogger(WxPayServiceHolderFactoryBean.class);

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    PartnerService partnerService;
    @Autowired
    SystemConfigService systemConfigService;


    @Override
    public WxPayServiceHolder getObject() throws Exception {
        WxPayServiceHolder wxPayServiceHolder = new WxPayServiceHolder(new WxPayServiceHolder.Store() {
            @Override
            public WxPayService obtainPartner(int id) {
                Partner partner = partnerService.find(id);
                if (partner == null) {
                    return null;
                }
                return create(partner);
            }

            @Override
            public WxPayService obtainPartnerMp(int id) {
                Partner partner = partnerService.find(id);
                if (partner == null) {
                    return null;
                }
                return createForMp(partner);
            }

            @Override
            public WxPayService obtainPartnerMa(int id) {
                Partner partner = partnerService.find(id);
                if (partner == null) {
                    return null;
                }
                return createForMa(partner);
            }
        });

        List<Partner> partnerList = partnerService.findAll();
        for (Partner partner : partnerList) {
            wxPayServiceHolder.putMp(partner.getId(), createForMp(partner));

            wxPayServiceHolder.putMa(partner.getId(), createForMa(partner));
        }

        return wxPayServiceHolder;
    }

    @Override
    public Class<?> getObjectType() {
        return WxPayServiceHolder.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private WxPayService create(Partner partner) {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(partner.getWeixinAppId());
        config.setMchId(partner.getWeixinPartnerCode());
        config.setMchKey(partner.getWeixinPartnerKey());
        config.setNotifyUrl(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.STATIC_URL.getValue()) + Constant.WEIXIN_PAY_OK);

        WxPayService service = new WxPayServiceImpl();
        service.setConfig(config);
        return service;
    }

    private WxPayService createForMp(Partner partner) {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(partner.getMpAppId());
        config.setMchId(partner.getMpPartnerCode());
        config.setMchKey(partner.getMpPartnerKey());
        config.setNotifyUrl(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.STATIC_URL.getValue()) + Constant.WEIXINMP_PAY_OK);

        WxPayService service = new WxPayServiceImpl();
        service.setConfig(config);
        return service;
    }

    private WxPayService createForMa(Partner partner) {
        WxPayConfig config = new WxPayConfig();
        config.setAppId(partner.getMaAppId());
        config.setMchId(partner.getMaPartnerCode());
        config.setMchKey(partner.getMaPartnerKey());
        config.setNotifyUrl(systemConfigService.findConfigValue(ConstEnum.SystemConfigKey.STATIC_URL.getValue()) + Constant.WEIXINMA_PAY_OK);

        WxPayService service = new WxPayServiceImpl();
        service.setConfig(config);
        return service;
    }

}
