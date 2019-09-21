package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.Agent;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AgentServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.AgentService.update(..))")
    private void update() {}

    @Around("update()")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        Agent agent = (Agent) joinPoint.getArgs()[0];
        Object result = joinPoint.proceed();
        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_AGENT_INFO, agent.getId()));
        return result;
    }

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.AgentService.delete(..))")
    private void delete() {}

    @Around("delete()")
    public Object delete(ProceedingJoinPoint joinPoint) throws Throwable {
        Integer id = (Integer) joinPoint.getArgs()[0];
        Object result = joinPoint.proceed();
        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_AGENT_INFO, id));
        return result;
    }
}
