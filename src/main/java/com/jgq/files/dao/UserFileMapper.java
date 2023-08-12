package com.jgq.files.dao;

import com.jgq.files.entity.UserFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserFileMapper {

    //获取所有文件信息
    List<UserFile> findAllFiles();

    //保存文件上传记录
    void saveFile(UserFile userFile);

    //根据文件id获取文件信息
    UserFile findFileById(Integer id);

    //根据id更新下载次数
    void updateDowncounts(UserFile userFile);

    //根据文件id删除文件记录
    void deleteFile(Integer id);
}
