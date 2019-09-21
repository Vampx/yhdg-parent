package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.VoiceMessageTemplate;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class VoiceMessageTemplateServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.VoiceMessageTemplateService.find(..))")
    private void find() {}

    @Around("find()")
    public Object find(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer partnerId = (Integer) joinPoint.getArgs()[0];
        Long id = (Long) joinPoint.getArgs()[1];
        String key = CacheKey.key(CacheKey.K_ID_V_VOICE_MESSAGE_TEMPLATE, id, partnerId);
        String value = (String) memCachedClient.get(key);
        if(value != null) {
            return value;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.VoiceMessageTemplateService.update(..))")
    private void update() {}

    @Around("update()")
    public void update(ProceedingJoinPoint joinPoint) throws Throwable {
        VoiceMessageTemplate template = (VoiceMessageTemplate) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_VOICE_MESSAGE_TEMPLATE, template.getId(), template.getPartnerId());
        memCachedClient.delete(key);
        joinPoint.proceed();
    }
}
