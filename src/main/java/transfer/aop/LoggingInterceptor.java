package transfer.aop;

import io.micronaut.aop.MethodInterceptor;
import io.micronaut.aop.MethodInvocationContext;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;

/**
 * Класс <class>LoggingInterceptor</class> provide debug logging for method marker {@link @DebugLog}
 */
@Singleton
@Slf4j
public class LoggingInterceptor implements MethodInterceptor<Object, Object> {

    @Override
    public Object intercept(MethodInvocationContext<Object, Object> context) {
        var parameterString = context.getParameterValueMap().toString();
        var methodName = context.getMethodName();
        log.debug("Method '{}' called with params: {}", methodName, parameterString);
        return context.proceed();
    }
}

    
    