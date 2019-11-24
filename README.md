# feihuTool_yun
某云上用的飞狐工具web（由于预算原因、采用网盘机制、拒绝使用数据库）

用于发布在某云上的web

需求出处：
相信你不需要回公司、回家、只要用手机、无论何时何地都可以执行指定程序
是现代互联网的玩法。但这一直是公司级别的应用、个人很少有能力这么玩的。
----这个例子就是简单实现这个玩法的。

问题所在：
由于在云上，无论是数据库还是存储空间都是按容量和时间计算费用的。
这虽然比自己搭建服务器成本低、
----但还是所需不少（买电脑、网费、电费、安装、维护、域名租用费）。

解决方案：
因此、采用最小消耗、只在云容器上放置最小web程序、将持久层（文件存储）放到网盘。
----即使如此，也是每天0.5元左右的消耗。T_T


接下来是一些问题的对策

问题1、コンテナー 'Maven 依存関係' が存在しないライブラリー '..\jna-4.2.1.jar' を参照しています	
答：把jna-4.2.1.jar这个包放在下面的文件夹下
    C:\Users\(你的用户名)\.m2\repository\com\sun\jna\jna\4.2.1

    jna-4.2.1.jar 在 (你的eclipse工程路径)\FeihuTool_Convert\jar

问题2、java.lang.NumberFormatException: null
	java.lang.Integer.parseInt(Integer.java:542)
	java.lang.Integer.parseInt(Integer.java:615)
	ConvertTool.impl.PROPERTY.取得Port(PROPERTY.java:88)
答：请在此处放置DLL文件:C:\pleiades\eclipse\.
    请在此处放置property文件:C:\pleiades\eclipse\.