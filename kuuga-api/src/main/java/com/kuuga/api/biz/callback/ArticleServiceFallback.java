package com.kuuga.api.biz.callback;

import com.kuuga.api.biz.model.Article;
import com.kuuga.api.biz.service.ArticleService;
import com.kuuga.api.common.QueryPage;
import com.kuuga.api.common.ResponseParam;
import com.kuuga.api.common.SysContant;
import feign.hystrix.FallbackFactory;

/**
 * ClassName: ArticleServiceFallback <br/>
 * Description: 文章服务的降级处理<br/>
 * date: 2020/10/13 22:32<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public class ArticleServiceFallback implements FallbackFactory {
    @Override
    public Object create(Throwable throwable) {
        return new ArticleService(){

            @Override
            public void saveOrUpdateArticle(Article article) {

            }

            @Override
            public ResponseParam listArticle(QueryPage queryPage) {
                return new ResponseParam(SysContant.SUCCESS,null,0);
            }

            @Override
            public void updateArticlePessimistic(String payId, String recId, double amount) throws InterruptedException {

            }

            @Override
            public void updateArticleOptimistic(String payId, String recId, double amount)  throws InterruptedException{

            }
        };
    }
}
