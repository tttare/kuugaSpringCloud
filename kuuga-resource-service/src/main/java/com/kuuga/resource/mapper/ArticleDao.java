package com.kuuga.resource.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuuga.api.biz.model.Article;

/**
 * ClassName: ArticleDao <br/>
 * Description: <br/>
 * date: 2020/10/12 23:52<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public interface ArticleDao extends BaseMapper<Article> {
    Article selectByIdForUpdate(String id);

    int updateWithVersion(Article article);
}
