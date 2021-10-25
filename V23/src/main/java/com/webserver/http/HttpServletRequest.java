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
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
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
    private String requestURI;//保存URI中的请求部分，“？”左侧内容
    private String queryString;//保存URI的参数部分，“？”右侧内容
    private Map<String,String> parameters = new HashMap<>();//保存每一组参数，key：参数名 value：参数值

    //消息头相关信息
    private Map<String,String> headers = new HashMap<>();

    //消息正文相关信息

    private Socket socket;

    public HttpServletRequest(Socket socket) throws IOException, EmptyRequestException {
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
    private void parseRequestLine() throws IOException, EmptyRequestException {
        String line = readLine();
        System.out.println("请求行:" + line);
        if (line.isEmpty()) {//如果读取请求行的内容是空字符串，则说明本次为空请求
            throw new EmptyRequestException("空请求");
        }
//            method = line.substring(0,line.indexOf(' '));
//            uri = line.substring(line.indexOf(' ')+1,line.lastIndexOf(' '));
//            protocol = line.substring(line.lastIndexOf(' ')+1);

        String[] data = line.split("\\s");
        method = data[0];
        uri = data[1];
        protocol = data[2];

        parseUri();//进一步解析uri
        System.out.println("method:" + method);
        System.out.println("uri:" + uri);
        System.out.println("protocol:" + protocol);

    }

    /**
     * 进一步解析uri
     */
    private void parseUri() {
        /*
            uri是有两种情况的:有参数和无参数
            无参数情况:
            /myweb/index.html

            有参数情况:
            /myweb/reg?username=fancq&password=123456&nickname=chuanqi&age=22

            解析过程:
            1:如果uri中没有参数，则直接将uri赋值给requestURI
            2:如果uri中含有参数:
              2.1:将请求部分(uri中"?"左侧内容)赋值给requestURI
              2.2:将参数部分(uri中"?"右侧内容)赋值给queryString
              2.3:再将参数部分进行拆分得到每一组参数
                  2.3.1:将参数部分按照"&"拆分出每一组参数
                  2.3.2:再将每一组参数按照"="拆分为参数名和参数值
                  2.3.3:将参数名作为key,参数值作为value保存到parameters这个Map。
         */

        String[] data = uri.split("\\?");
        requestURI = data[0];
        if(data.length>1) {
            //System.out.println("hai"+data[0]+" "+data[1]);
            queryString = data[1];
            parseParameters(queryString);
        }
        System.out.println("requestURI:" + requestURI);
        System.out.println("queryString:" + queryString);
        System.out.println("parameters:" + parameters);
    }

    private void parseParameters(String line){
        String[] parseArray = line.split("&");
        for (String para : parseArray) {
            String[] arr = para.split("=");
            if(arr.length>1) {
                parameters.put(arr[0], arr[1]);
            }else{
                parameters.put(arr[0],null);
            }
        }
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
        while (!(line = readLine()).isEmpty()) {
            System.out.println("消息头：" + line);
            //拆分消息头，分别得到消息头的名字和值
//                String host = line.substring(0,line.indexOf(' '));
//                String zhi = line.substring(line.indexOf(' ')+1);
//                System.out.println("头"+host);
//                System.out.println("值："+zhi);

            String[] data = line.split(":\\s");
            headers.put(data[0],data[1]);
        }
        System.out.println("headers:" + headers);
    }

    /**
     * 解析消息正文
     */
    private void parseContent() {
        //根据消息头Content-Length来判定是否存在消息正文
        System.out.println("........");
            if (headers.containsKey("Content-Length")) {
                //获取Content-Length头的值得到正文长度
                int len = Integer.parseInt(headers.get("Content-Length"));
                System.out.println(len);
                byte[] data = new byte[len];
                try {
                    InputStream in = socket.getInputStream();
                    in.read(data);//将正文数据读入data数组
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //根据Content-Type判断正文类型来进行对应的正文解析工作
                String contentType = headers.get("Content-Type");
                //仅判断是否为form表单提交的不含附件的数据
                if ("application/x-www-form-urlencoded".equals(contentType)) {
                    try {
                        String line = new String(data, "ISO8859-1");
                        System.out.println("正文内容:" + line);
                        parseParameters(line);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }//else if(){} 可以继续判断其他正文类型做其他解析
        }
    }

    /**
     * 通过socket获取输入流读取客户端发送过来的一行字符串(以CRLF结尾)
     * 该方法在解析请求的环节被复用，通常复用的代码无需处理异常，都是将其抛给调用者解决。
     *
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
        char cur = 'a';//表示本次读取到的字符
        char pre = 'a';//表示上次读取到的字符
        while ((d = in.read()) != -1) {
            cur = (char) d;
            /*
                由于上面使用了静态导入，因此使用HttpContext中的静态属性CR，LF时
                无需再写成HttpContext.CR这种形式了，可直接使用CR
             */
            if (pre == CR && cur == LF) {//上次为回车，本次为换行就停止读取
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
     *
     * @param name
     * @return
     */
    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }
    public String getParameter(String name){
        String value = parameters.get(name);
        if(value != null) {
            //将value转码，将%XX这样的内容按照UTF-8编码还原对应文字后返回
            try {
                value = URLDecoder.decode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
