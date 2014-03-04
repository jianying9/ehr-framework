package com.ehr.framework.threadpool;

import com.ehr.framework.logger.LogFactory;
import java.util.concurrent.*;
import org.slf4j.Logger;

/**
 *
 * @author nelson
 */
public class ThreadPoolImpl implements ThreadPool {
    //日志对象

    private final Logger logger = LogFactory.getFrameworkLogger();
    //在线程池最小默认的线程数
    private int DEFAULT_MIN_THREAD_COUNT = 2;
    //在线程池最大默认的线程数
    private int DEFAULT_MAX_THREAD_COUNT = 100;
    //默认工作队列的大小
    private int DEFAULT_QUEUE_SIZE = 100;
    //把线程池中多于最小线程数的闲置的线程移除的默认时间
    private int DEFAULT_KEEPALIVE_TIMEOUT = 30000;
    //执行服务的对象
    private ExecutorService workService;
    //执行对象的工作队列
    private BlockingQueue<Runnable> workQueue;

    /**
     * 初始化线程池
     *
     * @param corePoolSize 线程池中最小的线程数
     * @param maximumPoolSize 线程池中最大的线程数
     * @param keepAliveTime 移除闲置线程的时间
     * @param queueSize 工作队列的大小
     */
    public ThreadPoolImpl(int corePoolSize, int maximumPoolSize, int keepAliveTime, int queueSize) {
        this.logger.info("开始初始化线程池....");
        if (queueSize <= 0) {
            queueSize = this.DEFAULT_QUEUE_SIZE;
        }
        if (corePoolSize <= 0) {
            corePoolSize = this.DEFAULT_MIN_THREAD_COUNT;
        }
        if (maximumPoolSize <= 0) {
            maximumPoolSize = this.DEFAULT_MAX_THREAD_COUNT;
        }
        if (corePoolSize >= maximumPoolSize) {
            corePoolSize = maximumPoolSize;
        }
        if (keepAliveTime <= 0) {
            keepAliveTime = this.DEFAULT_KEEPALIVE_TIMEOUT;
        }

        TimeUnit unit = TimeUnit.MILLISECONDS;
        this.workQueue = new LinkedBlockingQueue<Runnable>(queueSize);
        this.workService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, this.workQueue);
        this.logger.info("初始化线程池结束....");
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return this.workService.submit(task);
    }

    @Override
    public void execute(Runnable task) {
        this.workService.execute(task);
    }

    /**
     * 判断线程池中的队列是否还剩一个
     *
     * @return
     */
    @Override
    public boolean isRemainMoreThanOne() {
        return this.workQueue.remainingCapacity() >= 1;
    }

    /**
     * 判断线程池中的队列是否满
     *
     * @return
     */
    @Override
    public boolean isFull() {
        return this.workQueue.remainingCapacity() == 0;
    }

    /**
     * 判断队列是否为空
     *
     * @return
     */
    @Override
    public boolean isEmpty() {
        return this.workQueue.isEmpty();
    }
}
