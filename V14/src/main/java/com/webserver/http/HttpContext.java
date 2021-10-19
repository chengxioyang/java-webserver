package com.webserver.http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

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
//        cont.put("html","text/html");
//        cont.put("css","text/css");
//        cont.put("js","application/javascript");
//        cont.put("png","image/png");
//        cont.put("jpg","image/jpeg");
//        cont.put("gif","image/gif");
        /*
            通过解析config目录下的web.xml文件，将根标签下所有名为<mime-mapping>的
            子标签获取到，并将其中的子标签：
            <extension>中间的文本作为key
            <mime-type>中间的文本作为value
            存入mimeMapping中完成初始化
         */
        SAXReader reader = new SAXReader();

        try {
            Document doc = reader.read("./config/web.xml");
            Element root = doc.getRootElement();

            List<Element> emList = root.elements("mime-mapping");

            for (Element ext : emList){
                String econt = ext.elementText("extension");
                String evalue = ext.elementText("mime-type");
                cont.put(econt,evalue);
            }
            //System.out.println("daxiao:"+emList.size());

        } catch (DocumentException e) {
            e.printStackTrace();
        }


    }

    /**
     * 根据资源后缀名获取对应的Content-Type头信息
     * @param type
     * @return
     */
    public static String getCont(String type){
        return cont.get(type);
    }

    public static void main(String[] args) {

    }
}
