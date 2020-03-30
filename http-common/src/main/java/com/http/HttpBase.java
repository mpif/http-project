/**
 * Copyright (C), 2015-2020, 京东
 * FileName: HttpBase
 * Author:   caishengzhi
 * Date:     2020/3/28 9:10
 * Description: HTTP基础类
 */
package com.http;


import com.http.util.ArrayUtil;
import com.http.util.CollectionUtil;
import com.http.util.StringUtil;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * HTTP基础类
 *
 * @author: caishengzhi
 * @Date: 2020/03/28 09:10
 * @since: 1.0.0
 */
public class HttpBase {

    public static final String DEFAULT_CHARSET = "utf-8";

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String HTTP_10 = "HTTP/1.0";
    public static final String HTTP_11 = "HTTP/1.1";
    public static final String BLANK = " ";

    /**
     * 回车，CR，Carriage Return
     */
    public static final String CR = "\r";

    /**
     * 换行, LF，Line Feed
     */
    public static final String LF = "\n";

    public static final String HOST = "Host:";

    public static final String USER_AGENT = "User-Agent:";

    public static final String ACCEPT = "Accept:";

    public static final String CONNECTION = "Connection:";

    public static final String CONTENT_TYPE = "Content-Type:";

    public static final String CONTENT_DISPOSITION = "Content-Disposition:";

    public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding:";

    public static final String LINE_SEPERATOR = "\r\n";

//    public static final String BOUNDARY = "----WebKitFormBoundaryxPczeX2yoPFwBjl8";
    public static final String BOUNDARY = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
    public static final String BOUNDARY_PREFIX = "--";
    public static final String BOUNDARY_SUFFIX = "--";

    public static StringBuilder create() {
        return new StringBuilder();
    }

    /**
     * 对str进行编码
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String str) {
        String encodeStr = "";
        try {
            encodeStr = URLEncoder.encode(str, DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            encodeStr = str;
            e.printStackTrace();
        }
        return encodeStr;
    }

    /**
     * 获取编码后的query
     * @param query
     * @return
     */
    public static String getEncodedQuery(String query) {
        StringBuffer sb = new StringBuffer();
        if(StringUtil.isNotBlank(query)) {
            String[] params = query.split("&");
            if(ArrayUtil.isNotEmpty(params)) {
                for(int i = 0; i < params.length; i ++) {
                    String[] arr = params[i].split("=");
                    if(ArrayUtil.isNotEmpty(arr) && arr.length == 2) {
                        if(i == 0) {
                            sb.append(arr[0]).append("=").append(encode(arr[1]));
                        } else {
                            sb.append("&").append(arr[0]).append("=").append(encode(arr[1]));
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * 组装post请求的body
     * @param paramMap
     * @return
     */
    public static String generatePostBody(Map<String, String> paramMap) {
        StringBuffer sb = new StringBuffer();
        if(paramMap != null && paramMap.size() > 0) {
            Iterator<String> iter = paramMap.keySet().iterator();
            int index = 0;
            String key = "";
            while(iter.hasNext()) {
                key = iter.next();
                if(index != 0) {
                    sb.append("&");
                }
                sb.append(key).append("=").append(encode(paramMap.get(key)));
                index++;
            }
        }
        return sb.toString();
    }

    public static long getTotalSize(Map<String, String> paramMap, List<String> fileList) {
        long totalSize = 0L;

        if(CollectionUtil.isNotEmpty(paramMap)) {
            Iterator<Map.Entry<String, String>> iter = paramMap.entrySet().iterator();
            Map.Entry<String, String> item = null;
            while(iter.hasNext()) {
                item = iter.next();
                totalSize += item.getValue().getBytes().length;
            }
        }

        if(CollectionUtil.isNotEmpty(fileList)) {
            File file = null;
            for(String filePath : fileList) {
                file = new File(filePath);
                totalSize += file.length();
            }
        }

        return totalSize;
    }

}