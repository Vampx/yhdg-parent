package cn.com.yusong.yhdg.frontserver.service.yms;


import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.yms.TerminalStrategy;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedConfig;
import cn.com.yusong.yhdg.frontserver.entity.StrategyXml;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
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

    @Pointcut("execution(* cn.com.yusong.yhdg.frontserver.service.yms.TerminalStrategyService.find(..))")
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

    @Pointcut("execution(* cn.com.yusong.yhdg.frontserver.service.yms.TerminalStrategyService.findStrategyXml(..))")
    private void findStrategyXml() {}

    @Around("findStrategyXml()")
    public Object findStrategyXml(ProceedingJoinPoint joinPoint) throws Throwable {
        Long id = (Long) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_STRATEGY_XML, id);
        StrategyXml strategyXml = (StrategyXml) memCachedClient.get(key);
        if(strategyXml != null) {
            return strategyXml;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_WEEK);
        }
        return result;
    }

}
