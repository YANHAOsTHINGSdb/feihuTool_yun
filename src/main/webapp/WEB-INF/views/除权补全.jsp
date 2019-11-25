<!DOCTYPE html SYSTEM "about:legacy-compat">
<!-- saved from url=(0041)https://tomcat.apache.org/download-90.cgi -->
<%@ page session="false" pageEncoding="UTF-8"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="./resources/tomcat.css" rel="stylesheet" type="text/css">
<link href="./resources/fonts.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="js/jquery-3.3.1.js"></script>
<script type="text/javascript" src="js/jquery.json.js"></script>

<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="js/jquery.cookie.js"></script>

<script type="text/javascript" src="js/tabulator.min.js"></script>
<script type="text/javascript" src="js/tabulator.js"></script>
<script type="module" src="js/js.cookie.js"></script>
<title>飞狐数据</title>

</head>
<body>
<script type="text/javascript">
	// JavaScript 页面自动执行（加载）js的几种方法
	// https://blog.csdn.net/wzp6010625/article/details/53171604

	// 启动的时候就会初始化该函数
	$(function() {
		//===================================================================
		// 【读取cookie】				应该是js的工作				读取本地cookie文件
		//===================================================================
		//  参照 https://github.com/js-cookie/js-cookie
		var bduss = Cookies.get('bduss');
		var name = Cookies.get('name');
		var ptoken = Cookies.get('ptoken');
		var stoken = Cookies.get('stoken');
		var workdir = Cookies.get('workdir');
		var uid = Cookies.get('uid');
		var nameshow = Cookies.get('nameshow');

/* 		bduss = $.cookie('bduss');
		name = $.cookie('name');
		ptoken = $.cookie('ptoken');
		stoken = $.cookie('stoken');
		workdir = $.cookie('workdir');
		uid = $.cookie('uid');
		nameshow = $.cookie('nameshow'); */
		if(bduss===undefined){
			// 1、如果没有取到bduss信息
			//    就进行显示百度登陆画面
			$("#baidu_login").css("display", "");//显示
			//$("#baidu_login").css("display", "none");//非显示
		} else{
			$("#baidu_login_finished").css("display", "");//显示
			//$("#baidu_login_finished").css("display", "none");//非显示
		}
	});
</script>
	<script>
		$(function() {
			$("#chuquan_download_btn").click(function() {
			    $("#fbean").attr("action","chuquan_download");
			    $("#fbean").submit();
			});
		});

		<%-- 追加按钮 按下--%>
		function click_BaiduLogin(){

			// 打开子画面
			$.ajax({
				// 使用Httpclient来替代客户端的jsonp跨域解决方案 https://www.cnblogs.com/digdeep/p/4198643.html
				// http://www.baidu.com/cache/user/html/login-1.2.html
				// https://passport.baidu.com/v2/?login&Type=1&tpl=netdisk
				// https://wappass.baidu.com/cgi-bin/genimage?tcG4a345670d4ed560a024d1544a901cc13239f973474012d7b
				//url		: "http://localhost:8080/feihuTool/baiduLogin",
				url     : "/feihuTool/baiduLogin",
				// serialize()=输出序列化表单值的结果、参照https://www.w3school.com.cn/jquery/ajax_serialize.asp
				type    :'get',
				dataType:'html',
				success : function(data){
						// 显示子画面：将取到的画面html值放入subPage中
						$('#subPage').html(data);
				}, // success完结
				complete:function(){
					isSubmitted = false;
				}
			});
		}
		// 取得Cookie的值
		function getCookie(c_name){
			if (document.cookie.length>0){//判断cookie是否存在
				//获取cookie名称加=的索引值
				var c_start = document.cookie.indexOf(c_name + "=");
				if (c_start!=-1){ //说明这个cookie存在
					//获取cookie名称对应值的开始索引值
					c_start=c_start + c_name.length+1
					//从c_start位置开始找第一个分号的索引值，也就是cookie名称对应值的结束索引值
					c_end=document.cookie.indexOf(";",c_start)  
					//如果找不到，说明是cookie名称对应值的结束索引值就是cookie的长度
					if (c_end==-1) c_end=document.cookie.length
					//unescape() 函数可对通过 escape() 编码的字符串进行解码
					//获取cookie名称对应的值，并返回
					return unescape(document.cookie.substring(c_start,c_end))
				}
			}
			return "" //不存在返回空字符串
		}

		//
		function getBDUSS() {
			var iframe = document.getElementById("iframe_baidu");

			//var str ="<input type='text' value=' " +getCookie('BDUSS')+"'/>";
			//document.getElementById("BDUSS").innerHTML=str;
		}
	</script>
	<div id="wrapper">
		<header id="header">
			<jsp:include page="toolBar.jsp"/>
		</header>
		<main id="middle">
		<div>
			<div id="mainLeft">
					<div id="nav-wrapper">
						<nav>
						<div>
							<h2>行情补全</h2>
							<ul>
								<li><a href="HangQing">飞狐导入行情数据下载</a>
								</li>
							</ul>
						</div>
						<div>
							<h2>除权补全</h2>
							<ul>
								<li><a href="ChuQuan">飞狐导入除权数据下载</a>
								</li>
							</ul>
						</div>
						<div>
							<h2>财务补全</h2>
							<ul>
								<li><a href="CaiWu">飞狐导入财务数据下载</a>
								</li>
							</ul>
						</div>

					</nav>
				</div>
			</div>
			<div id="mainRight">
				<div id="content">
					<h2 style="display: none;">Content</h2>
					<h3 id="Tomcat_9_Software_Downloads">除权补全</h3>
					<div>
						<!--<button type="button" onclick="click_BaiduLogin()">登陆百度网盘</button>-->
						<!-- 如何将一个网页的一部分嵌入到自己的网页中 https://zhidao.baidu.com/question/1051062419381258939.html -->
						<!-- https://passport.baidu.com/v2/?reg&u=https%3A%2F%2Fpan.baidu.com&#174;Type=1&tpl=netdisk -->
						<!--<iframe id="iframe_baidu" src="/feihuTool/baiduLogin_request" width="580px"  height="580px"></iframe>
						<!--<br>-->
						<!--BDUSS = <div id="BDUSS">-->
						<!--<br>-->
						<!--<input type="button" id="取得BDUSS" value="取得BDUSS" onClick="getBDUSS();">-->
					</div>
					<!-- 这里是子画面-->
					<div id="baidu_login" style="display:none;">
						<jsp:include page="百度BDUSS.jsp" />
					</div>
					<!-- 成功 -->
					<div id="baidu_login_finished"  class="panel panel-succeed" style="display:none;">
			            <div class="panel-heading">
			                <h3 class="panel-title">百度网盘登录成功</h3>
			            </div>
					</div>
					</div>
					<div class="text">
						<p>处理流程：先从通达信券商服务器下载最后交易日的全部有效证券代码、
							再根据代码下载每只股票的历史除权数据，之后将下载数据出力到文件和数据库进行备份 最后，最后将数据做成飞狐导入用下载文件。</p>
						<input type="button" id="chuquan_download_btn" value="飞狐导入除权数据下载">
					</div>

			</div>
		</div>

		<form id ="fbean" name="fbean" method="post">
		</form>
		</main>
		<footer id="footer">
			所有权 © 2017-2019, 颜老师工作室 <br>
		</footer>
	</div>
</body>
</html>