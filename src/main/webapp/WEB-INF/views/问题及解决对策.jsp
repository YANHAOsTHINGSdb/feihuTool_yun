
<!DOCTYPE html SYSTEM "about:legacy-compat">

<%@ page session="false" pageEncoding="UTF-8"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">

<title>问题及解决对策</title>

</head>
<body>

项目日记 2019/10/18 【问题集合】

<div>
问：这些组件怎么搭在一起工作的
<br>
答：首先要有一个工程，把组件放进工程，就可以一起工作了。
</div>
<br>
<div>
问：什么叫组件啊
<br>
答：就是别人或者你自己写的程序，通常以扩展名为jar的文件形式存在。
</div>
<br>
<div>
问：能再具体说一下吗
<br>
答：首先你要确定是个什么工程。如果是web。那么所有资源要在web server主导下工作
<br><p>1、web server 的工作方式，要看web .xml如何配置的
<br>	web .xml 主要记录【springMVC】、【拦截器】、【servlet 】的配置文件地址
<br>		例、springMVC信息会在 applicationContext.xml 中配置
</p>
<p>2、springMVC 主要控制【组件预加载】、【数据库链接池】
<br>	【组件预加载】 多为固定设置（全加载）参照：https://www.cnblogs.com/afeng2010/p/10133797.html
<br>      	【数据库链接池】主要是【持久层组件】和【数据库连接信息】
<br>		例、持久层组件会在 persistence.xml中配置
<br>			数据库连接信息会在 application.properties 中配置</p>
<p>3、拦截器 多为固定设置。抄袭即可</p>
<p>4、servlet 主要控制网页信息
<br>		例、servlet信息会在 servlet-context.xml 中配置</p>

</div>
<br>
<div>
问：web.xml 是怎么来的
<br>答：上网搜的，然后自己校对。
</div>
<br>
<div>
问：web.xml 里面写的是代码吗
<br>答：算是。有专门的程序负责解析web.xml 。如果有写，就处理。如果没写，就不处理。
	但你得配置正确。整个xml都像填表，之后程序负责处理。
	这也是人与程序的对话。
</div>
<br>
<div>
问：上面提到的组件要一一下载吗
<br>答：不需要。eclipse里大部分已经集成，个别组件通过maven自动配置即可。
</div>
<br><div>
问：web.xml 里面为什么要走写其他文件的地址
<br>答：因为，整个配置信息会很大。为了便于读写，把他们分到不同文件的。
</div>
<br>

<div>
问：maven是什么
<br>答：自动把pom.xml 中记载的jar配置到工程里。
</div>
<br><div>
问：Spring JPA Config IllegalArgumentException: No persistence unit with name found
<br>答：
	如果找不到persistenceUnitName
	说明你没有指明文件persistence.xml的具体位置。参照applicationContext.xml
	参照：
	https://stackoverflow.com/questions/29223359/spring-jpa-config-illegalargumentexception-no-persistence-unit-with-name-found
</div>
<br><div>
问：怎么才能看出是tomcat项目
</div>
<br>
<div>
问：怎么才能确认是maven项目
</div>
<br>
<div>
问：怎么才能确认是jsp网页
</div>
<br>
<div>
问：怎么才能确认是spring项目
</div>
<br>
<div>
问：怎么才能确认是postgresql数据库
</div>
<br>
<div>
问：怎么才能确认是jpa
</div>
<br>
<div>
问：怎么才能确认是hibernate
</div>
<br>
<div>
问：怎么才能确认是jdbc
</div>
<br>
<div>
问：严重: Error configuring application listener of class [org.springframework.web.context.ContextLoaderListener]
监听器 [org.springframework.web.context.ContextLoaderListener]出现问题。
<br>
答：maven没有出现在class path中
<br>	参考：https://blog.csdn.net/hunhun1122/article/details/80027352
</div>
<br>
<div>
问：This application has no explicit mapping for /error, so you are seeing this as a fallback.
              或 No Spring WebApplicationInitializer types detected on classpat
<br>
答：没有放log4j.xml
</div>
<br>
<div>
问：persistence.xml该放在哪
<br>答：
	persistence.xml没有放在resourse/META-INF/下
</div>

<br>
<div>
问：org.springframework.beans.factory.CannotLoadBeanClassException: Cannot find class [org.springframework.http.converter.json.MappingJacksonHttpMessageConverter]
<br>
答：spring3升级到spring4后的眩晕感觉
	https://www.cnblogs.com/shihaiming/p/6702572.html
