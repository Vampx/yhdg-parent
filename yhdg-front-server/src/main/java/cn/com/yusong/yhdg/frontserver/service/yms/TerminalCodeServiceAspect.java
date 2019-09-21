package cn.com.yusong.yhdg.frontserver.service.yms;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.yms.TerminalCode;
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
public class TerminalCodeServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.frontserver.service.yms.TerminalCodeService.find(java.lang.String))")
    private void find() {}

    @Around("find()")
    public Object find(ProceedingJoinPoint joinPoint) throws Throwable {
        String id = (String) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_ID_V_TERMINAL_CODE, id);
        TerminalCode terminalCode = (TerminalCode) memCachedClient.get(key);
        if(terminalCode != null) {
            return terminalCode;
        }

        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_DAY);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.frontserver.service.yms.TerminalCodeService.findByCode(java.lang.String))")
    private void findByCode() {}

    @Around("findByCode()")
    public Object findByCode(ProceedingJoinPoint joinPoint) throws Throwable {
        String code = (String) joinPoint.getArgs()[0];
        String key = CacheKey.key(CacheKey.K_CODE_V_TERMINAL_CODE, code);
        TerminalCode terminalCode = (TerminalCode) memCachedClient.get(key);
        if(terminalCode != null) {
            return terminalCode;
        }
        Object result = joinPoint.proceed();
        if(result != null) {
            memCachedClient.set(key, result, MemCachedConfig.CACHE_ONE_DAY);
        }
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.frontserver.service.yms.TerminalCodeService.insert(..))")
    private void insert() {}

    @Around("insert()")
    public Object insert(ProceedingJoinPoint joinPoint) throws Throwable {
        TerminalCode entity = (TerminalCode) joinPoint.getArgs()[0];
        Object result = joinPoint.proceed();

        String idKey = CacheKey.key(CacheKey.K_ID_V_TERMINAL_CODE, entity.getId());
        memCachedClient.set(idKey, entity, MemCachedConfig.CACHE_ONE_DAY);
        String codeKey = CacheKey.key(CacheKey.K_CODE_V_TERMINAL_CODE, entity.getCode());
        memCachedClient.set(codeKey, entity, MemCachedConfig.CACHE_ONE_DAY);

        return result;
    }
}
