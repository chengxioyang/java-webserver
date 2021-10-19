package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

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
public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            //1解析请求
            HttpServletRequest request = new HttpServletRequest(socket);
            HttpServletResponse response = new HttpServletResponse(socket);

            //2处理请求
            String path = request.getUri();
            System.out.println("请求路径：" + path);
            File file = new File("./webapps" + path);

            if (file.exists() && file.isFile()) {
                response.setEntity(file);//设置正文
            } else {
                response.setStatusCode(404);
                response.setStatusReason("NOT Found");
                response.setEntity(new File("./webapps/root/404.html"));
            }

            //3发送响应
            response.response();

            System.out.println("响应发送完毕");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //HTTP协议要求一次交互完毕后要断开TCP连接
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
