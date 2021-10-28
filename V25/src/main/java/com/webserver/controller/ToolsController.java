package com.webserver.controller;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import qrcode.QRCodeUtil;

import java.io.FileOutputStream;
@Controller
public class ToolsController {
    @RequestMapping("/myweb/createQR")
    public void createQR(HttpServletRequest request, HttpServletResponse response){
        System.out.println("二维码生成");
        String content = request.getParameter("content");
        System.out.println("内容："+content);

        try {
            QRCodeUtil.encode(content,response.getOutputStream());
            response.sendContentType("image/jpeg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("二维码生成完毕");
    }

}
