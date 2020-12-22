package com.kuuga.resource.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuuga.api.Exception.RetryException;
import com.kuuga.api.biz.enums.ArticleActionEnum;
import com.kuuga.api.biz.enums.ArticleStateEnum;
import com.kuuga.api.biz.model.Article;
import com.kuuga.api.biz.model.ArticleLog;
import com.kuuga.api.biz.service.ArticleService;
import com.kuuga.api.common.SysContant;
import com.kuuga.api.common.QueryPage;
import com.kuuga.api.common.ResponseParam;
import com.kuuga.resource.mapper.ArticleDao;
import com.kuuga.resource.mapper.ArticlelogDao;
import com.kuuga.resource.service.BaseService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ClassName: ArticleServiceImpl <br/>
 * Description:  <br/>
 * date: 2020/10/13 22:22<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Log
@RestController()
@RequestMapping(value = "/resource")
public class ArticleServiceImpl extends BaseService implements ArticleService {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private ArticlelogDao articleLogDao;

    @PostMapping(value = "/article/add")
    @Transactional
    public void saveOrUpdateArticle(@RequestBody Article article) {
        ArticleLog articleLog = new ArticleLog();
        Date currentDate = new Date();
        if(article.getId()==null){
            article.setState(ArticleStateEnum.INITIAL.code);
            article.setCreateTime(currentDate);
            articleDao.insert(article);
            articleLog.setArtId(article.getId());
            articleLog.setAction(ArticleActionEnum.CREATE);
            articleLog.setActionTime(currentDate);
        }else{
            article.setLastUpdateTime(currentDate);
            articleDao.updateById(article);
            articleLog.setArtId(article.getId());
            articleLog.setAction(ArticleActionEnum.UPDATE);
            articleLog.setActionTime(currentDate);
        }
        //记录日志
        articleLogDao.insert(articleLog);
    }

    @PostMapping(value = "/article/list")
    public ResponseParam listArticle(@RequestBody QueryPage queryPage) {
        IPage<Article> userPage = new Page<>(queryPage.getPageNum(), queryPage.getPageSize());
        IPage<Article> articleIPage = articleDao.selectPage(userPage, null);
        return new ResponseParam(SysContant.SUCCESS,articleIPage.getRecords(),articleIPage.getTotal());
    }

    /**
     * 实现悲观锁更新
     * isolation : 隔离级别
     * propagation : 事务传播级别
     * */
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRES_NEW)
    public void updateArticlePessimistic(String payId,String recId,double amount){
        Article pay,rec;
        /**
         * 悲观锁查询方式 select * from table where id = #{id} for update;//锁表型查询
         * */
        try {
            pay = articleDao.selectByIdForUpdate(payId);
            rec = articleDao.selectByIdForUpdate(recId);
            /**
             *模拟转账
             * */
            pay.setBalance(pay.getBalance().subtract(new BigDecimal(amount)));
            rec.setBalance(rec.getBalance().add(new BigDecimal(amount)));
            /**
             * 更新数据
             * */
            Thread.sleep(50);
            articleDao.updateById(pay);
            articleDao.updateById(rec);
            log.info(pay.getTitle()+"->"+rec.getTitle()+"金额："+amount);
        }catch(Exception e){
            log.info(e.getMessage());
            //手动回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

    }

    /**
     * 实现乐观锁更新
     *
     * */
    @Transactional
    public void updateArticleOptimistic(String payId,String recId,double amount) throws InterruptedException {
        Article pay,rec;
        /**
         * 乐观锁查询，查询不锁表
         */
        pay = articleDao.selectById(payId);
        rec = articleDao.selectById(recId);
        /**
         *模拟转账
         * */
        pay.setBalance(pay.getBalance().subtract(new BigDecimal(amount)));
        rec.setBalance(rec.getBalance().add(new BigDecimal(amount)));
        /**
         * 按id和版本号查询，来保证当前数据未被其他人动过
         * */
        Thread.sleep(50);
        int r1 = articleDao.updateWithVersion(pay);
        int r2 = articleDao.updateWithVersion(rec);
        if(r1<1 || r2<1){
            /**
             * 修改0行数据，证明当前version已无法查询到数据，原数据已被修改版本，需抛异常或重试
             * */
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(pay.getTitle()+"->"+rec.getTitle()+"转账失败");
        }else{
            log.info(pay.getTitle()+"->"+rec.getTitle()+"金额："+amount);
        }
    }
}
