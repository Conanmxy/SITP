package com.example.s1.rxjava;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Administrator on 2017/10/23.
 */
public class RxBus2 {
    private static volatile RxBus2 defaultInstance;

    private final Subject<Object,Object>bus;

    public RxBus2(){
        bus=new SerializedSubject<>(PublishSubject.create());
    }

    //单利RxBus
    public static RxBus2 getDefault(){
        if(defaultInstance==null){
            synchronized (RxBus2.class){
                if(defaultInstance==null){
                    defaultInstance=new RxBus2();
                }
            }
        }
        return defaultInstance;
    }

    //发送一个新的事件
    public void post(Object o){
        bus.onNext(o);
    }

    //根据传递的eventType 类型返回特定类型的被观察者
    public<T>Observable<T> toObservable(Class<T> eventType){
        return bus.ofType(eventType);
    }
}
