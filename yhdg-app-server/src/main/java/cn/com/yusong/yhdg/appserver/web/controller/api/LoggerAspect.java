package cn.com.yusong.yhdg.appserver.web.controller.api;

import cn.com.yusong.yhdg.common.utils.AppUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
public class LoggerAspect {

    final static Logger log = LogManager.getLogger(LoggerAspect.class);
    
    /**
     * 定义一个方法，用于声明切入点表达式，方法中一般不需要添加其他代码
     * 使用@Pointcut声明切入点表达式
     * 后面的通知直接使用方法名来引用当前的切点表达式；如果是其他类使用，加上包名即可
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void declareJoinPointExpression(){}
    
    @Around(value="declareJoinPointExpression()")
    public Object aroundMethod(ProceedingJoinPoint point) throws Throwable {

        if(log.isDebugEnabled()) {
            String methodName = point.getSignature().getName();
            log.debug("class: {} method: {}", point.getTarget().getClass(), methodName);

            if(point.getArgs() != null && point.getArgs().length > 0) {
                int length = point.getArgs().length;
                for(int i = 0; i < length; i++) {
                    if(point.getArgs()[i] instanceof HttpServletRequest) {

                    } else if(point.getArgs()[i] instanceof HttpServletResponse) {

                    } else {
                        try {
                            log.debug("param: {} {}", i, AppUtils.encodeJson2(point.getArgs()[i]));
                        } catch (Exception e) {
                            log.error("encodeJson error", e);
                        }
                    }
                }
            }

            long time = System.currentTimeMillis();
            Object result = point.proceed();
            if(log.isDebugEnabled()) {
                log.debug("class: {} method: {} run: {}ms", point.getTarget().getClass(), methodName, (System.currentTimeMillis() - time));
            }

            if(result == null) {
                log.debug("response: null");
            } else {
                try {
                    log.debug("response: {}", AppUtils.encodeJson2(result));
                } catch (Exception e) {
                    log.error("encodeJson error", e);
                }
            }
            return result;

        } else {
            return point.proceed();
        }
    }
}