package cn.com.yusong.yhdg.agentappserver.config;

import cn.com.yusong.yhdg.agentappserver.service.basic.AreaService;
import cn.com.yusong.yhdg.common.entity.AreaCache;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContextAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        AreaCache areaCache = context.getBean(AreaCache.class);
        areaCache.set(context.getBean(AreaService.class).findAll());
    }
}
