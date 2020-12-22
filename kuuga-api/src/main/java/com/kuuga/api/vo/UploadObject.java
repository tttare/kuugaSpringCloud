package com.kuuga.api.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * ClassName: UploadObject <br/>
 * Description: <br/>
 * date: 2020/2/6 19:39<br/>
 *
 * @author: tttare<br />
 * @since JDK 1.8
 */
@Getter
@Setter
public class UploadObject {
    private String name;
    private long size;
    private String type;
    private String fileData;
    private int height;
    private int width;
}
