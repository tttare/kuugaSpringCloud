package com.kuuga.api.tttareDemmo;

import java.util.concurrent.*;

/**
 * @ClassName: ThreadLocalAndThreadPool
 * @Author: qiuyongkang
 * @Description: ${description}
 * @Date: 2020/12/14 20:48
 * @Version: 1.0
 */
public class ThreadLocalAndThreadPool {

    public static int a = 0;
    /**
     * ThreadLocal,为线程存放独有变量,改变时不影响其他线程,防止线程安全问题
     * 线程在多个方法的调用中,减少参数传递,可以存放线程的上下文信息
     * */
    public static ThreadLocal<Integer> ints = new ThreadLocal<>();

    /**
     * 创建一个核心线程数及最大线程数为16的线程池
     * public ThreadPoolExecutor(int corePoolSize,
     *                               int maximumPoolSize,
     *                               long keepAliveTime,
     *                               TimeUnit unit,
     *                               BlockingQueue<Runnable> workQueue)
     * 线程池核心参数
     * corePoolSize:核心线程数,线程池常驻的线程数量,即使这线线程闲置也不会回收,除非设置了allowCoreThreadTimeOut
     * maximumPoolSize:最大线程数,一个任务被提交到线程池以后，首先会找有没有空闲存活线程，如果有则直接执行，如果没有则会缓存到工作队列（后面会介绍）中，如果工作队列满了，才会创建一个新线程，然后从工作队列的头部取出一个任务交由新线程来处理，而将刚提交的任务放入工作队列尾部。线程池不会无限制的去创建新线程，它会有一个最大线程数量的限制，这个数量即由maximunPoolSize指定
     * keepAliveTime:空闲线程存活时间,一个线程如果处于空闲状态，并且当前的线程数量大于corePoolSize，那么在指定时间后，这个空闲线程会被销毁，这里的指定时间由keepAliveTime来设定
     * unit:keepAliveTime的时间单位
     * workQueue:核心线程数一满,新任务被提交后，会先进入到此工作队列中，任务调度时再从队列中取出任务
     *      workQueue:jdk实现了四种队列:1.ArrayBlockingQueue:基于数组的有界阻塞队列，按FIFO排序。新任务进来后，会放到该队列的队尾，有界的数组可以防止资源耗尽问题。当线程池中线程数量达到corePoolSize后，再有新任务进来，则会将任务放入该队列的队尾，等待被调度。如果队列已经是满的，则创建一个新线程，如果线程数量已经达到maxPoolSize，则会执行拒绝策略。
     *                                 2.LinkedBlockingQuene:基于链表的无界阻塞队列（其实最大容量为Interger.MAX），按照FIFO排序。由于该队列的近似无界性，当线程池中线程数量达到corePoolSize后，再有新任务进来，会一直存入该队列，而不会去创建新线程直到maxPoolSize，因此使用该工作队列时，参数maxPoolSize其实是不起作用的。
     *                                 3.SynchronousQuene:一个不缓存任务的阻塞队列，生产者放入一个任务必须等到消费者取出这个任务。也就是说新任务进来时，不会缓存，而是直接被调度执行该任务，如果没有可用线程，则创建新线程，如果线程数量达到maxPoolSize，则执行拒绝策略。
     *                                 4.PriorityBlockingQueue:具有优先级的无界阻塞队列，优先级通过参数Comparator实现
     * threadFactory:线程工厂,创建一个新线程时使用的工厂，可以用来设定线程名、是否为daemon线程等等
     * handler 拒绝策略:
     *      1.CallerRunsPolicy:该策略下，在调用者线程中直接执行被拒绝任务的run方法，除非线程池已经shutdown，则直接抛弃任务。
     *      2.AbortPolicy:该策略下，直接丢弃任务，并抛出RejectedExecutionException异常
     *      3.DiscardPolicy:该策略下，直接丢弃任务，什么都不做。
     *      4.DiscardOldestPolicy:该策略下，抛弃进入队列最早的那个任务，然后尝试把这次拒绝的任务放入队列
     * 线程池执行方法
     *      execute(Runnable):执行任务,无返回值
     *      submit(Runnable):执行任务,返回Future类实列
     * Future:Future.get()方法能抛出返回线程池线程的异常信息,如果执行无异常,则能获取CallBack实现类的call方法return的对象
     *      故Future能帮助主线程获取新开线程的执行情况,捕获线程中的运行异常
     *      Future.get(timeOut.timeUnit):能设置新开线程执行的超时时间,如果超时,在主线程上抛出异常,新开线程可以继续进行
     * */
    public static ExecutorService executorService = Executors.newFixedThreadPool(16);


    public static void main(String[] args) {
        try {
            test03();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线程池加ThreadLocal
     **/
    public static void test01(){

        for(int i=0;i<10;i++){
            final int temp = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    ints.set(temp);
                    System.out.println(ints.get());
                }
            });
        }

    }

    public static void test02() throws InterruptedException {

        //发令枪
        CountDownLatch count = new CountDownLatch(10);//定义最大计数

        for(int i=0;i<10;i++) {
            final int temp = i;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    a += temp;
                    System.out.println(a);
                    count.countDown();//计数减一
                }
            });
        }

        count.await();//发令枪计数不为零时,将阻塞主进程,当计数为0时,继续执行
        a=0;
        System.out.println("-----");
        for(int i=0;i<10;i++){
            a+=i;
            System.out.println(a);
        }

    }


    //使用Future得到线程运行结果信息
    public static void test03() throws ExecutionException, InterruptedException, TimeoutException {
        Future<String> future = executorService.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {
                //String a = null;
                //System.out.println(a.split(","));
                return "success";
            }
        });

        Future<?> future2 = executorService.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("执行结束");
            }
        });

        //System.out.println(future2.get(500,TimeUnit.MILLISECONDS));
        System.out.println(future.get(500,TimeUnit.MILLISECONDS));
    }

    /**
     * Object wait() 和 Thread sleep()方法的区别
     * sleep:Thread类的静态方法,可以在任何代码块中使用,必须指定线程休眠时间,休眠期间不会释放锁
     * wait:Object类方法,可以指定和不指定休眠时间,不指定时,可以用notify和notifyAll唤醒线程,休眠期间会释放锁,且只能在同步方法和同步代码块中执行
     * */
    public void test04() throws InterruptedException {
        String s = new String("aa");
        s.wait();

    }

}