</div>
<br>
<div>
问：missing artifact org.springframework:spring-aop:jar:4.2.4.RELEASE
<br>答：多半是maven消化不了导致的。先确定pom.xml是否有其配置信息。再update一次maven看看
</div>
<br>
<div>
问：需要的jar上哪去找
<br>答：上网查
	例、要找  spring-aop:jar 这个包。直接百度： org.springframework:spring-aop:jar maven 即可。
</div>
<br>
<div>
问：org.hibernate.hql.internal.ast.QuerySyntaxException: 除权信息 is not mapped
<br>答：只要将【entity名】替代【表名】即可。
<br>
<br>        例、　@Entity						// 表明该类 (UserEntity) 为一个实体类
<br>		   @Table(name = "除权信息")		// @Table 当实体类与其映射的数据库表名不同名时需要使用
<br>		   public class 除权Entity extends AbstractEntity implements IEntity {
<br>
<br>		修改前
<br>			-----------------------------------------------------------------------------
<br>			SELECT u FROM 除权信息 u WHERE 。。。（以下省略）
<br>
<br>		修改后
<br>			-----------------------------------------------------------------------------
<br>			SELECT u FROM 除权Entity u WHERE 。。。（以下省略）
<br>
<br>	参考
<br>		SSH整合报错：org.hibernate.hql.internal.ast.QuerySyntaxException: User is not mapped[......]
<br>			https://www.cnblogs.com/zhangliang88/p/5476340.html
</div>
<br>
<div>
问：javax.persistence.TransactionRequiredException: No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call
<br>
答：在对应的@Service或组件上添加@Transactional即可。
<br>
<br>        例、　修改前
<br>			-----------------------------------------------------------------------------
<br>			@Service
<br>			public class 除权数据Service  extends 爸爸数据Service{
<br>				@Transactional
<br>				private void 存储除权数据_by除权数据(List<String> 除权数据list) {
<br>			-----------------------------------------------------------------------------
<br>		修改后
<br>			-----------------------------------------------------------------------------
<br>			@Service
<br>			@Transactional
<br>			public class 除权数据Service  extends 爸爸数据Service{
<br>			-----------------------------------------------------------------------------
<br>	参考
<br>		Spring mvc 配置事务的注意点	https://blog.csdn.net/zxcvqwer19900720/article/details/20999623
<br>		spring + jpa 配置问题，No transactional EntityManager available
<br>									https://www.oschina.net/question/1788122_167278
<br>
<br>		JPA entityManagerFactory配置详解
<br>									https://blog.csdn.net/weixin_30376453/article/details/97070574
<br>		SPRING+JPA+Hibernate配置方法 https://www.cnblogs.com/stronghan/p/5559105.html
</div>
<br>
<div>
问：ERROR: org.springframework.web.context.ContextLoader - Context initialization failed
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in ServletContext resource [/WEB-INF/spring/applicationContext.xml]: Invocation of init method failed; nested exception is javax.persistence.PersistenceException: [PersistenceUnit: springJpa] Unable to build Hibernate SessionFactory
<br>
答：
<br>	修改前
<br>		-----------------------------------------------------------------------------
<br>		<prop key="hibernate.hbm2ddl.auto">update</prop>
<br>
<br>	参考
<br>
<br>		https://blog.csdn.net/u011686226/article/details/52020590
</div>
<br>
<div>
问：プロジェクト・ファセットのバージョンを 動的 Web モジュール から 3.1 に変更できません。
<br>答：	将下面的配置保持一致。具体参见参考
<br>	org.eclipse.wst.common.project.facet.core.xml
<br>	web.xml
<br>	pom.xml
<br>
<br>	参考（mac系统需要将隐藏文件显示出来）
<br>		将Maven项目的Dynamic Web Module 改为3.1版本
<br>		https://blog.csdn.net/qq_22771739/article/details/80493159
</div>
<br>
<div>
问：java.lang.NoClassDefFoundError: com/sun/jna/Library
<br>答：在pom.xml中加入
<br>	-----------------------------------
<br>		<dependency>
<br>			<groupId>com.sun.jna</groupId>
<br>			<artifactId>jna</artifactId>
<br>			<version>4.2.1</version>
<br>		</dependency>
<br>	-----------------------------------
<br>	由于此时maven没有最新版的jna包，所以要手动添加
</div>
<br>
<div>
问：java.lang.NoClassDefFoundError: ConvertTool/impl/StockData除权ConvertTool
<br>答：项目FeihuData_Convert没有被加到环境变量
<br>	解决：
<br>	project-->property-->depolyment-->add-->FeihuData_Convert
</div>
<br>
<div>
问：Exception in thread "main" java.lang.UnsatisfiedLinkError: Unable to load library
 at com.sun.jna.NativeLibrary.loadLibrary(NativeLibrary.java:277)
<br>
<br>       错误：严重: Servlet.service() for servlet [appServlet] in context with path [/feihuTool] threw exception [Handler dispatch failed; nested exception is java.lang.UnsatisfiedLinkError: Unable to load library 'TdxHqApi20991230.dll': ÕҲ»µ½ָ¶¨] with root cause
<br>
<br>
<br>答： 1、java.lang.UnsatisfiedLinkError: Unable to load library ‘xxx’: Native library (win32-x86-64/ID_Fpr.dll),必须使用 32 位的 Jvm 才行。
<br>	2、修改：
<br> 		修改前：Native.loadLibrary("TdxHqApi20991230.dll");
<br> 		修改后：Native.loadLibrary( 这里放全路径...);
<br>
<br>	3、总结：
<br>		1)、将问题描述清楚也是门功夫
<br>	 	（否则百度不到正确的帖子
<br>		2)、尝试不同的思考方向
<br>		3)、解决问题的过程就是学习的过程
<br>		4)、冲破这个阻力位，实力就会再涨一个楼梯凳
</div>
<br>
<div>
问：百度网盘 调用出现{"error_code":4,"error_msg":"No permission to do this operation","request_id":7015566025241457221}
<br>答：
<br>
<br>	很多概念性问题没有解决
<br>	1、百度要对程序进行授权（不是所有程序都可以使用百度网盘，要事先登记）
<br>		登记后取到以下信息： CLIENTID、CLIENTSECRET、REDIRECTURI
<br>		参考
<br>			如何使用百度云API接口  https://zhidao.baidu.com/question/1370882080002591139.html
<br>			百度OAuth                        http://developer.baidu.com/wiki/index.php?title=docs/oauth
<br>		坑：关于REDIRECTURI，开发阶段填oob即可。
<br>
<br>        2.1、通过1、取到的信息，可以申请到【授权码】
<br>		参考
<br>			开发相关资源 https://openapi.baidu.com/wiki/index.php?title=docs/oauth/showcase
<br>
<br>        2.1.1、通过2、的【授权码】再加上用户自己的信息，就可以得到【Access Token】
<br>		参考
<br>			使用Implicit Grant方式获取Access Token
<br>						https://openapi.baidu.com/wiki/index.php?title=%E4%BD%BF%E7%94%A8Implicit_Grant%E6%96%B9%E5%BC%8F%E8%8E%B7%E5%8F%96Access_Token
<br>
<br>	2.1.1.1、有了【Access Token】程序就可以进出(该用户的)百度网盘了
<br>
<br>	2.2 也可以通过网页工具直接取得 【Access Token】
<br>
<br>		参考
<br>			利用百度云盘API上传文件至百度云盘    https://yq.aliyun.com/articles/316201
<br>
<br>       2、开通PCS API的权限
<br>		参考
<br>			百度——个人云存储pcs——Android使用百度云盘(1)——获取access token
<br>                        https://www.xuebuyuan.com/3254993.html
</div>
<br>
<div>
问：百度网盘 关闭PCS API的权限
<br>答：用UA（在互联网上抓取数据的时候，经常需要程序伪装成浏览器来避开服务端的一些限制，这是收集的一些浏览器的User-Agent
       NetdiskUA = "netdisk;2.2.51.6;netdisk;10.0.63;PC;android-android"
