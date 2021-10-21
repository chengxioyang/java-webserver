package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.vo.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 用于处理与用户数据相关的操作
 */
public class UserController {
    //所有用户信息所保存的目录
    public static final String USER_DIR = "./users/";

    static {
        File dir = new File(USER_DIR);
        if(!dir.exists()){//如果该目录不存在则自动创建
            dir.mkdir();
        }
    }
    /**
     * 处理用户注册操作
     * @param request   请求对象
     * @param response  响应对象
     */
    public void reg(HttpServletRequest request, HttpServletResponse response){
        System.out.println("开始处理用户注册...");
        /*
            1.通过request对象获取reg.html上表单提交的用户数据
            2.将该用户信息保存
            3.设置response对象给用户响应注册结果页面
         */
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);
        int age = Integer.parseInt(ageStr);
        /*

            2
            将该用户信息取名为：用户名.obj写入到该文件中
            将用户信息以User对象形式表示，并将其序列化到文件里.
         */
        User user = new User(username,password,nickname,age);
        try (
                FileOutputStream fos = new FileOutputStream(USER_DIR+username+".obj");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ){
            oos.writeObject(user);
            //3设置响应对象发送注册成功页面
            File file = new File("./webapps/myweb/reg_success.html");
            response.setEntity(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("用户注册处理完毕...");



    }
}
