package com.kuuga.api.biz.enums;

/**
 * ClassName: ArticleActionEnum <br/>
 * Description: <br/>
 * date: 2020/10/16 19:00<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
public enum ArticleActionEnum {
    CREATE(1,"创建文章"),
    UPDATE(2,"修改文章"),
    HIDDEN(3,"隐藏文章"),
    DELETE(4,"删除文章");

    public int code;
    public String text;

    private ArticleActionEnum(int code,String text){
        this.code = code;
        this.text = text;
    }

}
