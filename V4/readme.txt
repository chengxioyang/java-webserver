重构代码
进行功能拆分，将解析请求的细节移动到HttpServletRequest类中，使得ClientHandler仅关注处理
一次HTTP交互的流程控制，而每一步的处理细节都交给其他类来完成。

实现:
1:新建一个包:com.webserver.http
  这个包中将来存放所有和HTTP协议相关的类
2:在http包中新建类:HttpServletRequest,请求对象
  该类的每一个实例用于表示客户端发送过来的一个请求
3:在HttpServletRequest中定义对应的属性(请求行，消息头，消息正文的相关信息)，并在构造
  方法中完成解析初始化的操作。
4:ClientHandler第一步解析请求仅需要改为实例化HttpServletRequest来完成解析请求。