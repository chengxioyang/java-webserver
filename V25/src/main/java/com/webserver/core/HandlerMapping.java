package com.webserver.core;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * 保存所有请求与对应的业务处理关系
 */
public class HandlerMapping {
    /*
        key:请求路径
        value:处理该请求的业务类及对应方法

        例如：
        key:/myweb/reg
        value:MethodMapping{
            controller:UserController
            method:reg()
        }
     */
    private static Map<String, MethodMapping> mapping = new HashMap<>();


    static {
        try {
            initMapping();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static void initMapping() throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException {
        /*
            扫描com.webserver.controller包中的所有类，并将所有被注解@Controller
            的类实例化，然后将@RequestMapping标注的方法获取，并将这两项以一个
            MethodMapping表示，最终将@RequestMapping注解传递的参数(请求路径)作为
            key，将这个MethodMapping作为value保存到mapping这个map

            mapping初始化后的大致样子:
                    key                         value
            "/myweb/reg"              MethodMapping UserController reg()
            "/myweb/login"            MethodMapping UserController login()
            "/myweb/showAllUser"      MethodMapping
            "/myweb/writeArticle"     MethodMapping ArtcileController write()
            "/myweb/showAllArticle"   MethodMapping
            "/myweb/createQR"         MethodMapping
         */


        File dir = new File(HandlerMapping.class.getClassLoader().getResource("./com/webserver/controller").toURI());
        File[] subs = dir.listFiles(f -> f.getName().endsWith(".class"));
        for (File s : subs) {
            String className = s.getName().substring(0, s.getName().lastIndexOf("."));
            Class cls = Class.forName("com.webserver.controller." + className);
            //判断该类是否被注解@Controller标注了
            if (cls.isAnnotationPresent(Controller.class)) {
                Object controller = cls.newInstance();
                //获取其定义的所有方法
                Method[] methods = cls.getDeclaredMethods();
                for (Method method : methods) {
                    //该方法是否被注解@RequestMapping标注
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        //通过注解获取该方法对应的请求路径(从注解上的参数获取)
                        RequestMapping rs = method.getAnnotation(RequestMapping.class);
                        String key = rs.value();
                        MethodMapping value = new MethodMapping(controller, method);
                        mapping.put(key, value);
                    }
                }
            }
        }
        System.out.println(mapping.size());
    }

    public static void main(String[] args) {

    }

    /**
     * 根据请求路径获取对应的业务处理类
     * @param path
     * @return
     */
    public static MethodMapping getMethod(String path) {
        return mapping.get(path);
    }


    public static class MethodMapping {
        private Object controller;//某个Controller实例
        private Method method;//对应的业务处理方法

        public MethodMapping(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }

    }


}
