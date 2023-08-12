package com.jgq.files.service.Impl;

import com.jgq.files.dao.UserFileMapper;
import com.jgq.files.entity.UserFile;
import com.jgq.files.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserFileServiceImpl implements UserFileService {

    private final UserFileMapper userFileMapper;

    @Override
    public List<UserFile> findAllFiles() {
        return userFileMapper.findAllFiles();
    }

    @Override
    public void saveFile(UserFile userFile) {
        //当类型中含有image时说明当前类型一定为图片类型
        String isImg = userFile.getType().startsWith("image") ? "是" : "否";
        //设置文件是否是图片
        userFile.setIsImg(isImg);
        //设置文件默认下载数量为0
        userFile.setDowncounts(0);
        //设置文件上传时间
        userFile.setUploadTime(new Date());
        userFileMapper.saveFile(userFile);
    }

    @Override
    public UserFile findFileById(Integer id) {
        return userFileMapper.findFileById(id);
    }

    @Override
    public void updateDowncounts(UserFile userFile) {
        userFileMapper.updateDowncounts(userFile);
    }

    @Override
    public void deleteFile(Integer id) {
        userFileMapper.deleteFile(id);
    }
}
