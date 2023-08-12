package com.jgq.files.service;

import com.jgq.files.entity.UserFile;

import java.util.List;

public interface UserFileService {

    //    查询所有的文件信息
    List<UserFile> findAllFiles();

    //    保存上传的文件记录
    void saveFile(UserFile userFile);

    //    根据文件id查找文件信息
    UserFile findFileById(Integer id);

    //    更新文件下载量
    void updateDowncounts(UserFile userFile);

    //    删除文件
    void deleteFile(Integer id);
}
