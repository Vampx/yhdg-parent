package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
public class CustomerServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.appserver.service.basic.CustomerService.findLoginToken(..))")
    private void findLoginToken() {}

    @Around("findLoginToken()")
    public Object findLoginToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_CUSTOMER_ID_V_LOGIN_TOKEN, id);
        Object result = memCachedClient.get(key);
        if(result != null) {
            return result;
        }

        result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_DAY);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.appserver.service.basic.CustomerService.updateLoginToken(..))")
    private void updateLoginToken() {}

    @Around("updateLoginToken()")
    public Object updateLoginToken(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_CUSTOMER_ID_V_LOGIN_TOKEN, id);
        memCachedClient.delete(key);
        return joinPoint.proceed();
    }
}
