package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * WebServer Web容器
 * Web容器的主要职责
 * 1：管理部署在容器中的所有Webapp(网络应用)，每个应用都包含：网页，素材，逻辑代码等
 * 相当于每个网络容器就是俗称的一个“网站”
 * 2：与客户端(通常是浏览器)建立TCP链接并基于HTTP协议进行交互，使得浏览器可以访问部署在
 * 容器中的某个网络应用
 */
public class WebServer {
    private ServerSocket serverSocket;


    public WebServer(){
        try {
            System.out.println("正在启动服务端");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端启用完毕");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void start(){
        try {
            while (true){
                System.out.println("等待客户端链接");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端链接了");

                //启动一个线程处理该客户端HTTP交互
                ClientHandler handler = new ClientHandler(socket);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        WebServer server = new WebServer();
        server.start();
    }
}
