/**
 * Copyright (C), 2015-2020, 京东
 * FileName: RegexUtil
 * Author:   caishengzhi
 * Date:     2020/3/26 10:10
 * Description: 正则表达式工具类
 */
package com.http.util;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * 正则表达式工具类
 *
 * @author: caishengzhi
 * @Date: 2020/03/26 10:10
 * @since: 1.0.0
 */
public class RegexUtil {

    public static String parse(String txt, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(txt);
        if(matcher.matches()) {
            return matcher.group(0);
        }
        return null;
    }

}