/**
 * Copyright (C), 2015-2020, 京东
 * FileName: CollectionUtil
 * Author:   caishengzhi
 * Date:     2020/3/28 9:14
 * Description: 集合工具类
 */
package com.http.util;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 集合工具类
 *
 * @author: caishengzhi
 * @Date: 2020/03/28 09:14
 * @since: 1.0.0
 */
public class CollectionUtil {

    public static Map<String, String> createMap() {
        return new HashMap<String, String>();
    }

    public static boolean isEmpty(Map map) {
        if(map == null || map.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(List list) {
        if(list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotEmpty(List list) {
        return !isEmpty(list);
    }

}