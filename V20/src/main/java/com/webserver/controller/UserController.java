package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.vo.User;

import java.io.*;

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
        /*
            必要的数据验证：
            要求用户名，密码，昵称，年龄不能为空，并且年龄这个字符串表示的必须是一个整数
            否则直接向response中设置一个错误页面：reg_info_err.html
            该页面居中显示一行字：注册信息输入有误，请重新注册

         */

        if(username==null || password==null || nickname==null || ageStr==null || !ageStr.matches("[0-9]+")) {
            File file = new File("./webapps/myweb/reg_info_err.html");
            response.setEntity(file);
            return;
        }
        File userfile = new File(USER_DIR+username + ".obj");
        if(userfile.exists()){
            File file = new File("./webapps/myweb/reg_have_user.html");
            response.setEntity(file);
            return;
        }

        /*
            检测是否为重复用户，若该用户存在则直接响应页面：reg_have_user.html
            该页面居中显示一行字：该用户已存在，请重新注册
         */
            int age = Integer.parseInt(ageStr);
        /*

            2
            将该用户信息取名为：用户名.obj写入到该文件中
            将用户信息以User对象形式表示，并将其序列化到文件里.
         */
            User user = new User(username, password, nickname, age);
            try (
                    FileOutputStream fos = new FileOutputStream(userfile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
            ) {

                oos.writeObject(user);

                //3设置响应对象发送注册成功页面
                File file = new File("./webapps/myweb/reg_success.html");
                response.setEntity(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        System.out.println("用户注册处理完毕...");

    }
    public void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("用户开始登录...");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username==null || password==null){
            File file = new File("./webapps/myweb/login_error.html");
            response.setEntity(file);
            return;
        }
        File userFile = new File(USER_DIR+username+".obj");
        if(userFile.exists()) {
            try (
                    FileInputStream fis = new FileInputStream(userFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                User u = (User) ois.readObject();
                System.out.println(u);

                if (password.equals(u.getPassword())) {
                    File file = new File("./webapps/myweb/login_success.html");
                    response.setEntity(file);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("用户登录完毕...");
        File file = new File("./webapps/myweb/login_fail.html");
        response.setEntity(file);
    }
}
