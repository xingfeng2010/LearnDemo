package com.administrator.learndemo.aop.aspect;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.administrator.learndemo.LearnApplication;
import com.administrator.learndemo.aop.AopEntryActivity;
import com.administrator.learndemo.aop.EventHolder

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

//切面
@Aspect
class LoginAapect {

//    @Around("execution(@com.example.administrator.aop_aspectj.annotation.LoginTrace *  *(..))")
//    public boolean TestCanDelete(ProceedingJoinPoint joinPoint) {
//        Log.e("zdh", "TestCanDelete ------------- IN");
//        try {
//            joinPoint.proceed();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//        return true;
//    }

    @Around("execution(* com.administrator.learndemo.aop.TestOperation.canCancel(..))")
     fun TestCanDelete(joinPoint:ProceedingJoinPoint):Boolean {
        Log.e("zdh", "TestCanDelete ------------- IN");
        joinPoint.proceed()
        return true;
    }

    //切入点 ("execution(@注解全类名 * *(..)")
    @Pointcut("execution(@com.administrator.learndemo.aop.annotation.LoginTrace *  *(..))")
    fun methodAnnotationWithLoginTrace() {
    }


    //切入点逻辑处理（可以对 切入点 前处理 后处理  前后处理 ）
    //参数--->切入点方法名()--->methodAnnotationWithLoginTrace()
//    @After("methodAnnotationWithLoginTrace()")//切入点后运行 --》使用注解LoginTrace的方法运行完后执行这个方法代码
//    @Before("methodAnnotationWithLoginTrace()")//切入点前运行
    @Before("methodAnnotationWithLoginTrace()")//切入点前 后 都运行(后运行是在ProceedingJoinPoint对象非空时运行)
    public fun weaveJoinPoint(joinPoint:ProceedingJoinPoint):Any {
        Log.e("zdh", "-------------前");


        if (!LearnApplication.getInstance().isLogin()){
//            Activity context = MainActivity.getInstance();
//            context.startActivity(new Intent(context,LoginActivity.class));

            EventHolder.instance.post("startLoginActivity")
        }
//        Object proceed=null;
//        if (joinPoint != null) {
//            //如果使用Around注解需要返回proceed对象，要不然注解方法里面代码不执行，其他返回null
//             proceed = joinPoint.proceed();
//            Log.e("zdh", "-------------后");
//        }

        return joinPoint.proceed()
    }


}
