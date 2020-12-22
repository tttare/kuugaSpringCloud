package com.kuuga.redis;

import com.kuuga.api.system.model.User;
import com.kuuga.redis.util.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.CastUtils;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisApplicationTests {

    public static ExecutorService executorService = Executors.newFixedThreadPool(16);

    @Autowired
    public RedisUtil redisUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void doSecKill() {
        //有10件商品,40人进行秒杀
        redisUtil.set("store",10);
        for (int i=0;i<40;i++){
            Future<?> submit = executorService.submit(new Runnable() {
                @Override
                public void run() {
                    RLock lock = redissonClient.getLock("store_lock");
                    lock.lock();
                    Integer store = Integer.valueOf(redisUtil.get("store").toString());
                    if (store > 0) {
                        store -= 1;
                        System.out.println("当前库存:" + store + "抢购成功");
                    }
                    redisUtil.set("store", store);
                    lock.unlock();
                }
            });

            try {
                submit.get();//不使用get() lock.lock()居然没有效果???????
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    static class ParamT{
        public String userName;
        public String password;

        public String getPassword() {
            return password;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @Test
    public void redisTest(){
        ParamT user = new ParamT();
        user.setUserName("tttare");
        user.setPassword("111789");
        redisUtil.set("user",user);
        ParamT userRedis = (ParamT)redisUtil.get("user");
        System.out.println("redis数据:"+userRedis.getUserName()+"..."+userRedis.getPassword());
    }

}
