package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 线程任务，负责与指定的客户端进行HTTP交互
 * HTTP协议要求客户端与服务端的一次交互采取"一问一答"。因此这里处理交互分为三步完成:
 * 1:解析请求
 * 2:处理请求
 * 3:响应客户端
 */
public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    public void run() {
        try{
            //1解析请求
            HttpServletRequest request = new HttpServletRequest(socket);



            //2处理请求
            String path = request.getUri();
            System.out.println("请求路径："+path);


            //3发送响应
            File file = new File("./webapps" + path);
            /*
                http://localhost:8088/myweb/index.html
                path="/myweb/index.html"

                http://localhost:8088/myweb/classTable.html
                path="/myweb/classTable.html"


                http://localhost:8088/
                path="/"
                下面执行报错:
                java.io.FileNotFoundException: .\webapps (拒绝访问。)
                原因:抽象路径是"\"等同于定位的是webapps目录，目录是不能被文件流读取的


                http://localhost:8088/myweb/classTable123.html
                path="/myweb/classTable123.html"
                下面执行报错:
                java.io.FileNotFoundException: .\webapps\myweb\classTable123.html (系统找不到指定的文件。)
                原因:webapps/myweb/下没有classTable123.html这个文件
                    说明浏览器地址栏上写路径的时候输入有错误的地方
             */

            OutputStream out = socket.getOutputStream();

            //3.1发送状态行
            String line = "HTTP/1.1 200 OK";
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//发送了一个回车符CR
            out.write(10);//发送了一个换行符LF

            //3.2发送响应头
            line = "Content-Type: text/html";
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);

            line = "Content-Length: "+file.length();
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);

            //单独发送CRLF表示响应头发送完毕了
            out.write(13);
            out.write(10);

            //3.3发送响应正文
            FileInputStream fis = new FileInputStream(file);
            int len;
            byte[] data = new byte[1024*10];
            while ((len = fis.read(data)) != -1){
                out.write(data,0,len);
            }
            System.out.println("响应发送完毕");


        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                //HTTP协议要求一次交互完毕后要断开TCP连接
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
