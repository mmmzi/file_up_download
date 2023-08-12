package com.jgq.files.controller;

import com.jgq.files.demos.web.User;
import com.jgq.files.entity.UserFile;
import com.jgq.files.service.UserFileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("file")
@RequiredArgsConstructor
public class UserFileController {

    private final UserFileService userFileService;

    /**
     * 展示所有的文件信息
     *
     * @param model
     * @return filIndex.html 文件主页面
     */
    @GetMapping("")
    public String showIndex(Model model) {
        List<UserFile> files = userFileService.findAllFiles();
        //向前端传递键值对
        model.addAttribute( "files", files);
        return "fileIndex";
    }

    /**
     * 上传用户文件
     *
     * @param newfile 前端上传文件的name值
     * @return "/file" 显示文件主页面的路径
     * @throws IOException
     */
    @PostMapping("upload")
    public String upload(MultipartFile newfile) throws IOException {

        //获取原始文件名称
        String oldFileName = newfile.getOriginalFilename();

        //获取原始文件后缀
        String extension = "." + FilenameUtils.getExtension(newfile.getOriginalFilename());

        //生成新的文件名，由当前时间+UUID字符串+文件后缀组成
        String newFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + UUID.randomUUID().toString().replace("-", "") + extension;

        //获取文件大小
        long size = newfile.getSize();

        //获取文件类型
        String type = newfile.getContentType();

        //根据日期生成目录
        //ResourceUtils.getURL("classpath:").getPath()获取了 resource目录下的路径信息
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static/files";
        String dateFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String dateDirPath = realPath + "/" + dateFormat;
        File dateDir = new File(dateDirPath);
        if (!dateDir.exists()) {
            //创建文件夹
            dateDir.mkdirs();
        }

        //处理文件上传，将文件上传到dateDir文件目录下
        newfile.transferTo(new File(dateDir, newFileName));

        //将文件信息放入数据库中保存
        UserFile userFile = new UserFile();
        userFile.setOldFileName(oldFileName);
        userFile.setNewFileName(newFileName);
        userFile.setExt(extension);
        userFile.setSize(String.valueOf(size));
        userFile.setType(type);
        userFile.setPath("/files" + "/" + dateFormat);
        userFileService.saveFile(userFile);
        return "redirect:/file";
    }

    /**
     * 文件下载
     *
     * @param openStyle 前端传入的值，若openStyple=inline代表打开文件，openStyple=attachment代表下载文件
     * @param id        需要下载的文件id
     * @param response
     * @throws IOException
     */
    @GetMapping("download")
    public void download(String openStyle, Integer id, HttpServletResponse response) throws IOException {
        //获取打开方式
        openStyle = openStyle == null ? "attachment" : openStyle;
        //根据id获取文件信息
        UserFile userFile = userFileService.findFileById(id);

        //点击下载链接时更新下载次数
        if ("attachment".equals(openStyle)) {
            userFile.setDowncounts(userFile.getDowncounts() + 1);
            userFileService.updateDowncounts(userFile);
        }

        //根据文件信息中文件名字和文件存储路径获取文件输入流
        String realpath = ResourceUtils.getURL("classpath:").getPath() + "/static" + userFile.getPath();

        //获取文件输入流
        FileInputStream is = new FileInputStream(new File(realpath, userFile.getNewFileName()));

        //附件下载
//        response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(userFile.getOldFileName(),"UTF-8"));
        //打开文件
//        response.setHeader("content-disposition","inline;fileName="+ URLEncoder.encode(userFile.getOldFileName(),"UTF-8"));

        //下载或打开文件，设置HTTP响应头的content-disposition字段，用于告知客户端如何处理文件。
        response.setHeader("content-disposition", openStyle + ";fileName=" + URLEncoder.encode(userFile.getOldFileName(), "UTF-8"));

        //获取相应输出流
        ServletOutputStream os = response.getOutputStream();

        //文件拷贝
        IOUtils.copy(is, os);

        //关闭输入流和输出流
        IOUtils.closeQuietly(is);
        IOUtils.closeQuietly(os);
    }

    /**
     * 根据文件id删除文件
     *
     * @param id 要删除文件的id
     * @return "/file" 显示文件主页面的路径
     * @throws FileNotFoundException
     */
    @GetMapping("delete")
    public String deleteFile(Integer id) throws FileNotFoundException {
        //根据id查询文件信息
        UserFile userFile = userFileService.findFileById(id);

        //删除文件
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "/static" + userFile.getPath();
        File file = new File(realPath, userFile.getNewFileName());
        if (file.exists()) {
            file.delete();
        }
        userFileService.deleteFile(id);
        return "redirect:/file";
    }
}
