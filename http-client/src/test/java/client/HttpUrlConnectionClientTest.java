/**
 * Copyright (C), 2015-2020, 京东
 * FileName: HttpUrlConnectionClientTest
 * Author:   caishengzhi
 * Date:     2020/3/29 6:59
 * Description: HttpUrlConnectionClient测试类
 */
package client;


import com.http.client.HttpUrlConnectionClient;
import com.http.util.CollectionUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * HttpUrlConnectionClient测试类
 *
 * @author: caishengzhi
 * @Date: 2020/03/29 06:59
 * @since: 1.0.0
 */
public class HttpUrlConnectionClientTest {

    private HttpUrlConnectionClient httpUrlConnectionClient;

    @Before
    public void before() {
        httpUrlConnectionClient = new HttpUrlConnectionClient();
    }

    @Test
    public void multiUploadTest() {

        String uploadUrlStr = "http://localhost:8088/multiUpload";
        String filePath = "C:\\Users\\caishengzhi\\Pictures\\chrome更新前版本.png";
//        String filePath = null;

        String paramKey = "fileType";
        String paramVal = "文件类型";

        httpUrlConnectionClient.uploadParamAndFile(uploadUrlStr, paramKey, paramVal, filePath);


    }

    @Test
    public void multiParamAndFileUploadTest() {

        String uploadUrlStr = "http://localhost:8088/multiUpload";

        String filePath = "C:\\Users\\caishengzhi\\Pictures\\chrome更新前版本.png";
        String filePath2 = "C:\\Users\\caishengzhi\\Pictures\\blocker.png";
        List<String> fileList = new ArrayList<String>();
        fileList.add(filePath);
        fileList.add(filePath2);

        Map<String, String> paramMap = CollectionUtil.createMap();
        paramMap.put("fileType", "valueOfFileType");
        paramMap.put("fileCount", "123");

        httpUrlConnectionClient.uploadMultiParamAndFile(uploadUrlStr, paramMap, fileList);


    }



}