package com.kuuga.api.biz.service;

import com.kuuga.api.biz.callback.ArticleServiceFallback;
import com.kuuga.api.biz.model.Article;
import com.kuuga.api.common.QueryPage;
import com.kuuga.api.common.ResponseParam;
import com.kuuga.api.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: ArticleService <br/>
 * Description: <br/>
 * date: 2020/10/13 22:21<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Service
@FeignClient(value = "kuuga-resource-service", fallback = ArticleServiceFallback.class)
public interface ArticleService {

    @PostMapping(value = "/article/add")
    void saveOrUpdateArticle(@RequestBody Article article);

    @PostMapping(value = "/article/list")
    ResponseParam listArticle(@RequestBody QueryPage queryPage);

    void updateArticlePessimistic(String payId,String recId,double amount) throws InterruptedException;

    void updateArticleOptimistic(String payId,String recId,double amount) throws InterruptedException;

}
