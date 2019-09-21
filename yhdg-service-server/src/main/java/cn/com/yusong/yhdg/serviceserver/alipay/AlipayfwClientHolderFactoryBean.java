package cn.com.yusong.yhdg.serviceserver.alipay;

import cn.com.yusong.yhdg.common.domain.basic.Alipayfw;
import cn.com.yusong.yhdg.common.domain.basic.Partner;
import cn.com.yusong.yhdg.common.tool.alipay.AlipayfwClientHolder;
import cn.com.yusong.yhdg.common.tool.alipay.CustomAlipayClient;
import cn.com.yusong.yhdg.serviceserver.service.basic.AlipayfwService;
import cn.com.yusong.yhdg.serviceserver.service.basic.PartnerService;
import com.alipay.api.AlipayClient;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlipayfwClientHolderFactoryBean implements FactoryBean<AlipayfwClientHolder> {

    static Logger log = LogManager.getLogger(AlipayfwClientHolderFactoryBean.class);

    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String SIGN_TYPE = "RSA2";

    @Autowired
    PartnerService partnerService;
    @Autowired
    AlipayfwService alipayfwService;


    @Override
    public AlipayfwClientHolder getObject() throws Exception {
        AlipayfwClientHolder alipayClientHolder = new AlipayfwClientHolder(new AlipayfwClientHolder.Store() {
            @Override
            public AlipayClient obtainPartner(int id) {
                Partner partner = partnerService.find(id);
                if (partner == null) {
                    return null;
                }
                return createFromPartner(partner);
            }

            @Override
            public AlipayClient obtainAlipayfw(int id) {
                Alipayfw alipayfw = alipayfwService.find(id);
                if (alipayfw == null) {
                    return null;
                }
                return createFromAlipayfw(alipayfw);
            }
        });

        List<Partner> partnerList = partnerService.findAll();
        for (Partner partner : partnerList) {
            alipayClientHolder.putPartner(partner.getId(), createFromPartner(partner));
        }

        List<Alipayfw> alipayfwList = alipayfwService.findAll();
        for(Alipayfw alipayfw : alipayfwList) {
            if (StringUtils.isNotEmpty(alipayfw.getAppId())) {
                alipayClientHolder.putAlipayfw(alipayfw.getId(), createFromAlipayfw(alipayfw));
            }
        }

        return alipayClientHolder;
    }

    @Override
    public Class<?> getObjectType() {
        return AlipayfwClientHolder.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private CustomAlipayClient createFromPartner(Partner partner) {
        CustomAlipayClient alipayClient = new CustomAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                partner.getFwAppId(),
                partner.getFwPriKey(),
                "json",
                CHARSET_UTF_8,
                partner.getFwAliKey(),
                SIGN_TYPE);
        alipayClient.appId = 0;
        return alipayClient;
    }

    private CustomAlipayClient createFromAlipayfw(Alipayfw alipayfw) {
        String fwPrikey = alipayfw.getPriKey();
        String fwAliKey = alipayfw.getAliKey();

        CustomAlipayClient alipayClient = new CustomAlipayClient(
                "https://openapi.alipay.com/gateway.do",
                alipayfw.getAppId(),
                fwPrikey,
                "json",
                CHARSET_UTF_8,
                fwAliKey,
                SIGN_TYPE);
        alipayClient.appId = alipayfw.getId();
        return alipayClient;
    }
}
