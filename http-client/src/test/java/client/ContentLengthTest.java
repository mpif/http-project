/**
 * Copyright (C), 2015-2020, 京东
 * FileName: ContentLengthTest
 * Author:   caishengzhi
 * Date:     2020/3/30 0:00
 * Description: 内容长度测试
 */
package client;


import org.junit.Test;

import java.io.File;

/**
 *
 * 内容长度测试
 *
 * @author: caishengzhi
 * @Date: 2020/03/30 00:00
 * @since: 1.0.0
 */
public class ContentLengthTest {

    /**
     * value加上文件大小为：309704
     * 浏览器显示上传的Content-Length为：310233
     */
    @Test
    public void contentLengthTest() {

        long expectedSize = 310328L;

        long totalSize = 0L;

        String valueOfFileType = "fTypeHaha";
        String valueOfFileCount = "4789";

//        String boundary = "---------------------------168037971832414782023539857849";

        StringBuilder builder = new StringBuilder();
        builder.append("-----------------------------168037971832414782023539857849");
        builder.append("Content-Disposition: form-data; name=\"file\"; filename=\"chrome更新前版本.png\"");
        builder.append("Content-Type: image/png");
        builder.append("-----------------------------168037971832414782023539857849");
        builder.append("Content-Disposition: form-data; name=\"file\"; filename=\"blocker.png\"");
        builder.append("Content-Type: image/png");
        builder.append("-----------------------------168037971832414782023539857849");
        builder.append("Content-Disposition: form-data; name=\"fileType\"");
        builder.append("-----------------------------168037971832414782023539857849");
        builder.append("Content-Disposition: form-data; name=\"fileCount\"");

        String endLine = "-----------------------------168037971832414782023539857849--";

        File file01 = new File("C:\\Users\\caishengzhi\\Pictures\\blocker.png");
        File file02 = new File("C:\\Users\\caishengzhi\\Pictures\\chrome更新前版本.png");

        totalSize += valueOfFileType.getBytes().length;
        totalSize += valueOfFileCount.getBytes().length;
        totalSize += builder.toString().getBytes().length;
        totalSize += endLine.getBytes().length;
        totalSize += file01.length();
        totalSize += file02.length();

        System.out.println("totalSize=" + totalSize);

//        -----------------------------168037971832414782023539857849
//        Content-Disposition: form-data; name="file"; filename="chromeæ´æ°åçæ¬.png"
//        Content-Type: image/png
//
//                -----------------------------168037971832414782023539857849
//        Content-Disposition: form-data; name="file"; filename="blocker.png"
//        Content-Type: image/png
//
//                -----------------------------168037971832414782023539857849
//        Content-Disposition: form-data; name="fileType"
//        fTypeHaha
//                -----------------------------168037971832414782023539857849
//        Content-Disposition: form-data; name="fileCount"
//        4789
//        -----------------------------168037971832414782023539857849--


    }


}