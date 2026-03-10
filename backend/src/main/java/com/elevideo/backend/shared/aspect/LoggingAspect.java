package com.elevideo.backend.shared.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    // ----------------------------------------------------------------
    // Pointcuts
    // ----------------------------------------------------------------

    /** Todos los métodos públicos de clases anotadas con @Service. */
    @Pointcut("within(@org.springframework.stereotype.Service *) && execution(public * *(..))")
    public void servicePublicMethods() {}

    /** Todos los métodos de clases anotadas con @RestController. */
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerMethods() {}

    /** Métodos explícitamente anotados con @LogExecution. */
    @Pointcut("@annotation(com.elevideo.backend.shared.aspect.LogExecution)")
    public void logExecutionAnnotated() {}

    // ----------------------------------------------------------------
    // Advice: Controllers — log HTTP request/response
    // ----------------------------------------------------------------

    @Around("controllerMethods()")
    public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String httpMethod = "N/A";
        String uri        = "N/A";

        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            httpMethod = request.getMethod();
            uri        = request.getRequestURI();
        }

        String controller = joinPoint.getTarget().getClass().getSimpleName();
        String action     = ((MethodSignature) joinPoint.getSignature()).getMethod().getName();

        long start = System.currentTimeMillis();
        log.info("REQUEST  [{} {}] -> {}.{}()", httpMethod, uri, controller, action);

        try {
            Object result   = joinPoint.proceed();
            long   duration = System.currentTimeMillis() - start;

            if (result instanceof ResponseEntity<?> re) {
                log.info("RESPONSE [{} {}] -> {} | {}ms", httpMethod, uri, re.getStatusCode().value(), duration);
            } else {
                log.info("RESPONSE [{} {}] -> OK | {}ms", httpMethod, uri, duration);
            }

            return result;

        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("ERROR    [{} {}] -> {}.{}() | {}ms | {}",
                    httpMethod, uri, controller, action, duration, ex.getMessage(), ex);
            throw ex;
        }
    }

    // ----------------------------------------------------------------
    // Advice: Services — log method execution automatically
    // ----------------------------------------------------------------

    @Around("servicePublicMethods()")
    public Object logService(ProceedingJoinPoint joinPoint) throws Throwable {
        String className  = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        long start = System.currentTimeMillis();
        log.debug("SERVICE  {}.{}() START", className, methodName);

        try {
            Object result   = joinPoint.proceed();
            long   duration = System.currentTimeMillis() - start;
            log.debug("SERVICE  {}.{}() OK | {}ms", className, methodName, duration);
            return result;

        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - start;
            log.warn("SERVICE  {}.{}() ERROR | {}ms | {}", className, methodName, duration, ex.getMessage());
            throw ex;
        }
    }

    // ----------------------------------------------------------------
    // Advice: @LogExecution — log con label personalizado (INFO level)
    // ----------------------------------------------------------------

    @Around("logExecutionAnnotated() && @annotation(logExecution)")
    public Object logExecution(ProceedingJoinPoint joinPoint, LogExecution logExecution) throws Throwable {
        String className  = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String label      = logExecution.value().isBlank() ? "" : " [" + logExecution.value() + "]";

        long start = System.currentTimeMillis();
        log.info("EXEC     {}.{}(){} START", className, methodName, label);

        try {
            Object result   = joinPoint.proceed();
            long   duration = System.currentTimeMillis() - start;
            log.info("EXEC     {}.{}(){} OK | {}ms", className, methodName, label, duration);
            return result;

        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - start;
            log.error("EXEC     {}.{}(){} ERROR | {}ms | {}",
                    className, methodName, label, duration, ex.getMessage(), ex);
            throw ex;
        }
    }
}
