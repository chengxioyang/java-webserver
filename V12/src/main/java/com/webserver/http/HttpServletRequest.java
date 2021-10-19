package com.webserver.http;
/**
 * 静态导入
 * 语法：
 * import static 包名.类名.静态成员(静态属性，静态方法)
 */

import static com.webserver.http.HttpContext.CR;
import static com.webserver.http.HttpContext.LF;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpServletRequest是JAVA EE标准定义的一个类
 * 该实例用于表示客户端发送过来的一个HTTP请求内容
 */
public class HttpServletRequest {
    //请求行的相关信息
    private String method;//请求方式
    private String uri;//抽象路径部分
    private String protocol;//协议版本

    //消息头相关信息
    private Map<String,String> headers = new HashMap<>();

    //消息正文相关信息

    private Socket socket;
    public HttpServletRequest(Socket socket) throws IOException {
        this.socket = socket;

        //1.1解析请求行
        parseRequestLine();

        //1.2解析消息头
        parseHeaders();

        //1.2解析消息正文
        parseContent();

    }

    /**
     * 解析请求行
     */
    private void parseRequestLine() throws IOException {
        String line = readLine();
        System.out.println("请求行:"+line);

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
    }

    /**
     * 解析消息头
     */
    private void parseHeaders() throws IOException {
            /*
                String提供的方法
                boolean isEmpty()
                判断当前字符串是否为空字符串
             */
        //循环读取若干行（若干消息头），若单独返回CRLF则返回值为空串，那么就停止循环
        String line;
        while (!(line = readLine()).isEmpty()){
            System.out.println("消息头："+line);
            //拆分消息头，分别得到消息头的名字和值
//                String host = line.substring(0,line.indexOf(' '));
//                String zhi = line.substring(line.indexOf(' ')+1);
//                System.out.println("头"+host);
//                System.out.println("值："+zhi);

            String[] data = line.split("\\s");
            headers.put(data[0],data[1]);
        }
        System.out.println("headers:"+headers);
    }

    /**
     * 解析消息正文
     */
    private void parseContent(){}

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
            /*
                由于上面使用了静态导入，因此使用HttpContext中的静态属性CR，LF时
                无需再写成HttpContext.CR这种形式了，可直接使用CR
             */
            if(pre==CR && cur==LF){//上次为回车，本次为换行就停止读取
                break;
            }
            builder.append(cur);
            pre = cur;
        }
        return builder.toString().trim();
    }


    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getProtocol() {
        return protocol;
    }

    /**
     * 获取给定消息头对应的值
     * @param name
     * @return
     */
    public String getHeader(String name){
        return headers.get(name);
    }
}
