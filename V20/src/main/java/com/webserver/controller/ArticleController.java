package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import com.webserver.vo.Article;

import java.io.*;

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
        File titleFile = new File(TITLE_DIR+title+".obj");
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
}
