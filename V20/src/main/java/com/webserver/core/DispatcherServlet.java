package com.webserver.core;

import com.webserver.controller.ArticleController;
import com.webserver.controller.UserController;
import com.webserver.http.HttpContext;
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
        String path = request.getRequestURI();
        System.out.println("请求路径：" + path);
        UserController controller = new UserController();
        if("/myweb/reg".equals(path)){//该请求为注册页面上form表单提交的请求
            controller.reg(request,response);
        }else if("/myweb/login".equals(path)){//该请求为登录界面上form表单提交的请求
            controller.login(request,response);
        }else if("/myweb/writeArticle".equals(path)){
            ArticleController controller1 = new ArticleController();
            controller1.writeArticle(request,response);

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
