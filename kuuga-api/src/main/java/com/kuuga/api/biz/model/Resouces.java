package com.kuuga.api.biz.model;

import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * ClassName: Resouces <br/>
 * Description: 用户上传的资源<br/>
 * date: 2020/2/6 10:32<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Getter
@Setter
public class Resouces {

    private String id;
    private String fileName;
    private String type;//类型
    private String suffix;//后缀
    private long size;//文件大小原始值
    private String space;//占用空间
    private Date uploadDate;//上传时间
    private String uploaderId;//上传人id
    private String filePath;//文件存放路径
    private String state;//状态

    public void setSize(long size){
        this.size = size;
        this.space = calculate(size);
    }

    private String calculate(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        if(size <1024){
            return size+"Bit";
        }else if(size <1048576){
            return df.format((double) size / 1024) + "KB";
        }else if(size <1073741824){
            return df.format((double) size / 1048576) + "MB";
        }else{
            return df.format((double) size / 1073741824) +"GB";
        }
    }
}
