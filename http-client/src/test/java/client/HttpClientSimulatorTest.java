/**
 * Copyright (C), 2015-2020, 京东
 * FileName: HttpClientSimulatorTest
 * Author:   caishengzhi
 * Date:     2020/3/26 11:51
 * Description: http客户端模拟器
 */
package client;


import com.http.client.HttpClientSimulator;
import com.http.util.CollectionUtil;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * http客户端模拟器
 *
 * @author: caishengzhi
 * @Date: 2020/03/26 11:51
 * @since: 1.0.0
 */
public class HttpClientSimulatorTest {

    private HttpClientSimulator httpClientSimulator;

    private static final String URL_PREFIX = "http://localhost:8088";

    @Before
    public void before() {
        httpClientSimulator = new HttpClientSimulator();
    }

    @Test
    public void getTest() throws IOException {


        String urlStr = URL_PREFIX + "/upload?userName=张三";
//        String urlStr = "http://localhost:8088/upload?userName=zhangsan";
//        urlStr = URLEncoder.encode(urlStr, "UTF-8");
//        System.out.println("urlStr:");
//        System.out.println(urlStr);
        String responseCotent = httpClientSimulator.get(urlStr);
        System.out.println("responseContent=" + responseCotent);

    }

    @Test
    public void postUrlParamTest() throws IOException {

        String postUrl = URL_PREFIX + "/login?name=wangwu&pass=456";
        Map<String, String> paramMap = null;
        String responseCotent = httpClientSimulator.post(postUrl, paramMap);
        System.out.println("responseContent:");
        System.out.println(responseCotent);

    }

    @Test
    public void postBodyParamTest() throws IOException {

        String postUrl = URL_PREFIX + "/login";
        Map<String, String> paramMap = CollectionUtil.createMap();
        paramMap.put("name", "wangwu22");
        paramMap.put("pass", "456_222");
        long start = System.currentTimeMillis();
        String responseStr = httpClientSimulator.post(postUrl, paramMap);
        long end = System.currentTimeMillis();

        System.out.println("responseStr:");
        System.out.println(responseStr);

        System.out.println("read response cost:[" + (end - start)/1000 + "s]");


    }

    @Test
    public void postAttachmentTest() throws IOException {

        String postUrl = URL_PREFIX + "/multiUpload";

        String filePath = "C:\\Users\\caishengzhi\\Pictures\\chrome更新前版本.png";

        long start = System.currentTimeMillis();
        String responseStr = httpClientSimulator.postAttachment(postUrl, filePath);
        long end = System.currentTimeMillis();

        System.out.println("responseStr:");
        System.out.println(responseStr);

        System.out.println("read response cost:[" + (end - start)/1000 + "s]");


    }

    @Test
    public void postParamAndAttachmentTest() throws IOException {

        String postUrl = URL_PREFIX + "/multiUpload";
        Map<String, String> paramMap = CollectionUtil.createMap();
//        paramMap.put("fileType", "赵六");
        paramMap.put("fileType", "fTypeHaha");
        paramMap.put("fileCount", "4789");

        List<String> attachList = new ArrayList<>();
        attachList.add("C:\\Users\\caishengzhi\\Pictures\\blocker.png");
        attachList.add("C:\\Users\\caishengzhi\\Pictures\\chrome更新前版本.png");
        attachList.add("C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速.png");
//        attachList.add("C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速2.png");
//        attachList.add("C:\\Users\\caishengzhi\\Pictures\\电脑-wifi-测速3.png");
//        attachList.add("C:\\Users\\caishengzhi\\Pictures\\电脑-宽带连接-测速1.png");
//        attachList.add("C:\\Users\\caishengzhi\\Pictures\\电脑-宽带连接-测速2.png");
//        attachList.add("C:\\Users\\caishengzhi\\Pictures\\电脑-宽带连接-测速3.png");

        long start = System.currentTimeMillis();
        String responseStr = httpClientSimulator.postParamAndFile(postUrl, paramMap, attachList);
        long end = System.currentTimeMillis();

        System.out.println("responseStr:");
        System.out.println(responseStr);

        System.out.println("read response cost:[" + (end - start)/1000 + "s]");


    }


}