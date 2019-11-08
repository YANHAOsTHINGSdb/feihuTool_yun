package server.service;

import server.pcs.BaiduHttpService百度服务器登陆服务;
import server.pcs.bean.BaiduLoginRes百度登陆请求Bean;

public class 百度登陆Service {

	public 百度登陆Service() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public BaiduLoginRes百度登陆请求Bean 百度登陆请求(String username, String password, String verifycode, String vcodestr) {

		// RSA加密(密码肯定是经过了RSA加密)


		// 准备post


		// 准备header

		// 连接百度服务器
		return new BaiduHttpService百度服务器登陆服务().登陆百度网盘_byUserName_passwd(username, password);

		// 解析返回信息（ LoginJSON 从百度服务器解析的数据结构 ）

		// 返回解析结果（ LoginJSON 从百度服务器解析的数据结构 ）

	}

}
