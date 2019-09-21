package cn.com.yusong.yhdg.webserver.config;

import cn.com.yusong.yhdg.common.entity.AreaCache;
import cn.com.yusong.yhdg.webserver.service.basic.AreaService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringApplicationContextAware implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        AreaService areaService = context.getBean(AreaService.class);
        AreaCache areaCache = context.getBean(AreaCache.class);
        areaCache.set(areaService.findAll(), areaService.findAllCity());
    }
}
