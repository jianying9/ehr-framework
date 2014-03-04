package com.ehr.framework.threadpool;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 *
 * @author nelson
 */
public interface ThreadPool {

    /**
     * 执行任务
     * @param <T>
     * @param task
     * @return 
     */
    public <T> Future<T> submit(Callable<T> task);
    
    /**
     * 
     * @param task 
     */
    public void execute(Runnable task);

    /**
     * 判断线程池中的队列是否还剩一个
     *
     * @return
     */
    public boolean isRemainMoreThanOne();

    /**
     * 判断线程池中的队列是否满
     *
     * @return
     */
    public boolean isFull();

    /**
     * 判断队列是否为空
     *
     * @return
     */
    public boolean isEmpty();
}
