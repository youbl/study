package com.example.threaddemo;

import com.example.threaddemo.lockDemo.LockClas;
import com.example.threaddemo.poolDemo.MyRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.concurrent.*;

@SpringBootApplication
@Slf4j
public class ThreadDemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ThreadDemoApplication.class, args);
    }

    @Override
    public void run(String... args) {
        testWaitNotBlock();
        // testThreadLock();
        // testThreadPool();
    }


    private void testWaitNotBlock() {
        // 演示 Future.get虽然会抛出TimeoutExcetption，但是并不会中断线程
        // 参考 https://stackoverflow.com/questions/16231508/does-a-future-timeout-kill-the-thread-execution
        ExecutorService ex = Executors.newSingleThreadExecutor();
        Future<?> f = ex.submit(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("not interrupt, finished");
            }
        });
        try {
            f.get(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void testThreadLock() {
        // 演示wait 会释放锁的效果
        new Thread(new LockClas.LockClas1()).start();
        new Thread(new LockClas.LockClas2()).start();
    }

    private void testThreadPool() {
        log.info("程序开始");
        /*
public ThreadPoolExecutor(
  int corePoolSize,   // 核心线程数，可以理解为最小线程数
  int maximumPoolSize,// 允许创建的最大线程数，
  long keepAliveTime, // 线程空闲销毁时间，超过核心线程数的那些线程会被销毁
  TimeUnit unit,      // keepAliveTime的时间单位
  BlockingQueue<Runnable> workQueue,// 无空闲线程时，存储待执行任务的队列
  ThreadFactory threadFactory,      // 线程工厂，用于创建线程，默认为 Executors.defaultThreadFactory()
  RejectedExecutionHandler handler) // 队列容量满或线程不接收时的阻塞处理程序，默认为 new AbortPolicy()，抛异常
        * */

        ArrayBlockingQueue queue = new ArrayBlockingQueue(3);

        // 创建一个任务池，2个核心线程，最多4个线程，队列长度为3，队列满线程满时，抛异常
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
                2,
                4,
                0,
                TimeUnit.SECONDS,
                queue,
                runnable -> {
                    return new Thread(runnable, "newTh-" + getTaskName(runnable));
                },
                new ThreadPoolExecutor.AbortPolicy()
        );
        new Thread(() -> loopPrint(threadPool)).start();

        int i = 1;
        printStatus(threadPool);
        String threadName = "t" + i++;
        threadPool.submit(new MyRunnable(threadName, 10));
        log.info("{} 加入线程池", threadName);

        printStatus(threadPool);
        threadName = "t" + (i++);
        threadPool.submit(new MyRunnable(threadName, 20));
        log.info("{} 加入线程池", threadName);

        printStatus(threadPool);
        threadName = "t" + (i++);
        threadPool.submit(new MyRunnable(threadName, 10));
        log.info("{} 加入线程池", threadName);

        printStatus(threadPool);
        threadName = "t" + (i++);
        threadPool.submit(new MyRunnable(threadName, 5));
        log.info("{} 加入线程池", threadName);

        printStatus(threadPool);
        threadName = "t" + (i++);
        threadPool.submit(new MyRunnable(threadName, 30));
        log.info("{} 加入线程池", threadName);

        printStatus(threadPool);
        threadName = "t" + (i++);
        threadPool.submit(new MyRunnable(threadName, 40));
        log.info("{} 加入线程池", threadName);

        printStatus(threadPool);
        threadName = "t" + (i++);
        threadPool.submit(new MyRunnable(threadName, 25));
        log.info("{} 加入线程池", threadName);

        printStatus(threadPool);
        threadName = "t" + (i++);
        // 因为最多4个线程，队列长度为3，下面这个任务是第8个，放不下，所以会抛异常，中断当前主线程，但不会中断线程池，线程池会继续运行
        // Caused by: java.util.concurrent.RejectedExecutionException:
        // Task java.util.concurrent.FutureTask@6e78fcf5 rejected from
        // java.util.concurrent.ThreadPoolExecutor@56febdc[Running, pool size = 4, active threads = 4, queued tasks = 3, completed tasks = 0]
        //threadPool.submit(new MyRunnable(threadName, 25));
        //log.info("{} 加入线程池", threadName);

        printStatus(threadPool);
    }

    /*
程序运行结果如下：
结论：
1、任务并不是先来后到顺序执行，只要缓冲队列长度足够长，只会使用核心线程去运行，并不会新建线程；
2、队列满了，才会去创建新线程，跑后来的任务，而不是先跑队列里的任务
3、线程空闲后，才会按先来后到顺序，执行缓冲队列里的任务
[    main] c.e.threaddemo.ThreadDemoApplication     : 程序开始
[    main] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:0 [] 线程信息: 活动线程数:0 完成数:0
[    main] c.e.threaddemo.ThreadDemoApplication     : t1 加入线程池
[    main] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:0 [] 线程信息:newTh-t1 活动线程数:1 完成数:0
[    main] c.e.threaddemo.ThreadDemoApplication     : t2 加入线程池
[    main] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:0 [] 线程信息:newTh-t1, newTh-t2 活动线程数:2 完成数:0
[    main] c.e.threaddemo.ThreadDemoApplication     : t3 加入线程池
[    main] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:1 [t3] 线程信息:newTh-t1, newTh-t2 活动线程数:2 完成数:0
[    main] c.e.threaddemo.ThreadDemoApplication     : t4 加入线程池
[    main] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:2 [t3, t4] 线程信息:newTh-t1, newTh-t2 活动线程数:2 完成数:0
[    main] c.e.threaddemo.ThreadDemoApplication     : t5 加入线程池
[    main] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:3 [t3, t4, t5] 线程信息:newTh-t1, newTh-t2 活动线程数:2 完成数:0
[    main] c.e.threaddemo.ThreadDemoApplication     : t6 加入线程池
[    main] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:3 [t3, t4, t5] 线程信息:newTh-t1, newTh-t2, newTh-t6 活动线程数:3 完成数:0
[    main] c.e.threaddemo.ThreadDemoApplication     : t7 加入线程池
[    main] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:3 [t3, t4, t5] 线程信息:newTh-t1, newTh-t2, newTh-t7, newTh-t6 活动线程数:4 完成数:0
[newTh-t1] c.e.threaddemo.poolDemo.MyRunnable       : t1运行了，休眠了10秒
[Thread-4] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:2 [t4, t5] 线程信息:newTh-t1, newTh-t2, newTh-t7, newTh-t6 活动线程数:4 完成数:1
[newTh-t1] c.e.threaddemo.poolDemo.MyRunnable       : t3运行了，休眠了10秒
[newTh-t2] c.e.threaddemo.poolDemo.MyRunnable       : t2运行了，休眠了20秒
[Thread-4] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:0 [] 线程信息:newTh-t1, newTh-t2, newTh-t7, newTh-t6 活动线程数:4 完成数:3
[newTh-t1] c.e.threaddemo.poolDemo.MyRunnable       : t4运行了，休眠了5秒
[newTh-t7] c.e.threaddemo.poolDemo.MyRunnable       : t7运行了，休眠了25秒
[Thread-4] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:0 [] 线程信息:newTh-t2, newTh-t6 活动线程数:2 完成数:5
[newTh-t6] c.e.threaddemo.poolDemo.MyRunnable       : t6运行了，休眠了40秒
[Thread-4] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:0 [] 线程信息:newTh-t2, newTh-t6 活动线程数:1 完成数:6
[newTh-t2] c.e.threaddemo.poolDemo.MyRunnable       : t5运行了，休眠了30秒
[Thread-4] c.e.threaddemo.ThreadDemoApplication     : 队列信息:长度:0 [] 线程信息:newTh-t2, newTh-t6 活动线程数:0 完成数:7
    * */

    private void loopPrint(ThreadPoolExecutor threadPool) {
        while (true) {
            sleep(1);
            printStatus(threadPool);
        }
    }

    private String lastInfo;

    private void printStatus(ThreadPoolExecutor threadPool) {
        StringBuilder sb = new StringBuilder();
        String queueInfo = enumQueueInfo(threadPool.getQueue());
        sb.append("队列信息:")
                .append(queueInfo)
                .append(" 线程信息:")
                .append(enumWorkerInfo(threadPool));

        int threadNum = threadPool.getActiveCount();
        sb.append(" 活动线程数:")
                .append(threadNum);

        //int poolSize = threadPool.getPoolSize();
        //long taskCnt = threadPool.getTaskCount();
        long completeCnt = threadPool.getCompletedTaskCount();
        sb.append(" 完成数:" + completeCnt);//.append("\n");
        String info = sb.toString();
        if (info.equals(lastInfo))
            return;
        lastInfo = info;
        log.info(info);
    }

    private String enumQueueInfo(BlockingQueue paraQueue) {
        if (!(paraQueue instanceof ArrayBlockingQueue)) {
            return "类型不对";
        }
        try {
            StringBuilder ret = new StringBuilder();
            ArrayBlockingQueue queue = (ArrayBlockingQueue) paraQueue;
            for (Object item : queue) {
                // FutureTask task = (FutureTask) item;
                if (ret.length() > 0)
                    ret.append(", ");
                String taskName = getCallableTaskName(item);
                ret.append(taskName);
            }

            return "长度:" + queue.size() + " [" + ret + "]";
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    private String enumWorkerInfo(ThreadPoolExecutor threadPool) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            Field field = getField(threadPool, "workers");
            HashSet hashSet = (HashSet) field.get(threadPool);
            for (Object obj : hashSet) {
                Object objThread = getField(obj, "thread").get(obj);
                if (stringBuilder.length() > 0)
                    stringBuilder.append(", ");
                stringBuilder.append(((Thread) objThread).getName());
            }
            return stringBuilder.toString();
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    private String getTaskName(Runnable runnable) {
        try {
            Field field = getField(runnable, "firstTask");
            return getCallableTaskName(field.get(runnable));
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    private String getCallableTaskName(Object task) {
        try {
            Field field = getField(task, "callable");
            Object adapter = null;
            adapter = field.get(task);
            Field taskField = getField(adapter, "task");
            MyRunnable runnable2 = (MyRunnable) taskField.get(adapter);
            return runnable2.getName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Field getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (Exception exp) {
            throw new RuntimeException(exp);
        }
    }

    private void sleep(int second) {
        try {
            Thread.sleep(second * 1000);
        } catch (Exception exp) {
            log.error(exp.toString());
        }
    }
}
