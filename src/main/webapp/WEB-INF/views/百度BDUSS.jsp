<!DOCTYPE html>

<%@ page session="false" pageEncoding="UTF-8"%>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>百度BDUSS获取工具</title>
    <meta name="viewport" content="width=240, user-scalable=no, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0" />

    <link rel="shortcut icon" href="resources/baiduBDUSS/favicon.ico" type="image/x-icon" />

    <link rel="stylesheet" href="css/baiduBDUSS/bootstrap-3.3.7.min.css">
    <link rel="stylesheet" href="css/baiduBDUSS/main.css">
    <script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="js/baiduBDUSS/baidu_login.js" charset="utf-8" ></script>
    <script type="text/javascript" src="js/baiduBDUSS/bootstrap-3.3.7.min.js"></script>
	<script type="text/javascript" src="js/baiduBDUSS/clipboard-1.7.1.min.js"></script>
</head>

<body>
    <header class="page-header">
        <div class="baidu-header">
            <h1>百度BDUSS获取工具
                <!-- 模板输入版本号 -->
                <small>{{.}}</small>
            </h1>
            <br/>
            <br/>
            <a href="https://github.com/iikira/Baidu-Login" target="_blank">查看帮助</a>
        </div>
    </header>

    <div class="container">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">获取百度 BDUSS</h3>
            </div>
            <div class="panel-body">
                <form action="javascript:baidu_login()" id="login-form-wrapper" class="form-horizontal" role="form">
                    <!-- 用户 -->
                    <div class="form-group">
                        <label for="username" class="col-sm-1 control-label">百度用户</label>
                        <div class="col-sm-10">
                            <!-- 自动聚焦输入 -->
                            <input type="text" class="form-control" id="login-username" name="username" autofocus="autofocus" placeholder="手机号/邮箱/用户名">
                        </div>
                    </div>

                    <!-- 密码 -->
                    <div class="form-group">
                        <label for="password" class="col-sm-1 control-label">百度密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="login-password" name="password" placeholder="请输入密码">
                        </div>
                    </div>

                    <!-- 验证码 -->
                    <div class="form-group" id="login-verifyWrapper">
                        <label for="verifycode" class="col-sm-1 control-label">验证码</label>
                        <p class="col-sm-10">
                            <input type="text" class="form-control" id="login-verifycode" name="verifycode" placeholder="请输入验证码">

                            <span class="input-group-btn">
                                <img id="login-verifyCodeImg" class="img-thumbnail" src="" style="float: right; margin-left: 10px; margin-bottom: 10px;"
                                    onclick="refleshImg()">
                            </span>

                        </p>
                    </div>

                    <!-- 验证字串 -->
                    <input type="hidden" id="login-vcodestr" name="vcodestr" value="" />

                    <!-- 提交 -->
                    <div class="form-group">
                        <div class="col-sm-offset-1 col-sm-10">
                            <button type="submit" class="btn btn-info btn-block" id="login-submit">登录</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- 验证手机或邮箱 -->
        <div class="panel panel-info" id="login-verify" style="display:none;">
            <div class="panel-heading">
                <h3 class="panel-title">
                    需要验证手机或邮箱
                </h3>
            </div>

            <div class="panel-body">
                <p class="">你的账号需要验证手机或邮箱才能继续登录</p>
                <p>手机号码:
                    <span id="my-phone"></span>
                </p>
                <p>邮箱:
                    <span id="my-email"></span>
                </p>
                <p>请点击以下按钮，会向你的 手机／邮箱 发送验证码</p>
                <form>
                    <input type="button" id="phone-submit" class="btn btn-info" value="手机验证" style="width:49%;" onclick="sendType('mobile')">
                    <input type="button" id="email-submit" class="btn btn-info" value="邮箱验证" style="width:49%;" onclick="sendType('email')">
                </form>

                <!-- 提交手机/邮箱验证码 -->
                <form action="javascript:verify_login()" id="verifycode-wrapper" class="form-horizontal" role="form" style="display:none;">
                    <input type="hidden" name="type" id="verify-type">
                    <input type="hidden" name="token" id="token" />
                    <input type="hidden" name="u" id="u" />

                    <br/>
                    <p id="sended-status"></p>
                    <p id="code-error" style="color:red;"></p>
                    <!-- 手机/邮箱验证码 -->
                    <div class="input-group">
                        <span class="input-group-addon">验证码</span>
                        <input type="text" class="form-control" name="vcode" placeholder="手机/邮箱验证码">
                    </div>
                    <br/>
                    <!-- 提交 -->
                    <button type="submit" class="btn btn-info btn-block" id="code-submit">提交</button>

                </form>

            </div>
        </div>

        <!-- 成功 -->
        <div class="panel panel-succeed" id="succeed-div" style="display:none;">
            <div class="panel-heading">
                <h3 class="panel-title">登录成功</h3>
            </div>

            <div class="panel-body">
                <p id="code-msg"></p>

                <!-- USERNAME -->
                <div class="input-group">
                    <span class="input-group-addon">用户名: </span>
                    <input type="text" class="form-control" value="" id="USERNAME">
                    <span class="input-group-btn">
                        <button class="btn btn-default" data-clipboard-target="#USERNAME">
                            <img src="resources/baiduBDUSS/clippy.svg" alt="Copy to clipboard" width="13">
                        </button>
                    </span>

                </div>
                <br/>

                <!-- BDUSS -->
                <div class="input-group">
                    <span class="input-group-addon">BDUSS: </span>
                    <input type="text" class="form-control" value="" id="BDUSS">
                    <span class="input-group-btn">
                        <button class="btn btn-default" data-clipboard-target="#BDUSS">
                            <img src="resources/baiduBDUSS/clippy.svg" alt="Copy to clipboard" width="13">
                        </button>
                    </span>
                </div>
                <br/>

                <!-- PTOKEN -->
                <div class="input-group">
                    <span class="input-group-addon">PTOKEN: </span>
                    <input type="text" class="form-control" value="" id="PTOKEN">
                    <span class="input-group-btn">
                        <button class="btn btn-default" data-clipboard-target="#PTOKEN">
                            <img src="resources/baiduBDUSS/clippy.svg" alt="Copy to clipboard" width="13">
                        </button>
                    </span>
                </div>
                <br/>

                <!-- STOKEN -->
                <div class="input-group">
                    <span class="input-group-addon">STOKEN: </span>
                    <input type="text" class="form-control" value="" id="STOKEN">
                    <span class="input-group-btn">
                        <button class="btn btn-default" data-clipboard-target="#STOKEN">
                            <img src="resources/baiduBDUSS/clippy.svg" alt="Copy to clipboard" width="13">
                        </button>
                    </span>
                </div>
                <br/>

                <!-- COOKIE -->
                <div class="input-group">
                    <span class="input-group-addon">COOKIE数据: </span>
                    <input type="text" class="form-control" value="" id="COOKIE">
                    <span class="input-group-btn">
                        <button class="btn btn-default" data-clipboard-target="#COOKIE">
                            <img src="resources/baiduBDUSS/clippy.svg" alt="Copy to clipboard" width="13">
                        </button>
                    </span>
                </div>
                <br/>

            </div>
        </div>
    </div>

    <footer id="footer">Copyright © 2017 - 2018,
        <a href="https://github.com/iikira/Baidu-Login" target="_blank">iikira/Baidu-Login</a>.</footer>

    <script>
        new Clipboard('.btn');
    </script>
</body>

</html>