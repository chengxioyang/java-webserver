package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            //1.解析请求
            InputStream in = socket.getInputStream();
            int d;
            StringBuilder builder = new StringBuilder();
            char cur = 'a';//表示本次读取到的字符
            char pre = 'a';//表示上次读取到的字符
            while ((d = in.read())!= -1){
                cur = (char) d;
                if(pre==13 && cur==10){//上次为回车，本次为换行就停止读取
                    break;
                }
                builder.append(cur);
                pre = cur;
            }
            String line = builder.toString().trim();
            System.out.println(line);

            //请求行的相关信息
            String method;//请求方式
            String uri;//抽象路径部分
            String protocol;//协议版本


//            method = line.substring(0,line.indexOf(' '));
//            uri = line.substring(line.indexOf(' ')+1,line.lastIndexOf(' '));
//            protocol = line.substring(line.lastIndexOf(' ')+1);

            String[] data = line.split("\\s");
            method = data[0];
            /*
                可能出现下标越界，这说明出现了空请求，后续版本会解决的！目前先不管
                出现了就清除浏览器缓存重新访问，或者换个浏览器试试
             */
            uri = data[1];
            protocol = data[2];

            System.out.println(method);
            System.out.println(uri);
            System.out.println(protocol);
            //2.处理请求

            //3.发送响应

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                //HTTP协议要求一次交互完毕后要断开TCP链接
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
