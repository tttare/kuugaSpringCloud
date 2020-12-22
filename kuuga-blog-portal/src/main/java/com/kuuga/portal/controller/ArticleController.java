package com.kuuga.portal.controller;

import com.kuuga.api.biz.model.Article;
import com.kuuga.api.biz.service.ArticleService;
import com.kuuga.api.common.QueryPage;
import com.kuuga.api.common.ResponseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: ArticleController <br/>
 * Description: <br/>
 * date: 2020/10/16 15:06<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@RestController()
@RequestMapping("/blog")
public class ArticleController {

    @Autowired
    private ArticleService articleService = null;

    @RequestMapping("/article/list")
    public ResponseParam listArticle(@RequestBody QueryPage query) {
        return this.articleService.listArticle(query);
    }

    @RequestMapping("/article/saveOrUpdateArticle")
    public void saveOrUpdateArticle(@RequestBody Article article) {
        this.articleService.saveOrUpdateArticle(article);
    }
}
