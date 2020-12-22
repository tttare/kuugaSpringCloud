package com.kuuga.api.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * ClassName: ResponseParam <br/>
 * Description: 接口响应实体类<br/>
 * date: 2019/9/6 14:15<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ResponseParam implements Serializable {

    // 响应码
    private String code;
    // 数据
    private Object root;
    // 接口响应描述
    private String desc;
    // 数据条数
    private long total;

    public ResponseParam(String code,String desc){
        this.code=code;
        this.desc=desc;
    }

    public ResponseParam(String code,Object root,long total){
        this.code=code;
        this.root=root;
        this.total=total;
    }

    public ResponseParam(Throwable e) {
        super();
        this.code = SysContant.FAIL;
        this.desc = e.getMessage();
    }
}
