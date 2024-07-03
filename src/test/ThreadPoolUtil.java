package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolUtil {
   //线程池核心线程数
   private static int CORE_POOL_SIZE = 8;
   //线程池最大线程数
   private static int MAX_POOL_SIZE = 1000;
   //额外线程空状态生存时间
   private static int KEEP_ALIVE_TIME = 10000;
   //阻塞队列。当核心线程都被占用，且阻塞队列已满的情况下，才会开启额外线程。
   private static BlockingQueue workQueue = new ArrayBlockingQueue(10);
   //线程池
   private static ThreadPoolExecutor threadPool;

   private static final String THREAD_NAME_PREFIX = "BacnetThread-";

   private ThreadPoolUtil() {
   }

   //线程工厂
   private static final ThreadFactory threadFactory = new ThreadFactory() {
      private final AtomicInteger mCount = new AtomicInteger();

      @Override
      public Thread newThread(Runnable r) {
         return new Thread(r, THREAD_NAME_PREFIX + mCount.getAndIncrement());
      }
   };

   static {
      threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
              TimeUnit.SECONDS, workQueue, threadFactory);
   }

   public static void execute(Runnable runnable) {
      threadPool.execute(runnable);
   }


   public static void cancel(Runnable runnable) {
      if (null == runnable || threadPool == null) {
         return;
      }
      threadPool.remove(runnable);
   }

   public static void execute(FutureTask futureTask) {
      threadPool.execute(futureTask);
   }

   public static void cancel(FutureTask futureTask) {
      futureTask.cancel(true);
   }
}
