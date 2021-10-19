package com.webserver.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 该类定义所有和HTTP协议有关可被复用的信息
 */
public class HttpContext {
    /**
     * 回车符
     */
    public static final char CR = 13;
    /**
     * 换行符
     */
    public static final char LF = 10;
    /**
     * 存放所有资源的后缀与Content-Type头的对应关系
     * key：资源后缀名
     * value：Content-Type头对应的值
     */

    private static Map<String,String> cont = new HashMap<>();

    static {
        //初始化cont
        initCont();
    }

    private static void initCont(){
        cont.put("html","text/html");
        cont.put("css","text/css");
        cont.put("js","application/javascript");
        cont.put("png","image/png");
        cont.put("jpg","image/jpeg");
        cont.put("gif","image/gif");
    }

    /**
     * 根据资源后缀名获取对应的Content-Type头信息
     * @param type
     * @return
     */
    public static String getCont(String type){
        return cont.get(type);
    }
}
