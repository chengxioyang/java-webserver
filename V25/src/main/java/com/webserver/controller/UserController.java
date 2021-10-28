package com.webserver.controller;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.vo.User;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理与用户数据相关的操作
 */
@Controller
public class UserController {
    //传入的参数为当前类的类对象即可
    private static Logger log = Logger.getLogger(UserController.class);
    //所有用户信息所保存的目录
    public static final String USER_DIR = "./users/";

    static {
        File dir = new File(USER_DIR);
        if (!dir.exists()) {//如果该目录不存在则自动创建
            dir.mkdir();
        }
    }

    /**
     * 处理用户注册操作
     *
     * @param request  请求对象
     * @param response 响应对象
     */
    @RequestMapping("/myweb/reg")
    public void reg(HttpServletRequest request, HttpServletResponse response) {
        log.info("开始处理用户注册...");
        /*
            1.通过request对象获取reg.html上表单提交的用户数据
            2.将该用户信息保存
            3.设置response对象给用户响应注册结果页面
         */
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username + "," + password + "," + nickname + "," + ageStr);
        /*
            必要的数据验证：
            要求用户名，密码，昵称，年龄不能为空，并且年龄这个字符串表示的必须是一个整数
            否则直接向response中设置一个错误页面：reg_info_err.html
            该页面居中显示一行字：注册信息输入有误，请重新注册

         */

        if (username == null || password == null || nickname == null || ageStr == null || !ageStr.matches("[0-9]+")) {
            File file = new File("./webapps/myweb/reg_info_err.html");
            response.setEntity(file);
            return;
        }
        File userfile = new File(USER_DIR + username + ".obj");
        if (userfile.exists()) {
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
//            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        log.info("用户注册处理完毕...");

    }
    @RequestMapping("/myweb/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("用户开始登录...");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null) {
            File file = new File("./webapps/myweb/login_error.html");
            response.setEntity(file);
            return;
        }
        File userFile = new File(USER_DIR + username + ".obj");
        if (userFile.exists()) {
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
//                e.printStackTrace();
                log.error(e.getMessage(),e);
            }
        }
        System.out.println("用户登录完毕...");
        File file = new File("./webapps/myweb/login_fail.html");
        response.setEntity(file);
    }

    /**
     * 生成显示所有用户信息的动态页面
     *
     * @param request
     * @param response
     */
    @RequestMapping("/myweb/showAllUser")
    public void showAllUser(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("开始处理动态页面");
        //1 将users目录中的所有用户读取回来
        List<User> userList = new ArrayList<>();
        //  扫描users目录下的所有.obj文件，并将每个文件中的User对象反序列化回来后存入userList
        File dir = new File(USER_DIR);
        File[] subs = dir.listFiles(f -> f.getName().endsWith(".obj"));

        for (File userFile : subs) {
            try (
                    FileInputStream fis = new FileInputStream(userFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
            ) {
                User user = (User) ois.readObject();
                userList.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(userList);


        //2 将所有用户拼接到HTML代码中并生成页面
        PrintWriter pw = response.getWriter();
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"en\">");
        pw.println("<head>");
        pw.println("<meta charset=\"UTF-8\">");
        pw.println("<title>Title</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<center>");
        pw.println("<h1>用户列表</h1>");
        pw.println("<table border=\"1\">");
        pw.println("<tr>");
        pw.println("<td>用户名</td>");
        pw.println("<td>密码</td>");
        pw.println("<td>昵称</td>");
        pw.println("<td>年龄</td>");
        pw.println("</tr>");
        for (User user : userList) {
            pw.println("<tr>");
            pw.println("<td>" + user.getUsername() + "</td>");
            pw.println("<td>" + user.getPassword() + "</td>");
            pw.println("<td>" + user.getNickname() + "</td>");
            pw.println("<td>" + user.getAge() + "</td>");
            pw.println("</tr>");
        }


        pw.println("</table>");
        pw.println("</center>");
        pw.println("</body>");
        pw.println("</html>");


        //3 设置响应头Content-Type，告知浏览器这些动态数据是一个页面
        response.sendContentType("text/html");


        System.out.println("动态页面生成完毕！");
    }
}
