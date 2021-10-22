package com.webserver.http;
import static com.webserver.http.HttpContext.CR;
import static com.webserver.http.HttpContext.LF;
import javax.xml.bind.PrintConversionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示HTTP的响应内容
 * 每个响应由三部分构成：状态行，响应头，响应正文
 */
public class HttpServletResponse {
    //状态行相关信息
    private int statusCode = 200;//状态代码，默认值200
    private String statusReason = "OK";//状态描述
    private Socket socket;

    //响应头相关信息
    private Map<String,String> headers = new HashMap<>();
    //响应正文相关信息
    private File entity;//正文对应的实体文件

    public HttpServletResponse(Socket socket) {
        this.socket = socket;
    }


    /**
     * 将当前响应对象内容按照表示的HTTP响应格式发送给客户端
     */
    public void response() throws IOException {
        //1发送状态行
        sendStatusLine();
        //2发送响应头
        sendHeaders();
        //3发送响应正文
        sendContent();

    }

    private void sendStatusLine() throws IOException {
        String line = "HTTP/1.1" + " " + statusCode + " " + statusReason;
        println(line);
    }

    private void sendHeaders() throws IOException {
//        //Content-Type: text/html(CRLF)
//        String line = "Content-Type: text/html";
//        println(line);
//        //Content-Length: xxxx(CRLF)
//        line = "Content-Length: " + entity.length();
//        println(line);
        Set<Map.Entry<String,String>> entrySet = headers.entrySet();
        for(Map.Entry<String,String> e : entrySet){
            String name = e.getKey();
            String value = e.getValue();
            String line = name + ": " + value;
            println(line);
        }

        //单独发送CRLF表示响应头发送完毕了
        println("");

    }

    private void sendContent() throws IOException {
        if(entity != null){
            try (
                    FileInputStream fis = new FileInputStream(entity);
            ) {
                OutputStream out = socket.getOutputStream();
                int len;
                byte[] data = new byte[1024 * 10];
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
            }
        }
    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        out.write(line.getBytes("ISO8859-1"));
        out.write(CR);//发送了一个回车符CR
        out.write(LF);//发送了一个换行符LF
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getEntity() {
        return entity;
    }

    /**
     * 设置响应正文对应的实体文件
     * 设置文件的同时会自动根据该文件添加对应的响应头Content-Type和Content-Length
     * @param entity
     */
    public void setEntity(File entity) {
        this.entity = entity;
        String name = entity.getName();
        String type = name.substring(name.lastIndexOf(".")+1);
        String value = HttpContext.getCont(type);
        putHeader("Content-Type",value);
        putHeader("Content-Length",entity.length()+"");
    }

    public void putHeader(String name,String value){
        this.headers.put(name,value);
    }
}
