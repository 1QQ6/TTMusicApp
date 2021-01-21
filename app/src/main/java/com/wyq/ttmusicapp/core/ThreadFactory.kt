package com.wyq.ttmusicapp.core

import java.util.concurrent.*

/**
 *
 * 线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式
 * 这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
 * 说明：Executors返回的线程池对象的弊端如下：
 *
 * 1）FixedThreadPool和SingleThreadPool:
 *   允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM。
 * 2）CachedThreadPool:
 *   允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。
 *
 */
class ThreadFactory private constructor() {
    /**
     * 核心线程数
     */
    var corePoolSize = 3

    /**
     * 最大线程数
     */
    var maximumPoolSize = 6

    /**
     * 超过 corePoolSize 线程数量的线程最大空闲时间
     */
    var keepAliveTime: Long = 2

    /**
     * 以秒为时间单位
     */
    var unit = TimeUnit.SECONDS

    /**
     * 创建工作队列，用于存放提交的等待执行任务
     */
    var workQueue: BlockingQueue<Runnable> =
        ArrayBlockingQueue(2)
    private var mMusicOperateExecutor: ExecutorService? = null
    val musicOperateExecutor: ExecutorService
        get() {
            if (mMusicOperateExecutor == null) {
                mMusicOperateExecutor = ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime,
                    unit,
                    workQueue,
                    ThreadPoolExecutor.AbortPolicy()
                )
            }
            return mMusicOperateExecutor!!
        }

    companion object {
        private val mInstance =
            ThreadFactory()

        fun get(): ThreadFactory {
            return mInstance
        }
    }
}