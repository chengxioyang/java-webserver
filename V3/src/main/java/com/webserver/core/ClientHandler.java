package com.webserver.core;

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

            //1.1解析请求行
            String line = readLine();
            System.out.println("请求行:"+line);
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
                可能出现下标越界，这说明出现了空请求，后续版本会解决的!目前先不管
                出现了就清除浏览器缓存从新访问，或换个浏览器先试试。
             */
            uri = data[1];
            protocol = data[2];

            System.out.println("method:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);

            //1.2解析消息头
            //消息头相关信息
            Map<String,String> headers = new HashMap<>();
            /*
                String提供的方法
                boolean isEmpty()
                判断当前字符串是否为空字符串
             */
            //循环读取若干行（若干消息头），若单独返回CRLF则返回值为空串，那么就停止循环
            while (!(line = readLine()).isEmpty()){
                System.out.println("消息头："+line);
                //拆分消息头，分别得到消息头的名字和值
//                String host = line.substring(0,line.indexOf(' '));
//                String zhi = line.substring(line.indexOf(' ')+1);
//                System.out.println("头"+host);
//                System.out.println("值："+zhi);

                data = line.split("\\s");
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:"+headers);



            //2处理请求

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

    /**
     * 通过socket获取输入流读取客户端发送过来的一行字符串(以CRLF结尾)
     * 该方法在解析请求的环节被复用，通常复用的代码无需处理异常，都是将其抛给调用者解决。
     * @return
     * @throws IOException
     */
    private String readLine() throws IOException {
        /*
            在socket不变的前提下，无论调用多少次getInputStream()方法获取的输入流始终是同一个输入流
         */
        InputStream in = socket.getInputStream();
        int d;
        StringBuilder builder = new StringBuilder();
        char cur='a';//表示本次读取到的字符
        char pre='a';//表示上次读取到的字符
        while((d = in.read())!=-1){
            cur = (char)d;
            if(pre==13 && cur==10){//上次为回车，本次为换行就停止读取
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }

}
