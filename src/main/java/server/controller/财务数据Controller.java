package server.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import server.service.财务数据Service;

@Controller
public class 财务数据Controller extends 爸爸Controller{

	public 财务数据Controller() {

	}

	public static void main(String[] args) {

	}

	@Autowired
	财务数据Service 财务数据service;

	@RequestMapping(value = "caiwu_download", method = RequestMethod.POST)
	public void 取得财务导入数据(HttpServletResponse response) {
		//财务数据Service 财务数据service = new 财务数据Service();

		//
		String s文件全路径;

		s文件全路径 = 财务数据service.下载财务导入数据();

		// 将文件提供下载
		将指定文件提供下载_byHttpServletResponse_文件全路径(response, s文件全路径);

	}


	@RequestMapping(value = "CaiWu", method = RequestMethod.GET)
	public String 财务信息(Locale locale, Model model) {
		return "财务补全";

	}
}





