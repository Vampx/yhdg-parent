package cn.com.yusong.yhdg.agentserver.service.basic;

import cn.com.yusong.yhdg.common.constant.CacheKey;
import cn.com.yusong.yhdg.common.domain.basic.Customer;
import cn.com.yusong.yhdg.common.tool.memcached.MemCachedClient;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CustomerServiceAspect {

    @Autowired
    MemCachedClient memCachedClient;

    @Pointcut("execution(* cn.com.yusong.yhdg.agentserver.service.basic.CustomerService.update(..))")
    private void update() {}

    @Around("update()")
    public Object update(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Customer customer = (Customer) joinPoint.getArgs()[0];
        memCachedClient.delete(CacheKey.key(CacheKey.K_ID_V_CUSTOMER_INFO, customer.getId()));
        return result;
    }
}
