package com.webserver.core;

import com.webserver.controller.ArticleController;
import com.webserver.controller.ToolsController;
import com.webserver.controller.UserController;
import com.webserver.http.HttpContext;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理请求细节
 */
public class DispatcherServlet {

    public void service(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        String path = request.getRequestURI();
        System.out.println("请求路径：" + path);
        HandlerMapping.MethodMapping mapping = HandlerMapping.getMethod(path);
        if(mapping != null){
            mapping.getMethod().invoke(mapping.getController(),request,response);
        } else{
            File file = new File("./webapps" + path);
            if (file.exists() && file.isFile()) {
                response.setEntity(file);//设置正文
            } else {
                response.setStatusCode(404);
                response.setStatusReason("NOT Found");
                file = new File("./webapps/root/404.html");
                response.setEntity(file);
                response.putHeader("Content-Type","text/html");
                response.putHeader("Content-Length",file.length()+"");
            }
        }
        response.putHeader("Server","WebServer");
    }
}
