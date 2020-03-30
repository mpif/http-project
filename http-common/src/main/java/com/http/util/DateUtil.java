/**
 * Copyright (C), 2015-2020, 京东
 * FileName: DateUtil
 * Author:   caishengzhi
 * Date:     2020/3/28 7:14
 * Description: 日期工具类
 */
package com.http.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 *
 * 日期工具类
 *
 * @author: caishengzhi
 * @Date: 2020/03/28 07:14
 * @since: 1.0.0
 */
public class DateUtil {

    /**
     * 默认日期格式
     * @Author caishengzhi
     * @param null
     * @Date 2020/3/28 7:18
     * @return
     */
    private static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 按pattern格式来格式化date
     */
    public static String format(LocalDateTime date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date);
    }

    /**
     * 按默认格式来格式化date
     */
    public static String format(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_PATTERN);
        return formatter.format(date);
    }

}