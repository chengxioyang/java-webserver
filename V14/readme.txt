重构代码
将HttpContext中初始化mimeMapping的操作由原来的固定存放6个值改为通过解析config/web.xml
文件初始化

web.xml是Tomcat提供的一个文件位置在Tomcat安装目录下的config目录中
该文件将所有的资源后缀与对应的Content-Type值定义在其中，一共1011个

实现：
1：在当前项目目录下新建目录config
2：将Tomcat提供的web.xml复制到config目录下
3：修改HttpContext的初始化mimeMapping方法initMimeMapping()
最终初始化后mimeMapping这个Map有1011个元素，至此我们的服务端应该支持了所有资源后缀对应的
类型了，响应任何资源的类型都没有问题











