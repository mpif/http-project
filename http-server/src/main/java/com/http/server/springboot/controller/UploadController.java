package com.http.server.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
    public String toUpload() {
        return "upload";
    }

    @GetMapping("/multiUpload")
    public String toMultiUpload() {
        return "multiUpload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();

//        String filePath = "C:/Users/Adminstrator/Downloads/uploadFiles/";
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
    public String multiUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filePath = uploadDir;
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return "上传第" + (i++) + "个文件失败";
            }
            String fileName = file.getOriginalFilename();

            File dest = new File(filePath + fileName);
            try {
                if(!dest.exists()) {
                    if(!dest.getParentFile().exists()) {
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
        }

        return "上传成功";

    }

}
