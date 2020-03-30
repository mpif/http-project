/**
 * Copyright (C), 2015-2020, 京东
 * FileName: HttpContentBuilder
 * Author:   caishengzhi
 * Date:     2020/3/26 9:14
 * Description: HTTP内容构造器
 */
package com.http.client;


import com.http.HttpBase;
import com.http.util.CollectionUtil;
import com.http.util.IoUtil;
import com.http.util.StringUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * HTTP内容构造器
 *
 * @author: caishengzhi
 * @Date: 2020/03/26 09:14
 * @since: 1.0.0
 */
public class HttpContentBuilder extends HttpBase {




    public static String buildBasicGet(String url) throws UnsupportedEncodingException {
        String request = "";
        String path = StringUtil.getUri(url);
        String query = StringUtil.getQuery(url);
        path = path + "?" + getEncodedQuery(query);
        System.out.println("path:");
        System.out.println(path);

        request = create().append(GET).append(BLANK).append(path).append(BLANK).append(HTTP_11).append("\r\n")
//        request = create().append(GET).append(BLANK).append(url).append(BLANK).append(HTTP_11).append("\r\n")
                .append(HOST).append(StringUtil.getAddress(url)).append("\r\n")
                .append(USER_AGENT).append("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36").append("\r\n")
                .append("Content-Type:text/html;charset=utf-8").append("\r\n")
                .append("Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").append("\r\n")
//                .append("Accept-Encoding:gzip, deflate, sdch").append("\r\n")
//                .append("Accept-Language:zh-CN,zh;q=0.8,en;q=0.6").append("\r\n")
//                .append("Upgrade-Insecure-Requests:1").append("\r\n")
//                .append("Connection: Keep-Alive").append("\r\n")
                .append("Connection: close").append("\r\n")
                .append("\r\n").toString();
        return request;
    }

    public static String buildUrlPost(String url, Map<String, String> paramMap) {
        String request = "";
        String path = StringUtil.getUri(url);
        String query = StringUtil.getQuery(url);
        path = path + "?" + getEncodedQuery(query);
        System.out.println("path:");
        System.out.println(path);

        request = create().append(POST).append(BLANK).append(path).append(BLANK).append(HTTP_11).append("\r\n")
//        request = create().append(GET).append(BLANK).append(url).append(BLANK).append(HTTP_11).append("\r\n")
                .append("Host:").append(StringUtil.getAddress(url)).append("\r\n")
                .append("User-Agent:").append("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36").append("\r\n")
                .append("Content-Type:text/html;charset=utf-8").append("\r\n")
                .append("Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").append("\r\n")
//                .append("Accept-Encoding:gzip, deflate, sdch").append("\r\n")
//                .append("Accept-Language:zh-CN,zh;q=0.8,en;q=0.6").append("\r\n")
//                .append("Upgrade-Insecure-Requests:1").append("\r\n")
//                .append("Connection: Keep-Alive").append("\r\n")
                .append("Connection: close").append("\r\n")
                .append("\r\n").toString();
        return request;
    }

    /**
     * body提交参数, Content-Type、Content-Length这两个header必传
     * 如果只传参数, 则Content-Type设置为application/x-www-form-urlencoded就行
     * @param url
     * @param paramMap
     * @return
     */
    public static String buildBodyPost(String url, Map<String, String> paramMap) {
        String request = "";
        String path = StringUtil.getUri(url);
//        System.out.println("path:");
//        System.out.println(path);

        String body = generatePostBody(paramMap);
//        System.out.println("body:");
//        System.out.println(body);

        request = create().append(POST).append(BLANK).append(path).append(BLANK).append(HTTP_11).append("\r\n")
                .append("Host:").append(StringUtil.getAddress(url)).append("\r\n")
//                .append("User-Agent:").append("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36").append("\r\n")
                .append("Content-Type:application/x-www-form-urlencoded").append("\r\n")
                .append("Content-Length: ").append(body.length()).append("\r\n")
//                .append("Connection: Keep-Alive").append("\r\n")
//                .append("Connection: keep-alive").append("\r\n")
                .append("Connection: close").append("\r\n")
                .append("\r\n")
                .append(body).append("\r\n")
                .append("\r\n").toString();
        return request;
    }

