package com.webserver.test;

import qrcode.QRCodeUtil;

import java.io.FileOutputStream;

public class TestQRCode {
    public static void main(String[] args) throws Exception {
        String line = "https://www.vmall.com/";
        FileOutputStream fos = new FileOutputStream("./qr.jpg");
        QRCodeUtil.encode(line,fos);
        System.out.println("生成完毕");
    }
}
