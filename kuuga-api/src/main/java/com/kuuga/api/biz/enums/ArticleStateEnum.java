package com.kuuga.api.biz.enums;

/**
 * ClassName: ArticleStateEnum <br/>
 * Description: <br/>
 * date: 2020/10/16 18:34<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public enum ArticleStateEnum {
    INITIAL(1,"正常"),
    HIDDEN(3,"隐藏"),
    DELETE(4,"已删除"),
    BANNED(5,"已封禁");

    public Integer code;
    public String text;
    private ArticleStateEnum(Integer code, String text) {
        this.code = code;
        this.text = text;
    }
}
