/**
 * Copyright (C), 2015-2020, 京东
 * FileName: CommonHttpClient
 * Author:   caishengzhi
 * Date:     2020/3/24 21:58
 * Description: 通常情况下的http客户端
 */
package com.http.client;


import com.http.HttpBase;
import com.http.util.CollectionUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * 通常情况下的http客户端
 *
 * @author: caishengzhi
 * @Date: 2020/03/24 21:58
 * @since: 1.0.0
 */
public class CommonHttpClient extends HttpBase {

    /**
     * 发送get请求
     */
    public static String get(String url) throws IOException {

        String responseBody = "";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            responseBody = httpclient.execute(httpget, responseHandler);
            System.out.println("----------------------------------------");
        } finally {
            httpclient.close();
        }
        return responseBody;
    }

    /**
     * 发送get请求
     */
    public static String post(String url, Map<String, String> paramMap) throws IOException {

        String responseBody = "";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(url);

            if(paramMap != null && paramMap.size() > 0) {
                List<NameValuePair> nameValuePairList = new ArrayList<>(paramMap.size());
                Iterator<String> iter = paramMap.keySet().iterator();
                String key = "";
                while(iter.hasNext()) {
                    key = iter.next();
                    nameValuePairList.add(new BasicNameValuePair(key, paramMap.get(key)));
                }
                HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList);
                httpPost.setEntity(httpEntity);
            }

            System.out.println("Executing request " + httpPost.getRequestLine());

            // Create a custom response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                @Override
                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };
            responseBody = httpclient.execute(httpPost, responseHandler);
            System.out.println("----------------------------------------");
        } finally {
            httpclient.close();
        }
        return responseBody;
    }

    public static String singleFileUpload(String url, String filePath) throws Exception {
        if (filePath == null || filePath.trim().length() == 0)  {
            System.out.println("File path not given");
            System.exit(1);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost httppost = new HttpPost(url);

            File file = new File(filePath);

            InputStreamEntity reqEntity = new InputStreamEntity(
                    new FileInputStream(file), -1, ContentType.APPLICATION_OCTET_STREAM);
            reqEntity.setChunked(true);
            // It may be more appropriate to use FileEntity class in this particular
            // instance but we are using a more generic InputStreamEntity to demonstrate
            // the capability to stream out data from any arbitrary source
            //
            // FileEntity entity = new FileEntity(file, "binary/octet-stream");

            httppost.setEntity(reqEntity);

            System.out.println("Executing request: " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                result = EntityUtils.toString(response.getEntity());
                System.out.println(result);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    }

    public static String multiFileUpload(String url, Map<String, String> paramMap, List<String> filePathList) throws Exception {
//        if (filePathList == null || filePathList.size() == 0)  {
//            System.out.println("File path not given");
//            System.exit(1);
//        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost httppost = new HttpPost(url);

            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            //解决乱码方法:1：设置模式为BROWSER_COMPATIBLE，并设置字符集为UTF8
            multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            multipartEntityBuilder.setCharset(Charset.forName("UTF-8"));

            //解决乱码方法2：设置模式为RFC6532
//            multipartEntityBuilder.setMode(HttpMultipartMode.RFC6532);
            /**
             *
             * 乱码问题参考资料：
             * HttpClient上传文件中文名乱码
             * https://blog.csdn.net/youshounianhua123/article/details/81100778
             *
             */

            for(String filePath : filePathList) {
                multipartEntityBuilder.addPart("file", new FileBody(new File(filePath)));
            }
            if(CollectionUtil.isNotEmpty(paramMap)) {
                Iterator<String> iter = paramMap.keySet().iterator();
                String key = "";
                while(iter.hasNext()) {
                    key = iter.next();
                    multipartEntityBuilder.addPart(key, new StringBody(paramMap.get(key), ContentType.create("text/plain", "UTF-8")));
                }
            }
            HttpEntity reqEntity = multipartEntityBuilder.build();
            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                result = EntityUtils.toString(resEntity);
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return result;
    }

}