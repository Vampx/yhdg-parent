package cn.com.yusong.yhdg.agentserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.agentserver.config.AppConfig;
import cn.com.yusong.yhdg.agentserver.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccreditService extends AbstractService {
    static Logger log = LoggerFactory.getLogger(AccreditService.class);

    @Autowired
    AppConfig appConfig;

    public void getAccreditTime() {
        try {
            String key = findConfigValue(ConstEnum.SystemConfigKey.ACCREDIT_KEY.getValue());
            Long expiryTime = getAccreditTime(key, "agentserver");
            log.debug("服务授权接口返回=" + expiryTime);
            if (expiryTime != 0) {
                appConfig.setExpiryTime(expiryTime);
            }
        } catch (Exception e) {
            log.error("服务授权接口出错=" + e.getMessage());
        }
    }
}
