package com.tina.mvpcore.net.rx.databus;

/**
 * @author yxc
 * @date 2018/11/20
 */

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 数据总线
 */
public class RxBus {

    private Set<Object> subscribers;

    /**
     * 注册
     */
    public synchronized void register(Object subsciber) {
        subscribers.add(subsciber);
    }

    /**
     * 取消注册
     */
    public synchronized void unRegister(Object subscriber) {
        subscribers.remove(subscriber);
    }

    private static volatile RxBus instance;

    //读写分离 CopyOnWriteArraySet的集合
    private RxBus() {
        //读写分离的集合
        subscribers = new CopyOnWriteArraySet<>();
    }

    public static synchronized RxBus getInstance() {
        if (null == instance) {
            synchronized (RxBus.class) {
                if (null == instance) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }


    /**
     *
     */
    public void chainProcess(Function function) {
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(function)//进行网络访问
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object data) throws Exception {
                        //data 会传送到总线上
                        if (data == null) {
                            return;
                        }
                        send(data);//把数据送到P层
                    }
                });
    }


    private void send(Object data) {

        for (Object subscriber : subscribers) {
            callMethodByAnnotation(subscriber, data);
        }

    }

    private void callMethodByAnnotation(Object target, Object data) {
        //1. 得到presenter中写的所有的方法
        Method[] methodArray = target.getClass().getDeclaredMethods();
        for (int i = 0; i < methodArray.length; i++) {
            try {
                if (methodArray[i].getAnnotation(RegisterRxBus.class) != null) {
                    //如果哪个方法上面用了我们写的注解
                    //把数据传上去，
                    Class paramType = methodArray[i].getParameterTypes()[0];
                    if (data.getClass().getName().equals(paramType.getName())) {
                        //执行
                        methodArray[i].invoke(target, new Object[]{data});
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
