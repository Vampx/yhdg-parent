package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.VoiceConfig;
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
public class VoiceConfigServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.VoiceConfigService.update(..))")
    private void update() {}

    @Around("update()")
    public void update(ProceedingJoinPoint joinPoint) throws Throwable {
        VoiceConfig config = (VoiceConfig) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_VOICE_CONFIG, config.getAgentId());
        memCachedClient.delete(key);
        joinPoint.proceed();
    }
}
