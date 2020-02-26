package transfer.aop.annotation;

import io.micronaut.aop.Around;
import io.micronaut.context.annotation.Type;
import transfer.aop.LoggingInterceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Around
@Type(LoggingInterceptor.class)
public @interface DebugLog {
}