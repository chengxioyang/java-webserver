代码重构
功能拆分，将发送响应的细节工作从ClientHandler拆分到HttpServletResponse响应对象上。

实现:
1:在com.webserver.http包下新建类:HttpServletResponse响应对象
  该类的每一个实例用于表示一个HTTP的响应内容
2:在响应对象上定义方法response，用于将当前响应对象内容发送给客户端
  该方法中的逻辑就是ClientHandler中发送响应的细节
3:定义属性，将响应对象各部分信息都以属性形式表示(想法与请求对象一致。)
4:将ClientHandler中的代码修改，完成使用响应对象发送响应。

/*
                http://localhost:8088/myweb/index.html
                path="/myweb/index.html"

                http://localhost:8088/myweb/classTable.html
                path="/myweb/classTable.html"


                http://localhost:8088/
                path="/"
                下面执行报错:
                java.io.FileNotFoundException: .\webapps (拒绝访问。)
                原因:抽象路径是"\"等同于定位的是webapps目录，目录是不能被文件流读取的


                http://localhost:8088/myweb/classTable123.html
                path="/myweb/classTable123.html"
                下面执行报错:
                java.io.FileNotFoundException: .\webapps\myweb\classTable123.html (系统找不到指定的文件。)
                原因:webapps/myweb/下没有classTable123.html这个文件
                    说明浏览器地址栏上写路径的时候输入有错误的地方
             */