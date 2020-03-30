/**
 * Copyright (C), 2015-2020, 京东
 * FileName: CommonHttpClientTest
 * Author:   caishengzhi
 * Date:     2020/3/25 11:39
 * Description: http客户端测试类
 */
package client;


import com.http.client.CommonHttpClient;
import com.http.util.CollectionUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * http客户端测试类
 *
 * @author: caishengzhi
 * @Date: 2020/03/25 11:39
 * @since: 1.0.0
 */
public class CommonHttpClientTest {

    private String urlPrefix = "http://localhost:8088";

    /**
     * getTest
     *
     * @throws IOException
     */
    @Test
    public void getTest() throws IOException {

        String url = urlPrefix + "/upload";
        String responseBody = CommonHttpClient.get(url);
        System.out.println("url=" + url);
        System.out.println("responseBody=");
        System.out.println(responseBody);

    }

    /**
     * postTest
     *
     * @throws IOException
     */
    @Test
    public void postTest() throws IOException {

        String url = urlPrefix + "/login";
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("name", "zhangsan");
        paramMap.put("pass", "123");
        String responseBody = CommonHttpClient.post(url, paramMap);
        System.out.println("url=" + url);
        System.out.println("responseBody=");
        System.out.println(responseBody);

    }

    /**
     * singleFileUploadTest
     *
     * @throws IOException
     */
    @Test
    public void singleFileUploadTest() throws Exception {

        String url = urlPrefix + "/upload";
        String filePath = "C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速.png";
        String responseBody = CommonHttpClient.singleFileUpload(url, filePath);
        System.out.println("url=" + url);
        System.out.println("responseBody=");
        System.out.println(responseBody);

    }

    /**
     * multiFileUploadTest
     *
     * @throws IOException
     */
    @Test
    public void multiFileUploadTest() throws Exception {

        String url = urlPrefix + "/multiUpload";

        Map<String, String> paramMap = CollectionUtil.createMap();
        paramMap.put("fileType", "赵六");
        paramMap.put("fileCount", "4789");

        String filePath = "C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速.png";
        String filePath2 = "C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速2.png";
        String filePath3 = "C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速3.png";
        List<String> filePathList = new ArrayList<>();
        filePathList.add(filePath);
        filePathList.add(filePath2);
        filePathList.add(filePath3);
        String responseBody = CommonHttpClient.multiFileUpload(url, paramMap, filePathList);
        System.out.println("url=" + url);
        System.out.println("responseBody=");
        System.out.println(responseBody);

    }

}