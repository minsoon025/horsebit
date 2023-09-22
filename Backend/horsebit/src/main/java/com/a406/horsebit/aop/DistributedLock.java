package com.a406.horsebit.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    /* Name of lock */
    String key();

    /* Time unit of lock */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /* waiting time for lock */
    long waitTime() default 5L;

    /* maximum lock time */
    long leaseTime() default 3L;
}
