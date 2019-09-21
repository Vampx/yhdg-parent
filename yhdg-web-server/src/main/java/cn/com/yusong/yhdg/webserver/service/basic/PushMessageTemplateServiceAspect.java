package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.PushMessageTemplate;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class PushMessageTemplateServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.PushMessageTemplateService.update(..))")
    private void update() {}

    @Around("update()")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        PushMessageTemplate template = (PushMessageTemplate) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_PUSH_MESSAGE_TEMPLATE, template.getId());
        memCachedClient.delete(key);
        return joinPoint.proceed();
    }
}
