/**
 * Copyright (C), 2015-2020, 京东
 * FileName: RegexUtilTest
 * Author:   caishengzhi
 * Date:     2020/3/26 10:12
 * Description: 正则表达式测试类
 */
package util;


import com.http.util.RegexUtil;
import org.junit.Test;

import java.net.URI;

/**
 *
 * 正则表达式测试类
 *
 * @author: caishengzhi
 * @Date: 2020/03/26 10:12
 * @since: 1.0.0
 */
public class RegexUtilTest {

    @Test
    public void domainTest() {

        String txt = "http://plus.m.jd.com/index";
        String regex = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+.?";
        System.out.println(RegexUtil.parse(txt, regex));

        URI uri = URI.create(txt);
        System.out.println(uri.getHost());
        System.out.println(uri.getPath());

    }




}