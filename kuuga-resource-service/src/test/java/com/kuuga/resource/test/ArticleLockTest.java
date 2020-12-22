package com.kuuga.resource.test;

import com.kuuga.api.biz.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * ClassName: ArticleLockTest <br/>
 * Description: <br/>
 * date: 2020/10/20 21:57<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ArticleLockTest {

    private static final int COUNT = 50;
    private CountDownLatch latch = new CountDownLatch(COUNT);
    private List<Runnable> transferThreads = new ArrayList<>();
    @Autowired
    private ArticleService articleService;

    /**
     * 测试悲观锁
     * */
    @Test
    public void  testPessimistic() throws InterruptedException {
        log.info("测试开始:"+new Date().toString());
        for(int i=0;i<COUNT;i++){
            transferThreads.add(new Runnable(){

                @Override
                public void run() {
                    try{
                        articleService.updateArticlePessimistic("1233e62eb0e552a4ff0093d6bef43f02",
                                "3d54ff962a3c82725de13582f83fd7ab",10);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        latch.countDown();
                    }


                }
            });
        }
        for (Runnable r:transferThreads){
            r.run();
        }
        latch.await();
        log.info("测试结束:"+new Date().toString());
    }

    /**
     * 测试乐观锁
     * */
    @Test
    public void testOptimistic() throws InterruptedException {
        log.info("测试开始:"+new Date().toString());
        for(int i=0;i<COUNT;i++){
            transferThreads.add(new Runnable(){

                @Override
                public void run() {
                    try{
                        articleService.updateArticleOptimistic("1233e62eb0e552a4ff0093d6bef43f02",
                                "3d54ff962a3c82725de13582f83fd7ab",10);
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        latch.countDown();
                    }


                }
            });
        }
        for (Runnable r:transferThreads){
            r.run();
        }
        latch.await();
        log.info("测试结束:"+new Date().toString());
    }


}
