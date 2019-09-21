package cn.com.yusong.yhdg.agentserver.service.yms;


import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.entity.json.ExtResult;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ymsTerminalStrategyServiceAspect")
@Aspect
public class TerminalStrategyServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.yms.TerminalStrategyService.find(..))")
    private void find() {}

    @Around("find()")
    public Object find(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, id);
        TerminalStrategy strategy = (TerminalStrategy) memCachedClient.get(key);
        if(strategy != null) {
            return strategy;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.yms.TerminalStrategyService.update(..))")
    private void update() {}

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.yms.TerminalStrategyService.delete(..))")
    public void delete(){}

    @Around("update()")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer result = (Integer)joinPoint.proceed();
        if(result > 0) {
            TerminalStrategy strategy = (TerminalStrategy) joinPoint.getArgs()[0];
            memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, strategy.getId()));
            memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY_XML, strategy.getId()));

        }
        return result;
    }

    @Around("delete()")
    public Object delete(ProceedingJoinPoint joinPoint) throws Throwable {
        ExtResult result = (ExtResult)joinPoint.proceed();
        if(result.isSuccess()) {
            Long id = (Long) joinPoint.getArgs()[0];
            memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY, id));
            memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY_XML, id));
        }
        return result;
    }
}
