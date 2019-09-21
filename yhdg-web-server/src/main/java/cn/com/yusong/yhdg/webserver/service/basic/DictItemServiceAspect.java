package cn.com.yusong.yhdg.webserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.DictItem;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.webserver.persistence.basic.DictItemMapper;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Aspect
public class DictItemServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;
    @Autowired
    DictItemMapper dictItemMapper;

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.DictItemService.findByCategory(..))")
    private void findByCategory() {
    }

    @Around("findByCategory()")
    public Object findByCategory(ProceedingJoinPoint joinPoint) throws Throwable {
        Long categoryId = (Long) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, categoryId);
        List<DictItem> dictItem = (List<DictItem>) memCachedClient.get(key);
        if(dictItem != null) {
            return dictItem;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.DictItemService.update(..))")
    private void update() {}

    @Around("update()")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        DictItem dictItem = (DictItem) joinPoint.getArgs()[0];
        Object result = joinPoint.proceed();

        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictItem.getCategoryId()));
        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_MAP, dictItem.getCategoryId()));
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.DictItemService.insert(..))")
    private void insert() {}

    @Around("insert()")
    public Object insert(ProceedingJoinPoint joinPoint) throws Throwable {
        DictItem dictItem = (DictItem) joinPoint.getArgs()[0];
        Object result = joinPoint.proceed();

        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictItem.getCategoryId()));
        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_MAP, dictItem.getCategoryId()));
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.DictItemService.delete(..))")
    private void delete() {}

    @Around("delete()")
    public Object delete(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        DictItem dictItem = dictItemMapper.find(id);

        Object result = joinPoint.proceed();

        if (dictItem != null) {
            String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictItem.getCategoryId());
            memCachedClient.delete(key);
            memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_MAP, dictItem.getCategoryId()));
        }

        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.webserver.service.basic.DictItemService.updateOrderNum(..))")
    private void updateOrderNum() {}

    @Around("updateOrderNum()")
    public Object updateOrderNum(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        DictItem dictItem = dictItemMapper.find(id);

        Object result = joinPoint.proceed();
        if (dictItem != null) {
            String key = CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_LIST, dictItem.getCategoryId());
            memCachedClient.delete(key);
            memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_DICT_ITEM_MAP, dictItem.getCategoryId()));
        }

        return result;
    }

}
