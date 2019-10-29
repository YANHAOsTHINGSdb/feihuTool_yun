package server.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import server.service.除权数据Service;

@Controller
public class 除权数据Controller  extends 爸爸Controller{

	public 除权数据Controller() {

	}

	public static void main(String[] args) {

	}

	@Autowired
	除权数据Service 除权数据service;

	@RequestMapping(value = "chuquan_download", method = RequestMethod.POST)
	public void 取得除权导入数据(HttpServletResponse response) {
		// 除权数据Service 除权数据service = new 除权数据Service();

		// 
		String s文件全路径;

		s文件全路径 = 除权数据service.下载除权导入数据();

		// 将文件提供下载
		// SpringMVC实现文件下载的两种方法 https://blog.csdn.net/xiongyouqiang/article/details/80488202
		将指定文件提供下载_byHttpServletResponse_文件全路径(response, s文件全路径);

	}

	@RequestMapping(value = "ChuQuan", method = RequestMethod.GET)
	public String 除权信息(Locale locale, Model model) {
		return "除权补全";

	}
}
