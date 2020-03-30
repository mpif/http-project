/**
 * Copyright (C), 2015-2020, 京东
 * FileName: HttpClientSimulator
 * Author:   caishengzhi
 * Date:     2020/3/24 21:59
 * Description: http客户端模拟器
 */
package com.http.client;


import com.http.HttpBase;
import com.http.util.CollectionUtil;
import com.http.util.IoUtil;
import com.http.util.StringUtil;
import com.sun.org.apache.xml.internal.serialize.LineSeparator;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 *
 * http客户端模拟器
 *
 * @author: caishengzhi
 * @Date: 2020/03/24 21:59
 * @since: 1.0.0
 */
public class HttpClientSimulator extends HttpBase {

    /**
     * 创建只有一个线程的线程池
     */
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * 模拟http get请求
     *
     * @Author caishengzhi
     * @param urlStr
     * @Date 2020/3/26 12:47
     * @return java.lang.String
     */
    public String get(String urlStr) throws IOException {
        String response = "";

        String host = StringUtil.getHost(urlStr);
        int port = StringUtil.getPort(urlStr);
        String requestContent = HttpContentBuilder.buildBasicGet(urlStr);
        System.out.println("requestContent:");
        System.out.println(requestContent);

        Socket socket = new Socket(host, port);
        OutputStream os = socket.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.write(requestContent);
        bw.flush();

        InputStream is = socket.getInputStream();
        response = IoUtil.getString(is);

        IoUtil.close(is);

//        Future<String> future = null;
//        long start = System.currentTimeMillis();
//        if(!socket.isInputShutdown()) {
//            InputStream is = socket.getInputStream();
//            Callable callable = new Callable() {
//                @Override
//                public Object call() throws Exception {
//                    String response = IoUtil.getString(is);
//                    IoUtil.close(is);
//                    return response;
//                }
//            };
//            future = executorService.submit(callable);
//        }
//        try {
//            String result = future.get();
//            System.out.println("result:");
//            System.out.println(result);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("read response cost:[" + (end - start)/1000 + "s]");

        IoUtil.close(bw);

        return response;
    }

    /**
     * 模拟http post请求
     *
     * @Author caishengzhi
     * @param urlStr
     * @Date 2020/3/26 12:47
     * @return java.lang.String
     */
    public String post(String urlStr, Map<String, String> paramMap) throws IOException {
        String response = "";

        String host = StringUtil.getHost(urlStr);
        int port = StringUtil.getPort(urlStr);
        String requestStr = "";
        if(paramMap == null) {
            requestStr = HttpContentBuilder.buildUrlPost(urlStr, paramMap);
        } else {
            requestStr = HttpContentBuilder.buildBodyPost(urlStr, paramMap);
        }
        System.out.println("requestStr:");
        System.out.println(requestStr);

        Socket socket = new Socket(host, port);
//        socket.setKeepAlive(true);
//        socket.setSoTimeout(10000);
        socket.setTcpNoDelay(true);
        OutputStream os = socket.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        bw.write(requestStr);
        bw.flush();

        Future<String> future = null;

        if(!socket.isInputShutdown()) {
            InputStream is = socket.getInputStream();

            Callable callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    String response = IoUtil.getString(is);
                    IoUtil.close(is);
                    return response;
                }
            };