<br>
<br>	原理：
<br>			利用UA方式
<br>			提供 appid和百度BDUSS、模拟网盘操作
<br>	模拟网站
<br> 		1、可以查看
<br>		2、可以下载
<br>
<br>
<br>	使用百度 BDUSS 来登录百度帐号 ：我们可以通过 百度BDUSS, 来实现模拟登录百度帐号。成功获取百度BDUSS，最好把它保存在电脑硬盘或网盘里面，方便以后调用。
<br>	https://github.com/iikira/BaiduPCS-Go/wiki/%E5%85%B3%E4%BA%8E-%E8%8E%B7%E5%8F%96%E7%99%BE%E5%BA%A6-BDUSS
<br>
<br>
<br>	BaiduPCS-Go出现403 Forbidden错误解决方法
<br>	https://blog.csdn.net/a564126786/article/details/85948412
</div>
<br>
<div>
问：要下载的文件被直接推到网上乱码显示、而没有被提示下载保存
<br>答：缺少设置响应头和客户端保存文件名
<br>	   -------------------------------------------------------------------
<br>	    response.setCharacterEncoding("utf-8");
<br>	    response.setContentType("multipart/form-data");
<br>	    response.setHeader("Content-Disposition", "attachment;fileName=" + s文件全路径);
<br>           -------------------------------------------------------------------
<br>	参考 Java Web实现文件下载和乱码处理方法  https://www.jb51.net/article/95153.htm
</div>
</body>
</html>