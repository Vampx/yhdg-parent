package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.Weixinmp;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class WeixinmpServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.WeixinmpService.find(..))")
    private void find() {}
    @Around("find()")
    public Object find(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer id = (Integer) joinPoint.getArgs()[0];
        if (id == null) {
            return joinPoint.proceed();
        }
        String key = CacheKey.key(CacheKey.K_ID_V_WEIXINMP, id);
        Weixinmp v = (Weixinmp) memCachedClient.get(key);
        if (v != null) {
            return v;
        }

        Object result = joinPoint.proceed();
        if (result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_DAY);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.WeixinmpService.update(..))")
    private void update() {}
    @Around("update()")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        Weixinmp entity = (Weixinmp) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_WEIXINMP, entity.getId());
        memCachedClient.delete(key);
        return joinPoint.proceed();
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.WeixinmpService.delete(..))")
    private void delete() {}
    @Around("delete()")
    public Object delete(ProceedingJoinPoint joinPoint) throws Throwable {
        int id = (Integer) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_WEIXINMP, id);
        memCachedClient.delete(key);
        return joinPoint.proceed();
    }

}
