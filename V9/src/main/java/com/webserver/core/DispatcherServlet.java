package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;

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
        } else {
            response.setStatusCode(404);
            response.setStatusReason("NOT Found");
            response.setEntity(new File("./webapps/root/404.html"));
        }
    }
}
