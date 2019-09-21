package cn.com.yusong.yhdg.appserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Aspect
public class DictItemServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.appserver.service.basic.DictItemService.findByCategory(..))")
    private void findByCategory() {}

    @Around("findByCategory()")
    public Object findByCategory(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, id);
        List<DictItem> dictItemList = (List<DictItem>) memCachedClient.get(key);
        if(dictItemList != null) {
            return dictItemList;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.appserver.service.basic.DictItemService.findMapByCategory(..))")
    private void findMapByCategory() {}

    @Around("findMapByCategory()")
    public Object findMapByCategory(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_MAP, id);
        Map<String, String> dictItemMap = (Map<String, String> ) memCachedClient.get(key);
        if(dictItemMap != null) {
            return dictItemMap;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return result;
    }
}
