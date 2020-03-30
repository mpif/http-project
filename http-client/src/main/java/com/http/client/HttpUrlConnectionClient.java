/**
 * Copyright (C), 2015-2020, 京东
 * FileName: HttpUrlConnectionClient
 * Author:   caishengzhi
 * Date:     2020/3/29 6:56
 * Description: 用HttpUrlConnection模拟http客户端
 */
package com.http.client;


import com.http.util.CollectionUtil;
import com.http.util.IoUtil;
import com.http.util.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * 用HttpUrlConnection模拟http客户端
 *
 * @author: caishengzhi
 * @Date: 2020/03/29 06:56
 * @since: 1.0.0
 */
public class HttpUrlConnectionClient {

    // 换行符
    final String newLine = "\r\n";
    final String boundaryPrefix = "--";
    // 定义数据分隔线
    String BOUNDARY = "========7d4a6d158c9";

    public void uploadFile(String urlStr, String fileName) {

        InputStream is = null;

        try {

            // 服务器的域名
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());
            // 上传文件
            File file = new File(fileName);
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName()
                    + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);
            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());
            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(new FileInputStream(file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();
            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            is = conn.getInputStream();
            String responseStr = readResponseContent(is);
            System.out.println(responseStr);

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            IoUtil.close(is);
        }

    }

    public void uploadParamAndFile(String urlStr, String paramKey, String paramVal, String fileName) {

        InputStream is = null;
        try {

            // 服务器的域名
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());

            String paramContent = generatorKeyAndValue(paramKey, paramVal);
            out.write(paramContent.getBytes());

            if(StringUtil.isNotBlank(fileName)) {
                // 上传文件
                File file = new File(fileName);
                StringBuilder sb = new StringBuilder();
                sb.append(boundaryPrefix);
                sb.append(BOUNDARY);
                sb.append(newLine);
                // 文件参数,photo参数名可以随意修改
                sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName()
                        + "\"" + newLine);
                sb.append("Content-Type:application/octet-stream");
                // 参数头设置完以后需要两个换行，然后才是参数内容
                sb.append(newLine);
                sb.append(newLine);
                // 将参数头的数据写入到输出流中
                out.write(sb.toString().getBytes());
                // 数据输入流,用于读取文件数据
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                byte[] bufferOut = new byte[1024];
                int bytes = 0;
                // 每次读1KB数据,并且将文件数据写入到输出流中
                while ((bytes = in.read(bufferOut)) != -1) {
                    out.write(bufferOut, 0, bytes);
                }
                // 最后添加换行
                out.write(newLine.getBytes());
                in.close();
            } else {
                System.out.println("fileName is null");
            }

            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            is = conn.getInputStream();
            String responseStr = readResponseContent(is);
            System.out.println(responseStr);

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            IoUtil.close(is);
        }
    }

    public void uploadMultiParamAndFile(String urlStr, Map<String, String> paramMap, List<String> fileList) {

        InputStream is = null;
        try {

            // 服务器的域名
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            OutputStream out = new DataOutputStream(conn.getOutputStream());

            if(CollectionUtil.isNotEmpty(paramMap)) {
                Iterator<String> iter = paramMap.keySet().iterator();
                String key = "";
                while(iter.hasNext()) {
                    key = iter.next();
                    String paramContent = generatorKeyAndValue(key, paramMap.get(key));
                    out.write(paramContent.getBytes());
                    System.out.print(paramContent);
                }
            }


            if(CollectionUtil.isNotEmpty(fileList)) {
                String filePath = "";
                for(int i = 0; i < fileList.size(); i ++) {
                    filePath = fileList.get(i);

                    // 上传文件
                    File file = new File(filePath);
                    StringBuilder sb = new StringBuilder();
                    sb.append(boundaryPrefix);
                    sb.append(BOUNDARY);
                    sb.append(newLine);
                    // 文件参数,photo参数名可以随意修改
                    sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName()
                            + "\"" + newLine);
                    sb.append("Content-Type:application/octet-stream");
                    // 参数头设置完以后需要两个换行，然后才是参数内容
                    sb.append(newLine);
                    sb.append(newLine);
                    // 将参数头的数据写入到输出流中
                    out.write(sb.toString().getBytes());
                    System.out.print(sb.toString());

                    // 数据输入流,用于读取文件数据
                    DataInputStream in = new DataInputStream(new FileInputStream(file));
                    byte[] bufferOut = new byte[1024];
                    int bytes = 0;
                    // 每次读1KB数据,并且将文件数据写入到输出流中
                    while ((bytes = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, bytes);
                    }
                    // 最后添加换行
                    out.write(newLine.getBytes());
                    System.out.print(newLine);
                    in.close();
                }
            } else {
                System.out.println("fileList is null");
            }

            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            System.out.print(newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine);

            out.flush();
            out.close();

            is = conn.getInputStream();
            String responseStr = readResponseContent(is);
            System.out.println(responseStr);

        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            IoUtil.close(is);
        }
    }

    /**
     * 上传参数
     * --OCqxMF6-JxtxoMDHmoG5W5eY9MGRsTBp
     * Content-Disposition: form-data; name="lng"
     *
     * 116.361545
     */
    public String generatorKeyAndValue(String key, String value) {
        StringBuilder paramSb = new StringBuilder();
        paramSb.append(boundaryPrefix);
        paramSb.append(BOUNDARY);
        paramSb.append(newLine);//fileCount
        paramSb.append("Content-Disposition: form-data; name=\"" + key + "\"").append(newLine);
        paramSb.append(newLine);
        paramSb.append(value);
        paramSb.append(newLine);
        return paramSb.toString();
    }

    public String readResponseContent(InputStream is) throws IOException {
        // 定义BufferedReader输入流来读取URL的响应
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}