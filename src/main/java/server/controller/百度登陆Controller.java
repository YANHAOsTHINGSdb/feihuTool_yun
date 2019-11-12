package server.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import server.pcs.BaiduHttpService百度服务器登陆服务;
import server.pcs.bean.BaiduLoginRes百度登陆请求Bean;

@Controller
public class 百度登陆Controller extends HttpServlet{

	public 百度登陆Controller() {
		super();
	}

    private static final long serialVersionUID = 1L;


    @RequestMapping(value = "baiduLogin_request", method = RequestMethod.GET)
    protected String baiduLoginPage() {
    	return "百度BDUSS";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.doPost(request, response);
    }

//    @RequestMapping(value = "baiduLogin", method = RequestMethod.GET)
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//    {
//        String url = request.getParameter("url");
////        if(url != null){
////            if(!HttpUtil.returnResponseOfUrl(url, response.getOutputStream())){
////                if(!HttpUtil.returnResponseOfUrl(url, response.getOutputStream())){ // 如果出错，再试一次
////                    // log.error("url:" + url);
////                };
////            }else {
////            	response.setContentType("text/html; charset=UTF-8");//注意text/html，和application/html
////            	response.getWriter().print("<html><body><script type='text/javascript'>alert('无权下载文件！');</script></body></html>");
////            	response.getWriter().close();
////            }
////        }
//
//        url="https://passport.baidu.com/v2/api/?login";
//        HttpClient client = new HttpClient();
//        GetMethod get = new GetMethod(url);
//        // 本次连接是否自动处理重定向
//        get.setFollowRedirects(true);
//        client.executeMethod(get);
//        System.out.println(Arrays.toString(client.getState().getCookies()));
//    }

    @RequestMapping(value = "baiduLogin", method = RequestMethod.POST)
    @ResponseBody //将返回结果转成Json
    protected BaiduLoginRes百度登陆请求Bean doPostexecBaiduLogin(HttpServletResponse p, HttpServletRequest r){
    	// 新建 BaiduClientBean
    	// 设置 百度用户名
    	String username = r.getParameter("username");
    	// 设置 密码
    	String password = r.getParameter("password");
    	// 设置 图片验证码
    	String verifycode = r.getParameter("verifycode");
    	// 与 图片验证码 相对应的字符串
    	String vcodestr = r.getParameter("vcodestr");

    	// 调用 百度登陆service.百度登陆请求（username, password, verifycode, vcodestr）
    	return new BaiduHttpService百度服务器登陆服务(verifycode, vcodestr).登陆百度网盘_byUserName_passwd(username, password);

    	// return;
    }

    @RequestMapping(value = "sendcode", method = RequestMethod.POST)
    @ResponseBody //将返回结果转成Json
    protected Map sendcode(HttpServletResponse p, HttpServletRequest r){

    	String type = r.getParameter("type");
    	// 设置 密码
    	String token = r.getParameter("token");

		return new BaiduHttpService百度服务器登陆服务().申请发送验证码到手机或邮箱(type, token);

    }

    @RequestMapping(value = "verifylogin", method = RequestMethod.POST)
    @ResponseBody //将返回结果转成Json
    protected Map verifylogin(HttpServletResponse p, HttpServletRequest r){
    	// vcode, token, u, verifyType, "jsonp1"

    	String vcode = r.getParameter("vcode");
    	// 设置 密码
    	String token = r.getParameter("token");

    	String u = r.getParameter("u");

    	String verifyType = r.getParameter("type");

		return new BaiduHttpService百度服务器登陆服务().发送从手机或邮箱取到的验证码(vcode, token, u, verifyType);

    }
}
