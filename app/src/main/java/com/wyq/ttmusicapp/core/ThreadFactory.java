package com.wyq.ttmusicapp.core;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**

 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式
 这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 说明：Executors返回的线程池对象的弊端如下：

 1）FixedThreadPool和SingleThreadPool:
   允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
 2）CachedThreadPool:
   允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。

 */

public class ThreadFactory {

    private static ThreadFactory mInstance = new ThreadFactory();

    /**
     * 核心线程数
     */
    int corePoolSize = 3;
    /**
     * 最大线程数
     */
    int maximumPoolSize = 6;
    /**
     * 超过 corePoolSize 线程数量的线程最大空闲时间
     */
    long keepAliveTime = 2;
    /**
     * 以秒为时间单位
     */
    TimeUnit unit = TimeUnit.SECONDS;
    /**
     * 创建工作队列，用于存放提交的等待执行任务
     */
    BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);

    private ExecutorService mMusicOperateExecutor;

    private ThreadFactory() {

    }

    public static ThreadFactory get() {
        return mInstance;
    }

    public ExecutorService getMusicOperateExecutor() {
        if (mMusicOperateExecutor == null) {
            mMusicOperateExecutor = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    unit,
                    workQueue,
                    new ThreadPoolExecutor.AbortPolicy());
        }
        return mMusicOperateExecutor;
    }
}
