package cn.com.yusong.yhdg.routeserver.service.hdg;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.hdg.CabinetCode;
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
public class CabinetCodeServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.routeserver.service.hdg.CabinetCodeService.find(java.lang.String))")
    private void find() {}

    @Around("find()")
    public Object find(ProceedingJoinPoint joinPoint) throws Throwable {
        String id = (String) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_SUBCABINET_CODE, id);
        CabinetCode subcabinetCode = (CabinetCode) memCachedClient.get(key);
        if(subcabinetCode != null) {
            return subcabinetCode;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_HOUR);
        }
        return result;
    }
}
