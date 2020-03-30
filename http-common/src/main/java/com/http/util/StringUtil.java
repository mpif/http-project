/**
 * Copyright (C), 2015-2020, 京东
 * FileName: StringUtil
 * Author:   caishengzhi
 * Date:     2020/3/26 10:04
 * Description: 字符串工具类
 */
package com.http.util;


import java.net.URI;

/**
 *
 * 字符串工具类
 *
 * @author: caishengzhi
 * @Date: 2020/03/26 10:04
 * @since: 1.0.0
 */
public class StringUtil {

    /**
     * 获取uri
     *
     * @Author caishengzhi
     * @param url
     * @Date 2020/3/26 11:50
     * @return java.lang.String
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 判断字符串str是否为空
     */
    public static boolean isBlank(String str) {
        if(str == null || str.trim().length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取uri
     *
     * @Author caishengzhi
     * @param url
     * @Date 2020/3/26 11:50
     * @return java.lang.String
     */
    public static String getUri(String url) {
        return URI.create(url).getPath();
    }

    /**
     * 获取url中拼接的参数
     *
     * @Author caishengzhi
     * @param url
     * @Date 2020/3/26 11:50
     * @return java.lang.String
     */
    public static String getQuery(String url) {
        return URI.create(url).getQuery();
    }

    /**
     * 获取url中拼接的参数
     *
     * @Author caishengzhi
     * @param url
     * @Date 2020/3/26 11:50
     * @return java.lang.String
     */
    public static String getPathAndQuery(String url) {
        URI uri = URI.create(url);
        StringBuffer sb = new StringBuffer();
        sb.append(uri.getPath());
        String query = uri.getQuery();
        if(StringUtil.isNotBlank(query)) {
            sb.append("?").append(query);
        }
        return sb.toString();
    }

    /**
     * 获取url中的Host
     *
     * @Author caishengzhi
     * @param url
     * @Date 2020/3/26 11:50
     * @return java.lang.String
     */
    public static String getHost(String url) {
        return URI.create(url).getHost();
    }

    /**
     * 获取url中的port
     *
     * @Author caishengzhi
     * @param url
     * @Date 2020/3/26 11:50
     * @return java.lang.Integer
     */
    public static int getPort(String url) {
        return URI.create(url).getPort();
    }

    /**
     * 获取url中的Host+Port
     *
     * @Author caishengzhi
     * @param url
     * @Date 2020/3/26 11:50
     * @return java.lang.String
     */
    public static String getAddress(String url) {
        URI uri = URI.create(url);
        StringBuffer sb = new StringBuffer();
        sb.append(uri.getHost()).append(":").append(uri.getPort());
        return sb.toString();
    }

}