            future = executorService.submit(callable);

        }

        try {
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        IoUtil.close(bw);

        return response;
    }

    /**
     * 模拟http post请求
     *
     * @Author caishengzhi
     * @param urlStr
     * @Date 2020/3/26 12:47
     * @return java.lang.String
     */
    public String postFile(String urlStr, List<String> fileList) throws IOException {
        String response = "";

        String host = StringUtil.getHost(urlStr);
        int port = StringUtil.getPort(urlStr);
        String addr = StringUtil.getAddress(urlStr);

        String requestStr = "";


        Socket socket = new Socket(host, port);
        socket.setKeepAlive(true);
        socket.setSoTimeout(5000);
//        socket.setTcpNoDelay(true);
        OutputStream os = socket.getOutputStream();


        OutputStream out = new DataOutputStream(os);
        StringBuilder builder = create().append(POST).append(BLANK).append(addr).append(BLANK).append(HTTP_11).append("\r\n")
                .append("Host:").append(addr).append("\r\n")
                .append("Connection: Keep-Alive").append("\r\n")
//                .append("Connection: close").append("\r\n")
                .append("Content-Type: ").append("multipart/form-data; boundary=").append(BOUNDARY).append("\r\n")
                .append("\r\n")
                ;

        requestStr = builder.toString();
        System.out.println("requestStr:");
        System.out.print(requestStr);

        out.write(requestStr.getBytes());

        if(CollectionUtil.isNotEmpty(fileList)) {
            StringBuilder sb;
            String filePath;
            File file = null;
            for(int i = 0; i < fileList.size(); i ++) {
                filePath = fileList.get(i);
                sb = create();
                file = new File(filePath);
                DataInputStream in = new DataInputStream(new FileInputStream(file));
                try {
                    /**
                     * 写分隔符--${boundary}，并回车换行
                     */
                    String boundaryStr = BOUNDARY_PREFIX + BOUNDARY + LINE_SEPERATOR;
                    sb.append(boundaryStr);
                    sb.append("Content-Disposition: form-data; name=\"file\"").append("; filename=\"").append(file.getName()).append("\"").append(LINE_SEPERATOR);
//                    sb.append("Content-Type: application/octet-stream").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
                    sb.append("Content-Type: image/png").append(LINE_SEPERATOR);
                    sb.append("Content-Transfer-Encoding: binary").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
                    out.write(sb.toString().getBytes());

                    System.out.print(sb.toString());

                    byte[] bufferOut = new byte[1024];
                    int len = 0;
                    // 每次读1KB数据,并且将文件数据写入到输出流中
                    while ((len = in.read(bufferOut)) != -1) {
                        out.write(bufferOut, 0, len);
                    }

                    //回车换行
                    out.write(LINE_SEPERATOR.getBytes());

                    System.out.print(LINE_SEPERATOR);

                } catch (Exception e) {
                    System.out.println("写文件类型的表单参数异常" + e);
                } finally {
                    IoUtil.close(in);
                }

            }
        }
        String endLine = HttpContentBuilder.create().append(BOUNDARY_PREFIX).append(BOUNDARY).append(BOUNDARY_SUFFIX).append(LINE_SEPERATOR).toString();
        out.write(endLine.getBytes());
        System.out.print(endLine);

        out.flush();

        Future<String> future = null;

        if(!socket.isInputShutdown()) {
            InputStream is = socket.getInputStream();

            Callable callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    String response = IoUtil.getString(is);
                    IoUtil.close(is);
                    return response;
                }
            };

            future = executorService.submit(callable);

        }

        try {
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


//        IoUtil.close(bw);
        IoUtil.close(out);

        return response;
    }

    /**
     * 模拟http post请求
     *
     * @Author caishengzhi
     * @param urlStr
     * @Date 2020/3/26 12:47
     * @return java.lang.String
     */
    public String postAttachment(String urlStr, String filePath) throws IOException {
        String response = "";

        String host = StringUtil.getHost(urlStr);
        int port = StringUtil.getPort(urlStr);
        String uri = StringUtil.getPathAndQuery(urlStr);
        String addr = StringUtil.getAddress(urlStr);

        String requestStr = "";

        Socket socket = new Socket(host, port);
        OutputStream os = socket.getOutputStream();
        OutputStream out = new DataOutputStream(os);

        File file = new File(filePath);

        StringBuilder builder = create();
        builder.append(POST).append(BLANK).append(uri).append(BLANK).append(HTTP_11).append(LINE_SEPERATOR);
        builder.append(HOST).append(addr).append(LINE_SEPERATOR);
//        builder.append("Content-Length:96381").append(LINE_SEPERATOR);
        builder.append("Content-Length:90232").append(LINE_SEPERATOR);
        builder.append("Content-Type: multipart/form-data;boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
//        builder.append(BOUNDARY_PREFIX).append("----WebKitFormBoundary7MA4YWxkTrZu0gW").append(LINE_SEPERATOR);
//        builder.append("Content-Disposition: form-data;name=\"fileType\"").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
//        builder.append("valueOfFileType").append(LINE_SEPERATOR);
//        builder.append(BOUNDARY_PREFIX).append("----WebKitFormBoundary7MA4YWxkTrZu0gW").append(LINE_SEPERATOR);
//        builder.append("Content-Disposition: form-data;name=\"fileCount\"").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
//        builder.append("valueOfFileCount_1000").append(LINE_SEPERATOR);
        builder.append(BOUNDARY_PREFIX).append("----WebKitFormBoundary7MA4YWxkTrZu0gW").append(LINE_SEPERATOR);
        builder.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"").append(LINE_SEPERATOR);
        builder.append("Content-Type: application/octet-stream").append(LINE_SEPERATOR).append(LINE_SEPERATOR);

        requestStr = builder.toString();
        System.out.println("requestStr:");
        System.out.print(requestStr);

        out.write(requestStr.getBytes());

        DataInputStream in = new DataInputStream(new FileInputStream(file));
        try {

            byte[] bufferOut = new byte[1024];
            int len = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((len = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, len);
            }
            //回车换行
            out.write(LINE_SEPERATOR.getBytes());
            out.write(LINE_SEPERATOR.getBytes());
            System.out.print(LINE_SEPERATOR);
            System.out.print(LINE_SEPERATOR);
        } catch (Exception e) {
            System.out.println("写文件类型的表单参数异常" + e);
        } finally {
            IoUtil.close(in);
        }

        String endLine = "------WebKitFormBoundary7MA4YWxkTrZu0gW--" + LINE_SEPERATOR;
        System.out.print(endLine);

        out.write(endLine.getBytes());
        out.flush();

        InputStream is = socket.getInputStream();
        response = IoUtil.getString(is);

        IoUtil.close(is);
        IoUtil.close(out);

        return response;
    }

    /**
     * 模拟http post请求
     *
     * @Author caishengzhi
     * @param urlStr
     * @Date 2020/3/26 12:47
     * @return java.lang.String
     */
    public String postParamAndFile(String urlStr, Map<String, String> paramMap, List<String> fileList) throws IOException {
        String response = "";

        String host = StringUtil.getHost(urlStr);
        int port = StringUtil.getPort(urlStr);
        String uri = StringUtil.getPathAndQuery(urlStr);
        String addr = StringUtil.getAddress(urlStr);

        Socket socket = new Socket(host, port);
        OutputStream os = socket.getOutputStream();
        OutputStream out = new DataOutputStream(os);

        StringBuilder builder = create();
        builder.append(POST).append(BLANK).append(uri).append(BLANK).append(HTTP_11).append(LINE_SEPERATOR);
        builder.append(HOST).append(addr).append(LINE_SEPERATOR);
//        long contentLength = 0L;
        long dataLength = getTotalSize(paramMap, fileList);
        long contentLength = (long) (dataLength * (1 + 0.2));
        System.out.println("dataLength=" + dataLength + ", contentLength: " + contentLength);
        builder.append("Content-Length:").append(contentLength).append(LINE_SEPERATOR);
//        builder.append("Content-Length:310233").append(LINE_SEPERATOR);
        builder.append("Content-Type: multipart/form-data;boundary=").append(BOUNDARY).append(LINE_SEPERATOR).append(LINE_SEPERATOR);

        if(CollectionUtil.isNotEmpty(paramMap)) {
            Iterator<String> iter = paramMap.keySet().iterator();
            String key = "";
            String val = "";
            while(iter.hasNext()) {
                key = iter.next();
                val = paramMap.get(key);
                builder.append(BOUNDARY_PREFIX).append(BOUNDARY).append(LINE_SEPERATOR);
                builder.append("Content-Disposition: form-data;name=\"" + key + "\"").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
                builder.append(val).append(LINE_SEPERATOR);
            }
            out.write(builder.toString().getBytes());
            System.out.print(builder.toString());
            contentLength += builder.toString().getBytes().length;
        }

        if(CollectionUtil.isNotEmpty(fileList)) {
            File file = null;
            String filePath = "";
            for(int i = 0; i < fileList.size(); i ++) {
                filePath = fileList.get(i);
                file = new File(filePath);
                builder = create();
                builder.append(BOUNDARY_PREFIX).append(BOUNDARY).append(LINE_SEPERATOR);
                builder.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"").append(LINE_SEPERATOR);
                builder.append("Content-Type: application/octet-stream").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
//                builder.append("Content-Type: image/png").append(LINE_SEPERATOR).append(LINE_SEPERATOR);
                out.write(builder.toString().getBytes());
                System.out.print(builder.toString());
                contentLength += builder.toString().getBytes().length;

                writeTo(out, filePath);

                // 数据输入流,用于读取文件数据
//                DataInputStream in = new DataInputStream(new FileInputStream(file));
//                byte[] bufferOut = new byte[1024];
//                int bytes;
//                // 每次读1KB数据,并且将文件数据写入到输出流中
//                while ((bytes = in.read(bufferOut)) != -1) {
//                    out.write(bufferOut, 0, bytes);
//                    contentLength += bytes;
//                }
//                // 最后添加换行
//                out.write(LINE_SEPERATOR.getBytes());
//                System.out.print(LINE_SEPERATOR);

                contentLength += LINE_SEPERATOR.getBytes().length;

//                IoUtil.close(in);

            }
        }

        builder = create();
        builder.append(LINE_SEPERATOR).append(BOUNDARY_PREFIX).append(BOUNDARY).append(BOUNDARY_SUFFIX).append(LINE_SEPERATOR);
        out.write(builder.toString().getBytes());
        System.out.print(builder.toString());

        out.flush();

        InputStream is = socket.getInputStream();
        response = IoUtil.getString(is);

        IoUtil.close(is);
        IoUtil.close(out);

        return response;
    }

    public static void writeTo(OutputStream out, String filePath) throws IOException {

        File file = new File(filePath);
        if(!file.exists()) {
            System.out.println("[" + filePath + "] not exists!");
            return;
        }

        // 数据输入流,用于读取文件数据
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        byte[] bufferOut = new byte[1024];
        int bytes = 0;
        // 每次读1KB数据,并且将文件数据写入到输出流中
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        // 最后添加换行
        out.write(LINE_SEPERATOR.getBytes());
        in.close();

    }

    public String getResponseContent(InputStream is) {
        String response = "";
        Future<String> future = null;
        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                String response = IoUtil.getString(is);
                IoUtil.close(is);
                return response;
            }
        };
        future = executorService.submit(callable);

        try {
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return response;
    }

}