    /**
     * Content-Length: 1505383
     * Content-Type: multipart/form-data; boundary=----WebKitFormBoundaryxPczeX2yoPFwBjl8
     *
     * @param url
     * @param paramMap
     * @param attachList
     * @return
     */
    public static String buildParamAndAttachmentPost(String url, Map<String, String> paramMap, List<String> attachList) {
        String request = "";
        String path = StringUtil.getUri(url);
        String addr = StringUtil.getAddress(url);

        StringBuilder builder = create().append(POST).append(BLANK).append(path).append(BLANK).append(HTTP_11).append("\r\n")
                .append("Host:").append(addr).append("\r\n")
                .append("Connection: close").append("\r\n")
                .append(CONTENT_TYPE).append("multipart/form-data; boundary=").append(BOUNDARY).append("\r\n")
                .append("Content-Length: ").append("123").append("\r\n")
//                .append("Connection: Keep-Alive").append("\r\n")
                .append("\r\n");

        request = builder.toString();
        return request;
    }

    /**
     * ------WebKitFormBoundaryrGKCBY7qhFd3TrwA
     * Content-Disposition: form-data; name="text"
     *
     * title
     * @param paramMap
     * @return
     */
    public static String generateParamContent(Map<String, String> paramMap) {
        StringBuilder sb = create();
        if(paramMap != null && paramMap.size() > 0) {
            Iterator<String> iter = paramMap.keySet().iterator();
            String key = "";
            int index = 0;
            while(iter.hasNext()) {
                key = iter.next();
//                sb.append(BOUNDARY_PREFIX).append(BOUNDARY).append(LINE_SEPERATOR)
//                .append(CONTENT_DISPOSITION).append(" form-data; name=\"" + key + "\"").append(LINE_SEPERATOR)
////                .append(CONTENT_TYPE).append("text/plain; charset=UTF-8").append(LINE_SEPERATOR)
//                .append(LINE_SEPERATOR)
////                .append(encode(paramMap.get(key))).append(LINE_SEPERATOR);
//                .append(paramMap.get(key)).append(LINE_SEPERATOR);
                sb.append(generatorKeyAndValue(key, paramMap.get(key)));
                index++;
            }
        }
        return sb.toString();
    }

    public static String generatorKeyAndValue(String key, String value) {
        StringBuilder paramSb = new StringBuilder();
        paramSb.append(BOUNDARY_PREFIX);
        paramSb.append(BOUNDARY);
        paramSb.append(LINE_SEPERATOR);//fileCount
        paramSb.append("Content-Disposition: form-data; name=\"" + key + "\"").append(LINE_SEPERATOR);
        paramSb.append(LINE_SEPERATOR);
        paramSb.append(value);
        paramSb.append(LINE_SEPERATOR);
        return paramSb.toString();
    }

    /**
     * ------WebKitFormBoundaryrGKCBY7qhFd3TrwA
     * Content-Disposition: form-data; name="file"; filename="chrome.png"
     * Content-Type: image/png
//     * Content-Transfer-Encoding: binary
     * Content-Transfer-Encoding: base64
     *
     * PNG ... content of chrome.png ...
     * ------WebKitFormBoundaryrGKCBY7qhFd3TrwA--
     * @param fileList
     * @return
     */
    public static String generateAttachContent(List<String> fileList) {
        StringBuilder sb = create();
        if(CollectionUtil.isNotEmpty(fileList)) {
            Iterator<String> iter = fileList.iterator();
            String key = "";
            File file = null;
            int index = 0;
            while(iter.hasNext()) {
                key = iter.next();
                file = new File(key);

                sb.append(BOUNDARY).append(LINE_SEPERATOR)
                  .append(CONTENT_DISPOSITION).append(" form-data; name=\"file\"; filename=\"").append(file.getName()).append("\"").append(LINE_SEPERATOR)
                  .append(CONTENT_TYPE).append("image/png").append(LINE_SEPERATOR)
                  .append(CONTENT_TRANSFER_ENCODING).append("base64").append(LINE_SEPERATOR)
                  .append(LINE_SEPERATOR)
                  .append(IoUtil.getBase64Str(file)).append(LINE_SEPERATOR);
                index++;
            }

        }
        return sb.toString();
    }

}