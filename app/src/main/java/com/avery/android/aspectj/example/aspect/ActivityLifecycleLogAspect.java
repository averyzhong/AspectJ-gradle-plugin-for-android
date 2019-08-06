package com.avery.android.aspectj.example.aspect;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class ActivityLifecycleLogAspect {
    private static final String TAG = ActivityLifecycleLogAspect.class.getSimpleName();

    @Pointcut("execution(* com.avery.android.aspectj.example.MainActivity.on*(..))")
    public void logActivityLifecycle() {}

    @Before("logActivityLifecycle()")
    public void log(final JoinPoint joinPoint) {
        Log.v(TAG, "log: " + joinPoint.toLongString());
    }

}
