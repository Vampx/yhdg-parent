package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.SystemConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class SystemConfigServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.SystemConfigService.findConfigValue(..))")
    private void findConfigValue() {}

    @Around("findConfigValue()")
    public Object findConfigValue(ProceedingJoinPoint joinPoint) throws Throwable {
        String id = (String) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, id);
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

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.SystemConfigService.update(..))")
    private void update() {}

    @Around("update()")
    public void update(ProceedingJoinPoint joinPoint) throws Throwable {
        SystemConfig systemConfig = (SystemConfig) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_CONFIG_VALUE, systemConfig.getId());
        memCachedClient.delete(key);
        joinPoint.proceed();
    }
}
