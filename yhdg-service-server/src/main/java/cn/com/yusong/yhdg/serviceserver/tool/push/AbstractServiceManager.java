package cn.com.yusong.yhdg.serviceserver.tool.push;

import cn.com.yusong.yhdg.common.constant.ConstEnum;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.serviceserver.service.basic.SystemConfigService;
import com.xiaomi.xmpush.server.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

public abstract class AbstractServiceManager {

    @Autowired
    protected SystemConfigService systemConfigService;

    protected PushService huaweiPushService;
    protected PushService xiaomiPushService;
    protected PushService meizuPushService;
    protected PushService jpushPushService;

    public PushService getPushService(ConstEnum.PushType pushType) {
        if(pushType == ConstEnum.PushType.HUAWEI) {
            return huaweiPushService;
        } else if(pushType == ConstEnum.PushType.XIAOMI) {
            return xiaomiPushService;
        } else if(pushType == ConstEnum.PushType.MEIZU) {
            return meizuPushService;
        } else if(pushType == ConstEnum.PushType.JPUSH || pushType == ConstEnum.PushType.IOS) {
            return jpushPushService;
        } else {
            throw new IllegalArgumentException("无效的推送类型[" + pushType.getName() + "]");
        }
    }
    public abstract void buildJpushPushService(Map<String, String> map);
    public abstract void buildXiaomiPushService(Map<String, String> map);
    public abstract void buildHWPushService(Map<String, String> map);
}
