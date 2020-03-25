/**
 * Copyright (C), 2015-2020, 京东
 * FileName: CommonHttpClientTest
 * Author:   caishengzhi
 * Date:     2020/3/25 11:39
 * Description: http客户端测试类
 */
package client;


import com.http.client.CommonHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * http客户端测试类
 *
 * @author: caishengzhi
 * @Date: 2020/03/25 11:39
 * @since: 1.0.0
 */
public class CommonHttpClientTest {

    /**
     * getTest
     *
     * @throws IOException
     */
    @Test
    public void getTest() throws IOException {

        String url = "";
        String responseBody = CommonHttpClient.get(url);
        System.out.println("url=" + url);
        System.out.println("responseBody=" + responseBody);

    }

    /**
     * postTest
     *
     * @throws IOException
     */
    @Test
    public void postTest() throws IOException {

        String url = "";
        String responseBody = CommonHttpClient.get(url);
        System.out.println("url=" + url);
        System.out.println("responseBody=" + responseBody);

    }

    /**
     * singleFileUploadTest
     *
     * @throws IOException
     */
    @Test
    public void singleFileUploadTest() throws Exception {

        String url = "http://localhost:8080/upload";
        String filePath = "C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速.png";
        String responseBody = CommonHttpClient.singleFileUpload(url, filePath);
        System.out.println("url=" + url);
        System.out.println("responseBody=" + responseBody);

    }

    /**
     * multiFileUploadTest
     *
     * @throws IOException
     */
    @Test
    public void multiFileUploadTest() throws Exception {

        String url = "http://localhost:8080/upload";
        String filePath = "C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速.png";
        List<String> filePathList = new ArrayList<>();
        filePathList.add(filePath);
        String responseBody = CommonHttpClient.multiFileUpload(url, filePathList);
        System.out.println("url=" + url);
        System.out.println("responseBody=" + responseBody);

    }

}