package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理请求细节
 */
public class DispatcherServlet {

    public void service(HttpServletRequest request, HttpServletResponse response){
        String path = request.getUri();
        System.out.println("请求路径：" + path);
        File file = new File("./webapps" + path);

        if (file.exists() && file.isFile()) {
            response.setEntity(file);//设置正文
            String name = file.getName();
            String type = name.substring(name.lastIndexOf(".")+1);
            Map<String,String> cont = new HashMap<>();
            cont.put("html","text/html");
            cont.put("css","text/css");
            cont.put("js","application/javascript");
            cont.put("png","image/png");
            cont.put("jpg","image/jpeg");
            cont.put("gif","image/gif");
            String value = cont.get(type);

            response.putHeader("Content-Type",value);
            response.putHeader("Content-Length",file.length()+"");
        } else {
            response.setStatusCode(404);
            response.setStatusReason("NOT Found");
            file = new File("./webapps/root/404.html");
            response.setEntity(file);
            response.putHeader("Content-Type","text/html");
            response.putHeader("Content-Length",file.length()+"");
        }
        response.putHeader("Server","WebServer");
    }
}
