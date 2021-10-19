此版本重构HttpServletResponse的代码，将发送响应头的环节从固定的发送两个头改为可以
根据需要进行设置从而发送响应头。

实现:
1:在HttpServletResponse中定义一个属性:Map headers
  这个Map存放所有的响应头，key:响应头的名字  value:响应头的值

2:提供一个方法:putHeader,可以使得外界根据需要向响应对象中添加要发送的响应头

3:修改响应对象中发送响应头的方法:sendHeaders
  改为遍历headers这个Map，将所有要发送的响应头逐个发送

4:在DispatcherServlet的处理请求环节，设置要发送的响应头进行发送。
