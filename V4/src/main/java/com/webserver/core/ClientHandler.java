package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
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
            System.out.println("请求路径："+request.getUri());


            //3发送响应

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
