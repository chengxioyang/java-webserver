package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.vo.Article;
import com.webserver.vo.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ArticleController {
    public static final String TITLE_DIR = "./articles/";

    static {
        File file = new File(TITLE_DIR);
        if(!file.exists()){
            file.mkdir();
        }
    }
    public void writeArticle(HttpServletRequest request, HttpServletResponse response){
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String content = request.getParameter("content");
        if(title==null || content==null){
            File file = new File("./webapps/myweb/article_fail.html");
            response.setEntity(file);
            return;
        }
        File titleFile = new File(TITLE_DIR+title+".txt");
        Article article = new Article(title,author,content);
        try (
                FileOutputStream fos = new FileOutputStream(titleFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ){
            oos.writeObject(article);
            File file = new File("./webapps/myweb/article_success.html");
            response.setEntity(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("发表成功");
    }
    public void showAllArticle(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理动态文章...");
        //将articles目录中的所有用户读取回来
        List<Article> articleList = new ArrayList<>();
        File dir = new File(TITLE_DIR);
        File[] subs = dir.listFiles(f -> f.getName().endsWith(".obj"));
        for(File titleFile : subs){
            try (
                    FileInputStream fis = new FileInputStream(titleFile);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    ){
                Article article = (Article) ois.readObject();
                articleList.add(article);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(articleList);

        //将所有文章拼接到HTML代码中并生成页面
        PrintWriter pw = response.getWriter();
        pw.println("<!DOCTYPE html>");
        pw.println("<html lang=\"en\">");
        pw.println("<head>");
        pw.println("<meta charset=\"UTF-8\">");
        pw.println("<title>Title</title>");
        pw.println("</head>");
        pw.println("<body>");
        pw.println("<center>");
        pw.println("<h1>文章列表</h1>");
        pw.println("<table border=\"1\">");
        pw.println("<tr>");
        pw.println("<td>标题</td>");
        pw.println("<td>作者</td>");
        //pw.println("<td>文章正文</td>");
        pw.println("</tr>");
        for (Article article : articleList) {
            pw.println("<tr>");
            pw.println("<td><a href=\"/myweb/showAllArticle?title="+article.getTitle()+"\">"+article.getTitle()+"</a></td>");
            pw.println("<td>" + article.getAuthor() + "</td>");
            //pw.println("<td>" + article.getContent() + "</td>");
            pw.println("</tr>");
        }


        pw.println("</table>");
        pw.println("</center>");
        pw.println("</body>");
        pw.println("</html>");



        //设置响应头Content-Type，告知浏览器这些动态数据是一个页面
        response.sendContentType("text/html");
        pw = response.getWriter();

        System.out.println("动态文章处理完毕");
    }
}
