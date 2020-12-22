package com.kuuga.api.biz.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kuuga.api.biz.enums.ArticleActionEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@TableName("tb_article_log")
public class ArticleLog implements Serializable {
	@TableId(value = "log_id", type = IdType.ASSIGN_UUID)
	private String logId;
	@TableField(value = "art_id")
	private String artId;
	@TableField(exist = false)
	private ArticleActionEnum action;//文章操作
	@TableField(value = "log_action")
	private Integer logAction;//操作code
	@TableField(value = "action_text")
	private String actionText;//操作名称
	private String info;//操作备注
	@TableField(value = "action_time")
	private Date actionTime;//操作时间

	public void setAction(ArticleActionEnum action){
		this.actionText = action.text;
		this.logAction = action.code;
	}
}
