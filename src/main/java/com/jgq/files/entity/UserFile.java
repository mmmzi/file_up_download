package com.jgq.files.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * 用户上传的文件类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserFile {
    private Integer id;
    private String oldFileName;
    private String newFileName;
    private String ext;
    private String path;
    private String size;
    private String type;
    private String isImg;
    private Integer downcounts;
    private Date uploadTime;
}
