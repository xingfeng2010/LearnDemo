package com.administrator.learndemo.dynamic;

import android.util.Log;

import java.lang.reflect.Proxy;

public class TestDynamic {
    public void testDynamicPropxy() {
        //被代理类
        IPrint realPrint = new RealPrint();
        //我们要代理哪个类，就将该对象传进去，最后是通过该被代理对象来调用其方法的
        MyInvocationHandler handler = new MyInvocationHandler(realPrint);
        //生成代理类
        IPrint print = (IPrint) Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                realPrint.getClass().getInterfaces(), handler);
        //输出代理类对象
        Log.i("testDynamicPropxy", "Proxy : " + print.getClass().getName());
        Log.i("testDynamicPropxy", "Proxy super : " + print.getClass().getSuperclass().getName());
        Log.i("testDynamicPropxy", "Proxy interfaces : " + print.getClass().getInterfaces()[0].getName());
        //调用代理类print方法
        print.print("hahah");
    }
}
