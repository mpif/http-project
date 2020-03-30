/**
 * Copyright (C), 2015-2020, 京东
 * FileName: IoUtil
 * Author:   caishengzhi
 * Date:     2020/3/26 9:28
 * Description: IO工具类
 */
package com.http.util;


import com.http.HttpBase;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.BASE64EncoderStream;

import java.io.*;

/**
 *
 * IO工具类
 *
 * @author: caishengzhi
 * @Date: 2020/03/26 09:28
 * @since: 1.0.0
 */
public class IoUtil {

    /**
     * 读取字符串
     */
    public static String getString(InputStream is) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = "";
        while((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
//        while((len = is.read(buffer)) != -1) {
//            baos.write(buffer, 0, len);
//        }
        return sb.toString();
    }

    public static String getBase64Str(File file) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            baos = new ByteArrayOutputStream();
            bos = new BufferedOutputStream(new BASE64EncoderStream(baos));

            byte[] buffer = new byte[4096];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (bos != null) {
                    bos.flush();
                    bos.close();
                    bos = null;
                }
                if (bis != null) {
                    bis.close();
                    bis = null;
                }
            } catch (IOException ex3) {
                ex3.printStackTrace();
            }
        }
        return baos.toString();
    }

    /**
     * 关闭bw
     */
    public static void close(BufferedWriter bw) {
        try {
            bw.flush();
            bw.close();
            bw = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭os
     */
    public static void close(OutputStream os) {
        try {
            os.flush();
            os.close();
            os = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭is
     */
    public static void close(InputStream is) {
        try {
            is.close();
            is = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}