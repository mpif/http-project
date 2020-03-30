/**
 * Copyright (C), 2015-2020, 京东
 * FileName: ArrayUtil
 * Author:   caishengzhi
 * Date:     2020/3/28 9:15
 * Description: 数组工具类
 */
package com.http.util;


/**
 *
 * 数组工具类
 *
 * @author: caishengzhi
 * @Date: 2020/03/28 09:15
 * @since: 1.0.0
 */
public class ArrayUtil {

    /**
     * 判断数组是否为空
     * @param arr
     * @return
     */
    public static boolean isEmpty(String[] arr) {
        if(arr == null || arr.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断数组是否不为空
     * @param arr
     * @return
     */
    public static boolean isNotEmpty(String[] arr) {
        return !isEmpty(arr);
    }

}