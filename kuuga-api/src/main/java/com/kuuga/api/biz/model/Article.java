package com.kuuga.api.biz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuuga.api.system.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 博客网站的主要对象：博客文章
 * */
@Data
@TableName("tb_article")
@NoArgsConstructor
public class Article implements Serializable {
	//文章id
	@TableId(value = "id", type = IdType.ASSIGN_UUID)
	private String id;
	//文章标题
	private String title;
	//文章摘要
	private String abs;
	//文章资源id
	@TableField(value = "resource_id")
	private String resourceId;
	//文章对应的博客资源
	@TableField(exist = false)
	private ArticleResource articleResource;
	//文章的标签
	@TableField(exist = false)
	private List<Tag> tags;
	//文章的分类
	@TableField(exist = false)
	private List<Category> categories;
	//文章作者
	@TableField(exist = false)
	private User author;
	//文章作者账号
	@TableField(value = "author_no")
	private String authorNo;
	//文章作者户名
	@TableField(value = "author_name")
	private String authorName;
	//创建时间
	@TableField(value = "create_time")
	private Date createTime;
	//博文最后更新时间
	@TableField(value = "last_update_time")
	private Date lastUpdateTime;
	@TableField(exist = false)
	private List<ArticleLog> articleLogs;
	//文章状态
	private Integer state;

	/**
	 * balance及version字段是为了做悲观和乐观所的测试，无实际意义
	 * */
	private BigDecimal balance;

	private Integer version;
}
