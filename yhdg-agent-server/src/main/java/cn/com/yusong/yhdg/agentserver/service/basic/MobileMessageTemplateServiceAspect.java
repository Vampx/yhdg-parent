package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.MobileMessageTemplate;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MobileMessageTemplateServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.MobileMessageTemplateService.find(..))")
    private void find() {}

    @Around("find()")
    public Object findByCustomer(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer agentId = (Integer) joinPoint.getArgs()[0];
        Long id = (Long) joinPoint.getArgs()[1];
        String key = CacheKey.key(CacheKey.K_ID_V_MOBILE_MESSAGE_TEMPLATE, agentId, id);
        MobileMessageTemplate mobileMessageTemplate = (MobileMessageTemplate)memCachedClient.get(key);
        if(mobileMessageTemplate != null) {
            return mobileMessageTemplate;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.MobileMessageTemplateService.update(..))")
    private void update() {}

    @Around("update()")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        MobileMessageTemplate mobileMessageTemplate = (MobileMessageTemplate) joinPoint.getArgs()[0];
        Object result = joinPoint.proceed();

        String key = CacheKey.key(CacheKey.K_ID_V_MOBILE_MESSAGE_TEMPLATE, mobileMessageTemplate.getPartnerId(), mobileMessageTemplate.getId());
        memCachedClient.delete(key);
        return result;
    }
}
