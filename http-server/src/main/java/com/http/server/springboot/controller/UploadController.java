package com.http.server.springboot.controller;

import com.http.util.CollectionUtil;
import com.http.util.DateUtil;
import com.http.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Description
 * @Author sgl
 * @Date 2018-05-15 14:04
 */
@Controller
public class UploadController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

    private String uploadDir = "D:/caishengzhi/Downloads/uploadFiles/";

    @GetMapping("/upload")
    public String toUpload(@RequestParam(name="userName", required = false) String userName) {

        System.out.println("userName=" + userName + ", time=" + DateUtil.format(LocalDateTime.now()));

        return "upload";
    }

    @GetMapping("/multiUpload")
    public String toMultiUpload(@RequestParam(name="pass", required = false) String pass) {

        System.out.println("pass=" + pass + ", time=" + DateUtil.format(LocalDateTime.now()));

        return "multiUpload";
    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestParam(name="name", required = false) String name,
                        @RequestParam(name="pass", required = false) String pass) {

        System.out.println("name=" + name + ", pass=" + pass + ", time=" + DateUtil.format(LocalDateTime.now()));

        return "responseBody[name=" + name + ", pass=" + pass + "]";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();

        String filePath = uploadDir;
        File dest = new File(filePath + fileName);
        try {

            if(!dest.exists()) {
                boolean mkdirsResult = dest.mkdirs();
                System.out.println("mkdirsResult=" + mkdirsResult);
                dest.createNewFile();
            }

            file.transferTo(dest);
            LOGGER.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            LOGGER.error("上传失败, filePath=" + dest.getAbsolutePath(), e);
        }
        return "上传失败！";
    }

    @PostMapping("/multiUpload")
    @ResponseBody
    public String multiUpload(HttpServletRequest request) throws IOException, ServletException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files = multipartRequest.getFiles("file");

//        HttpHeaders httpHeaders = multipartRequest.getMultipartHeaders("Content-Disposition");
//        ContentDisposition contentDisposition = httpHeaders.getContentDisposition();
//        String name = contentDisposition.getName();

        long totalBytesUploaded = 0L;

        String fileType = multipartRequest.getParameter("fileType");
        System.out.println("fileType:" + fileType);
        if(StringUtil.isNotBlank(fileType)) {
            totalBytesUploaded += fileType.getBytes().length;
        }

        String fileCount = multipartRequest.getParameter("fileCount");
        System.out.println("fileCount:" + fileCount);
        if(StringUtil.isNotBlank(fileCount)) {
            totalBytesUploaded += fileCount.getBytes().length;
        }

        String filePath = uploadDir;
        if(CollectionUtil.isNotEmpty(files)) {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (file.isEmpty()) {
                    return "上传第" + (i++) + "个文件失败";
                }
                String fileName = file.getOriginalFilename();

                File dest = new File(filePath + fileName);
                try {
                    if (!dest.exists()) {
                        if (!dest.getParentFile().exists()) {
                            dest.getParentFile().mkdirs();
                        }
                        dest.createNewFile();
                    }
                    file.transferTo(dest);
                    LOGGER.info("第" + (i + 1) + "个文件上传成功");
                } catch (IOException e) {
                    LOGGER.error(e.toString(), e);
                    return "上传第" + (i++) + "个文件失败";
                }
                totalBytesUploaded += dest.length();
            }

            System.out.println("上传总大小为=" + totalBytesUploaded);
            return "上传成功";
        } else {
            return "参数file为空";
        }

    }

}
