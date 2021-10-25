package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import qrcode.QRCodeUtil;

import java.io.FileOutputStream;

public class ToolsController {
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
