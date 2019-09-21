package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.ImageConvertSize;
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
public class ImageConvertSizeServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.ImageConvertSizeService.find(..))")
    private void find() {}

    @Around("find()")
    public Object findByCustomer(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer id = (Integer) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_IMAGE_CONVERT_SIZE, id);
        ImageConvertSize imageConvertSize = (ImageConvertSize)memCachedClient.get(key);
        if(imageConvertSize != null) {
            return imageConvertSize;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.ImageConvertSizeService.update(..))")
    private void update() {}

    @Around("update()")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        ImageConvertSize imageConvertSize = (ImageConvertSize) joinPoint.getArgs()[0];
        Object result = joinPoint.proceed();

        String key = CacheKey.key(CacheKey.K_ID_V_IMAGE_CONVERT_SIZE, imageConvertSize.getId());
        memCachedClient.delete(key);
        return result;
    }
}
