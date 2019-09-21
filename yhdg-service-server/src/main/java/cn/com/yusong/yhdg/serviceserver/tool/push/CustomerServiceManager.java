package cn.com.yusong.yhdg.serviceserver.tool.push;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.serviceserver.service.basic.SystemConfigService;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/*客户的推送*/
@Component
public class CustomerServiceManager extends AbstractServiceManager {
    static Logger log = LogManager.getLogger(CustomerServiceManager.class);
    @Autowired
    protected SystemConfigService systemConfigService;
    @PostConstruct
    public void init() { //初始化推送服务


        Map<String, String> map = systemConfigService.findMap();
        buildJpushPushService(map);
        buildXiaomiPushService(map);


    }

    @Override
    public void buildJpushPushService(Map<String, String> map) {
        String jpushMasterSecret = map.get(ConstEnum.SystemConfigKey.CUSTOMER_JPUSH_MASTER_SECRET.getValue());
        String jpushAppKey = map.get(ConstEnum.SystemConfigKey.CUSTOMER_JPUSH_APP_KEY.getValue());
        log.debug("jpushMasterSecret: {}, jpushAppKey: {}", jpushMasterSecret, jpushAppKey);

        if(StringUtils.isNotEmpty(jpushAppKey) && StringUtils.isNotEmpty(jpushMasterSecret)) {
            jpushPushService = new JpushPushService(jpushMasterSecret, jpushAppKey);
        }

    }

    @Override
    public void buildXiaomiPushService(Map<String, String> map) {
        String pushPackage = map.get(ConstEnum.SystemConfigKey.CUSTOMER_PUSH_PACKAGE.getValue());
        String xiaomiAppSecret = map.get(ConstEnum.SystemConfigKey.CUSTOMER_XIAOMI_APP_SECRET.getValue());
        int formalPlatform = Integer.parseInt(map.get(ConstEnum.SystemConfigKey.FORMAL_PLATFORM.getValue()));
        log.debug("pushPackage: {}, xiaomiAppSecret: {}, formalPlatform: {}", pushPackage, xiaomiAppSecret, formalPlatform);

        if(StringUtils.isNotEmpty(xiaomiAppSecret)) {
            xiaomiPushService = new XiaomiPushService(formalPlatform, xiaomiAppSecret, pushPackage);
        }
    }

    @Override
    public void buildHWPushService(Map<String, String> map) {

    }

}
