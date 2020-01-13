/**
 * Copyright (C), 2015-2020, 京东
 * FileName: UploadController
 * Author:   caishengzhi
 * Date:     2020/1/13 21:35
 * Description: 上传下载
 */
package com.http.server.springboot;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 *
 * 上传下载
 *
 * @author: mpif
 * @Date: 2020/01/13 21:35
 * @since: 1.0.0
 */
@Controller
public class UploadController {

    private Logger log = LoggerFactory.getLogger(UploadController.class);

    @RequestMapping("/")
    String home() {
        System.out.println("welcome home!!!");
        return "Hello World!";
    }

    @GetMapping("/toUpload")
    String toUpload() {
        System.out.println("welcome home!!!");
        return "upload";
    }

    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String fileName = file.getOriginalFilename();
        String filePath = "D:/caishengzhi/Downloads/upload files/";
        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            log.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
        return "上传失败！";
    }

}